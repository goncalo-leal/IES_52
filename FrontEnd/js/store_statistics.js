import consts from "./consts.js";
import SessionManager from "./session.js";
import updateView from "./common.js"

var date = new Date();
var day = new Date(date.getTime())
day= day.getFullYear()+'-'+day.getMonth()+1+'-'+day.getDate()
/*
    Pedir ajuda ao gonçalo
*/
var store_id = new URLSearchParams(window.location.search).get('id');

$(document).ready(function() {
    updateView();

    $('#search_date').datetimepicker({
        format: 'DD-MM-YYYY',
    });
    $('#search_date input').val(day.split('-').reverse().join('-'))

    $('#set_date').click(function() {
        day = $('#search_date input').val().split('-').reverse().join('-')
        loadShoppingByHours();
    });

    loadPeopleByWeek();
    compareLastWeek();
    loadDataBySensorLastHour();
    loadDataBySensorToday();
    loadDataBySensorWeek();
    loadDataBySensorMonth();
    loadShoppingByHours();

    
});

const loadShoppingByHours = function () {  
    
    console.log(day)
    $.ajax({
        url: consts.BASE_URL + '/api/PeopleInStoreByhours/' + store_id +'/'+day,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data){
            renderBarGraphic(data,'shoppingByHours')
        },
        error: function(){
            console.log("Error calling /api/PeopleInShoppingByhours/'")
        }
    })
}


const loadDataBySensorToday= function(){
    $.ajax({
        url: consts.BASE_URL + '/api/AllSensorsForThatStoreToday/' + store_id,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            if (data) {
                renderBarGraphic(data,'barChartToday')
             
            } else {
                console.log("No data");
            }

        },

        error: function() {
            console.log(" erro na call");
        }
    })
}

const loadDataBySensorWeek= function(){
    $.ajax({
        url: consts.BASE_URL + '/api/AllSensorsForThatStoreWeek/' + store_id,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            if (data) {
                renderBarGraphic(data,'barChartWeek')

            

            } else {
                console.log("No data");
            }

        },

        error: function() {
            console.log(" erro na call");
        }
    })
}
const loadDataBySensorLastHour= function(){
    $.ajax({
        url: consts.BASE_URL + '/api/AllSensorsForThatStoreHour/' +store_id,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            if (data) {
                renderBarGraphic(data,'barChartLastHour')
                
           

            } else {
                console.log("No data");
            }

        },

        error: function() {
            console.log(" erro na call");
        }
    })
}
const loadDataBySensorMonth= function(){
    $.ajax({
        url: consts.BASE_URL + '/api/AllSensorsForThatStoreMonth/' + store_id,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            if (data) {
                renderBarGraphic(data,'barChartMonth')
                
            } else {
                console.log("No data");
            }

        },

        error: function() {
            console.log(" erro na call");
        }
    })
}

