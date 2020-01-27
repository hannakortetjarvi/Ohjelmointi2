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
 * Tunnepäiväkirjan tunteet, joka osaa mm. lisätä merkinnälle tunnetilan merkinnän luonnin yhteydessä
 * @author Hanna Kortetjärvi
 * @version 1.0 22.3.2019
 */
public class Tunnetilat implements Iterable<Tunnetila> {
    private boolean muutettu = false;
    private String tiedostonPerusNimi = "";
    private int lkm = 0;
    private static final int MAX_MERKINTOJENTUNTEITA = 20;
	
	/** Taulukko tietyn merkinnän tunnetiloista */
	//private final Collection<Tunnetila> alkiot = new ArrayList<Tunnetila>();
	private static Tunnetila[] alkiot = new Tunnetila[0];
	
	/**
	 * Tunnetilojen alustaminen
	 */
	public Tunnetilat() {
		//
	}
	
	/**
	 * Hakee tunnetilan ID:n perusteella
	 * @param ID Tunnetilan ID
	 * @return Tunnetila
	 * @throws SailoException Jos ongelma
	 */
	public String getTunne(int ID) throws SailoException {
		Tunnepaivakirja kirja = new Tunnepaivakirja();
		String tunne = kirja.getTunnetila(ID);
		return tunne;
	}
	
	/**
	 * Haetaan tunnetilan tiedot merkintään
	 * @param tnro Tunteen ID 
	 * @param pvmnro Päivämäärän ID
	 * @param voim Tunteen voimakkuus (arvotaan)
	 * @return Tunteen tiedot
	 * @throws SailoException Jos ongelma
	 */
	public String getTunnetilaMerkintaan(int tnro, int pvmnro, int voim) throws SailoException {
		Tunnetila tunne = new Tunnetila();
		String tunnetila = tunne.getTunnetilaMerkintaan(tnro, pvmnro, voim);
		return tunnetila;
	}
	
	/**
	 * Korvataan tunnetila jos se muuttuu
	 * @param tun vertailtava tunnetila
     * @example
     * <pre name="test">
     * #THROWS SailoException,CloneNotSupportedException
     * #PACKAGEIMPORT
     * Tunnetilat tunnetilat = new Tunnetilat();
     * Tunnetila tun1 = new Tunnetila(), tun2 = new Tunnetila();
     * tun1.setTunnusNro(1);
     * tun2.setTunnusNro(2);
     * tunnetilat.getLkm() === 0;
     * tunnetilat.korvaa(tun1); tunnetilat.getLkm() === 1;
     * tunnetilat.korvaa(tun2); tunnetilat.getLkm() === 2;
     * Tunnetila tun3 = new Tunnetila();
     * tun3.vastaaTunnetila(1,1,7);
     * tun3.setTunnusNro(1);
     * Iterator<Tunnetila> i2=tunnetilat.iterator();
     * i2.next() === tun1;
     * tunnetilat.korvaa(tun3); tunnetilat.getLkm() === 2;
     * i2=tunnetilat.iterator();
     * Tunnetila h = i2.next();
     * h === tun3;
     * h == tun3 === true;
     * h == tun1 === false;
     * </pre>
	 */
	public void korvaa(Tunnetila tun) {
        int id = tun.getTunnusNro();
        for (int i = 0; i < lkm; i++) {
            if ( alkiot[i].getTunnusNro() == id ) {
                alkiot[i] = tun;
                muutettu = true;
                return;
            }
        }
        lisaa(tun);
	}
	
	/**
	 * Palauttaa viitteen i:teen merkintään
	 * @param i monennenko tunnetilan viite halutaan
	 * @return viite tunnetilaan, jonka indeksi on i
	 * @throws IndexOutOfBoundsException jos i ei ole sallitulla alueella
	 */
	public Tunnetila anna(int i) throws IndexOutOfBoundsException {
		if (i < 0 || lkm <= i) throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
		return alkiot[i];
	}
	
	/**
	 * Lisää uuden tunnetilan tietorakenteeseen. Ottaa tunnetilan omistukseensa
	 * @param tun Lisättävä tunnetila
     * @example
     * <pre name="test">
     * Tunnetila tun = new Tunnetila();
     * Tunnetilat tunteet = new Tunnetilat();
     * tunteet.lisaa(tun);
     * tunteet.anna(0) === tun;
     * </pre>
	 */
	public void lisaa(Tunnetila tun) {
		if (lkm >= alkiot.length) {
			alkiot  = Arrays.copyOf(alkiot, alkiot.length + MAX_MERKINTOJENTUNTEITA);
		}
		alkiot[lkm] = tun;
		lkm++;
		muutettu = true;
	}
	
