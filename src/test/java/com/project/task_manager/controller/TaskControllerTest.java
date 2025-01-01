package com.project.task_manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.task_manager.dto.TaskRequestDto;
import com.project.task_manager.dto.TaskResponseDto;
import com.project.task_manager.dto.TaskResponsePage;
import com.project.task_manager.entity.Project;
import com.project.task_manager.entity.UserEntity;
import com.project.task_manager.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TaskService taskService;
    @Autowired
    private ObjectMapper objectMapper;

    private UserEntity user;
    private Project project;
    private TaskRequestDto taskRequestDto;
    private TaskResponseDto taskResponseDto;

    @BeforeEach
    public void init() {
        user = new UserEntity();
        user.setId(1L);
        user.setUsername("testuser");

        project = new Project();
        project.setId(1L);
        project.setName("testproject");
        project.setUser(user);

        taskRequestDto = new TaskRequestDto();
        taskRequestDto.setName("testtaskrequest");
        taskRequestDto.setDescription("testdescription");

        taskResponseDto = new TaskResponseDto();
        taskResponseDto.setId(1L);
        taskResponseDto.setName("testtaskresponse");
        taskResponseDto.setDescription("testdescription");
    }

    @Test
    public void TaskController_CreateTask_ReturnsTaskResponseDto() throws Exception {
        when(taskService.createTask(any(TaskRequestDto.class), any(Long.class))).thenReturn(taskResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/project/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(taskRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("testtaskresponse"));
    }

    @Test
    public void TaskController_GetTasksByProject_ReturnsTaskResponsePage() throws Exception {
        TaskResponsePage taskResponsePage = new TaskResponsePage();
        taskResponsePage.setTasks(List.of(taskResponseDto));
        when(taskService.getTasksByProjectId(Mockito.anyLong(),
                Mockito.anyInt(), Mockito.anyInt())).thenReturn(taskResponsePage);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/project/1")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.tasks[0].name")
                        .value("testtaskresponse"));
    }

    @Test
    public void TaskController_UpdateTask_ReturnsTaskResponseDto() throws Exception {
        when(taskService.updateTask(any(Long.class),any(TaskRequestDto.class),
                any(Long.class))).thenReturn(taskResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/project/1/task/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(taskRequestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("testtaskresponse"));
    }

    @Test
    public void TaskController_DeleteTask_ReturnsNoContent() throws Exception {
        doNothing().when(taskService).deleteTask(any(Long.class), any(Long.class));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/project/1/task/1")
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
