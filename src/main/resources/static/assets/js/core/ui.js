import { currentUser, login, logout } from "./api.js";

export function createPaths(basePath = ".") {
  return {
    home: `${basePath}/index.html`,
    problems: `${basePath}/pages/problems/index.html`,
    problem: (id) => `${basePath}/pages/problem/index.html?id=${id}`,
    contests: `${basePath}/pages/contests/index.html`,
    exams: `${basePath}/pages/exams/index.html`,
    submissions: `${basePath}/pages/submissions/index.html`,
    operations: `${basePath}/pages/operations/index.html`
  };
}

export function renderShell(activeKey, basePath = ".") {
  const appShell = document.getElementById("app-shell");
  const paths = createPaths(basePath);
  const menu = [
    ["home", paths.home, "홈"],
    ["problems", paths.problems, "문제"],
    ["contests", paths.contests, "대회"],
    ["exams", paths.exams, "시험"],
    ["submissions", paths.submissions, "제출"],
    ["operations", paths.operations, "운영"]
  ];

  const user = currentUser();
  const authHtml = user
    ? `<div class="auth-box" style="margin-left:auto;display:flex;gap:8px;align-items:center;">
         <span class="badge badge-green">${user.role} #${user.subjectId}</span>
         <button id="logout-btn" class="btn">로그아웃</button>
       </div>`
    : `<div class="auth-box" style="margin-left:auto;display:flex;gap:6px;align-items:center;">
         <select id="login-role">
           <option value="USER">USER</option>
           <option value="SUPERVISOR">SUPERVISOR</option>
         </select>
         <input id="login-id" type="number" value="1" style="width:64px;" aria-label="id" />
         <button id="login-btn" class="btn btn-primary">로그인</button>
       </div>`;

  appShell.innerHTML = `
    <header class="site-header" style="display:flex;align-items:center;gap:16px;">
      <a class="brand-block" href="${paths.home}">
        <span class="brand-logo">S</span>
        <div class="brand-copy">
          <strong>SWE Demo</strong>
          <span>Online Judge</span>
        </div>
      </a>
      <nav class="global-menu">
        ${menu.map(([key, href, label]) => `<a class="${key === activeKey ? "is-active" : ""}" href="${href}">${label}</a>`).join("")}
      </nav>
      ${authHtml}
    </header>
  `;

  if (user) {
    document.getElementById("logout-btn").addEventListener("click", () => {
      logout();
      window.location.reload();
    });
  } else {
    document.getElementById("login-btn").addEventListener("click", async () => {
      try {
        await login(document.getElementById("login-role").value, document.getElementById("login-id").value);
        window.location.reload();
      } catch (e) {
        alert("로그인 실패: " + e.message);
      }
    });
  }
}

export function findProblemName(problemId, problems) {
  return problems.find((problem) => problem.problemId === problemId)?.problemTitle ?? "-";
}

export function findUserName(userId, users) {
  return users.find((user) => user.userId === userId)?.userName ?? "-";
}

export function statusBadgeClass(value) {
  if (["AC", "PASS", "참가 가능", "진행 중", "활성", true].includes(value)) return "badge badge-green";
  if (["WA", "응시 예정", "준비 중", "예정"].includes(value)) return "badge badge-yellow";
  if (["FAIL", "TLE", "RE", "MLE", "CE", false].includes(value)) return "badge badge-red";
  return "badge badge-blue";
}

export function escapeHtml(value) {
  return String(value ?? "")
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;");
}

/** ISO LocalDateTime("2026-06-21T17:00:00") → "2026-06-21 17:00" */
export function formatDateTime(iso) {
  if (!iso) return "-";
  return String(iso).replace("T", " ").slice(0, 16);
}

/** 시작/종료 시각과 마감 여부로 상태 라벨 도출 */
export function derivePhase(startTime, endTime, closed) {
  if (closed) return "마감";
  const now = Date.now();
  const start = startTime ? Date.parse(startTime) : null;
  const end = endTime ? Date.parse(endTime) : null;
  if (start && now < start) return "준비 중";
  if (end && now > end) return "종료";
  return "진행 중";
}
