import consts from "./consts.js";
import SessionManager from "./session.js";
import updateView from "./common.js"

var users;
var table;
var states=[];
var base_url = consts.BASE_URL

$(document).ready(function() {

    table = $("#managers").DataTable({
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
            { "searchable": true, "targets": 2 },
            { "searchable": false, orderable: false, "targets": 3 },
        ],
        "dom": '<"top"i>rt<"bottom"><"clear">'
    });
    
    $('#mySearchButton').on( 'keyup click', function () {
        table.search($('#mySearchText').val()).draw();
    } );

    $("input[data-bootstrap-switch]").each(function(){
        $(this).bootstrapSwitch();
    })

    updateView();
    userStates();

    loadTable();

    $("#base_url").text(consts.BASE_URL);

    $("#mySearchText").on('input', function() {
        search();
    })
});

const userStates = function() {
    var estados='';
    $.ajax({
        url: consts.BASE_URL + '/api/UserStates/',
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            if (data) {
                estados = data;
                for (var i=0; i< estados.length; i++){
                    states.push(estados[i].description);
                }
                console.log(states)
            }
        },
        error: function() {
            console.log("erro na call");
        }
    })
}

const loadTable = function() {
    $.ajax({
        url: consts.BASE_URL + '/api/StoreManagerShopping/' + SessionManager.get("session").shopping.id,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            if (data) {
                users = data;
                renderTable(data);
            } else {
                console.log("No store managers for this shopping");
            }
        },

        error: function() {
            console.log("erro na call");
        }
    })
}

const loadUserStates = function(number, description) {
    var selectVal='';
    var selectInput='';
    
    console.log(states)
    for (var i=0; i< states.length; i++){
        selectVal = states[i];
        if (selectVal == description){
            selectInput  += '<option value='+ i +' selected>'+ selectVal +'</option>';
        }
        else{
            if (selectVal != "Waiting approvement"){
                selectInput  += '<option value='+ i +'>'+ selectVal +'</option>';
            }
        }
    }
    console.log(selectInput)
    return selectInput;
}

const renderTable = function (data) {
    var table_data = []

    var number=0;
    $("#managers_body").empty();
    data.forEach(function(e, i) {
        var options = loadUserStates(number++, e.user.state.description);
        table_data.push(['<p>'+e.user.name+'</p>', '<p class="text-center">'+e.user.email+'</p>', '<p class="text-center">'+e.store.name+'</p>', '<p class="text-center" ><select id="user_'+ e.user.id +'" name="states'+ i +'" class="browser-default custom-select" style="max-width:200px;">'+ options +'</select></p>']); //onchange="changeState('+ e.user.id +')"
    })
   
    table.clear();
    table.rows.add( table_data ).draw();
    changeState()
}

const changeState = function () {
    $('select').on('change',function (e) { 
        var optionSelected = $(this).find("option:selected");
        var textSelected   = optionSelected.text();
        var idToUpdate = $(this).attr('id').replace('user_', '');

        console.log(textSelected, idToUpdate)
        if (textSelected === 'Approved'){
            $.ajax({
                url: consts.BASE_URL + '/api/updateAcceptStoreManager/'+idToUpdate,
                type: "PUT", 
            })
        }

        if (textSelected === 'Blocked'){
            $.ajax({
                url: consts.BASE_URL + '/api/updateBlockStoreManager/'+idToUpdate,
                type: "PUT", 
            })
        }
        
    });
}

const trTemplate = function (name, email, shop, id ,i) {
    return `
    <tr>
        <td>
            ${name}
        </td>
        <td class="text-center">
            ${email}
        </td>
        <td class="text-center">
            ${shop}
        </td>
        <td class="text-center">
            <select name="states${i}" class="browser-default custom-select" style="max-width:200px;">
            </select>
        </td>
        <td id="userId" style="display:none">
            ${id}
        </td>
    </tr>
    `
}


const search = function () {
    var input = $("#mySearchText").val().toLowerCase();
    var temp = [];

    console.log(users[0]);
    users.forEach(function(e, i) {
        var name = e.user.name.toLowerCase();
        if (name != null && name.includes(input)) {
            temp.push(e);
        }
    })

    renderTable(temp);
}
