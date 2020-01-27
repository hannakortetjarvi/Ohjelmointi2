package fxTunnepaivakirja;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebEngine;

/**
* Tulostuksen hoitava luokka
* 
* @author Hanna
* @version 14.2.2019
*/
public class TulostusController implements ModalControllerInterface<String> {
    @FXML TextArea tulostusAlue;
    
    /**
     * Poistutaan ikkunasta
     */
    @FXML private void handleOK() {
        ModalController.closeStage(tulostusAlue);
    }

    /**
     * Tulostusmetodi
     */
    @FXML private void handleTulosta() {
        PrinterJob job = PrinterJob.createPrinterJob();
        if ( job != null && job.showPrintDialog(null) ) {
            WebEngine webEngine = new WebEngine();
            webEngine.loadContent("<pre>" + tulostusAlue.getText() + "</pre>");
            webEngine.print(job);
            job.endJob();
        }
    }

    
    @Override
    public String getResult() {
    	return null;
    } 

    
    @Override
    public void setDefault(String oletus) {
        tulostusAlue.setText(oletus);
    }

    
    /**
    * Mitä tehdään kun dialogi on näytetty
    */
    @Override
    public void handleShown() {
        //
    }
    
    /**
     * Haetaan textArea
     * @return tulostusAlue
     */
    public TextArea getTextArea() {
    	return tulostusAlue;
    }
    
    
    /**
    * Näyttää tulostusalueessa tekstin
    * @param tulostus tulostettava teskti
     * @return TulostusCtrl 
    */
    public static TulostusController tulosta(String tulostus) {
    	TulostusController tulostusCtrl = ModalController.showModeless(TulostusController.class.getResource("Tulostus.fxml"), "Tulostus", tulostus);
    	return tulostusCtrl;
    }

}