package ru.sfedu.projectpuskaFinalVersion.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static ru.sfedu.projectpuskaFinalVersion.Constants.CFG_KEY;
import static ru.sfedu.projectpuskaFinalVersion.Constants.PATH_TO_NATIVE_LIB_WINDOWS;


public class ConfigurationUtilTest {

    @Before
    public void setUp() throws Exception {
        System.setProperty(CFG_KEY, "src/test/resources/config1.properties");
    }

    @Test
    public  void test() throws Exception {
        Assert.assertEquals(ConfigurationUtil.getConfigurationEntry(PATH_TO_NATIVE_LIB_WINDOWS), "667");
    }
}