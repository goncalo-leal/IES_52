import consts from "./consts.js";
import SessionManager from "./session.js";
import { updateView, requestWithToken } from "./common.js";

var shoppingCapacity;
var complete_data;

$(document).ready(function() {
    updateView();

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
    
    loadMaxStoreCapacity()
    $("#capacity").attr({
        "max" : 20,
        "min" : 5
    });

    
    $("#add_store").click(function(){
        var data_complete = {"location":$("#location").val(), "name":$("#store_name").val(),
            "capacity":$("#capacity").val(), "opening":$("#opening").val(), "closing":$("#closing").val(), "img":$("#img_url").val()};
        
        if ($("#location").val()!="" && $("#store_name").val()!="" && $("#capacity").val()!="" && $("#opening").val()!="" && $("#closing").val()!="" && $("#img_url").val()!="") {
            requestWithToken("POST", '/api/stores/addStore/' + SessionManager.get("session").shopping.id, function(data) {
                SweetAlert.fire(
                    'Store Added!',
                    'You added a new store to the shopping!',
                    'success'
                ).then(function() {
                    window.location.href = "./home.html"
                })
            }, 
            
            function() {
                SweetAlert.fire(
                    'Error!',
                    'Error adding the store!',
                    'error'
                ).then(function() {
                    window.location.href = "./add_store.html"
                })
            },
            data_complete)
        }
    })
});

const loadMaxStoreCapacity = function() {
    requestWithToken("GET", '/api/shoppings/Shopping?id=' + SessionManager.get("session").shopping.id, function(data) {
        if (data) {
            if (data) {
                shoppingCapacity = data.capacity;
                storesCapacity(data.stores);
            } else {
                console.log("No data");
            }
        } else {
            console.log("No data");
        }
    }, function() {
        SweetAlert.fire(
            'Error!',
            'Error calling the method! ' + consts.BASE_URL + '/api/Shopping?id=' + SessionManager.get("session").shopping.id ,
            'error'
        ).then(function() {
            window.location.href = "./add_store.html"
        })
    })

    return;
}

const storesCapacity = function(data) {
    var total_stores=0;
    data.forEach(function(e, i) {
        total_stores+=e.capacity;
    })
    
    $("#capacity").attr({
        "max" : shoppingCapacity - total_stores,
        "min" : 1
    });

    return;
}
