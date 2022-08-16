package pl.kozdrun.learn.blockchain.transaction;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static pl.kozdrun.learn.blockchain.logging.Logger.blockchainLog;

public final class TransactionPool {

    @Getter
    private List<Transaction> transactions;

    private UtxoPool utxoPool;

    public TransactionPool(UtxoPool utxoPool) {
        this.transactions = new ArrayList<>();
        this.utxoPool = utxoPool;
    }

    public void add(Transaction transaction) {
        transactions.add(transaction);
        blockchainLog("New transaction was added to the pool: " + transaction.getId(), Thread.currentThread());

        for (TransactionOutput transactionOutput : transaction.getOutputs()) {
            utxoPool.add(transactionOutput);
        }
        for (TransactionInput transactionInput : transaction.getInputs()) {
            utxoPool.remove(transactionInput.getId());
        }
    }

    public void remove(String id) {
        Transaction transaction = transactions.stream()
                .filter(tx -> tx.getId().equals(id))
                .findFirst().get();
        transactions.remove(transaction);
    }
}