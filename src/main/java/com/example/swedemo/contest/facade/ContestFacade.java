package com.example.swedemo.contest.facade;

import com.example.swedemo.contest.dto.request.ContestJoinRequest;
import com.example.swedemo.contest.dto.response.UserContestResponse;
import com.example.swedemo.contest.entity.Contest;
import com.example.swedemo.contest.entity.UserContest;
import com.example.swedemo.contest.repository.UserContestRepository;
import com.example.swedemo.contest.service.ContestService;
import com.example.swedemo.global.exception.BusinessException;
import com.example.swedemo.global.exception.ErrorCode;
import com.example.swedemo.user.entity.User;
import com.example.swedemo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContestFacade {

    private final ContestService contestService;
    private final UserService userService;
    private final UserContestRepository userContestRepository;

    @Transactional
    public UserContestResponse joinContest(Long contestId, ContestJoinRequest request) {
        Contest contest = contestService.getById(contestId);
        User user = userService.getById(request.getUserId());

        if (userContestRepository.existsByContestAndUser(contest, user)) {
            throw new BusinessException(ErrorCode.DUPLICATE_PARTICIPATION);
        }

        UserContest userContest = UserContest.builder()
                .contest(contest)
                .user(user)
                .joinedAt(LocalDateTime.now())
                .build();

        return UserContestResponse.from(userContestRepository.save(userContest));
    }

    @Transactional(readOnly = true)
    public List<UserContestResponse> getParticipants(Long contestId) {
        return userContestRepository.findByContestContestId(contestId).stream()
                .map(UserContestResponse::from)
                .collect(Collectors.toList());
    }
}
