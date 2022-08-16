package pl.kozdrun.learn.blockchain.blockchain;

import lombok.Data;
import pl.kozdrun.learn.blockchain.config.Configuration;
import pl.kozdrun.learn.blockchain.event.BlockchainEventManager;
import pl.kozdrun.learn.blockchain.event.impl.NewBlockAddedEvent;
import pl.kozdrun.learn.blockchain.node.trader.Trader;
import pl.kozdrun.learn.blockchain.transaction.Transaction;
import pl.kozdrun.learn.blockchain.transaction.TransactionPool;
import pl.kozdrun.learn.blockchain.transaction.UtxoPool;

import java.util.LinkedList;
import java.util.List;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static pl.kozdrun.learn.blockchain.logging.Logger.blockchainLog;

@Data
public final class Blockchain {

    private final UtxoPool utxoPool;
    private final TransactionPool transactionPool;

    private List<Block> blocks;

    public Blockchain() {
        this.utxoPool = new UtxoPool();
        this.transactionPool = new TransactionPool(utxoPool);
        this.blocks = new LinkedList<>();
    }

    public void addBlock(Block block, String minerId) {
        //stop and inform miners about new
        blockchainLog("Add new block to the blockchain on position " + getSize() + ": " + block.getHash(), Thread.currentThread());

        for (Transaction blockTransactions : block.getTransactions()) {
            transactionPool.remove(blockTransactions.getId());
        }

        blocks.add(block);

        BlockchainEventManager.getInstance().publishEvent(new NewBlockAddedEvent(block, minerId));
    }

    public int getSize() {
        return blocks.size();
    }

    public String getLastBlockHash() {
        if (isNotEmpty(blocks)) {
            return blocks.get(blocks.size() - 1).getHash();
        }
        return Configuration.GENESIS_HASH;
    }

    public void addGenesisBlock(Trader initialTrader) {
        GenesisBlock genesisBlock = new GenesisBlock(initialTrader);
        blocks.add(genesisBlock);
        getUtxoPool().add(genesisBlock.getGenesisUtxo());
    }
}