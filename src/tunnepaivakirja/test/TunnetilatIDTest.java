package tunnepaivakirja.test;
// Generated by ComTest BEGIN
import java.io.File;
import tunnepaivakirja.*;
import java.util.*;
import static org.junit.Assert.*;
import org.junit.*;
// Generated by ComTest END

/**
 * Test class made by ComTest
 * @version 2019.05.03 23:54:49 // Generated by ComTest
 *
 */
@SuppressWarnings("all")
public class TunnetilatIDTest {



  // Generated by ComTest BEGIN
  /** testLisaa39 */
  @Test
  public void testLisaa39() {    // TunnetilatID: 39
    TunnetilatID tunteet = new TunnetilatID(); 
    TunnetilaID tid = new TunnetilaID(); 
    tunteet.lisaa(tid); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** 
   * testAnna60 
   * @throws IndexOutOfBoundsException when error
   * @throws SailoException when error
   */
  @Test
  public void testAnna60() throws IndexOutOfBoundsException, SailoException {    // TunnetilatID: 60
    TunnetilaID tidtest = new TunnetilaID(); 
    TunnetilatID tidttest = new TunnetilatID(); 
    tidttest.lisaa(tidtest); 
    assertEquals("From: TunnetilatID line: 65", tidtest, tidttest.anna(0)); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testGetTunnetilaTesti92 */
  @Test
  public void testGetTunnetilaTesti92() {    // TunnetilatID: 92
    TunnetilaID tid = new TunnetilaID(); 
    assertEquals("From: TunnetilatID line: 94", "Masennus", tid.palautaTunnetilaTesti(1)); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testPoista108 */
  @Test
  public void testPoista108() {    // TunnetilatID: 108
    TunnetilatID tunteet = new TunnetilatID(); 
    TunnetilaID tunne1 = new TunnetilaID(); tunne1.setTunnusNro(1); 
    TunnetilaID tunne2 = new TunnetilaID(); tunne2.setTunnusNro(2); 
    tunteet.lisaa(tunne1); 
    tunteet.lisaa(tunne2); 
    assertEquals("From: TunnetilatID line: 115", 1, tunteet.poista(tunne1.getTunnusNro())); assertEquals("From: TunnetilatID line: 115", 1, tunteet.getLkm()); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** 
   * testEtsiId134 
   * @throws SailoException when error
   */
  @Test
  public void testEtsiId134() throws SailoException {    // TunnetilatID: 134
    TunnetilatID tunteet = new TunnetilatID(); 
    TunnetilaID tun = new TunnetilaID(); 
    tun.setTunnusNro(1); 
    tunteet.lisaa(tun); 
    assertEquals("From: TunnetilatID line: 140", -1, tunteet.etsiId(0)); 
    assertEquals("From: TunnetilatID line: 141", 0, tunteet.etsiId(1)); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testEtsi155 */
  @Test
  public void testEtsi155() {    // TunnetilatID: 155
    TunnetilaID tidtest = new TunnetilaID(); 
    TunnetilatID tidttest = new TunnetilatID(); 
    tidttest.lisaa(tidtest); 
    assertEquals("From: TunnetilatID line: 159", 1, tidttest.etsi().size()); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testEtsi177 */
  @Test
  public void testEtsi177() {    // TunnetilatID: 177
    TunnetilaID tidtest = new TunnetilaID(); 
    TunnetilaID tidtest2 = new TunnetilaID(); 
    TunnetilatID tidttest = new TunnetilatID(); 
    tidtest.vastaaID(1); 
    tidtest2.vastaaID(2); 
    tidttest.lisaa(tidtest); 
    tidttest.lisaa(tidtest2); 
    assertEquals("From: TunnetilatID line: 185", tidtest, tidttest.etsi("Masennus")); 
    assertEquals("From: TunnetilatID line: 186", tidtest2, tidttest.etsi("Suru")); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** 
   * testLueTiedostosta206 
   * @throws SailoException when error
   */
  @Test
  public void testLueTiedostosta206() throws SailoException {    // TunnetilatID: 206
    TunnetilatID tidt = new TunnetilatID(); 
    TunnetilaID tid1 = new TunnetilaID(), tid2 = new TunnetilaID(); 
    tid1.vastaaID(1); 
    tid2.vastaaID(2); 
    String hakemisto = "testi"; 
    String tiedNimi = hakemisto+"/nimet"; 
    File ftied = new File(tiedNimi+".dat"); 
    File dir = new File(hakemisto); 
    dir.mkdir(); 
    ftied.delete(); 
    try {
    tidt.lueTiedostosta(tiedNimi); 
    fail("TunnetilatID: 220 Did not throw SailoException");
    } catch(SailoException _e_){ _e_.getMessage(); }
    tidt.lisaa(tid1); 
    tidt.lisaa(tid2); 
    tidt.tallenna(); 
    tidt = new TunnetilatID();  // Poistetaan vanhat luomalla uusi
    tidt.lueTiedostosta(tiedNimi);  // johon ladataan tiedot tiedostosta.
    Iterator<TunnetilaID> i = tidt.iterator(); 
    assertEquals("From: TunnetilatID line: 227", tid1, i.next()); 
    assertEquals("From: TunnetilatID line: 228", tid2, i.next()); 
    assertEquals("From: TunnetilatID line: 229", false, i.hasNext()); 
    tidt.lisaa(tid2); 
    tidt.tallenna(); 
    assertEquals("From: TunnetilatID line: 232", true, ftied.delete()); 
    File fbak = new File(tiedNimi+".bak"); 
    assertEquals("From: TunnetilatID line: 234", true, fbak.delete()); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** 
   * testTidtIterator319 
   * @throws SailoException when error
   */
  @Test
  public void testTidtIterator319() throws SailoException {    // TunnetilatID: 319
    TunnetilatID tidt = new TunnetilatID(); 
    TunnetilaID tid1 = new TunnetilaID(), tid2 = new TunnetilaID(); 
    tid1.rekisteroi(); tid2.rekisteroi(); 
    tidt.lisaa(tid1); 
    tidt.lisaa(tid2); 
    tidt.lisaa(tid1); 
    StringBuffer ids = new StringBuffer(30); 
    for (TunnetilaID tid :tidt) // Kokeillaan for-silmukan toimintaa
    ids.append(" "+tid.getTunnusNro()); 
    String tulos = " " + tid1.getTunnusNro() + " " + tid2.getTunnusNro() + " " + tid1.getTunnusNro(); 
    assertEquals("From: TunnetilatID line: 338", tulos, ids.toString()); 
    ids = new StringBuffer(30); 
    for (Iterator<TunnetilaID>  i=tidt.iterator(); i.hasNext(); ) { // ja iteraattorin toimintaa
    TunnetilaID tid = i.next(); 
    ids.append(" "+tid.getTunnusNro()); 
    }
    assertEquals("From: TunnetilatID line: 346", tulos, ids.toString()); 
    Iterator<TunnetilaID>  i=tidt.iterator(); 
    assertEquals("From: TunnetilatID line: 349", true, i.next() == tid1); 
    assertEquals("From: TunnetilatID line: 350", true, i.next() == tid2); 
    assertEquals("From: TunnetilatID line: 351", true, i.next() == tid1); 
    try {
    i.next(); 
    fail("TunnetilatID: 353 Did not throw NoSuchElementException");
    } catch(NoSuchElementException _e_){ _e_.getMessage(); }
  } // Generated by ComTest END
}