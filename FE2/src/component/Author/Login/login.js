import { useState } from "react";
import { API_END_POINT } from "../../../const";
import { ArrowNext } from "../../../dekit/icons/arrow-next";
import "./login.css";
import axios from "axios";
import qs from "qs";
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

export function Login(props) {
  const [username, setUsername] = useState("");
  const [password, setPassWord] = useState("");

  const onHandleLogin = async () => {
    var data = qs.stringify({
      username: username,
      password: password,
    });
    var config = {
      method: "post",
      url: "https://iot-health.onrender.com/user/login",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
      },
      data: data,
    };

    await axios(config)
      .then(function (response) {
        localStorage.setItem('token', response.data)
        toast.success('Welcome', {position: toast.POSITION.TOP_RIGHT,  autoClose: 3000 })
        props.changePageToDevice()
      })
      .catch(function (error) {
        toast.error('Wrong user name or password', {
          position: toast.POSITION.TOP_RIGHT, autoClose: 3000
        })
      });
  };

  const onChangeUsername = (event) => {
    console.log(event.target.value);
    setUsername(event.target.value);
  };

  const onChangepassword = (event) => {
    setPassWord(event.target.value);
  };

  return (<>
    <div className="de-login-container">
      <div className="de-login">
        <h1> Login </h1>
        <input
          type="text"
          placeholder="User Name"
          onChange={onChangeUsername}
        />
        <input type="text" placeholder="Password" onChange={onChangepassword} />
        <div className="de-button-wrap">
          <button className="de-button ds-bg-clr-green" onClick={onHandleLogin}>
            Login
          </button>
        </div>
        <div className="de-line">
          Don't have account?{" "}
          <div className="de-link" onClick={props.changePageToRegister}>
            Register
          </div>
        </div>
      </div>
    </div>
    <ToastContainer />
  </>
  );
}
