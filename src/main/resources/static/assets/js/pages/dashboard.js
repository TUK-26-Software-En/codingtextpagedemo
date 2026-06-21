import { notices } from "../core/data.js";
import { createPaths, renderShell, statusBadgeClass, findProblemName, formatDateTime, derivePhase } from "../core/ui.js";
import { getJSON, currentUser } from "../core/api.js";

renderShell("home", ".");
const paths = createPaths(".");

document.getElementById("dashboard-notices").innerHTML = notices
  .map((n) => `<article class="notice-item"><div>${n.title}</div><div class="muted">${n.date}</div></article>`)
  .join("");

(async () => {
  const [problems, contests, exams, users] = await Promise.all([
    getJSON("/problems").catch(() => []),
    getJSON("/contests").catch(() => []),
    getJSON("/exams").catch(() => []),
    getJSON("/users").catch(() => [])
  ]);

  document.getElementById("dashboard-summary").innerHTML = [
    ["문제", problems.length],
    ["유저", users.length],
    ["대회", contests.length],
    ["시험", exams.length]
  ]
    .map(([label, value]) => `<article class="summary-card"><strong>${value}</strong><div>${label}</div></article>`)
    .join("");

  document.getElementById("dashboard-contests").innerHTML = contests.length
    ? contests
        .map((c) => {
          const phase = derivePhase(c.startTime, c.endTime, c.closed);
          return `
            <article class="contest-card">
              <div class="badge-row"><span class="${statusBadgeClass(phase)}">${phase}</span></div>
              <h3>${c.contestTitle}</h3>
              <div class="muted">${formatDateTime(c.startTime)} - ${formatDateTime(c.endTime)}</div>
            </article>`;
        })
        .join("")
    : `<p class="muted">예정된 대회가 없습니다.</p>`;

  document.getElementById("dashboard-exams").innerHTML = exams.length
    ? exams
        .map((e) => {
          const phase = derivePhase(e.startTime, e.endTime, e.closed);
          return `
            <article class="info-card">
              <div class="badge-row"><span class="badge badge-blue">${e.examType ?? "-"}</span><span class="${statusBadgeClass(phase)}">${phase}</span></div>
              <h3>${e.examTitle}</h3>
              <div class="muted">${formatDateTime(e.startTime)} - ${formatDateTime(e.endTime)}</div>
            </article>`;
        })
        .join("")
    : `<p class="muted">예정된 시험이 없습니다.</p>`;

  document.getElementById("dashboard-problems").innerHTML = `
    <div class="table-wrap">
      <table class="table">
        <thead><tr><th>번호</th><th>제목</th><th>난이도</th><th>포인트</th></tr></thead>
        <tbody>
          ${problems
            .slice(0, 5)
            .map(
              (p) => `<tr><td>${p.problemId}</td><td><a class="problem-link" href="${paths.problem(p.problemId)}">${p.problemTitle}</a></td><td>${p.problemGrade ?? "-"}</td><td>${p.problemPoint}</td></tr>`
            )
            .join("") || `<tr><td colspan="4" class="muted">문제 없음</td></tr>`}
        </tbody>
      </table>
    </div>`;

  const user = currentUser();
  let recent = [];
  if (user) {
    recent = await getJSON(`/submissions/user/${user.subjectId}`).catch(() => []);
  }
  document.getElementById("dashboard-submissions").innerHTML = `
    <div class="table-wrap">
      <table class="table">
        <thead><tr><th>문제</th><th>결과</th><th>언어</th><th>시각</th></tr></thead>
        <tbody>
          ${
            user
              ? recent
                  .slice(0, 5)
                  .map(
                    (s) => `<tr><td><a class="problem-link" href="${paths.problem(s.problemId)}">${findProblemName(s.problemId, problems)}</a></td><td><span class="${statusBadgeClass(s.submissionStatus)}">${s.submissionStatus}</span></td><td>${s.submissionLanguage}</td><td>${formatDateTime(s.submittedAt)}</td></tr>`
                  )
                  .join("") || `<tr><td colspan="4" class="muted">제출 없음</td></tr>`
              : `<tr><td colspan="4" class="muted">로그인 시 최근 제출 표시</td></tr>`
          }
        </tbody>
      </table>
    </div>`;
})();
