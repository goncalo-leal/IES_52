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
    $("#login_error").text("");
    $.ajax({
        url: consts.BASE_URL + '/auth/login/?email=' + email + "&password=" + psw,
        type: "POST",
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            if (data) {
                console.log(data)
                SessionManager.set("session", data);
                
                if (SessionManager.get("session").user.authority == "ROLE_SHOPPING_MANAGER") {
                    window.location.href = "./home.html";
                } else {
                    window.location.href = "./store.html?id=" + data.store.id
                }
            } else {
                console.log("nao autenticado");
                $("#login_error").text("Invalid login, please try again.");
            }
        },

        error: function() {
            console.log("erro na call");
            $("#login_error").text("Invalid login, please try again.");
        }
    })
}
