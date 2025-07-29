package com.example.personal_finance_tracker.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterDTO {
    private String email;
    private String password;
    private String fullName;
}
