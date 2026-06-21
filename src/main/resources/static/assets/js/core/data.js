export const providers = [
  { providerId: 1, provider: "GOOGLE" },
  { providerId: 2, provider: "KAKAO" },
  { providerId: 3, provider: "GITHUB" }
];

export const users = [
  { userId: 1, providerId: 1, providerName: "GOOGLE", userName: "gunwoo", userInfo: "알고리즘과 백엔드를 같이 보는 사용자", userPoint: 850, userRank: "GOLD" },
  { userId: 2, providerId: 2, providerName: "KAKAO", userName: "minseo", userInfo: "대회 참가 위주 사용자", userPoint: 720, userRank: "SILVER" },
  { userId: 3, providerId: 3, providerName: "GITHUB", userName: "jiho", userInfo: "시험 준비 중", userPoint: 410, userRank: "BRONZE" }
];

export const supervisors = [
  { supervisorId: 1, providerName: "GOOGLE", supervisorName: "김교수" },
  { supervisorId: 2, providerName: "KAKAO", supervisorName: "박감독" }
];

export const organizations = [
  { organizationId: 1, organizationName: "서울대학교", organizationDescription: "교내 대회 및 시험 운영 기관" },
  { organizationId: 2, organizationName: "Acme Tech", organizationDescription: "채용형 코딩 테스트 운영 기관" }
];

export const categories = [
  { categoryId: 1, categoryName: "구현" },
  { categoryId: 2, categoryName: "기초" },
  { categoryId: 3, categoryName: "자료구조" },
  { categoryId: 4, categoryName: "시뮬레이션" },
  { categoryId: 5, categoryName: "그래프" },
  { categoryId: 6, categoryName: "다익스트라" }
];

export const problems = [
  {
    problemId: 1001,
    problemTitle: "두 수의 합",
    problemContent: "정수 A와 B가 주어진다. 두 수의 합을 출력하는 프로그램을 작성하시오.",
    problemGrade: "BRONZE",
    problemPoint: 100,
    problemLanguage: "JAVA",
    acceptanceRate: "68.4%",
    categoryIds: [1, 2],
    statement: ["정수 A와 B가 주어진다.", "두 수의 합을 출력하는 프로그램을 작성하시오."],
    inputDescription: "첫째 줄에 A와 B가 공백으로 구분되어 주어진다.",
    outputDescription: "A+B를 출력한다.",
    exampleInput: "1 2",
    exampleOutput: "3"
  },
  {
    problemId: 1024,
    problemTitle: "배열 회전",
    problemContent: "N개의 정수 배열을 오른쪽으로 K번 회전시킨 결과를 출력하시오.",
    problemGrade: "SILVER",
    problemPoint: 250,
    problemLanguage: "PYTHON",
    acceptanceRate: "43.1%",
    categoryIds: [3, 4],
    statement: ["N개의 정수로 구성된 배열이 있다.", "배열을 오른쪽으로 K번 회전시킨 결과를 출력하시오."],
    inputDescription: "첫째 줄에 N과 K가 주어진다. 둘째 줄에 배열 원소가 주어진다.",
    outputDescription: "회전된 배열의 상태를 공백으로 구분하여 출력한다.",
    exampleInput: "5 2\n1 2 3 4 5",
    exampleOutput: "4 5 1 2 3"
  },
  {
    problemId: 1149,
    problemTitle: "최단 경로 연습",
    problemContent: "시작 정점에서 각 정점까지의 최단 거리를 구하시오.",
    problemGrade: "GOLD",
    problemPoint: 500,
    problemLanguage: "CPP",
    acceptanceRate: "31.8%",
    categoryIds: [5, 6],
    statement: ["방향 그래프가 주어진다.", "시작 정점에서 각 정점까지의 최단 거리를 구하시오."],
    inputDescription: "첫째 줄에 정점 수 V와 간선 수 E가 주어진다. 둘째 줄에 시작 정점이 주어진다.",
    outputDescription: "각 정점까지의 최단 거리를 한 줄에 하나씩 출력한다.",
    exampleInput: "5 6\n1\n1 2 2\n1 3 5",
    exampleOutput: "0\n2\n5\nINF\nINF"
  }
];

export const testcases = [
  { testcaseId: 1, problemId: 1001, inputData: "1 2", outputData: "3" },
  { testcaseId: 2, problemId: 1001, inputData: "30 12", outputData: "42" },
  { testcaseId: 3, problemId: 1024, inputData: "5 2\n1 2 3 4 5", outputData: "4 5 1 2 3" },
  { testcaseId: 4, problemId: 1149, inputData: "5 6\n1\n1 2 2\n1 3 5", outputData: "0\n2\n5\nINF\nINF" }
];

export const submissions = [
  { submissionId: 50121, userId: 1, problemId: 1001, submissionLanguage: "JAVA", submittedCode: "public class Main {\\n    public static void main(String[] args) {\\n        java.util.Scanner sc = new java.util.Scanner(System.in);\\n        int a = sc.nextInt();\\n        int b = sc.nextInt();\\n        System.out.println(a + b);\\n    }\\n}", submissionStatus: "AC", submissionScore: 100, submittedAt: "2026-06-20 18:20" },
  { submissionId: 50122, userId: 1, problemId: 1024, submissionLanguage: "PYTHON", submittedCode: "n, k = map(int, input().split())\\narr = list(map(int, input().split()))\\nprint(*arr)", submissionStatus: "WA", submissionScore: 20, submittedAt: "2026-06-20 18:43" },
  { submissionId: 50123, userId: 1, problemId: 1149, submissionLanguage: "CPP", submittedCode: "#include <bits/stdc++.h>\\nusing namespace std;\\nint main() {\\n    cout << 0 << '\\\\n';\\n}", submissionStatus: "AC", submissionScore: 100, submittedAt: "2026-06-20 19:10" }
];

