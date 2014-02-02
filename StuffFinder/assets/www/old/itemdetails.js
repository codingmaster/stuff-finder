var scroll = new iScroll('wrapper', { vScrollbar: false, hScrollbar:false, hScroll: false });

var id = getUrlVars()["id"];

var db;

document.addEventListener("deviceready", onDeviceReady, false);

function onDeviceReady() {
	console.log("opening database");
    db = window.openDatabase("StuffDatabase", "1.0", "StuffFinder", 200000);
	console.log("database opened");
    db.transaction(getItem, transaction_error);
}

function transaction_error(tx, error) {
	$('#busy').hide();
    alert("Database Error: " + tx.message);
}

function getItem(tx) {
	$('#busy').show();
	var sql = "select i.id, i.name, i.category, i.description, i.picture, p.name place, p.id placeid " +
				"from items i left join places p on p.id = i.placeId " +
				"where i.id=:id group by i.name order by i.name";
	tx.executeSql(sql, [id], getItem_success);
}

function getItem_success(tx, results) {
	$('#busy').hide();
	var item = results.rows.item(0);
	$("#itemDetails").append(
		'<li><h3>' + item.name + '</h3></li>' +
		'<li><img class="detailsPic" src="' + 'pics/' + item.picture + '"/></li>' + 
		'<li><p>Description: ' +  item.description + '<p/></li>' + 
		'<li><a href="placedetails.html?id=' + item.placeid + '">' + 'Place: '+ item.place + '</a></li>'


		);
	setTimeout(function(){
		scroll.refresh();
	});
	db = null;
}




function getUrlVars() {
    var vars = [], hash;
    var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
    for(var i = 0; i < hashes.length; i++)
    {
        hash = hashes[i].split('=');
        vars.push(hash[0]);
        vars[hash[0]] = hash[1];
    }
    return vars;
}
