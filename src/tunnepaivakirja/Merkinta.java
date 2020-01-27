package tunnepaivakirja;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import fi.jyu.mit.ohj2.Mjonot;
import javafx.collections.ObservableList;
import kanta.Tarkistaja;

import static kanta.SatunnaisiaArvoja.*;
/**
 * Tunnepäiväkirjan merkintä, joka osaa mm. itse huolehti tunnusNro:staan
 * @author Hanna Kortetjärvi
 * @version 1.0 22.3.2019
 */
public class Merkinta implements Cloneable {
    private int        tunnusNro;
    private String     pvm            = "";
    private String     kello          = "";
    public String     tunnetila1     = "";
    public String     tunnetila2     = "";
    public String     tunnetila3     = "";
    private String     tilanne        = "";
    private String     tunnereaktio   = "";
    private String     lisatietoja    = "";
    private static int seuraavaNro    = 1;
    
    /**
     * @return Päivämäärä
     * @example
     * <pre name="test">
     *   Merkinta Pvm = new Merkinta();
     *   Pvm.vastaaEnsimmainen();
     *   Pvm.getTunnusNro() === 0;
     * </pre>
     */
    public String getPvm() {
        return pvm;
    }
    
    /**
     * @return Kellonaika
     */
    public String getKello() {
        return kello;
    }
    
    /**
     * Palautetaan tunnetila listasta
     * @param nro Monesko tunne
     * @return Tunnetila jos löytyy
     * @throws SailoException Jos ongelma
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Tunnepaivakirja kirja = new Tunnepaivakirja();
     * Merkinta pvm = new Merkinta();
     * pvm.setTunnusNro(1);
     * Tunnetila tun1 = new Tunnetila();
     * Tunnetila tun2 = new Tunnetila();
     * Tunnetila tun3 = new Tunnetila();
     * TunnetilaID tid1 = new TunnetilaID(); tid1.vastaaID(1);
     * TunnetilaID tid2 = new TunnetilaID(); tid2.vastaaID(2);
     * TunnetilaID tid3 = new TunnetilaID(); tid3.vastaaID(3);
     * tun1.vastaaTunnetila(1,1,7);
     * tun2.vastaaTunnetila(2,1,6);
     * tun3.vastaaTunnetila(3,1,5);
     * kirja.lisaa(pvm);
     * kirja.lisaa(tid1);
     * kirja.lisaa(tid2);
     * kirja.lisaa(tid3);
     * kirja.lisaa(tun1);
     * kirja.lisaa(tun2);
     * kirja.lisaa(tun3);
     * pvm.getTunne(1) === "Masennus";
     * pvm.getTunne(2) === "Suru";
     * pvm.getTunne(3) === "Ilo";
     * </pre>
     */
    public String getTunne(int nro) throws SailoException {
    	Merkinnat merkinnat = new Merkinnat();
    	List<Tunnetila> tunteita = merkinnat.annaTunteita(tunnusNro);
    	if (nro == 1) { //Tunnetila 1
    		if (tunteita.size() < 1) return "";
    		Tunnetila tunne1 = tunteita.get(0);
        	String tunne = tunne1.haeMerkintaan();
    		String tunnetila = tunne.substring(0, tunne.length() - 4);
    		if (tunnetila.charAt(tunnetila.length()-1) == ' ') tunnetila = tunnetila.substring(0, tunnetila.length() - 1);
    		return tunnetila;
    	}
    	if (nro == 2) { //Tunnetila 2
    		if (tunteita.size() < 2) return "";
    		Tunnetila tunne1 = tunteita.get(1);
        	String tunne = tunne1.haeMerkintaan();
    		String tunnetila = tunne.substring(0, tunne.length() - 4);
    		if (tunnetila.charAt(tunnetila.length()-1) == ' ') tunnetila = tunnetila.substring(0, tunnetila.length() - 1);
    		return tunnetila;
    	}
    	if (nro == 3) { //Tunnetila 3
    		if (tunteita.size() < 3) return "";
    		Tunnetila tunne1 = tunteita.get(2);
        	String tunne = tunne1.haeMerkintaan();
    		String tunnetila = tunne.substring(0, tunne.length() - 4);
    		if (tunnetila.charAt(tunnetila.length()-1) == ' ') tunnetila = tunnetila.substring(0, tunnetila.length() - 1);
    		return tunnetila;
    	}
    	return "";
    }
    
