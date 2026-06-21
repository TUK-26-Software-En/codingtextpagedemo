import {
  categories,
  contests,
  exams,
  operations,
  organizations,
  providers,
  solutions,
  submissions,
  supervisors,
  testcases,
  users
} from "../core/data.js";
import { renderShell, statusBadgeClass } from "../core/ui.js";

renderShell("operations", "../..");

// API hook: POST /api/organizations
document.getElementById("organization-board").innerHTML = `
  <div class="board-list">
    ${operations.organization
      .map(
        (item) => `
          <article class="board-card">
            <div class="badge-row"><span class="${statusBadgeClass(item.state)}">${item.state}</span></div>
            <strong>${item.name}</strong>
          </article>
        `
      )
      .join("")}
  </div>
  <div class="table-wrap">
    <table class="table">
      <thead><tr><th>기관 ID</th><th>기관 이름</th><th>기관 설명</th></tr></thead>
      <tbody>
        ${organizations
          .map(
            (org) => `
              <tr>
                <td>${org.organizationId}</td>
                <td>${org.organizationName}</td>
                <td>${org.organizationDescription}</td>
              </tr>
            `
          )
          .join("")}
      </tbody>
    </table>
  </div>
`;

// API hook: POST /api/contests/{id}/problems
// API hook: POST /api/contests/{id}/supervisors/{supervisorId}
// API hook: POST /api/exams/{id}/problems
// API hook: POST /api/exams/{id}/supervisors/{supervisorId}
document.getElementById("supervisor-board").innerHTML = `
  <div class="board-list">
    ${operations.supervisor
      .map(
        (item) => `
          <article class="board-card">
            <div class="badge-row"><span class="${statusBadgeClass(item.state)}">${item.state}</span></div>
            <strong>${item.name}</strong>
          </article>
        `
      )
      .join("")}
  </div>
  <div class="table-wrap">
    <table class="table">
      <thead><tr><th>감독자 ID</th><th>제공자</th><th>이름</th></tr></thead>
      <tbody>
        ${supervisors
          .map(
            (supervisor) => `
              <tr>
                <td>${supervisor.supervisorId}</td>
                <td>${supervisor.providerName}</td>
                <td>${supervisor.supervisorName}</td>
              </tr>
            `
          )
          .join("")}
      </tbody>
    </table>
  </div>
`;

document.getElementById("contest-admin-table").innerHTML = `
  <div class="table-wrap">
    <table class="table">
      <thead><tr><th>대회 ID</th><th>제목</th><th>설명</th><th>시작</th><th>종료</th></tr></thead>
      <tbody>
        ${contests
          .map(
            (contest) => `
              <tr>
                <td>${contest.contestId}</td>
                <td>${contest.contestTitle}</td>
                <td>${contest.contestDescription}</td>
                <td>${contest.startTime}</td>
                <td>${contest.endTime}</td>
              </tr>
            `
          )
          .join("")}
      </tbody>
    </table>
  </div>
`;

document.getElementById("exam-admin-table").innerHTML = `
  <div class="table-wrap">
    <table class="table">
      <thead><tr><th>시험 ID</th><th>제목</th><th>설명</th><th>유형</th><th>시작</th><th>종료</th></tr></thead>
      <tbody>
        ${exams
          .map(
            (exam) => `
              <tr>
                <td>${exam.examId}</td>
                <td>${exam.examTitle}</td>
                <td>${exam.examDescription}</td>
                <td>${exam.examType}</td>
                <td>${exam.startTime}</td>
                <td>${exam.endTime}</td>
              </tr>
            `
          )
          .join("")}
      </tbody>
    </table>
  </div>
`;

document.querySelector(".site-main").insertAdjacentHTML(
  "beforeend",
  `
    <section class="content-grid two-col">
      <section class="panel">
        <div class="panel-head"><h2>제공자 / 사용자</h2></div>
        <!-- API hook: GET /api/providers -->
        <div class="table-wrap">
          <table class="table">
            <thead><tr><th>Provider ID</th><th>Provider</th></tr></thead>
            <tbody>${providers.map((provider) => `<tr><td>${provider.providerId}</td><td>${provider.provider}</td></tr>`).join("")}</tbody>
          </table>
        </div>
        <!-- API hook: GET /api/users -->
        <div class="table-wrap">
          <table class="table">
            <thead><tr><th>User ID</th><th>Provider ID</th><th>Provider</th><th>User Name</th><th>User Info</th><th>Point</th><th>Rank</th></tr></thead>
            <tbody>
              ${users
                .map(
                  (user) => `
                    <tr>
                      <td>${user.userId}</td>
                      <td>${user.providerId}</td>
                      <td>${user.providerName}</td>
                      <td>${user.userName}</td>
                      <td>${user.userInfo}</td>
                      <td>${user.userPoint}</td>
                      <td>${user.userRank}</td>
                    </tr>
                  `
                )
                .join("")}
            </tbody>
          </table>
        </div>
      </section>
      <section class="panel">
        <div class="panel-head"><h2>카테고리 / 테스트케이스 / 풀이</h2></div>
        <!-- API hook: GET /api/categories -->
        <div class="table-wrap">
          <table class="table">
            <thead><tr><th>Category ID</th><th>Name</th></tr></thead>
            <tbody>${categories.map((category) => `<tr><td>${category.categoryId}</td><td>${category.categoryName}</td></tr>`).join("")}</tbody>
          </table>
        </div>
        <!-- API hook: GET /api/testcases/{id}, GET /api/testcases/problem/{problemId} -->
        <div class="table-wrap">
          <table class="table">
            <thead><tr><th>Testcase ID</th><th>Problem ID</th><th>Input</th><th>Output</th></tr></thead>
            <tbody>${testcases.map((testcase) => `<tr><td>${testcase.testcaseId}</td><td>${testcase.problemId}</td><td>${testcase.inputData}</td><td>${testcase.outputData}</td></tr>`).join("")}</tbody>
          </table>
        </div>
        <!-- API hook: GET /api/solutions, GET /api/solutions/{id} -->
        <div class="table-wrap">
          <table class="table">
            <thead><tr><th>Solution ID</th><th>Problem ID</th><th>User ID</th><th>User Name</th><th>Title</th><th>Content</th></tr></thead>
            <tbody>${solutions.map((solution) => `<tr><td>${solution.solutionId}</td><td>${solution.problemId}</td><td>${solution.userId}</td><td>${solution.userName}</td><td>${solution.solutionTitle}</td><td>${solution.solutionContent}</td></tr>`).join("")}</tbody>
          </table>
        </div>
      </section>
    </section>
    <section class="panel">
      <div class="panel-head"><h2>제출 DTO 전체 필드 확인</h2></div>
      <div class="table-wrap">
        <table class="table">
          <thead><tr><th>Submission ID</th><th>User ID</th><th>Problem ID</th><th>Language</th><th>Status</th><th>Score</th><th>Submitted At</th><th>Submitted Code</th></tr></thead>
          <tbody>${submissions.map((submission) => `<tr><td>${submission.submissionId}</td><td>${submission.userId}</td><td>${submission.problemId}</td><td>${submission.submissionLanguage}</td><td>${submission.submissionStatus}</td><td>${submission.submissionScore}</td><td>${submission.submittedAt}</td><td><div class="code-box">${submission.submittedCode.replaceAll("<", "&lt;").replaceAll(">", "&gt;")}</div></td></tr>`).join("")}</tbody>
        </table>
      </div>
    </section>
  `
);
