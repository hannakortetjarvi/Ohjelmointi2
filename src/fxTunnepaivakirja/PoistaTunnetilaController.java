package fxTunnepaivakirja;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.control.Label;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import tunnepaivakirja.Tunnepaivakirja;
import tunnepaivakirja.Tunnetila;
import tunnepaivakirja.TunnetilaID;
import tunnepaivakirja.Tunnetilat;
import tunnepaivakirja.TunnetilatID;

/**
 * Kontrolleri tunnetilan poistamiseen päiväkirjasta
 * @author Hanna Kortetjärvi
 * @version 23.3.2019
 */
public class PoistaTunnetilaController implements ModalControllerInterface<TunnetilaID>,Initializable{
	
	@FXML private ComboBox<String> cbTunnetilat;
	@FXML private Label labelVirhe;
	
	@Override
	public void initialize(URL url, ResourceBundle bundle) {
		alusta();
	}
	
	/**
	 * Peruutusmetodi
	 */
	@FXML void handleCancel() {
		tid = null;
		ModalController.closeStage(cbTunnetilat);
    }
	
	/**
	 * Poistetaan tunnetila, jos sitä ei ole käytössä missään
	 */
    @FXML void handleOK() {
    	String tulos = onkoKaytossa();
    	if (tulos.equals("")) return;
    	asetaTunnetila();
    	if ( !Dialogs.showQuestionDialog("Poisto", "Poistetaanko tunnetila päiväkirjasta: " + tid.palautaTunnetila(), "Kyllä", "Ei") )
            return;
    	ModalController.closeStage(cbTunnetilat);
    }

	//===================================================
	
    private TunnetilaID tid;
    private static ObservableList<String> valinnat;
    
    /**
     * Tekee tarvittavat alustukset
     */
	private void alusta() {
		valinnat = TunnepaivakirjaGUIController.annaValinnat();
		cbTunnetilat.setItems(valinnat);
		cbTunnetilat.getSelectionModel().select(valinnat.get(0));
	}
	
	/**
	 * Asetetaan poistettava tunnetila
	 */
	private void asetaTunnetila() {
		String valinta = cbTunnetilat.getValue();
    	tid = Tunnepaivakirja.etsi(valinta);
	}
	
	/**
	 * Tarkistetaan onko tunnetila käytössä missään
	 * @return "" jos ongelma, ok jos ei
	 */
	private String onkoKaytossa() {
		String valinta = cbTunnetilat.getValue();
		TunnetilaID poisto = Tunnepaivakirja.etsi(valinta);
		int id = poisto.getTunnusNro();
		
		Tunnetilat tunteet = new Tunnetilat();
		List<Tunnetila> loydetty = tunteet.annaTunnetilatIDilla(id);
		
		if (loydetty.size() != 0) {
			naytaVirhe("Tunnetilaa käytetään merkinnässä");
			return "";
		}
		return "ok";
	}
	
	/**
	 * Näyttää virheen jos on ongelma
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
	public TunnetilaID getResult() {
		return tid;
	}

	@Override
	public void handleShown() {
		cbTunnetilat.requestFocus();
		
	}

	@Override
	public void setDefault(TunnetilaID oletus) {
		tid = oletus;
		
	}
	
	/**
	 * Kysytään käyttäjältä, mikä tunnetila halutaan poistaa
	 * @param modalityStage mille ollaan modaalisia, null = sovellukselle
	 * @param oletus mitä dataan näytetään oletuksena
	 * @return null jos painetaan Cancel, muuten täytetty tietue
	 */
    public static TunnetilaID kysyTID(Stage modalityStage, TunnetilaID oletus) {
        return ModalController.<TunnetilaID, UusiTunnetilaController>showModal(
                    UusiTunnetilaController.class.getResource("PoistaTunnetila.fxml"),
                    "Tunnep�iv�kirja",
                    modalityStage, oletus, null 
                );
    }
    
}
