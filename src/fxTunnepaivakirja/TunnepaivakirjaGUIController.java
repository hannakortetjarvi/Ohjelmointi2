package fxTunnepaivakirja;

import java.awt.Desktop;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.StringGrid;
import fi.jyu.mit.fxgui.TextAreaOutputStream;
import fi.jyu.mit.ohj2.Mjonot;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import tunnepaivakirja.Merkinnat;
import tunnepaivakirja.Merkinta;
import tunnepaivakirja.Tunnepaivakirja;
import tunnepaivakirja.Tunnetila;
import tunnepaivakirja.TunnetilaID;
import tunnepaivakirja.TunnetilatID;
import tunnepaivakirja.SailoException;

/**
 * Luokka tunnepäiväkirjan käyttöliittymän tapahtumien hoitamiseksi.
 * @author Hanna Kortetjärvi
 * @version 1.0 22.3.2019
 *
 */
public class TunnepaivakirjaGUIController implements Initializable {
	
	@FXML private TextField hakuehto;
	@FXML private Label labelVirhe;
	@FXML private ListChooser<Merkinta> chooserMerkinnat;
	@FXML private ScrollPane panelMerkinta;
	@FXML private ComboBoxChooser<String> cbKentat;
	@FXML private StringGrid<Merkinta> tableMerkinnat;
	
	// Lista, jossa näkyy luodut tunnetilat. 
	private static ObservableList<String> valinnat = FXCollections.observableArrayList(); 
	
	private String nimi = "Matti Meikäläinen";
	
	@Override
	public void initialize(URL url, ResourceBundle bundle) {
		alusta();
	}
	
	/**
	 * Merkinnän hakeminen
	 */
    @FXML private void handleHakuehto() {
    	haeHakuehto(0);
    }
    
    /**
     * Tunnepäiväkirjan avaus
     */
    @FXML private void handleAvaa() {
    	avaa();
    }
    
    /**
     * Tunnepäiväkirjan sulkeminen
     */
    @FXML private void handleLopeta() {
    	tallenna();
    	Platform.exit();
    }
    
    /**
     * Suunnitelman avaus
     */
    @FXML private void handleApua() {
    	avustus();
    }
    
    /**
     * Tunnepäiväkirjan versio & tekijä
     */
    @FXML private void handleTietoja() {
    	ModalController.showModal(TunnepaivakirjaGUIController.class.getResource("Tietoja.fxml"), "Tunnepäiväkirja", null, "");
    }
    
    /**
     * Muokkausikkunan avaus
     * @throws SailoException Jos ongelma
     * @throws CloneNotSupportedException Jos ongelma
     */
    @FXML private void handleMuokkaa() throws SailoException, CloneNotSupportedException {
    	haeTunne(0);
    	muokkaa();
    }
    
    /**
     * Merkinnän poistaminen
     */
	@FXML private void handlePoistaMerkinta() {
		poistaMerkinta();
    }
	
	/**
	 * Tunnetilan poistaminen.
	 * Jos tunnetila on käytössä jossakin merkinnässä -> Poistaminen ei ole mahdollista
	 */
	@FXML private void handlePoistaTunnetila() {
		poistaTunnetila();
	}

	/**
	 * Tunnepäiväkirjan tallentaminen
	 */
	@FXML private void handleTallenna() {
		tallenna();
    }
	
	/**
	 * Merkintöjen tulostaminen
	 * @throws SailoException 
	 */
	@FXML private void handleTulosta() throws SailoException {
		TulostusController tulostusCtrl = TulostusController.tulosta(null);
		tulostaValitut(tulostusCtrl.getTextArea());
	}

	/**
	 * Uuden merkinnän luominen
	 */
    @FXML private void handleUusiMerkinta() {
    	uusiMerkinta();
    }
    
    /**
     * Uuden tunnetilan luominen
     * @throws SailoException 
     */
    @FXML private void handleUusiTunnetila() throws SailoException {
    	uusiTunnetila();
    }
    
    //===================================================
    // Ei käyttöliittymään suoraan liittyvää koodia
    
    private Tunnepaivakirja tunnepaivakirja;
    private Merkinta merkintaKohdalla;
    private static Merkinta apumerkinta = new Merkinta(); 
    
    /**
     * Tekee tarvittavat alustukset
     */
    protected void alusta() {
    	chooserMerkinnat.clear();
    	tableMerkinnat.clear();
    	chooserMerkinnat.addSelectionListener (e -> merkintaKohdalla = chooserMerkinnat.getSelectedObject());
    }
    
