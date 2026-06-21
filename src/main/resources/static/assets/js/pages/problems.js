import { createPaths, renderShell } from "../core/ui.js";
import { getJSON } from "../core/api.js";

renderShell("problems", "../..");
const paths = createPaths("../..");

const difficultyFilter = document.getElementById("difficulty-filter");
const languageFilter = document.getElementById("language-filter");
const keywordFilter = document.getElementById("keyword-filter");
const countLabel = document.getElementById("problem-count");
const tbody = document.getElementById("problem-table-body");

let problems = [];

function renderTable() {
  const difficulty = difficultyFilter.value;
  const language = languageFilter.value;
  const keyword = keywordFilter.value.trim().toLowerCase();

  const filtered = problems.filter((problem) => {
    const difficultyMatch = difficulty === "ALL" || problem.problemGrade === difficulty;
    const languageMatch = language === "ALL" || problem.problemLanguage === language;
    const keywordMatch = !keyword || problem.problemTitle.toLowerCase().includes(keyword);
    return difficultyMatch && languageMatch && keywordMatch;
  });

  countLabel.textContent = `${filtered.length}개`;
  tbody.innerHTML = filtered
    .map(
      (problem) => `
        <tr>
          <td>${problem.problemId}</td>
          <td><a class="problem-link" href="${paths.problem(problem.problemId)}">${problem.problemTitle}</a></td>
          <td>${problem.problemGrade ?? "-"}</td>
          <td>${problem.problemLanguage ?? "-"}</td>
          <td>${problem.problemPoint}</td>
          <td>-</td>
          <td>-</td>
        </tr>
      `
    )
    .join("");
}

(async () => {
  try {
    // GET /api/problems
    problems = await getJSON("/problems");
  } catch (e) {
    tbody.innerHTML = `<tr><td colspan="7">문제를 불러오지 못했습니다: ${e.message}</td></tr>`;
    return;
  }

  const difficulties = ["ALL", ...new Set(problems.map((p) => p.problemGrade).filter(Boolean))];
  const languages = ["ALL", ...new Set(problems.map((p) => p.problemLanguage).filter(Boolean))];
  difficultyFilter.innerHTML = difficulties.map((v) => `<option value="${v}">${v}</option>`).join("");
  languageFilter.innerHTML = languages.map((v) => `<option value="${v}">${v}</option>`).join("");

  renderTable();
  difficultyFilter.addEventListener("change", renderTable);
  languageFilter.addEventListener("change", renderTable);
  keywordFilter.addEventListener("input", renderTable);
})();
