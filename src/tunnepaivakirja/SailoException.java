package tunnepaivakirja;

/**
 * Poikkeusluokka tietorakenteesta aiheutuville poikkeuksille.
 * @author Hanna Kortetjärvi
 * @version 1.0, 1.3.2019
 */
public class SailoException extends Exception {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Poikkeuksen muodostaja jolle tuodaan poikkeudessa käytettävä viesti
	 * @param viesti Poikkeuksen viesti
	 */
	public SailoException(String viesti) {
		super(viesti);
	}

}
