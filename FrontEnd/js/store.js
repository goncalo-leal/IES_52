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

})

const updateStoreViews = function() {
    fetchStore();
    fetchTodayInfo();
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