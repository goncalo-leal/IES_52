import SessionManager from "./session.js";


const updateView = function () {
    if (SessionManager.get("session") === null) {
        alert("Please log in!");
        window.location.href = "./login.html";
    }

    $("#loginBttn").text("Log Out");
    $("#sm_name").text(SessionManager.get("session").user.name);

    $("body").fadeIn(300);
}

export default updateView;