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
        url: consts.BASE_URL + '/auth/login/?email=' + email + "&password=" + psw,
        type: "POST",
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            if (data) {
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
