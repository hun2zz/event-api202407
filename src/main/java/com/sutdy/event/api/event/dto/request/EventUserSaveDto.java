package com.sutdy.event.api.event.dto.request;


import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventUserSaveDto {
    private String email;
    private String password;

}
