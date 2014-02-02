var id = getUrlVars()["id"];
function transaction_error(tx, error) {
	$('#busy').hide();
    alert("Database Error: " + tx.message);
}

function populateDB_success() {
	$('#busy').hide();
	dbCreated = true;
    db.transaction(getElements, transaction_error);
}

function getElements(tx) {
	var sql = "select p.id, p.name, p.description, p.picture " + 
				"from places p " +
				"group by p.name order by p.name";
	tx.executeSql(sql, [], getPlaces_success);
}


function addElement(tx){

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
		$('#placeList').append('<li><a href="placedetails.html?id=' + place.id + '" rel="external">' +
				'<img src="pics/items/' + place.picture + '" class="list-icon"/>' +
				'<p class="line1">' + place.name +  '</p>' +
				'<p class="line2">' + place.description + '</p>' +
			//	'<p class="line4">' + place.place + '</p>' +
				'</a></li>');
    }

    $("li a").bind( "taphold", showDeletePopup(place));

}

function getElement(tx) {
	$('#busy').show();
	var sql = "select p.id, p.name, p.description, p.picture " +
				"from places p " +
				"where p.id=:id group by p.name order by p.name";
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
}

