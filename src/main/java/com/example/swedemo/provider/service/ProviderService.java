package com.example.swedemo.provider.service;

import com.example.swedemo.global.exception.BusinessException;
import com.example.swedemo.global.exception.ErrorCode;
import com.example.swedemo.provider.dto.request.ProviderCreateRequest;
import com.example.swedemo.provider.dto.response.ProviderResponse;
import com.example.swedemo.provider.entity.Provider;
import com.example.swedemo.provider.repository.ProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProviderService {

    private final ProviderRepository providerRepository;

    @Transactional
    public ProviderResponse create(ProviderCreateRequest request) {
        Provider provider = Provider.builder()
                .provider(request.getProvider())
                .build();
        return ProviderResponse.from(providerRepository.save(provider));
    }

    public List<ProviderResponse> findAll() {
        return providerRepository.findAll().stream()
                .map(ProviderResponse::from)
                .collect(Collectors.toList());
    }

    public ProviderResponse findById(Long id) {
        return ProviderResponse.from(getById(id));
    }

    public Provider getById(Long id) {
        return providerRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.PROVIDER_NOT_FOUND));
    }
}
