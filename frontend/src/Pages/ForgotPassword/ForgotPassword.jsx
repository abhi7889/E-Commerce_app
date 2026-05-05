import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './ForgotPassword.css';

const API_BASE_URL = 'http://localhost:8081/api/v1.0';

export default function ForgotPassword() {
  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [otp, setOtp] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [step, setStep] = useState(1);
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');

  const handleSendOtp = async (event) => {
    event.preventDefault();
    setMessage('');
    setError('');

    try {
      const response = await fetch(`${API_BASE_URL}/send-reset-otp`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email }),
      });

      const data = await response.json();
      if (!response.ok) {
        throw new Error(data.message || 'Unable to send reset code');
      }

      setMessage(data.message || 'Reset code sent.');
      setStep(2);
    } catch (err) {
      setError(err.message || 'Unable to send reset code.');
    }
  };

  const handleResetPassword = async (event) => {
    event.preventDefault();
    setMessage('');
    setError('');

    if (newPassword !== confirmPassword) {
      setError('Passwords do not match');
      return;
    }

    try {
      const response = await fetch(`${API_BASE_URL}/reset-password`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, otp, newPassword }),
      });

      const data = await response.json();
      if (!response.ok) {
        throw new Error(data.message || 'Unable to reset password');
      }

      setMessage(data.message || 'Password updated successfully. Please sign in.');
      setError('');
      setOtp('');
      setNewPassword('');
      setConfirmPassword('');
      setStep(3);
    } catch (err) {
      setError(err.message || 'Unable to reset password.');
    }
  };

  return (
    <div className="forgot-main-screen">
      <div className="forgot-card">
        <h2>Forgot Password</h2>
        {step === 1 && (
          <form className="forgot-form" onSubmit={handleSendOtp}>
            <label>Email</label>
            <input
              type="email"
              placeholder="Enter your registered email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
            <button type="submit" className="forgot-button">
              Send Reset Code
            </button>
          </form>
        )}

        {step === 2 && (
          <form className="forgot-form" onSubmit={handleResetPassword}>
            <label>Email</label>
            <input
              type="email"
              placeholder="Registered email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
            <label>Reset Code</label>
            <input
              type="text"
              placeholder="Enter OTP"
              value={otp}
              onChange={(e) => setOtp(e.target.value)}
              required
            />
            <label>New Password</label>
            <input
              type="password"
              placeholder="New password"
              value={newPassword}
              onChange={(e) => setNewPassword(e.target.value)}
              required
            />
            <label>Confirm Password</label>
            <input
              type="password"
              placeholder="Confirm password"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              required
            />
            <button type="submit" className="forgot-button">
              Reset Password
            </button>
          </form>
        )}

        {step === 3 && (
          <div className="forgot-success">
            <p>Your password was reset successfully.</p>
            <button className="forgot-button" onClick={() => navigate('/login')}>
              Go to Login
            </button>
          </div>
        )}

        {message && <p className="success-message">{message}</p>}
        {error && <p className="error-message">{error}</p>}

        <div className="forgot-links">
          <button className="forgot-link" onClick={() => navigate('/login')}>
            Back to Login
          </button>
        </div>
      </div>
    </div>
  );
}
