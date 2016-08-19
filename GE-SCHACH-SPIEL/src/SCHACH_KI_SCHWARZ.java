import java.io.IOException;

import ki.*;
import frontend.Frontend;

public class SCHACH_KI_SCHWARZ {
	public static void main(String[] args) throws IOException{
		new Frontend(Konstanten.zumServer,false,new KI002()); // false: schwarz
	}
}
