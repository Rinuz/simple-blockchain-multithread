package pl.kozdrun.learn.blockchain.node.trader;

import lombok.Getter;
import pl.kozdrun.learn.blockchain.blockchain.Blockchain;
import pl.kozdrun.learn.blockchain.node.BlockchainNode;
import pl.kozdrun.learn.blockchain.transaction.Transaction;
import pl.kozdrun.learn.blockchain.transaction.TransactionBuilder;
import pl.kozdrun.learn.blockchain.transaction.TransactionOutput;
import pl.kozdrun.learn.blockchain.util.AppState;
import pl.kozdrun.learn.blockchain.util.Randomizer;
import pl.kozdrun.learn.blockchain.wallet.Wallet;

import java.math.BigDecimal;
import java.util.List;

import static pl.kozdrun.learn.blockchain.logging.Logger.traderLog;

public class Trader extends BlockchainNode {

    @Getter
    private Wallet wallet;

    public Trader(Blockchain blockchain) {
        this(blockchain, new Wallet(blockchain));
    }

    public Trader(Blockchain blockchain, Wallet wallet) {
        super(blockchain);
        this.wallet = wallet;
    }

    @Override
    public void work() {
        Transaction transaction = trade();
        if (transaction != null) {
            traderLog("Created new transaction: " + transaction.getId(), this);
            getBlockchain().getTransactionPool().add(transaction);
        }
    }

    private Transaction trade() {
        if (AppState.getInstance().getTraders().size() < 2) {
            traderLog("Cannot trade because at least 2 traders are required", this);
            return null;
        }

        if (!haveSomeMoney()) {
            traderLog("I do not have any money in my wallet", this);
            return null;
        }

        traderLog("Start trading", this);

        BigDecimal currentBalance = wallet.currentBalance();
        BigDecimal totalAmountToTransfer = currentBalance;

        Trader receiver = Randomizer.shuffleTradeDifferentThan(this);

        List<TransactionOutput> myUtxos = getBlockchain().getUtxoPool().getOwnedBy(wallet.getPublicKey());

        Transaction newTransaction = new TransactionBuilder()
                .withSender(this)
                .withReceiver(receiver)
                .withUTXOs(myUtxos)
                .withAmount(totalAmountToTransfer)
                .buildTransaction();
        newTransaction.generateSignature(wallet.getPrivateKey());
        return newTransaction;
    }

    private boolean haveSomeMoney() {
        return wallet.currentBalance().compareTo(BigDecimal.ZERO) > 0;
    }
}