export const solutions = [
  { solutionId: 1, problemId: 1001, userId: 1, userName: "gunwoo", solutionTitle: "입력만 받아서 바로 더하기", solutionContent: "Scanner 또는 BufferedReader로 입력을 받은 뒤 두 수를 더해 출력하면 된다." },
  { solutionId: 2, problemId: 1149, userId: 2, userName: "minseo", solutionTitle: "다익스트라 기본 구현", solutionContent: "우선순위 큐를 사용해 최소 거리 정점을 반복적으로 꺼내며 relax를 수행한다." }
];

export const contests = [
  {
    contestId: 1,
    contestTitle: "2026 Spring Mock Contest",
    contestDescription: "학기 말 대비 모의 알고리즘 대회",
    startTime: "2026-06-28 13:00",
    endTime: "2026-06-28 15:00",
    status: "참가 가능",
    participants: 42,
    problemConfigs: [
      { problemId: 1001, problemOrder: 1, contestProblemScore: 100 },
      { problemId: 1024, problemOrder: 2, contestProblemScore: 200 },
      { problemId: 1149, problemOrder: 3, contestProblemScore: 300 }
    ],
    participantResults: [
      { userContestId: 1, contestId: 1, userId: 1, userName: "gunwoo", joinedAt: "2026-06-18 09:00", totalScore: 600, rank: 1, solvedCount: 3 },
      { userContestId: 2, contestId: 1, userId: 2, userName: "minseo", joinedAt: "2026-06-18 09:12", totalScore: 520, rank: 2, solvedCount: 3 },
      { userContestId: 3, contestId: 1, userId: 3, userName: "jiho", joinedAt: "2026-06-18 09:34", totalScore: 410, rank: 3, solvedCount: 2 }
    ]
  },
  {
    contestId: 2,
    contestTitle: "Campus Open Challenge",
    contestDescription: "교내 공개 연습 대회",
    startTime: "2026-07-04 10:00",
    endTime: "2026-07-04 12:00",
    status: "준비 중",
    participants: 18,
    problemConfigs: [
      { problemId: 1001, problemOrder: 1, contestProblemScore: 100 },
      { problemId: 1024, problemOrder: 2, contestProblemScore: 200 }
    ],
    participantResults: [
      { userContestId: 4, contestId: 2, userId: 2, userName: "minseo", joinedAt: "2026-06-20 11:00", totalScore: 0, rank: 0, solvedCount: 0 }
    ]
  }
];

export const exams = [
  {
    examId: 1,
    examTitle: "Backend Screening Test",
    examDescription: "채용형 백엔드 필기/코딩 혼합 시험",
    examType: "COMPANY_TEST",
    startTime: "2026-07-12 14:00",
    endTime: "2026-07-12 16:00",
    status: "응시 예정",
    problemConfigs: [
      { problemId: 1001, problemOrder: 1, examProblemScore: 20 },
      { problemId: 1024, problemOrder: 2, examProblemScore: 30 }
    ],
    results: [
      { userExamId: 1, examId: 1, userId: 1, userName: "gunwoo", joinedAt: "2026-07-12 13:55", totalScore: 86, passStatus: true, examGrade: "PASS" },
      { userExamId: 2, examId: 1, userId: 2, userName: "minseo", joinedAt: "2026-07-12 13:58", totalScore: 78, passStatus: true, examGrade: "PASS" },
      { userExamId: 3, examId: 1, userId: 3, userName: "jiho", joinedAt: "2026-07-12 14:01", totalScore: 54, passStatus: false, examGrade: "FAIL" }
    ]
  },
  {
    examId: 2,
    examTitle: "자료구조 인증 시험",
    examDescription: "기초 자료구조 이해도 평가 시험",
    examType: "CERTIFICATION",
    startTime: "2026-07-19 09:00",
    endTime: "2026-07-19 11:00",
    status: "준비 중",
    problemConfigs: [
      { problemId: 1001, problemOrder: 1, examProblemScore: 20 }
    ],
    results: [
      { userExamId: 4, examId: 2, userId: 1, userName: "gunwoo", joinedAt: "2026-07-19 08:50", totalScore: 82, passStatus: true, examGrade: "PASS" }
    ]
  }
];

export const notices = [
  { title: "6월 28일 모의 대회 오픈", date: "2026-06-21" },
  { title: "제출 엔진 점검 예정", date: "2026-06-23" },
  { title: "기관용 시험 템플릿 추가", date: "2026-06-24" }
];

export const operations = {
  organization: [
    { name: "문제 출제", state: "활성" },
    { name: "대회 개설", state: "활성" },
    { name: "시험 개설", state: "활성" },
    { name: "기관 문제 등록", state: "활성" }
  ],
  supervisor: [
    { name: "대회 문제 추가", state: "활성" },
    { name: "대회 감독자 배정", state: "활성" },
    { name: "시험 문제 추가", state: "활성" },
    { name: "시험 결과 목록 조회", state: "활성" }
  ]
};
