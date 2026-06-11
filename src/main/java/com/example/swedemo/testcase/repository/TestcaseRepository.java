package com.example.swedemo.testcase.repository;

import com.example.swedemo.testcase.entity.Testcase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestcaseRepository extends JpaRepository<Testcase, Long> {
    List<Testcase> findByProblemProblemId(Long problemId);
}
