package com.project.task_manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.task_manager.dto.*;
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

    private TaskRequestDto taskRequestDto;
    private TaskResponseDto taskResponseDto;

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

        taskRequestDto = new TaskRequestDto();
        taskRequestDto.setName("testtaskrequest");
        taskRequestDto.setDescription("testdescription");
        taskRequestDto.setCompleted(false);

        taskResponseDto = new TaskResponseDto();
        taskResponseDto.setName("testtaskresponse");
        taskResponseDto.setDescription("testdescription");
        taskResponseDto.setCompleted(false);
        taskResponseDto.setProjectId(1L);
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
    public void AdminController_GetProjectsByUser_ReturnsProjectResponsePage() throws Exception {
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        ProjectResponsePage projectResponsePage = new ProjectResponsePage();
        projectResponsePage.setProjects(List.of(projectResponseDto));
        when(adminService.getProjectsByUser(Mockito.any(UserEntity.class), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(projectResponsePage);

        mockMvc.perform(get("/api/admin/user/1/projects")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(projectRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projects[0].name").value("testprojectresponse"))
                .andExpect(jsonPath("$.projects[0].description").value("testdescription"));
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

    @Test
    public void AdminController_CreateTask_ReturnsTaskResponseDto() throws Exception {
        when(adminService.createTask(Mockito.any(TaskRequestDto.class), Mockito.anyLong(),
                Mockito.anyLong())).thenReturn(taskResponseDto);

        mockMvc.perform(post("/api/admin/user/1/projects/1/tasks")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(taskRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("testtaskresponse"))
                .andExpect(jsonPath("$.description").value("testdescription"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    public void AdminController_GetTasksByProject_ReturnsTaskResponsePage() throws Exception {
        TaskResponsePage taskResponsePage = new TaskResponsePage();
        taskResponsePage.setTasks(List.of(taskResponseDto));
        when(adminService.getTasksByProject(Mockito.anyLong(), Mockito.anyLong(),
                Mockito.anyInt(), Mockito.anyInt())).thenReturn(taskResponsePage);

        mockMvc.perform(get("/api/admin/user/1/projects/1/tasks")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(taskRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tasks[0].name").value("testtaskresponse"))
                .andExpect(jsonPath("$.tasks[0].description").value("testdescription"))
                .andExpect(jsonPath("$.tasks[0].completed").value(false));
    }

    @Test
    public void AdminController_UpdateTask_ReturnsTaskResponseDto() throws Exception {
        when(adminService.updateTask(Mockito.anyLong(), Mockito.any(TaskRequestDto.class),
                Mockito.anyLong(), Mockito.anyLong())).thenReturn(taskResponseDto);

        mockMvc.perform(put("/api/admin/user/1/projects/1/tasks/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(taskRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("testtaskresponse"))
                .andExpect(jsonPath("$.description").value("testdescription"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    public void AdminController_DeleteTask_ReturnsNoContent() throws Exception {
        doNothing().when(adminService).deleteTask(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong());

        mockMvc.perform(delete("/api/admin/user/1/projects/1/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
