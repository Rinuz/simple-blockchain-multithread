package pl.kozdrun.learn.blockchain.node.miner;

import lombok.Getter;
import pl.kozdrun.learn.blockchain.blockchain.Block;
import pl.kozdrun.learn.blockchain.blockchain.Blockchain;
import pl.kozdrun.learn.blockchain.config.Configuration;
import pl.kozdrun.learn.blockchain.event.BlockchainEventManager;
import pl.kozdrun.learn.blockchain.event.impl.NewBlockAddedEvent;
import pl.kozdrun.learn.blockchain.event.listeners.NewBlockAddedListener;
import pl.kozdrun.learn.blockchain.node.BlockchainNode;
import pl.kozdrun.learn.blockchain.transaction.Transaction;
import pl.kozdrun.learn.blockchain.transaction.TransactionPool;
import pl.kozdrun.learn.blockchain.wallet.Wallet;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static pl.kozdrun.learn.blockchain.blockchain.BlockchainHelper.isGoldenHasFoundFor;
import static pl.kozdrun.learn.blockchain.logging.Logger.minerLog;


public class Miner extends BlockchainNode implements NewBlockAddedListener {

    @Getter
    private Wallet wallet;
    private boolean newBlockWasAdded = false;
    private ReentrantLock minerSynchronizationLock;

    public Miner(Blockchain blockchain) {
        super(blockchain);
        BlockchainEventManager.getInstance().registerListener(this);
        this.wallet = new Wallet(blockchain);
        this.minerSynchronizationLock = new ReentrantLock();
    }

    private BigDecimal reward = BigDecimal.ZERO;

    @Override
    public void work() {
        Block block = mine();
        if (block != null && minerSynchronizationLock.tryLock()) {
            if (newBlockWasAdded) {
                minerLog("Postpone mined block because new block was already added to the blockchain", this);
                newBlockWasAdded = false;
                return;
            }

            reward = reward.add(Configuration.REWARD);
            minerLog("New block " + block.getId() + " was mined. Actual reward is: " + reward, this);
            getBlockchain().addBlock(block, getNodeId());

            minerSynchronizationLock.unlock();
        }
    }

    @Override
    public void newBlockAddedAction(NewBlockAddedEvent event) {
        if (!event.getMinerId().equals(getNodeId())) {
            newBlockWasAdded = true;
        }
    }

    private Block mine() {
        TransactionPool transactionPool = getBlockchain().getTransactionPool();
        Block block = Block.createNewFor(getBlockchain());

        List<Transaction> transactions = transactionPool.getTransactions();
        if (isEmpty(transactions)) {
            minerLog("Cannot mine because there are no transactions. Wait 3 sec", this);
            return null;
        }

        block.addTransactions(transactions, getBlockchain());

        while (!isGoldenHasFoundFor(block)) {
            if (newBlockWasAdded) {
                minerLog("Stop mining because new block was already added to the blockchain", this);
                newBlockWasAdded = false;
                return null;
            }
            block.incrementNonce();
            block.generateHash();
        }

        return block;
    }
}