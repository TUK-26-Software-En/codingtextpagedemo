import { createPaths, escapeHtml, renderShell, statusBadgeClass, formatDateTime } from "../core/ui.js";
import { getJSON, currentUser } from "../core/api.js";

renderShell("submissions", "../..");
const paths = createPaths("../..");

const tbody = document.getElementById("submission-table-body");

(async () => {
  const user = currentUser();
  if (!user) {
    tbody.innerHTML = `<tr><td colspan="7" class="muted">로그인 후 내 제출 기록을 볼 수 있습니다 (상단 USER 로그인).</td></tr>`;
    return;
  }

  let submissions = [];
  try {
    // GET /api/submissions/user/{userId}
    submissions = await getJSON(`/submissions/user/${user.subjectId}`);
  } catch (e) {
    tbody.innerHTML = `<tr><td colspan="7">불러오기 실패: ${e.message}</td></tr>`;
    return;
  }

  tbody.innerHTML = submissions.length
    ? submissions
        .map(
          (s) => `
            <tr>
              <td>${s.submissionId}</td>
              <td><a class="problem-link" href="${paths.problem(s.problemId)}">#${s.problemId}</a></td>
              <td>${user.subjectId}</td>
              <td>${s.submissionLanguage}</td>
              <td><span class="${statusBadgeClass(s.submissionStatus)}">${s.submissionStatus}</span></td>
              <td>${s.submissionScore}</td>
              <td>${formatDateTime(s.submittedAt)}</td>
            </tr>
            <tr><td colspan="7"><div class="code-box">${escapeHtml(s.submittedCode)}</div></td></tr>`
        )
        .join("")
    : `<tr><td colspan="7" class="muted">제출 기록이 없습니다.</td></tr>`;
})();
