import { useState } from 'react'
import './device-modal.css'
import axios from "axios";
import qs from 'qs';
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

export function DeviceModal(props) {

    const [name, setName]= useState()
    const [info, setInfor] = useState()

    const onHandleUpdate = async() => {
        var data = qs.stringify({
            name: name,
            info: info,
          });
          var config = {
            method: "put",
            url: `https://iot-health.onrender.com/heartSensor/${props.device._id}`,
            headers: {
                token: localStorage.getItem("token"),
            },
            data: data,
          };
      
          await axios(config)
            .then(function (response) {
              toast.success('Update device successfully', { position: toast.POSITION.TOP_RIGHT, autoClose: 3000 })
              props.closeDeviceModal()
            })
            .catch(function (error) {
              alert(error);
            });
    }

    const onChangeName = (event) => {
        setName(event.target.value)
    }

    const onChangeId = (event) => {
        setInfor(event.target.value)
    }

    return (
        <>
            <div className='de-device-modal'>
                <div className='de-pop-up' onClick={props.closeDeviceModal} />
                <div className='de-main-content'>
                    <h1>Device Infomation</h1>
                    <div className='de-wrap-input'>
                        <label>Device Name</label>
                        <input placeholder={props.device.name} onChange={onChangeName}></input>
                    </div>
                    <div className='de-wrap-input'>
                        <label>Device Info</label>
                        <input placeholder={props.device.info} onChange={onChangeId}></input>
                    </div>
                    <div className='de-wrap-button'>
                        <div className='ds-button-update' onClick={onHandleUpdate}>Update</div>
                    </div>
                </div>
            </div>
            <ToastContainer />
        </>
    )
}