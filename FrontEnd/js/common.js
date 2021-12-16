import SessionManager from "./session.js";

const updateView = function () {
    $("#loginBttn").text("Log Out");
    $("#sm_name").text(SessionManager.get("session").user.name);
}

export default updateView;