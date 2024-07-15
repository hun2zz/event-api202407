package com.sutdy.event.api.event.dto.response;


import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {


    private String email;

    private String role;

    private String token;
}
