package tunnepaivakirja;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Tunnepäiväkirjan tunnetilatID, joka osaa mm. lisätä uuden tunnetilan käytettäväksi päiväkirjaan
 * @author Hanna Kortetjärvi
 * @version 1.0, 21.3.2019
 */
public class TunnetilatID implements Iterable<TunnetilaID> {

    private boolean muutettu = false;
    private String tiedostonPerusNimi = "tunteetID";
    private int lkm = 0;
    private String kokoNimi = "";
    private static final int MAX_TUNTEITA = 10; 
    
	/** Taulukko tunnetiloista */
	private static TunnetilaID alkiot[] = new TunnetilaID[0];
	
	/**
	 * Oletusmuodostaja
	 */
	public TunnetilatID() {
		// 
	}
	
	/**
	 * Lisätään uusi tunnetilan tietorakenteeseen.
	 * @param tid Lisättävä tunnetila
     * @example
     * <pre name="test">
     * TunnetilatID tunteet = new TunnetilatID();
     * TunnetilaID tid = new TunnetilaID();
     * tunteet.lisaa(tid);
	 * </pre>
	 */
	public void lisaa(TunnetilaID tid) {
		if (lkm >= alkiot.length) {
			TunnetilatID.alkiot  = Arrays.copyOf(alkiot, alkiot.length + MAX_TUNTEITA);
		}
		alkiot[lkm] = tid;
		lkm++;
		muutettu = true;
	}
	
	/**
	 * Antaa tunnetilaID:n annetulla indeksillä
	 * @param i indeksi
	 * @return löydetyt
	 * @throws IndexOutOfBoundsException jos indeksi virheellinen
     * @example
     * <pre name="test">
     * #THROWS IndexOutOfBoundsException, SailoException
     * TunnetilaID tidtest = new TunnetilaID();
     * TunnetilatID tidttest = new TunnetilatID();
     * tidttest.lisaa(tidtest);
     * tidttest.anna(0) === tidtest;
     * </pre>
	 */
    public TunnetilaID anna(int i) throws IndexOutOfBoundsException {
        if ( i < 0 || lkm <= i ) throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        return alkiot[i];
    }
    
	/**
	 * Hakee tunnetilan ID:n perusteella
	 * @param ID Tunnetilan ID
	 * @return tunnetila
	 */
	public String getTunnetila(int ID) {
		TunnetilaID tid = new TunnetilaID();
		for (int i = 0; i < alkiot.length; i++) {
		    if (alkiot[i] == null) break;
		    if (alkiot[i].getTunnusNro() == ID) tid = alkiot[i];
		}
		return tid.tunnetila;
	}
	
	/**
	 * Testimetodi tunnetilan palauttamiselle
	 * @param ID id
	 * @return tunnetila
     * @example
     * <pre name="test">
     * TunnetilaID tid = new TunnetilaID();
     * tid.palautaTunnetilaTesti(1) === "Masennus";
     * </pre>
	 */
	public String getTunnetilaTesti(int ID) {
		TunnetilaID tid = new TunnetilaID();
		String tunne = tid.palautaTunnetilaTesti(ID);
		return tunne;
	}
	
	/**
	 * Poistometodi
	 * @param id Poistettavan id
	 * @return 1 jos poisto onnistuu
     * @example
     * <pre name="test">
     * #import java.io.File;
     *  TunnetilatID tunteet = new TunnetilatID();
     *  TunnetilaID tunne1 = new TunnetilaID(); tunne1.setTunnusNro(1);
     *   TunnetilaID tunne2 = new TunnetilaID(); tunne2.setTunnusNro(2);
     *  tunteet.lisaa(tunne1);
     *  tunteet.lisaa(tunne2);
     *  tunteet.poista(tunne1.getTunnusNro()) === 1 ; tunteet.getLkm() === 1;
     *  </pre>
	 */
    public int poista(int id) { 
        int ind = etsiId(id); 
        if (ind < 0) return 0; 
        lkm--; 
        for (int i = ind; i < lkm; i++) 
            alkiot[i] = alkiot[i + 1]; 
        alkiot[lkm] = null; 
        muutettu = true; 
        return 1; 
    } 
	
