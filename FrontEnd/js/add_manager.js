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

    console.log(data);


    requestWithToken("POST", '/api/storemanagers/addStoreManager/' + store, function(data) {
        alert("Success! Manager added.");
        window.location.href = "./home.html"
    }, data)
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
    })
}