    /**
     * Palautetaan tunne
     * @param nro monesko tunne
     * @return tunnetila
     * @example
     * <pre name="test">
     *   Merkinta Pvm = new Merkinta();
     *   Pvm.vastaaEnsimmainen();
     *   Pvm.Tunne(1) === "Masennus";
     *   Pvm.Tunne(2) === "Ahdistus";
     *   Pvm.Tunne(3) === "";
     * </pre>
     */
    public String Tunne(int nro) {
    	if (nro == 1) { //Tunnetila 1
    		String tunnetila = tunnetila1.substring(0, tunnetila1.length() - 4);
    		if (tunnetila.charAt(tunnetila.length()-1) == ' ') tunnetila = tunnetila.substring(0, tunnetila.length() - 1);
    		return tunnetila;
    	}
    	if (nro == 2) { //Tunnetila 2
    		if ("".equals(tunnetila2)) return "";
    		String tunnetila = tunnetila2.substring(0, tunnetila2.length() - 4);
    		if (tunnetila.charAt(tunnetila.length()-1) == ' ') tunnetila = tunnetila.substring(0, tunnetila.length() - 1);
    		return tunnetila;
    	}
    	if (nro == 3) { //Tunnetila 3
    		if ("".equals(tunnetila3)) return "";
    		String tunnetila = tunnetila3.substring(0, tunnetila3.length() - 4);
    		if (tunnetila.charAt(tunnetila.length()-1) == ' ') tunnetila = tunnetila.substring(0, tunnetila.length() - 1);
    		return tunnetila;
    	}
    	return "";
    }
    
