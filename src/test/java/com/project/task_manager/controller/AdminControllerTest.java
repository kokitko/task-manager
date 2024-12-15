package com.project.task_manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.task_manager.dto.ProjectRequestDto;
import com.project.task_manager.dto.ProjectResponseDto;
import com.project.task_manager.entity.UserEntity;
import com.project.task_manager.repository.UserRepository;
import com.project.task_manager.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AdminService adminService;
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;

    private UserEntity user;
    private ProjectRequestDto projectRequestDto;
    private ProjectResponseDto projectResponseDto;

    @BeforeEach
    public void init() {
        user = new UserEntity();
        user.setId(1L);
        user.setUsername("user");
        user.setPassword("password");

        projectRequestDto = new ProjectRequestDto();
        projectRequestDto.setName("testprojectrequest");
        projectRequestDto.setDescription("testdescription");

        projectResponseDto = new ProjectResponseDto();
        projectResponseDto.setName("testprojectresponse");
        projectResponseDto.setDescription("testdescription");
    }

    @Test
    public void AdminController_CreateProject_ReturnsProjectResponseDto() throws Exception {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(adminService.createProject(Mockito.any(ProjectRequestDto.class), Mockito.any(UserEntity.class)))
                .thenReturn(projectResponseDto);

        mockMvc.perform(post("/api/admin/user/1/projects")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(projectRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("testprojectresponse"))
                .andExpect(jsonPath("$.description").value("testdescription"));
    }

    @Test
    public void AdminController_GetProjectsByUser_ReturnsProjectResponseDtoList() throws Exception {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        when(adminService.getProjectsByUser(Mockito.any(UserEntity.class)))
                .thenReturn(List.of(projectResponseDto));

        mockMvc.perform(get("/api/admin/user/1/projects")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(projectRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("testprojectresponse"))
                .andExpect(jsonPath("$[0].description").value("testdescription"));
    }

    @Test
    public void AdminController_UpdateProject_ReturnsProjectResponseDto() throws Exception {
        when(adminService.updateProject(Mockito.anyLong(), Mockito.any(ProjectRequestDto.class),
                Mockito.anyLong())).thenReturn(projectResponseDto);

        mockMvc.perform(put("/api/admin/user/1/projects/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(projectRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("testprojectresponse"))
                .andExpect(jsonPath("$.description").value("testdescription"));
    }

    @Test
    public void AdminController_DeleteProject_ReturnsNoContent() throws Exception {
        doNothing().when(adminService).deleteProject(Mockito.anyLong(), Mockito.anyLong());

        mockMvc.perform(delete("/api/admin/user/1/projects/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
