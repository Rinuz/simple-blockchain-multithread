package pl.kozdrun.learn.blockchain.util;

import pl.kozdrun.learn.blockchain.node.trader.Trader;
import pl.kozdrun.learn.blockchain.util.AppState;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Random;

public class Randomizer {

    public static BigDecimal shuffleAmountLowerThan(BigDecimal maxNumber) {
        BigDecimal randomDouble = new BigDecimal(Math.random());
        BigDecimal actualRandomDec = randomDouble.multiply(maxNumber);
        actualRandomDec = actualRandomDec.setScale(2, RoundingMode.HALF_DOWN);
        return actualRandomDec;
    }

    public static Trader shuffleTradeDifferentThan(Trader trader) {
        List<Trader> traders = AppState.getInstance().getTraders();

        if (traders.size() > 1) {
            Random random = new Random();
            while (true) {
                int randomIndex = random.nextInt(0, traders.size());
                Trader randomTrader = traders.get(randomIndex);

                if (!randomTrader.equals(trader)) {
                    return randomTrader;
                }
            }
        }
        throw new IllegalStateException("To less traders was initialized to shuffling");
    }
}