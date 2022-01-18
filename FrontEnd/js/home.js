import consts from "./consts.js";
import SessionManager from "./session.js";
import updateView from "./common.js"

var stores_table;
var parks_table;
var past_info_stores;
var past_info_parks;
var contador = 0;
var p_contador = 0;

var stores = [];
var parks = [];
var storesData={};

$(document).ready(function() {
    updateView();
    
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
            var currentIndex = $('#carouselExampleIndicators div.active').index();
            var curr, cap, id, nome;
            [curr, cap, id, nome] = storesData[currentIndex];
            var donutChartCanvas = $('#'+id).get(0).getContext('2d')
            $("#storeName"+currentIndex).html(nome);
            renderDonut(curr, cap, id, nome);
            contador++;
        }
    });

    $('#carouselParks').on('slid.bs.carousel', function () {
        if (p_contador <= parks.length-1){
            var p_currentIndex = $('#carouselParks div.active').index();
            var p_curr, p_cap, p_id, p_nome;
            [p_curr, p_cap, p_id, p_nome] = [parks[p_currentIndex].current_capacity, parks[p_currentIndex].capacity, "parkDonut"+p_currentIndex, parks[p_currentIndex].name];
            var donutChartCanvas = $('#'+p_id).get(0).getContext('2d')
            $("#parkName"+p_currentIndex).html(p_nome);
            renderDonut(p_curr, p_cap, p_id, p_nome);
            p_contador++;
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


                loadShoppingsParks();
                loadShoppingStores();
                past_info_parks = getAllParksLastHourEntrance();
                getAllStoresLastHourEntrance();
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
    console.log(stores)
    for (var i = 0; i < stores.length; i++){
        if (i == 0){
            $('<li data-target="#carouselExampleIndicators" data-slide-to="0" class="active"></li>').appendTo('#to_remove2')
            $('<div class="carousel-item active"><canvas class="d-block w-100" heigh="200" id="donut0" ></canvas><div class="carousel-caption d-none d-md-block">\
            <h5 style="color:black;" id="storeName0"></h5>\
            </div>\
            </div>').appendTo('#itemsStores');
            $( "#to_remove1" ).remove();
            $( "#to_remove2" ).remove();
        }
        else{
            $('<li data-target="#carouselExampleIndicators" data-slide-to="'+i+'"></li>').appendTo('#to_remove2')
            $('<div class="carousel-item"><canvas class="d-block w-100" heigh="200" id="donut'+i+'" ></canvas><div class="carousel-caption d-none d-md-block">\
            <h5 style="color:black;" id="storeName'+i+'"></h5>\
            </div>\</div>').appendTo('#itemsStores');
        }
    }
    for (var j=0; j< parks.length; j++){
        if (j == 0){
            $('<li data-target="#carouselParks" data-slide-to="0" class="active"></li>').appendTo('#arrow_parks')
            $('<div class="carousel-item active"><canvas class="d-block w-100" heigh="200" id="parkDonut0" ></canvas><div class="carousel-caption d-none d-md-block">\
            <h5 style="color:black;" id="parkName0"></h5>\
            </div>\
            </div>').appendTo('#itemsParks');
            $( "#arrow_parks" ).remove();
            $( "#carousel_item" ).remove();
        }
        else{
            $('<li data-target="#carouselParks" data-slide-to="'+i+'"></li>').appendTo('#arrow_parks')
            $('<div class="carousel-item"><canvas class="d-block w-100" heigh="200" id="parkDonut'+j+'" ></canvas><div class="carousel-caption d-none d-md-block">\
            <h5 style="color:black;" id="parkName'+j+'"></h5>\
            </div>\</div>').appendTo('#itemsParks');
        }
    }

    var p_curr, p_cap, p_id, p_nome 
    
    [p_curr, p_cap, p_id, p_nome]= [parks[0].current_capacity, parks[0].capacity, "parkDonut0", parks[0].name];
    renderDonut(p_curr, p_cap, p_id, p_nome);
    $("#parkName0").html(p_nome);
    p_contador++;
    



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
    }

    $("#parkcurcap").html(occupied);
    $("#parkmaxcap").html(total);
    renderDonut(occupied, total, "donutPark");
}

const getAllStoresLastHourEntrance= function(){

    $.ajax({
        url: consts.BASE_URL + '/api/CountLastHoursForStores/' + SessionManager.get("session").shopping.id,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            if (data) {
                past_info_stores = data;
                renderStoresTable(stores);
                return;
            } else {
                console.log("No data");
            }
        },

        error: function() {
            console.log(" erro na call");
        }
    });
}

const getAllParksLastHourEntrance= function(){

    $.ajax({
        url: consts.BASE_URL + '/api/CountLastHoursForParks/' + SessionManager.get("session").shopping.id,
        type: "GET", 
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
            if (data) {
                past_info_parks = data
                renderParksTable(parks);

            } else {
                console.log("No data");
            }
        },

        error: function() {
            console.log(" erro na call");
        }
    });

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
            table_data.push([e.name, '<p class="text-center">'+e.current_capacity+'</p>', '<p class="text-center"><b><span class="text-success mr-1"><i class="ion ion-android-arrow-up text-success"></i> ' + difference + '%</span></b></p>', '<a href="/store.html?id=' + e.id + '" class="text-muted float-right"><i class="fas fa-search"></i></a>']);
        }
        else{
            table_data.push([e.name, '<p class="text-center">'+e.current_capacity+'</p>', '<p class="text-center"><b><span class="text-warning mr-1"><i class="ion ion-android-arrow-up text-warning"></i> ' + difference + '%</span></b></p>', '<a href="/store.html?id=' + e.id + '" class="text-muted float-right"><i class="fas fa-search"></i></a>']);
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
            console.log("HEYYY")
            let two_hours_ago = past_info_parks[e.id]["2_hours_ago"];
            let last_hour = past_info_parks[e.id]["last_hour"];
            
            if (two_hours_ago == 0) {
                difference = (last_hour- two_hours_ago) * 100        
            }
            else {
                difference = ((last_hour - two_hours_ago) / two_hours_ago) * 100
            }
        }
        difference = Math.round(difference)
        if (difference > 0){
            table_data.push([e.name, '<p class="text-center">'+e.current_capacity+'</p>', '<p class="text-center"><b><span class="text-success mr-1"><i class="ion ion-android-arrow-up text-success"></i> ' + difference + '%</span></b></p>', '<a href="#" class="text-muted float-right"><i class="fas fa-search"></i></a>']);
        }
        else{
            table_data.push([e.name, '<p class="text-center">'+e.current_capacity+'</p>', '<p class="text-center"><b><span class="text-warning mr-1"><i class="ion ion-android-arrow-up text-warning"></i> ' + difference + '%</span></b></p>', '<a href="#" class="text-muted float-right"><i class="fas fa-search"></i></a>']);
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
            'Occupied',
            'Free',
        ],
        datasets: [
            {
            data: [curr,total-curr],
            backgroundColor : ['#ff0000', '#13a300'],
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

    var barChartCanvas = $('#barChart').get(0).getContext('2d');

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