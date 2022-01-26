import consts from "./consts.js";
import SessionManager from "./session.js";
import {updateView, requestWithToken} from "./common.js";

var shopping = []
var stores = []
var parks = []
var select_options = {}

$(document).ready(function() {

    updateView();

    $("#add_sensor").click(function() {
        let type = $("#sensor_type").val();
        let location = $("#sensor_location").val();
        let name = $("#sensor_name").val();
        let association = $("#sensor_association").val();

        if (type == "-1" || location == "-1" || name == "" || association == "-1") {
            SweetAlert.fire(
                'Invalid form!',
                'All fields are required',
                'error'
            );
        }
        else {

            let body;
            if (location === "Shopping") {
                let park_connected;
                SweetAlert.fire({
                    title: 'Shopping Sensor',
                    text: 'Is this sensor connected to a park?',
                    type: 'question',
                    showDenyButton: true,
                    confirmButtonText: 'Yes',
                    denyButtonText: 'No',
                }).then(function(result) {
                    if (result.isConfirmed) {
                        park_connected = true;
                    } else if (result.isDenied) {
                        park_connected = false; 
                    }

                    body = {
                        "type": type,
                        "var": park_connected,
                        "name": name
                    };

                    if (!association.includes("shopping_")){
                        Swal.fire('Error', 'You must choose a shopping', 'error');
                    }
                    else {
                        requestWithToken(
                            "POST",
                            "/api/sensorsshopping/addSensorShopping/"+association.replace("shopping_", ""),
                            added,
                            error,
                            body
                        );
                    }
                });

            }
            else {
                body = {
                    "type": type,
                    "name": name
                }

                if (location === "Park") {

                    if (!association.includes("park_")){
                        Swal.fire('Error', 'You must choose a park', 'error');
                    }
                    else {
                        requestWithToken(
                            "POST",
                            "/api/sensorspark/addSensorPark/"+association.replace("park_", ""),
                            added,
                            error,
                            body
                        );
                    }
                }
                else if (location === "Store") {
                    if (!association.includes("store_")){
                        Swal.fire('Error', 'You must choose a store', 'error');
                    }
                    else {
                        requestWithToken(
                            "POST",
                            "/api/sensorsstore/addSensorStore/"+association.replace("store_", ""),
                            added,
                            error,
                            body
                        );
                    }
                }
            }
        }
    });

    getShoppingInfo();
    getShoppingStores();
    getShoppingParks();
});

const added = function() {
    Swal.fire('Saved!', '', 'success').then(() => document.location.href = "sensors_management.html");
}

const error = function(error) {
    Swal.fire('Error', 'Error while adding a new sensor!', 'error')
    console.log(error)
}

const getShoppingInfo = function() {
    requestWithToken("GET", "/api/shoppings/Shopping?id="+SessionManager.get("session").shopping.id, addShopping, errorGet);
}

const getShoppingStores = function() {
    requestWithToken("GET", "/api/shoppings/storesShopping/"+SessionManager.get("session").shopping.id, addStores, errorGet);
}

const getShoppingParks = function() {
    requestWithToken("GET", "/api/shoppings/parksShopping/"+SessionManager.get("session").shopping.id, addParks, errorGet);
}

const addShopping = function(data) {
    shopping = data;
    console.log("shopping", data);
    $("#sensor_association").html(
        $("#sensor_association").html()+"<option value='shopping_"+data.id+"'>"+data.name+"</option>"
    );
}

const addStores = function(data) {
    stores = data;
    console.log("stores", data);
    let options = $("#sensor_association").html();
    data.forEach(function(store) {
        options = options + "<option value='store_"+store.id+"'>"+store.name+"</option>";
    });
    $("#sensor_association").html(options);
}

const addParks = function(data) {
    parks = data;
    console.log("parks", data);
    let options = $("#sensor_association").html();
    data.forEach(function(park) {
        options = options + "<option value='park_"+park.id+"'>"+park.name+"</option>";
    });
    $("#sensor_association").html(options);
}

const errorGet = function() {
    Swal.fire(
        'Error', 
        'Error while getting shopping info!',
        'error'
    ).then(function() {
        window.location.href = "./sensors_management.html"
    });
}