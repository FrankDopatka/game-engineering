package frontend;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;

public class EventHandlerHistorie implements ActionListener{
	private Frontend frontend;
	private int zugNummer;
	private boolean zugVonWeiss=false;
	
	public EventHandlerHistorie(Frontend frontend, int x, int y) {
		this.frontend=frontend;
		this.zugNummer=y;
		if (x==1) this.zugVonWeiss=true;
	}

	private void resetButtons(){
		for (JButton b:frontend.getHistorieButtons()){
			b.setBackground(new Color(200,200,200));
			b.setEnabled(true);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		frontend.setHistorienAnsicht(true);
		resetButtons();
		JButton b=(JButton)e.getSource();
		b.setBackground(new Color(200,200,0));
		BufferedImage brettBild=(BufferedImage)frontend.getBackendSpiel().getBildHistorie(frontend.ichSpieleWeiss(),zugNummer,zugVonWeiss);
		if (brettBild!=null) frontend.setBrett(brettBild);
	}
}