    /**
     * Etsitään tunnetilan id
     * @param id Etsittävä id
     * @return -1 jos ei löydy
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * TunnetilatID tunteet = new TunnetilatID();
     * TunnetilaID tun = new TunnetilaID();
     * tun.setTunnusNro(1);
     * tunteet.lisaa(tun);
     * tunteet.etsiId(0) === -1;
     * tunteet.etsiId(1) === 0;
     * </pre>
     */
	public int etsiId(int id) {
		for (int i = 0; i < lkm; i++) {
			if (id == alkiot[i].getTunnusNro()) return i;
		}
		return -1;
	}
	
	/**
	 * Etsitään kaikki tunnetilat alkioista
	 * @return löytyneet tunnetilat Collectionina
     * @example
     * <pre name="test">
     * TunnetilaID tidtest = new TunnetilaID();
     * TunnetilatID tidttest = new TunnetilatID();
     * tidttest.lisaa(tidtest);
     * tidttest.etsi().size() === 1;
     * </pre>
	 */
    public Collection<TunnetilaID> etsi() { 
        Collection<TunnetilaID> loytyneet = new ArrayList<TunnetilaID>(); 
        for (TunnetilaID tid : alkiot) { 
        	if (tid == null) break;
            loytyneet.add(tid);  
        } 
        return loytyneet; 
    }
    
    
    /**
     * Etsii tunnetilat ehdon perusteella
     * @param ehto hakuehto
     * @return löydetyt tunnetilaID:t
     * @example
     * <pre name="test">
     * TunnetilaID tidtest = new TunnetilaID();
     * TunnetilaID tidtest2 = new TunnetilaID();
     * TunnetilatID tidttest = new TunnetilatID();
     * tidtest.vastaaID(1);
     * tidtest2.vastaaID(2);
     * tidttest.lisaa(tidtest);
     * tidttest.lisaa(tidtest2);
     * tidttest.etsi("Masennus") === tidtest;
     * tidttest.etsi("Suru") === tidtest2;
     * </pre>
     */
    public TunnetilaID etsi(String ehto) {
    	TunnetilaID loydetty = new TunnetilaID();
    	for (TunnetilaID tid : alkiot) {
    		if (ehto.equals(tid.palautaTunnetila())) {
    			loydetty = tid;
    			break;
    		}
    	}
    	return loydetty;
    }
	
