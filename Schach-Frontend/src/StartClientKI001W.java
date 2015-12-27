import java.io.IOException;

import ki.*;
import frontend.Frontend;

public class StartClientKI001W {
	public static void main(String[] args) throws IOException{
		new Frontend(Konstanten.zumServer,true,new KI001()); // false: schwarz
	}
}
