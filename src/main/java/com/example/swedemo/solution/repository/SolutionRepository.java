package com.example.swedemo.solution.repository;

import com.example.swedemo.solution.entity.Solution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolutionRepository extends JpaRepository<Solution, Long> {
    List<Solution> findByProblemProblemId(Long problemId);
}
