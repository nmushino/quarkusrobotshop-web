const appvalues = document.querySelector('#appvalues');
const storeId = appvalues.dataset.storeId;
const streamUrl = appvalues.dataset.streamUrl;
const loyaltyStreamUrl = appvalues.dataset.loyaltyStreamUrl;

/* Display the modal popup with selected data */
$('#myModal').on('show.bs.modal', function (event) {

    // hide the alert for successfully adding an item
    $('#item_added_alert').hide();

    var button = $(event.relatedTarget)
    var item = button.data('whatever')
    var item_display_name = button.data('display_name');
    var item_type = button.data('item_type');
    var item_price = button.data('item_price');
    var modal = $(this);
    //        modal.find('.modal-body inplut').val(recipient)
    modal.find('#item_label').text(item_display_name);
    modal.find('#item').val(item);
    modal.find('#item_type').val(item_type);
    modal.find('#item_price').val(item_price);
    displayCurrentOrder();
});

/* Display the current order */
function displayCurrentOrder(){

    // clear out the list to start fresh
    $('#current_order').empty();

    // get the current order
    let homerobots = $('#order_form').find('input[name="homerobots"]').serializeArray();
    let prorobot = $('#order_form').find('input[name="prorobot"]').serializeArray();
    let current_items = homerobots.concat(prorobot);

    if(current_items.length >= 1){
        current_items.forEach(function(e){
            console.log(e);
            let order = JSON.parse(e.value);
            console.log(order);
            $('#current_order').append('<li class="borderless input-cafe">'+ displayFriendlyItem(order.item) + ' for ' + order.name + '</li>');
        });
    }
}

/* Update the current order after a new item is added */
function updateCurrentOrder(item, name){
    let new_line = $('<li class="borderless input-cafe">'+ displayFriendlyItem(item) + ' for ' + name + '</li>')
        .fadeIn(500);
    $('#current_order')
        .append(new_line);
}

/* Modal popup - add item to the order*/
$('#item_form').submit(function( event ) {

    let item_type = $('#item_form').find('input[name="item_type"]').val();
    let item = $('#item_form').find('input[name="item"]').val();
    let name = $('#item_form').find('input[name="name"]').val();
    let price = $('#item_form').find('input[name="item_price"]').val();

    let item_to_add = { 'item' : item, 'name': name, 'price': price};

    if(item_type == 'homerobot'){
        console.log('adding homerobot');
        let homerobots = $('#order_form').find('input[name="homerobots"]').serializeArray();
        if(homerobots.length === 0){
            $('<input>').attr({
                type: 'hidden',
                id: 'homerobots-0',
                name: 'homerobots',
                value: JSON.stringify(item_to_add)
            }).appendTo('form');
        }else if(homerobots.length >= 1){
            $('<input>').attr({
                type: 'hidden',
                id: 'homerobots-' + homerobots.length++,
                name: 'homerobots',
                value: JSON.stringify(item_to_add)
            }).appendTo('form');
        }
        console.log('added');

    }else if(item_type == 'prorobot'){
        console.log('adding prorobot item');
        var prorobot = $('#order_form').find('input[name="prorobot"]').serializeArray();
        if(prorobot.length === 0){
            $('<input>').attr({
                type: 'hidden',
                id: 'prorobot-0',
                name: 'prorobot',
                value: JSON.stringify(item_to_add)
            }).appendTo('form');
        }else if(prorobot.length >= 1){
            $('<input>').attr({
                type: 'hidden',
                id: 'kithen-' + prorobot.length++,
                name: 'prorobot',
                value: JSON.stringify(item_to_add)
            }).appendTo('form');
        }
        console.log('added');
    }

    // display the alert for successfully adding an item
    $('#item_added_alert').text(displayFriendlyItem(item) + ' for ' + name + ' added to order.').show();
    setTimeout(function(){
        updateCurrentOrder(item, name);
    }, 2000);
    setTimeout(function(){
        $('#item_added_alert').fadeOut(1000);
    }, 3000);

    event.preventDefault();
});

