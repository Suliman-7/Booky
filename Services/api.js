import axios from 'axios';

const API_URL = 'http://localhost:8080/api/v1/book'; // Replace with your Spring Boot API URL

const apiClient = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export default apiClient;