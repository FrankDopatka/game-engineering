package ru.daten;

public class D_AuswahlUniversum extends D {
	
public D_AuswahlUniversum() {
		
		addString("name","");
		addInt("id",0);
	}

	public D_AuswahlUniversum(String name,int id) {
		
		addString("name",name);
		addInt("id",id);
	}
	
}