/* Modal popup - submit order */
$("#order_form").submit(function(event){
    console.log("order submitted");
    let homerobots = $('#order_form').find('input[name="homerobots"]').map(function(){
        return JSON.parse($(this).val());
    }).get();
    let prorobot = $('#order_form').find('input[name="prorobot"]').map(function(){
        return JSON.parse($(this).val());
    }).get();

    // if the user clicks "Place Order" before "Add"
    if((homerobots === undefined || homerobots.length == 0 ) && (prorobot === undefined || prorobot.length == 0)){

        let item_type = $('#item_form').find('input[name="item_type"]').val();
        let item = $('#item_form').find('input[name="item"]').val();
        let name = $('#item_form').find('input[name="name"]').val();
        let price = $('#item_form').find('input[name="item_price"]').val();
        let item_to_add = { 'name': name, 'item': item, 'price': price };

        if(item_type == 'homerobot'){
            console.log('adding homerobot');
            homerobots.push(item_to_add);
        }else if(item_type == 'prorobot'){
            prorobot.push(item_to_add);
        }

    }

    let rewards_id = $('#rewards_id').val();

    let order = {};
    order.commandType = 'PLACE_ORDER';
    order.id = uuidv4();
    order.orderSource = 'WEB';
    order.storeId = storeId;
    order.rewardsId = rewards_id;
    order.homerobotItems = [];
    order.prorobotItems = []

    if(homerobots.length >= 1){
        for (i = 0; i < homerobots.length; i++) {
            console.log(homerobots[i]);
            order.homerobotItems.push(homerobots[i]);
        }
    }
    if(prorobot.length >= 1){
        for (i = 0; i < prorobot.length; i++) {
            console.log(prorobot[i]);
            order.prorobotItems.push(prorobot[i]);
        }
    }

    console.log("Sending order from web client: " + JSON.stringify(order));

    var jqxhr = $.ajax({
        beforeSend: function(xhrObj){
            xhrObj.setRequestHeader("Content-Type","application/json");
            xhrObj.setRequestHeader("Accept","application/json");
        },
        type: "POST",
        url: "/order",
        data: JSON.stringify(order),
        success: function(){ console.log('success for ' + JSON.stringify(order)); },
        contentType: 'application/json',
        dataType: 'json'
    })
        .done(function() {
            console.log( "done" );
            $('#order_form').find('input[name="homerobots"]').map(function () {
                $(this).remove();
            })
            $('#order_form').find('input[name="prorobot"]').map(function () {
                $(this).remove();
            })
        })
        .fail(function(jqxhr, errorStatus, errorThrown) {
            console.log( "error : " + errorThrown + " " + errorStatus);
        })
        .always(function() {
            console.log( "finished" );
        });

    // close the modal
    $('#myModal').modal('toggle');

    // prevent form submission
    event.preventDefault();
});

function mapToObjectRec(m) {
    let lo = {}
    for(let[k,v] of m) {
        if(v instanceof Map) {
            lo[k] = mapToObjectRec(v)
        }
        else {
            lo[k] = v
        }
    }
    return lo
}

// create a uuid for the order id
function uuidv4() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
        return v.toString(16);
    });
}

// Status Board
$(function () {
    //var source = new EventSource("http://localhost:8080/dashboard/stream");
    //let source = new EventSource(streamUrl);
    var source = new EventSource(streamUrl);

    source.onopen = function(e) {
        console.log("SSE open!");
    };
    
    source.onerror = () => {
        console.log('SSE disconnected!');
        source.close();
    };

    //source.onmessage = function(e) {
    source.addEventListener("message", function(e) {
        if ('END-OF-STREAM' == e.data) {
            source.close(); // stop retry
        }
        var state = JSON.parse(e.data);
        console.log("stare: " + state);
        $("tbody").append(line(state));

        if(state.commandType=="PLACE_ORDER"){
            $("tbody").append(line(state));
        }
        if(state.commandType=="IN_PROGRESS"){40
            console.log(state);
            //$("tbody").append(line(state));
            display(state);
        }
        if(state.commandType=="FULFILLED"){40
            console.log(state);
//              $("#"+state.itemId).replaceWith(line(state));
//              setTimeout(cleanup(state.itemId), 15000);
            display(state);
        }
    });

    // Loyalty toast notification
    var loyaltySource = new EventSource(loyaltyStreamUrl);
    loyaltySource.onmessage = function(e) {
        console.log(e);
        var localtyReward = JSON.parse(e.data);
        var rewardTest = "You have won a free " + localtyReward.reward + "!"

        toastr.options = {
            "closeButton": false,
            "debug": false,
            "newestOnTop": false,
            "progressBar": false,
            "positionClass": "toast-top-right",
            "preventDuplicates": false,
            "onclick": null,
            "showDuration": "300",
            "hideDuration": "1000",
            "timeOut": "15000",
            "extendedTimeOut": "1000",
            "showEasing": "swing",
            "hideEasing": "linear",
            "showMethod": "fadeIn",
            "hideMethod": "fadeOut"
        }

        toastr["success"](rewardTest)
    };

});

