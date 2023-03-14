import { useEffect, useState } from "react";
import { EraseIcon } from "../../../dekit/icons/erase";
import { FlagIcon } from "../../../dekit/icons/flag";
import "./list-device.css";
import axios from "axios";
import { PlayerIcon } from "../../../dekit/icons/player";
import { UserModal } from "../../Modal/UserModal/user-modal";
import { DeviceModal } from "../../Modal/DeviceModal/device-modal";
import { Device } from "../Device/device";
import { AddDeviceModal } from "../../Modal/AddDeviceModal/add-device-modal";
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

export function ListDevice(props) {
  const [userModal, setUserModal] = useState("off");
  const [deviceModal, setDeviceModal] = useState("off");
  const [addDeviceModal, setAddDeviceModal] = useState("off");
  const [listDevice, setListDevice] = useState();
  const [selected, setSelected] = useState(0);
  const [reset, setReset] = useState(0);
  const [firstLoad] = useState(true);

  useEffect(() => {
    var config = {
      method: "get",
      url: "https://iot-health.onrender.com/heartSensor",
      headers: {
        token: localStorage.getItem("token"),
      },
    };

    axios(config)
      .then(function (response) {
        setListDevice(response.data);
      })
      .catch(function (error) {
        alert(error);
      });

  }, [reset]);

  useEffect(() => {
    toast.success('Welcome', { position: toast.POSITION.TOP_RIGHT, autoClose: 3000 })
  }, [firstLoad])

  // useEffect(() => {
  //   let message = '';
  //   for(let i= 0; i< listDevice.length; i++) {
  //     if(listDevice[i].warning){
  //       message = listDevice[i].warning + ' at ' +listDevice[i].create 
  //       break;
  //     }
  //   }
  //   const timer = setTimeout(() => {
  //     if(message != '')
  //     toast.warning(message, { position: toast.POSITION.TOP_RIGHT, autoClose: 3000 })
  //   }, 60000 * 10);
  //   return () => clearTimeout(timer);
  
  // }, [listDevice])

  const onHandleEraseModal = (id) => {
    var config = {
      method: "delete",
      url: `https://iot-health.onrender.com/heartSensor/${id}`,
      headers: {
        token: localStorage.getItem("token"),
      },
    };

    axios(config)
      .then(function (response) {
        toast.success('Erase device successfully', { position: toast.POSITION.TOP_RIGHT, autoClose: 3000 })
        setReset(1 - reset)
      })
      .catch(function (error) {
        alert(error);
      });
  }

  const onCloseModal = () => {
    setUserModal('off')
    setDeviceModal('off')
    setAddDeviceModal('off')
    setReset(1 - reset)
  }

  return (
    <>
      <div className='de-container-list-device'>
        <h1>Device chart</h1>
        <div className='de-wrap-list-device'>
          <div className="de-list-device">
            <div onClick={() => setUserModal("on")}>
              <PlayerIcon className="de-player-icon" />
            </div>
            {listDevice &&
              listDevice.map((element, index) => (
                <div className={`ds-device ${index === selected && 'de-device-hightlight'}`} key={element._id}>
                  <div className="ds-title" onClick={() => setSelected(index)}>{element.name}</div>
                  <div className="ds-icon">
                    <div onClick={() => setDeviceModal(index)}>
                      <FlagIcon className={`ds-flag-icon ${index === selected && 'ds-flag-highlight'}`} />
                    </div>
                    <div onClick={() => onHandleEraseModal(element._id)}>
                      <EraseIcon className="ds-erase-icon" />
                    </div>
                  </div>
                </div>
              ))}
            <div className="ds-add-device" onClick={() => setAddDeviceModal('on')}>
              Add device
            </div>
          </div>
          {listDevice && <Device device={listDevice[selected]} />}
        </div>
      </div>
      {userModal === "on" && (
        <UserModal closeUserModal={onCloseModal} toLogin={props.toLogin} />
      )}
      {deviceModal !== "off" && (
        <DeviceModal closeDeviceModal={onCloseModal} device={listDevice[deviceModal]} />
      )}
      {addDeviceModal === 'on' && (
        <AddDeviceModal closeAddDeviceModal={onCloseModal} />
      )}
      <ToastContainer />
    </>
  );
}
