package com.example.swedemo.auth.service;

import com.example.swedemo.auth.AuthRole;
import com.example.swedemo.auth.JwtProvider;
import com.example.swedemo.auth.dto.LoginRequest;
import com.example.swedemo.auth.dto.TokenResponse;
import com.example.swedemo.supervisor.service.SupervisorService;
import com.example.swedemo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 개발용 로그인 서비스. role에 맞는 도메인 서비스로 식별자 존재를 검증한 뒤 JWT를 발급한다.
 * (자격 증명 검증은 하지 않음 — 데모용. 운영은 실제 OAuth2/자격검증 필요.)
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final SupervisorService supervisorService;
    private final JwtProvider jwtProvider;

    public TokenResponse login(LoginRequest request) {
        AuthRole role = request.getRole();
        Long subjectId = (role == AuthRole.USER)
                ? userService.getById(request.getId()).getUserId()
                : supervisorService.getById(request.getId()).getSupervisorId();

        String token = jwtProvider.issue(subjectId, role);
        return new TokenResponse(token, role.name(), subjectId);
    }
}
