import consts from "./consts.js";
import SessionManager from "./session.js";
import updateView from "./common.js"

var store_id = new URLSearchParams(window.location.search).get('id');

if (store_id === null) {
    window.location.href = "./index.html";
}

var store;

$(document).ready(function() {
    fetchStore();
    updateView();

})


const updateViewStore = function() {
    console.log(store);
    $('#store_name').text(store.name);
    $('#store_location').text(store.location);
}



const fetchStore = function() {
    $.ajax({
        url: consts.BASE_URL + '/api/Store?id=' + store_id,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            if (data) {
                store = data;
                updateViewStore()
            }
        },

        error: function() {
            console.log(" erro na call");
        }
    });
}