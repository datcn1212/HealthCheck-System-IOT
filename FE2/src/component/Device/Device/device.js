import { useEffect, useState } from 'react';
import { ResponsiveContainer, LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend } from 'recharts';
import './device.css'
import axios from 'axios';
import { ToastContainer, toast } from 'react-toastify';


export function Device(props) {

    const [device, setDevice] = useState()

    useEffect(() => {
        if (!props.device?.Id) return;
        var config = {
            method: "get",
            url: `https://iot-health.onrender.com/heartSensorData/${props.device.Id}`,
            headers: {
                token: localStorage.getItem("token"),
            },
        };

        axios(config)
            .then(function (response) {
                setDevice(response.data);
            })
            .catch(function (error) {
                alert(error);
            });
    }, [props])

    // useEffect(() => {
    //     let message = '';
    //     for (let i = 0; i < device.length; i++) {
    //         if (device[i].warning) {
    //             message = device[i].warning + ' at ' + device[i].create
    //             break;
    //         }
    //     }
    //         if (message != '')
    //             toast.warning(message, { position: toast.POSITION.TOP_RIGHT, autoClose: 3000 })

    // }, [device])

    return <>
        {props.device && <div className='de-wrap-chat'>
            <ResponsiveContainer className="chart" height={800} width={1500}>
                <LineChart
                    width={600}
                    height={300}
                    data={device}
                    margin={{ top: 5, right: 30, left: 20, bottom: 5 }}
                >
                    <XAxis dataKey="create" />
                    <YAxis />
                    <CartesianGrid strokeDasharray="3 3" />
                    <Tooltip />
                    <Legend />
                    <Line type="monotone" dataKey="heartRate" stroke="#8884d8" activeDot={{ r: 25 }} />
                    <Line type="monotone" dataKey="bodyTemperature" stroke="#82ca9d" />
                    <Line type="monotone" dataKey="diastolic" stroke="#823251" />
                    <Line type="monotone" dataKey="systolic" stroke="#822a9f" />
                    <Line type="monotone" dataKey="warning" />
                </LineChart>
            </ResponsiveContainer>
            <h3>{props.device.name}'s chart</h3>
        </div>
        }
        <ToastContainer />
    </>
}