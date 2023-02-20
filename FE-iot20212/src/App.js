import { useState } from "react";
import "./App.css";
import { Login } from "./component/Author/Login/login";
import { Register } from "./component/Author/Register/register";
import { Device } from "./component/Device/Device/device";
import { ListDevice } from "./component/Device/ListDevice/list-device";
import { UserModal } from "./component/Modal/UserModal/user-modal";

function App() {
  const [page, setPage] = useState("login");

  return (
    <>
      {page === "login" && (
        <Login
          changePageToRegister={() => setPage("register")}
          changePageToDevice={() => setPage("device")}
        />
      )}
      {page === "register" && (
        <Register
          changePageToLogin={() => setPage("login")}
          changePageToDevice={() => setPage("device")}
        />
      )}
      {page === "device" && <ListDevice toLogin = {() => setPage('login')}/>}
      {/* <ListDevice /> */}
      {/* <UserModal /> */}
      {/* <Device /> */}
    </>
  );
}

export default App;
