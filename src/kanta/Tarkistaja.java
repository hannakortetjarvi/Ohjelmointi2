package kanta;

/**
 * Tarkistaja joka tarkistaa että jono sisältää vain valittuja merkkejä.
 * Hyväksyy tyhjän jonon.
 * @author Hanna Kortetjärvi
 * @version 26.4.2019
 */
public class Tarkistaja  {
	

    /**
     * Onko jonossa vain sallittuja merkkejä
     * @param jono      tutkittava jono
     * @param sallitut  merkit joita sallitaan
     * @return true jos vain sallittuja, false muuten
     * @example
     * <pre name="test">
     *   onkoVain("123","12")   === false;
     *   onkoVain("123","1234") === true;
     *   onkoVain("","1234") === true;
     * </pre> 
     */
    public static boolean onkoVain(String jono, String sallitut) {
        for (int i=0; i<jono.length(); i++)
            if ( sallitut.indexOf(jono.charAt(i)) < 0 ) return false;
        return true;
    }


    private final String sallitut;


    /**
     * Luodaan tarkistaja joka hyväksyy sallitut merkit
     * @param sallitut hyväksyttävät merkit
     */
    public Tarkistaja(String sallitut) {
        this.sallitut = sallitut;
    }

    /**
     * Tarkistaa että jono validi
     * @param jono tutkittava jono
     * @return null jos vain valittujan merkkejä, muuten virheilmoitus
     * @example
     * <pre name="test">
     *   Tarkistaja tar = new Tarkistaja("0123456789");
     *   tar.tarkistaKello("12") === "Anna kellonaika muodossa 00:00";
     *   tar.tarkistaKello("14:00") === null;
     *   tar.tarkistaKello("12:aa")   === "Ajassa saa olla vain numeroita";
     * </pre>
     */
    public String tarkistaKello(String jono) {
        int pituus = jono.length();
        if ( pituus != 5 ) return "Anna kellonaika muodossa 00:00";
        String tunti = jono.substring(0,2);
        if ( !onkoVain(tunti,sallitut)) return "Ajassa saa olla vain numeroita";
        int t = Integer.parseInt(tunti);
        if ( 24 < t ) return "Liian monta tuntia";
        if (jono.charAt(2) != ':') return "Anna kellonaika muodossa 00:00";
        String min = jono.substring(3);
        if ( !onkoVain(min,sallitut)) return "Ajassa saa olla vain numeroita";
        int m = Integer.parseInt(min);
        if ( m >= 60 )  return "Liian monta minuuttia";
        return null;
    }
    
    /**
     * Tarkistaa että jono validi
     * @param jono tutkittava jono
     * @return null jos vain valittujan merkkejä, muuten virheilmoitus
     * @example
     * <pre name="test">
     *   Tarkistaja tar = new Tarkistaja("0123456789");
     *   tar.tarkistaVoima("1") === null;
     *   tar.tarkistaVoima("12") === "Anna voimakkuus väliltä 1-10";
     *   tar.tarkistaVoima("a")   === "Voimassa saa olla vain numeroita";
     * </pre>
     */
    public String tarkistaVoima(String jono) {
    	if (!onkoVain(jono,sallitut)) return "Voimassa saa olla vain numeroita";
    	int v = Integer.parseInt(jono);
    	if (v < 1 || v > 10) return "Anna voimakkuus väliltä 1-10";
    	return null;
    }

}