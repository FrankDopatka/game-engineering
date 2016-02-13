package ru.backend.karte.karten;

import ru.backend.karte.Karte;

public class Weltraum extends Karte {

	public Weltraum(){
		setArt("Weltraum");
	}

	@Override
	public void setErlaubteFeldArten() {
		addErlaubteFeldArt("Leere");
		addErlaubteFeldArt("DschungelPlanet");
		addErlaubteFeldArt("EisPlanet");
		addErlaubteFeldArt("ErdPlanet");
		addErlaubteFeldArt("HuegelPlanet");
		addErlaubteFeldArt("SteinBergPlanet");
		addErlaubteFeldArt("SteppePlanet");
		addErlaubteFeldArt("SumpfPlanet");
		addErlaubteFeldArt("WaldPlanet");
		addErlaubteFeldArt("WasserPlanet");
		addErlaubteFeldArt("WiesePlanet");
		addErlaubteFeldArt("WuestenPlanet");
		addErlaubteFeldArt("EisMond");
		addErlaubteFeldArt("WuestenMond");
		addErlaubteFeldArt("Mond");
		addErlaubteFeldArt("Wurmloch");
	}
}
