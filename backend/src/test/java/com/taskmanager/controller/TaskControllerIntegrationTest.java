package com.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmanager.model.Task;
import com.taskmanager.model.TaskStatus;
import com.taskmanager.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllTasks_ShouldReturnTaskList() throws Exception {
        // Given
        Task task1 = new Task("Task 1", "Description 1", TaskStatus.TODO);
        Task task2 = new Task("Task 2", "Description 2", TaskStatus.DONE);
        when(taskService.getAllTasks()).thenReturn(Arrays.asList(task1, task2));

        // When & Then
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[1].title").value("Task 2"));
    }

    @Test
    void createTask_ShouldCreateTask_WhenValidInput() throws Exception {
        // Given
        Task task = new Task("New Task", "New Description", TaskStatus.TODO);
        when(taskService.createTask(any(Task.class))).thenReturn(task);

        // When & Then
        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New Task"))
                .andExpect(jsonPath("$.description").value("New Description"))
                .andExpect(jsonPath("$.status").value("TODO"));
    }

    @Test
    void createTask_ShouldReturnBadRequest_WhenTitleIsBlank() throws Exception {
        // Given
        Task task = new Task("", "Description", TaskStatus.TODO);

        // When & Then
        mockMvc.perform(post("/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateTask_ShouldUpdateTask_WhenTaskExists() throws Exception {
        // Given
        UUID taskId = UUID.randomUUID();
        Task updatedTask = new Task("Updated Task", "Updated Description", TaskStatus.DONE);
        when(taskService.updateTask(eq(taskId), any(Task.class))).thenReturn(Optional.of(updatedTask));

        // When & Then
        mockMvc.perform(put("/tasks/{id}", taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"))
                .andExpect(jsonPath("$.status").value("DONE"));
    }

    @Test
    void updateTask_ShouldReturnNotFound_WhenTaskDoesNotExist() throws Exception {
        // Given
        UUID taskId = UUID.randomUUID();
        Task updatedTask = new Task("Updated Task", "Updated Description", TaskStatus.DONE);
        when(taskService.updateTask(eq(taskId), any(Task.class))).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(put("/tasks/{id}", taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedTask)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteTask_ShouldDeleteTask_WhenTaskExists() throws Exception {
        // Given
        UUID taskId = UUID.randomUUID();
        when(taskService.deleteTask(taskId)).thenReturn(true);

        // When & Then
        mockMvc.perform(delete("/tasks/{id}", taskId))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteTask_ShouldReturnNotFound_WhenTaskDoesNotExist() throws Exception {
        // Given
        UUID taskId = UUID.randomUUID();
        when(taskService.deleteTask(taskId)).thenReturn(false);

        // When & Then
        mockMvc.perform(delete("/tasks/{id}", taskId))
                .andExpect(status().isNotFound());
    }
}
