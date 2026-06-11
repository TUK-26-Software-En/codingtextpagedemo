package com.example.swedemo.supervisor.service;

import com.example.swedemo.global.exception.BusinessException;
import com.example.swedemo.global.exception.ErrorCode;
import com.example.swedemo.provider.entity.Provider;
import com.example.swedemo.provider.service.ProviderService;
import com.example.swedemo.supervisor.dto.request.SupervisorCreateRequest;
import com.example.swedemo.supervisor.dto.response.SupervisorResponse;
import com.example.swedemo.supervisor.entity.Supervisor;
import com.example.swedemo.supervisor.repository.SupervisorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SupervisorService {

    private final SupervisorRepository supervisorRepository;
    private final ProviderService providerService;

    @Transactional
    public SupervisorResponse create(SupervisorCreateRequest request) {
        Provider provider = providerService.getById(request.getProviderId());
        Supervisor supervisor = Supervisor.builder()
                .provider(provider)
                .supervisorName(request.getSupervisorName())
                .build();
        return SupervisorResponse.from(supervisorRepository.save(supervisor));
    }

    public List<SupervisorResponse> findAll() {
        return supervisorRepository.findAll().stream()
                .map(SupervisorResponse::from)
                .collect(Collectors.toList());
    }

    public SupervisorResponse findById(Long id) {
        return SupervisorResponse.from(getById(id));
    }

    @Transactional
    public void delete(Long id) {
        supervisorRepository.delete(getById(id));
    }

    public Supervisor getById(Long id) {
        return supervisorRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.SUPERVISOR_NOT_FOUND));
    }
}
