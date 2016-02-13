package frontend.karte.flaechen;

import javax.swing.JLabel;

import frontend.EventHandler.EventHandler;
import ru.daten.D_Feld;

public abstract class Platz extends JLabel{
	protected static final long serialVersionUID = 1L;
	protected int x;
	protected int y;
	protected String feldArt;
	
	public Platz(int x,int y,String feldArt) {
		this.x=x;
		this.y=y;
		this.feldArt = feldArt;
		setEnabled(true);
		setOpaque(true);
		setFocusable(false);
		setVisible(true);
	}
	
	public String getFeldArt(){
		return feldArt;
	}
	
	public void setFeldArt(String feldArt){
		this.feldArt = feldArt;
	}
	
	public abstract void setEventhandler(EventHandler eventHandler);

	public int getPosX(){
		return x;
	}
	public int getPosY(){
		return y;
	}
	public int[] getPos(){
		int[] ausgabe={getPosX(),getPosY()};
		return ausgabe;
	}

	public abstract void zeichnen();
	
	public abstract void zeichnen(D_Feld daten);
	
	public abstract void terminate();
	
}
