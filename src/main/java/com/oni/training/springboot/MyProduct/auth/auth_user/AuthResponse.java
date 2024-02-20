package com.oni.training.springboot.MyProduct.auth.auth_user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    @Schema(description = "The JWT token.", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJxdzI4NDI1MzgyNjk0QGdtYWlsLmNvbSIsImp0aSI6IjY1ZDM4NDlhMDI2N2RiNzNiMTRiNGY1NiIsImlhdCI6MTcwODM2MDg1OCwiZXhwIjoxNzA4NDQ3MjU4fQ.50AHReM6R3-eG5WeYLqwHrENUXR4YTt13akOTDkR8PM")
    private String token;
    @Schema(description = "The user's real name.", example = "oni")
    private String name;

}