	/**
	 * Poistetaan tunnetila
	 * @param tunnusNro tunnetilan ID
	 * @return 1 jos onnistuu
     * @example
     * <pre name="test"> 
     * #import java.io.File;
     *  Tunnetilat tunteet = new Tunnetilat();
     *  Tunnetila tunne1 = new Tunnetila(); tunne1.vastaaTunnetila(1,1,7);
     *   Tunnetila tunne2 = new Tunnetila(); tunne2.vastaaTunnetila(2,1,6);
     *  tunteet.lisaa(tunne1);
     *  tunteet.lisaa(tunne2);
     *  tunteet.poista(tunne1.getTunnusNro()) === 1 ; tunteet.getLkm() === 1;
     * </pre>
	 */
	public int poista(int tunnusNro) {
        int ind = etsiId(tunnusNro); 
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
	 * @param id id
	 * @return -1 jos ei löydy, indeksi jos löytyy
	 * @return1 jos löytyy niin indeksi
     * @example
     * <pre name="test"> 
     * Tunnetilat tunteet = new Tunnetilat();
     * Tunnetila tun = new Tunnetila();
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
     * Lukee tunnetilat tiedostosta.
     * @param tied tiedoston nimen alkuosa
     * @throws SailoException jos lukeminen epäonnistuu
     * 
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.File;
     *  Tunnetilat tunteet = new Tunnetilat();
     *  Tunnetila tunne1 = new Tunnetila(); tunne1.vastaaTunnetila(1,1,1);
     *  Tunnetila tunne2 = new Tunnetila(); tunne2.vastaaTunnetila(2,2,1);
     *  Tunnetila tunne3 = new Tunnetila(); tunne3.vastaaTunnetila(1,1,1);
     *  Tunnetila tunne4 = new Tunnetila(); tunne4.vastaaTunnetila(2,2,1);
     *  Tunnetila tunne5 = new Tunnetila(); tunne5.vastaaTunnetila(1,1,1);
     *  String tiedNimi = "testi";
     *  File ftied = new File(tiedNimi+".dat");
     *  ftied.delete();
     *  tunteet.lueTiedostosta(tiedNimi); #THROWS SailoException
     *  tunteet.lisaa(tunne1);
     *  tunteet.lisaa(tunne2);
     *  tunteet.lisaa(tunne3);
     *  tunteet.lisaa(tunne4);
     *  tunteet.lisaa(tunne5);
     *  tunteet.tallenna();
     *  tunteet = new Tunnetilat();
     *  tunteet.lueTiedostosta(tiedNimi);
     *  Iterator<Tunnetila> i = tunteet.iterator();
     *  i.next().toString() === tunne1.toString();
     *  i.next().toString() === tunne2.toString();
     *  i.next().toString() === tunne3.toString();
     *  i.next().toString() === tunne4.toString();
     *  i.next().toString() === tunne5.toString();
     *  i.hasNext() === false;
     *  tunteet.lisaa(tunne1);
     *  tunteet.tallenna();
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
                Tunnetila tun = new Tunnetila();
                tun.parse(rivi); // voisi olla virhekäsittely
                lisaa(tun);
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
     * Tallentaa tunnetilat tiedostoon.
     * @throws SailoException jos talletus epäonnistuu
     */
    public void tallenna() throws SailoException {
        if ( !muutettu ) return;

        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete(); //  if ... System.err.println("Ei voi tuhota");
        ftied.renameTo(fbak); //  if ... System.err.println("Ei voi nimetä");

        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
            for (Tunnetila tun : this) {
                fo.println(tun.toString());
            }
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
        }

        muutettu = false;
    }
	/**
	 * Palauttaa tunnetilojen lukumäärän
	 * @return tunnetilojen lukumäärä
     * @example
     * <pre name="test"> 
     * Tunnetilat tunteet = new Tunnetilat();
     * Tunnetila tun1 = new Tunnetila();
     * Tunnetila tun2 = new Tunnetila();
     * tunteet.lisaa(tun1);
     * tunteet.lisaa(tun2);
     * tunteet.getLkm() === 2;
     * </pre>
	 */
	public int getLkm() {
		return this.lkm;
	}
	
