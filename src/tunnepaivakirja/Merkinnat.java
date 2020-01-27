package tunnepaivakirja;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import fi.jyu.mit.ohj2.WildChars;

/**
 * Tunnepäiväkirjan merkistä joka osaa mm. lisätä uuden merkinnän
 * @author Hanna Kortetjärvi
 * @version 1.0, 22.3.2019
 */
public class Merkinnat implements Iterable<Merkinta>, Cloneable {
	private static final int MAX_MERKINTOJA = 20;
	private boolean muutettu = false;
	private int lkm = 0;
	private String kokoNimi = "";
	private String tiedostonPerusNimi = "merkinnat";
	private static Merkinta[] alkiot = new Merkinta[0];
	
	/**
	 * Oletusmuodostaja
	 */
	public Merkinnat() {
		// Attribuuttien oma alustus riittää
	}
	
	/**
	 * Hakee päiväkirjan kautta arvot merkinnän tunteelle
	 * @param tnro Tunteen ID
	 * @param pvmnro Päivämäärän ID
	 * @param voim Tunteen voimakkuus (arvotaan)
	 * @return Tunteen tiedot
	 * @throws SailoException Jos ongelma
	 */
	public String getTunnetilaMerkintaan(int tnro, int pvmnro, int voim) throws SailoException {
		Tunnepaivakirja kirja = new Tunnepaivakirja();
		String tunnetila = kirja.getTunnetilaMerkintaan(tnro, pvmnro, voim);
		return tunnetila;
	}
	
	/**
	 * Palauttaa tunnetilat ehdon mukaan Collectionina
	 * @param ehto ehto hakemiselle
	 * @return löydetyt tunteet
	 */
	public Collection<Tunnetila> etsi(int ehto) {
		Tunnepaivakirja kirja = new Tunnepaivakirja();
		Collection<Tunnetila> tunteet = kirja.etsi(ehto);
		return tunteet;
	}
	
	/**
	 * Palauttaa tunnetilat ehdon mukaan
	 * @return tunnetilat
	 */
	public Tunnetilat annaTunteet() {
		Tunnepaivakirja kirja = new Tunnepaivakirja();
		return kirja.annaTunteet();
	}
	
	/**
	 * Antaa tunteet listana
	 * @param nro päivämäärän numero
	 * @return tunnetilat
	 */
	public List<Tunnetila> annaTunteita(int nro) {
		Tunnepaivakirja kirja = new Tunnepaivakirja();
		return kirja.annaTunteita(nro);
	}
	
	/**
	 * Lisää uuden merkinnän tietorakenteeseen. Ottaa merkinnän omistukseensa.
	 * @param merkinta lisättävään merkinnän viite. Huom tietorakenne muuttuu omistajaksi
	 * @example
	 * <pre name="test">
	 * #THROWS SailoException
	 * Merkinnat merkinnat = new Merkinnat();
	 * Merkinta pvm1 = new Merkinta(), pvm2 = new Merkinta();
	 * merkinnat.getLkm() === 0;
	 * merkinnat.lisaa(pvm1); merkinnat.getLkm() === 1;
	 * merkinnat.lisaa(pvm2); merkinnat.getLkm() === 2;
	 * merkinnat.lisaa(pvm1); merkinnat.getLkm() === 3;
	 * merkinnat.anna(0) === pvm1;
	 * merkinnat.anna(1) === pvm2;
	 * merkinnat.anna(2) === pvm1;
	 * merkinnat.lisaa(pvm1); merkinnat.getLkm() === 4;
	 * merkinnat.lisaa(pvm1); merkinnat.getLkm() === 5;
	 * </pre>
	 */
	public void lisaa(Merkinta merkinta) {
		if (lkm >= alkiot.length) {
			alkiot  = Arrays.copyOf(alkiot, alkiot.length + MAX_MERKINTOJA);
		}
		alkiot[lkm] = merkinta;
		lkm++;
		muutettu = true;
	}
	
