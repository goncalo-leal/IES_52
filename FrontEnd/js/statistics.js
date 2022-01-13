import consts from "./consts.js";
import SessionManager from "./session.js";
import updateView from "./common.js"


$(document).ready(function() {
    updateView();

  
    compararLastWeek()
    loadDataBySensorLastHour()
    loadDataBySensorToday()
    loadDataBySensorWeek()
    loadDataBySensorMonth()
    // OUTRO VAI SER DE BARRAS SENSOR A SENSOR ONDE VOU MOSTRAR QUAL OS QUE TEM MAIS ADESÃO
    // day, week , last 2 weeks
    //falta o last hour
    
});




const loadDataBySensorToday= function(){
    $.ajax({
        url: consts.BASE_URL + '/api/AllSensorsForThatShoppingToday/' + SessionManager.get("session").shopping.id,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            if (data) {
                renderBarGraphic(data,'barChartToday')
                var shopping =data["Shopping"]
                var park=data["Park"]
                 var var_shop=0
                var var_park=0
                for (const [key, value] of Object.entries(shopping)) {
                    var_shop+=value
                  }
                for (const [key, value] of Object.entries(park)) {
                    console.log(key, value);
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
                console.log("aqui", data)
                renderBarGraphic(data,'barChartWeek')

                var shopping =data["Shopping"]
                var park=data["Park"]
                 var var_shop=0
                var var_park=0
                for (const [key, value] of Object.entries(shopping)) {
                    var_shop+=value
                  }
                for (const [key, value] of Object.entries(park)) {
                    console.log(key, value);
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
                console.log("aqui", data)
                renderBarGraphic(data,'barChartLastHour')

                var shopping =data["Shopping"]
                var park=data["Park"]
                 var var_shop=0
                var var_park=0
                for (const [key, value] of Object.entries(shopping)) {
                    var_shop+=value
                  }
                for (const [key, value] of Object.entries(park)) {
                    console.log(key, value);
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
                console.log("aqui MONTH", data)
                renderBarGraphic(data,'barChartMonth')
                var shopping =data["Shopping"]
                var park=data["Park"]
                 var var_shop=0
                var var_park=0
                for (const [key, value] of Object.entries(shopping)) {
                    var_shop+=value
                  }
                for (const [key, value] of Object.entries(park)) {
                    console.log(key, value);
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

const compararLastWeek= function(){
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
        for (const [key, v] of Object.entries(value)) {
            console.log(key+": "+v)
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
    
    console.log(areaChartData)

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
        maintainAspectRatio     : true,
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