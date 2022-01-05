import consts from "./consts.js";
import SessionManager from "./session.js";
import updateView from "./common.js"

$(document).ready(function() {
    updateView();
    loadShoppingInfo();
    loadPeopleByWeek();
    $("#mySearchText").on('input', function() {
        search();
    })
})



var stores;
var parks;
const loadShoppingInfo = function() {
    $("#s_name").text(SessionManager.get("session").shopping.name);

    $.ajax({
        url: consts.BASE_URL + '/api/Shopping?id=' + SessionManager.get("session").shopping.id,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            if (data) {
                var cur_capacity = 0;
                stores = data.stores;
                $("#shopcurcap").text(data.current_capacity);
                $("#shopmaxcap").text(data.capacity);
                parks = data.parks;

                if (parks.length > 0) {
                    console.log("calculate parks...");
                } else {
                    $("#parkingTab").empty();
                }

                renderTable(stores);
            } else {
                console.log("No store for this shopping");
            }
        },

        error: function() {
            console.log("erro na call");
        }
    })
}


const renderTable = function (data) {
    $("#stores_body").empty();
    data.forEach(function(e, i) {
        $("#stores_body").append(trTemplate(e.name, e.current_capacity, 10));
    })
}


const trTemplate = function (name, ocupation, growth) {
    return `
    <tr>
        <td>
            ${name}
        </td>
        <td class="text-center">
            ${ocupation}
        </td>
        <td class="text-center">
            <small class="text-success mr-1">
                <i class="fas fa-arrow-up"></i>
                ${growth}%
            </small>
        </td>
        <td class="text-right">
            <a href="#" class="text-muted">
                <i class="fas fa-search"></i>
            </a>
        </td>
    </tr>
    `
}


const search = function () {
    var input = $("#mySearchText").val().toLowerCase();
    var temp = [];

    stores.forEach(function(e, i) {
        var name = e.name.toLowerCase();
        if (name != null && name.includes(input)) {
            temp.push(e);
        }
    })

    renderTable(temp);
}


const loadPeopleByWeek = function() {
    $.ajax({
        url: consts.BASE_URL + '/api/PeopleInShoppingLast7Days/' + SessionManager.get("session").shopping.id,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            if (data) {
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


const renderGraphic = function (mapa) {
    console.log(mapa);
    var areaChartData = {
        labels  : ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'],
        datasets: [
            {
            label               : 'This week',
            backgroundColor     : 'rgba(0,255,0,0.9)',
            borderColor         : 'rgba(0,255,0,0.8)',
            pointRadius          : false,
            pointColor          : '#3b8bba',
            pointStrokeColor    : 'rgba(0,255,0,1)',
            pointHighlightFill  : '#fff',
            pointHighlightStroke: 'rgba(0,255,0,1)',
            data                : [mapa["MONDAY"], mapa["TUESDAY"], mapa["WEDNESDAY"], mapa["THURSDAY"], mapa["FRIDAY"], mapa["SATURDAY"], mapa["SUNDAY"]]
         
            },
            {
            label               : 'Last week',
            backgroundColor     : 'rgba(255, 0, 0, 1)',
            borderColor         : 'rgba(255, 0, 0, 1)',
            pointRadius         : false,
            pointColor          : 'rgba(255, 0, 0, 1)',
            pointStrokeColor    : '#c1c7d1',
            pointHighlightFill  : '#fff',
            pointHighlightStroke: 'rgba(220,220,220,1)',
            data                : [mapa["LAST_MONDAY"], mapa["LAST_TUESDAY"], mapa["LAST_WEDNESDAY"], mapa["LAST_THURSDAY"], mapa["LAST_FRIDAY"], mapa["LAST_SATURDAY"], mapa["LAST_SUNDAY"]]
            },
        ]
}
    var barChartCanvas = $('#barChart').get(0).getContext('2d')
    var barChartData = $.extend(true, {}, areaChartData)
    var temp0 = areaChartData.datasets[0]
    var temp1 = areaChartData.datasets[1]
    barChartData.datasets[0] = temp1
    barChartData.datasets[1] = temp0

    var barChartOptions = {
        responsive              : true,
        maintainAspectRatio     : false,
        datasetFill             : false
    }

    new Chart(barChartCanvas, {
        type: 'bar',
        data: barChartData,
        options: barChartOptions
    })



}