	/**
	 * Palauttaa viitteen i:teen merkintään
	 * @param i monennenko merkinnän viite halutaan
	 * @return viite merkintään, jonka indeksi on i
	 * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella
	 */
	public Merkinta anna(int i) throws IndexOutOfBoundsException {
		if (i < 0 || lkm <= i) throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
		return alkiot[i];
	}
	
	/**
	 * Käydään läpi merkinnät ja korvataan/lisätään
	 * @param merkinta verrattava merkintä
	 */
	public void korvaaTaiLisaa(Merkinta merkinta) {
        int id = merkinta.getTunnusNro();
        for (int i = 0; i < lkm; i++) {
            if ( alkiot[i].getTunnusNro() == id ) {
                alkiot[i] = merkinta;
                muutettu = true;
                return;
            }
        }
        lisaa(merkinta);
	}
	
	/**
	 * Poistometodi
	 * @param id Poistettavan id
	 * @return 1 jos poisto onnistuu
     * @example 
     * <pre name="test"> 
     * #THROWS SailoException  
     * Merkinnat merkinnat = new Merkinnat(); 
     * Merkinta pvm1 = new Merkinta(), pvm2 = new Merkinta(), pvm3 = new Merkinta(); 
     * pvm1.rekisteroi(); pvm2.rekisteroi(); pvm3.rekisteroi(); 
     * int id1 = pvm1.getTunnusNro(); 
     * merkinnat.lisaa(pvm1); merkinnat.lisaa(pvm2); merkinnat.lisaa(pvm3); 
     * merkinnat.poista(id1+1) === 1; 
     * merkinnat.getLkm() === 2; 
     * merkinnat.poista(id1) === 1; merkinnat.getLkm() === 1; 
     * merkinnat.poista(id1+3) === 0; merkinnat.getLkm() === 1; 
     * </pre> 
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
     * Etsitään merkinnän id
     * @param id Etsittävä id
     * @return -1 jos ei läydy
     */
	public int etsiId(int id) {
		for (int i = 0; i < lkm; i++) {
			if (id == alkiot[i].getTunnusNro()) return i;
		}
		return -1;
	}

	/**
	 * Lukee merkistön tiedostosta.
	 * @param tied tiedoston hakemisto
	 * @throws SailoException jos lukeminen epäonnistuu
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * #import java.io.File;
     *  Merkinnat merkinnat = new Merkinnat();
     *  Merkinta pvm1 = new Merkinta(), pvm2 = new Merkinta();
     *  String hakemisto = "testi";
     *  String tiedNimi = hakemisto+"/merkinnat";
     *  File ftied = new File(tiedNimi+".dat");
     *  File dir = new File(hakemisto);
     *  dir.mkdir();
     *  ftied.delete();
     *  merkinnat.lueTiedostosta(tiedNimi); #THROWS SailoException
     *  merkinnat.lisaa(pvm1);
     *  merkinnat.lisaa(pvm2);
     *  merkinnat.tallenna();
     *  merkinnat = new Merkinnat();            // Poistetaan vanhat luomalla uusi
     *  merkinnat.lueTiedostosta(tiedNimi);  // johon ladataan tiedot tiedostosta.
     *  Iterator<Merkinta> i = merkinnat.iterator();
     *  i.next() === pvm1;
     *  i.next() === pvm2;
     *  i.hasNext() === false;
     *  merkinnat.lisaa(pvm2);
     *  merkinnat.tallenna();
     *  ftied.delete() === true;
     *  File fbak = new File(tiedNimi+".bak");
     *  fbak.delete() === true;
     * </pre>
	 */
	public void lueTiedostosta(String tied) throws SailoException {
		setTiedostonPerusNimi(tied);
		try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {
			String rivi;
			while ( (rivi = fi.readLine()) != null) {
				rivi = rivi.trim();
				if ("".contentEquals(rivi) || rivi.charAt(0) == ';') continue;
				Merkinta merkinta = new Merkinta();
				merkinta.parse(rivi);
				lisaa(merkinta);
			}
			muutettu = false;
		} catch (FileNotFoundException e) {
			throw new SailoException ("Tiedosto " + getTiedostonNimi() + " ei aukea");
		} catch (IOException e) {
			throw new SailoException ("Ongelmia tiedoston kanssa: " + e.getMessage());
		}
			
	}
	
