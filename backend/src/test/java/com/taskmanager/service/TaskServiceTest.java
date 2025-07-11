package com.taskmanager.service;

import com.taskmanager.model.Task;
import com.taskmanager.model.TaskStatus;
import com.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task task1;
    private Task task2;
    private UUID taskId;

    @BeforeEach
    void setUp() {
        taskId = UUID.randomUUID();
        task1 = new Task("Test Task 1", "Description 1", TaskStatus.TODO);
        task2 = new Task("Test Task 2", "Description 2", TaskStatus.IN_PROGRESS);
    }

    @Test
    void getAllTasks_ShouldReturnAllTasks() {
        // Given
        List<Task> tasks = Arrays.asList(task1, task2);
        when(taskRepository.findAll()).thenReturn(tasks);

        // When
        List<Task> result = taskService.getAllTasks();

        // Then
        assertEquals(2, result.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void getTaskById_ShouldReturnTask_WhenTaskExists() {
        // Given
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task1));

        // When
        Optional<Task> result = taskService.getTaskById(taskId);

        // Then
        assertTrue(result.isPresent());
        assertEquals(task1.getTitle(), result.get().getTitle());
        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    void getTaskById_ShouldReturnEmpty_WhenTaskDoesNotExist() {
        // Given
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // When
        Optional<Task> result = taskService.getTaskById(taskId);

        // Then
        assertFalse(result.isPresent());
        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    void createTask_ShouldReturnSavedTask() {
        // Given
        when(taskRepository.save(any(Task.class))).thenReturn(task1);

        // When
        Task result = taskService.createTask(task1);

        // Then
        assertNotNull(result);
        assertEquals(task1.getTitle(), result.getTitle());
        verify(taskRepository, times(1)).save(task1);
    }

    @Test
    void updateTask_ShouldReturnUpdatedTask_WhenTaskExists() {
        // Given
        Task existingTask = new Task("Old Title", "Old Description", TaskStatus.TODO);
        Task updatedTask = new Task("New Title", "New Description", TaskStatus.DONE);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

        // When
        Optional<Task> result = taskService.updateTask(taskId, updatedTask);

        // Then
        assertTrue(result.isPresent());
        assertEquals("New Title", result.get().getTitle());
        assertEquals("New Description", result.get().getDescription());
        assertEquals(TaskStatus.DONE, result.get().getStatus());
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    void updateTask_ShouldReturnEmpty_WhenTaskDoesNotExist() {
        // Given
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // When
        Optional<Task> result = taskService.updateTask(taskId, task1);

        // Then
        assertFalse(result.isPresent());
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void deleteTask_ShouldReturnTrue_WhenTaskExists() {
        // Given
        when(taskRepository.existsById(taskId)).thenReturn(true);

        // When
        boolean result = taskService.deleteTask(taskId);

        // Then
        assertTrue(result);
        verify(taskRepository, times(1)).existsById(taskId);
        verify(taskRepository, times(1)).deleteById(taskId);
    }

    @Test
    void deleteTask_ShouldReturnFalse_WhenTaskDoesNotExist() {
        // Given
        when(taskRepository.existsById(taskId)).thenReturn(false);

        // When
        boolean result = taskService.deleteTask(taskId);

        // Then
        assertFalse(result);
        verify(taskRepository, times(1)).existsById(taskId);
        verify(taskRepository, never()).deleteById(taskId);
    }
}