    /**
     * Asetetaan päiväkirjalle nimi
     * @param title Päiväkirjan omistajan nimi
     */
    private void setTitle(String title) {
    	ModalController.getStage(hakuehto).setTitle(title);
    }
    
    /**
     * Alustaa tunnepäiväkirjan lukemalla sen valitun nimisestä tiedostosta
     * @param kirjoittajannimi tiedosto josta päiväkirjan tiedot luetaan
     * @return Jos ongelma, niin virhe. muuten null
     */
    protected String lueTiedosto(String kirjoittajannimi) {
    	setTitle("Tunnepäiväkirja - " + kirjoittajannimi);
    	try {
    		tunnepaivakirja.lueTiedostosta(kirjoittajannimi);
    		hae(0);
    		haeTunne(0);
    		return null;
    	} catch (SailoException e) {
    		hae(0);
    		haeTunne(0);
    		String virhe = e.getMessage();
    		if (virhe != null) Dialogs.showMessageDialog(virhe);
    		return virhe;
    	}
    }
    
    /**
     * Tietojen tallentaminen
     * @return null jos onnistuu, muuten virhe tekstinä
     */
    private String tallenna() {
    	try {
    		tunnepaivakirja.tallenna();
    		return null;
    	} catch (SailoException ex) {
    		Dialogs.showMessageDialog("Tallennuksessa ongelmia! " + ex.getMessage());
    		return ex.getMessage();
    	}
    }

    /**
     * Kysytään tiedoston nimi ja luetaan se
     * @return true jos onnistui, false jos ei
     */
    public boolean avaa() {
    	String uusinimi = KysyNimiController.kysyNimi(null, nimi);
    	if (uusinimi == null) return false;
    	lueTiedosto(uusinimi);
    	return true;
    }
    
    /**
     * Avataan muokkausikkuna, jossa merkintää muokataan
     * @throws SailoException jos Ongelma
     * @throws CloneNotSupportedException Jos ongelma
     */
    public void muokkaa() throws SailoException, CloneNotSupportedException {
        if ( merkintaKohdalla == null ) return;
        Merkinta merkinta;
        Merkinnat merkinnat = new Merkinnat();
		merkinta = MuokkaaMerkintaaController.kysyMerkinta(null, merkintaKohdalla.clone());
		MuokkaaMerkintaaController.poistaYlimaarainen();
		if ( merkinta == null ) return;
		tunnepaivakirja.korvaaTaiLisaa(merkinta);
		
    	List<Tunnetila> tunteita = merkinnat.annaTunteita(merkinta.getTunnusNro());
        Tunnetila tun1 = new Tunnetila();
        tun1.setTunnusNro(tunteita.get(0).getTunnusNro());
        tun1.vastaaTunnetila(tunnepaivakirja.palautaID(merkinta.Tunne(1)), merkinta.getTunnusNro(), merkinta.Voima(1));
        tunnepaivakirja.korvaa(tun1);
        if (tunteita.size() == 1 && !merkinta.Tunne(2).equals("") && merkinta.Tunne(3).equals("")) {
            Tunnetila tun2 = new Tunnetila();
            tun2.vastaaTunnetila(tunnepaivakirja.palautaID(merkinta.Tunne(2)), merkinta.getTunnusNro(), merkinta.Voima(2));
            tun2.rekisteroi();
            tunnepaivakirja.lisaa(tun2);
        }
        
        if (tunteita.size() == 1 && !merkinta.Tunne(2).equals("") && !merkinta.Tunne(3).equals("")) {
            Tunnetila tun2 = new Tunnetila();
            tun2.vastaaTunnetila(tunnepaivakirja.palautaID(merkinta.Tunne(2)), merkinta.getTunnusNro(), merkinta.Voima(2));
            tun2.rekisteroi();
            tunnepaivakirja.lisaa(tun2);
            Tunnetila tun3 = new Tunnetila();
            tun3.vastaaTunnetila(tunnepaivakirja.palautaID(merkinta.Tunne(3)), merkinta.getTunnusNro(), merkinta.Voima(3));
            tun3.rekisteroi();
            tunnepaivakirja.lisaa(tun3);
        }
        
        if (tunteita.size() == 2) {
            Tunnetila tun2 = new Tunnetila();
        	tun2 = new Tunnetila();
        	tun2.setTunnusNro(tunteita.get(1).getTunnusNro());
        	tun2.vastaaTunnetila(tunnepaivakirja.palautaID(merkinta.Tunne(2)), merkinta.getTunnusNro(), merkinta.Voima(2));
        	if (tun2.voimakkuus == 0) {
        	    tunnepaivakirja.poista(tun2);
        	    merkinta.tunnetila2 = "";
        	}
        	else {
        	    tunnepaivakirja.korvaa(tun2);
        	}
        	
        	if (!merkinta.Tunne(3).equals("")) {
        	    Tunnetila tun3 = new Tunnetila();
                tun3.vastaaTunnetila(tunnepaivakirja.palautaID(merkinta.Tunne(3)), merkinta.getTunnusNro(), merkinta.Voima(3));
                tun3.rekisteroi();
                tunnepaivakirja.lisaa(tun3);
        	}
        }
        if (tunteita.size() == 3) {
            Tunnetila tun2 = new Tunnetila();
            Tunnetila tun3 = new Tunnetila();
        	tun2.setTunnusNro(tunteita.get(1).getTunnusNro());
        	tun2.vastaaTunnetila(tunnepaivakirja.palautaID(merkinta.Tunne(2)), merkinta.getTunnusNro(), merkinta.Voima(2));
        	tun3.setTunnusNro(tunteita.get(2).getTunnusNro());
        	tun3.vastaaTunnetila(tunnepaivakirja.palautaID(merkinta.Tunne(3)), merkinta.getTunnusNro(), merkinta.Voima(3));
        	if (tun2.voimakkuus == 0) {
                tunnepaivakirja.poista(tun2);
                merkinta.tunnetila2 = "";
                tunnepaivakirja.poista(tun3);
                merkinta.tunnetila3 = "";
            }
        	else {
        	    tunnepaivakirja.korvaa(tun2);
                tunnepaivakirja.korvaa(tun3);
        	}
        	if (tun3.voimakkuus == 0) {
                tunnepaivakirja.korvaa(tun2);
                tunnepaivakirja.poista(tun3);
                merkinta.tunnetila3 = "";
            }
            else {
                tunnepaivakirja.korvaa(tun2);
                tunnepaivakirja.korvaa(tun3);
            }
        }
		
		tableMerkinnat.clear();
		hae(merkinta.getTunnusNro());
    }
    
