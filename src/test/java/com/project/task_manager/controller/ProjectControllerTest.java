package com.project.task_manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.task_manager.dto.ProjectRequestDto;
import com.project.task_manager.dto.ProjectResponseDto;
import com.project.task_manager.entity.Project;
import com.project.task_manager.entity.UserEntity;
import com.project.task_manager.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ProjectController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class ProjectControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProjectService projectService;
    @Autowired
    private ObjectMapper objectMapper;

    private static final String URL = "/api/projects";
    private UserEntity user = new UserEntity();
    private Project project = new Project();
    private ProjectResponseDto projectResponseDto = new ProjectResponseDto();
    private ProjectRequestDto projectRequestDto = new ProjectRequestDto();

    @BeforeEach
    public void init() {
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("testpassword");
        user.setEmail("testemail");

        project.setId(1L);
        project.setName("testproject");
        project.setDescription("testdescription");
        project.setUser(user);

        projectResponseDto.setId(1L);
        projectResponseDto.setName("testprojectresponse");
        projectResponseDto.setDescription("testdescriptionresponse");

        projectRequestDto.setName("testprojectrequest");
        projectRequestDto.setDescription("testdescriptionrequest");
    }
}
