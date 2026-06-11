package com.example.swedemo.contest.repository;

import com.example.swedemo.contest.entity.Contest;
import com.example.swedemo.contest.entity.UserContest;
import com.example.swedemo.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserContestRepository extends JpaRepository<UserContest, Long> {
    boolean existsByContestAndUser(Contest contest, User user);
    List<UserContest> findByContestContestId(Long contestId);
}
