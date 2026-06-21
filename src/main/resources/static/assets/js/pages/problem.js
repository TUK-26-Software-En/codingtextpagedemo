import { createPaths, escapeHtml, renderShell, statusBadgeClass, formatDateTime } from "../core/ui.js";
import { getJSON, postJSON, currentUser } from "../core/api.js";

renderShell("problems", "../..");
const paths = createPaths("../..");

const LANGUAGES = ["PYTHON", "JAVA", "C", "CPP", "JAVASCRIPT", "KOTLIN"];

const params = new URLSearchParams(window.location.search);
const problemId = Number(params.get("id"));

const titleBlock = document.getElementById("problem-title-block");
const statementBox = document.getElementById("problem-tab-statement");
const ioBox = document.getElementById("problem-tab-io");
const submissionsBox = document.getElementById("problem-tab-submissions");
const submitBox = document.getElementById("problem-submit-box");

function bindTabs() {
  document.querySelectorAll(".tab-button").forEach((button) => {
    button.addEventListener("click", () => {
      document.querySelectorAll(".tab-button").forEach((t) => t.classList.remove("is-active"));
      document.querySelectorAll(".tab-panel").forEach((p) => p.classList.remove("is-active"));
      button.classList.add("is-active");
      document.getElementById(`problem-tab-${button.dataset.tab}`).classList.add("is-active");
    });
  });
}

async function renderSubmissions() {
  let submissions = [];
  try {
    submissions = await getJSON(`/submissions/problem/${problemId}`);
  } catch (_) { /* ignore */ }
  submissionsBox.querySelector("#problem-submission-rows").innerHTML = submissions.length
    ? submissions
        .map(
          (s) => `
            <tr>
              <td>${s.submissionId}</td>
              <td>${s.userId}</td>
              <td>${s.submissionLanguage}</td>
              <td><span class="${statusBadgeClass(s.submissionStatus)}">${s.submissionStatus}</span></td>
              <td>${s.submissionScore}</td>
              <td><div class="code-box">${escapeHtml(s.submittedCode)}</div></td>
            </tr>`
        )
        .join("")
    : `<tr><td colspan="6" class="muted">제출 없음</td></tr>`;
}

(async () => {
  let problem;
  try {
    problem = await getJSON(`/problems/${problemId}`);
  } catch (e) {
    titleBlock.innerHTML = `<h1>문제를 불러오지 못했습니다</h1><p class="muted">${e.message}</p>`;
    return;
  }

  const [testcases, solutionsAll] = await Promise.all([
    getJSON(`/testcases/problem/${problemId}`).catch(() => []),
    getJSON(`/solutions`).catch(() => [])
  ]);
  const solutions = solutionsAll.filter((s) => s.problemId === problemId);
  const example = testcases[0];

  document.title = `SWE Demo | ${problem.problemTitle}`;

  titleBlock.innerHTML = `
    <div>
      <h1>${problem.problemId}. ${problem.problemTitle}</h1>
      <div class="badge-row">
        <span class="badge badge-blue">${problem.problemGrade ?? "-"}</span>
        <span class="badge">${problem.problemLanguage ?? "-"}</span>
        <span class="badge">${problem.problemPoint} pt</span>
      </div>
    </div>`;

  statementBox.innerHTML = `
    <section class="statement-block">
      <h3>문제</h3>
      <p>${escapeHtml(problem.problemContent)}</p>
    </section>`;

  ioBox.innerHTML = `
    <section class="example-block">
      <h3>예제 입력</h3>
      <div class="code-box">${example ? escapeHtml(example.inputData) : "-"}</div>
    </section>
    <section class="example-block">
      <h3>예제 출력</h3>
      <div class="code-box">${example ? escapeHtml(example.outputData) : "-"}</div>
    </section>
    <section class="example-block">
      <h3>테스트케이스 (${testcases.length})</h3>
      <div class="table-wrap">
        <table class="table">
          <thead><tr><th>ID</th><th>입력</th><th>출력</th></tr></thead>
          <tbody>
            ${testcases
              .map((tc) => `<tr><td>${tc.testcaseId}</td><td><div class="code-box">${escapeHtml(tc.inputData)}</div></td><td><div class="code-box">${escapeHtml(tc.outputData)}</div></td></tr>`)
              .join("") || `<tr><td colspan="3" class="muted">테스트케이스 없음</td></tr>`}
          </tbody>
        </table>
      </div>
    </section>
    <section class="example-block">
      <h3>풀이</h3>
      ${solutions
        .map((s) => `<div class="pipeline-step"><strong>${escapeHtml(s.solutionTitle)}</strong><div class="muted">${escapeHtml(s.userName)}</div><p>${escapeHtml(s.solutionContent)}</p></div>`)
        .join("") || `<p class="muted">등록된 풀이 없음</p>`}
    </section>`;

  submissionsBox.innerHTML = `
    <div class="table-wrap">
      <table class="table">
        <thead><tr><th>제출 번호</th><th>유저</th><th>언어</th><th>결과</th><th>점수</th><th>코드</th></tr></thead>
        <tbody id="problem-submission-rows"></tbody>
      </table>
    </div>`;
  await renderSubmissions();

  // 제출 폼 (POST /api/submissions)
  const langOptions = LANGUAGES
    .map((l) => `<option value="${l}" ${l === problem.problemLanguage ? "selected" : ""}>${l}</option>`)
    .join("");
  submitBox.innerHTML = `
    <form id="submit-form" class="submit-form">
      <label>언어
        <select id="submit-language">${langOptions}</select>
      </label>
      <label>소스 코드
        <textarea id="submit-code" spellcheck="false" rows="10">a, b = map(int, input().split())
print(a + b)</textarea>
      </label>
      <div class="quick-actions">
        <button type="submit" class="btn btn-primary">제출 / 채점</button>
        <a class="btn" href="${paths.problems}">목록</a>
      </div>
      <div id="submit-result"></div>
    </form>`;

  document.getElementById("submit-form").addEventListener("submit", async (event) => {
    event.preventDefault();
    const result = document.getElementById("submit-result");
    const user = currentUser();
    if (!user) {
      result.innerHTML = `<p class="badge badge-yellow">로그인이 필요합니다 (상단 USER 로그인).</p>`;
      return;
    }
    result.innerHTML = `<p class="muted">채점 중...</p>`;
    try {
      const submission = await postJSON("/submissions", {
        userId: user.subjectId,
        problemId,
        submissionLanguage: document.getElementById("submit-language").value,
        submittedCode: document.getElementById("submit-code").value
      });
      result.innerHTML = `<p>결과 <span class="${statusBadgeClass(submission.submissionStatus)}">${submission.submissionStatus}</span> / 점수 ${submission.submissionScore} <span class="muted">(${formatDateTime(submission.submittedAt)})</span></p>`;
      await renderSubmissions();
    } catch (e) {
      result.innerHTML = `<p class="badge badge-red">제출 실패: ${e.message}</p>`;
    }
  });

  bindTabs();
})();
