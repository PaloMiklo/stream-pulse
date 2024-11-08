package com.palomiklo.streampulse

import junit.framework.Test
import junit.framework.TestCase
import junit.framework.TestSuite

/**
 * Unit test for spring App.
 */
class AppTest

/**
 * Create the test case
 *
 * @param testName name of the test case
 */
    (testName: String?) : TestCase(testName) {
    /**
     * Rigourous Test :-)
     */
    fun testApp() {
        assertTrue(true)
    }

    companion object {
        /**
         * @return the suite of tests being tested
         */
        fun suite(): Test {
            return TestSuite(AppTest::class.java)
        }
    }
}