    /**
     * Lukee TunnetilatID tiedostosta. 
     * @param tied tiedoston perusnimi
     * @throws SailoException jos lukeminen epäonnistuu
     * 
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.File;
     * 
     *  TunnetilatID tidt = new TunnetilatID();
     *  TunnetilaID tid1 = new TunnetilaID(), tid2 = new TunnetilaID();
     *  tid1.vastaaID(1);
     *  tid2.vastaaID(2);
     *  String hakemisto = "testi";
     *  String tiedNimi = hakemisto+"/nimet";
     *  File ftied = new File(tiedNimi+".dat");
     *  File dir = new File(hakemisto);
     *  dir.mkdir();
     *  ftied.delete();
     *  tidt.lueTiedostosta(tiedNimi); #THROWS SailoException
     *  tidt.lisaa(tid1);
     *  tidt.lisaa(tid2);
     *  tidt.tallenna();
     *  tidt = new TunnetilatID();            // Poistetaan vanhat luomalla uusi
     *  tidt.lueTiedostosta(tiedNimi);  // johon ladataan tiedot tiedostosta.
     *  Iterator<TunnetilaID> i = tidt.iterator();
     *  i.next() === tid1;
     *  i.next() === tid2;
     *  i.hasNext() === false;
     *  tidt.lisaa(tid2);
     *  tidt.tallenna();
     *  ftied.delete() === true;
     *  File fbak = new File(tiedNimi+".bak");
     *  fbak.delete() === true;
     * </pre>
     */
    public void lueTiedostosta(String tied) throws SailoException {
        setTiedostonPerusNimi(tied);
        try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {
        	String rivi;
            while ( (rivi = fi.readLine()) != null ) {
                rivi = rivi.trim();
                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                TunnetilaID tid = new TunnetilaID();
                tid.parse(rivi); // voisi olla virhekäsittely
                lisaa(tid);
            }
            muutettu = false;

        } catch ( FileNotFoundException e ) {
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
        } catch ( IOException e ) {
            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
        }
    }

    
    /**
     * Luetaan aikaisemmin annetun nimisestä tiedostosta
     * @throws SailoException jos tulee poikkeus
     */
    public void lueTiedostosta() throws SailoException {
        lueTiedostosta(getTiedostonPerusNimi());
    }
    
    
    /**
     * Tallentaa TunnetilaID:t tiedostoon.
     * @throws SailoException jos talletus epäonnistuu
     */
    public void tallenna() throws SailoException {
        if ( !muutettu ) return;

        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete(); //  if ... System.err.println("Ei voi tuhota");
        ftied.renameTo(fbak); //  if ... System.err.println("Ei voi nimetä");

        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
        	for (TunnetilaID tid : this) {
            	fo.println(tid.toString());
            }
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }

        muutettu = false;
    }
    
    /**
     * Palauttaa tiedoston kokonimen
     * @return Kokonimi
     */
    public String getKokoNimi() {
    	return kokoNimi;
    }
	
	/**
	 * Palauttaa päiväkirjan tunnetilojen lukumäärän
	 * @return lukumäärä
	 */
	 public int getLkm() {
		 return lkm;
	 }
	 
	 /**
	  * Palauttaa TunnetilatID iteraattorin
	  */
	 @Override
	 public Iterator<TunnetilaID> iterator() {
		 return new TidtIterator();
	 }
	 
	    /**
	     * Luokka TunnetilatID:n iteroimiseksi.
	     * @example
	     * <pre name="test">
	     * #THROWS SailoException 
	     * #PACKAGEIMPORT
	     * #import java.util.*;
	     * 
	     * TunnetilatID tidt = new TunnetilatID();
	     * TunnetilaID tid1 = new TunnetilaID(), tid2 = new TunnetilaID();
	     * tid1.rekisteroi(); tid2.rekisteroi();
	     *
	     * tidt.lisaa(tid1); 
	     * tidt.lisaa(tid2); 
	     * tidt.lisaa(tid1); 
	     * 
	     * StringBuffer ids = new StringBuffer(30);
	     * for (TunnetilaID tid :tidt)   // Kokeillaan for-silmukan toimintaa
	     *   ids.append(" "+tid.getTunnusNro());           
	     * 
	     * String tulos = " " + tid1.getTunnusNro() + " " + tid2.getTunnusNro() + " " + tid1.getTunnusNro();
	     * 
	     * ids.toString() === tulos; 
	     * 
	     * ids = new StringBuffer(30);
	     * for (Iterator<TunnetilaID>  i=tidt.iterator(); i.hasNext(); ) { // ja iteraattorin toimintaa
	     *   TunnetilaID tid = i.next();
	     *   ids.append(" "+tid.getTunnusNro());           
	     * }
	     * 
	     * ids.toString() === tulos;
	     * 
	     * Iterator<TunnetilaID>  i=tidt.iterator();
	     * i.next() == tid1  === true;
	     * i.next() == tid2  === true;
	     * i.next() == tid1  === true;
	     * 
	     * i.next();  #THROWS NoSuchElementException
	     *  
	     * </pre>
	     */
	    public class TidtIterator implements Iterator<TunnetilaID> {
	        private int kohdalla = 0;


	        /**
	         * Onko olemassa vielä seuraavaa TunnetilaID:tä
	         * @see java.util.Iterator#hasNext()
	         * @return true jos on vielä jäseniä
	         */
	        @Override
	        public boolean hasNext() {
	            return kohdalla < getLkm();
	        }


	        /**
	         * Annetaan seuraava tid
	         * @return seuraava tid
	         * @throws NoSuchElementException jos seuraava alkiota ei enää ole
	         * @see java.util.Iterator#next()
	         */
	        @Override
	        public TunnetilaID next() throws NoSuchElementException {
	            if ( !hasNext() ) throw new NoSuchElementException("Ei oo");
	            return anna(kohdalla++);
	        }


	        /**
	         * Tuhoamista ei ole toteutettu
	         * @throws UnsupportedOperationException aina
	         * @see java.util.Iterator#remove()
	         */
	        @Override
	        public void remove() throws UnsupportedOperationException {
	            throw new UnsupportedOperationException("Me ei poisteta");
	        }
	    }
	    
	    /**
	     * Asetetaan tiedoston perusnimi
	     * @param tied Asetettava nimi tiedostolle
	     */
	    public void setTiedostonPerusNimi(String tied) {
	        tiedostonPerusNimi = tied;
	    }


	    /**
	     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
	     * @return tallennustiedoston nimi
	     */
	    public String getTiedostonPerusNimi() {
	        return tiedostonPerusNimi;
	    }


	    /**
	     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
	     * @return tallennustiedoston nimi
	     */
	    public String getTiedostonNimi() {
	        return tiedostonPerusNimi + ".dat";
	    }


	    /**
	     * Palauttaa varakopiotiedoston nimen
	     * @return varakopiotiedoston nimi
	     */
	    public String getBakNimi() {
	        return tiedostonPerusNimi + ".bak";
	    }
	 
	 /**
	  * Haetaan kaikki tunnetilatID, joiden ID on tunnusnro
	  * @param tunnusnro Tunnetilan tunnusnumero
	  * @return tietorakenne jossa viitteet löydettyihin tunnetiloihin
	  */
	 public List<TunnetilaID> annaTunnetilatID(int tunnusnro) {
		 List<TunnetilaID> loydetyt = new ArrayList<TunnetilaID>();
		 for (TunnetilaID tid : alkiot) {
			 if (tid == null) break;
			 if (tid.getTunnusNro() == tunnusnro) loydetyt.add(tid);
		 }
		 return loydetyt;
	 }
	 
	 /**
	  * Haetaan kaikki tunnetilatID
	  * @return tietorakenne jossa viitteet löydettyihin tunnetiloihin
	  */
	 public List<TunnetilaID> annaTunnetilatID() {
		 List<TunnetilaID> loydetyt = new ArrayList<TunnetilaID>();
		 for (TunnetilaID tid : alkiot) {
			 loydetyt.add(tid);
		 }
		 return loydetyt;
	 }
	 
	 /**
	  * Testiohjelma tunnetilojen ID:ille;
	  * @param args ei käytössä
	 * @throws SailoException jos ongelma
	  */
	 public static void main(String[] args) throws SailoException {
		 TunnetilatID idt = new TunnetilatID();
		 TunnetilaID id1 = new TunnetilaID();
		 id1.vastaaID(1);
		 TunnetilaID id2 = new TunnetilaID();
		 id2.vastaaID(2);
		 TunnetilaID id3 = new TunnetilaID();
		 id3.vastaaID(3);
		 
		 idt.lisaa(id1);
		 idt.lisaa(id2);
		 idt.lisaa(id3);
		 
		 System.out.println("============ TunnetilatID testi ============");
		
		List<TunnetilaID> idt2 = idt.annaTunnetilatID();
		for (TunnetilaID tid : idt2) {
			tid.tulosta(System.out);
		}
	 }

}
