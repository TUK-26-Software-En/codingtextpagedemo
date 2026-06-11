package com.example.swedemo.problem.repository;

import com.example.swedemo.problem.entity.ProblemCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProblemCategoryRepository extends JpaRepository<ProblemCategory, Long> {
    List<ProblemCategory> findByProblemProblemId(Long problemId);
}
