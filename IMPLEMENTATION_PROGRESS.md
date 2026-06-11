# Coding Test Platform - 구현 진행 상황

## 프로젝트 개요
Spring Boot 기반 온라인 코딩 테스트 플랫폼 백엔드

---

## 변경 이력

### [2026-06-11] Submission - submitted_code 필드 추가

**변경 배경**
유저가 문제를 풀어서 제출할 때 실제로 작성한 코드가 저장되어야 함.
기존에는 언어·결과만 저장하고 코드 본문이 없었음.

**변경 파일**
| 파일 | 변경 내용 |
|------|-----------|
| `submission/entity/Submission.java` | `submittedCode TEXT` 필드 추가 |
| `submission/dto/request/SubmissionCreateRequest.java` | `submittedCode` 요청 필드 추가 |
| `submission/service/SubmissionService.java` | Submission 빌더에 `submittedCode` 전달 |
| `submission/dto/response/SubmissionResponse.java` | `submittedCode` 응답 필드 추가 + `from()` 반영 |

**제출 흐름 (현재)**
```
POST /api/submissions
  userId, problemId, submissionLanguage, submittedCode
       ↓
  User, Problem 엔티티 조회
       ↓
  FakeJudgeService.judge() → JudgeResult(AC, 100)
       ↓
  Submission 생성 (코드 + 결과 저장)
       ↓
  UserProblem 갱신 (신규 생성 or 상태 업데이트)
       ↓
  SubmissionResponse 반환
```

---

## 구현 현황

### ✅ 완료

#### 설정 / 인프라
- [x] `build.gradle` — springdoc-openapi 의존성 추가
- [x] `application.properties` — H2, JPA, Swagger 설정
- [x] `SwedemoApplication` — @EnableJpaAuditing 분리
- [x] `global/config/JpaConfig` — @EnableJpaAuditing
- [x] `global/config/SecurityConfig` — 전체 허용 (개발용)
- [x] `global/config/SwaggerConfig` — OpenAPI 정보 설정

#### 공통 인프라
- [x] `global/common/BaseEntity` — createdAt, updatedAt 자동 관리
- [x] `global/common/ApiResponse<T>` — 표준 응답 형식
- [x] `global/exception/ErrorCode` — 전체 에러 코드 enum
- [x] `global/exception/BusinessException` — 비즈니스 예외
- [x] `global/exception/GlobalExceptionHandler` — 전역 예외 처리

#### Enum
- [x] `global/common/enums/Rank` — BRONZE~RUBY
- [x] `global/common/enums/Language` — C, CPP, JAVA, PYTHON, JAVASCRIPT, KOTLIN
- [x] `global/common/enums/SubmissionStatus` — AC, WA, TLE, MLE, RE, CE
- [x] `global/common/enums/ExamType` — CERTIFICATION, COMPANY_TEST, SCHOOL_TEST

#### Provider 도메인
- [x] Entity, Repository, DTO(Request/Response), Service, Controller

#### User 도메인
- [x] Entity(User, UserProblem), Repository, DTO, Service, Controller

#### Supervisor 도메인
- [x] Entity, Repository, DTO, Service, Controller

#### Organization 도메인
- [x] Entity(Organization, OrganizationProblem, OrganizationContest, OrganizationExam)
- [x] Repository, DTO, Service, Controller

#### Category 도메인
- [x] Entity, Repository, DTO, Service, Controller

#### Problem 도메인
- [x] Entity(Problem, ProblemCategory), Repository, DTO, Service, Controller

#### Testcase 도메인
- [x] Entity, Repository, DTO, Service, Controller

#### Submission 도메인
- [x] Entity, Repository, DTO, Service, Controller
- [x] FakeJudgeService 연동 → 제출 시 Mock 채점 수행

#### Solution 도메인
- [x] Entity, Repository, DTO, Service, Controller

#### Contest 도메인
- [x] Entity(Contest, ContestProblem, UserContest, ContestSupervisor)
- [x] Repository, DTO, Service, Facade, Controller
- [x] `ContestFacade` — 참가 중복 검사, UserContest 생성

#### Exam 도메인
- [x] Entity(Exam, ExamProblem, UserExam, ExamSupervisor)
- [x] Repository, DTO, Service, Facade, Controller
- [x] `ExamFacade` — 응시 중복 검사, UserExam 생성

#### Judge 도메인
- [x] `JudgeService` (interface) — Strategy Pattern
- [x] `JudgeResult` — 채점 결과 DTO
- [x] `FakeJudgeService` — Mock 채점 구현 (항상 AC/100점)

---

## API 엔드포인트 목록

