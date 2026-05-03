import React from 'react';
import { useNavigate } from 'react-router-dom';
import './Login.css';

export default function Login() {
  const navigate = useNavigate();

  return (
    <div className="main--screen">
      <div className="right--side">
        <h2 className="right--text">Welcome back</h2>
        <p className="right--text--below">Please sign in to continue</p>
        <form className="fields">
          <input className="email" placeholder="Email" />
          <input className="password" type="password" placeholder="Password" />
          <button type="button" className="login--button">
            Sign In
          </button>
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
          </div>
        </form>
      </div>
    </div>
  );
}
