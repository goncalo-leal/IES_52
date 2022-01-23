import consts from "./consts.js";
import SessionManager from "./session.js";
import { updateView, requestWithToken } from "./common.js"


var date = new Date();
var day = new Date(date.getTime())
day= day.getFullYear()+'-'+day.getMonth()+1+'-'+day.getDate()

var store_id = new URLSearchParams(window.location.search).get('id');

$(document).ready(function() {
    updateView();

    if (store_id == null) {
        SweetAlert.fire(
            'Error!',
            'Invalid URL',
            'error'
        ).then((result) => {
            console.log("ok")
            window.location.href = "./home.html"
        });
    }
    else {
        $('#search_date').datetimepicker({
            format: 'DD-MM-YYYY',
        });
        $('#search_date input').val(day.split('-').reverse().join('-'))
    
        $('#set_date').click(function() {
            day = $('#search_date input').val().split('-').reverse().join('-')
            loadShoppingByHours();
        });
    
        initialize();
        setInterval(initialize, 60000);
    }
    
});

const initialize = function() {
    console.log("reload");
    loadPeopleByWeek();
    compareLastWeek();
    loadDataBySensorLastHour();
    loadDataBySensorToday();
    loadDataBySensorWeek();
    loadDataBySensorMonth();
    loadShoppingByHours();
}

const loadShoppingByHours = function () {  
    
    console.log(day)
    requestWithToken("GET", '/api/sensorsdata/PeopleInStoreByhours/' + store_id +'/'+day, function(data) {
        renderLinearGraphic(data,'shoppingByHours')
    })
}


const loadDataBySensorToday= function(){
    requestWithToken("GET", '/api/sensorsdata/AllSensorsForThatStoreToday/' + store_id, function(data) {
        if (data) {
            renderBarGraphic(data,'barChartToday')
         
        } else {
            console.log("No data");
        }
    })
}

const loadDataBySensorWeek= function(){
    requestWithToken("GET", '/api/sensorsdata/AllSensorsForThatStoreWeek/' + store_id, function(data) {
        if (data) {
            renderBarGraphic(data,'barChartWeek')
        } else {
            console.log("No data");
        }
    })
}

const loadDataBySensorLastHour= function(){
    requestWithToken("GET", '/api/sensorsdata/AllSensorsForThatStoreHour/' +store_id, function(data) {
        if (data) {
            renderBarGraphic(data,'barChartLastHour')
        } else {
            console.log("No data");
        }
    })
}


const loadDataBySensorMonth= function(){
    requestWithToken("GET",  '/api/sensorsdata/AllSensorsForThatStoreMonth/' + store_id, function(data) {
        if (data) {
            renderBarGraphic(data,'barChartMonth')
            
        } else {
            console.log("No data");
        }
    })
}

const compareLastWeek= function(){
    requestWithToken("GET", '/api/sensorsdata/PeopleInStoreTodayCompareWithLaskWeek/' + store_id, function(data) {
        if (data) {
            var dif = data["Today"] 
            var dif2 = data["LastWeek"]
            $("#TextoComparatorioSemanaPassada").text("Last week around this time " +dif2+ " people entered. \n Today "+ dif +" people entered")
        } else {
            console.log("No data");
        }
    })
}

const loadPeopleByWeek = function() {
    requestWithToken("GET", '/api/sensorsdata/PeopleInStoreLast14Days/' + store_id, function(data) {
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

const renderLinearGraphic = function (data,id) {
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
        type: 'line',
        data: barChartData,
        options: barChartOptions
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