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
                console.log(data)
                SessionManager.set("session", data);
                
                if (SessionManager.get("session").user.authority == "ROLE_SHOPPING_MANAGER") {
                    window.location.href = "./home.html";
                } else {
                    if (SessionManager.get("session").user.state.id == 1) {
                        window.location.href = "./register.html"
                    } else {
                        window.location.href = "./store.html?id=" + data.store.id
                    }
                }
            } else {
                console.log("nao autenticado");
            }
        },

        error: function() {
            console.log("erro na call");
        }
    })
}
