import AuthService from "./AuthService";

const API_BASE_URL = "http://localhost:8081/api/v1.0";

export async function apiFetch(endpoint, options = {}) {
  const token = AuthService.getToken();

  const response = await fetch(`${API_BASE_URL}${endpoint}`, {
    ...options,
    headers: {
      "Content-Type": "application/json",
      ...(token ? { Authorization: `Bearer ${token}` } : {}),
      ...(options.headers || {}),
    },
    credentials: "include",
  });

  const text = await response.text();
  const data = text ? JSON.parse(text) : {};

  if (!response.ok) {
    throw new Error(data.message || data.error || "Request failed");
  }

  return data;
}