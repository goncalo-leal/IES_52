import consts from "./consts.js";
import SessionManager from "./session.js";
import updateView from "./common.js"

var users;

$(document).ready(function() {
    updateView();
    loadTable();

    $("#mySearchText").on('input', function() {
        search();
    })
});


const loadTable = function() {
    $.ajax({
        url: consts.BASE_URL + '/api/StoreManagerShopping/' + SessionManager.get("session").shopping.id,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            if (data) {
                users = data;
                renderTable(data);
            } else {
                console.log("No store managers for this shopping");
            }
        },

        error: function() {
            console.log("erro na call");
        }
    })
}


const renderTable = function (data) {
    $("#managers_body").empty();
    data.forEach(function(e, i) {
        $("#managers_body").append(trTemplate(e.user.name, e.user.email, e.store.name, true));
    })
}


const trTemplate = function (name, email, shop, active) {
    return `
    <tr>
        <td>
            ${name}
        </td>
        <td class="text-center">
            ${email}
        </td>
        <td class="text-center">
            ${shop}
        </td>
        <td class="text-right">
            <input type="checkbox" name="my-checkbox" checked data-bootstrap-switch data-off-color="danger">
        </td>
    </tr>
    `
}


const search = function () {
    var input = $("#mySearchText").val().toLowerCase();
    var temp = [];

    console.log(users[0]);
    users.forEach(function(e, i) {
        var name = e.user.name.toLowerCase();
        if (name != null && name.includes(input)) {
            temp.push(e);
        }
    })

    renderTable(temp);
}
