import { renderShell, statusBadgeClass, escapeHtml, formatDateTime, derivePhase } from "../core/ui.js";
import { getJSON } from "../core/api.js";

renderShell("exams", "../..");

const list = document.getElementById("exam-list");
const detail = document.getElementById("exam-detail");

let exams = [];
let selectedId = null;

async function renderDetail() {
  const exam = exams.find((e) => e.examId === selectedId);
  if (!exam) {
    detail.innerHTML = `<p class="muted">시험이 없습니다.</p>`;
    return;
  }
  const phase = derivePhase(exam.startTime, exam.endTime, exam.closed);
  let results = [];
  try {
    results = await getJSON(`/exams/${exam.examId}/results`);
  } catch (_) { /* ignore */ }

  detail.innerHTML = `
    <div class="statement-block">
      <h3>${escapeHtml(exam.examTitle)}</h3>
      <div class="badge-row">
        <span class="badge badge-blue">${exam.examType ?? "-"}</span>
        <span class="${statusBadgeClass(phase)}">${phase}</span>
        <span class="badge">${formatDateTime(exam.startTime)} - ${formatDateTime(exam.endTime)}</span>
      </div>
      <p>${escapeHtml(exam.examDescription)}</p>
    </div>
    <div class="statement-block">
      <h3>결과</h3>
      <div class="table-wrap">
        <table class="table">
          <thead><tr><th>응시 ID</th><th>User</th><th>응시 시각</th><th>총점</th><th>합격</th><th>등급</th></tr></thead>
          <tbody>
            ${results
              .map((r) => `<tr><td>${r.userExamId}</td><td>${escapeHtml(r.userName)} (#${r.userId})</td><td>${formatDateTime(r.joinedAt)}</td><td>${r.totalScore}</td><td><span class="${statusBadgeClass(r.passStatus)}">${r.passStatus ? "PASS" : "FAIL"}</span></td><td>${r.examGrade ?? "-"}</td></tr>`)
              .join("") || `<tr><td colspan="6" class="muted">응시 결과 없음</td></tr>`}
          </tbody>
        </table>
      </div>
    </div>`;
}

(async () => {
  try {
    exams = await getJSON("/exams");
  } catch (e) {
    list.innerHTML = `<p>불러오기 실패: ${e.message}</p>`;
    return;
  }
  if (!exams.length) {
    list.innerHTML = `<p class="muted">시험 없음</p>`;
    detail.innerHTML = `<p class="muted">시험 없음</p>`;
    return;
  }
  selectedId = exams[0].examId;

  list.innerHTML = exams
    .map((e) => {
      const phase = derivePhase(e.startTime, e.endTime, e.closed);
      return `
        <button class="list-item" data-id="${e.examId}">
          <div class="badge-row"><span class="badge badge-blue">${e.examType ?? "-"}</span><span class="${statusBadgeClass(phase)}">${phase}</span></div>
          <strong>${escapeHtml(e.examTitle)}</strong>
          <div class="muted">${formatDateTime(e.startTime)} - ${formatDateTime(e.endTime)}</div>
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
