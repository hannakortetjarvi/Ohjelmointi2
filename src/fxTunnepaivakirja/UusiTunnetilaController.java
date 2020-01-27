package fxTunnepaivakirja;

import javafx.scene.control.Label;
import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import tunnepaivakirja.Merkinta;
import tunnepaivakirja.TunnetilaID;

/**
 * Kontrolleri uuden tunnetilan luomiselle
 * @author Hanna Kortetjärvi
 * @version 23.4.2019
 */
public class UusiTunnetilaController implements ModalControllerInterface<TunnetilaID>,Initializable {
	
	@FXML private ComboBox<String> cbTunteet;
	@FXML private TextField textTunne;
	@FXML private Label labelVirhe;
    
	@Override
	public void initialize(URL url, ResourceBundle bundle) {
		alusta();  
	}
	
	/**
	 * Luodaan uusi tunnetila, jos ei ole ongelmaa
	 */
	@FXML private void handleOK() {
		String tulos = tarkistaTunne();
		if (tulos.equals("")) return;
		luoTunnetila();
		ModalController.closeStage(textTunne);
    }
	
	/**
	 * Poistutaan ikkunasta
	 */
	@FXML private void handleCancel() {
		ModalController.closeStage(textTunne);
    }

//==========================================
	
	private TunnetilaID tid;
	private static ObservableList<String> valinnat;
	
	/**
	 * Alustetaan ikkuna
	 */
	private void alusta() {
		valinnat = TunnepaivakirjaGUIController.annaValinnat();
		if (valinnat.isEmpty()) return;
		cbTunteet.setItems(valinnat);
		cbTunteet.getSelectionModel().select(valinnat.get(0));
	}
	
	/**
	 * Tarkistetaan onko tunteen luonnissa ongelmia
	 * @return "" jos on, ok jos ei
	 */
	private String tarkistaTunne() {
		String tunne = textTunne.getText();
		if (tunne.equals("")) {
			naytaVirhe("Tunne ei voi olla tyhjä");
			return "";
		}
		for (int i = 0; i < valinnat.size(); i++) {
			if (valinnat.get(i).equals(tunne)) {
				naytaVirhe("Tunnetila on jo olemassa");
				return "";
			}
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
	
	/**
	 * Luodaan tunnetila
	 */
	private void luoTunnetila() {
		String tunne = textTunne.getText();
		tid.setTunne(tunne);
	}

	@Override
	public TunnetilaID getResult() {
		return tid;
	}

	@Override
	public void handleShown() {
		textTunne.requestFocus();
	}

	@Override
	public void setDefault(TunnetilaID oletus) {
		tid = oletus;
	}
	
	/**
	 * Kysyy käyttäjältä uuden tunnetilan
	 * @param modalityStage mille ollaan modaalisia, null = sovellukselle
	 * @param oletus mitä dataan näytetään oletuksena
	 * @return null jos painetaan Cancel, muuten täytetty tietue
	 */
    public static TunnetilaID luoTunnetila(Stage modalityStage, TunnetilaID oletus) {
        return ModalController.<TunnetilaID, UusiTunnetilaController>showModal(
                    UusiTunnetilaController.class.getResource("UusiTunnetila.fxml"),
                    "Tunnepäiväkirja",
                    modalityStage, oletus, null 
                );
    }
}
