package dev.dorateam.secutility;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;

public class DefaultSecutilityImplTest {

    @Test
    public void simpleTest() {
        Secutility secutility = new DefaultSecutilityImpl(
                DefaultSecutilityImplTest.class.getResourceAsStream("/secutilityCfgExample.txt"));
        commonTest(secutility);
    }

    @Test
    @EnabledIfSystemProperty(named = "runOnUserHomeDir", matches = "true")
    public void userHomeDirAsDefaultTest() {
        Secutility secutility = new DefaultSecutilityImpl();
        commonTest(secutility);
    }

    private void commonTest(Secutility secutility) {
        // cfg 3:6;9:11;4:7
        //              0  1  2  3  4  5  6  7  8  9  10 11 12 13
        String param = "aa bb cc dd ff gg hh jj ll oo ii uu yy tt";
        String exp   = "aa bb cc hh jj gg dd ff ll uu ii oo yy tt";
        String actual = secutility.restoreS(param);
        Assertions.assertEquals(exp, actual);
    }
}
