import SessionManager from "./session.js";

$(document).ready(function() {
    alert(SessionManager.get("session").shopping.id);
})