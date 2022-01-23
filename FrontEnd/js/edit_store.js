import consts from "./consts.js";
import SessionManager from "./session.js";
import { updateView, requestWithToken } from "./common.js";

var shoppingCapacity;
var maxCapacity;
var store_id

$(document).ready(function () {
    updateView();

    store_id = new URLSearchParams(window.location.search).get('id');
    if (store_id === null) {
        window.location.href = "./store_management.html";
    }
    loadStore(store_id)

    $('#opening_timepicker').datetimepicker({
        format: 'HH:mm',
        use24hours: true,
    });
    
    $('#closing_timepicker').datetimepicker({
        format: 'HH:mm',
        use24hours: true,
    });

    $("#capacity").on('input', function(e) {
        $(this).val($(this).val().replace(/[^0-9]/g, ''));
    });

    // carregar o máximo de capacidade que a loja pode ter
    loadMaxStoreCapacity()

    $("#edit_store").click(function() {
        let store_name = $("#store_name").val(); // document.getElementById("store_name").value()
        let location = $("#location").val();
        let capacity = $("#capacity").val();
        let opening = $("#opening").val().split(":");
        let closing = $("#closing").val().split(":");

        if (store_name === "" || location === "" || capacity === "" || opening === "" || closing === "") {
            // erro: "All fields must be filled"
        }
        else if (parseInt(opening[0]) > parseInt(closing[0]) || 
        (parseInt(opening[0]) == parseInt(closing[0]) && parseInt(opening[1]) > parseInt(closing[1]))) {
            // erro: "Closing must be after opening hours" 
        }
        else {
            var data = {
                "id":       store_id,
                "name":     store_name,
                "location": location,
                "capacity": capacity,
                "opening":  [parseInt(opening[0]), parseInt(opening[1])],
                "closing":  [parseInt(closing[0]), parseInt(closing[1])]
            };

            requestWithToken("PUT", '/api/stores/updateStore', function(data) {
                console.log("Store added");
                window.location.replace("store_management.html");
            }, data)
        }
    });
});

const loadStore = function(store_id) {
    requestWithToken("GET", '/api/stores/Store?id=' + store_id, function(data) {
        if (data) {
            console.log(data);

            $("#store_name").val(data.name);
            $("#location").val(data.location);
            $("#capacity").val(data.capacity);

            let opening_hours = data.opening[0]
            if (opening_hours < 10)
                opening_hours = "0" + opening_hours

            let opening_minutes = data.opening[1]
            if (opening_minutes < 10)
                opening_minutes = "0" + opening_minutes

            let closing_hours = data.closing[0]
            if (closing_hours < 10)
                closing_hours = "0" + closing_hours

            let closing_minutes = data.closing[1]
            if (closing_minutes < 10)
                closing_minutes = "0" + closing_minutes

            $("#opening").val(opening_hours + ":" + opening_minutes)
            $("#closing").val(closing_hours + ":" + closing_minutes)
        } else {
            console.log("No data");
        }
    })
}

const loadMaxStoreCapacity = function() {
    requestWithToken("GET", '/api/shoppings/Shopping?id=' + SessionManager.get("session").shopping.id, function(data) {
        if (data) {
            shoppingCapacity = data.capacity;
            storesCapacity(data.stores);
        } else {
            console.log("No data");
        }
    })
}

const storesCapacity = function(data) {
    var total_stores = 0;
    data.forEach(function(e, i) {
        total_stores += e.capacity;
    });

    maxCapacity = shoppingCapacity - total_stores
    
    $("#capacity").attr({
        "max" : shoppingCapacity - total_stores,
        "min" : 1
    });

    return;
}