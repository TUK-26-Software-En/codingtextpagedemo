import { createPaths, renderShell, statusBadgeClass, escapeHtml, formatDateTime, derivePhase } from "../core/ui.js";
import { getJSON } from "../core/api.js";

renderShell("contests", "../..");
createPaths("../..");

const list = document.getElementById("contest-list");
const detail = document.getElementById("contest-detail");

let contests = [];
let selectedId = null;

async function renderDetail() {
  const contest = contests.find((c) => c.contestId === selectedId);
  if (!contest) {
    detail.innerHTML = `<p class="muted">대회가 없습니다.</p>`;
    return;
  }
  const phase = derivePhase(contest.startTime, contest.endTime, contest.closed);
  let participants = [];
  try {
    participants = await getJSON(`/contests/${contest.contestId}/participants`);
  } catch (_) { /* ignore */ }

  detail.innerHTML = `
    <div class="statement-block">
      <h3>${escapeHtml(contest.contestTitle)}</h3>
      <div class="badge-row">
        <span class="${statusBadgeClass(phase)}">${phase}</span>
        <span class="badge">${formatDateTime(contest.startTime)} - ${formatDateTime(contest.endTime)}</span>
        <span class="badge">참가 ${participants.length}명</span>
      </div>
      <p>${escapeHtml(contest.contestDescription)}</p>
    </div>
    <div class="statement-block">
      <h3>참가자 / 리더보드</h3>
      <div class="table-wrap">
        <table class="table">
          <thead><tr><th>참가 ID</th><th>User</th><th>참가 시각</th><th>총점</th><th>순위</th><th>해결 수</th></tr></thead>
          <tbody>
            ${participants
              .map((r) => `<tr><td>${r.userContestId}</td><td>${escapeHtml(r.userName)} (#${r.userId})</td><td>${formatDateTime(r.joinedAt)}</td><td>${r.totalScore}</td><td>${r.rank}</td><td>${r.solvedCount}</td></tr>`)
              .join("") || `<tr><td colspan="6" class="muted">참가자 없음</td></tr>`}
          </tbody>
        </table>
      </div>
    </div>`;
}

(async () => {
  try {
    contests = await getJSON("/contests");
  } catch (e) {
    list.innerHTML = `<p>불러오기 실패: ${e.message}</p>`;
    return;
  }
  if (!contests.length) {
    list.innerHTML = `<p class="muted">대회 없음</p>`;
    detail.innerHTML = `<p class="muted">대회 없음</p>`;
    return;
  }
  selectedId = contests[0].contestId;

  list.innerHTML = contests
    .map((c) => {
      const phase = derivePhase(c.startTime, c.endTime, c.closed);
      return `
        <button class="list-item" data-id="${c.contestId}">
          <div class="badge-row"><span class="${statusBadgeClass(phase)}">${phase}</span></div>
          <strong>${escapeHtml(c.contestTitle)}</strong>
          <div class="muted">${formatDateTime(c.startTime)} - ${formatDateTime(c.endTime)}</div>
        </button>`;
    })
    .join("");

  list.querySelectorAll("[data-id]").forEach((button) => {
    button.addEventListener("click", () => {
      selectedId = Number(button.dataset.id);
      renderDetail();
    });
  });

  await renderDetail();
})();
