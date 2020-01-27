package fxTunnepaivakirja;
	
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import tunnepaivakirja.Tunnepaivakirja;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;

/**
 * 
 * @author Hanna Kortetjärvi
 * @version 1.3.2019
 * 
 * Pääohjelma tunnepäiväkirja-ohjelman ajamiseksi
 *
 */
public class TunneMain extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			final FXMLLoader ldr = new FXMLLoader(getClass().getResource("TunnepaivakirjaGUIView.fxml"));
			final Pane root = (Pane)ldr.load();
			final TunnepaivakirjaGUIController tunnepaivakirjaCtrl = (TunnepaivakirjaGUIController)ldr.getController();
			
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("Tunne.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Tunnepaivakirja");
			primaryStage.setOnCloseRequest((event) -> {
				if ( !tunnepaivakirjaCtrl.voikoSulkea() ) event.consume();
				});
			
			Tunnepaivakirja tunnepaivakirja = new Tunnepaivakirja();
			tunnepaivakirjaCtrl.setTunnepaivakirja(tunnepaivakirja);
			
			primaryStage.show();
			if ( !tunnepaivakirjaCtrl.avaa() ) Platform.exit();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Käynnistetään käyttöliittymä
	 * @param args komentorivin parametrit
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
