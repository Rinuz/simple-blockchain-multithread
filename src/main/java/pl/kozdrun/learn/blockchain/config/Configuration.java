package pl.kozdrun.learn.blockchain.config;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class Configuration {

    public static final int DIFFICULTY = 2;
    public static final BigDecimal REWARD = BigDecimal.valueOf(50);
    public static final String GENESIS_HASH = "0000000000000000000000000000000000000000000000000000000000000000";
    public static final BigDecimal GENESIS_TX_AMOUNT = BigDecimal.valueOf(50);
}