function display(state){
    let count = (Math.floor(Math.random() * 15) * 1000) + 5000;
    console.log(count);
    $("#"+state.itemId).replaceWith(line(state));
    setTimeout(function(){ $("#"+state.itemId).remove(); }, count);
}

function cleanup(itemid){
    console.log("time to cleanup" + itemid);
//      $("#"+itemid).remove();
}

function line(state) {

    var orderId = state.orderId;
    var id = state.itemId;
    var product = state.item;
    var customer = state.name;
    var status = state.status;
    var preparedBy = state.madeBy;
    /*
      if (state.item) {
          homerobot = state.item.preparedBy;
      }
    */
    return "<tr id='" + id + "'>" +
        "<td>" + customer + "</td>" +
        "<td>" + displayFriendlyItem(product) + "</td>" +
        "<td>" + displayFriendlyStatus(status) + "</td>" +
        "<td>" + displayFriendlyPreparedBy(preparedBy) + "</td></tr>";
    
}

/* Display friendly prepared by or nothing */
function displayFriendlyPreparedBy(value){
    return value === undefined ? ""
        : value === null ? ""
        : value === 'null' ? ""
        : value;
}

/* Display friendly product names
*/
function displayFriendlyItem(item){
    let result;

    switch(item){
        // homerobots
        case "CP0FB2_BLACK":
            console.log("Black CP0FB2");
            result = "BLACK HOMEROBOT CP0FB2"
            break;
        case "CP1FC3_HOME":
            console.log("CP1FC3 HOME");
            result = "CP1FC3 HOMEROBOT HOME";
            break;
        case "CH99A9":
            console.log("CH99A9");
            result = "CH99A9 HOMEROBOT ";
            break;
        case "CK9FA3":
            console.log("CK9FA3");
            result = "CK9FA3 HOMEROBOT ";
            break;
        case "CS92C3":
            console.log("CS92C3");
            result = "CS92C3 HOMEROBOT ";
            break;
        // prorobot
        case "FAMKD8D8":
            console.log("FAMKD8D8");
            result = "FAMKD8D8 PROROBOT";
            break;
        case "FAC94S3":
            console.log("FAC94S3");
            result = "FAC94S3 PROROBOT";
            break;
        case "FAND78K":
            console.log("FAND78K");
            result = "FAND78K PROROBOT";
            break;
        case "FASKK9K":
            console.log("FASKK9K");
            result = "FASKK9K PROROBOT";
            break;
        // default to the returned value
        default:
            result = item;
            console.log(item);

    }
    return result;
}

function displayFriendlyStatus(status){
    let result;
    switch(status){
        case "PLACE_ORDER":
            console.log("Place Order");
            result = "Place Order";
            break;
        case "IN_PROGRESS":
            console.log("In progress");
            result = "In Progress";
            break;
        case "FULFILLED":
            console.log("Ready!");
            result = "Ready";
            break;
        default:
            result = status;
    }
    return result;
}

/*    $('#rewards_id').on('change', function() {

    let validation = new RegExp(validations['email'][0]);
    // validate the email value against the regular expression
    if (!validation.test(this.value)){
        // If the validation fails then we show the custom error message
        this.setCustomValidity(validations['email'][1]);
        $('#rewards_entered').disable();
        $("#rewards_id").css({"background-color": "color"});
    } else {
        // This is really important. If the validation is successful you need to reset the custom error message
        this.setCustomValidity('');
        $('#rewards_entered').enable();
    }

});*/

$('#rewards_modal').on('submit', function() {

    let rewards_id = $('#rewards_id').val();
    console.log("rewards email entered: " + rewards_id);
    $('#rewards_display_id').text(rewards_id);
    $.cookie('rewards_email',rewards_id, 10);
    $('#btn_cancel').click();
});

$('#rewardsModal').on('shown.bs.modal', function() {
    $('#rewards_id').focus();
});

$( document ).ready(function() {
    let email = $.cookie('rewards_email');

    if (email === undefined){
        //nothing
    }else{
        $('#rewards_id').val(email);
        $('#rewards_display_id').text(email);
    }
});
