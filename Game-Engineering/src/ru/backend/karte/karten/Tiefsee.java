package ru.backend.karte.karten;

import ru.backend.karte.Karte;

public class Tiefsee extends Karte {

	public Tiefsee(){
		setArt("Tiefsee");
	}

	@Override
	public void setErlaubteFeldArten() {
		addErlaubteFeldArt("Tiefsee");
	}
}
