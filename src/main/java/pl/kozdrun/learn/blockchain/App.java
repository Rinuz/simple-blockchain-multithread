package pl.kozdrun.learn.blockchain;

import pl.kozdrun.learn.blockchain.blockchain.Blockchain;
import pl.kozdrun.learn.blockchain.node.trader.Trader;

import static pl.kozdrun.learn.blockchain.util.AppUtils.*;

public class App {

    public static void main(String[] args) {
        Blockchain blockChain = new Blockchain();
        Trader initialTrader = new Trader(blockChain);

        blockChain.addGenesisBlock(initialTrader);

        runTrader(initialTrader);
        runNewTraders(5, blockChain);
        runNewMiners(5, blockChain);
    }
}