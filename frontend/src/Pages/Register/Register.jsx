import React from "react";
// import images from "../../assets/images";
import "./Register.css";
import { useNavigate } from "react-router-dom";


export default function Register() {
  let navigate = useNavigate();
  return (
    <div className="main--screen">
      <div className="right--side">
        <h2 className="right--text">Create an account</h2>
        <form className="fields">
          <input className="email" placeholder="Name" />
          <input className="email" placeholder="Email" />

          <input
            type="none"
            placeholder="Mobile"
            name="mobileNumber"
            className="email"
          />
          <input className="password" placeholder="Password" />
          <div className="chk-box">
            <input type="checkbox" required />
            <label className="label-text">
              By creating an account, I agree to our terms of use and privacy
              policy
            </label>
          </div>
          <button className="login--button">Create Account</button>
<div>
<div className="login-register">
          <p className="login-caption">
            Already have an account?{" "}
            <span onClick={() => {
              navigate('/login');
            }} className="login-url">
              Sign In
            </span>
          </p>
        </div>
          </div>
        </form>
      </div>
    </div>
  );
}