    /**
     * Palautetaan tunnetilan voimakkuus listasta
     * @param nro Monesko tunnetila
     * @return Tunnetilan voimakkuus
     * @throws SailoException Jos ongelma
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Tunnepaivakirja kirja = new Tunnepaivakirja();
     * Merkinta pvm = new Merkinta();
     * pvm.setTunnusNro(1);
     * Tunnetila tun1 = new Tunnetila();
     * Tunnetila tun2 = new Tunnetila();
     * Tunnetila tun3 = new Tunnetila();
     * TunnetilaID tid1 = new TunnetilaID(); tid1.vastaaID(1);
     * TunnetilaID tid2 = new TunnetilaID(); tid2.vastaaID(2);
     * TunnetilaID tid3 = new TunnetilaID(); tid3.vastaaID(3);
     * tun1.vastaaTunnetila(1,1,7);
     * tun2.vastaaTunnetila(2,1,6);
     * tun3.vastaaTunnetila(3,1,5);
     * kirja.lisaa(pvm);
     * kirja.lisaa(tid1);
     * kirja.lisaa(tid2);
     * kirja.lisaa(tid3);
     * kirja.lisaa(tun1);
     * kirja.lisaa(tun2);
     * kirja.lisaa(tun3);
     * pvm.getVoima(1) === "7";
     * pvm.getVoima(2) === "6";
     * pvm.getVoima(3) === "5";
     * </pre>
     */
    public String getVoima(int nro) throws SailoException {
    	Merkinnat merkinnat = new Merkinnat();
    	List<Tunnetila> tunteita = merkinnat.annaTunteita(tunnusNro);
    	if (nro == 1) { //Tunnetila 1
    		if (tunteita.size() < 1) return "";
    		Tunnetila tunne1 = tunteita.get(0);
        	String tunne = tunne1.haeMerkintaan();
    		String voimakkuus = tunne.substring(tunne.length() - 3);
    		if (voimakkuus.charAt(0) == '(') voimakkuus = voimakkuus.substring(1,2);
    		else if (voimakkuus.charAt(0) == '1') voimakkuus = voimakkuus.substring(0,2);
    		return voimakkuus;
    	}
    	
    	if (nro == 2) { //Tunnetila 2
    		if (tunteita.size() < 2) return "";
    		Tunnetila tunne1 = tunteita.get(1);
        	String tunne = tunne1.haeMerkintaan();
    		String voimakkuus = tunne.substring(tunne.length() - 3);
    		if (voimakkuus.charAt(0) == '(') voimakkuus = voimakkuus.substring(1,2);
    		else if (voimakkuus.charAt(0) == '1') voimakkuus = voimakkuus.substring(0,2);
    		return voimakkuus;
    	}
    	
    	if (nro == 3) { //Tunnetila 3
    		if (tunteita.size() < 3) return "";
    		Tunnetila tunne1 = tunteita.get(2);
        	String tunne = tunne1.haeMerkintaan();
    		String voimakkuus = tunne.substring(tunne.length() - 3);
    		if (voimakkuus.charAt(0) == '(') voimakkuus = voimakkuus.substring(1,2);
    		else if (voimakkuus.charAt(0) == '1') voimakkuus = voimakkuus.substring(0,2);
    		return voimakkuus;
    	}
    	return "";
    }
    
    
    /**
     * Palautetaan tunnetilan voimakkuus
     * @param nro monesko tunnetila
     * @return voimakkuus
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Merkinta pvm = new Merkinta();
     * pvm.tunnetila1 = "Masennus (4)";
     * pvm.tunnetila2 = "Ahdistus (10)";
     * pvm.tunnetila3 = "Onni (2)";
     * pvm.Voima(1) === "4";
     * pvm.Voima(2) === "10";
     * pvm.Voima(3) === "2";
     * </pre>
     */
    public String Voima(int nro) {
    	if (nro == 1) { //Tunnetila 1
    		String voimakkuus = tunnetila1.substring(tunnetila1.length() - 3);
    		if (voimakkuus.charAt(0) == '(') voimakkuus = voimakkuus.substring(1,2);
    		else if (voimakkuus.charAt(0) == '1') voimakkuus = voimakkuus.substring(0,2);
    		return voimakkuus;
    	}
    	
    	if (nro == 2) { //Tunnetila 2
    		if ("".equals(tunnetila2)) return "";
    		String voimakkuus = tunnetila2.substring(tunnetila2.length() - 3);
    		if (voimakkuus.charAt(0) == '(') voimakkuus = voimakkuus.substring(1,2);
    		else if (voimakkuus.charAt(0) == '1') voimakkuus = voimakkuus.substring(0,2);
    		return voimakkuus;
    	}
    	
    	if (nro == 3) { //Tunnetila 3
    		if ("".equals(tunnetila3)) return "";
    		String voimakkuus = tunnetila3.substring(tunnetila3.length() - 3);
    		if (voimakkuus.charAt(0) == '(') voimakkuus = voimakkuus.substring(1,2);
    		else if (voimakkuus.charAt(0) == '1') voimakkuus = voimakkuus.substring(0,2);
    		return voimakkuus;
    	}
    	return "";
    }
    
    /**
     * @return Tilanne/tapahtuma
     * @example
     * <pre name="test">
     * @example
     * <pre name="test">
     *   Merkinta Pvm = new Merkinta();
     *   Pvm.vastaaEnsimmainen();
     *   Pvm.getTilanne() === "Tuleva tentti";
     * </pre>
     * </pre>
     */
    public String getTilanne() {
        return tilanne;
    }
    
    /**
     * @return Tunnereaktio
     * @example
     * <pre name="test">
     *   Merkinta Pvm = new Merkinta();
     *   Pvm.vastaaEnsimmainen();
     *   Pvm.getReaktio() === "Nopea syke";
     * </pre>
     */
    public String getReaktio() {
        return tunnereaktio;
    }
    
