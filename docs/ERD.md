---

수정사항

submission에 유저가 제출한코드가 누락된걸 해결했습니다

<aside>
💡

초안임을 감안하고 읽어주세요

컬럼은 대략적인 구성으로 만들어졌습니다

이렇게 하고 기타 기획에 따라 내용이 바뀌면 좋을듯 합니다

SOLID도 어느정도 반영해두었습니다

</aside>

!image.png

```sql
table PROVIDER {
  provider_id int [pk]
  provider varchar
}

enum RANK {
  BRONZE
  SILVER
  GOLD
  PLATINUM
  DIAMOND
  RUBY
}

enum LANGUAGE {
  C
  CPP
  JAVA
  PYTHON
  JAVASCRIPT
  KOTLIN
}

enum SUBMISSION_STATUS {
  AC
  WA
  TLE
  MLE
  RE
  CE
}

enum EXAM_TYPE {
  CERTIFICATION
  COMPANY_TEST
  SCHOOL_TEST
}

table USER {
  user_id int [pk]
  provider_id int [ref: - PROVIDER.provider_id]
  user_name varchar
  user_info varchar
  user_point int
  user_rank RANK
}

table SUPERVISOR {
  supervisor_id int [pk]
  provider_id int [ref: - PROVIDER.provider_id]
  supervisor_name varchar
}

table ORGANIZATION {
  organization_id int [pk]
  organization_name varchar
  organization_description text
}

table CATEGORY {
  category_id int [pk]
  category_name varchar
}

table PROBLEM {
  problem_id int [pk]
  problem_title varchar
  problem_content text
  problem_grade RANK
  problem_point int
  problem_language LANGUAGE
}

table TESTCASE {
  testcase_id int [pk]
  problem_id int [ref: > PROBLEM.problem_id]
  input_data text
  output_data text
}

table PROBLEM_CATEGORY {
  problem_category_id int [pk]
  problem_id int [ref: > PROBLEM.problem_id]
  category_id int [ref: > CATEGORY.category_id]
}

table SUBMISSION {
  submission_id int [pk]
  user_id int [ref: > USER.user_id]
  problem_id int [ref: > PROBLEM.problem_id]

  submission_language LANGUAGE

  submitted_code text

  submission_status SUBMISSION_STATUS
  submission_score int

  submitted_at datetime
}

table USER_PROBLEM {
  user_problem_id int [pk]
  user_id int [ref: > USER.user_id]
  problem_id int [ref: > PROBLEM.problem_id]
  user_problem_status SUBMISSION_STATUS
  solved_at datetime
}

table SOLUTION {
  solution_id int [pk]
  problem_id int [ref: > PROBLEM.problem_id]
  user_id int [ref: > USER.user_id]
  solution_title varchar
  solution_content text
}

table CONTEST {
  contest_id int [pk]
  contest_title varchar
  contest_description text
  start_time datetime
  end_time datetime
}

table CONTEST_PROBLEM {
  contest_problem_id int [pk]
  contest_id int [ref: > CONTEST.contest_id]
  problem_id int [ref: > PROBLEM.problem_id]
  problem_order int
  contest_problem_score int
}

table USER_CONTEST {
  user_contest_id int [pk]
  contest_id int [ref: > CONTEST.contest_id]
  user_id int [ref: > USER.user_id]
  joined_at datetime
  total_score int
  rank int
  solved_count int
}

table CONTEST_SUPERVISOR {
  contest_supervisor_id int [pk]
  contest_id int [ref: > CONTEST.contest_id]
  supervisor_id int [ref: > SUPERVISOR.supervisor_id]
}

table EXAM {
  exam_id int [pk]
  exam_title varchar
  exam_description text
  exam_type EXAM_TYPE
  start_time datetime
  end_time datetime
}

table EXAM_PROBLEM {
  exam_problem_id int [pk]
  exam_id int [ref: > EXAM.exam_id]
  problem_id int [ref: > PROBLEM.problem_id]
  problem_order int
  exam_problem_score int
}

table USER_EXAM {
  user_exam_id int [pk]
  exam_id int [ref: > EXAM.exam_id]
  user_id int [ref: > USER.user_id]
  joined_at datetime
  total_score int
  pass_status boolean
  exam_grade varchar
}

table EXAM_SUPERVISOR {
  exam_supervisor_id int [pk]
  exam_id int [ref: > EXAM.exam_id]
  supervisor_id int [ref: > SUPERVISOR.supervisor_id]
}

table ORGANIZATION_PROBLEM {
  organization_problem_id int [pk]
  organization_id int [ref: > ORGANIZATION.organization_id]
  problem_id int [ref: > PROBLEM.problem_id]
}

table ORGANIZATION_CONTEST {
  organization_contest_id int [pk]
  organization_id int [ref: > ORGANIZATION.organization_id]
  contest_id int [ref: > CONTEST.contest_id]
}

table ORGANIZATION_EXAM {
  organization_exam_id int [pk]
  organization_id int [ref: > ORGANIZATION.organization_id]
  exam_id int [ref: > EXAM.exam_id]
}

```

---

# 유저,감독(계정)

!image.png

소셜 로그인을 둬서 보안성을 유지합니다(구글 로그인 예상)

문제를 풀고, 대회에 참가하고, 시험을 보는 USER와 

대회,시험의 감독,관리 권한을 가지는 SUPERVISOR를 분리하여 SRP를 만족시키고자 합니다

---

# 문제

!image.png

problem을 기점으로, 유저와 해당 문제의 관계 (해결,미해결등)를 표기하는 유저-문제, 출제기관과의 관계를 보여주는 기관-문제 등의 관계를 가집니다

사용자의 문제 제출 기록을 SUBMISSION으로 구분지어 놓음으로써 SRP를 만족시킵니다

백준의 질의응답을 참고하여, 사용자가 해답을 등록할 수 있게 기획하였습니다

카테고리를 외부에두고, 카테고리 - 문제를 N:M으로 둬서 OCP를 만족시킵니다

이 외에도 대회,시험의 문제들을 N:M 중간테이블을 통해 확장시킴으로써 OCP를 만족시킵니다

---

# 대회

!image.png

대회-유저 테이블과 대회-감독 테이블,주최기관 - 문제를 N:M 중간테이블로 두었습니다 이역시 OCP를 만족시킵니다.

# 시험

!image.png

 시험 또한 비슷한 구조를 가집니다