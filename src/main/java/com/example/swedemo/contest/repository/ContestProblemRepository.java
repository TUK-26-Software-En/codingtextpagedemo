package com.example.swedemo.contest.repository;

import com.example.swedemo.contest.entity.ContestProblem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContestProblemRepository extends JpaRepository<ContestProblem, Long> {
    List<ContestProblem> findByContestContestId(Long contestId);
}