    /**
     * @return Lisätietoja
     * @example
     * <pre name="test">
     *   Merkinta Pvm = new Merkinta();
     *   Pvm.vastaaEnsimmainen();
     *   Pvm.getTietoja() === "";
     * </pre>
     */
    public String getTietoja() {
        return lisatietoja;
    }
    
    /**
     * Asetetaan päivämäärä
     * @param s pvm
     * @return null
     * @example
     * <pre name="test">
     *   Merkinta Pvm = new Merkinta();
     *   Pvm.vastaaEnsimmainen();
     *   Pvm.setPvm("10.12.2019");
     *   Pvm.getPvm() === "10.12.2019";
     * </pre>
     */
	public String setPvm(String s) {
		pvm = s;
		return null;
	}
    /**
     * Asetetaan kellonaika
     * @param s klo
     * @return null
     * @example
     * <pre name="test">
     *   Merkinta Pvm = new Merkinta();
     *   Pvm.vastaaEnsimmainen();
     *   Pvm.setKello("12:00");
     *   Pvm.getKello() === "12:00";
     * </pre>
     */
	public String setKello(String s) {
		kello = s;
		return null;
	}
    /**
     * Asetetaan 1.tunne
     * @param s tunne
     * @return null
     * @example
     * <pre name="test">
     *   Merkinta Pvm = new Merkinta();
     *   Pvm.vastaaEnsimmainen();
     *   Pvm.setTunne1("Ahdistus (7)");
     *   Pvm.Tunne(1) === "Ahdistus";
     * </pre>
     */
	public String setTunne1(String s) {
		tunnetila1 = s;
		return null;
	}
    /**
     * Asetetaan 2.tunne
     * @param s tunne
     * @return null
     * @example
     * <pre name="test">
     *   Merkinta Pvm = new Merkinta();
     *   Pvm.vastaaEnsimmainen();
     *   Pvm.setTunne2("Suru (5)");
     *   Pvm.Tunne(2) === "Suru";
     * </pre>
     */
	public String setTunne2(String s) {
		tunnetila2 = s;
		return null;
	}
    /**
     * Asetetaan 3.tunne
     * @param s tunne
     * @return null
     * @example
     * <pre name="test">
     *   Merkinta Pvm = new Merkinta();
     *   Pvm.vastaaEnsimmainen();
     *   Pvm.setTunne3("Masennus (3)");
     *   Pvm.Tunne(3) === "Masennus";
     * </pre>
     */
	public String setTunne3(String s) {
		tunnetila3 = s;
		return null;
	}
    /**
     * Asetetaan tilanne
     * @param s tilanne
     * @return null
     * @example
     * <pre name="test">
     *   Merkinta Pvm = new Merkinta();
     *   Pvm.vastaaEnsimmainen();
     *   Pvm.setTilanne("Tentti");
     *   Pvm.getTilanne() === "Tentti";
     * </pre>
     */
	public String setTilanne(String s) {
		tilanne = s;
		return null;
	}
    /**
     * Asetetaan reaktio
     * @param s reaktio
     * @return null
     * @example
     * <pre name="test">
     *   Merkinta Pvm = new Merkinta();
     *   Pvm.vastaaEnsimmainen();
     *   Pvm.setReaktio("Paniikki");
     *   Pvm.getReaktio() === "Paniikki";
     * </pre>
     */
	public String setReaktio(String s) {
		tunnereaktio = s;
		return null;
	}
    /**
     * Asetetaan lisätietoja
     * @param s tietoja
     * @return null
     * @example
     * <pre name="test">
     *   Merkinta Pvm = new Merkinta();
     *   Pvm.vastaaEnsimmainen();
     *   Pvm.setTietoja("Kiire?");
     *   Pvm.getTietoja() === "Kiire?";
     * </pre>
     */
	public String setTietoja(String s) {
		lisatietoja = s;
		return null;
	}
    
    /**
     * @return merkinnän kenttien lukumäärä
     */
    public int getKenttia() {
        return 8;
    }
    
    /**
     * @return ensimmäinen käyttäjän syötettävän kentän indeksi
     */
    public int ekaKentta() {
        return 0;
    }
    
