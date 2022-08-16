package pl.kozdrun.learn.blockchain.blockchain;

import lombok.Data;
import pl.kozdrun.learn.blockchain.transaction.Transaction;

import java.util.ArrayList;
import java.util.List;

import static pl.kozdrun.learn.blockchain.logging.Logger.blockchainLog;
import static pl.kozdrun.learn.blockchain.util.CryptographyHelper.generateSHA256For;

@Data
public class Block {

    private long id;
    private long nonce;
    private long timestamp;
    protected String hash;
    private String prevHash;
    protected List<Transaction> transactions;

    public Block(long id, String prevHash) {
        this.id = id;
        this.nonce = 0;
        this.transactions = new ArrayList<>();
        this.prevHash = prevHash;
        this.timestamp = System.currentTimeMillis();
        generateHash();
    }

    public void addTransactions(List<Transaction> newTransactions, Blockchain blockchain) {
        for (Transaction newTransaction : newTransactions) {
            addTransaction(newTransaction, blockchain);
        }
        blockchainLog("Added " + newTransactions.size() + " transactions to new block: " + id, Thread.currentThread());
    }

    private boolean addTransaction(Transaction newTransaction, Blockchain blockchain) {
        if (newTransaction == null) {
            return false;
        }

        if (!newTransaction.allTxRequirementsFilled()) {
            blockchainLog("Transaction " + newTransaction.getId() + " does not meet all requirements: " + newTransaction.getId(), Thread.currentThread());
            return false;
        }

        if (!newTransaction.verifySignature()) {
            blockchainLog("Transaction " + newTransaction.getId() + " has not valid signature", Thread.currentThread());
            return false;
        }

        transactions.add(newTransaction);
        blockchainLog("Transaction " + newTransaction.getId() + " is valid and was added to new block: " + id, Thread.currentThread());
        return true;
    }

    public long incrementNonce() {
        return nonce++;
    }

    public void generateHash() {
        hash = generateSHA256For(
                id,
                timestamp,
                transactions,
                prevHash,
                nonce);
    }

    public static Block createNewFor(Blockchain blockchain) {
        synchronized (blockchain) {
            return new Block(blockchain.getSize(), blockchain.getLastBlockHash());
        }
    }
}