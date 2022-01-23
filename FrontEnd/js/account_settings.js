import { requestWithToken } from "./common.js";
import consts from "./consts.js";
import SessionManager from "./session.js";

$(document).ready(function () {
    loadUserInformation();
    $("#submit").click(function (e){
        e.preventDefault() 
        updateData()
    });
});

var nome, email, pass, gender, id, birthday, state_id, state_desc;
var n_nome, n_email, n_pass1, n_pass2;


const loadUserInformation = function(){
    requestWithToken("GET", '/api/users/User?id=' + SessionManager.get("session").shopping.id, function(data) {
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
            //$("#password").val(pass);
            //$("#confirm_password").val(pass);
            $("#dateTimePicker").val(data.birthday);
            $("#dateTimePicker").attr('readonly', 'readonly');
            

        } else {
            console.log("No store for this shopping");
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
            console.log(SessionManager.get("session"));
        }
        else{
            var data = {"id":id,"password":pass,"email":n_email,"name":n_nome,"gender":gender,"birthday":birthday,"state":{"id":state_id,"description":state_desc}}
            requestWithToken("PUT", '/api/users/updateUser', function(data) {
                alert("Updated");
                var tp = SessionManager.get("session");

                tp.user.name = n_nome;
                SessionManager.set("session", tp);
                window.location.href = "./home.html";
            }, data)            
        }
    }

}