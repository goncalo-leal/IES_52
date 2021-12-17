import consts from "./consts.js";
import SessionManager from "./session.js";
import updateView from "./common.js"

$(document).ready(function() {
    updateView();
    loadShoppingInfo();

    $("#mySearchText").on('input', function() {
        search();
    })
})



var stores;
var parks;

const loadShoppingInfo = function() {
    $("#s_name").text(SessionManager.get("session").shopping.name);

    $.ajax({
        url: consts.BASE_URL + '/api/Shopping?id=' + SessionManager.get("session").shopping.id,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            if (data) {
                var cur_capacity = 0;
                stores = data.stores;
                $("#shopcurcap").text(data.current_capacity);
                $("#shopmaxcap").text(data.capacity);

                parks = data.parks;

                if (parks.length > 0) {
                    console.log("calculate parks...");
                } else {
                    $("#parkingTab").empty();
                }

                renderTable(stores);
            } else {
                console.log("No store for this shopping");
            }
        },

        error: function() {
            console.log("erro na call");
        }
    })
}


const renderTable = function (data) {
    $("#stores_body").empty();
    data.forEach(function(e, i) {
        $("#stores_body").append(trTemplate(e.name, e.current_capacity, 10));
    })
}


const trTemplate = function (name, ocupation, growth) {
    return `
    <tr>
        <td>
            ${name}
        </td>
        <td class="text-center">
            ${ocupation}
        </td>
        <td class="text-center">
            <small class="text-success mr-1">
                <i class="fas fa-arrow-up"></i>
                ${growth}%
            </small>
        </td>
        <td class="text-right">
            <a href="#" class="text-muted">
                <i class="fas fa-search"></i>
            </a>
        </td>
    </tr>
    `
}


const search = function () {
    var input = $("#mySearchText").val().toLowerCase();
    var temp = [];

    stores.forEach(function(e, i) {
        var name = e.name.toLowerCase();
        if (name != null && name.includes(input)) {
            temp.push(e);
        }
    })

    renderTable(temp);
}