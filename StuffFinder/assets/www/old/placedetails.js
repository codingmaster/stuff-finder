var scroll = new iScroll('wrapper', { vScrollbar: false, hScrollbar:false, hScroll: false });

var id = getUrlVars()["id"];

var db;

document.addEventListener("deviceready", onDeviceReady, false);

function onDeviceReady() {
	console.log("opening database");
    db = window.openDatabase("StuffDatabase", "1.0", "StuffFinder", 200000);
	console.log("database opened");
    db.transaction(getPlace, transaction_error);
}

function transaction_error(tx, error) {
	$('#busy').hide();
    alert("Database Error: " + tx.message);
}

function getPlace(tx) {
	$('#busy').show();
	var sql = "select p.id, p.name, p.description, p.picture " +
				"from places p " +
				"where p.id=:id";
	tx.executeSql(sql, [id], getPlace_success);
}

function getPlace_success(tx, results) {
	$('#busy').hide();
	var place = results.rows.item(0);
	$("#placeDetails").append(
		'<li><h3>' + place.name + '</h3></li>' +
		'<li><img class="detailsPic" src="' + 'pics/items/' + place.picture + '"/></li>' + 
		'<li><p>Description: ' +  place.description + '<p/></li>'
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
