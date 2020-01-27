package fxTunnepaivakirja;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import kanta.Tarkistaja;
import tunnepaivakirja.Merkinta;
import tunnepaivakirja.SailoException;
import tunnepaivakirja.Tunnepaivakirja;
import tunnepaivakirja.Tunnetila;
import tunnepaivakirja.TunnetilaID;
import tunnepaivakirja.Tunnetilat;
import tunnepaivakirja.TunnetilatID;

/**
 * Kysytään merkinnän tiedot
 * @author Hanna Kortetjärvi
 * @version 1.0 2.4.2019
 */
public class MuokkaaMerkintaaController implements ModalControllerInterface<Merkinta>,Initializable {
    
	@FXML private DatePicker editPvm;
	@FXML private TextField editKello;
	@FXML private TextField editTilanne;
	@FXML private TextField editReaktio;
	@FXML private TextField editVoima1;
	@FXML private TextField editVoima2;
	@FXML private TextField editVoima3;
	@FXML private TextArea editTietoja;
    @FXML private ChoiceBox<String> editTunne1;
    @FXML private ChoiceBox<String> editTunne2;
    @FXML private ChoiceBox<String> editTunne3;
	@FXML private Label labelVirhe;
	
	private String virhe;
	

    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();  
    }
    
    /**
     * Tallennetaan tiedot
     * @throws SailoException Jos ongelma
     */
    @FXML private void handleTallenna() throws SailoException {
    	
    	String tulos = lopputarkistus();
    	if ("".equals(tulos)) return;
    	
    	for (int i = 1; i < 11; i++) {
    		kasitteleMuutosMerkintaan(i);
    	}
        ModalController.closeStage(editTietoja);
    }

    /**
     * Poistutaan muokkauksesta
     */
    @FXML private void handlePeruuta() {
        merkintaKohdalla = varaMerkinta;
        ModalController.closeStage(editTietoja);
    }

