import java.io.IOException;

import frontend.Frontend;

public class SCHACH_WEISS {
	public static void main(String[] args) throws IOException{
		new Frontend(Konstanten.zumServer,true); // true: weiss
	}
}
