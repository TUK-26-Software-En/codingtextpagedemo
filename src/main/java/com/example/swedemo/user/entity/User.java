package com.example.swedemo.user.entity;

import com.example.swedemo.global.common.BaseEntity;
import com.example.swedemo.global.common.enums.Rank;
import com.example.swedemo.provider.entity.Provider;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id")
    private Provider provider;

    @Column(nullable = false)
    private String userName;

    private String userInfo;

    @Builder.Default
    private int userPoint = 0;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Rank userRank = Rank.BRONZE;

    public void update(String userName, String userInfo) {
        this.userName = userName;
        this.userInfo = userInfo;
    }

    public void addPoint(int point) {
        this.userPoint += point;
    }
}
