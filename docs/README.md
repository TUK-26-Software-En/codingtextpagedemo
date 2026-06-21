# 코딩 테스트 플랫폼 — 문서

본 디렉터리는 코딩 테스트 플랫폼(server-client 기반)의 기획·구조·동작·다이어그램 문서를 모은다. 본 프로젝트는 소프트웨어 공학의 정석적 방법(요구사항 → 설계 → 단계적 구현 → 문서화)을 준수하여 진행하며, **각 작업(Phase)이 끝날 때마다 본 문서군을 갱신**한다.

## 문서 목록

| 문서 | 내용 |
| --- | --- |
| [기획서.md](기획서.md) | 목적·목표·대상 사용자·기능/비기능 요구사항·시스템 범위·Phase 로드맵 |
| [구조.md](구조.md) | 기술 스택·패키지/계층 구조·프로파일 구성·컨테이너 토폴로지 |
| [작동흐름.md](작동흐름.md) | 실행 방법(local/docker)·요청 라이프사이클·핵심 흐름 |
| [ERD.md](ERD.md) | 데이터 모델(테이블·관계) |
| [Class Diagram.md](Class%20Diagram.md) | 핵심 클래스 명세·클래스 다이어그램 |
| [usecase.md](usecase.md) | 유스케이스(유저/운영/제출 관점) |
| [Sequence Diagram.md](Sequence%20Diagram.md) | 제출 채점·대회 참가·시험 응시 시퀀스 |
| [Component Diagram.md](Component%20Diagram.md) | 배포·내부 모듈·도메인 컴포넌트 |
| [Batch Diagram.md](Batch%20Diagram.md) | 배치 흐름(대회/시험 마감·집계) — Phase 4 설계 |
| [Design Pattern + Springboot.md](Design%20Pattern%20+%20Springboot.md) | SOLID·디자인 패턴 적용 정리 |

진행 이력은 루트 [IMPLEMENTATION_PROGRESS.md](../IMPLEMENTATION_PROGRESS.md) 참조.

## 문서 형식 규칙

- 한국어 서술 + `## N.` 번호 절 구성, 도입부에 1문장 요약("~ 명세서이다")
- 다이어그램은 Mermaid 코드블록으로 작성(`classDiagram` / `sequenceDiagram` / `flowchart`)
- 향후(미구현) 컴포넌트·흐름은 점선/주석/별도 절로 명시하여 현재 구현과 구분

## 다이어그램 종류

| 종류 | 문서 |
| --- | --- |
| Class | [Class Diagram.md](Class%20Diagram.md) |
| Sequence | [Sequence Diagram.md](Sequence%20Diagram.md) |
| Component | [Component Diagram.md](Component%20Diagram.md) |
| Batch | [Batch Diagram.md](Batch%20Diagram.md) |
| ERD | [ERD.md](ERD.md) |
| Use Case | [usecase.md](usecase.md) |
