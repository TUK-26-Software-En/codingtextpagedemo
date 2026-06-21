package com.example.swedemo.contest.repository;

import com.example.swedemo.contest.entity.Contest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ContestRepository extends JpaRepository<Contest, Long> {

    /** 종료 시각이 지났으나 아직 마감(집계)되지 않은 대회. */
    List<Contest> findByEndTimeBeforeAndClosedFalse(LocalDateTime time);
}
