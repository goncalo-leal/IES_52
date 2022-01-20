import consts from "./consts.js";
import SessionManager from "./session.js";
import updateView from "./common.js"

var stores_table;
var parks_table;
var past_info_stores;
var past_info_parks;
var contador = 0;
var p_contador = 0;

var stores = [];
var parks = [];
var storesData={};


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

    stores_table = $("#stores").DataTable({
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
            { "searchable": false, "targets": 1 },
            { "searchable": false, "targets": 2 },
            { "searchable": false, orderable: false, "targets": 3 },
        ],
        "dom": '<"top"i>rt<"bottom"><"clear">'
        
    });

    parks_table = $("#parks").DataTable({
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
            { "searchable": false, "targets": 1 },
            { "searchable": false, "targets": 2 },
            { "searchable": false, orderable: false, "targets": 3 },
        ],
        "dom": '<"top"i>rt<"bottom"><"clear">'
    });

    initialize();
    setInterval(initialize, 60000);
});

const initialize = function() {
    console.log("reload");
    calculateParkPercentage();
    getLastWeekShoppingInfo();
    loadShoppingInfo();
    getLastWeekParkInfo();
    loadShoppingEntrancesLastHour();
}

const calculateParkPercentage = function(){
    $.ajax({
        url: consts.BASE_URL + '/api/PeopleInParkInLastHour2/' + shopping_id,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            if (data) {
                var duas_horas = data["2_hours_ago"];
                var ultima_hora = data["last_hour"];
                if (duas_horas==0){
                    duas_horas=1;
                }
                var percentagem = ((parseInt(ultima_hora)-parseInt(duas_horas))/parseInt(duas_horas))*100;
                percentagem=Math.round(percentagem);
                if (percentagem<0){
                    percentagem = Math.abs(percentagem);
                }
                if (percentagem>0){
                    $("#park_capacity_percentagem").html('<i class="ion ion-android-arrow-up text-success">'+percentagem+'% in last hour</i>');
                }
                else{
                    $("#park_capacity_percentagem").html('<i class="ion ion-android-arrow-up text-warning">'+percentagem+'% in last hour</i>');
                }

            } else {
                console.log("No data");
            }
        },

        error: function() {
            console.log(" erro na call");
        }
    });
}

const getLastWeekShoppingInfo= function(){
    var date = new Date();
    date.setDate(date.getDate() - 7);
    var finalDate = date.getFullYear()+'-'+ (date.getMonth()+1)+'-' +date.getDate()
    $.ajax({
        url: consts.BASE_URL + '/api/PeopleInShoppingByhoursVsLaskWeek/' + shopping_id+'/'+finalDate,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            if (data) {
                console.log("dados",data)
                renderBarGraphic(data["Today"],data["Week"],"barChartShopping")
            } else {
                console.log("No data");
            }
        },

        error: function() {
            console.log(" erro na call");
        }
    });

}
const getLastWeekParkInfo= function(){
    var date = new Date();
    date.setDate(date.getDate() - 7);
    var finalDate = date.getFullYear()+'-'+ (date.getMonth()+1)+'-' +date.getDate()
    $.ajax({
        url: consts.BASE_URL + '/api/PeopleInParkByhoursVsLaskWeek/' + shopping_id+'/'+finalDate,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            if (data) {
                console.log("dados",data)
                renderBarGraphic(data["Today"],data["Week"],"barChartPark")
            } else {
                console.log("No data");
            }
        },

        error: function() {
            console.log(" erro na call");
        }
    });

}
const getAllStoresLastHourEntrance= function(){

    $.ajax({
        url: consts.BASE_URL + '/api/CountLastHoursForStores/' + shopping_id,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            if (data) {
                
                past_info_stores = data
                renderStoresTable(stores);


            } else {
                console.log("No data");
            }
        },

        error: function() {
            console.log(" erro na call");
        }
    });
}


