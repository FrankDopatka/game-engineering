package ru.backend.karte.karten;

import ru.backend.karte.Karte;

public class Weltraum extends Karte {

	public Weltraum(){
		setArt("Weltraum");
	}

	@Override
	public void setErlaubteFeldArten() {
		addErlaubteFeldArt("Leere");
		addErlaubteFeldArt("Planet");
		addErlaubteFeldArt("Mond");
		addErlaubteFeldArt("Wurmloch");
	}
}
