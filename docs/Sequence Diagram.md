## 시퀀스 다이어그램 명세서

본 명세서는 코딩 테스트 플랫폼의 핵심 비즈니스 흐름을 시퀀스 다이어그램으로 정리한 문서이다. 기준은 현재 구현된 컨트롤러/서비스/퍼사드 코드이며, 채점은 `JudgeService` 인터페이스를 통해 `FakeJudgeService`가 호출되는 현 구조와 `CodeJudgeService`로의 교체 지점을 함께 표현한다.

## 1. 문제 제출 및 채점

`POST /api/submissions` → `SubmissionService.create`

```mermaid
sequenceDiagram
  actor User
  participant C as SubmissionController
  participant S as SubmissionService
  participant US as UserService
  participant PS as ProblemService
  participant J as JudgeService
  participant SR as SubmissionRepository
  participant UPR as UserProblemRepository

  User->>C: POST /api/submissions (userId, problemId, language, code)
  C->>S: create(request)
  S->>US: getById(userId)
  US-->>S: User
  S->>PS: getById(problemId)
  PS-->>S: Problem
  S->>J: judge(problemId, language, sourceCode)
  J-->>S: JudgeResult(status, score)
  S->>SR: save(Submission)
  SR-->>S: Submission
  S->>UPR: findByUserAndProblem(user, problem)
  alt 기존 풀이 존재
    UPR-->>S: UserProblem
    S->>S: updateStatus(status)
  else 신규
    S->>UPR: save(new UserProblem)
  end
  S-->>C: SubmissionResponse
  C-->>User: ApiResponse<SubmissionResponse>
```

> `JudgeService`는 인터페이스이므로 운영 시 `FakeJudgeService` → `CodeJudgeService` 교체에도 위 흐름은 변하지 않는다(OCP/DIP).

## 2. 대회 참가

`POST /api/contests/{id}/join` → `ContestFacade.joinContest`

```mermaid
sequenceDiagram
  actor User
  participant C as ContestController
  participant F as ContestFacade
  participant CS as ContestService
  participant US as UserService
  participant UCR as UserContestRepository

  User->>C: POST /api/contests/{id}/join (userId)
  C->>F: joinContest(contestId, request)
  F->>CS: getById(contestId)
  CS-->>F: Contest
  F->>US: getById(userId)
  US-->>F: User
  F->>UCR: existsByContestAndUser(contest, user)
  alt 이미 참가함
    UCR-->>F: true
    F-->>C: BusinessException(DUPLICATE_PARTICIPATION)
    C-->>User: 에러 응답
  else 신규 참가
    UCR-->>F: false
    F->>UCR: save(UserContest)
    UCR-->>F: UserContest
    F-->>C: UserContestResponse
    C-->>User: ApiResponse<UserContestResponse>
  end
```

## 3. 시험 응시

`POST /api/exams/{id}/take` → `ExamFacade.takeExam`

```mermaid
sequenceDiagram
  actor User
  participant C as ExamController
  participant F as ExamFacade
  participant ES as ExamService
  participant US as UserService
  participant UER as UserExamRepository

  User->>C: POST /api/exams/{id}/take (userId)
  C->>F: takeExam(examId, request)
  F->>ES: getById(examId)
  ES-->>F: Exam
  F->>US: getById(userId)
  US-->>F: User
  F->>UER: existsByExamAndUser(exam, user)
  alt 이미 응시함
    UER-->>F: true
    F-->>C: BusinessException(DUPLICATE_PARTICIPATION)
  else 신규 응시
    UER-->>F: false
    F->>UER: save(UserExam)
    UER-->>F: UserExam
    F-->>C: UserExamResponse
  end
```

## 4. 실제 채점 — CodeJudgeService (Phase 2 구현)

`judge.mode=code`일 때 동작. `SubmissionService` 호출부는 `FakeJudgeService`와 동일(인터페이스). 제출 코드를 언어별 `LanguageRunner`(Strategy)로 앱 컨테이너 내부에서 실행하고 테스트케이스 출력과 비교한다.

```mermaid
sequenceDiagram
  participant S as SubmissionService
  participant J as CodeJudgeService
  participant TR as TestcaseRepository
  participant R as LanguageRunner (Python/Java)
  participant P as Process (ProcessBuilder)

  S->>J: judge(problemId, language, sourceCode)
  J->>TR: findByProblemProblemId(problemId)
  TR-->>J: List<Testcase>
  J->>R: prepare(workDir, sourceCode)
  alt 컴파일 실패(Java)
    R-->>J: CompileException
    J-->>S: JudgeResult(CE, 0)
  else 준비 성공
    loop 각 테스트케이스
      J->>R: run(workDir, input, timeoutMs)
      R->>P: 실행 (stdin=input, timeout)
      P-->>R: stdout / exitCode / timedOut
      R-->>J: ExecResult
      J->>J: 비교 (timedOut→TLE, exit≠0→RE, 출력불일치→WA)
    end
    J-->>S: JudgeResult(AC/100 또는 첫 실패 status/0)
  end
```

> 본 Phase는 앱 컨테이너 내부 실행으로 격리가 약하다(완화: timeout, 임시 디렉터리 격리·정리). 운영 수준 격리(제출별 컨테이너/별도 judge 서비스)는 향후 과제.

## 5. 인증 — 로그인 및 보호 자원 접근 (Phase 3)

JWT 발급(로그인)과, 이후 토큰으로 보호 자원(변경계)에 접근하는 흐름.

```mermaid
sequenceDiagram
  actor C as Client
  participant AC as AuthController
  participant AS as AuthService
  participant JP as JwtProvider
  participant F as JwtAuthenticationFilter
  participant API as 보호 자원 Controller
  participant EP as AuthenticationEntryPoint

  Note over C,JP: 1) 로그인
  C->>AC: POST /api/auth/login {role, id}
  AC->>AS: login(request)
  AS->>AS: 역할별 존재 검증 (User/Supervisor)
  AS->>JP: issue(subjectId, role)
  JP-->>AS: JWT
  AS-->>C: ApiResponse(accessToken)

  Note over C,EP: 2) 보호 자원 접근
  C->>F: POST /api/submissions (Authorization: Bearer <jwt>)
  F->>JP: parse(token)
  alt 유효 토큰
    JP-->>F: Claims(sub, role)
    F->>API: 인증 컨텍스트로 진행
    API-->>C: 200 ApiResponse
  else 없음/위조/만료
    F->>F: 컨텍스트 비움
    API->>EP: 인증 필요
    EP-->>C: 401 ApiResponse(UNAUTHORIZED)
  end
```