const loadShoppingInfo = function() {
    console.log(shopping_id)
    $.ajax({
        url: consts.BASE_URL + '/api/Shopping?id=' + shopping_id,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            if (data) {
                var cur_capacity = 0;
                stores = data.stores;
                parks = data.parks;
                
                
                $("#shopcurcap").text(data.current_capacity);
                $("#shopmaxcap").text(data.capacity);
                
                renderDonut(data.current_capacity, data.capacity, "donutShopping");
                if (parks.length > 0) {
                    console.log("calculate parks...");
                } else {
                    $("#parkingTab").empty();
                }
                

                loadShoppingsParks();
                getAllStoresLastHourEntrance();    
                getAllParksLastHourEntrance();
                //loadShoppingStores();
            } else {
                console.log("No store for this shopping");
            }
        },

        error: function() {
            console.log("erro na call");
        }
    })
}
//const loadParksEntrancesLastHour = function() {
//    $.ajax()
//    parks.forEach(function(e,i){
//        alert(i);
//    })
//}



const loadShoppingsParks = function() {
    let occupied = 0;
    let total = 0;

    for (var i = 0; i < parks.length; i++){
        occupied += parks[i]["current_capacity"];
        total += parks[i]["capacity"];
    }

    $("#parkcurcap").html(occupied);
    $("#parkmaxcap").html(total);
    renderDonut(occupied, total, "donutPark");
}



const getAllParksLastHourEntrance= function(){
    $.ajax({
        url: consts.BASE_URL + '/api/CountLastHoursForParks/' + shopping_id,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            if (data) {
                past_info_parks = data
                renderParksTable(parks);

            } else {
                console.log("No data");
            }
        },

        error: function() {
            console.log(" erro na call");
        }
    });
}

const loadShoppingEntrancesLastHour= function(){
    $.ajax({
        url: consts.BASE_URL + '/api/PeopleInShoppingInLastHour/' + shopping_id,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            if (data) {
                let horas_atrs=data["2_hours_ago"];
                let horas_atual=data["last_hour"];
                let diferença=0
                if (horas_atrs ==0 ){
                    diferença=(horas_atual- horas_atrs)*100
               
                }
                else{
                    diferença=(( horas_atual- horas_atrs)/horas_atrs)*100
                    
                }
                if (diferença >0){
                    $("#shopping_capacity_percentagem").html("<i class='ion ion-android-arrow-up text-success' >"+diferença+ "% in last hour</i>")
                }
                else{
                    $("#shopping_capacity_percentagem").html("<i class='ion ion-android-arrow-down text-warning' >"+diferença+ "% in last hour</i>")

                }
            } else {
                console.log("No data");
            }

        },

        error: function() {
            console.log(" erro na call");
        }
    })
}

const renderStoresTable = function (data) {
    var table_data = []

    $("#stores_body").empty();
    data.forEach(function(e, i) {
        let difference = 0
        if (past_info_stores !== null) {

            console.log("oast info stores"+past_info_stores);
            let horas_atrs = past_info_stores[e.id]["2_hours_ago"];
            let horas_atual = past_info_stores[e.id]["last_hour"];
            if (horas_atrs == 0) {
                difference = (horas_atual- horas_atrs) * 100        
            }
            else {
                difference = ((horas_atual - horas_atrs) / horas_atrs) * 100           
            }
        }
        
        if (difference >0){
            table_data.push([e.name, '<p class="text-center">'+e.current_capacity+'</p>', '<p class="text-center"><b><span class="text-success mr-1"><i class="ion ion-android-arrow-up text-success"></i> ' + difference + '%</span></b></p>', '<a href="/store.html?id=' + e.id + '" class="text-muted float-right"><i class="fas fa-search"></i></a>']);
        }
        else{
            table_data.push([e.name, '<p class="text-center">'+e.current_capacity+'</p>', '<p class="text-center"><b><span class="text-warning mr-1"><i class="ion ion-android-arrow-up text-warning"></i> ' + difference + '%</span></b></p>', '<a href="/store.html?id=' + e.id + '" class="text-muted float-right"><i class="fas fa-search"></i></a>']);
        }
    });
    
    stores_table.clear();
    stores_table.rows.add(table_data).draw();
    
}

