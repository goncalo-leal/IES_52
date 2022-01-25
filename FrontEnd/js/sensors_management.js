import consts from "./consts.js";
import SessionManager from "./session.js";
import {updateView, requestWithToken} from "./common.js";

var table_shopping;
var table_park;
var table_store;

$(document).ready(function() {

    updateView();

    table_shopping = $("#shopping_sensors").DataTable({
        "lengthChange": false,
        "searching": true,
        "ordering": true,
        "autoWidth": false,
        "responsive": true,
        "paging": false,
        "lengthChange": false,
        "info": false,
        "columnDefs": [
            { "searchable": true, "targets": 0 },
            { "searchable": true, "targets": 1 },
            { "searchable": false, orderable: false, "targets": 2 },
            { "searchable": false, orderable: false, "targets": 3 },
        ],
        "dom": '<"top"i>rt<"bottom"><"clear">'
    });

    $('#search_shopping').on( 'input', function () {
        table_shopping.search($('#search_shopping').val()).draw();
    });

    table_store = $("#stores_sensors").DataTable({
        "lengthChange": false,
        "searching": true,
        "ordering": true,
        "autoWidth": false,
        "responsive": true,
        "paging": false,
        "lengthChange": false,
        "info": false,
        "columnDefs": [
            { "searchable": true, "targets": 0 },
            { "searchable": true, "targets": 1 },
            { "searchable": false, orderable: false, "targets": 2 },
        ],
        "dom": '<"top"i>rt<"bottom"><"clear">'
    });

    $('#search_stores').on( 'input', function () {
        table_store.search($('#search_stores').val()).draw();
    });

    table_park = $("#parks_sensors").DataTable({
        "lengthChange": false,
        "searching": true,
        "ordering": true,
        "autoWidth": false,
        "responsive": true,
        "paging": false,
        "lengthChange": false,
        "info": false,
        "columnDefs": [
            { "searchable": true, "targets": 0 },
            { "searchable": true, "targets": 1 },
            { "searchable": false, orderable: false, "targets": 2 },
        ],
        "dom": '<"top"i>rt<"bottom"><"clear">'
    });

    $('#search_park').on( 'input', function () {
        table_park.search($('#search_park').val()).draw();
    });

    loadSensors();

});

const loadSensors = function (data) {
    $.ajax({
        url: consts.BASE_URL + '/mq/ShoppingAllSensors/' + SessionManager.get("session").shopping.id,
        type: "GET",
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            console.log(data);
            renderShoppingTable(data.shopping);
            renderStoreTable(data.store);
            renderParkTable(data.park);
        },

        error: function() {
            SweetAlert.fire(
                'Error!',
                'Error while getting sensors!',
                'error'
            )
        }
    });
};

const renderShoppingTable = function (sensors) {
    let tr = [];
    sensors.forEach(function(sensor) {

        tr.push([
            '<p>' + sensor.sensor.name + '</p>',
            '<p class="text-center">' + sensor.sensor.type + '</p>',
            '<p class="text-center">' + sensor.park_associated + '</p>',
            '<a class="btn btn-default btn-sm float-right" href="edit_sensor.html?id='+ sensor.sensor.id +'"><i class="fas fa-edit"></i></a>'
        ]);

    });
    table_shopping.rows.add( tr ).draw();
}

const renderStoreTable = function (sensors) {
    let tr = [];
    sensors.forEach(function(sensor) {

        tr.push([
            '<p>' + sensor.sensor.name + '</p>',
            '<p class="text-center">' + sensor.sensor.type + '</p>',
            '<a class="btn btn-default btn-sm float-right" href="edit_sensor.html?id='+ sensor.sensor.id +'"><i class="fas fa-edit"></i></a>'
        ]);

    });
    table_store.rows.add( tr ).draw();
}

const renderParkTable = function (sensors) {
    let tr = [];
    sensors.forEach(function(sensor) {

        tr.push([
            '<p>' + sensor.sensor.name + '</p>',
            '<p class="text-center">' + sensor.sensor.type + '</p>',
            '<a class="btn btn-default btn-sm float-right" href="edit_sensor.html?id='+ sensor.sensor.id +'"><i class="fas fa-edit"></i></a>'
        ]);

    });
    table_park.rows.add( tr ).draw();
}