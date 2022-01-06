import consts from "./consts.js";
import SessionManager from "./session.js";
import updateView from "./common.js"

var users;
var states=[];

$(document).ready(function() {
    updateView();
    userStates();

    loadTable();

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
    
    for (var i=0; i< states.length; i++){
        selectVal = states[i];
        if (selectVal == description){
            selectInput  = '<option value='+ i +' selected>'+ selectVal +'</option>';
        }
        else{
            if (selectVal != "Waiting approvement"){
                selectInput  = '<option value='+ i +'>'+ selectVal +'</option>';
            }
        }
        $("select[name='states"+number+"']").append(selectInput);
    }
}



const renderTable = function (data) {
    var number=0;
    $("#managers_body").empty();
    data.forEach(function(e, i) {
        $("#managers_body").append(trTemplate(e.user.name, e.user.email, e.store.name, e.user.id, i));
        loadUserStates(number++, e.user.state.description);
    })
    changeState()
}


const changeState = function(){
    
    $('select').on('change',function (e) { 
        var optionSelected = $(this).find("option:selected");
        var textSelected   = optionSelected.text();
        var idToUpdate = parseInt($(this).parent().parent().find('#userId').text());

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
