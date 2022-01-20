import consts from "./consts.js";
import SessionManager from "./session.js";

$(document).ready(function () {
    loadUserInformation();
    $("#submit").click(function (){ 
        updateData()
    });
});

var nome, email, pass, gender, id, birthday, state_id, state_desc;
var n_nome, n_email, n_pass1, n_pass2;


const loadUserInformation = function(){
    
    $.ajax({
        url: consts.BASE_URL + '/api/User?id=' + SessionManager.get("session").user.id,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            if (data) {
                $("#nome_bar").html(data.name);
                nome = data.name;
                $("#nome").val(data.name);
                $("#email").val(data.email);
                email = data.email;
                pass = data.password;
                gender = data.gender;
                id = data.id;
                state_id = data.state.id;
                state_desc = data.state.description;
                $("#password").val(pass);
                $("#confirm_password").val(pass);
                $("#dateTimePicker").val(data.birthday);
                $("#dateTimePicker").attr('readonly', 'readonly');
                

            } else {
                console.log("No store for this shopping");
            }
        },

        error: function() {
            console.log("erro na call");
        }
    })
}

const updateData = function(){
    n_nome=$("#nome").val();
    n_email=$("#email").val();
    n_pass1=$("#password").val();
    n_pass2=$("#confirm_password").val();
    
    if (n_pass1!=n_pass2){
        alert("Passwords doesn't match!");
    }
    else{
        if (nome==n_nome && email == n_email && n_pass1 ==pass){
            alert("Nothing updated");
        }
        else{
            var data = {"id":id,"password":pass,"email":n_email,"name":n_nome,"gender":gender,"birthday":birthday,"state":{"id":state_id,"description":state_desc}, "authority":SessionManager.get("session").user.authority}

            $.ajax({
                url: consts.BASE_URL + '/api/updateUser',
                type: "PUT", 
                data: JSON.stringify(data), 
                contentType: "application/json",
                dataType: "json",
            }).done(function () {
                var tmp = SessionManager.get("session");
                tmp.user=data
                SessionManager.set("session", tmp);
                alert("Updated")
            })
        }
    }

}