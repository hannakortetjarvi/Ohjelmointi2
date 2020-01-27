package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Testit tunnepäiväkirja-ohjelmalle
 * @author Hanna Kortetjärvi
 * @version 23.4.2019
 */
@RunWith(Suite.class)
@SuiteClasses({
    tunnepaivakirja.test.MerkinnatTest.class,
    tunnepaivakirja.test.MerkintaTest.class,
    tunnepaivakirja.test.TunnepaivakirjaTest.class,
    tunnepaivakirja.test.TunnetilaIDTest.class,
    tunnepaivakirja.test.TunnetilaTest.class,
    tunnepaivakirja.test.TunnetilatIDTest.class,
    tunnepaivakirja.test.TunnetilatTest.class,
    kanta.test.TarkistajaTest.class
    })
public class AllTests {
 //
}