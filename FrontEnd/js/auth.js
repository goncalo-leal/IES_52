import consts from "./consts.js";
import SessionManager from "./session.js";

$(document).ready(function() {
    $("#login").click(function() {
        login();
    })
});



const login = function() {
    var email = $("#email").val();
    var psw = $("#psw").val();

    $.ajax({
        url: consts.BASE_URL + '/auth/login',
        type: "POST",
        contentType: "application/json",
        crossDomain: true,
        data: JSON.stringify({"email": email, "password": psw}),
        success: function(data) {
            if (data) {
                console.log(data);
                SessionManager.set("session", data);
                window.location.href = "./index.html";
            } else {
                console.log("nao autenticado");
            }
        },

        error: function() {
            console.log("erro na call");
        }
    })
}
