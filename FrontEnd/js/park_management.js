import consts from "./consts.js";
import SessionManager from "./session.js";
import {updateView, requestWithToken} from "./common.js"

var table;
var parks = [];

$(document).ready(function () {
    updateView();
    loadParks();

    table = $("#parks").DataTable({
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
            { "searchable": false, "targets": 2 },
            { "searchable": false, "targets": 3 },
            { "searchable": false, "targets": 4 },
            { "searchable": false, orderable: false, "targets": 5 },
        ],
        "dom": '<"top"i>rt<"bottom"><"clear">'
    });

    $('#mySearchText').on( 'input', function () {
        table.search($('#mySearchText').val()).draw();
    });
});

const loadParks = function () {
    requestWithToken("GET", 
                    '/api/shoppings/Shoppings?id=' + SessionManager.get("session").shopping.id, 
                    function(data){
                        console.log(data);
                        table.clear();
                        $("#parks_body").empty();
                        for (var i = 0; i < data[0].parks.length; i++) {
                            requestWithToken("GET", '/api/parks/Park?id=' + data[0].parks[i]["id"], 
                                function(park) {
                                    parks.push(park)
                                    var tr = [];
                                    tr.push([
                                        '<p>' + park.name + '</p>', 
                                        '<p class="text-center">' + park.location + '</p>',
                                        '<p class="text-center">' + park.capacity + '</p>',
                                        '<p class="text-center">' + park.opening + '</p>',
                                        '<p class="text-center">' + park.closing + '</p>',
                                        '<a class="btn btn-default btn-sm float-right" href="edit_park.html?id='+ park.id +'"><i class="fas fa-edit"></i></a>'
                                    ]);
                                    console.log(tr)
                                    table.rows.add( tr ).draw();
                                })
                        }
                    },
                    function(){
                        console.log("erro na call");
                    }
                    );
};

const renderTable = function (parks) {
    var table_data = [];

    $("#parks_body").empty();
    parks.forEach(function(store, i) {
        tr = [];
        tr.push('<p>' + parks.name + '</p>');
        tr.push('<p class="text-center">' + parks.location + '</p>');
        tr.push('<p class="text-center">' + parks.capacity + '</p>');
        tr.push('<p class="text-center">' + parks.opening + '</p>');
        tr.push('<p class="text-center">' + parks.closing + '</p>');
        tr.push('<a class="btn btn-default btn-sm" href="edit_store.html?id='+ parks.id +'"><i class="fas fa-edit"></i></a>');

        table_data.push(tr);
    });

    table.clear();
    table.rows.add( table_data ).draw();
}