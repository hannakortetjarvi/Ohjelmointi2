package tunnepaivakirja;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.*;

import fi.jyu.mit.ohj2.Mjonot;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * TunnetilaID joka mm. huolehtii omasta tunnusnumerostaan
 * @author Hanna Kortetjärvi
 * @version 1.0 22.3.2019
 */
public class TunnetilaID {
	private int tunnusNro;
	public String tunnetila;
	
	private String[] tunnetilat = {"Masennus","Suru","Ilo", "Ahdistus", "Häpeä", "Onni", "Viha"};
	ObservableList<String> valinnat = FXCollections.observableArrayList(); 
	
	private static int seuraavaNro = 1;
	
	/**
	 * Alustetaan TunnetilaID
	 */
	public TunnetilaID() {
		// Vielä ei tee mitään
	}
	
	/**
	 * Alustetaan tietyn tunnetilan ID
	 * @param tunnusNro Tunnetilan viitenumero
     * @example
     * <pre name="test">
     * TunnetilaID tid = new TunnetilaID(2);
     * tid.getTunnusNro() === 2;
     * TunnetilaID tid2 = new TunnetilaID(3);
     * tid2.getTunnusNro() === 3;
     * </pre>
	 */
	public TunnetilaID(int tunnusNro) {
		this.tunnusNro = tunnusNro;
	}
	
	/**
	 * Apumetodi, jolla saadaan täytettyä testiarvot TunnetilaID:lle
	 * @param nro Viite tunnetilaan
	 * @example
     * <pre name="test">
     *  TunnetilaID tid1 = new TunnetilaID();
     *  tid1.vastaaID(1);
     *  tid1.palautaTunnetila() === "Masennus";
     *  tid1.vastaaID(2);
     *  tid1.palautaTunnetila() === "Suru";
     * </pre>
	 */
	public void vastaaID(int nro) {
		tunnusNro = nro;
		tunnetila = tunnetilat[nro - 1];
	}
	
	/**
	 * Palauttaa tietyn ID:n tunnetilan
	 * Käytetään, kun halutaan lisätä tunnetila päiväkirjaan valittavaksi
	 * @return ID:n tunnetila
	 * @example
     * <pre name="test">
     *  TunnetilaID tid = new TunnetilaID();
     *  tid.vastaaID(3);
     *  tid.palautaTunnetila() === "Ilo";
     *  tid.vastaaID(4);
     *  tid.palautaTunnetila() === "Ahdistus";
     * </pre>
	 */
	public String palautaTunnetila() {
		return tunnetila;
	}
	
	/**
	 * Testimetodi tunnetilan palattamiselle
	 * @param nro Tunnetilan numero
	 * @return tunnetila
	 * @example
     * <pre name="test">
     *  TunnetilaID tid = new TunnetilaID();
     *  tid.vastaaID(1);
     *  tid.palautaTunnetilaTesti(tid.getTunnusNro()) === "Masennus";
     *  tid.vastaaID(2);
     *  tid.palautaTunnetilaTesti(tid.getTunnusNro()) === "Suru";
     * </pre>
	 */
	public String palautaTunnetilaTesti(int nro) {
	    int numero = nro;
		tunnusNro = numero;
		if (nro > 7) numero = 1;
		tunnetila = tunnetilat[numero - 1];
		return tunnetila;
	}
	
	/**
	 * Palautetaan tietyn tunnetilan ID
	 * @param tunne Tunnetila
	 * @return Tunnetilan ID
	 * @example
     * <pre name="test">
     *  TunnetilaID tid = new TunnetilaID();
     *  tid.palautaID("Masennus") === 0;
     *  tid.palautaID("Ahdistus") === 3;
     * </pre>
	 */
	public int palautaID(String tunne) {
		int id = 0;
		for (int i = 1; i < tunnetilat.length; i++) {
			if (tunne.equals(tunnetilat[i])) id = i;
		}
		return id;
	}
	
	/**
	 * Asetetaan ID:lle tunnetila
	 * @param s Asetettava tunne
	 * @return null
	 * @example
     * <pre name="test">
     * TunnetilaID tid = new TunnetilaID();
     * tid.setTunne("Masennus");
     * tid.palautaTunnetila() === "Masennus";
     * </pre>
	 */
	public String setTunne(String s) {
		tunnetila = s;
		return null;
	}
	
	
	/**
	 * Tulostetaan Tunnetilan ID & tunnetila
	 * @param out tietovirta johon tulostetaan
	 */
	public void tulosta(PrintStream out) {
		out.println(tunnusNro + " " + tunnetila);

	}
	
