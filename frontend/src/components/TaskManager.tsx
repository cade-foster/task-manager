import React, { useState, useEffect } from 'react';
import { Task, TaskStatus } from '../types/Task';
import { taskService } from '../services/taskService';
import './TaskManager.css';

const TaskManager: React.FC = () => {
  const [tasks, setTasks] = useState<Task[]>([]);
  const [newTaskTitle, setNewTaskTitle] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetchTasks();
  }, []);

  const fetchTasks = async () => {
    try {
      setLoading(true);
      const fetchedTasks = await taskService.getAllTasks();
      setTasks(fetchedTasks);
      setError(null);
    } catch (err) {
      setError('Failed to fetch tasks');
      console.error('Error fetching tasks:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateTask = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!newTaskTitle.trim()) return;

    try {
      const newTask = await taskService.createTask({
        title: newTaskTitle,
        status: TaskStatus.TODO,
      });
      setTasks([...tasks, newTask]);
      setNewTaskTitle('');
      setError(null);
    } catch (err) {
      setError('Failed to create task');
      console.error('Error creating task:', err);
    }
  };

  const handleMarkAsDone = async (task: Task) => {
    if (!task.id) return;

    try {
      const updatedTask = await taskService.updateTask(task.id, {
        ...task,
        status: TaskStatus.DONE,
      });
      setTasks(tasks.map(t => t.id === task.id ? updatedTask : t));
      setError(null);
    } catch (err) {
      setError('Failed to update task');
      console.error('Error updating task:', err);
    }
  };

  const handleDeleteTask = async (taskId: string) => {
    try {
      await taskService.deleteTask(taskId);
      setTasks(tasks.filter(t => t.id !== taskId));
      setError(null);
    } catch (err) {
      setError('Failed to delete task');
      console.error('Error deleting task:', err);
    }
  };

  const getStatusColor = (status: TaskStatus) => {
    switch (status) {
      case TaskStatus.TODO:
        return '#f39c12';
      case TaskStatus.IN_PROGRESS:
        return '#3498db';
      case TaskStatus.DONE:
        return '#27ae60';
      default:
        return '#95a5a6';
    }
  };

  return (
    <div className="task-manager">
      <h1>Task Manager</h1>

      {error && <div className="error">{error}</div>}

      <form onSubmit={handleCreateTask} className="task-form">
        <input
          type="text"
          placeholder="Enter task title..."
          value={newTaskTitle}
          onChange={(e) => setNewTaskTitle(e.target.value)}
          className="task-input"
        />
        <button type="submit" className="add-button">Add Task</button>
      </form>

      {loading ? (
        <div className="loading">Loading tasks...</div>
      ) : (
        <div className="task-list">
          {tasks.length === 0 ? (
            <div className="no-tasks">No tasks yet. Create your first task!</div>
          ) : (
            tasks.map((task) => (
              <div key={task.id} className="task-item">
                <div className="task-content">
                  <h3 className="task-title">{task.title}</h3>
                  {task.description && (
                    <p className="task-description">{task.description}</p>
                  )}
                  <span
                    className="task-status"
                    style={{ backgroundColor: getStatusColor(task.status) }}
                  >
                    {task.status}
                  </span>
                </div>
                <div className="task-actions">
                  {task.status !== TaskStatus.DONE && (
                    <button
                      onClick={() => handleMarkAsDone(task)}
                      className="done-button"
                    >
                      Mark as Done
                    </button>
                  )}
                  <button
                    onClick={() => task.id && handleDeleteTask(task.id)}
                    className="delete-button"
                  >
                    Delete
                  </button>
                </div>
              </div>
            ))
          )}
        </div>
      )}
    </div>
  );
};

export default TaskManager;
