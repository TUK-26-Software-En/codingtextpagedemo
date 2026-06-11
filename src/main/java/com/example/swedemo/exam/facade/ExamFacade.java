package com.example.swedemo.exam.facade;

import com.example.swedemo.exam.dto.request.ExamTakeRequest;
import com.example.swedemo.exam.dto.response.UserExamResponse;
import com.example.swedemo.exam.entity.Exam;
import com.example.swedemo.exam.entity.UserExam;
import com.example.swedemo.exam.repository.UserExamRepository;
import com.example.swedemo.exam.service.ExamService;
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
public class ExamFacade {

    private final ExamService examService;
    private final UserService userService;
    private final UserExamRepository userExamRepository;

    @Transactional
    public UserExamResponse takeExam(Long examId, ExamTakeRequest request) {
        Exam exam = examService.getById(examId);
        User user = userService.getById(request.getUserId());

        if (userExamRepository.existsByExamAndUser(exam, user)) {
            throw new BusinessException(ErrorCode.DUPLICATE_PARTICIPATION);
        }

        UserExam userExam = UserExam.builder()
                .exam(exam)
                .user(user)
                .joinedAt(LocalDateTime.now())
                .build();

        return UserExamResponse.from(userExamRepository.save(userExam));
    }

    @Transactional(readOnly = true)
    public List<UserExamResponse> getResults(Long examId) {
        return userExamRepository.findByExamExamId(examId).stream()
                .map(UserExamResponse::from)
                .collect(Collectors.toList());
    }
}
