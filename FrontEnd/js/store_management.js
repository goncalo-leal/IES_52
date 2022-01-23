import consts from "./consts.js";
import SessionManager from "./session.js";
import { updateView, requestWithToken } from "./common.js"

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
    requestWithToken("GET", '/api/shoppings/Shoppings?id=' + SessionManager.get("session").shopping.id, function(data) {
        console.log(data);

        table.clear();
        $("#stores_body").empty();

        for (var i = 0; i < data[0].stores.length; i++) {
            requestWithToken("GET", '/api/stores/Store?id=' + data[0].stores[i]["id"], function(store) {
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
            } )
        }
    })
};
