import consts from "./consts.js";
import SessionManager from "./session.js";
import updateView from "./common.js"

$(document).ready(function() {
    updateView();
});




/*
const login = function() {
    var email = $("#email").val();
    var psw = $("#psw").val();

    console.log(`{ email: ${email}, psw: ${psw} }`)

    $.ajax({
        url: BASE_URL + '/auth/login/?email=' + email + "&password=" + psw,
        type: "POST",
        "dataType": "json",
        success: function(data) {
            console.log(data);

            if (data) {
                localStorage.setItem("email", email);
                localStorage.setItem("shopping", 1)
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
*/

const add_manager = function() {
    var email = $("#email").val();
    var username = $("#username").val()
    var gender = $('input[type="radio"][name="gender"]:checked').val()
    var birthday = null
    var store = $("store").val()

    $.ajax({
        url: BASE_URL + '/api/'
    })
}

const loadStores = function() {
    $.ajax({
        url: consts.BASE_URL + '/api/Stores'
    })
}