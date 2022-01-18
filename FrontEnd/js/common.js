import SessionManager from "./session.js";


const updateView = function () {
    if (SessionManager.get("session") === null && window.location.pathname != "/index.html") {
        window.location.href = "./index.html";
    }

    $("body").fadeIn(300);
}

export default updateView;