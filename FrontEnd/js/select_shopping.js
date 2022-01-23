import consts from "./consts.js";
import SessionManager from "./session.js";
import { updateView, requestWithToken } from "./common.js";

$(document).ready(function() {

    populateSelect();

    $("#choose_shopping").click(function() {
        $("#error_msg").text("");
        let shopping_id = $("#shoppings_select").val();

        if (shopping_id == -1) {
            $("#error_msg").text("You must select a valid shopping!");
        }
        else {
            SessionManager.set("shopping", shopping_id);
            window.location.href = "index.html"
        }
    });

});

const populateSelect = function() {
    $.ajax({
        url: consts.BASE_URL + '/api/shoppings/Shoppings',
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            if (data.length > 0) {
                let options = $("#shoppings_select").html();
                data.forEach(element => {
                    options = options + '<option value="'+ element["id"] +'">'+ element["name"] +'</option>'
                });
                $("#shoppings_select").html(options);
            } else {
                console.log("No data");
            }
        },

        error: function() {
            console.log(" erro na call");
        }
    });
}