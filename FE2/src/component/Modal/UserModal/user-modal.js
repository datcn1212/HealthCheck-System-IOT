import { useEffect, useState } from 'react'
import './user-modal.css'
import axios from "axios";
import qs from "qs";

export function UserModal(props) {
    const [userInfo, setUserInfo] = useState();
    const [username, setUsername] = useState();
    const [name, setName] = useState();
    const [age, setAge] = useState();
    const [weight, setWeight] = useState();
    const [height, setHeight] = useState()

    useEffect(() => {
        var config = {
            method: "get",
            url: "https://iot-health.onrender.com/user/information",
            headers: {
                token: localStorage.getItem("token"),
            },
        };

        axios(config)
            .then(function (response) {
                setUserInfo(response.data);
                setUsername(response.data.username);
                setName(response.data.name);
                setAge(response.data.age);
                setWeight(response.data.weight);
                setHeight(response.data.height);
            })
            .catch(function (error) {
                alert(error);
            });
    }, [userInfo])

    const onChangeUsername = (event) => {
        setUsername(event.target.value)
    }

    const onChangeName = (event) => {
        setName(event.target.value)
    }

    const onChangeAge = (event) => {
        setAge(event.target.value)
    }

    const onChangeWeight = (event) => {
        setWeight(event.target.value)
    }

    const onChangeHeight = (event) => {
        setHeight(event.target.value)
    }

    const onHandleLogout = () => {
        localStorage.setItem('token', '')
        props.toLogin()
    }

    const onSaveInformation = async () => {
        var data = qs.stringify({
            name: name,
            username: username,
            age: age,
            weight: weight,
            height: height
        });
        var config = {
            method: "put",
            url: `https://iot-health.onrender.com/user/information`,
            headers: {
                token: localStorage.getItem("token"),
            },
            data: data,
        };

        await axios(config)
            .then(function (response) {
                alert('success')
                props.closeUserModal()
            })
            .catch(function (error) {
                alert(error);
            });
    }

    return (
        <>
            <div className='de-user-modal'>
                <div className='de-pop-up' onClick={props.closeUserModal} />
                <div className='de-main-content'>
                    <h1>User Infomation</h1>
                    <div className='de-wrap-input'>
                        <label>User Name</label>
                        <input placeholder={username} onChange={onChangeUsername}></input>
                    </div>
                    <div className='de-wrap-input'>
                        <label>Name</label>
                        <input placeholder={name} onChange={onChangeName}></input>
                    </div>
                    <div className='de-wrap-input'>
                        <label>Age</label>
                        <input placeholder={age} onChange={onChangeAge}></input>
                    </div>
                    <div className='de-wrap-input'>
                        <label>Weight</label>
                        <input placeholder={weight} onChange={onChangeWeight}></input>
                    </div>
                    <div className='de-wrap-input'>
                        <label>Height</label>
                        <input placeholder={height} onChange={onChangeHeight}></input>
                    </div>
                    <div className='de-wrap-button'>
                        <button className='de-button' onClick={onSaveInformation}>Save</button>
                        <button className='de-button de-logout' onClick={onHandleLogout}>Logout</button>
                    </div>
                </div>
            </div>
        </>
    )
}