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

var shopping_id=1

$(document).ready(function() {
    

    getLastWeekShoppingInfo()
    past_info_parks = getAllParksLastHourEntrance();
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

    loadShoppingInfo();
    getLastWeekParkInfo();
    loadShoppingEntrancesLastHour();

   
});
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

                renderParksTable(parks);

                loadShoppingsParks();
            } else {
                console.log("No store for this shopping");
            }
        },

        error: function() {
            console.log("erro na call");
        }
    })
}




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
    var to_ret = null

    $.ajax({
        url: consts.BASE_URL + '/api/CountLastHoursForParks/' + shopping_id,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            if (data) {
                to_ret = data
            } else {
                console.log("No data");
            }
        },

        error: function() {
            console.log(" erro na call");
        }
    });

    return to_ret;
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
                    $("#shopping_capacity_percentagem").after($("<i class='ion ion-android-arrow-up text-success' ></i>").text(diferença+ "% in last hour")  )
                }
                else{
                    $("#shopping_capacity_percentagem").after($("<i class='ion ion-android-arrow-down text-warning' ></i>").text(diferença+ "% in last hour")  )

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


const renderParksTable = function (data) {
    var table_data = []

    $("#parks_body").empty();
    data.forEach(function(e, i) {
        let difference = 0
        if (past_info_parks !== null) {
            console.log("HEYYY")
            let two_hours_ago = past_info_parks[e.id]["2_hours_ago"];
            let last_hour = past_info_parks[e.id]["last_hour"];
            
            if (two_hours_ago == 0) {
                difference = (last_hour- two_hours_ago) * 100        
            }
            else {
                difference = ((last_hour - two_hours_ago) / two_hours_ago) * 100
            }
        }
        console.log(difference)
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