package tunnepaivakirja;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * Tunnepaivakirja-luokka, joka huolehtii merkistöstä. Pääosin kaikki metodit ovat vain "välittäjämetodeja" merkistään.
 * @author Hanna Kortetjärvi
 * @version 1.0, 1.3.2019
  * Testien alustus
 * @example
 * <pre name="testJAVA">
 * #import tunnepaivakirja.SailoException;
 *  private Tunnepaivakirja tunnepaivakirja;
 *  private Merkinta pvm1;
 *  private Merkinta pvm2;
 *  private int jid1;
 *  private int jid2;
 *  private Tunnetila tunne1;
 *  private Tunnetila tunne2;
 *  private Tunnetila tunne3; 
 *  private Tunnetila tunne4; 
 *  public void alustaKirja() {
 *    tunnepaivakirja = new Tunnepaivakirja();
 *    pvm1 = new Merkinta(); pvm1.vastaaEnsimmainen(); pvm1.rekisteroi();
 *    pvm2 = new Merkinta(); pvm2.vastaaEnsimmainen(); pvm2.rekisteroi();
 *    jid1 = pvm1.getTunnusNro();
 *    jid2 = pvm2.getTunnusNro();
 *    try {
 *    tunnepaivakirja.lisaa(pvm1);
 *    tunnepaivakirja.lisaa(pvm2);
 *    } catch ( Exception e) {
 *       System.err.println(e.getMessage());
 *    }
 *  }
 * </pre>
 */
public class Tunnepaivakirja {
    private Merkinnat merkinnat = new Merkinnat();
    private static TunnetilatID tidt = new TunnetilatID();
    private static Tunnetilat tunteita = new Tunnetilat();
    
    /**
     * Palautaa tunnepäiväkirjan merkintämäärän
     * @return merkintöjen määrä
     * @example 
     * <pre name="test">
     * alustaKirja();
     * tunnepaivakirja.getMerkintoja() === 2;
     * </pre>
     */
    public int getMerkintoja() {
        return merkinnat.getLkm();
    }
    
    /**
     * Etsitään hakuehtoon vastaavat merkinnät
     * @param hakuehto Hakuehto
     * @param k etsittävän kentän indeksi
     * @return löytyneet merkinnät
     * @throws SailoException Jos jokin menee väärin
     * @example 
     * <pre name="test">
     *   #THROWS CloneNotSupportedException, SailoException
     *   alustaKirja();
     *   Merkinta merkinta3 = new Merkinta(); merkinta3.rekisteroi();
     *   merkinta3.setPvm("12.10.2018");
     *   tunnepaivakirja.lisaa(merkinta3);
     *   Collection<Merkinta> loytyneet = tunnepaivakirja.etsi("*12*",0);
     *   loytyneet.size() === 1;
     *   Iterator<Merkinta> it = loytyneet.iterator();
     *   it.next() == merkinta3 === true; 
     * </pre>
     */ 
	public Collection<Merkinta> etsi(String hakuehto, int k) throws SailoException {
		return merkinnat.etsi(hakuehto,k);
	}
	
	/**
	 * Etsii kaikki merkinnät päiväkirjaan
	 * @return merkinnät
	 * @throws SailoException jos ongelma
     * @example 
     * <pre name="test">
     *   #THROWS CloneNotSupportedException, SailoException
     *   alustaKirja();
     *   Merkinta merkinta3 = new Merkinta(); merkinta3.rekisteroi();
     *   merkinta3.setPvm("12.10.2018");
     *   tunnepaivakirja.lisaa(merkinta3);
     *   Collection<Merkinta> loytyneet = tunnepaivakirja.etsi("*12*",0);
     *   Iterator<Merkinta> it = loytyneet.iterator();
     *   it.next() == merkinta3 === true; 
     * </pre>
	 */
	public Collection<Merkinta> etsi() throws SailoException {
		return merkinnat.etsiKaikki();
	}
	
	
    /**
     * Etsitään hakuehtoon vastaavat TunnetilaID:t
     * @return Löytyneet merkinnät
     * @throws SailoException Jos jokin menee väärin
     */
	public Collection<TunnetilaID> etsitunne() throws SailoException {
		return tidt.etsi();
	}
	
