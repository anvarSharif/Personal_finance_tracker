package com.example.personal_finance_tracker.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterResponseDTO {
    private Long id;
    private String email;
    private String fullName;
}
