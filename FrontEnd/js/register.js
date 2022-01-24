import consts from "./consts.js";
import SessionManager from "./session.js";
import updateView from "./common.js"

$(document).ready(function() {
    updateView();
    get_cur_info();
    $("#accept").click(function(e) {
        e.preventDefault();
        accept_manager();
    })
    //Date picker
    $('#birthdaydate').datetimepicker({
        format: 'L'
    });
});

const accept_manager = function() {
    var email = $("#email").val();
    var username = $("#username").val()
    var password = $("#password").val()
    var gender = $('input[type="radio"][name="gender"]:checked').val()
    var birthday = $("#birthdaydate input").val().split("/").reverse().join("-")

    var data = {
        "id": SessionManager.get("session").user.id,
        "email": email,
        "name": username,
        "password": password,
        "gender": gender,
        "birthday": birthday,
    };

    $.ajax({
        url: consts.BASE_URL + '/api/acceptInvite',
        type: "PUT",
        contentType: "application/json",
        data: JSON.stringify(data),
        dataType: "json",
        success: function(data) {
            var tp = SessionManager.get("session");
            tp.user.name = username;
            SessionManager.set("session", tp);
            alert("Success! You can start managing your store.");
            window.location.href = "./index.html"
        },

        error: function() {
            console.log("erro");
        }

    })
}

const get_cur_info = function() {

    $.ajax({
        url: consts.BASE_URL + '/api/StoreManager?id=' + SessionManager.get("session").user.id,
        type: "GET",
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            $("#store").text(data.store.name)
            $("#email").val(data.user.email)
        },

        error: function() {
            console.log("erro");
        }

    })
}

