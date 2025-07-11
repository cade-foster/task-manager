package com.taskmanager.service;

import com.taskmanager.model.Task;
import com.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service class for managing {@link Task}s, providing methods for CRUD operations.
 */
@Service
public class TaskService {

    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Retrieves all {@link Task}s from the repository.
     *
     * @return a list of all {@link Task}s
     */
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    /**
     * Retrieves a {@link Task} by its unique identifier.
     *
     * @param id the UUID of the {@link Task} to retrieve
     * @return an Optional containing the {@link Task} if found, or empty if not found
     */
    public Optional<Task> getTaskById(UUID id) {
        return taskRepository.findById(id);
    }

    /**
     * Creates a new {@link Task} and saves it to the repository.
     *
     * @param task the {@link Task} to create
     * @return the created {@link Task}
     */
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    /**
     * Updates an existing {@link Task} with new information.
     *
     * @param id          the UUID of the {@link Task} to update
     * @param updatedTask the {@link Task} object containing updated information
     * @return an Optional containing the updated {@link Task} if found, or empty if not found
     */
    public Optional<Task> updateTask(UUID id, Task updatedTask) {
        return taskRepository.findById(id)
                .map(task -> {
                    task.setTitle(updatedTask.getTitle());
                    task.setDescription(updatedTask.getDescription());
                    task.setStatus(updatedTask.getStatus());
                    return taskRepository.save(task);
                });
    }

    /**
     * Deletes a {@link Task} by its unique identifier.
     *
     * @param id the UUID of the {@link Task} to delete
     * @return true if the {@link Task} was deleted, false if the {@link Task} was not found
     */
    public boolean deleteTask(UUID id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
