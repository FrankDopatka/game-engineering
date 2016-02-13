package ru.backend.karte.karten;

import ru.backend.karte.Karte;

public class Mond extends Karte {

	public Mond(){
		setArt("Mond");
	}
	

	@Override
	public void setErlaubteFeldArten() {
		addErlaubteFeldArt("Berg");
		addErlaubteFeldArt("Eis");
		addErlaubteFeldArt("Huegel");
		addErlaubteFeldArt("Wueste");
	}
}