const compareLastWeek= function(){
    $.ajax({
        url: consts.BASE_URL + '/api/PeopleInStoreTodayCompareWithLaskWeek/' + SessionManager.get("session").shopping.id,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            if (data) {
                var dif = data["Today"] 
                var dif2 = data["LastWeek"]
                $("#TextoComparatorioSemanaPassada").text("Last week around this time " +dif2+ " people entered. \n Today "+ dif +" people entered")
            } else {
                console.log("No data");
            }

        },

        error: function() {
            console.log(" erro na call");
        }
    })
}
const loadPeopleByWeek = function() {
    $.ajax({
        url: consts.BASE_URL + '/api/ParksMovementInShoppingLast14Days/' + SessionManager.get("session").shopping.id,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            if (data) {
                var number = [data.mapa["MONDAY"], data.mapa["TUESDAY"], data.mapa["WEDNESDAY"], data.mapa["THURSDAY"], data.mapa["FRIDAY"], data.mapa["SATURDAY"], data.mapa["SUNDAY"]];
                var total_visitors = 0;
                for (var i=0; i<number.length; i++){
                    total_visitors =total_visitors + number[i];
                }
                var numbers = [data.mapa["LAST_MONDAY"], data.mapa["LAST_TUESDAY"], data.mapa["LAST_WEDNESDAY"], data.mapa["LAST_THURSDAY"], data.mapa["LAST_FRIDAY"], data.mapa["LAST_SATURDAY"], data.mapa["LAST_SUNDAY"]];
                var total_visitors_last = 0;
                for (var x=0; x<numbers.length; x++){
                    total_visitors_last = total_visitors_last + numbers[x];
                }
                $("#shopping_capacity").html(total_visitors);
                let diferença=0
                if (total_visitors_last ==0 ){
                    diferença=(total_visitors- total_visitors_last)*100
               
                }
                else{
                    diferença=(( total_visitors- total_visitors_last)/total_visitors_last)*100
                    
                }
                diferença=diferença.toFixed(2)
                if (diferença > 0){
                    $("#Total_diferença_semanas").html("<i class='ion ion-android-arrow-up text-success' ></i> " + diferença + "% Since last week")
                }
                else{
                    $("#Total_diferença_semanas").html("<i class='ion ion-android-arrow-down text-warning' ></i> " + diferença+ "% Since last week")

                }
                renderGraphic(data.mapa);
            } else {
                console.log("No data");
            }

        },

        error: function() {
            console.log("erro na call");
        }
    })
}

const renderDonut = function (curr, total, id, title=""){
    var donutChartCanvas = $('#'+id).get(0).getContext('2d')
    //var donutChartCanvas = $('#donut1').get(0).getContext('2d')
    
    var donutData = {        
        labels: [
            'Doors',
            'Parks',
        ],
        datasets: [
            {
            data: [curr,total],
            backgroundColor : ['#f56954', '#00a65a'],
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

const renderBarGraphic = function (data,id) {
    var labels = []
    var info = []
    for (const [key, value] of Object.entries(data)) {
        labels.push(key)
        info.push(value)
    }
    var areaChartData = {
        labels  : labels,
        datasets: [
            {
                label               : 'Entries',
                backgroundColor     : '#007bff',
                borderColor         : '#007bff',
                data                : info
            }
        ]
    }

    var barChartCanvas = $('#'+id).get(0).getContext('2d');

    var barChartData = $.extend(true, {}, areaChartData)
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
            scaleStartValue: 0,
            yAxes: [{
                // display: false,
                gridLines: {
                    display: true,
                    lineWidth: '4px',
                    color: 'rgba(0, 0, 0, .2)',
                    zeroLineColor: 'transparent'
                },
                ticks: {
                    beginAtZero: true
                }
            }],
            xAxes: [{
                display: true,
                gridLines: {
                    display: false
                },
                ticks: ticksStyle
            }],
           
        }
    }

    new Chart(barChartCanvas, {
        type: 'bar',
        data: barChartData,
        options: barChartOptions
    })
}



const renderGraphic = function (mapa) {
    var areaChartData = {
        labels  : ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'],
        datasets: [
            {
                label               : 'This week',
                backgroundColor     : '#007bff',
                borderColor         : '#007bff',
                data                : [mapa["MONDAY"], mapa["TUESDAY"], mapa["WEDNESDAY"], mapa["THURSDAY"], mapa["FRIDAY"], mapa["SATURDAY"], mapa["SUNDAY"]]
            },
            {
                label               : 'Last week',
                backgroundColor     : '#ced4da',
                borderColor         : '#ced4da',
                data                : [mapa["LAST_MONDAY"], mapa["LAST_TUESDAY"], mapa["LAST_WEDNESDAY"], mapa["LAST_THURSDAY"], mapa["LAST_FRIDAY"], mapa["LAST_SATURDAY"], mapa["LAST_SUNDAY"]]
            },
        ]
    }

    var barChartCanvas = $('#barChartPark').get(0).getContext('2d');

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