    /**
     * Tarkistetaan onko tallennus tehty
     * @return true jos tallennus tehty ja sovelluksen saa sulkea, false jos ei
     */
    public boolean voikoSulkea() {
    	tallenna();
    	return true;
    }
    
    /**
     * Poistetaan merkintä
     */
    public void poistaMerkinta() {
        Merkinta merkinta = merkintaKohdalla;
        if ( merkinta == null ) return;
        if ( !Dialogs.showQuestionDialog("Poisto", "Poistetaanko merkintä: " + merkinta.getPvm(), "Kyllä", "Ei") )
            return;
        tunnepaivakirja.poista(merkinta);
        
        Merkinnat merkinnat = new Merkinnat();
        List<Tunnetila> tunteita = merkinnat.annaTunteita(merkinta.getTunnusNro());
        tunnepaivakirja.poista(tunteita.get(0));
        if ("".equals(merkinta.tunnetila2) == false) tunnepaivakirja.poista(tunteita.get(1));
        if ("".equals(merkinta.tunnetila3) == false) tunnepaivakirja.poista(tunteita.get(2));
        tableMerkinnat.clear();
        int index = chooserMerkinnat.getSelectedIndex();
        hae(0);
        chooserMerkinnat.setSelectedIndex(index);
    }
    
    /**
     * Poistetaan tunnetila
     */
	private void poistaTunnetila() {
		if (valinnat.isEmpty()) {
			Dialogs.showMessageDialog("Tunnepäiväkirjassa ei ole tunnetiloja");
			return;
		}
		TunnetilaID tid = new TunnetilaID();
		tid = PoistaTunnetilaController.kysyTID(null, tid);
		if ( tid == null ) return;
		tunnepaivakirja.poista(tid);
		valinnat.remove(tid.palautaTunnetila());
	}
    
