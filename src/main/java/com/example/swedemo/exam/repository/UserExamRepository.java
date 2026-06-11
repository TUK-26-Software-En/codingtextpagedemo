package com.example.swedemo.exam.repository;

import com.example.swedemo.exam.entity.Exam;
import com.example.swedemo.exam.entity.UserExam;
import com.example.swedemo.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserExamRepository extends JpaRepository<UserExam, Long> {
    boolean existsByExamAndUser(Exam exam, User user);
    List<UserExam> findByExamExamId(Long examId);
}