	/**
	 * Etsii annetulla ehdolla olevat tunnetilaID:t
	 * @param ehto ehto String muodossa
	 * @return löydetyt id:t
     * @example 
     * <pre name="test">
     * #THROWS SailoException
     * alustaKirja();
     * TunnetilaID tid1 = new TunnetilaID();
     * tid1.vastaaID(1);
     * TunnetilaID tid2 = new TunnetilaID();
     * tid2.vastaaID(2);
     * TunnetilaID tid3 = new TunnetilaID();
     * tid3.vastaaID(3);
     * tunnepaivakirja.lisaa(tid1);
     * tunnepaivakirja.lisaa(tid2);
     * tunnepaivakirja.lisaa(tid3);
     * Tunnepaivakirja.etsi("Masennus") === tid1;
     * </pre>
	 */
	public static TunnetilaID etsi(String ehto) {
		return tidt.etsi(ehto);
	}
	
	/**
	 * Etsii merkinnän tunnetilat Collection muodossa
	 * @param ehto merkinnän ID
	 * @return löydetyt tunnetilat
     * @example 
     * <pre name="test">
     * #THROWS SailoException
     * Tunnepaivakirja testi2 = new Tunnepaivakirja();
     * Tunnetila tunne = new Tunnetila();
     * tunne.vastaaTunnetila(2,10,7);
     * testi2.lisaa(tunne);
     * testi2.etsi(10).size() === 1;
     * </pre>
	 */
	public Collection<Tunnetila> etsi(int ehto) {
		return tunteita.annaTunteet(ehto);
	}
	
	/**
	 * Hakee kaikki tunteet merkinnöille
	 * @return Tunnetilat
	*/
	public Tunnetilat annaTunteet() {
		return tunteita;
	}
	
	/**
	 * Antaa merkinnän tunteet listamuodossa
	 * @param nro Pvm:n ID
	 * @return tunteet
     * @example 
     * <pre name="test">
     * #THROWS SailoException
     * Tunnepaivakirja testi2 = new Tunnepaivakirja();
     * Tunnetila tunne = new Tunnetila();
     * tunne.vastaaTunnetila(2,11,7);
     * testi2.lisaa(tunne);
     * testi2.annaTunteita(11).size() === 1;
     * </pre>
	 */
	public List<Tunnetila> annaTunteita(int nro) {
		return tunteita.annaTunnetilat(nro);
	}
	
    
    /**
     * Hakee tunnetilan ID:n perusteella
     * @param ID tunnetilan ID
     * @return Tunnetila
     * @throws SailoException jos ongelma
     * @example
     * <pre name="test">
     * #THROWS SailoException  
     * alustaKirja();
     *  tunnepaivakirja.etsi("*",0).size() === 2;
     *  tunnepaivakirja.lisaa(pvm1);
     *  tunnepaivakirja.etsi("*",0).size() === 3;
     * </pre>
     */
    public String getTunnetila(int ID) throws SailoException {
    	TunnetilatID idt = new TunnetilatID();
    	String tunne = idt.getTunnetila(ID);
    	return tunne;
    }
    
    /**
     * Testimetodi tunnetilan hakemiselle
     * @param ID Tunnetilan ID
     * @return tunnetila
     * @example
     * <pre name="test">
     * alustaKirja();
     * tunnepaivakirja.getTunnetilaTesti(1) === "Masennus";
     * </pre>
     */
    public String getTunnetilaTesti(int ID) {
    	TunnetilatID idt = new TunnetilatID();
    	String tunne = idt.getTunnetilaTesti(ID);
    	return tunne;
    }
    
    /**
     * Hakee tunnetilan tiedot merkintään
     * @param tnro Tunteen ID
     * @param pvmnro Päivämäärän ID
     * @param voim Tunteen voimakkuus (arvotaan)
     * @return Tunteen tiedot
     * @throws SailoException Jos ongelma
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * alustaKirja();
     * tunnepaivakirja.getTunnetilaMerkintaan(1,1,7) === "Masennus (7)";
     * </pre>
     */
    public String getTunnetilaMerkintaan(int tnro, int pvmnro, int voim) throws SailoException {
    	Tunnetilat tunteet = new Tunnetilat();
    	String tunnetila = tunteet.getTunnetilaMerkintaan(tnro, pvmnro, voim);
    	return tunnetila;
    }
    
