import consts from "./consts.js";
import SessionManager from "./session.js";
import updateView from "./common.js"


$(document).ready(function() {
    updateView();
    loadShoppingEntrancesLastToday()
    loadShoppingEntrancesLastWeek()
    compararLastWeek()
    loadShoppingEntrancesLastMonth()
    loadDataBySensorToday()

    // OUTRO VAI SER DE BARRAS SENSOR A SENSOR ONDE VOU MOSTRAR QUAL OS QUE TEM MAIS ADESÃO
    // day, week , last 2 weeks
    //falta o last hour
    
});

const loadShoppingEntrancesLastToday= function(){
    $.ajax({
        url: consts.BASE_URL + '/api/PeopleEntrancesTodayByShoppingOrPark/' + SessionManager.get("session").shopping.id,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            if (data) {
                console.log(data)
                renderDonut(data["Shopping"],data["Park"], "donutChartParkvsShopping", "Shopping vs Park")
            } else {
                console.log("No data");
            }

        },

        error: function() {
            console.log(" erro na call");
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
                console.log(data)
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
const loadShoppingEntrancesLastWeek= function(){
    $.ajax({
        url: consts.BASE_URL + '/api/PeopleEntrancesWeekByShoppingOrPark/' + SessionManager.get("session").shopping.id,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            if (data) {
                console.log(data)
                renderDonut(data["Shopping"],data["Park"], "donutChartParkvsShoppinglastWeek", "Shopping vs Park Last Week")
            } else {
                console.log("No data");
            }

        },

        error: function() {
            console.log(" erro na call");
        }
    })
}
const loadShoppingEntrancesLastMonth= function(){
    $.ajax({
        url: consts.BASE_URL + '/api/PeopleEntrancesMonthByShoppingOrPark/' + SessionManager.get("session").shopping.id,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            if (data) {
                console.log(data)
                renderDonut(data["Shopping"],data["Park"], "donutChartParkvsShoppinglastMonth", "Shopping vs Park Last Week")
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