// REST API 클라이언트. 동일 출처(/api)로 호출하며 ApiResponse 를 언랩한다.
// JWT 토큰은 localStorage에 저장하고 변경계 요청에 Bearer 헤더로 부착한다.

const BASE = "/api";
const TOKEN_KEY = "authToken";
const ROLE_KEY = "authRole";
const SUBJECT_KEY = "authSubjectId";

export function getToken() {
  return localStorage.getItem(TOKEN_KEY);
}

export function isLoggedIn() {
  return Boolean(getToken());
}

export function currentUser() {
  if (!isLoggedIn()) return null;
  return {
    token: getToken(),
    role: localStorage.getItem(ROLE_KEY),
    subjectId: Number(localStorage.getItem(SUBJECT_KEY))
  };
}

async function request(method, path, body) {
  const headers = { "Content-Type": "application/json" };
  const token = getToken();
  if (token) headers["Authorization"] = "Bearer " + token;

  const res = await fetch(BASE + path, {
    method,
    headers,
    body: body != null ? JSON.stringify(body) : undefined
  });

  let json = null;
  try { json = await res.json(); } catch (_) { /* no body */ }

  if (!res.ok || (json && json.success === false)) {
    const error = new Error((json && json.message) || `요청 실패 (${res.status})`);
    error.status = res.status;
    error.code = json && json.code;
    throw error;
  }
  return json ? json.data : null;
}

export const getJSON = (path) => request("GET", path);
export const postJSON = (path, body) => request("POST", path, body);

export async function login(role, id) {
  const data = await postJSON("/auth/login", { role, id: Number(id) });
  localStorage.setItem(TOKEN_KEY, data.accessToken);
  localStorage.setItem(ROLE_KEY, data.role);
  localStorage.setItem(SUBJECT_KEY, String(data.subjectId));
  return data;
}

export function logout() {
  localStorage.removeItem(TOKEN_KEY);
  localStorage.removeItem(ROLE_KEY);
  localStorage.removeItem(SUBJECT_KEY);
}
