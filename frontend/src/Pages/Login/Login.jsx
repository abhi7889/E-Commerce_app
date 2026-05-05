import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import AuthService from '../../services/AuthService';
import './Login.css';

const API_BASE_URL = 'http://localhost:8081/api/v1.0';

export default function Login() {
  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async (event) => {
    event.preventDefault();
    setMessage('');
    setError('');

    try {
      const response = await fetch(`${API_BASE_URL}/login`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        credentials: 'include',
        body: JSON.stringify({ email, password }),
      });

      const data = await response.json();
      if (!response.ok) {
        throw new Error(data.message || data.error || 'Login failed');
      }

      AuthService.setToken(data.token);
      setMessage('Login successful. Redirecting to homepage...');
      setError('');
      navigate('/home');
    } catch (err) {
      setError(err.message || 'Unable to login.');
      setMessage('');
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
              Don't have an account?{' '}
              <span
                onClick={() => {
                  navigate('/register');
                }}
                className="login-url"
              >
                Create Account
              </span>
            </p>
            <p className="login-caption">
              <span
                onClick={() => {
                  navigate('/forgot-password');
                }}
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
