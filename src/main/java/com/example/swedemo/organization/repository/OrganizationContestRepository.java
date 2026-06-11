package com.example.swedemo.organization.repository;

import com.example.swedemo.organization.entity.OrganizationContest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationContestRepository extends JpaRepository<OrganizationContest, Long> {
}
