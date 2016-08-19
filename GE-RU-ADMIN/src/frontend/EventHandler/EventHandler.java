package frontend.EventHandler;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import frontend.Frontend;
import frontend.karte.flaechen.Feld;
import frontend.karte.flaechen.Platz;
import ru.interfaces.iBackendKarteneditor;

public abstract class EventHandler implements MouseListener,MouseMotionListener {
	
	protected Frontend frontend;
	protected iBackendKarteneditor backendKarteneditor;
	protected boolean dragging=false;
	// generiere ID zur Vergabe einer RefKartenID
	protected int id = 0;
	
	
	public EventHandler(Frontend frontend) {
		this.frontend=frontend;
		this.backendKarteneditor=frontend.getBackendKarteneditor();
	}

	@Override
	public void mouseMoved(MouseEvent ev) {
		Platz feld=(Platz)ev.getSource();
		frontend.setStatus(feld.toString());
	}

	@Override
	public void mouseDragged(MouseEvent ev) {
		Platz feld=(Platz)ev.getSource();
		aktion(feld);
		dragging=true;
	}

	@Override
	public void mouseEntered(MouseEvent ev) {
		Platz feld=(Platz)ev.getSource();
		if (dragging) aktion(feld);
	}

	@Override
	public void mouseReleased(MouseEvent ev) {
		dragging=false;
	}

	@Override
	public void mouseClicked(MouseEvent ev) {
		Platz feld=(Platz)ev.getSource();
		aktion(feld);
	}
	
	
	protected abstract void aktion(Platz feld);
	
	
	// generiert eine Zufahlszahl
	protected int generiereID() {
		return ++this.id;
	}
	
	protected abstract void feldSetzen(Platz feld);

	protected void ressourceLoeschen(Feld feld){
		dragging=false;
		backendKarteneditor.delRessource(feld.getPos()[0],feld.getPos()[1]);
		feld.zeichnen();
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
	
}