    /**
     * Palauttaa Stringiin sopivan tunteen ID
     * @param tunne Tunnetila
     * @return ID
     * @example
     * <pre name="test">
     * alustaKirja();
     * tunnepaivakirja.palautaID("Masennus") === 1;
     * </pre>
     */
	public int palautaID(String tunne) {
		int id = 1;
		for (int i = 1; i < tidt.getLkm(); i++) {
			if (tunne.equals(tidt.anna(i).tunnetila)) id = tidt.anna(i).getTunnusNro();
		}
		return id;
	}
    
	/**
	 * Korvataan tai lisätään merkintä
	 * @param merkinta Tarkisteltava merkinä
     * @example
     * <pre name="test">
     * #THROWS SailoException  
     *  alustaKirja();
     *  tunnepaivakirja.etsi("*",0).size() === 2;
     *  tunnepaivakirja.korvaaTaiLisaa(pvm1);
     *  tunnepaivakirja.etsi("*",0).size() === 2;
     * </pre>
	 */
	public void korvaaTaiLisaa(Merkinta merkinta) {
		merkinnat.korvaaTaiLisaa(merkinta);
	}
	
	/**
	 * Korvataan tunnetila, jos muutos tapahtuu
	 * @param tun uusi tunnetila
     * @example
     * <pre name="test">
     * #THROWS SailoException,CloneNotSupportedException
     * #PACKAGEIMPORT
     * alustaKirja();
     * Tunnetilat tunnetilat = new Tunnetilat();
     * Tunnetila tun1 = new Tunnetila(), tun2 = new Tunnetila();
     * tun1.setTunnusNro(4);
     * tun2.setTunnusNro(2);
     * tunnetilat.getLkm() === 0;
     * tunnetilat.korvaa(tun1); tunnetilat.getLkm() === 1;
     * tunnetilat.korvaa(tun2); tunnetilat.getLkm() === 2;
     * Tunnetila tun3 = new Tunnetila();
     * tun3.vastaaTunnetila(1,1,7);
     * tun3.setTunnusNro(4);
     * Iterator<Tunnetila> i2=tunnetilat.iterator();
     * i2.next() === tun1;
     * tunnepaivakirja.korvaa(tun3); tunnetilat.getLkm() === 2;
     * i2=tunnetilat.iterator();
     * Tunnetila h = i2.next();
     * h === tun3;
     * h == tun1 === false;
     * </pre>
	 */
	public void korvaa(Tunnetila tun) {
		tunteita.korvaa(tun);
	}

	/**
	 * Lisää uuden merkinnän tunnepäiväkirjaan.
	 * @param merkinta lisättävä merkintä.
	 * @throws SailoException Jos lisäystä ei voi tehdä
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * #import java.util.*;
     * Tunnepaivakirja kirja = new Tunnepaivakirja();
     * Merkinta pvm1 = new Merkinta(), pvm2 = new Merkinta();
     * kirja.lisaa(pvm1); 
     * kirja.lisaa(pvm2); 
     * kirja.lisaa(pvm1);
     * Collection<Merkinta> loytyneet = kirja.etsi("",-1); 
     * Iterator<Merkinta> it = loytyneet.iterator();
     * it.next() === pvm1;
     * it.next() === pvm2;
     * it.next() === pvm1;
     * </pre>
     */
    public void lisaa(Merkinta merkinta) throws SailoException {
        merkinnat.lisaa(merkinta);
    }
    
