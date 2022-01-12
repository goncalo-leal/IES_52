import consts from "./consts.js";
import SessionManager from "./session.js";
import updateView from "./common.js";


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

});


const loadMaxStoreCapacity = function() {
    var shoppingCapacity;
    $.ajax({
        url: consts.BASE_URL + '/api/Shopping?id=' + SessionManager.get("session").shopping.id,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            if (data) {
                shoppingCapacity = data.capacity;
            } else {
                console.log("No data");
            }

        },

        error: function() {
            console.log("erro na call");
        }
    })
}
