package com.example.swedemo.user.service;

import com.example.swedemo.global.exception.BusinessException;
import com.example.swedemo.global.exception.ErrorCode;
import com.example.swedemo.provider.entity.Provider;
import com.example.swedemo.provider.service.ProviderService;
import com.example.swedemo.user.dto.request.UserCreateRequest;
import com.example.swedemo.user.dto.request.UserUpdateRequest;
import com.example.swedemo.user.dto.response.UserResponse;
import com.example.swedemo.user.entity.User;
import com.example.swedemo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final ProviderService providerService;

    @Transactional
    public UserResponse create(UserCreateRequest request) {
        Provider provider = providerService.getById(request.getProviderId());
        User user = User.builder()
                .provider(provider)
                .userName(request.getUserName())
                .userInfo(request.getUserInfo())
                .build();
        return UserResponse.from(userRepository.save(user));
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(UserResponse::from)
                .collect(Collectors.toList());
    }

    public UserResponse findById(Long id) {
        return UserResponse.from(getById(id));
    }

    @Transactional
    public UserResponse update(Long id, UserUpdateRequest request) {
        User user = getById(id);
        user.update(request.getUserName(), request.getUserInfo());
        return UserResponse.from(user);
    }

    @Transactional
    public void delete(Long id) {
        userRepository.delete(getById(id));
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));
    }
}
