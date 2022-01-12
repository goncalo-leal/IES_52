import consts from "./consts.js";
import SessionManager from "./session.js";
import updateView from "./common.js"

var table;
var stores = [];

$(document).ready(function () {
    updateView();
    loadStores();

    table = $("#stores").DataTable({
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

const loadStores = function () {
    $.ajax({
        url: consts.BASE_URL + '/api/Shoppings?id=' + SessionManager.get("session").shopping.id,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            console.log(data);

            table.clear();
            $("#stores_body").empty();

            for (var i = 0; i < data[0].stores.length; i++) {
                $.ajax({
                    url: consts.BASE_URL + '/api/Store?id=' + data[0].stores[i]["id"],
                    type: "GET", 
                    contentType: "application/json",
                    dataType: "json",
                    success: function(store) {
                        stores.push(store)
                        var tr = [];
                        tr.push([
                            '<p>' + store.name + '</p>', 
                            '<p class="text-center">' + store.location + '</p>',
                            '<p class="text-center">' + store.capacity + '</p>',
                            '<p class="text-center">' + store.opening + '</p>',
                            '<p class="text-center">' + store.closing + '</p>',
                            '<a class="btn btn-default btn-sm float-right" href="edit_store.html?id='+ store.id +'"><i class="fas fa-edit"></i></a>'
                        ]);
                        console.log(tr)
                        table.rows.add( tr ).draw();
                    }
                });
            }
        },
        error: function() {
            console.log("erro na call");
        }
    });
};

const renderTable = function (stores) {
    var table_data = [];

    $("#stores_body").empty();
    stores.forEach(function(store, i) {
        tr = [];
        tr.push('<p>' + store.name + '</p>');
        tr.push('<p class="text-center">' + store.location + '</p>');
        tr.push('<p class="text-center">' + store.capacity + '</p>');
        tr.push('<p class="text-center">' + store.opening + '</p>');
        tr.push('<p class="text-center">' + store.closing + '</p>');
        tr.push('<a class="btn btn-default btn-sm" href="edit_store.html?id='+ store.id +'"><i class="fas fa-edit"></i></a>');

        table_data.push(tr);
    });

    table.clear();
    table.rows.add( table_data ).draw();
}