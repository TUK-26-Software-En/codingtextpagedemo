package com.example.swedemo.user.repository;

import com.example.swedemo.problem.entity.Problem;
import com.example.swedemo.user.entity.User;
import com.example.swedemo.user.entity.UserProblem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProblemRepository extends JpaRepository<UserProblem, Long> {
    Optional<UserProblem> findByUserAndProblem(User user, Problem problem);
}
