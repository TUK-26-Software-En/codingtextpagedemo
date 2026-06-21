## 컴포넌트 다이어그램 명세서

본 명세서는 코딩 테스트 플랫폼을 컴포넌트 관점에서 정리한 문서이다. 배포 컴포넌트(컨테이너), 애플리케이션 내부 모듈, 도메인 간 의존을 나눠 표현한다. 기준은 현재 구현 코드와 Phase 1(컨테이너화) 결과이며, 향후 컴포넌트(채점기)는 점선/주석으로 구분한다.

## 1. 배포 컴포넌트 (Docker Compose)

```mermaid
flowchart LR
  client[(브라우저 / Swagger UI)]

  subgraph compose["docker compose 네트워크"]
    app["app 컨테이너\nSpring Boot REST API + 정적 FE 서빙 + 채점 런타임\n(static + python3/javac)\n:8080 (profile=docker)"]
    db[("db 컨테이너\nPostgreSQL 16\nvolume: pgdata")]
    judge["격리 judge 컨테이너\n(향후, 미구현)"]:::future
  end

  client -->|HTTP 8080| app
  app -->|JDBC 5432| db
  app -.->|향후 격리 실행| judge

  classDef future fill:#f7f7f7,stroke:#999,stroke-dasharray:5 5,color:#555;
```

- `app` : 멀티스테이지 `Dockerfile`(JDK21 빌드 → JDK21+python3 실행)로 생성. `judge.mode=code`면 **앱 내부에서** 제출 코드를 실행해 채점한다.
- `db` : healthcheck(`pg_isready`) 통과 후 `app` 기동, 데이터는 named volume 영속
- `격리 judge` : 앱 내부 실행의 격리 약점을 보완할 **향후** 분리 컴포넌트(제출별 컨테이너/별도 서비스)

## 2. 애플리케이션 내부 모듈 (계층)

```mermaid
flowchart TB
  subgraph web["Web 계층"]
    sec["JwtAuthenticationFilter\n(보안 필터)"]
    ctrl["Controller (도메인별)"]
    dto["DTO request/response"]
  end
  subgraph biz["비즈니스 계층"]
    svc["Service (도메인별)"]
    facade["Facade (Contest/Exam)"]
    judge["JudgeService (interface)"]
    fake["FakeJudgeService"]
    code["CodeJudgeService"]
    runner["LanguageRunner\n(Python/Java)"]
    batch["BatchScheduler\n(@Scheduled 집계)"]
  end
  subgraph data["영속 계층"]
    repo["Repository (Spring Data JPA)"]
    entity["Entity / BaseEntity"]
  end
  subgraph global["공통(global)"]
    api["ApiResponse<T>"]
    ex["GlobalExceptionHandler / ErrorCode"]
    cfg["Config (Jpa/Security/Swagger)"]
  end

  sec --> ctrl
  ctrl --> svc
  ctrl --> facade
  ctrl --> dto
  ctrl --> api
  facade --> svc
  svc --> repo
  svc --> judge
  fake -.implements.-> judge
  code -.implements.-> judge
  code --> runner
  code --> repo
  batch --> repo
  repo --> entity
  ctrl -.예외.-> ex
```

- 컨트롤러는 서비스/퍼사드에만 의존하고, 응답은 `ApiResponse<T>`로 통일한다.
- 퍼사드는 복합 흐름(대회 참가, 시험 응시)을 조합한다.
- 서비스는 리포지토리와 (제출의 경우) `JudgeService` 인터페이스에 의존한다.

## 3. 도메인 컴포넌트 의존

```mermaid
flowchart LR
  user["user / provider / supervisor"]
  problem["problem / testcase / category"]
  submission["submission / solution"]
  contest["contest (+facade)"]
  exam["exam (+facade)"]
  org["organization"]
  judge["judge"]

  submission --> user
  submission --> problem
  submission --> judge
  contest --> problem
  contest --> user
  exam --> problem
  exam --> user
  org --> problem
  org --> contest
  org --> exam
```

문제(`problem`)가 제출·대회·시험·기관의 공통 참조 대상이며, 채점(`judge`)은 제출에서만 사용된다. 상세 클래스 관계는 [Class Diagram.md](Class%20Diagram.md) 참조.