    /**
     * Antaa merkinnän tietoja taulukon täyttää varten
     * @param k monekso kohta taulukkoa
     * @return merkinnän osa
     * @throws SailoException Jos ongelma
     * @example
     * <pre name="test">
     * #THROWS SailoException
     *   Merkinta Pvm = new Merkinta();
     *   Pvm.vastaaEnsimmainen();
     *   Pvm.anna(0) === "23.08.2018";
     *   Pvm.anna(5) === "Tuleva tentti";
     *   Pvm.anna(6) === "Nopea syke";
     *   Pvm.anna(7) === "";
     * </pre>
     */
    public String anna(int k) throws SailoException {
    	Merkinnat merkinnat = new Merkinnat();
    	List<Tunnetila> tunteita = merkinnat.annaTunteita(tunnusNro);
    	
    	
        switch (k) {
            case 0:
                return "" + pvm;
            case 1:
                return "" + kello;
            case 2:
            	Tunnetila tunne1 = tunteita.get(0);
            	return "" + tunne1.haeMerkintaan(); //Tunnetila -> Luokka
            case 3:
            	if (tunteita.size() < 2) return "";
            	Tunnetila tunne2 = tunteita.get(1);
            	return "" + tunne2.haeMerkintaan(); //Tunnetila -> Luokka
            case 4:
            	if (tunteita.size() < 3) return "";
            	Tunnetila tunne3 = tunteita.get(2);
            	return "" + tunne3.haeMerkintaan(); //Tunnetila -> Luokka
            case 5:
            	return "" + tilanne;
            case 6:
            	return "" + tunnereaktio;
            case 7:
            	return "" + lisatietoja;
            default:
                return "???";
               
        }
    }
    
    /**
     * Kloonataan merkintä
     * @throws CloneNotSupportedException Jos ongelma
     * @return Kloonattu merkintä
     * @example
     * <pre name="test">
     * #THROWS CloneNotSupportedException 
     *   Merkinta merkinta = new Merkinta();
     *   merkinta.parse("   2|23.08.2018|12:00");
     *   Merkinta kopio = merkinta.clone();
     *   kopio.toString() === merkinta.toString();
     *   merkinta.parse("   3|23.08.2018|12:00");
     *   kopio.toString().equals(merkinta.toString()) === false;
     * </pre>
     */
    @Override
    public Merkinta clone() throws CloneNotSupportedException {
        Merkinta uusi;
        uusi = (Merkinta) super.clone();
        return uusi;
    }
    
    /**
     * Apumetodi, jolla saadaan alustettua ensimmäinen
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Merkinta pvm = new Merkinta();
     * pvm.vastaaEnsimmainen();
     * pvm.getPvm() === "23.08.2018";
     * pvm.getTilanne() === "Tuleva tentti";
     * pvm.getReaktio() === "Nopea syke";
     * </pre>
     */
	public void vastaaEnsimmainen() {
		pvm = "23.08.2018";
        kello = arvoKello();
        tilanne = "Tuleva tentti";
        tunnereaktio = "Nopea syke";
        lisatietoja = "";
            tunnetila1 = "Masennus (7)";
            tunnetila2 = "Ahdistus (6)";
            tunnetila3 = "";
    }

    /**
     * Tulostetaan merkinnän tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
    	out.println(String.format("%03d", tunnusNro));
    	out.println(pvm);
    	out.println(kello);
        out.println("Tilanne: " + tilanne);
        out.println("Tunnereaktio: " + tunnereaktio);
        out.println("Lisätietoja: " + lisatietoja);
    }
    
    /**
     * Tulostetaan merkinnän tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    
    /**
     * Antaa merkinnälle seuraavan rekisterinumeron.
     * @return Merkinnän uusi tunnusNro
     * @example
     * <pre name="test">
     *   Merkinta pvm1 = new Merkinta();
     *   pvm1.getTunnusNro() === 0;
     *   pvm1.rekisteroi();
     *   Merkinta pvm2 = new Merkinta();
     *   pvm2.rekisteroi();
     *   int n1 = pvm1.getTunnusNro();
     *   int n2 = pvm2.getTunnusNro();
     *   n1 === n2-1;
     * </pre>
     */
    public int rekisteroi() {
        if (tunnusNro != 0) return tunnusNro;
        tunnusNro = seuraavaNro;
        seuraavaNro++;
        return 0;
    }
    /**
     * Palauttaa merkinnän tunnusnumeron.
     * @return Merkinnän tunnusnumero
     * @example
     * <pre name="test">
     * Merkinta merkinta = new Merkinta();
     * merkinta.getTunnusNro() === 0;
     * </pre>
     */
    public int getTunnusNro() {
        return tunnusNro;
    }
    
