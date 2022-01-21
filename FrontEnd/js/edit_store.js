import consts from "./consts.js";
import SessionManager from "./session.js";
import updateView from "./common.js";

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
            SweetAlert.fire(
                'Error!',
                'All fields must be filled!',
                'error'
            )
        }
        else if (parseInt(opening[0]) > parseInt(closing[0]) || 
        (parseInt(opening[0]) == parseInt(closing[0]) && parseInt(opening[1]) > parseInt(closing[1]))) {
            // erro: "Closing must be after opening hours" 
            SweetAlert.fire(
                'Error!',
                'Closing must be after opening hours!',
                'error'
            )
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

            $.ajax({
                url: consts.BASE_URL + '/api/updateStore',
                type: "PUT", 
                data: JSON.stringify(data),
                contentType: "application/json",
                dataType: "json",
                success: function() {
                    SweetAlert.fire(
                        'Store Updated!',
                        'You updated the store!',
                        'success'
                    ).then(function() {
                        window.location.href = "./store_management.html"
                    })
                },        
                error: function() {
                    SweetAlert.fire(
                        'Error!',
                        'Error editing the store!',
                        'error'
                    ).then(function() {
                        window.location.href = "./add_store.html"
                    })
                }
            });
        }
    });
});

const loadStore = function(store_id) {
    $.ajax({
        url: consts.BASE_URL + '/api/Store?id=' + store_id,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
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
        },
        error: function() {
            SweetAlert.fire(
                'Error!',
                'Error loading store data!',
                'error'
            )
        }
    });
}

const loadMaxStoreCapacity = function() {
    $.ajax({
        url: consts.BASE_URL + '/api/Shopping?id=' + SessionManager.get("session").shopping.id,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            if (data) {
                shoppingCapacity = data.capacity;
                storesCapacity(data.stores);
            } else {
                console.log("No data");
            }

        },

        error: function() {
            SweetAlert.fire(
                'Error!',
                'Error loading shopping data!',
                'error'
            )
        }
    })
    return;
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