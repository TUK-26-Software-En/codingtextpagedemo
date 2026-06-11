package com.example.swedemo.supervisor.repository;

import com.example.swedemo.supervisor.entity.Supervisor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupervisorRepository extends JpaRepository<Supervisor, Long> {
}
