# Coding Test Platform - 구현 진행 상황

## 프로젝트 개요
Spring Boot 기반 온라인 코딩 테스트 플랫폼 백엔드

---

## 변경 이력

### [2026-06-22] Phase 5 — 프런트엔드 연동

**변경 배경**
미연결 정적 FE 프로토타입을 Spring 동일출처 서빙 + 실제 REST API에 연동(핵심 루프). 백엔드 무변경.

**변경 파일**
| 파일 | 변경 내용 |
|------|-----------|
| `resources/templates/{index.html,assets,pages}` → `resources/static/` | 이동(동일출처 정적 서빙) |
| `static/assets/js/core/api.js` | 신규 — fetch 래퍼 + JWT(localStorage) + 로그인 |
| `static/assets/js/core/ui.js` | createPaths(index.html 명시) + 헤더 로그인 위젯 + 헬퍼 |
| `static/assets/js/pages/{problems,problem,submissions,dashboard,contests,exams}.js` | 목 데이터 → REST API 연동 |
| `static/index.html` | 디렉터리 링크 → `index.html` 명시 |

**연동 범위**
- 로그인(개발용, 헤더) → 문제 목록/상세 → 코드 제출(실채점) → 결과(AC/점수) → 제출 기록
- 대시보드 요약/카드, 대회·시험 목록·결과(읽기). 운영 페이지는 정적 유지
- 응답 형상 차이는 FE가 적응(예제는 testcase에서 유도 등). 백엔드 Java 무변경

