package com.example.swedemo.exam.repository;

import com.example.swedemo.exam.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ExamRepository extends JpaRepository<Exam, Long> {

    /** 종료 시각이 지났으나 아직 마감(집계)되지 않은 시험. */
    List<Exam> findByEndTimeBeforeAndClosedFalse(LocalDateTime time);
}
