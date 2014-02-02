var db = window.openDatabase("StuffDatabase", "1.0", "StuffFinder", 200000);;
var dbCreated = false;

var scroll = new iScroll('wrapper', { vScrollbar: false, hScrollbar:false, hScroll: false });


function showPage(){
    if (!dbCreated){
        db.transaction(populateDB, transaction_error, populateDB_success);
        dbCreated = true;
    }
    var urlPage = getUrlVars()[0].split("#")[1];
    var getElements;
    if(urlPage === "places"){
        getElements = getPlaces;
    }
    else{
        getElements = getItems;
    }
    db.transaction(getElements, transaction_error);
}

function addElement(){
    db = window.openDatabase("StuffDatabase", "1.0", "StuffFinder", 200000);
    var urlPage = getUrlVars()[0].split("#")[1];
    var func;
    if(urlPage === "newplace"){
        func = addPlace;
    }
    else if(urlPage === "newitem"){
        func = addItem;
    }
    db.transaction(func, transaction_error);   
}

function showDeletePopup(element){
  //  alert("Popup " + element);
    if(element !== undefined){
        alert("popup for element " + element.name);
 //       $("#popupDialog").popup("open");
    }
    return false;
}


function transaction_error(tx, error) {
	$('#busy').hide();
    alert("Database Error: " + tx.message);
}

function populateDB_success() {
	$('#busy').hide();
	dbCreated = true;
    db.transaction(getItems, transaction_error);
}

function populateDB(tx){
    populatePlaces(tx);
    populateItems(tx);
}

function populatePlaces(tx) {
    $('#busy').show();
    tx.executeSql('DROP TABLE IF EXISTS places');
    var sql = 
        "CREATE TABLE IF NOT EXISTS places ( "+
        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "name VARCHAR(50), " +
        "description VARCHAR(255), " +
        "picture VARCHAR(200)" +
        ")";
    tx.executeSql(sql);

    tx.executeSql("INSERT INTO places (id,name,description,picture) VALUES (1,'fridge','put your food inside','fridge.jpg')");
    tx.executeSql("INSERT INTO places (id,name,description,picture) VALUES (2,'cupboard','put everything inside','cupboard.jpg')");
    tx.executeSql("INSERT INTO places (id,name,description,picture) VALUES (3,'street','if you dont have place in the room put something outside', 'street.jpg')");
    tx.executeSql("INSERT INTO places (id,name,description,picture) VALUES (4,'tvset','all your games are here','tvset.jpg')");
    tx.executeSql("INSERT INTO places (id,name,description,picture) VALUES (5,'city','many people living here','city.jpg')");
}



function populateItems(tx) {
    $('#busy').show();
    tx.executeSql('DROP TABLE IF EXISTS items');
    var sql = 
        "CREATE TABLE IF NOT EXISTS items ( "+
        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "name VARCHAR(50), " +
        "category VARCHAR(50), " +
        "description VARCHAR(255), " +
        "placeId INTEGER, " +
        "picture VARCHAR(200)" +
        ")";
    tx.executeSql(sql);

  tx.executeSql("INSERT INTO items (id,name,category,placeId,description,picture) VALUES (12,'Tasty Burger','food',4,'The hamburger is a very popular type of sandwich consisting of a bun with a meat patty in the middle, often combined with various condiments and/or toppings and garnishes.','items/burger.jpg')");
  tx.executeSql("INSERT INTO items (id,name,category,placeId,description,picture) VALUES (11,'Gun','weapon',5,'The gun has played a critical role in history. ', 'items/gun.png')");
  tx.executeSql("INSERT INTO items (id,name,category,placeId,description,picture) VALUES (10,'Tomato ketchup','food',5,'Ketchup (or catsup) is a tomato-based food condiment. ', 'items/ketchup.jpg')");
  tx.executeSql("INSERT INTO items (id,name,category,placeId,description,picture) VALUES (9,'Mario','game',2,'If youve found your way to Giant Bomb, you probably have a good idea of who Mario is and why hes significant.', 'items/mario.png')");
  tx.executeSql("INSERT INTO items (id,name,category,placeId,description,picture) VALUES (8,'NBA JAM','game',2,'NBA Jam is a basketball arcade game developed and released by Midway in 1993. ', 'items/nba.png')");
  tx.executeSql("INSERT INTO items (id,name,category,placeId,description,picture) VALUES (7,'Nintendo','game',4,'The Nintendo DS was released in 2004 and succeeded the Game Boy Advance. ', 'items/nintendo.png')");
  tx.executeSql("INSERT INTO items (id,name,category,placeId,description,picture) VALUES (5,'Barack Obama','person',1,'Barack Obama is the current President of the United States of America and was sworn in as the 44th President on January 20th, 2009.', 'items/obama.jpg')");
  tx.executeSql("INSERT INTO items (id,name,category,placeId,description,picture) VALUES (6,'Will Smith','person',4,'Someone thought it would be a good idea to make the Fresh Prince an unlockable character in NBA Jam Tournament Edition, supposedly because the sitcom made a point of Will Smith (the on-screen character) being a very good basketball player.', 'items/will_smith.jpg')");
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