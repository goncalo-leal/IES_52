import consts from "./consts.js";
import SessionManager from "./session.js";

var store_manager_pages=["login.html","store.html", "account_settings.html", "store_statistics.html"];
var shopping_manager_pages=["add_park.html" ,"park_management.html", "edit_park.html","login.html","home.html", "statistics.html", "user_management.html", "add_user.html", "store_management.html", "add_store.html", "account_settings.html", "edit_store.html"];
var users =["login.html","index.html", "select_shopping.html"];

$(document).ready(function() {
    $("#login").click(function(e) {
        e.preventDefault();
        login();
    })
});


const can_access = function(page) {
    var user_type = SessionManager.get("session").user.authority;
    if (user_type == "ROLE_SHOPPING_MANAGER" && shopping_manager_pages.includes(page)){
        return true;
    }
    if (user_type == "ROLE_STORE_MANAGER" && store_manager_pages.includes(page)){
        return true;
    }
    return false;
}


const login = function() {
    var email = $("#email").val();
    var psw = $("#psw").val();
    $("#login_error").text("");
    $.ajax({
        url: consts.BASE_URL + '/auth/login',
        type: "POST",
        contentType: "application/json",
        crossDomain: true,
        data: JSON.stringify({"email": email, "password": psw}),
        success: function(data) {
            if (data) {
                console.log(data)
                SessionManager.set("session", data);
                SessionManager.set("token", data.token);
                
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
                $("#login_error").text("Invalid login, please try again.");
            }
        },

        error: function() {
            console.log("erro na call");
            $("#login_error").text("Invalid login, please try again.");
        }
    })
}