const renderParksTable = function (data) {
    var table_data = []

    $("#parks_body").empty();
    data.forEach(function(e, i) {
        let difference = 0
        
        if (past_info_parks !== null) {
            
            let two_hours_ago = past_info_parks[e.id]["2_hours_ago"];
            let last_hour = past_info_parks[e.id]["last_hour"];
            
            if (two_hours_ago == 0) {
                difference = (last_hour- two_hours_ago) * 100        
            }
            else {
                difference = ((last_hour - two_hours_ago) / two_hours_ago) * 100
            }
        }
        
        if (difference > 0){
            table_data.push([e.name, '<p class="text-center">'+e.current_capacity+'</p>', '<p class="text-center"><b><span class="text-success mr-1"><i class="ion ion-android-arrow-up text-success"></i> ' + difference + '%</span></b></p>', '<a href="#" class="text-muted float-right"><i class="fas fa-search"></i></a>']);
        }
        else{
            table_data.push([e.name, '<p class="text-center">'+e.current_capacity+'</p>', '<p class="text-center"><b><span class="text-warning mr-1"><i class="ion ion-android-arrow-up text-warning"></i> ' + difference + '%</span></b></p>', '<a href="#" class="text-muted float-right"><i class="fas fa-search"></i></a>']);
        }
    });
   
    parks_table.clear();
    parks_table.rows.add( table_data ).draw();
}




const renderDonut = function (curr, total, id, title=""){
    var donutChartCanvas = $('#'+id).get(0).getContext('2d')
    //var donutChartCanvas = $('#donut1').get(0).getContext('2d')
    
    var donutData = {        
        labels: [
            'Occupied',
            'Free',
        ],
        datasets: [
            {
            data: [curr,total-curr],
            backgroundColor : ['#ff0000', '#13a300'],
            }
        ]
    }

    var donutOptions = {        
        maintainAspectRatio : false,
        responsive : true,
    }

    //Create pie or douhnut chart
    // You can switch between pie and douhnut using the method below.
    return new Chart(donutChartCanvas, {
        type: 'doughnut',
        data: donutData,
        options: donutOptions
    })    
}

const renderBarGraphic = function (today,week,id) {
    var labels = []
    var info1 = []
    var info2=[]
    for (const [key, value] of Object.entries(today)) {
            labels.push(key)
            info1.push(value)
    }
    for (const [key, value] of Object.entries(week)) {
        info2.push(value)
}
    var areaChartData = {
        labels :labels,
        datasets: [
            {
                label               : 'This week',
                backgroundColor     : '#007bff',
                borderColor         : '#007bff',
                data                : info1,
            },
            {
                label               : 'Last week',
                backgroundColor     : '#ced4da',
                borderColor         : '#ced4da',
                data                : info2
            },
        ]
    }

    var barChartCanvas = $('#'+id).get(0).getContext('2d');

    var barChartData = $.extend(true, {}, areaChartData)
    var temp0 = areaChartData.datasets[0]
    var temp1 = areaChartData.datasets[1]
    barChartData.datasets[0] = temp1
    barChartData.datasets[1] = temp0
    var mode = 'index'
    var intersect = true
    var ticksStyle = {
        fontColor: '#495057',
        fontStyle: 'bold'
    }

    var barChartOptions = {
        responsive              : true,
        maintainAspectRatio     : false,
        datasetFill             : false,
        tooltips: {
            mode: mode,
            intersect: intersect
        },
        hover: {
            mode: mode,
            intersect: intersect
        },
        legend: {
            display: false
        },
        scales: {
            yAxes: [{
                // display: false,
                gridLines: {
                    display: true,
                    lineWidth: '4px',
                    color: 'rgba(0, 0, 0, .2)',
                    zeroLineColor: 'transparent'
                },
            }],
            xAxes: [{
                display: true,
                gridLines: {
                    display: false
                },
                ticks: ticksStyle
            }]
        }
    }

    new Chart(barChartCanvas, {
        type: 'bar',
        data: barChartData,
        options: barChartOptions
    })
}