    /**
     * Lisätään tunnetila tunnepäiväkirjaan
     * @param tunne Lisättävä tunne
     * @throws SailoException Jos ei voi tehdä
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * #import java.util.*;
     * alustaKirja();
     * Tunnetila tun1 = new Tunnetila();
     * tun1.vastaaTunnetila(1, 1, 7);
     * tun1.setTunnusNro(1);
     * Tunnetila tun2 = new Tunnetila();
     * tunnepaivakirja.lisaa(tun1); 
     * tunnepaivakirja.lisaa(tun2); 
     * tunnepaivakirja.lisaa(tun1);
     * </pre>
	*/
    public void lisaa(Tunnetila tunne) throws SailoException {
    	tunteita.lisaa(tunne);
    }
    
    /**
     * Lisää uuden tunnetilan tunnepäiväkirjaan, jota voidaan käyttää merkintöjen kirjoittamisessa
     * Näin päiväkirjan omistaja voi itse valita haluamansa tunnetilat päiväkirjaan
     * @param tid Lisättävän tunnetilan id
     * @throws SailoException Jos ei voi tehdä
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * #import java.util.*;
     * Tunnepaivakirja kirja = new Tunnepaivakirja();
     * TunnetilaID tun1 = new TunnetilaID();
     * tun1.setTunnusNro(1);
     * tun1.setTunne("Onni");
     * TunnetilaID tun2 = new TunnetilaID();
     * kirja.lisaa(tun1); 
     * kirja.lisaa(tun2); 
     * kirja.lisaa(tun1);
     * </pre>
     */
    public void lisaa(TunnetilaID tid) throws SailoException {
    	tidt.lisaa(tid);
    }
    
    /**
     * Poistometodi merkinnälle
     * @param merkinta Poistettava merkintä
     * @return Poistetut
     * @example
     * <pre name="test">
     * #THROWS Exception
     *   alustaKirja();
     *   tunnepaivakirja.etsi("*",0).size() === 2;
     *   tunnepaivakirja.poista(pvm1) === 1;
     *   tunnepaivakirja.etsi("*",0).size() === 1;
     * </pre>
     */
    public int poista(Merkinta merkinta) {
        int ret = merkinnat.poista(merkinta.getTunnusNro()); 
        return ret; 
    }
    
    /**
     * Poistometodi ID:lle
     * @param tid Poistettava tunnetilaID
     * @return Poistetut
     * @example
     * <pre name="test">
     * #THROWS Exception
     *   alustaKirja();
     *   Tunnetila tunne = new Tunnetila();
     *   tunnepaivakirja.lisaa(tunne);
     *   Tunnetilat tunteet = tunnepaivakirja.annaTunteet();
     *   tunteet.getLkm() === 2;
     *   tunnepaivakirja.poista(tunne) === 1;
     *   Tunnetilat tunteet2 = tunnepaivakirja.annaTunteet();
     *   tunteet2.getLkm() === 1;
     * </pre>
     */
	public int poista(TunnetilaID tid) {
		int ret = tidt.poista(tid.getTunnusNro()); 
        return ret; 
	}
	
	/**
	 * Poistometodi tunteelle
	 * @param tunnetila Poistettava tunnetila
	 * @return poistetut
	 */
	public int poista(Tunnetila tunnetila) {
		int ret = tunteita.poista(tunnetila.getTunnusNro());
		return ret;
	}

    /**
     * Palauttaa i:n merkinnän
     * @param i monesko merkintä palautetaan
     * @return viite i:teen merkintään
     * @throws IndexOutOfBoundsException jos i on väärin
     * @example
     * <pre name="test">
     * #THROWS IndexOutOfBoundsException
     * alustaKirja();
     * tunnepaivakirja.annaMerkinta(1) === pvm2;
     * </pre>
     */
    public Merkinta annaMerkinta(int i) throws IndexOutOfBoundsException {
        return merkinnat.anna(i);
    }
    