	/**
	 * Luetaan tiedot tiedostosta
	 * @throws SailoException Jos esiintyy ongelma
	 */
	public void lueTiedostosta() throws SailoException {
		lueTiedostosta(getTiedostonPerusNimi());
	}
	
	/**
	 * Tallentaa merkistön tiedostoon.
	 * @throws SailoException jos talletus epäonnistuu
	 */
	public void tallenna() throws SailoException {
		if (!muutettu) return;
		File fbak = new File(getBakNimi());
		File ftied = new File(getTiedostonNimi());
		fbak.delete();
		ftied.renameTo(fbak);
		
		try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
			for (Merkinta merkinta : this) {
					fo.println(merkinta.toString());
				}
		} catch (FileNotFoundException ex) {
			throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
		} catch (IOException ex) {
			throw new SailoException("Tiedoston " + ftied.getName() + "kirjoittamisessa ongelmia");
		}
		
		muutettu = false;
		
	}
	
	/**
	 * Palauttaa tunnepäiväkirjan merkintöjen lukumäärän
	 * @return merkintöjen lukumäärä
	 */
	public int getLkm() {
		return this.lkm;
	}
	
	/**
	 * Palauttaa tiedoston kokonimen
	 * @return Tiedoston kokonimi
	 */
	public String getKokoNimi() {
		return kokoNimi;
	}
	
	/**
	 * Haetaan tiedoston perusnimi
	 * @return Tiedoston perusnimi
	 */
	public String getTiedostonPerusNimi() {
		return tiedostonPerusNimi;
	}
	
	/**
	 * Asetetaan tiedston perusnimeksi annettu nimi
	 * @param nimi Haluttu nimi tiedostolle
	 */
	public void setTiedostonPerusNimi(String nimi) {
		tiedostonPerusNimi = nimi;
	}
	
	/**
	 * Haetaan tiedoston nimi
	 * @return Tiedoston nimi
	 */
	public String getTiedostonNimi() {
		return getTiedostonPerusNimi() + ".dat";
	}
	
	/**
	 * Palauttaa varakopiotiedoston nimen
	 * @return varakopiotiedoston nimi
	 */
	public String getBakNimi() {
		return tiedostonPerusNimi + ".bak";
	}

    /**
     * Luokka merkintöjen iteroimiseksi.
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #PACKAGEIMPORT
     * #import java.util.*;
     * 
     * Merkinnat merkinnat = new Merkinnat();
     *  Merkinta pvm1 = new Merkinta(), pvm2 = new Merkinta();
     * pvm1.rekisteroi(); pvm2.rekisteroi();
     *
     * merkinnat.lisaa(pvm1); 
     * merkinnat.lisaa(pvm2);  
     * merkinnat.lisaa(pvm1);  
     * 
     * StringBuffer ids = new StringBuffer(30);
     * for (Merkinta pvm : merkinnat)   // Kokeillaan for-silmukan toimintaa
     *   ids.append(" "+pvm.getTunnusNro());           
     * 
     * String tulos = " " + pvm1.getTunnusNro() + " " + pvm2.getTunnusNro() + " " + pvm1.getTunnusNro();
     * 
     * ids.toString() === tulos; 
     * 
     * ids = new StringBuffer(30);
     * for (Iterator<Merkinta>  i=merkinnat.iterator(); i.hasNext(); ) { // ja iteraattorin toimintaa
     *   Merkinta pvm = i.next();
     *   ids.append(" "+pvm.getTunnusNro());           
     * }
     * 
     * ids.toString() === tulos;
     * 
     * Iterator<Merkinta>  i=merkinnat.iterator();
     * i.next() == pvm1  === true;
     * i.next() == pvm2  === true;
     * i.next() == pvm1  === true;
     * 
     * i.next();  #THROWS NoSuchElementException
     *  
     * </pre>
     */
    public class MerkinnatIterator implements Iterator<Merkinta> {
        private int kohdalla = 0;


        /**
         * Onko olemassa vielä seuraavaa merkintää
         * @see java.util.Iterator#hasNext()
         * @return true jos on vielä merkintöjä
         */
        @Override
        public boolean hasNext() {
            return kohdalla < getLkm();
        }


        /**
         * Annetaan seuraava merkintä
         * @return seuraava merkintä
         * @throws NoSuchElementException jos seuraava alkiota ei enää ole
         * @see java.util.Iterator#next()
         */
        @Override
        public Merkinta next() throws NoSuchElementException {
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
     * Palautetaan merkintäiteraattori
     */
    @Override
    public Iterator<Merkinta> iterator() {
        return new MerkinnatIterator();
    }
    
    /**
     * Etsitään kaikki merkinnät
     * @return tietorakenteen läytyneistä merkinnäistä 
     */ 
    public Collection<Merkinta> etsiKaikki() { 
        Collection<Merkinta> loytyneet = new ArrayList<Merkinta>(); 
        for (Merkinta merkinta : alkiot) { 
        	if (merkinta == null) break;
            loytyneet.add(merkinta);  
        } 
        return loytyneet; 
    }
    
    /**
     * Etsii merkinnät hakuehdon perusteella
     * @param hakuehto haettava ehto
     * @param k indeksi
     * @return merkinnät
     * @throws SailoException Jos ongelma
     */
    public Collection<Merkinta> etsi(String hakuehto, int k) throws SailoException {
        String ehto = "*"; 
        if ( hakuehto != null && hakuehto.length() > 0 ) ehto = hakuehto; 
        int hk = k; 
        if ( hk < 0 ) hk = 1;
        Collection<Merkinta> loytyneet = new ArrayList<Merkinta>();
        if (hk == 2) {
        	for (Merkinta pvm : this) { 
                if (WildChars.onkoSamat(pvm.anna(hk), ehto)) {
                	loytyneet.add(pvm);
                	continue;
                }
                if (WildChars.onkoSamat(pvm.anna(hk+1), ehto)) {
                	loytyneet.add(pvm);
                	continue;
                }
                if (WildChars.onkoSamat(pvm.anna(hk+2), ehto)) {
                	loytyneet.add(pvm);
                	continue;
                }
            }
        	return loytyneet;
        }
        for (Merkinta pvm : this) { 
            if (WildChars.onkoSamat(pvm.anna(hk), ehto)) loytyneet.add(pvm);   
        }
        return loytyneet; 
    }
    
	/**
	 * Testiohjelma merkistölle
	 * @param args ei käytössä
	 */
	public static void main(String args[]) {
		Merkinnat merkinnat = new Merkinnat();
		
		Merkinta pvm = new Merkinta(), pvm2 = new Merkinta();
		pvm.rekisteroi();
		pvm.vastaaEnsimmainen();
		pvm2.rekisteroi();
		pvm2.vastaaEnsimmainen();
		
		merkinnat.lisaa(pvm);
		merkinnat.lisaa(pvm2);
		merkinnat.lisaa(pvm2);
		merkinnat.lisaa(pvm2);
		
		System.out.println("============ Merkinnät testi ============");
			
		for (int i = 0; i < merkinnat.getLkm(); i++) {
			Merkinta merkinta = merkinnat.anna(i);
			System.out.println("Merkinnän nro " + i);
			merkinta.tulosta(System.out);
		}
	}


}
