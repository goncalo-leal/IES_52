import consts from "./consts.js";
import SessionManager from "./session.js";
import updateView from "./common.js"

$(document).ready(function() {
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

    updateView();
});