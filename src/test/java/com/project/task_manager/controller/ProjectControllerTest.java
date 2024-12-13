package com.project.task_manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.task_manager.dto.ProjectRequestDto;
import com.project.task_manager.dto.ProjectResponseDto;
import com.project.task_manager.entity.UserEntity;
import com.project.task_manager.repository.UserRepository;
import com.project.task_manager.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private ProjectResponseDto projectResponseDto;
    private ProjectRequestDto projectRequestDto;
    private UserEntity user;

    @BeforeEach
    public void init() {
        user = new UserEntity();
        user.setId(1L);
        user.setUsername("testuser");

        projectResponseDto = new ProjectResponseDto();
        projectResponseDto.setId(1L);
        projectResponseDto.setName("testprojectresponse");

        projectRequestDto = new ProjectRequestDto();
        projectRequestDto.setName("testprojectrequest");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(projectService.createProject(any(ProjectRequestDto.class), any(UserEntity.class)))
                .thenReturn(projectResponseDto);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("testuser", null)
        );
    }

    @Test
    public void ProjectController_CreateProject_ReturnsProjectResponseDto() throws Exception {
        when(projectService.createProject(any(ProjectRequestDto.class), any(UserEntity.class)))
                .thenReturn(projectResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(projectResponseDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(projectResponseDto.getName()));
    }

    @Test
    public void ProjectController_GetUserProjects_ReturnsProjectResponseDtoList() throws Exception {
        when(projectService.getProjectsByUser(any(UserEntity.class)))
                .thenReturn(List.of(projectResponseDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(projectResponseDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(projectResponseDto.getName()));
    }

    @Test
    public void ProjectController_UpdateProject_ReturnsProjectResponseDto() throws Exception {
        when(projectService.updateProject(Mockito.anyLong(), Mockito.any(ProjectRequestDto.class)))
                .thenReturn(projectResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/projects/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(projectRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(projectResponseDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(projectResponseDto.getName()));
    }

    @Test
    public void ProjectController_DeleteProject_ReturnsString() throws Exception {
        doNothing().when(projectService).deleteProject(Mockito.anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/projects/1")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}