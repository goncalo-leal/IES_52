import consts from "./consts.js";
import SessionManager from "./session.js";
import updateView from "./common.js"

var stores_table;
var parks_table;
var past_info_stores;
var past_info_parks;
var contador = 0;
var stores = [];
var parks = [];
var storesData={};

$(document).ready(function() {
    updateView();
    
    past_info_stores = getAllStoresLastHourEntrance();
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
    loadPeopleByWeek();
    loadShoppingEntrancesLastHour();

    $("#stores_search_txt").on('input', function() {
        stores_table.search($('#stores_search_txt').val()).draw();
    });

    $("#parks_search_txt").on('input', function() {
        parks_table.search($('#parks_search_txt').val()).draw();
    });

    $('#carouselExampleIndicators').on('slid.bs.carousel', function () {
        if (contador <= stores.length-1){
            var currentIndex = $('div.active').index();
            var curr, cap, id, nome;
            [curr, cap, id, nome] = storesData[currentIndex];
            var donutChartCanvas = $('#'+id).get(0).getContext('2d')
            $("#storeName"+currentIndex).html(nome);
            renderDonut(curr, cap, id, nome);
            contador++;
        }
    });
});

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
                parks = data.parks;
                
                $("#shopcurcap").text(data.current_capacity);
                $("#shopmaxcap").text(data.capacity);
                
                renderDonut(data.current_capacity, data.capacity, "donutShopping");
                if (parks.length > 0) {
                    console.log("calculate parks...");
                } else {
                    $("#parkingTab").empty();
                }

                renderStoresTable(stores);
                renderParksTable(parks);

                loadShoppingsParks();
                loadShoppingStores();
            } else {
                console.log("No store for this shopping");
            }
        },

        error: function() {
            console.log("erro na call");
        }
    })
}

const loadShoppingStores = function() {
    for (var i = 0; i < stores.length; i++){
        if (i == 0){
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
    storeInformation(stores);
    var curr, cap, id, nome;
    [curr, cap, id, nome] = storesData[0]
    renderDonut(curr, cap, id, nome);
    $("#storeName0").html(nome);
    contador++;
    return;
}

const storeInformation = function(data){
    data.forEach(function(e, i) {
        storesData[i]=[e.current_capacity, e.capacity, 'donut'+i, e.name];
    })
    return;
}

const loadShoppingsParks = function() {
    let occupied = 0;
    let total = 0;

    for (var i = 0; i < parks.length; i++){
        occupied += parks[i]["current_capacity"];
        total += parks[i]["capacity"];
        // aqui também podes gerar os donuts de cada park como tens no loadShoppingsStores()
    }

    $("#parkcurcap").html(occupied);
    $("#parkmaxcap").html(total);
    renderDonut(occupied, total, "donutPark");
}

const getAllStoresLastHourEntrance= function(){
    var to_ret = null;

    $.ajax({
        url: consts.BASE_URL + '/api/CountLastHoursForStores/' + SessionManager.get("session").shopping.id,
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

const getAllParksLastHourEntrance= function(){
    var to_ret = null

    $.ajax({
        url: consts.BASE_URL + '/api/CountLastHoursForParks/' + SessionManager.get("session").shopping.id,
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
        url: consts.BASE_URL + '/api/PeopleInShoppingInLastHour/' + SessionManager.get("session").shopping.id,
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

const renderStoresTable = function (data) {
    var table_data = []

    $("#stores_body").empty();
    data.forEach(function(e, i) {
        let difference = 0
        if (past_info_stores !== null) {
            let horas_atrs = past_info_stores[e.id]["2_hours_ago"];
            let horas_atual = past_info_stores[e.id]["last_hour"];

            if (horas_atrs == 0) {
                difference = (horas_atual- horas_atrs) * 100        
            }
            else {
                difference = ((horas_atual - horas_atrs) / horas_atrs) * 100           
            }
        }

        if (difference >0){
            table_data.push([e.name, '<p class="text-center">'+e.current_capacity+'</p>', '<p class="text-center"><b><span class="text-success mr-1"><i class="ion ion-android-arrow-up text-success"></i>' + difference + '%</span></b></p>', '<a href="/store.html?id=' + e.id + '" class="text-muted float-right"><i class="fas fa-search"></i></a>']);
        }
        else{
            table_data.push([e.name, '<p class="text-center">'+e.current_capacity+'</p>', '<p class="text-center"><b><span class="text-warning mr-1"><i class="ion ion-android-arrow-up text-warning"></i>' + difference + '%</span></b></p>', '<a href="/store.html?id=' + e.id + '" class="text-muted float-right"><i class="fas fa-search"></i></a>']);
        }
    });
   
    stores_table.clear();
    stores_table.rows.add( table_data ).draw();
}

const renderParksTable = function (data) {
    var table_data = []

    $("#parks_body").empty();
    data.forEach(function(e, i) {
        let difference = 0
        if (past_info_parks !== null) {
            let two_hours_ago = past_info_parks[e.id]["2_hours_ago"];
            let last_hour = past_info_parks[e.id]["last_hour"];
            
            if (two_hours_ago == 0) {
                difference = (last_hour- two_hours_ago) * 100        
            }
            else {
                difference = ((last_hour - two_hours_ago) / two_hours_ago) * 100
            }
        }

        if (difference > 0){
            table_data.push([e.name, '<p class="text-center">'+e.current_capacity+'</p>', '<p class="text-center"><b><span class="text-success mr-1"><i class="ion ion-android-arrow-up text-success"></i>' + difference + '%</span></b></p>', '<a href="#" class="text-muted float-right"><i class="fas fa-search"></i></a>']);
        }
        else{
            table_data.push([e.name, '<p class="text-center">'+e.current_capacity+'</p>', '<p class="text-center"><b><span class="text-warning mr-1"><i class="ion ion-android-arrow-up text-warning"></i>' + difference + '%</span></b></p>', '<a href="#" class="text-muted float-right"><i class="fas fa-search"></i></a>']);
        }
    });
   
    parks_table.clear();
    parks_table.rows.add( table_data ).draw();
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
                if (diferença >0){
                    $("#Total_diferença_semanas").after($("<i class='ion ion-android-arrow-up text-success' ></i>").text(diferença+ "% Since last week")  )
                }
                else{
                    $("#Total_diferença_semanas").after($("<i class='ion ion-android-arrow-down text-warning' ></i>").text(diferença+ "% Since last week")  )

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

    var barChartCanvas = $('#barChart').get(0).getContext('2d');

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