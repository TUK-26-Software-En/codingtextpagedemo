package com.example.swedemo.provider.repository;

import com.example.swedemo.provider.entity.Provider;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProviderRepository extends JpaRepository<Provider, Long> {
}
