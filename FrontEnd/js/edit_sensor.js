import consts from "./consts.js";
import SessionManager from "./session.js";
import {updateView, requestWithToken} from "./common.js";

var sensor_id;

$(document).ready(function() {

    updateView();
    sensor_id = new URLSearchParams(window.location.search).get('id');
    if (sensor_id === null) {
        window.location.href = "./sensors_management.html";
    }

    requestWithToken("GET", "/api/sensors/Sensor?id="+sensor_id, loadSensor, error);

    $("#edit_sensor").click(function () {
        let name = $("#sensor_name").val();
        let type = $("#sensor_type").val();

        let body = {
            "name": name,
            "type": type,
            "id": sensor_id
        }

        requestWithToken("PUT", '/api/sensors/updateSensor', function(data) {
            SweetAlert.fire(
                'Store Updated!',
                'You updated the store!',
                'success'
            ).then(function() {
                window.location.href = "./sensors_management.html"
            })
        },        
        function() {
            SweetAlert.fire(
                'Error!',
                'Error editing the store!',
                'error'
            ).then(function() {
                window.location.href = "./sensors_management.html"
            })
        },
        body)
    });

});

const loadSensor = function(data) {
    $("#sensor_name").val(data.name);
    $("#"+data.type).attr('selected', 'selected')
}

const error = function() {
    SweetAlert.fire(
        'Error!',
        'Error loading sensor data!',
        'error'
    ).then(function() {
        window.location.href = "./sensors_management.html";
    });
}