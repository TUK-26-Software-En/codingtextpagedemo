package com.example.swedemo.problem.entity;

import com.example.swedemo.global.common.BaseEntity;
import com.example.swedemo.global.common.enums.Language;
import com.example.swedemo.global.common.enums.Rank;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "problem")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Problem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long problemId;

    @Column(nullable = false)
    private String problemTitle;

    @Column(columnDefinition = "TEXT")
    private String problemContent;

    @Enumerated(EnumType.STRING)
    private Rank problemGrade;

    private int problemPoint;

    @Enumerated(EnumType.STRING)
    private Language problemLanguage;

    public void update(String problemTitle, String problemContent, Rank problemGrade,
                       int problemPoint, Language problemLanguage) {
        this.problemTitle = problemTitle;
        this.problemContent = problemContent;
        this.problemGrade = problemGrade;
        this.problemPoint = problemPoint;
        this.problemLanguage = problemLanguage;
    }
}
