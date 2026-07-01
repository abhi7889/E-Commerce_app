import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import AuthService from "../../services/AuthService";
import "./Login.css";

const API_BASE_URL = "https://e-commerceapp.up.railway.app/api/v1.0";

export default function Login() {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    if (AuthService.isAuthenticated()) {
      navigate("/home");
    }
  }, [navigate]);

  const handleSubmit = async (event) => {
    event.preventDefault();
    setMessage("");
    setError("");

    try {
      const response = await fetch(`${API_BASE_URL}/login`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
        body: JSON.stringify({ email, password }),
      });

      const data = await response.json();

      if (!response.ok) {
        throw new Error(data.message || data.error || "Login failed");
      }

      AuthService.setToken(data.token);
      AuthService.setUser({
        email: data.email,
        name: data.name || "",
        role: data.role || "USER",
      });

      setMessage("Login successful. Redirecting to homepage...");
      setError("");
      navigate("/home", { replace: true });
    } catch (err) {
      setError(err.message || "Unable to login.");
      setMessage("");
    }
  };

  return (
    <div className="main--screen">
      <div className="right--side">
        <h2 className="right--text">Welcome back</h2>
        <p className="right--text--below">Please sign in to continue</p>

        <form className="fields" onSubmit={handleSubmit}>
          <input
            className="email"
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />

          <input
            className="password"
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />

          <button type="submit" className="login--button">
            Sign In
          </button>

          {message && <p className="success-message">{message}</p>}
          {error && <p className="error-message">{error}</p>}

          <div className="login-register">
            <p className="login-caption">
              Don't have an account?{" "}
              <span onClick={() => navigate("/register")} className="login-url">
                Create Account
              </span>
            </p>

            <p className="login-caption">
              <span
                onClick={() => navigate("/forgot-password")}
                className="login-url"
              >
                Forgot Password?
              </span>
            </p>
          </div>
        </form>
      </div>
    </div>
  );
}
