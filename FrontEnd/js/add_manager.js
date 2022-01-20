import consts from "./consts.js";
import SessionManager from "./session.js";
import updateView from "./common.js"

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

    $.ajax({
        url: consts.BASE_URL + '/api/addStoreManager/' + store,
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(data),
        dataType: "json",
        success: function(data) {
            alert("Success! Manager added.");
            window.location.href = "./home.html"
        },

        error: function() {
            console.log("erro");
        }

    })
}

const loadStores = function() {
    $.ajax({
        url: consts.BASE_URL + '/api/storesShopping/' + SessionManager.get("session").shopping.id,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            if (data) {
                data.forEach(function(e, i) {
                    $("#store").append($("<option></option>").val(e.id).text(e.name));
                })
            } else {
                console.log("No stores for this shopping");
            }
        },

        error: function() {
            console.log("erro na call");
        }
    })
}