**검증 (브라우저, docker)**
`http://localhost:8080/` 대시보드 실데이터 렌더 → 로그인(USER #1) → 문제 상세 제출 → 화면에 `AC/100` 표시 확인.

---

### [2026-06-22] Phase 4 — 배치/집계 (대회·시험 자동 마감·랭킹)

**변경 배경**
대회/시험은 `endTime`이 지나도 결과가 집계되지 않았다. 종료된 대회/시험을 자동 마감하고 참가자 결과(랭킹/합격)를 집계한다.

**추가/변경 파일**
| 파일 | 변경 내용 |
|------|-----------|
| `batch/BatchConfig.java` | 신규 — @EnableScheduling |
| `batch/BatchScheduler.java` | 신규 — @Scheduled 주기 집계 |
| `batch/ContestAggregationService.java` | 신규 — 대회 마감·랭킹 집계 |
| `batch/ExamAggregationService.java` | 신규 — 시험 마감·합격/등급 집계 |
| `batch/controller/BatchController.java` (+dto/AggregationResult) | 신규 — POST /api/batch/aggregate 수동 트리거 |
| `contest/entity/Contest.java`, `exam/entity/Exam.java` | `closed` 플래그 + `close()` |
| `contest/entity/UserContest.java` | `applyResult(total, solved)`, `assignRank(rank)` |
| `exam/entity/UserExam.java` | `applyResult(total, pass, grade)` |
| `contest/repository/ContestRepository.java`, `exam/repository/ExamRepository.java` | `findByEndTimeBeforeAndClosedFalse` |
| `submission/repository/SubmissionRepository.java` | `existsBy...SubmittedAtBetween` (기간 내 AC 판정) |
| `application.properties` | `batch.aggregate-interval-ms`, `batch.initial-delay-ms` |

**집계 규칙**
- 대상: `endTime < now & !closed`
- 대회: solvedCount=기간 내 AC 문제 수, totalScore=Σ `contestProblemScore`, rank=총점 내림차순 순번
- 시험: totalScore=Σ(기간 내 AC `examProblemScore`), pass=총점≥만점60%, grade=만점대비% (A≥90/B≥80/C≥70/D≥60/F)
- 멱등: 집계 후 `close()` → 재실행 시 0건

**검증 (docker)**
대회 endTime 경과 후 집계 → totalScore=100/solvedCount=1/rank=1, 시험 → totalScore=20/pass=true/grade=A, 재트리거 0/0(멱등), @Scheduled 로그 확인. (서버 UTC 기준 시간창 주의)

---

### [2026-06-22] Phase 3 — 인증/인가 (JWT + 개발용 로그인)

**변경 배경**
전체 허용(`permitAll`)이던 보안을 **JWT 기반 stateless 인증**으로 전환. 읽기(GET) 공개, 변경계(POST/PUT/DELETE) 인증 필요.
User/Supervisor에 자격정보가 없어 본 Phase는 **개발용 로그인**(역할+id로 토큰 발급)으로 인증 메커니즘을 구현·검증한다.
실제 Google OAuth2 소셜 로그인은 client-secret/브라우저 필요 → 구조·문서로 후속.

**변경/추가 파일**
| 파일 | 변경 내용 |
|------|-----------|
| `auth/AuthRole.java` | 신규 — USER/SUPERVISOR |
| `auth/JwtProperties.java` | 신규 — jwt.secret/expiration-ms 바인딩 |
| `auth/JwtProvider.java` | 신규 — HS256 발급/검증(jjwt) |
| `auth/JwtAuthenticationFilter.java` | 신규 — Bearer 검증 → SecurityContext 설정 |
| `auth/RestAuthenticationEntryPoint.java` / `RestAccessDeniedHandler.java` / `ErrorJsonWriter.java` | 신규 — 401/403 표준 JSON 응답 |
| `auth/service/AuthService.java`, `auth/controller/AuthController.java` | 신규 — 로그인 |
| `auth/dto/LoginRequest.java`, `TokenResponse.java` | 신규 — 요청/응답 |
| `global/config/SecurityConfig.java` | permitAll → JWT 인가 규칙 + 필터/핸들러 + STATELESS |
| `global/config/SwaggerConfig.java` | bearerAuth 보안 스킴 추가 |
| `global/exception/ErrorCode.java` | UNAUTHORIZED/FORBIDDEN 추가 |
| `build.gradle` | jjwt 3종(api/impl/jackson) |
| `application.properties` | `jwt.secret`(env override), `jwt.expiration-ms` |

**인가 정책**
- 공개: 모든 GET, `POST /api/auth/login`, 회원가입(`POST /api/providers|users|supervisors`), h2-console, swagger
- 인증 필요: 그 외 모든 변경계(제출/풀이/배정/참가 등)
- 역할 강제는 미적용(토큰에 role 클레임은 포함)

**검증 (docker)**
토큰 없이 POST→401, GET→200, 로그인→토큰, Bearer로 POST→AC, 위조/변조 토큰→401, supervisor 회원가입→로그인 토큰. 전부 확인.

**보안 주의**
개발용 로그인은 자격검증 없이 id로 발급(데모 한정). `jwt.secret` 운영은 `JWT_SECRET` env 필수 교체. 세분 권한/실제 OAuth2는 후속.

---

### [2026-06-22] Phase 2 — 실제 채점 (CodeJudgeService)

**변경 배경**
Mock(`FakeJudgeService`)을 대체할 실제 채점 구현. 제출 코드를 앱 컨테이너 내부에서 실행(ProcessBuilder)하고
문제의 `Testcase`와 출력 비교로 채점한다. 기존 Strategy 구조(`JudgeService`)를 유지하여 `SubmissionService`는 무변경에 가깝다.

**핵심 변경: JudgeService 시그니처**
`judge(problemId, language)` → `judge(problemId, language, sourceCode)` (실제 채점에 코드 필요).

**변경 파일**
| 파일 | 변경 내용 |
|------|-----------|
| `judge/JudgeService.java` | 시그니처에 `sourceCode` 추가 |
| `judge/FakeJudgeService.java` | 시그니처 맞춤 + `@ConditionalOnProperty(judge.mode=fake, matchIfMissing)` |
| `judge/CodeJudgeService.java` | 신규 — 채점 오케스트레이션, `@ConditionalOnProperty(judge.mode=code)` |
| `judge/CompileException.java` | 신규 — 컴파일 실패(=CE) 신호 |
| `judge/runner/LanguageRunner.java` | 신규 — 언어별 실행 전략(Strategy) |
| `judge/runner/AbstractProcessRunner.java` | 신규 — 프로세스 실행 공통(Template Method) |
| `judge/runner/PythonRunner.java`, `JavaRunner.java` | 신규 — Python/Java 러너 |
| `judge/runner/ExecResult.java` | 신규 — 실행 결과 값 객체 |
| `submission/service/SubmissionService.java` | judge 호출에 `submittedCode` 전달 |
| `application.properties` | `judge.mode=fake`, `judge.time-limit-ms=2000` |
| `application-docker.properties` | `judge.mode=code` |
| `Dockerfile` | runtime base 21-jre→21-jdk + python3 설치 |

**채점 규칙**
모든 Testcase 통과 → `AC`/100. 첫 실패에서 종료: timeout→`TLE`, 종료코드≠0→`RE`, 출력 불일치→`WA`, 컴파일 실패→`CE`.
시간 제한 `judge.time-limit-ms`(기본 2000ms). 메모리 제한(MLE)은 본 Phase 미검출.

**검증 (docker, judge.mode=code)**
PYTHON 정답→AC/100, 오답→WA, 무한루프→TLE, JAVA(class Main) 정답→AC, 컴파일오류→CE 전부 확인.

**보안 주의**
앱 컨테이너 내부 실행 = 격리 약함. 완화: timeout, 임시 디렉터리 격리·정리. 운영 격리(제출별 컨테이너/별도 judge)는 향후.

---

### [2026-06-21] Phase 1 — PostgreSQL 영속성 / Docker Compose / 문서화

**변경 배경**
운영 DB를 PostgreSQL로 전환하고, app + DB를 Docker Compose로 번들 실행 가능하게 한다.
로컬/테스트는 H2를 유지하기 위해 프로파일을 분리한다. 도메인 로직은 변경하지 않는다(최소 변경).

**변경 파일**
| 파일 | 변경 내용 |
|------|-----------|
| `build.gradle` | `org.postgresql:postgresql` 드라이버 추가, `processResources`에서 `templates/.claude/**` 제외 |
| `application.properties` | datasource를 프로파일로 이동, 공통 설정만 유지, 기본 `active=local` |
| `application-local.properties` | 신규 — H2 인메모리(PostgreSQL 모드), create-drop |
| `application-docker.properties` | 신규 — PostgreSQL, env 오버라이드, ddl-auto=update |
| `Dockerfile` | 신규 — 멀티스테이지(JDK21 빌드 → JRE21 실행) |
| `build.gradle` | Java toolchain 17→21 |
| `docker-compose.yml` | 신규 — app + postgres:16(healthcheck, volume) |
| `.dockerignore` | 신규 — 빌드 컨텍스트 정리 |
| `.gitignore` | `src/main/resources/templates/.claude/`(외부 worktree) 제외 |
| `docs/` | 기획서·구조·작동흐름·Sequence/Component/Batch 다이어그램·README 신규, Class Diagram 중복 절 제거 |

**프로파일 구조**
```
application.properties (공통, 기본 active=local)
 ├─ application-local.properties  → H2 인메모리 (개발/테스트)
 └─ application-docker.properties → PostgreSQL (docker compose)
```

**실행**
```
./gradlew bootRun          # local(H2)
docker compose up --build  # docker(PostgreSQL)
```

---

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
- [x] `JudgeService` (interface) — Strategy Pattern, `judge(problemId, language, sourceCode)`
- [x] `JudgeResult` — 채점 결과 DTO
- [x] `FakeJudgeService` — Mock 채점 (judge.mode=fake, 항상 AC/100)
- [x] `CodeJudgeService` — 실제 실행 채점 (judge.mode=code)
- [x] `runner` — `LanguageRunner`(Strategy) + `AbstractProcessRunner`(Template Method) + `PythonRunner`/`JavaRunner`, `ExecResult`, `CompileException`

#### Auth 도메인 (Phase 3)
- [x] `JwtProvider` — HS256 JWT 발급/검증
- [x] `JwtAuthenticationFilter` — Bearer 인증 필터
- [x] `AuthService`/`AuthController` — 개발용 로그인
- [x] `SecurityConfig` — 읽기공개/쓰기인증 + 401/403 표준 응답

#### Batch 도메인 (Phase 4)
- [x] `BatchScheduler` — @Scheduled 주기 집계
- [x] `ContestAggregationService` — 대회 마감·랭킹
- [x] `ExamAggregationService` — 시험 마감·합격/등급
- [x] `BatchController` — 수동 트리거, `closed` 플래그로 멱등

---

## API 엔드포인트 목록

| 도메인 | 메서드 | 경로 | 설명 |
|--------|--------|------|------|
| Auth | POST | /api/auth/login | 로그인(개발용) — 역할+id로 JWT 발급 |
| Batch | POST | /api/batch/aggregate | 종료된 대회/시험 마감·집계(수동 트리거, 인증) |
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
- (Phase 2 ✅ 완료) 실제 채점 CodeJudgeService — Python/Java. 후속: C/CPP/JS/Kotlin 러너, MLE/메모리 제한, 격리 강화
- (Phase 3 ✅ 완료) JWT 인증 + 읽기공개/쓰기인증. 후속: 실제 Google OAuth2, 역할 세분 인가, 리프레시 토큰
- (Phase 4 ✅ 완료) 대회·시험 자동 마감 / 랭킹·합격 집계. 후속: 분산 락, 부분 점수, Submission-대회 연결
- (Phase 5 ✅ 완료) FE 연동(정적 동일출처, 핵심 루프). 후속: 대회/시험/운영 쓰기 UI, 실제 OAuth2 로그인, 코드 에디터
- (Phase 5) 프런트엔드 연동 (REST API 소비, CORS)
- 실시간 시험/랭킹 (WebSocket, Redis), MSA 전환, 실시간 부정행위 감지
