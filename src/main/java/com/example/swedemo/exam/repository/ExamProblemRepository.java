package com.example.swedemo.exam.repository;

import com.example.swedemo.exam.entity.ExamProblem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamProblemRepository extends JpaRepository<ExamProblem, Long> {
    List<ExamProblem> findByExamExamId(Long examId);
}