    /**
     * Lukee tunnepäiväkirjan tiedot tiedostosta
     * @param nimi Päiväkirjan omistaja
     * @throws SailoException jos lukeminen epäonnistuu
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.*;
     * #import java.util.*;
     * 
     *   
     *  String hakemisto = "testi";
     *  File ftied  = new File(hakemisto+"/merkinnat.dat");
     *  ftied.delete();
     *  Tunnepaivakirja tunnepaivakirja5 = new Tunnepaivakirja(); // tiedostoja ei ole, tulee poikkeus 
     *  tunnepaivakirja5.lueTiedostosta(hakemisto); #THROWS SailoException
     *  alustaKirja();
     *  tunnepaivakirja.setTiedosto(hakemisto); // nimi annettava koska uusi poisti sen
     *  tunnepaivakirja.tallenna(); 
     *  Collection<Merkinta> kaikki = tunnepaivakirja.etsi("",-1); 
     *  Iterator<Merkinta> it = kaikki.iterator();
     *  it.next() === pvm1;
     *  it.next() === pvm2;
     *  it.hasNext() === false;
     *  tunnepaivakirja.lisaa(pvm2);
     *  tunnepaivakirja.tallenna();
     *  ftied.delete()  === true;
     *  File fbak = new File(hakemisto+"/merkinnat.bak");
     *  fbak.delete() === true;
     * </pre>
     */
    public void lueTiedostosta(String nimi) throws SailoException {
        merkinnat = new Merkinnat();
        tunteita = new Tunnetilat();
        tidt = new TunnetilatID();
        
        setTiedosto(nimi);
        tidt.lueTiedostosta();
    	merkinnat.lueTiedostosta();
    	tunteita.lueTiedostosta();
    }
    
    /**
     * Asetetaan tiedosto tietylle nimelle
     * @param nimi Haluttu nimi tiedostolle
     */
    public void setTiedosto(String nimi) {
    	File dir = new File(nimi);
    	dir.mkdirs();
    	String hakemistonNimi = "";
    	if (!nimi.isEmpty()) hakemistonNimi = nimi + "/";
    	merkinnat.setTiedostonPerusNimi(hakemistonNimi + "merkinnat");
    	tunteita.setTiedostonPerusNimi(hakemistonNimi + "tunteet");
    	tidt.setTiedostonPerusNimi(hakemistonNimi + "tunteetID");
    }
    
    /**
     * Tallettaa tunnepäiväkirjan tiedot tiedostoon
     * @throws SailoException jos tallettamisessa ongelmia
     */
    public void tallenna() throws SailoException {
        String virhe = "";
        try {
        	merkinnat.tallenna();
        } catch ( SailoException ex ) {
        	virhe = ex.getMessage();
        }
        
        try {
        	tunteita.tallenna();
        } catch ( SailoException ex) {
        	virhe += ex.getMessage();
        }
        
        try {
        	tidt.tallenna();
        } catch (SailoException ex) {
        	virhe += ex.getMessage();
        }
        
        if ( !"".contentEquals(virhe)) throw new SailoException(virhe);
    }
    /**
     * Testiohjelma tunnepäiväkirjasta
     * @param args ei käytässä
     * @throws SailoException Jos virhe
     */
    public static void main(String args[]) throws SailoException {
        Tunnepaivakirja tunnepaivakirja = new Tunnepaivakirja();

        Merkinta pvm1 = new Merkinta(), pvm2 = new Merkinta();
        pvm1.rekisteroi();
        pvm1.vastaaEnsimmainen();
        pvm2.rekisteroi();
        pvm2.vastaaEnsimmainen();
        
        try {
            tunnepaivakirja.lisaa(pvm1);
            tunnepaivakirja.lisaa(pvm2);
        } catch (SailoException ex) {
        	System.out.println(ex.getMessage());
        }
            System.out.println("============= kirjan testi =================");
            for (int i = 0; i < tunnepaivakirja.getMerkintoja(); i++) {
                Merkinta merkinta = tunnepaivakirja.annaMerkinta(i);
                System.out.println("Merkintä paikassa: " + i);
                merkinta.tulosta(System.out);
                Tunnetila tun1 = new Tunnetila();
                Tunnetila tun2 = new Tunnetila();
                tun1.vastaaTunnetila(1, 1, 7);
                tun2.vastaaTunnetila(2, 1, 6);
                tunnepaivakirja.lisaa(tun1);
                tunnepaivakirja.lisaa(tun2);
                tunnepaivakirja.annaTunteita(merkinta.getTunnusNro());
                tun1.tulosta(System.out);
                tun2.tulosta(System.out);
                System.out.println("");
            }
            
    }

}