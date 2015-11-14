package ru.backend.karte.karten;

import ru.backend.karte.Karte;

public class Planet extends Karte {

	public Planet(){
		setArt("Planet");
		getDaten().addInt("idZielkarte",0);
	}

	@Override
	public void setErlaubteFeldArten() {
		addErlaubteFeldArt("Berg");
		addErlaubteFeldArt("Dschungel");
		addErlaubteFeldArt("Eis");
		addErlaubteFeldArt("Huegel");
		addErlaubteFeldArt("Kueste");
		addErlaubteFeldArt("Meer");
		addErlaubteFeldArt("Steppe");
		addErlaubteFeldArt("Sumpf");
		addErlaubteFeldArt("Wald");
		addErlaubteFeldArt("Wiese");
		addErlaubteFeldArt("Wueste");
	}
}