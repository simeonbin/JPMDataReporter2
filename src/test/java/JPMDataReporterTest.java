/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
// package JPMDataReporter;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author Simeon
 */
public class JPMDataReporterTest {

    public JPMDataReporterTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of entriesSortedByValues method, of class JPMDataReporter.
     */
    @Test
    public void testEntriesSortedByValues() {
        System.out.println("entriesSortedByValues");
        Map<String, Double> testEntityMap = new HashMap<>();
        testEntityMap.put("abc", 10000.0); testEntityMap.put("abd", 100000.0);
        testEntityMap.put("abe", 15000.0); testEntityMap.put("abf", 5000.0);
        testEntityMap.put("abg", 33000.0); testEntityMap.put("abh", 103000.0);


        SortedSet<Map.Entry<String, Double>> result = JPMDataReporter.entriesSortedByValues(testEntityMap);

        Assert.assertEquals( 6, result.size() );

        Double previous = null;
        for(Map.Entry<String, Double> entry : result) {
            Assert.assertNotNull( entry.getValue() );
            if (previous != null) {
                Assert.assertTrue( entry.getValue() <= previous );
            }
            previous = entry.getValue();
        }
    }

    /**
     * Test of addDays method, of class JPMDataReporter.
     */
    @Test
    public void testAddDays() throws ParseException {
        System.out.println("addDays");
        String settleDateString = "10 Mar 2017";
        Date settleDate =  new SimpleDateFormat("dd MMM yyyy").parse(settleDateString);

        int days = 1;
        String settleDateResultString = "11 Mar 2017";
        Date expDateResult = new SimpleDateFormat("dd MMM yyyy").parse(settleDateResultString);
        Date result = JPMDataReporter.addDays(settleDate, days);
        assertEquals(expDateResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //   fail("The test case is a prototype.");
    }

    /**
     * Test of isBankHoliday method, of class JPMDataReporter.
     */
    @Test
    public void testIsBankHoliday() throws ParseException  {
        System.out.println("isBankHoliday");
        String settleDateString = "10 Mar 2017";
        Date settleDate =  new SimpleDateFormat("dd MMM yyyy").parse(settleDateString);

        String currency = "SAR";
        boolean expResult = true;
        boolean result = JPMDataReporter.isBankHoliday(settleDate, currency);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //   fail("The test case is a prototype.");
    }

    /**
     * Test of calculateUsdAmountOfTrade method, of class JPMDataReporter.
     */
    @Test
    public void testCalculateUsdAmountOfTrade() {
        System.out.println("calculateUsdAmountOfTrade");
        Double ppu = 100.25;
        Long units = 200L;
        Double agreedFx = 0.50;
        JPMDataReporter instance = new JPMDataReporter();
        Double expResult = 10025D;
        Double result = instance.calculateUsdAmountOfTrade(ppu, units, agreedFx);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //  fail("The test case is a prototype.");
    }

    /**
     * Test of main method, of class JPMDataReporter.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        JPMDataReporter.main(args);
        // TODO review the generated test code and remove the default call to fail.
        //     fail("The test case is a prototype.");
    }

}
