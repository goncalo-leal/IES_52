import consts from "./consts.js";
import SessionManager from "./session.js";
import { requestWithToken, updateView } from "./common.js"

var store = "";
var store_id;
$(document).ready(function() {
    updateView();
    get_cur_info();
    $("#accept").click(function(e) {
        e.preventDefault();
        accept_manager();
    })
    //Date picker
    $('#birthdaydate').datetimepicker({
        format: 'DD-MM-YYYY',
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


    requestWithToken("PUT", '/api/users/acceptInvite',
    function(data) {
        var tp = SessionManager.get("session");
        tp.user.name = username;
        SessionManager.set("session", tp);
        SweetAlert.fire(
            'Signed In!',
            'You can now start managing ' +  store,
            'success'
        ).then(function() {
            window.location.href = "./store.html?id=" + store_id;
        })
    },
    function() {
        SweetAlert.fire(
            'Error!',
            'Something went wrong',
            'error'
        ).then(function() {
        })
    },

    data
    )
}

const get_cur_info = function() {
    requestWithToken("GET", '/api/storemanagers/StoreManager?id=' + SessionManager.get("session").user.id,
    
    function(data) {
        store_id = data.store.id;
        store = data.store.name;
        $("#store").text(data.store.name)
        $("#email").val(data.user.email) 
    },

    function() {
        console.log("erro");
    }
    )
}

