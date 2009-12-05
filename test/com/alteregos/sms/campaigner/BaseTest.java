package com.alteregos.sms.campaigner;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

/**
 *
 * @author john.emmanuel
 */
public class BaseTest {

    @BeforeClass
    public void setUp() {
        // code that will be invoked before this test starts
    }

    @AfterClass
    public void cleanUp() {
        // code that will be invoked after this test ends
    }

    @Test
    public void dummy(){
        Object o = null;
        assertEquals(null, o);
    }
}
