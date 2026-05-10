import React, { useState } from "react";
// import images from "../../assets/images";
import "./Register.css";
import { useNavigate } from "react-router-dom";

const API_BASE_URL = "http://localhost:8081/api/v1.0";

export default function Register() {
  const navigate = useNavigate();
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  const handleSubmit = async (event) => {
    event.preventDefault();
    setMessage("");
    setError("");

    try {
      const response = await fetch(`${API_BASE_URL}/register`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include",
        body: JSON.stringify({ name, email, password }),
      });

      const data = await response.json();
      if (!response.ok) {
        throw new Error(data.message || data.error || "Registration failed.");
      }

      setMessage("Registration successful. Please log in.");
      setError("");
      setName("");
      setEmail("");
      setPassword("");
      navigate("/login");
    } catch (err) {
      setError(err.message || "Unable to register.");
      setMessage("");
    }
  };

  return (
    <div className="main--screen">
      <div className="right--side">
        <h2 className="right--text">Create an account</h2>
        <form className="fields" onSubmit={handleSubmit}>
          <input
            className="email"
            type="text"
            placeholder="Name"
            value={name}
            onChange={(e) => setName(e.target.value)}
            required
          />
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
          <div className="chk-box">
            <input type="checkbox" required />
            <label className="label-text">
              By creating an account, I agree to our terms of use and privacy
              policy
            </label>
          </div>
          <button type="submit" className="login--button">
            Create Account
          </button>
          {message && <p className="success-message">{message}</p>}
          {error && <p className="error-message">{error}</p>}
          <div className="login-register">
            <p className="login-caption">
              Already have an account?{" "}
              <span onClick={() => navigate("/login")} className="login-url">
                Sign In
              </span>
            </p>
          </div>
        </form>
      </div>
    </div>
  );
}
