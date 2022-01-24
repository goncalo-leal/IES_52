import consts from "./consts.js";
import SessionManager from "./session.js";


const updateView = function () {
    if (SessionManager.get("session") === null && window.location.pathname != "/index.html") {
        window.location.href = "./index.html";
    }

    $("body").fadeIn(300);
}

const requestWithToken = function(type, path, callback, error, data) {
    var ajaxConf = {
        url: consts.BASE_URL + path,
        crossDomain: true,
        type: type, 
        contentType: "application/json",
        headers: { 'Authorization': 'Bearer ' + SessionManager.get("token") },
        success: function(data) {
            callback(data);
        },

        error: function(data) {
            //Authorization related errors
            if (data.error) {
                switch(data.code) {
                    case 401:
                        //codigo quado o user nao ta logged in

                    case 403:
                        //codigo quando o user ta logged in Mas nao te preMissao para aceder aos endpoints
                }

            //Logic related errors
            } else {
                if (error != null) {
                    error();
                }
            }
        }
    }

    if (data) {
        ajaxConf.data = JSON.stringify(data);
        ajaxConf.dataType = "json";
    }


    $.ajax(ajaxConf);
}

export { updateView, requestWithToken };