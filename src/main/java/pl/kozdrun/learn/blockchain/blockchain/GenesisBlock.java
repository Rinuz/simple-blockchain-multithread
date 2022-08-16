package pl.kozdrun.learn.blockchain.blockchain;

import pl.kozdrun.learn.blockchain.transaction.Transaction;
import pl.kozdrun.learn.blockchain.transaction.TransactionOutput;
import pl.kozdrun.learn.blockchain.config.Configuration;
import pl.kozdrun.learn.blockchain.node.trader.Trader;

import java.util.Arrays;
import java.util.Collections;

import static pl.kozdrun.learn.blockchain.config.Configuration.GENESIS_TX_AMOUNT;

public class GenesisBlock extends Block {

    private Transaction genesisTransaction;

    public GenesisBlock(Trader trader) {
        super(0, null);
        this.hash = Configuration.GENESIS_HASH;
        this.genesisTransaction = createGenesisTransaction(trader);
        this.transactions.add(genesisTransaction);
    }

    public TransactionOutput getGenesisUtxo() {
        return genesisTransaction.getOutputs().get(0);
    }

    private Transaction createGenesisTransaction(Trader trader) {
        TransactionOutput transactionOutput = new TransactionOutput(
                null,
                trader.getWallet().getPublicKey(),
                GENESIS_TX_AMOUNT);

        Transaction transaction = new Transaction(
                null,
                trader.getWallet().getPublicKey(),
                GENESIS_TX_AMOUNT,
                Collections.emptyList(),
                Arrays.asList(transactionOutput)
        );
        transaction.generateSignature(trader.getWallet().getPrivateKey());
        return transaction;
    }
}