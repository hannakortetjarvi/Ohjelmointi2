package kanta;

/**
 * Arvotaan tunnepaivakirjaan satunnaisia arvoja
 * @author Hanna Kortetjärvi
 * @version 1.0 2.4.2019
 */
public class SatunnaisiaArvoja {
	
	public static final String TARKISTUS = "01234567890:";
	
	/**
	 * Luodaan randomi luku
	 * @param ala alaraja
	 * @param yla yläraja
	 * @return Arvottu luku
	 */
	public static int rand(int ala, int yla) {
		double a = (yla-ala)*Math.random() + ala;
		return (int)Math.round(a);
	}
	
	/**
	 * Luodaan randomi kellonaika
	 * @return apukellonaika
	 */
	public static String arvoKello() {
		String apukello = String.format("%02d", rand(0,24)) + ":" + String.format("%02d",  rand(0,59));
		return apukello;
	}
	
	/**
	 * Luodaan random voimakkuus
	 * @return apuvoimakkuus
	 */
	public static int arvoVoimakkuus() {
		int apuvoima = rand(1,10);
		return apuvoima;
	}
}
