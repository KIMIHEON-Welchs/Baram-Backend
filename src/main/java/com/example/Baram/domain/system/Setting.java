package com.example.Baram.domain.system;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Setting
{

    @Id
    @Column(length = 100)
    private String settingKey; // 예: "app_version", "is_maintenance"

    @Column(length = 255, nullable = false)
    private String settingValue; // 예: "1.0.0", "false"

    @Column(columnDefinition = "TEXT")
    private String description; // 설명
}