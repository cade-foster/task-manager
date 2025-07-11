import axios from 'axios';
import { Task } from '../types/Task';

const API_BASE_URL = 'http://localhost:8080';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const taskService = {
  getAllTasks: async (): Promise<Task[]> => {
    const response = await api.get('/tasks');
    return response.data;
  },

  createTask: async (task: Omit<Task, 'id'>): Promise<Task> => {
    const response = await api.post('/tasks', task);
    return response.data;
  },

  updateTask: async (id: string, task: Omit<Task, 'id'>): Promise<Task> => {
    const response = await api.put(`/tasks/${id}`, task);
    return response.data;
  },

  deleteTask: async (id: string): Promise<void> => {
    await api.delete(`/tasks/${id}`);
  },
};
