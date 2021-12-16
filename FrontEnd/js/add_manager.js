


const BASE_URL = "http://localhost:8080"

$(document).ready(function() {
    $("#login").click(function() {
        login();
    })
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

const loadStores = function() {
    $.ajax({
        url: BASE_URL + '/api/'
    })
}