	/**
	 * Tulostetaan tunnetilan tiedot
	 * @param os tietovirta johon tulostetaan
	 */
	public void tulosta(OutputStream os) {
		tulosta(new PrintStream(os));
	}
	
	/**
	 * Antaa tunnetilalle seuraavan tunnusnumeron
	 * @return tunnetilan uusi ID
	 * @example
	 * <pre name="test">
	 * TunnetilaID id1 = new TunnetilaID();
	 * id1.getTunnusNro() === 0;
	 * id1.rekisteroi();
	 * TunnetilaID id2 = new TunnetilaID();
	 * id2.rekisteroi();
	 * int n1 = id1.getTunnusNro();
	 * int n2 = id2.getTunnusNro();
	 * n1 === n2-1;
	 * </pre>
	 */
	public int rekisteroi() {
		tunnusNro = seuraavaNro;
		seuraavaNro++;
		return tunnusNro;
	}
	
	/**
	 * Palautetaan Tunnetilan ID
	 * @return Tunnetilan ID
	 * @example
	 * <pre name="test">
	 * TunnetilaID id1 = new TunnetilaID();
	 * id1.getTunnusNro() === 0;
	 * id1.rekisteroi();
	 * TunnetilaID id2 = new TunnetilaID();
	 * id2.rekisteroi();
	 * int n1 = id1.getTunnusNro();
	 * int n2 = id2.getTunnusNro();
	 * n1 === n2-1;
	 * </pre>
	 */
	public int getTunnusNro() {
		return tunnusNro;
	}
	
	/**
	 * Asetetaan Tunnetilalle ID
	 * @param nr ID
	 * @example
     * <pre name="test">
     * TunnetilaID tid = new TunnetilaID();
     * tid.setTunnusNro(1);
     * tid.getTunnusNro() === 1;
     * </pre>
	 */
    public void setTunnusNro(int nr) {
        tunnusNro = nr;
        if ( tunnusNro >= seuraavaNro ) seuraavaNro = tunnusNro + 1;
    }
    
    /**
     * Palauttaa tunnetilaID:n merkkijonona tiedostoa varten
     * @return TunnetilaID merkkijonona
     * @example
     * <pre name="test">
     *   TunnetilaID tid = new TunnetilaID();
     *   tid.parse("   2  |  Suru");
     *   tid.toString().startsWith("2|Suru") === true; 
     * </pre>  
     */
    @Override
    public String toString() {
        return "" + getTunnusNro() + "|" + tunnetila;
    }
    
    /**
     * Selvittää tunnetilaID:n tiedot
     * @param rivi luettava rivi josta tiedot otetaan
     * @example
     * <pre name="test">
     *   TunnetilaID tid = new TunnetilaID();
     *   tid.parse("  2  |  Suru");
     *   tid.getTunnusNro() === 2;
     *   tid.toString().startsWith("2|Suru") === true; 
     *
     *   tid.rekisteroi();
     *   int n = tid.getTunnusNro();
     *   tid.parse(""+(n+20));       // Otetaan merkkijonosta vain tunnusnumero
     *   tid.rekisteroi();           // ja tarkistetaan että seuraavalla kertaa tulee yhtä isompi
     *   tid.getTunnusNro() === n+20+1;
     *     
     * </pre>
     */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        setTunnusNro(Mjonot.erota(sb, '|', getTunnusNro()));
        tunnetila = Mjonot.erota(sb, '|', tunnetila);
    }

    /**
     * Vertailumetodi
     */
    @Override
    public boolean equals(Object obj) {
        if ( obj == null ) return false;
        return this.toString().equals(obj.toString());
    }
    
    /**
     * Apumetodi
     */
    @Override
    public int hashCode() {
        return tunnusNro;
    }
	
	/**
	 * Testiohjelma TunnetilaID:lle
	 * @param args Ei käytössä
	 * @throws SailoException Jos ongelmia
	 */
	public static void main(String[] args) throws SailoException {
		TunnetilatID alkiot = new TunnetilatID();
		TunnetilaID tid1 = new TunnetilaID();
		tid1.vastaaID(1);
		alkiot.lisaa(tid1);
		TunnetilaID tid2 = new TunnetilaID();
		tid2.vastaaID(2);
		alkiot.lisaa(tid2);
		TunnetilaID tid3 = new TunnetilaID();
		tid3.vastaaID(3);
		alkiot.lisaa(tid3);
		
		List<TunnetilaID> idt2 = alkiot.annaTunnetilatID(1);
		 
		for (TunnetilaID tid : idt2) {
			tid.tulosta(System.out);
		}
	}
}