	/**
	 * Luokka tunnetilojen iteroimiseksi
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #PACKAGEIMPORT
     * #import java.util.*;
     * 
     * Tunnetilat tunnetilat = new Tunnetilat();
     * Tunnetila tun1 = new Tunnetila(); 
     * Tunnetila tun2 = new Tunnetila();
     * tun1.rekisteroi(); tun2.rekisteroi();
     *
     * tunnetilat.lisaa(tun1); 
     * tunnetilat.lisaa(tun2);
     * tunnetilat.lisaa(tun1);
     * 
     * StringBuffer ids = new StringBuffer(30);
     * for (Tunnetila tun : tunnetilat)   // Kokeillaan for-silmukan toimintaa
     *   ids.append(" "+tun.getTunnusNro());           
     * 
     * String tulos = " " + tun1.getTunnusNro() + " " + tun2.getTunnusNro() + " " + tun1.getTunnusNro();
     * 
     * ids.toString() === tulos; 
     * 
     * ids = new StringBuffer(30);
     * for (Iterator<Tunnetila>  i=tunnetilat.iterator(); i.hasNext(); ) { // ja iteraattorin toimintaa
     *   Tunnetila tunne = i.next();
     *   ids.append(" "+tunne.getTunnusNro());           
     * }
     * 
     * ids.toString() === tulos;
     * 
     * Iterator<Tunnetila>  i=tunnetilat.iterator();
     * i.next() == tun1  === true;
     * i.next() == tun2  === true;
     * i.next() == tun1  === true;
     * 
     * i.next();  #THROWS NoSuchElementException
     *  
     * </pre>
     */
    public class TunnetilatIterator implements Iterator<Tunnetila> {
        private int kohdalla = 0;


        /**
         * Onko olemassa vielä seuraavaa tunnetilaa
         * @see java.util.Iterator#hasNext()
         * @return true jos on vielä tunnetiloja
         */
        @Override
        public boolean hasNext() {
            return kohdalla < getLkm();
        }


        /**
         * Annetaan seuraava tunnetila
         * @return seuraava tunnetila
         * @throws NoSuchElementException jos seuraava alkiota ei enää ole
         * @see java.util.Iterator#next()
         */
        @Override
        public Tunnetila next() throws NoSuchElementException {
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
     * Palautetaan iteraattori
     */
	@Override
    public Iterator<Tunnetila> iterator() {
        return new TunnetilatIterator();
    }
	
	/**
	 * Asetetaan perusnimeksi tiedoston nimi
	 * @param tied Tiedoston nimi
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
	 * Haetaan kaikki merkinnän tunnetilat
	 * @param tunnusNro merkinnän tunnusnumero
	 * @return tietorakenne jossa viitteet löydettyihin tunnetiloihin
	*/
	public List<Tunnetila> annaTunnetilat(int tunnusNro) {
		List<Tunnetila> loydetyt = new ArrayList<Tunnetila>();
		for (Tunnetila tun : alkiot) {
			if (tun == null) break;
			if (tun.getpvmNro() == tunnusNro) loydetyt.add(tun);
		}
		return loydetyt;
	}
	
	/**
	 * Antaa tietyn ID:isen tunnetilan
	 * @param tunnusNro tunnetilan ID
	 * @return löytyneet
	 */
	public List<Tunnetila> annaTunnetilatIDilla(int tunnusNro) {
		List<Tunnetila> loydetyt = new ArrayList<Tunnetila>();
		for (Tunnetila tun : alkiot) {
			if (tun == null) break;
			if (tun.getTunneID() == tunnusNro) loydetyt.add(tun);
		}
		return loydetyt;
	}
	
	/**
	 * Antaa tunteet Collection muodossa
	 * @param nro päivämäärän ID
	 * @return löydetyt tunnetilat
	 */
	public Collection<Tunnetila> annaTunteet(int nro) {
		Collection<Tunnetila> loydetyt = new ArrayList<Tunnetila>();
		for (Tunnetila tun : alkiot) {
			if (tun == null) break;
			if (tun.getpvmNro() == nro) loydetyt.add(tun);
		}
		return loydetyt;
	}
	
	/**
	 * Testiohjelma tunnetiloille
	 * @param args ei käytössä
	 * @throws SailoException Jos ongelma
	 */
	public static void main(String[] args) throws SailoException {
		Tunnetilat tunteet = new Tunnetilat();
		int voima = 1;
		Tunnetila tunne1 = new Tunnetila();
		tunne1.vastaaTunnetila(1, 1, voima);
		Tunnetila tunne2 = new Tunnetila();
		tunne2.vastaaTunnetila(2, 1, voima);
		Tunnetila tunne3 = new Tunnetila();
		tunne3.vastaaTunnetila(3, 3, voima);
		Tunnetila tunne4 = new Tunnetila();
		tunne4.vastaaTunnetila(1, 3, voima);
		
		tunteet.lisaa(tunne1);
		tunteet.lisaa(tunne2);
		tunteet.lisaa(tunne3);
		tunteet.lisaa(tunne4);
		
		System.out.println("============ Tunnetilat testi ===========");
		
		List<Tunnetila> tunteet2 = tunteet.annaTunnetilat(1);
		
		for (Tunnetila tun : tunteet2) {
			tun.tulosta(System.out);
		}
		
		List<Tunnetila> tunteet3 = tunteet.annaTunnetilat(3);
		
		for (Tunnetila tun : tunteet3) {
			tun.tulosta(System.out);
		}
	}
}
