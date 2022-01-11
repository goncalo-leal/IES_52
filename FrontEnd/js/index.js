import consts from "./consts.js";
import SessionManager from "./session.js";
import updateView from "./common.js"

var table;
$(document).ready(function() {
    table = $("#stores").DataTable({
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
    
    $('#mySearchButton').on( 'keyup click', function () {
        table.search($('#mySearchText').val()).draw();

    } );

    updateView();
    loadShoppingsPark();
    loadShoppingInfo();
    loadShoppingStores();
    loadPeopleByWeek();

    $("#mySearchText").on('input', function() {
        search();
    })
    $('#carouselExampleIndicators').on('slid.bs.carousel', function () {
        if (contador<=stores.length-1){
            var currentIndex = $('div.active').index();
            var curr, cap, id, nome;
            [curr, cap, id, nome] = storesData[currentIndex];
            var donutChartCanvas = $('#'+id).get(0).getContext('2d')
            $("#storeName"+currentIndex).html(nome);
            renderDonut(curr, cap, id, nome);
            contador++;
        }
    })
})

var contador=0;
var stores;
var parks;
var storesData={};

const loadShoppingStores = function(){
    $.ajax({
        url: consts.BASE_URL + '/api/Shopping?id=' + SessionManager.get("session").shopping.id,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            if (data) {
                for (var i=0; i<data.stores.length; i++){
                    if (i==0){
                        $('<li data-target="#carouselExampleIndicators" data-slide-to="0" class="active"></li>').appendTo('.carousel-indicators')
                        $('<div class="carousel-item active"><canvas class="d-block w-100" heigh="200" id="donut0" ></canvas><div class="carousel-caption d-none d-md-block">\
                        <h5 style="color:black;" id="storeName0"></h5>\
                        </div>\
                        </div>').appendTo('.carousel-inner');
                        $( "#to_remove1" ).remove();
                        $( "#to_remove2" ).remove();
                    }
                    else{
                        $('<li data-target="#carouselExampleIndicators" data-slide-to="'+i+'"></li>').appendTo('.carousel-indicators')
                        $('<div class="carousel-item"><canvas class="d-block w-100" heigh="200" id="donut'+i+'" ></canvas><div class="carousel-caption d-none d-md-block">\
                        <h5 style="color:black;" id="storeName'+i+'"></h5>\
                        </div>\</div>').appendTo('.carousel-inner');
                    }
                    //<canvas id="visitors-chart" height="200"></canvas>
                }
                storeInformation(data.stores);
                var curr, cap, id, nome;
                [curr, cap, id, nome] = storesData[0]
                renderDonut(curr, cap, id, nome);
                $("#storeName0").html(nome);
                contador++;
            } else {
                console.log("No data");
            }

        },

        error: function() {
            console.log("erro na call");
        }
    })
    return;
}

const storeInformation = function(data){
    data.forEach(function(e, i) {
        storesData[i]=[e.current_capacity, e.capacity, 'donut'+i, e.name];
    })
    return;
}



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
                renderDonut(data.current_capacity, data.capacity, "donutShopping");
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

const loadShoppingsPark = function(){
    $.ajax({
        url: consts.BASE_URL + '/api/Park?id=' + SessionManager.get("session").shopping.id,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            if (data) {
                $("#parkcurcap").html(data.current_capacity);
                $("#parkmaxcap").html(data.capacity);    
                renderDonut(data.current_capacity, data.capacity, "donutPark");
            } else {
                console.log("No data");
            }

        },

        error: function() {
            console.log("erro na call");
        }
    })
}

const renderTable = function (data) {
    var table_data = []
    
    $("#stores_body").empty();
    data.forEach(function(e, i) {
        table_data.push([e.name, '<p class="text-center">'+e.current_capacity+'</p>', '<p class="text-center">'+10+'</p>', '<a href="#" class="text-muted float-right"><i class="fas fa-search"></i></a>']);
    })
   
    table.clear();
    table.rows.add( table_data ).draw();
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
                var number = [data.mapa["MONDAY"], data.mapa["TUESDAY"], data.mapa["WEDNESDAY"], data.mapa["THURSDAY"], data.mapa["FRIDAY"], data.mapa["SATURDAY"], data.mapa["SUNDAY"]];
                var total_visitors = 0;
                for (var i=0; i<number.length; i++){
                    total_visitors =total_visitors + number[i];
                }
                $("#shopping_capacity").html(total_visitors);
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


    
    var donutData        = {
        
      labels: [
          'Occuped',
          'Free',
      ],
      datasets: [
        {
          data: [curr,total-curr],
          backgroundColor : ['#f56954', '#00a65a'],
        }
      ]
    }
    var donutOptions     = {
        
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
