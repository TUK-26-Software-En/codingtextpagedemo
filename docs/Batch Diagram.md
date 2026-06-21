## 배치 다이어그램 명세서

본 명세서는 코딩 테스트 플랫폼의 배치(주기/이벤트성) 처리 흐름을 정리한 문서이다. **Phase 4에서 구현되었다.** `batch` 패키지가 `@Scheduled` 주기 배치와 수동 트리거(`POST /api/batch/aggregate`)로 종료된 대회/시험을 마감·집계한다. 마감 멱등성을 위해 `Contest.closed`/`Exam.closed` 플래그를 둔다.

## 1. 배치 대상 정리

| 배치 | 트리거 | 입력 | 출력(갱신) |
|------|--------|------|-----------|
| 대회 마감·랭킹 집계 | `Contest.endTime` 도달 | 해당 대회의 Submission/UserContest | `UserContest.totalScore`, `solvedCount`, `rank` |
| 시험 마감·합격 판정 | `Exam.endTime` 도달 | 해당 시험의 Submission/UserExam | `UserExam.totalScore`, `passStatus`, `examGrade` |

## 2. 대회 마감·랭킹 집계 흐름

```mermaid
flowchart TB
  start([스케줄러 주기 실행]) --> find{"endTime 경과 &\n미마감 Contest 존재?"}
  find -- 아니오 --> done([대기])
  find -- 예 --> lock[대회 마감 상태 전환]
  lock --> loadP[참가자(UserContest) 로드]
  loadP --> calc["참가자별 점수/해결 수 집계\n(Submission 기준)"]
  calc --> rank[점수 기준 정렬 → rank 부여]
  rank --> save[(UserContest 일괄 갱신)]
  save --> done
```

집계 규칙(설계):
- `solvedCount` = 해당 대회 문제 중 `AC` 받은 문제 수
- `totalScore` = 문제별 배점(`ContestProblem.contestProblemScore`) 합
- `rank` = `totalScore` 내림차순(동점 시 마지막 제출 시각 등 보조 기준)

## 3. 시험 마감·합격 판정 흐름

```mermaid
flowchart TB
  start([스케줄러 주기 실행]) --> find{"endTime 경과 &\n미마감 Exam 존재?"}
  find -- 아니오 --> done([대기])
  find -- 예 --> lock[시험 마감 상태 전환]
  lock --> loadP[응시자(UserExam) 로드]
  loadP --> calc["응시자별 totalScore 집계"]
  calc --> judge{"합격 기준 충족?"}
  judge -- 예 --> pass[passStatus=true, examGrade 부여]
  judge -- 아니오 --> fail[passStatus=false]
  pass --> save[(UserExam 일괄 갱신)]
  fail --> save
  save --> done
```

## 4. 구현 (Phase 4)

| 구성 요소 | 내용 |
|-----------|------|
| `BatchConfig` | `@EnableScheduling` |
| `BatchScheduler` | `@Scheduled(fixedDelay)` 주기 실행 (`batch.aggregate-interval-ms`, 기본 60s) |
| `ContestAggregationService` | `findByEndTimeBeforeAndClosedFalse` → 집계 → `close()` |
| `ExamAggregationService` | 동일 + 합격(≥만점60%)·등급(A/B/C/D/F) |
| `BatchController` | `POST /api/batch/aggregate` 수동 트리거(인증 필요) |

- 멱등성: `closed` 플래그로 재실행 시 중복 집계 방지(검증: 재트리거 시 0건)
- 트랜잭션: 집계 메서드 `@Transactional` (엔티티 변경 더티체킹 반영)
- AC 판정: `SubmissionRepository.existsBy...SubmittedAtBetween(userId, problemId, AC, start, end)` — 대회/시험 기간 내 AC

> 후속: 다중 인스턴스 시 분산 락, 부분 점수, Submission-대회 직접 연결, Spring Batch 전환.
