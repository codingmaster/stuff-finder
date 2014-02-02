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
	var sql = "select i.id, i.name, i.category, i.description, i.picture, p.name place " + 
				"from items i left join places p on i.placeId = p.id " +
				"group by i.name order by i.category";
	tx.executeSql(sql, [], getItems_success);
}



function addElement(tx){

	var name = $("#item-name").val();
	var category = $("#item-category").val(); 
	var description = $("#item-description").val();
    var sql = "INSERT INTO items (name,category,placeId,description,picture) "
    +"VALUES (:name,:category,null,:description,null)";

    tx.executeSql(sql, [name, category, description]);
}



function getItems_success(tx, results) {
	$('#busy').hide();
    var len = results.rows.length;
    for (var i=0; i<len; i++) {
    	var item = results.rows.item(i);
		$('#itemList').append('<li><a href="itemdetails.html?id=' + item.id + '" rel="external">' +
				'<img src="pics/' + item.picture + '" class="list-icon"/>' +
				'<p class="line1">' + item.name +  '</p>' +
				'<p class="line2">' + item.category + '</p>' +
		//		'<p class="line2">' + item.place + '</p>' +
				'</a></li>');
    }
    $("li a").bind( "taphold", function() {showDeletePopup(item); return false});
}

function getElement(tx) {
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
		'<li><a href="placedetails.html?id=' + item.placeid + '" rel="external">' + 'Place: '+ item.place + '</a></li>'


		);
}




