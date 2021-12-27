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

const loadUserStates = function() {
    var estados='';
    var selectVal='';
    var selectInput='';

    $.ajax({
        url: consts.BASE_URL + '/api/UserStates/',
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            if (data) {
                estados = data;
                for (var i=0; i< estados.length; i++){
                    selectVal = estados[i].description;
                    selectInput  = '<option value='+ selectVal +'>'+ selectVal +'</option>';
                    $('select[name="states"]').append(selectInput);
                }
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
        $("#managers_body").append(trTemplate(e.user.name, e.user.email, e.store.name, e.user.state.description));
    })
    loadUserStates();
}


const trTemplate = function (name, email, shop, description) {
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
        <td class="text-center">
            <select name="states" class="browser-default custom-select" style="max-width:200px;">
                <option selected="${description}">${description}</option>
            </select>
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
