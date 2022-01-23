import consts from "./consts.js";
import SessionManager from "./session.js";
import { updateView, requestWithToken } from "./common.js"

$(document).ready(function() {

    updateView();
    loadStores();
    $("#add_manager").click(function() {
        add_manager();
    })

    //Date picker
    $('#birthdaydate').datetimepicker({
        format: 'L'
    });
});

const add_manager = function() {
    var email = $("#email").val();
    var username = $("#username").val()
    var gender = $('input[type="radio"][name="gender"]:checked').val()
    var birthday = $("#birthdaydate input").val().split("/").reverse().join("-")
    console.log(birthday);

    var store = $("#store :selected").val()

    var data = {
        "email": email,
        "name": username,
        "password": username,
        "gender": gender,
        "birthday": birthday,
        "store": store
    };

    


    requestWithToken("POST", '/api/storemanagers/addStoreManager/' + store, function(data) {
        SweetAlert.fire(
            'Store Manager Added!',
            'You added a new store manager!',
            'success'
        ).then(function() {
            window.location.href = "./user_management.html"
        })
    }, 
    function() {
        SweetAlert.fire(
            "Can't add store manager!",
            'Error adding new store manager',
            'error'
        ).then(function() {
            window.location.href = "./add_user.html"
        })
    },
    data)
}

const loadStores = function() {

    requestWithToken("GET", '/api/shoppings/storesShopping/' + SessionManager.get("session").shopping.id, function(data) {
        if (data) {
            data.forEach(function(e, i) {
                $("#store").append($("<option></option>").val(e.id).text(e.name));
            })
        } else {
            console.log("No stores for this shopping");
        }
    },

    function() {
        SweetAlert.fire(
            "Error!",
            'Cannot load data!',
            'error'
        ).then(function() {
            window.location.href = "./add_user.html"
        })
    })
}
