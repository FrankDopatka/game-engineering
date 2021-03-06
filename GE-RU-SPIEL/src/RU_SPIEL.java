import java.io.IOException;

import frontend.Frontend;

public class RU_SPIEL {
	public static void main(String[] args) throws IOException{
		final String zumServer="http://127.0.0.1:8000";
		final int spielerAnzahl=2;
		
		for(int i=1;i<=spielerAnzahl;i++){
			final int idSpieler=i;
			new Thread(){
				@Override
				public void run(){
					new Frontend(zumServer,idSpieler);
				}
			}.start();
		}
	}
}
