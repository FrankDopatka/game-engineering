package schach.daten;

public enum ZugEnum {
	RochadeKurz,RochadeLang,EnPassant,BauerDoppelschritt,BauerUmwandlung,BauerUmwandlungImGange;
	
	public static ZugEnum toEnumFromString(String s){
		if ((s==null)||(s.length()==0)) return null;
		return valueOf(s);
	}
}