// ========================================================    
    private Merkinta merkintaKohdalla;
    private Merkinta varaMerkinta = merkintaKohdalla;
    private int k = 0;
    private static ObservableList<String> valinnat;
    
    /**
     * Tarkistetaan ennen tallentamista annetut tiedot
     * @return "" jos ongelma, ok jos ei
     * @throws SailoException Jos ongelma
     */
    public String lopputarkistus() throws SailoException {
    	if ( editPvm.getValue() == null ) { //Jos pvm tyhjä
            naytaVirhe("Päivämäärä ei saa olla tyhjä");
            return "";
        }
    	
    	Tunnepaivakirja tunnepaivakirja = new Tunnepaivakirja();
    	Collection<Merkinta> kaikki = tunnepaivakirja.etsi();
    	LocalDate paiva = editPvm.getValue();
    	
    	for (Merkinta pvm : kaikki) {
    	    if (pvm.getTunnusNro() == merkintaKohdalla.getTunnusNro()) continue;
    	    if (pvm.getPvm().equals(paivanMuutos(paiva))) { // Jos annettu päivämäärä on käytössä toisessa merkinnässä
    	        naytaVirhe("Päivämäärä on jo luotu");
                return "";
    	    }
    	}
    	
    	Tarkistaja kellontarkistus = new Tarkistaja("0123456789");
 	   	String s = editKello.getText();
 	   	virhe = kellontarkistus.tarkistaKello(s);
 	   	if ( virhe != null ) { // Jos kellonaika annettu väärin
 	   		naytaVirhe(virhe);
 	   		return "";
 	   	}
 	   	
 	   	String v;
 	   	Tarkistaja voimanTarkistus = new Tarkistaja("0123456789");
	   	v = editVoima1.getText();
	   	virhe = voimanTarkistus.tarkistaVoima(v);
	   	if ( virhe != null ) { // Jos voimakkuus annettu väärin
	   		naytaVirhe(virhe);
	   		return "";
	   	}
	   	
	   	v = editVoima2.getText();
	   	if (!v.equals("")) { // Jos voimakkuus annettu väärin
	   		virhe = voimanTarkistus.tarkistaVoima(v);
		   	if ( virhe != null ) {
		   		naytaVirhe(virhe);
		   		return "";
		   	}
	   	}
	   	
	   	v = editVoima3.getText();
	   	if (!v.equals("")) { // Jos voimakkuus annettu väärin
	   		virhe = voimanTarkistus.tarkistaVoima(v);
		   	if ( virhe != null ) {
		   		naytaVirhe(virhe);
		   		return "";
		   	}
	   	}
 	   	
	   	// Jos ei ole annettu tunnetta
 	   	if (editTunne1.getValue().equals("") || editVoima1.getText().equals("") || editTunne1.getValue() == "") {
 	   		naytaVirhe("Täytyy olla ainakin yksi tunne");
 	   		return "";
 	   	}
 	   	
 	   	// Jos annettu 2. tunne, muttei voimakkuutta
 	   	if (!editTunne2.getValue().equals("")) {
 	   		if (editVoima2.getText().equals("")) {
 	   			naytaVirhe("Anna myös tunteen voimakkuus");
 	   			return "";
 	   		}
 	   	}
 	   	
 	   	// Jos annettu 3. tunne, muttei voimakkuutta
 	   if (!editTunne3.getValue().equals("")) {
	   		if (editVoima3.getText().equals("")) {
	   			naytaVirhe("Anna myös tunteen voimakkuus");
	   			return "";
	   		}
	   	}
 	   
 	   // Jos annettu 2. voima, muttei tunnetilaa
 	   if (!editVoima2.getText().equals("")) {
 		   if (editTunne2.getValue().equals("")) {
 			   naytaVirhe("Anna myös tunnetila");
 			   return "";
 		   }
 	   }
 	   
 	   // Jos annettu 3. voima, muttei tunnetilaa
 	  if (!editVoima3.getText().equals("")) {
		   if (editTunne3.getValue().equals("")) {
			   naytaVirhe("Anna myös tunnetila");
			   return "";
		   }
	   }
 	  
 	  // Jos täytetty 3. tunne ennen 2. tunnetta
 	  if (!editTunne3.getValue().equals("") && editTunne2.getValue().equals("")) {
 		 naytaVirhe("Täytä tunnetilat järjestyksessä");
 		 return "";
 	  }
 	  
 	  //Jos tunnetila on jo käytössä
 	  if (editTunne1.getValue().equals(editTunne2.getValue()) || editTunne1.getValue().equals(editTunne3.getValue())) {
 	      naytaVirhe("Tunnetila on jo käytössä");
 	      return "";
 	  }
 	  
 	  //Jos tunnetila on jo käytössä
 	  if (editTunne2.getValue().equals(editTunne3.getValue()) && !editTunne3.getValue().equals("")) {
 	     naytaVirhe("Tunnetila on jo käytössä");
         return "";
 	  }
 	   	
 	   	return "ok"; // Jos kaikki ok
    }

    /**
     * Tyhjentään tekstikentät 
     */
    public void tyhjenna() {
        editPvm = null;
        editKello.setText("");
        editTunne1.getSelectionModel().select(null);
        editTunne2.getSelectionModel().select(null);
        editTunne3.getSelectionModel().select(null);
        editVoima1.setText("");
        editVoima2.setText("");
        editVoima3.setText("");
        editTilanne.setText("");
        editReaktio.setText("");
        editTietoja.setText("");
    }


    /**
     * Tekee tarvittavat muut alustukset.
     */
    protected void alusta() {
    	kasitteleMuutokset();
    }
    
    /**
     * Poistetaan valinnoista tyhjä kohta
     */
    public static void poistaYlimaarainen() {
    	valinnat.remove(valinnat.size()-1);
    }
    
    /**
     * Käsitellään tehdyt muutokset merkintään aktiivisesti
     */
    private void kasitteleMuutokset() {
    	int i = 0;
    	k = ++i;
    	editPvm.setOnKeyReleased( e -> kasitteleMuutosMerkintaan(k));
    	k = ++i;
        editKello.setOnKeyReleased( e -> kasitteleMuutosMerkintaan(k));
        k = ++i;
        editTunne1.setOnKeyReleased( e -> kasitteleMuutosMerkintaan(k));
        k = ++i;
        editTunne2.setOnKeyReleased( e -> kasitteleMuutosMerkintaan(k));
        k = ++i;
        editTunne3.setOnKeyReleased( e -> kasitteleMuutosMerkintaan(k));
        k = ++i;
        editVoima1.setOnKeyReleased( e -> kasitteleMuutosMerkintaan(k));
        k = ++i;
        editVoima2.setOnKeyReleased( e -> kasitteleMuutosMerkintaan(k));
        k = ++i;
        editVoima3.setOnKeyReleased( e -> kasitteleMuutosMerkintaan(k));
        k = ++i;
        editReaktio.setOnKeyReleased( e -> kasitteleMuutosMerkintaan(k));
        k = ++i;
        editTilanne.setOnKeyReleased( e -> kasitteleMuutosMerkintaan(k));
        k = ++i;
        editTietoja.setOnKeyReleased( e -> kasitteleMuutosMerkintaan(k));
    }
    
    /**
     * Käsittelee virheen
     * @param virhe Virhe
     */
    private void naytaVirhe(String virhe) {
        if ( virhe == null || virhe.isEmpty() ) {
            labelVirhe.setText("");
            return;
        }
        labelVirhe.setText(virhe);
    }
    
    
    @Override
    public void setDefault(Merkinta oletus) {
        merkintaKohdalla = oletus;
        try {
            naytaMerkinta(merkintaKohdalla);
        } catch (SailoException e) {
            e.printStackTrace();
        }
    }

    
    @Override
    public Merkinta getResult() {
    	for (int i = 1; i < 11; i++) {
    		kasitteleMuutosMerkintaan(i);
    	}
        return merkintaKohdalla;
    }
    
    
    
    /**
     * Mitä tehdään kun dialogi on näytetty
     */
    @Override
    public void handleShown() {
        editPvm.requestFocus();
    }
    
    /**
     * Käsitellään muutokset merkintään
     * @param k Muutettava kohta
     */
    private void kasitteleMuutosMerkintaan(int k) {
        if (merkintaKohdalla == null) return;
        String s;
        virhe = null;
        switch (k) {
           case 1 : 
        	   LocalDate paiva = editPvm.getValue();
        	   if (paiva == null) break;
        	   s = paivanMuutos(paiva);
        	   virhe = merkintaKohdalla.setPvm(s); 
        	   break;
           case 2 : 
        	   s = editKello.getText();
        	   virhe = merkintaKohdalla.setKello(s); 
        	   break;
           case 3 : 
        	   String tunne = editTunne1.getValue();
        	   String voima = editVoima1.getText();
        	   s = tunne + " (" + voima + ")"; 
        	   virhe = merkintaKohdalla.setTunne1(s); 
        	   break;
           case 4 : 
        	   tunne = editTunne2.getValue();
        	   if ("".equals(tunne)) {
        		   virhe = merkintaKohdalla.setTunne2(tunne);
        		   break;
        	   }
        	   voima = editVoima2.getText();
        	   s = tunne + " (" + voima + ")"; 
        	   virhe = merkintaKohdalla.setTunne2(s);
        	   break;
           case 5 : 
        	   tunne = editTunne3.getValue();
        	   if ("".equals(tunne)) {
        		   virhe = merkintaKohdalla.setTunne3(tunne);
        		   break;
        	   }
        	   voima = editVoima3.getText();
        	   s = tunne + " (" + voima + ")"; 
        	   virhe = merkintaKohdalla.setTunne3(s);
        	   break;
           case 6 : 
        	   tunne = editTunne1.getValue();
        	   voima = editVoima1.getText();
        	   s = tunne + " (" + voima + ")"; 
        	   virhe = merkintaKohdalla.setTunne1(s);
        	   break;
           case 7 : 
        	   tunne = editTunne2.getValue();
        	   if ("".equals(tunne)) {
        		   virhe = merkintaKohdalla.setTunne2(tunne);
        		   break;
        	   }
        	   voima = editVoima2.getText();
        	   s = tunne + " (" + voima + ")"; 
        	   virhe = merkintaKohdalla.setTunne2(s);
        	   break;
           case 8 : 
        	   tunne = editTunne3.getValue();
        	   if ("".equals(tunne)) {
        		   virhe = merkintaKohdalla.setTunne3(tunne);
        		   break;
        	   }
        	   voima = editVoima3.getText();
        	   s = tunne + " (" + voima + ")"; 
        	   virhe = merkintaKohdalla.setTunne3(s);
        	   break;
           case 9 : 
        	   s = editTilanne.getText();
        	   virhe = merkintaKohdalla.setTilanne(s); break;
           case 10 : 
        	   s = editReaktio.getText();
        	   virhe = merkintaKohdalla.setReaktio(s); break;
           case 11 : 
        	   s = editTietoja.getText();
        	   virhe = merkintaKohdalla.setTietoja(s); break;
           default:
        }
        if (virhe == null) {
            naytaVirhe(virhe);
        } else {
            naytaVirhe(virhe);
        }
    }
    
    
    /**
     * Näytetään merkinnän tiedot komponentteihin
     * @param merkinta näytettävä merkintä
     * @throws SailoException Jos ongelma
     */
	public void naytaMerkinta(Merkinta merkinta) throws SailoException {
        if (merkinta == null) return;
        
        LocalDate paiva = paivanMuutos(merkinta);
        editPvm.setValue(paiva);
        
        valinnat = TunnepaivakirjaGUIController.annaValinnat();
        valinnat.add("");
        
        editTunne1.setItems(valinnat);
        editTunne1.getSelectionModel().select(merkinta.getTunne(1));
        
        editTunne2.setItems(valinnat);
        editTunne2.getSelectionModel().select(merkinta.getTunne(2));
        
        editTunne3.setItems(valinnat);
        editTunne3.getSelectionModel().select(merkinta.getTunne(3));
        

        editKello.setText(merkinta.getKello());
        editVoima1.setText(merkinta.getVoima(1));
        editVoima2.setText(merkinta.getVoima(2));
        editVoima3.setText(merkinta.getVoima(3));
        editTilanne.setText(merkinta.getTilanne());
        editReaktio.setText(merkinta.getReaktio());
        editTietoja.setText(merkinta.getTietoja());
    }
    
	/**
	 * Apumetodi muuttamaan string LocalDate tyyppiseksi muuttujaksi
	 * @param merkinta muutettava merkintä
	 * @return Merkkijono muutettuna LocalDate:ksi
	 */
    public LocalDate paivanMuutos(Merkinta merkinta) {
    	if ("".equals(merkinta.getPvm())) {
    		return null;
    	}
    	String pvm = merkinta.getPvm();
    	String pattern = "dd.MM.yyyy";
    	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
    	return LocalDate.parse(pvm, dateFormatter);
    }
    
     /**
      * Apumetodi muuttamaan LocalDate String tyyppiseksi muuttujaksi
      * @param paiva Päivämäärä LocalDate muodossa
      * @return Päivämäärä stringinä
      */
    public String paivanMuutos(LocalDate paiva) {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    	String s = paiva.format(formatter);
    	return s;
    }
    
    
    /**
     * Luodaan merkinnän kysymisdialogi ja palautetaan sama tietue muutettuna tai null
     * @param modalityStage mille ollaan modaalisia, null = sovellukselle
     * @param oletus mitä dataan näytetään oletuksena
     * @return null jos painetaan Cancel, muuten täytetty tietue
     */
    public static Merkinta kysyMerkinta(Stage modalityStage, Merkinta oletus) {
        return ModalController.<Merkinta, MuokkaaMerkintaaController>showModal(
                    MuokkaaMerkintaaController.class.getResource("Muokkausikkuna.fxml"),
                    "Tunnepäiväkirja",
                    modalityStage, oletus, null 
                );
    }

}