| 도메인 | 메서드 | 경로 | 설명 |
|--------|--------|------|------|
| Provider | POST | /api/providers | 제공자 생성 |
| Provider | GET | /api/providers | 목록 조회 |
| Provider | GET | /api/providers/{id} | 단건 조회 |
| User | POST | /api/users | 사용자 생성 |
| User | GET | /api/users | 목록 조회 |
| User | GET | /api/users/{id} | 단건 조회 |
| User | PUT | /api/users/{id} | 정보 수정 |
| User | DELETE | /api/users/{id} | 삭제 |
| Supervisor | POST | /api/supervisors | 감독자 생성 |
| Supervisor | GET | /api/supervisors | 목록 조회 |
| Supervisor | GET | /api/supervisors/{id} | 단건 조회 |
| Supervisor | DELETE | /api/supervisors/{id} | 삭제 |
| Organization | POST | /api/organizations | 기관 생성 |
| Organization | GET | /api/organizations | 목록 조회 |
| Organization | GET | /api/organizations/{id} | 단건 조회 |
| Organization | PUT | /api/organizations/{id} | 수정 |
| Organization | DELETE | /api/organizations/{id} | 삭제 |
| Organization | POST | /api/organizations/{id}/problems/{problemId} | 문제 등록 |
| Organization | POST | /api/organizations/{id}/contests/{contestId} | 대회 등록 |
| Organization | POST | /api/organizations/{id}/exams/{examId} | 시험 등록 |
| Category | POST | /api/categories | 카테고리 생성 |
| Category | GET | /api/categories | 목록 조회 |
| Category | GET | /api/categories/{id} | 단건 조회 |
| Category | DELETE | /api/categories/{id} | 삭제 |
| Problem | POST | /api/problems | 문제 생성 |
| Problem | GET | /api/problems | 목록 조회 |
| Problem | GET | /api/problems/{id} | 단건 조회 |
| Problem | PUT | /api/problems/{id} | 수정 |
| Problem | DELETE | /api/problems/{id} | 삭제 |
| Problem | POST | /api/problems/{id}/categories/{categoryId} | 카테고리 추가 |
| Testcase | POST | /api/testcases | 테스트케이스 생성 |
| Testcase | GET | /api/testcases/{id} | 단건 조회 |
| Testcase | GET | /api/testcases/problem/{problemId} | 문제별 조회 |
| Testcase | PUT | /api/testcases/{id} | 수정 |
| Testcase | DELETE | /api/testcases/{id} | 삭제 |
| Submission | POST | /api/submissions | 제출 (Mock 채점) |
| Submission | GET | /api/submissions/{id} | 단건 조회 |
| Submission | GET | /api/submissions/user/{userId} | 유저별 조회 |
| Submission | GET | /api/submissions/problem/{problemId} | 문제별 조회 |
| Solution | POST | /api/solutions | 풀이 작성 |
| Solution | GET | /api/solutions | 목록 조회 |
| Solution | GET | /api/solutions/{id} | 단건 조회 |
| Solution | PUT | /api/solutions/{id} | 수정 |
| Solution | DELETE | /api/solutions/{id} | 삭제 |
| Contest | POST | /api/contests | 대회 생성 |
| Contest | GET | /api/contests | 목록 조회 |
| Contest | GET | /api/contests/{id} | 단건 조회 |
| Contest | PUT | /api/contests/{id} | 수정 |
| Contest | DELETE | /api/contests/{id} | 삭제 |
| Contest | POST | /api/contests/{id}/problems | 문제 등록 |
| Contest | POST | /api/contests/{id}/supervisors/{supervisorId} | 감독자 등록 |
| Contest | POST | /api/contests/{id}/join | 참가 신청 |
| Contest | GET | /api/contests/{id}/participants | 참가자 목록 |
| Exam | POST | /api/exams | 시험 생성 |
| Exam | GET | /api/exams | 목록 조회 |
| Exam | GET | /api/exams/{id} | 단건 조회 |
| Exam | PUT | /api/exams/{id} | 수정 |
| Exam | DELETE | /api/exams/{id} | 삭제 |
| Exam | POST | /api/exams/{id}/problems | 문제 등록 |
| Exam | POST | /api/exams/{id}/supervisors/{supervisorId} | 감독자 등록 |
| Exam | POST | /api/exams/{id}/take | 응시 신청 |
| Exam | GET | /api/exams/{id}/results | 결과 목록 |

---

## 설계 원칙 적용 현황

| 원칙 | 적용 내용 |
|------|-----------|
| SRP | 도메인별 Service 분리 (ProblemService, SubmissionService 등) |
| OCP | JudgeService 인터페이스 → FakeJudgeService / 향후 DockerJudgeService 추가 가능 |
| LSP | JudgeService 구현체 대체 가능 |
| ISP | 통합 Service 금지, 도메인별 분리 |
| DIP | `private final JudgeService judgeService` — 인터페이스 의존 |
| DI | 생성자 주입 (@RequiredArgsConstructor) |
| AOP | 전역 예외 처리 (@RestControllerAdvice), @Transactional |
| Facade | ContestFacade, ExamFacade — 복잡한 도메인 흐름 조합 |
| Strategy | JudgeService interface |
| Builder | 모든 Entity, DTO에 @Builder 적용 |
| Template Method | ApiResponse<T> 응답 형식 통일 |

---

## 미구현 (향후 확장)
- Docker 기반 실제 채점 (DockerJudgeService)
- 언어별 컴파일러/런타임
- 실시간 시험/랭킹 (WebSocket, Redis)
- MSA 전환
- 자동 종료 이벤트
- 실시간 부정행위 감지
