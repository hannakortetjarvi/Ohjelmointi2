package tunnepaivakirja;

import java.io.*;

import fi.jyu.mit.ohj2.Mjonot;
import static kanta.SatunnaisiaArvoja.*;

/**
 * Tunnetila joka osaa mm. huolehtia voimakkuudestaan
 * @author Hanna Kortetjärvi
 * @Version 1.0 22.3.2019
 */
public class Tunnetila {
	private int tunnusNro;
	private int pvmNro;
	private int tunneNro;
	public int voimakkuus;
	
	private static int seuraavaNro = 1;
	
	/**
	 * Alustetaan Tunnetila
	 */
	public Tunnetila() {
		// Ei tee vielä mitään
	}
	
	/**
	 * Alustetaan tietyn merkinnän tunnetila
	 * @param pvmNro päivämäärän numero
	 */
	public Tunnetila(int pvmNro) {
		this.pvmNro = pvmNro;
	}
	
	/**
	 * Apumetodi, jolla saadaan täytettyä testiarvot Tunnetilalle
	 * @param tnro tunteen numero
	 * @param pvm päivämäärän numero
	 * @param voim tunteen voimakkuus
	 * @example
	 * <pre name="test">
	 * Tunnetila tun = new Tunnetila();
	 * Tunnetila tun2 = new Tunnetila();
	 * tun2.vastaaTunnetila(1,1,7);
	 * tun.vastaaTunnetila(3,1,4);
	 * tun.getpvmNro() === 1;
	 * tun2.getpvmNro() === 1;
	 * </pre>
	 */
	public void vastaaTunnetila(int tnro, int pvm, int voim) {
		tunneNro = tnro;
		pvmNro = pvm;
		voimakkuus = voim;
	}
	
	/**
	 * Asettaa tunnetilalle arvot, voimakkuus String muodossa
	 * @param tnro tunnenro
	 * @param pvm pvmnro
	 * @param voim voimakkuus
	 * @example
	 * <pre name="test">
	 * Tunnetila tunne = new Tunnetila();
	 * tunne.vastaaTunnetila(1,3,"7");
	 * tunne.voimakkuus === 7;
	 * </pre>
	 */
	public void vastaaTunnetila(int tnro, int pvm, String voim) {
		tunneNro = tnro;
		pvmNro = pvm;
		if (voim.equals("")) voimakkuus = 0;
		else voimakkuus = Integer.parseInt(voim);
	}
	
	
	/**
	 * Hakee tunnetilan merkintään
	 * @param tnro Tunteen ID
	 * @param pvm Päivämäärän ID
	 * @param voim Tunteen voimakkuus (arvotaan)
	 * @return Tunteen tiedot
	 * @throws SailoException Jos ongelma
	 */
	public String getTunnetilaMerkintaan(int tnro, int pvm, int voim) throws SailoException {
		Tunnetila tunne = new Tunnetila();
		Tunnetilat tunteet = new Tunnetilat();
		tunne.vastaaTunnetila(tnro, pvm, voim);
		if (tnro == 0 || pvm == 0) return "";
		String tila = tunteet.getTunne(tnro);
		String voima = Integer.toString(tunne.voimakkuus);
		String tunnetila = tila + " (" + voima + ")";
		return tunnetila;
	}
	
	/**
	 * Tulostetaan merkinnän tunnetilan tiedot
	 * @param out Tietovirta johon tulostetaan
	 * @throws SailoException Jos ongelma
	 */
	public void tulosta(PrintStream out) throws SailoException {
		Tunnetilat tunteet = new Tunnetilat();
		String tunnetila = tunteet.getTunne(tunneNro);
		out.println(tunnetila + " (" + voimakkuus + ")");
	}
	
	/**
	 * Hakee String muodossa merkintään tunnetilan
	 * @return tunnetila
	 * @throws SailoException Jos ongelma
	 */
	public String haeMerkintaan() throws SailoException {
		Tunnetilat tunteet = new Tunnetilat();
		String tunnetila = tunteet.getTunne(tunneNro);
		return tunnetila + " (" + voimakkuus + ")";
	}
	
	/**
	 * 
	 * Tulostetaan merkinnän tiedot
	 * @param os tietovirta johon tulostetaan
	 * @throws SailoException Jos ongelma
	 */
	public void tulosta(OutputStream os) throws SailoException {
		tulosta(new PrintStream(os));
	}
	
