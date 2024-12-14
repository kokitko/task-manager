package com.project.task_manager.util;

import com.project.task_manager.entity.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JwtUtilsTest {
    @Autowired
    private JwtUtils jwtUtils;
    private final String username = "testuser";
    private final Role role = new Role(1L, "testrole");

    @Test
    public void JwtUtils_GenerateToken_ReturnsString() {
        String result = jwtUtils.generateToken(username, role);
        Assertions.assertThat(jwtUtils.extractUsername(result)).isEqualTo(username);
    }

    @Test
    public void JwtUtils_ExtractUsername_ReturnsString() {
        String token = jwtUtils.generateToken(username, role);
        String result = jwtUtils.extractUsername(token);
        Assertions.assertThat(result).isEqualTo(username);
    }

    @Test
    public void JwtUtils_ValidateToken_ReturnsBoolean() {
        String token = jwtUtils.generateToken(username, role);
        boolean result = jwtUtils.validateToken(token);
        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void JwtUtils_ExtractRole_ReturnsString() {
        String token = jwtUtils.generateToken(username, role);
        String result = jwtUtils.extractRole(token);
        Assertions.assertThat(result).isEqualTo(role.getAuthority());
    }
}
