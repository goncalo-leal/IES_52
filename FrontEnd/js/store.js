import consts from "./consts.js";
import SessionManager from "./session.js";
import updateView from "./common.js"

var store_id = new URLSearchParams(window.location.search).get('id');

if (store_id === null) {
    window.location.href = "./index.html";
}

var store;
var peopletoday;

$(document).ready(function() {
    updateView();
    updateStoreViews();
    setInterval(updateStoreViews, 60000);
})

const updateStoreViews = function() {
    fetchStore();
    fetchTodayInfo();
    customersToday();
    loadPeopleByWeek();
    getLastWeekShoppingInfo()

}

const fetchStore = function() {
    $.ajax({
        url: consts.BASE_URL + '/api/Store?id=' + store_id,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            if (data) {
                store = data;
                var percent = Math.round((store.current_capacity / store.capacity) * 100);
                var color = "bg-success";
                if (percent > 80 && percent < 90) {
                    color = "bg-warning";
                } 
                else if (percent >= 90) {
                    color = "bg-danger";
                }
                $('#cur_ocup_box').addClass(color);
                $('#cur_ocup_num').text(percent + ' %');
                $('#cur_ocup_bar').width(percent + '%');  

                $('#store_name').text(store.name);
                $('#store_location').text(store.location);
            
                var cur_cap = store.current_capacity;
                var cap = store.capacity;
            
                $("#storecurcap").text(cur_cap);
                $("#storemaxcap").text(cap);
                renderDonut(cur_cap, cap, "donutStore");
            }
        },

        error: function() {
            console.log(" erro na call");
        }
    });
}

const fetchTodayInfo = function() {
    $.ajax({
        url: consts.BASE_URL + '/api/PeopleInStoreToday/'+ store_id,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            peopletoday = data;
            $('#peopletoday').text(peopletoday);
        },

        error: function() {
            console.log(" erro na call");
        }
    });
}

const fetchLastWeek = function() {
    $.ajax({
        url: consts.BASE_URL + '/api/PeopleInStoreLast7Days/'+ store_id,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            
            $('#peopletoday').text(peopletoday);
        },

        error: function() {
            console.log(" erro na call");
        }
    })
}
const customersToday = function(){
    $.ajax({
        url: consts.BASE_URL + '/api/PeopleInStoreToday/'+ store_id,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            
            $('#peopletoday').text(data);
        },

        error: function() {
            console.log(" erro na call");
        }
    })
}

const loadPeopleByWeek = function() {
    $.ajax({
        url: consts.BASE_URL + '/api/PeopleInStoreLast14Days/' + store_id,
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
               
                let diferença=0
                if (total_visitors_last ==0 ){
                    diferença=(total_visitors- total_visitors_last)*100
               
                }
                else{
                    diferença=(( total_visitors- total_visitors_last)/total_visitors_last)*100
    
                }
                diferença=diferença.toFixed(0)
                $("#Weekly_grouth").text(diferença+"%")
               
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
            'Occupied',
            'Free',
        ],
        datasets: [
            {
            data: [curr,total-curr],
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

const getLastWeekShoppingInfo= function(){
    var date = new Date();
    date.setDate(date.getDate() - 7);
    var finalDate = date.getFullYear()+'-'+ (date.getMonth()+1)+'-' +date.getDate()
    $.ajax({
        url: consts.BASE_URL + '/api/PeopleInStoreByhours/' + store_id,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            if (data) {
                renderLinearGraphic(data,"barChartStoreTodayHours")
            } else {
                console.log("No data");
            }
        },

        error: function() {
            console.log(" erro na call");
        }
    });

}

const renderGraphic = function (mapa) {
    console.log(mapa);
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

    var barChartCanvas = $('#barChartLastWeek').get(0).getContext('2d');

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
const renderBarGraphic = function (today, id) {
    var labels = []
    var info1 = []
    console.log(today)
    for (const [key, value] of Object.entries(today)) {
        labels.push(key)
        info1.push(value)
    }
 
    var areaChartData = {
        labels :labels,
        datasets: [
            {
                label               : 'Today',
                backgroundColor     : '#007bff',
                borderColor         : '#007bff',
                data                : info1,
            }
        ]
    }

    var barChartCanvas = $('#'+id).get(0).getContext('2d');

    var barChartData = $.extend(true, {}, areaChartData)
    var temp0 = areaChartData.datasets[0]
    barChartData.datasets[0] = temp0
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