    /**
     * Asetetaan tunnusnumero
     * @param nr Asetettava ID
     * @example
     * <pre name="test">
     * Merkinta merkinta = new Merkinta();
     * merkinta.setTunnusNro(0);
     * merkinta.getTunnusNro() === 0;
     * Merkinta merkinta2 = new Merkinta();
     * merkinta2.setTunnusNro(4);
     * merkinta2.getTunnusNro() === 4;
     * </pre>
     */
    public void setTunnusNro(int nr) {
        tunnusNro = nr;
        if (tunnusNro >= seuraavaNro) seuraavaNro = tunnusNro + 1;
    }
    
    /**
     * Tehdään merkinnästä merkkijono
     * @example
     * <pre name="test">
     *   Merkinta merkinta = new Merkinta();
     *   merkinta.parse("   1  |  23.08.2018   | 17:00  |  Masennus (5)  | Ahdistus  (2)  |  |  Puhe  |  Käheä ääni  |  |");
     *   merkinta.toString().startsWith("1|23.08.2018|17:00|Masennus (5)|") === true; // on enemmäkin kuin 4 kenttää, siksi loppu |
     * </pre>  
     */
    @Override
    public String toString() {
        return "" +
                getTunnusNro() + "|" +
                pvm + "|" +
                kello + "|" +
                tilanne + "|" +
                tunnereaktio + "|" +
                lisatietoja;
    }
    
    /**
     * Erotetaan merkkijono osiksi
     * @param rivi Erotettava merkkijono
     * @example
     * <pre name="test">
     *   Merkinta merkinta = new Merkinta();
     *   merkinta.parse("   1  |  23.08.2018   | 17:00  |  Masennus (5)  | Ahdistus  (2)  |  |  Puhe  |  Käheä ääni  |  |");
     *   merkinta.getTunnusNro() === 1;
     *   merkinta.toString().startsWith("1|23.08.2018|17:00|Masennus (5)|") === true; // on enemmäkin kuin 4 kenttää, siksi loppu |
     * </pre>
     */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        setTunnusNro(Mjonot.erota(sb, '|', getTunnusNro()));
        pvm = Mjonot.erota(sb, '|', pvm);
        kello = Mjonot.erota(sb, '|', kello);
        tilanne = Mjonot.erota(sb, '|', tilanne);
        tunnereaktio = Mjonot.erota(sb, '|', tunnereaktio);
        lisatietoja = Mjonot.erota(sb, '|', lisatietoja);
    }
    
    /**
     * Vertailumetodi
     */
    @Override
    public boolean equals(Object merkinta) {
        if ( merkinta == null ) return false;
        return this.toString().equals(merkinta.toString());
    }

    /**
     * Apumetodi
     */
    @Override
    public int hashCode() {
        return tunnusNro;
    }
    
    /**
     * Testiohjelma merkinnälle.
     * @param args ei käytössä
     */
    public static void main(String args[]) {
        Merkinta pvm = new Merkinta(), pvm2 = new Merkinta();
        pvm.rekisteroi();
        pvm2.rekisteroi();
        
        pvm.tulosta(System.out);
        pvm2.tulosta(System.out);
        
        pvm.vastaaEnsimmainen();
        pvm2.vastaaEnsimmainen();
        
        pvm.tulosta(System.out);
        pvm2.tulosta(System.out);
    }
}