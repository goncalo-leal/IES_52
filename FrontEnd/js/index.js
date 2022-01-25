import consts from "./consts.js";
import SessionManager from "./session.js";
import { updateView } from "./common.js"

var stores = [];
var parks = [];
var total_parking_capacity = 0;
var current_parking_capacity = 0;
var open_stores = 0;


var shopping_id

$(document).ready(function() {
    console.log(SessionManager.get("session"))
    if (SessionManager.get("session") !== null) {
        SessionManager.set("shopping", SessionManager.get("session").shopping.id)
    }
    
    if (SessionManager.get("shopping") === null){
        window.location.href = "./select_shopping.html";
    }
    shopping_id = SessionManager.get("shopping")
    initialize();
    setInterval(initialize, 60000);
})



const initialize = function() {
    console.log("reload");
    update_index_view();
}

const update_index_view = function() {
    render_shopping_info();
}


const render_shopping_info = function() {
    var today = new Date()
    var horas_atuais=today.getHours()
    var minutos_atuais=today.getMinutes()
    clear_view();
    $.ajax({
        url: consts.BASE_URL + '/api/shoppings/Shopping?id=' + shopping_id,
        type: "GET",
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            if (data) {
                parks = data.parks;
                stores = data.stores;
                parks.forEach(function(park) {
                    

                    $("#park_container").append(park_card_template(park.name, park.location, park.current_capacity, park.capacity, park.opening.substring(0,5), park.closing.substring(0,5)))
                })
                console.log(open_stores)
                stores.forEach(function(store) {
                    //TODO: Comparar se a store ta aberta
                    var horas_abertura=store.opening.slice(0,2)
                    var minutos_abertura=store.opening.slice(3,5)
                    var horas_fecho=store.closing.slice(0,2)
                    var minutos_fecho=store.closing.slice(3,5)
                    console.log("horas",store.opening)
                    console.log("horas",minutos_fecho)

                    if (horas_abertura < horas_atuais && horas_fecho > horas_atuais){
                        open_stores=open_stores+1
                    }
                    else if (horas_abertura == horas_atuais && minutos_atuais > minutos_abertura ){
                        open_stores=open_stores+1 
                    }
                    else if (horas_fecho < horas_atuais &&  horas_abertura < horas_atuais && horas_abertura > horas_fecho){
                        open_stores=open_stores+1 
                    }
                    else if (horas_fecho == horas_atuais && minutos_fecho > minutos_atuais   ){
                        open_stores=open_stores+1 
                    }
                    
                    $("#store_container").append(store_card_template(store.name, store.location, store.current_capacity, store.capacity, store.opening.substring(0,5), store.closing.substring(0,5), store.img))
                });

                $("#sh_name").text(data.name);
                $("#capacity").text(`${data.current_capacity}/${data.capacity}`);
                $("#schedule").text(`${data.opening} - ${data.closing}`);
                $("#parking").text(`${current_parking_capacity}/${total_parking_capacity}`)
                $("#stores").text(open_stores)
            } else {
                console.log("Invalid shopping id");
            }
        },

        error: function() {
            console.log("erro na call");
        }
    })
}


const clear_view = function() {
    stores = [];
    parks = [];
    total_parking_capacity = 0;
    current_parking_capacity = 0;
    open_stores = 0;

    $("#park_container").empty()
    $("#store_container").empty()
}


const store_card_template = function(name, location, cur_cap, max_cap, opening, closing, img) {
    return `
    <div class="col-lg-3 col-sm-6 col-12">
        <div class="card">
            <div class="card-header">
                <div class="row">
                    <div class="col-md-6">
                        <h3>${name}</h3>
                        <p>${location}</p>
                    </div>
                    <div class="col-md-6">
                        <img src="${img}" class="rounded float-right img-fluid" style="height:100px">
                    </div>
                </div>
            </div>
            <div class="card-body">
                <div class="col-12">
                    <div class="info-box bg-info">
                            <span class="info-box-icon"><i class="fas fa-user-friends"></i></span>
                        <div class="info-box-content">
                            <span class="info-box-text">Capacity</span>
                            <span class="info-box-number">${cur_cap}/${max_cap}</span>
                        </div>
                    </div>
                </div>
                <div class="col-12">
                    <div class="row">
                        <div class="col-lg-6 col-md-12 col-12">
                            <div class="info-box bg-info">
                                <span class="info-box-icon"><i class="fas fa-clock"></i></span>
                
                                <div class="info-box-content">
                                    <span class="info-box-text">Opening</span>
                                    <span class="info-box-number">${opening}</span>
                                </div>
                            </div>
                        </div>
                        <div class="col-lg-6 col-md-12 col-12">
                            <div class="info-box bg-info">
                                <span class="info-box-icon"><i class="fas fa-door-closed"></i></span>
                
                                <div class="info-box-content">
                                    <span class="info-box-text">Closing</span>
                                    <span class="info-box-number">${closing}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    `
}


const park_card_template = function(name, location, cur_cap, max_cap, opening, closing) {
    return `
    <div class="col-md-3 col-sm-6 col-12">
        <div class="card">
            <div class="card-header">
                <h3>${name}</h3>
                <p>${location}</p>
            </div>
            <div class="card-body">
                <div class="col-12">
                    <div class="info-box bg-info">
                            <span class="info-box-icon"><i class="fas fa-car"></i></span>
                        <div class="info-box-content">
                            <span class="info-box-text">Capacity</span>
                            <span class="info-box-number">${cur_cap}/${max_cap}</span>
                        </div>
                    </div>
                </div>
                <div class="col-12">
                    <div class="row">
                        <div class="col-md-6 col-sm-6 col-12">
                            <div class="info-box bg-info">
                                <span class="info-box-icon"><i class="fas fa-clock"></i></span>
                
                                <div class="info-box-content">
                                    <span class="info-box-text">Opening</span>
                                    <span class="info-box-number">${opening}</span>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6 col-sm-6 col-12">
                            <div class="info-box bg-info">
                                <span class="info-box-icon"><i class="fas fa-door-closed"></i></span>
                
                                <div class="info-box-content">
                                    <span class="info-box-text">Closing</span>
                                    <span class="info-box-number">${closing}</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    `
}
