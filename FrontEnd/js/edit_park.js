import consts from "./consts.js";
import SessionManager from "./session.js";
import updateView from "./common.js";

var shoppingCapacity;
var maxCapacity;
var park_id

$(document).ready(function () {
    updateView();

    park_id = new URLSearchParams(window.location.search).get('id');
    if (park_id === null) {
        window.location.href = "./park_management.html";
    }
    loadPark(park_id)

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


    $("#edit_park").click(function() {
        let park_name = $("#park_name").val(); // document.getElementById("store_name").value()
        let location = $("#location").val();
        let capacity = $("#capacity").val();
        let opening = $("#opening").val().split(":");
        let closing = $("#closing").val().split(":");

        if (park_name === "" || location === "" || capacity === "" || opening === "" || closing === "") {
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
                "id":       park_id,
                "name":     park_name,
                "location": location,
                "capacity": capacity,
                "opening":  [parseInt(opening[0]), parseInt(opening[1])],
                "closing":  [parseInt(closing[0]), parseInt(closing[1])]
            };

            $.ajax({
                url: consts.BASE_URL + '/api/updatePark',
                type: "PUT", 
                data: JSON.stringify(data),
                contentType: "application/json",
                dataType: "json",
                success: function() {
                    SweetAlert.fire(
                        'Park Updated!',
                        'You updated the park!',
                        'success'
                    ).then(function() {
                        window.location.href = "./park_management.html"
                    })
                },        
                error: function() {
                    SweetAlert.fire(
                        'Error!',
                        'Error updating the park!',
                        'error'
                    )
                }
            });
        }
    });
});

const loadPark = function(park_id) {
    $.ajax({
        url: consts.BASE_URL + '/api/Park?id=' + park_id,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            if (data) {
                console.log(data);

                $("#park_name").val(data.name);
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
                'Error loading park data!',
                'error'
            )
        }
    });
}
