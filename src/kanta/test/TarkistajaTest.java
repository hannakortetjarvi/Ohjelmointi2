package kanta.test;
// Generated by ComTest BEGIN
import static org.junit.Assert.*;
import org.junit.*;
import static kanta.Tarkistaja.*;
import kanta.*;
// Generated by ComTest END

/**
 * Test class made by ComTest
 * @version 2019.04.26 23:31:30 // Generated by ComTest
 *
 */
@SuppressWarnings("all")
public class TarkistajaTest {



  // Generated by ComTest BEGIN
  /** testOnkoVain18 */
  @Test
  public void testOnkoVain18() {    // Tarkistaja: 18
    assertEquals("From: Tarkistaja line: 19", false, onkoVain("123","12")); 
    assertEquals("From: Tarkistaja line: 20", true, onkoVain("123","1234")); 
    assertEquals("From: Tarkistaja line: 21", true, onkoVain("","1234")); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testTarkistaKello47 */
  @Test
  public void testTarkistaKello47() {    // Tarkistaja: 47
    Tarkistaja tar = new Tarkistaja("0123456789"); 
    assertEquals("From: Tarkistaja line: 49", "Anna kellonaika muodossa 00:00", tar.tarkistaKello("12")); 
    assertEquals("From: Tarkistaja line: 50", null, tar.tarkistaKello("14:00")); 
    assertEquals("From: Tarkistaja line: 51", "Ajassa saa olla vain numeroita", tar.tarkistaKello("12:aa")); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testTarkistaVoima74 */
  @Test
  public void testTarkistaVoima74() {    // Tarkistaja: 74
    Tarkistaja tar = new Tarkistaja("0123456789"); 
    assertEquals("From: Tarkistaja line: 76", null, tar.tarkistaVoima("1")); 
    assertEquals("From: Tarkistaja line: 77", "Anna voimakkuus väliltä 1-10", tar.tarkistaVoima("12")); 
    assertEquals("From: Tarkistaja line: 78", "Voimassa saa olla vain numeroita", tar.tarkistaVoima("a")); 
  } // Generated by ComTest END
}