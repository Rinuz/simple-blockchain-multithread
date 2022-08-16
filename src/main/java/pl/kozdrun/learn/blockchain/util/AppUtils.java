package pl.kozdrun.learn.blockchain.util;

import lombok.experimental.UtilityClass;
import pl.kozdrun.learn.blockchain.blockchain.Blockchain;
import pl.kozdrun.learn.blockchain.node.miner.Miner;
import pl.kozdrun.learn.blockchain.node.BlockchainNode;
import pl.kozdrun.learn.blockchain.node.trader.Trader;

import java.util.function.Supplier;

import static pl.kozdrun.learn.blockchain.logging.Logger.initLog;

@UtilityClass
public class AppUtils {

    public static void runNewTraders(int count, Blockchain blockchain) {
        runThreads(count, () -> new Trader(blockchain));
    }

    public static void runNewMiners(int count, Blockchain blockchain) {
        runThreads(count, () -> new Miner(blockchain));
    }

    public static void runTrader(Trader trader) {
        runThreads(1, () -> trader);
    }

    private static void runThreads(int threadsCount, Supplier<BlockchainNode> threadSupplier) {
        for (int i = 0; i < threadsCount; i++) {
            BlockchainNode blockchainThread = threadSupplier.get();
            AppState.getInstance().getBlockchainNodes().add(blockchainThread);
            blockchainThread.start();
            initLog("Run thread " + blockchainThread.getClass() + ": " + blockchainThread.getNodeId());
        }
    }
}