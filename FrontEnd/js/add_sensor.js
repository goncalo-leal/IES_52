import consts from "./consts.js";
import SessionManager from "./session.js";
import {updateView, requestWithToken} from "./common.js";

$(document).ready(function() {

    updateView();

    $("#add_sensor").click(function() {
        let type = $("#sensor_type").val();
        let location = $("#sensor_location").val();
        let name = $("#sensor_name").val();

        if (type == "-1" || location == "-1" || name == "") {
            SweetAlert.fire(
                'Invalid form!',
                'All fields are required',
                'error'
            );
        }

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
    
                requestWithToken("POST", "/api/sensorsshopping/entranceShopping/"+SessionManager.get("session").shopping.id, added, error, body);
            });

        }
        else {
            body = {
                "type": type,
                "name": name
            }

            if (location === "Park") {
                requestWithToken("POST", "/api/sensorspark/addSensorPark/"+SessionManager.get("session").shopping.id, added, error, body);
            }
            else if (location === "Store") {
                requestWithToken("POST", "/api/sensorsstore/entranceStore/"+SessionManager.get("session").shopping.id, added, error, body);
            }
        }

    });

});

const added = function() {
    Swal.fire('Saved!', '', 'success')
}

const error = function() {
    Swal.fire('Error', 'Error while adding a new sensor!', 'error')
}