	/**
	 * Näytetään merkintä taulukossa
	 * @param pvm Näytettävä merkintä
	 * @throws SailoException Jos ongelma
	 */
    private void naytaMerkinta(Merkinta pvm) throws SailoException {
        int kenttia = pvm.getKenttia(); 
        String[] rivi = new String[kenttia-pvm.ekaKentta()]; 
        for (int i=0, k=pvm.ekaKentta(); k < kenttia; i++, k++) 
            rivi[i] = pvm.anna(k); 
        tableMerkinnat.add(pvm,rivi);
    }
    
    /**
     * Luo uuden merkinnän, jota aletaan muokkaamaan
     */
    protected void uusiMerkinta() {
    	try {
    		Merkinta uusi = new Merkinta();
    		uusi = MuokkaaMerkintaaController.kysyMerkinta(null, uusi);
    		MuokkaaMerkintaaController.poistaYlimaarainen();
    		if (uusi == null) return;
        	uusi.rekisteroi();
        	tunnepaivakirja.lisaa(uusi);

        	Tunnetila tunne1 = new Tunnetila();
        	tunne1.vastaaTunnetila(tunnepaivakirja.palautaID(uusi.Tunne(1)), uusi.getTunnusNro(), uusi.Voima(1));
        	tunne1.rekisteroi();
        	tunnepaivakirja.lisaa(tunne1);
        	
        	if ("".equals(uusi.tunnetila2) == false) {
        		Tunnetila tunne2 = new Tunnetila();
        		tunne2.vastaaTunnetila(tunnepaivakirja.palautaID(uusi.Tunne(2)), uusi.getTunnusNro(), uusi.Voima(2));
        		tunne2.rekisteroi();
        		tunnepaivakirja.lisaa(tunne2);
        	}
        	
        	if ("".equals(uusi.tunnetila3) == false) {
        		Tunnetila tunne3 = new Tunnetila();
        		tunne3.vastaaTunnetila(tunnepaivakirja.palautaID(uusi.Tunne(3)), uusi.getTunnusNro(), uusi.Voima(3));
        		tunne3.rekisteroi();
        		tunnepaivakirja.lisaa(tunne3);
        	}
    		tableMerkinnat.clear();
        	hae(uusi.getTunnusNro());
    	} catch (SailoException e) {
    		Dialogs.showMessageDialog("Ongelmia uuden luomisessa " + e.getMessage());
    		return;
    	}
    }
    
    /**
     * Luo uuden tunnetilan päiväkirjaan
     * @throws SailoException Jos ongelma
     */
    protected void uusiTunnetila() throws SailoException {
    	TunnetilaID tid = new TunnetilaID();
    	tid = UusiTunnetilaController.luoTunnetila(null, tid); 
    	if (tid == null) return;
    	if (tid.palautaTunnetila() == null) return;
    	if ("".equals(tid.palautaTunnetila())) return;
    	tid.rekisteroi();
    	try {
    		tunnepaivakirja.lisaa(tid);
    	} catch (SailoException e) {
    		Dialogs.showMessageDialog("Ongelmia lisäämisessä! " + e.getMessage()); ;
    	}
    	haeTunne(tid.getTunnusNro());
    }
    
    /**
     * Palauttaa valinnat ChoiceBoxia varten
     * @return valinnat
     */
    public static ObservableList<String> annaValinnat() {
    	return valinnat;
    }
    
    /**
     * Hakee tunnetilojen tiedot ChoiceBoxiin
     * @param tnro tunnenumero
     */
    protected void haeTunne(int tnro) {
    	Collection<TunnetilaID> tidt;
    	
    	valinnat = FXCollections.observableArrayList(); 
    	
    	try {
    		tidt = tunnepaivakirja.etsitunne();
    		for (TunnetilaID tid : tidt) {
    			if (tid == null) break;
    			valinnat.add(tid.palautaTunnetila());
    		}
    	} catch (SailoException ex) {
    		Dialogs.showMessageDialog("Jäsenen hakemisessa ongelmia! " + ex.getMessage());
    	}
    }
    /**
     * Hakee merkinnän tiedot listaan
     * @param nro Merkinnän numero, joka aktivoidaan haun jälkeen
     */
    protected void hae(int nro) {
    	int pnro = nro;
    	if (pnro <= 0) {
    		Merkinta kohdalla = merkintaKohdalla;
    		if (kohdalla != null) pnro = kohdalla.getTunnusNro();
    	}
    	chooserMerkinnat.clear();
    	
    	int index = 0;
    	Collection<Merkinta> merkinnat = new ArrayList<Merkinta>();
    	
    	try {
    		merkinnat = tunnepaivakirja.etsi();
    		int i = 0;
    		for (Merkinta merkinta : merkinnat) {
    			if (merkinta == null) break;
    			if (merkinta.getTunnusNro() == pnro) index = i;
    			chooserMerkinnat.add(merkinta.getPvm(), merkinta);
    			naytaMerkinta(merkinta);
    			i++;
    		}
    	} catch (SailoException ex) {
    		Dialogs.showMessageDialog("Merkinnän hakemisessa ongelmia! " + ex.getMessage());
    	}
    	
    	chooserMerkinnat.setSelectedIndex(index);
    }
    
