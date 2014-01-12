package de.mwolowyk.stuffinder.model;

public class Item {
	int id;
	String name;
	String category;
	String description;
	
	public Item(){
		
	}
	


	public Item(String name, String category, String description) {
		super();
		this.name = name;
		this.category = category;
		this.description = description;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}



	public String getCategory() {
		return category;
	}



	public void setCategory(String category) {
		this.category = category;
	}
	
	
	
	
}
