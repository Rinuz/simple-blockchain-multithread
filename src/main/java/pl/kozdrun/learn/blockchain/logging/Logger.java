package pl.kozdrun.learn.blockchain.logging;

import lombok.experimental.UtilityClass;
import pl.kozdrun.learn.blockchain.node.BlockchainNode;
import pl.kozdrun.learn.blockchain.node.miner.Miner;
import pl.kozdrun.learn.blockchain.node.trader.Trader;

@UtilityClass
public class Logger {

    public static void initLog(String log) {
        System.out.println("[INIT] " + log);
    }

    public static void blockchainLog(String log, Thread loggingNode) {
        if (loggingNode instanceof Trader) {
            System.out.println("[TRADER-BLOCKCHAIN-" +Thread.currentThread().getId() + "] " + log);
        } else if (loggingNode instanceof Miner) {
            System.out.println("[MINER-BLOCKCHAIN-" +Thread.currentThread().getId() + "] " + log);
        }
    }

    public static void traderLog(String log, BlockchainNode trader) {
        System.out.println("[TRADER-" +Thread.currentThread().getId() + "] " + log + " [" + trader.getNodeId() + "]");
    }

    public static void minerLog(String log, BlockchainNode miner) {
        System.out.println("[MINER-" +Thread.currentThread().getId() + "] " + log + " [" + miner.getNodeId() + "]");
    }
}