import { useState } from 'react'
import { API_END_POINT } from '../../../const'
import './register.css'
import axios from "axios";
import qs from "qs";

export function Register(props) {

    const [username, setUsername] = useState('');
    const [password, setPassWord] = useState('');
    const [name, setName]= useState('')
    const [age, setAge]= useState('')
    const [weight, setWeight]= useState('')
    const [height, setHeight]= useState('')

    const onHandleregister = async() => {
        var data = qs.stringify({
            username: username,
            password: password,
            name: name,
            age: age,
            weight: weight,
            height: height
          });
          var config = {
            method: "post",
            url: "https://iot-health.onrender.com/user/register",
            headers: {
              "Content-Type": "application/x-www-form-urlencoded",
            },
            data: data,
          };
      
          await axios(config)
            .then(async function (response) {
                var body = qs.stringify({
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
                      props.changePageToDevice()
                    })
                    .catch(function (error) {
                      console.log(error);
                    });
            })
            .catch(function (error) {
              alert(error);
            });
    }

    const onChangeName= (event) => {
        setName(event.target.value)
    }

    const onChangeAge= (event) => {
        setAge(event.target.value)
    }

    const onChangeWeight= (event) => {
        setWeight(event.target.value)
    }

    const onChangeHeight= (event) => {
        setHeight(event.target.value)
    }

    const onChangeUsername= (event) => {
        setUsername(event.target.value)
    }

    const onChangepassword= (event) => {
        setPassWord(event.target.value)
    }

    return <div className='de-register-container'>
        <div className='de-register'>
            <h1 onClick={props.changePage}> Register </h1>
            <input type='text' placeholder="User Name" onChange={onChangeUsername}/>
            <input type='text' placeholder="Password" onChange={onChangepassword}/>
            <input type='text' placeholder="Name" onChange={onChangeName}/>
            <input type='number' placeholder="Age" onChange={onChangeAge}/>
            <input type='text' placeholder="Weight" onChange={onChangeWeight}/>
            <input type='text' placeholder="Height" onChange={onChangeHeight}/>
            <div className='de-button-wrap'>
                <button className='de-button ds-bg-clr-blue' onClick={onHandleregister}>
                    Register
                </button>
            </div>
            <div className='de-line'>You have an account? <div className='de-link' onClick={props.changePageToLogin}>Login</div></div>
        </div>
    </div>
}