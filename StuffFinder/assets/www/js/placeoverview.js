function transaction_error(tx, error) {
	$('#busy').hide();
    alert("Database Error: " + tx.message);
}

function populateDB_success() {
	$('#busy').hide();
	dbCreated = true;
    db.transaction(getPlaces, transaction_error);
}

function getPlaces(tx) {
	var sql = "select p.id, p.name, p.description, p.picture " + 
				"from places p " +
				"group by p.name order by p.name";
	tx.executeSql(sql, [], getPlaces_success);
}


function addPlace(tx){

	var name = $("#place-name").val();
	var description = $("#place-description").val();
    var sql = "INSERT INTO places (name,description,picture) "
    +"VALUES (:name,null,:description)";

    tx.executeSql(sql, [name, description], getPlaces);
}


function getPlaces_success(tx, results) {
	$('#busy').hide();
    var len = results.rows.length;
    for (var i=0; i<len; i++) {
    	var place = results.rows.item(i);
		$('#placeList').append('<li><a href="placedetails.html?id=' + place.id + '">' +
				'<img src="pics/items/' + place.picture + '" class="list-icon"/>' +
				'<p class="line1">' + place.name +  '</p>' +
				'<p class="line2">' + place.description + '</p>' +
			//	'<p class="line4">' + place.place + '</p>' +
				'</a></li>');
    }

    $("li a").bind( "taphold", showDeletePopup(place));
	setTimeout(function(){
		scroll.refresh();
	},100);
	db = null;
}