    /**
     * Apumetodi hakemiselle
     * @return indeksinumero
     */
    protected int apuHaku() {
    	int nro = cbKentat.getSelectionModel().getSelectedIndex();
    	if (nro >= 3) return nro + 2;
    	return nro;
    }

    /**
     * Hakee merkinnän tiedot listaan hakuehdon perusteella
     * @param nro Merkinnän numero, joka aktivoidaan haun jälkeen
     */
    protected void haeHakuehto(int nro) {
    	int pnro = nro;
    	if (pnro <= 0) {
    		Merkinta kohdalla = merkintaKohdalla;
    		if (kohdalla != null) pnro = kohdalla.getTunnusNro();
    	}
    	int k = apuHaku() + apumerkinta.ekaKentta();
    	String ehto = hakuehto.getText();
    	
    	if (ehto.indexOf('*') < 0) ehto = "*" + ehto + "*";
    	
    	chooserMerkinnat.clear();
    	tableMerkinnat.clear();
    	
    	int index = 0;
    	Collection<Merkinta> merkinnat;
    	
    	try {
    		merkinnat = tunnepaivakirja.etsi(ehto,k);
    		int i = 0;
    		for (Merkinta merkinta : merkinnat) {
    			if (merkinta.getTunnusNro() == pnro) index = i;
    			chooserMerkinnat.add(merkinta.getPvm(), merkinta);
    			naytaMerkinta(merkinta);
    			i++;
    		}
    	} catch (SailoException ex) {
    		Dialogs.showMessageDialog("Merkinnän hakemisessa ongelmia! " + ex.getMessage());
    	}
    	
    	chooserMerkinnat.setSelectedIndex(index);
    }
    
    /**
     * @param tunnepaivakirja Tunnepäiväkirja jota käytetään tässä käyttöliittymässä
     */
    public void setTunnepaivakirja(Tunnepaivakirja tunnepaivakirja) {
    	this.tunnepaivakirja = tunnepaivakirja;
    }
    
    /**
     * Tulostaa merkinnän tiedot
     * @param os tietovirta johon tulostetaan
     * @param merkinta tulostettava merkintä
     * @throws SailoException Jos ongelma
     */
    public void tulosta(PrintStream os, final Merkinta merkinta) throws SailoException {
    	os.println("-----------------------------");
    	merkinta.tulosta(os);
    	os.println("-----------------------------");
        List<Tunnetila> tunteet = tunnepaivakirja.annaTunteita(merkinta.getTunnusNro());
		for (Tunnetila tun:tunteet) 
		    tun.tulosta(os); 
    }
    
    /**
     * Tulostaa listassa olevat jäsenet tekstialueeseen
     * @param text Tekstiarea, johon tiedot tulostetaan
     * @throws SailoException Jos ongelma tulostuksessa
     */
    public void tulostaValitut(TextArea text) throws SailoException {
    	try (PrintStream os = TextAreaOutputStream.getTextPrintStream(text)) {
    		os.println("Tulostetaan kaikki merkinnät");
    		Collection<Merkinta> merkinnat = tunnepaivakirja.etsi("", -1);
    		for (Merkinta merkinta : merkinnat) {
    			tulosta(os, merkinta);
    			os.println("\n\n");
    		}
    	} catch (SailoException ex) {
    		Dialogs.showMessageDialog("Merkinnän hakemisessa ongelmia! " + ex.getMessage());
    	}
    }
    
    /**
     * Näytetään ohjelman suunnitelma erillisessä selaimessa
     */
    private void avustus() {
    	Desktop desktop = Desktop.getDesktop();
    	try {
    		URI uri = new URI("https://tim.jyu.fi/view/kurssit/tie/ohj2/2019k/ht/korthkyt");
    		desktop.browse(uri);
    		} catch (URISyntaxException e) {
    			return;
    			} catch (IOException e) {
    				return;
    				}
    	}
}
