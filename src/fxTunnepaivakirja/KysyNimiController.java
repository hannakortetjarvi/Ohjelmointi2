package fxTunnepaivakirja;

import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Kysytään kirjoittajan nimi ja luodaan tätä varten dialogi
 * @author Hanna
 * @version 23.4.2019
 *
 */
public class KysyNimiController implements ModalControllerInterface<String>{
	
	@FXML private TextField textVastaus;
	private String vastaus = null;
	
	/**
	 * Käyttäjän nimen tallentaminen
	 */
	@FXML private void handleOK() {
		vastaus = textVastaus.getText();
		ModalController.closeStage(textVastaus);
	}
	
	/**
	 * Peruuta, sulkee ohjelman
	 */
	@FXML private void handleCancel() {
		ModalController.closeStage(textVastaus);
	}
	
	@Override
	public String getResult() {
		return vastaus;
	}
	
	@Override
	public void setDefault(String oletus) {
		textVastaus.setText(oletus);
	}
	
	/**
	 * Mitä tehdään, kun dialogi on näytetty
	 */
	@Override
	public void handleShown() {
		textVastaus.requestFocus();
	}
	
	/**
	 * Luodaan dialogi jossa kysytään nimeä, ja palautetaan siihen kirjoitettu nimi tai null
	 * @param modalityStage mille ollaan modaalisia, null = sovellukselle
	 * @param oletus mitä nimeä näytetään oletuksena
	 * @return null jos painetaan cancel, muuten nimi joka on kirjoitettu
	 */
	public static String kysyNimi(Stage modalityStage, String oletus) {
		return ModalController.showModal(
				KysyNimiController.class.getResource("Avausikkuna.fxml"),
				"Tunnep�iv�kirja",
				modalityStage, oletus);
	}
}
