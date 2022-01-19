import consts from "./consts.js";
import SessionManager from "./session.js";
import updateView from "./common.js"


$(document).ready(function() {
    updateView();

    loadPeopleByWeek();
    compareLastWeek();
    loadDataBySensorLastHour();
    loadDataBySensorToday();
    loadDataBySensorWeek();
    loadDataBySensorMonth();
    loadShoppingByHours();

    
});

const loadShoppingByHours = function () {  
    $.ajax({
        url: consts.BASE_URL + '/api/PeopleInShoppingByhours/' + SessionManager.get("session").shopping.id,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data){
            renderLinearGraphic(data,'shoppingByHours')
        },
        error: function(){
            console.log("Error calling /api/PeopleInShoppingByhours/'")
        }
    })
}


const loadDataBySensorToday= function(){
    $.ajax({
        url: consts.BASE_URL + '/api/AllSensorsForThatShoppingToday/' + SessionManager.get("session").shopping.id,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            if (data) {
                
                renderBarGraphicV2(data,'barChartToday')
                var shopping =data["Shopping"]
                var park=data["Park"]
                var var_shop=0
                var var_park=0
                for (const [key, value] of Object.entries(shopping)) {
                    var_shop+=value
                  }
                for (const [key, value] of Object.entries(park)) {
                    var_park+=value
                  }
                renderDonut(var_shop,var_park, "donutChartParkvsShopping", "Shopping vs Park")

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
        url: consts.BASE_URL + '/api/AllSensorsForThatShoppingWeek/' + SessionManager.get("session").shopping.id,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            if (data) {
                renderBarGraphicV2(data,'barChartWeek')

                var shopping =data["Shopping"]
                var park=data["Park"]
                var var_shop=0
                var var_park=0
                for (const [key, value] of Object.entries(shopping)) {
                    var_shop+=value
                  }
                for (const [key, value] of Object.entries(park)) {
                    var_park+=value
                  }
                renderDonut(var_shop,var_park, "donutChartParkvsShoppinglastWeek", "Shopping vs Park")


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
        url: consts.BASE_URL + '/api/lastHourShoppingAndParksbySensor/' + SessionManager.get("session").shopping.id,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            if (data) {
                renderBarGraphicV2(data,'barChartLastHour')

                var shopping =data["Shopping"]
                var park=data["Park"]
                var var_shop=0
                var var_park=0
                for (const [key, value] of Object.entries(shopping)) {
                    var_shop+=value
                  }
                for (const [key, value] of Object.entries(park)) {
                    var_park+=value
                  }
                renderDonut(var_shop,var_park, "donutChartLastHour", "Shopping vs Park")


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
        url: consts.BASE_URL + '/api/AllSensorsForThatShoppingMonth/' + SessionManager.get("session").shopping.id,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            if (data) {
                renderBarGraphicV2(data,'barChartMonth')
                var shopping =data["Shopping"]
                var park=data["Park"]
                var var_shop=0
                var var_park=0
                for (const [key, value] of Object.entries(shopping)) {
                    var_shop+=value
                  }
                for (const [key, value] of Object.entries(park)) {
                    var_park+=value
                  }
                renderDonut(var_shop,var_park, "donutChartParkvsShoppinglastMonth", "Shopping vs Park")


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
        url: consts.BASE_URL + '/api/PeopleInShoppingTodayCompareWithLaskWeek/' + SessionManager.get("session").shopping.id,
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

    if (curr ==0 && total == 0){
        $("#"+id+"Warning").removeClass("d-none")
        $("#"+id+"Warning").addClass("d-flex")
        document.getElementById(id).style.display="none"
    }
    else{
        $("#"+id+"Warning").removeClass("d-flex")
        $("#"+id+"Warning").addClass("d-none")
        document.getElementById(id).style.display="block"

    }
    //Create pie or douhnut chart
    // You can switch between pie and douhnut using the method below.
    return new Chart(donutChartCanvas, {
        type: 'doughnut',
        data: donutData,
        options: donutOptions
    })    
}

const renderBarGraphicV2 = function (data,id) {
    var labels = []
    var info = []
    for (const [key, value] of Object.entries(data)) {
        for (const [key, v] of Object.entries(value)) {
            labels.push(key)
            info.push(v )
        }
    }

    console.log(info)

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

const renderLinearGraphic = function (data,id){
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