	/**
	 * Antaa tunnetilalle seuraavan tunnusnumeron
	 * @return Tunnetilan uusi tunnusnumero
	 * @example
	 * <pre name="test">
	 * Tunnetila tunne1 = new Tunnetila();
	 * tunne1.getTunnusNro() === 0;
	 * tunne1.rekisteroi();
	 * Tunnetila tunne2 = new Tunnetila();
	 * tunne2.rekisteroi();
	 * int n1 = tunne1.getTunnusNro();
	 * int n2 = tunne2.getTunnusNro();
	 * n1 === n2-1;
	 * </pre>
	 */
	public int rekisteroi() {
		tunnusNro = seuraavaNro;
		seuraavaNro++;
		return tunnusNro;
	}
	
	/**
	 * Palautetaan tunnetilan ID
	 * @return tunnetilan ID
	 * @example
	 * <pre name="test">
	 * Tunnetila tunne = new Tunnetila();
	 * tunne.getTunnusNro() === 0;
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
	 * Tunnetila tunne = new Tunnetila();
	 * tunne.setTunnusNro(4);
	 * tunne.getTunnusNro() === 4;
	 * </pre>
	 */
    public void setTunnusNro(int nr) {
        tunnusNro = nr;
        if ( tunnusNro >= seuraavaNro ) seuraavaNro = tunnusNro + 1;
    }
	
	/**
	 * Palautetaan mihin päivämäärään tunnetila kuuluu
	 * @return Merkinnän ID
	 * @example
	 * <pre name="test">
	 * Tunnetila tunne1 = new Tunnetila();
	 * tunne1.vastaaTunnetila(2,3,4);
	 * Tunnetila tunne2 = new Tunnetila();
	 * tunne2.vastaaTunnetila(3,4,5);
	 * tunne1.getpvmNro() === 3;
	 * tunne2.getpvmNro() === 4;
	 * </pre>
	 */
	public int getpvmNro() {
		return pvmNro;
	}
	
	/**
	 * Palautetaan tunnetilaa vastaava voimakkuus
	 * @return Tunnetilan voimakkuus
	 * @example
	 * <pre name="test">
	 * Tunnetila tunne1 = new Tunnetila();
	 * tunne1.vastaaTunnetila(2,3,4);
	 * Tunnetila tunne2 = new Tunnetila();
	 * tunne2.vastaaTunnetila(3,4,5);
	 * tunne1.getVoimakkuus() === "4";
	 * tunne2.getVoimakkuus() === "5";
	 * </pre>
	 */
	public String getVoimakkuus() {
		String voima = Integer.toString(voimakkuus);
		return voima;
	}
	
	/**
	 * Palautetaan tunnetilaan asetettu ID
	 * @return ID
	 */
	public int getTunneID() {
	    return tunneNro;
	}
	
	/**
	 * Tehdään tunnetilasta merkkijono
     * @example
     * <pre name="test">
     *   Tunnetila tunne = new Tunnetila();
     *   tunne.parse("   1  |  1   | 7");
     *   tunne.toString().startsWith("1|1|7|") === true; // on enemmäkin kuin 3 kenttää, siksi loppu |
     * </pre>  
     */
    @Override
    public String toString() {
        return "" + getTunnusNro() + "|" + pvmNro + "|" + tunneNro + "|" + voimakkuus;
    }
    
    /**
     * Erotetaan merkkijono osiksi
     * @param rivi Erotettava merkkijono
     * @example
     * <pre name="test">
     *   Tunnetila tunne = new Tunnetila();
     *   tunne.parse("   1  |  1   | 7");
     *   tunne.getTunnusNro() === 1;
     *   tunne.toString().startsWith("1|1|7|") === true; // on enemmäkin kuin 3 kenttää, siksi loppu |
     *
     *   tunne.rekisteroi();
     *   int n = tunne.getTunnusNro();
     *   tunne.parse(""+(n+20));       // Otetaan merkkijonosta vain tunnusnumero
     *   tunne.rekisteroi();           // ja tarkistetaan että seuraavalla kertaa tulee yhtä isompi
     *   tunne.getTunnusNro() === n+20+1; 
     * </pre>
     */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        setTunnusNro(Mjonot.erota(sb, '|', getTunnusNro()));
        pvmNro = Mjonot.erota(sb, '|', pvmNro);
        tunneNro = Mjonot.erota(sb, '|', tunneNro);
        voimakkuus = Mjonot.erota(sb, '|', voimakkuus);
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
	 * Testiohjelma Tunnetilalle
	 * @param args Ei käytössä
	 * @throws SailoException Jos ongelma
	 */
	public static void main(String[] args) throws SailoException {
		int voima = 1;
		Tunnetila tun = new Tunnetila();
		tun.vastaaTunnetila(1, 1, voima);
		tun.tulosta(System.out);
	}
}
