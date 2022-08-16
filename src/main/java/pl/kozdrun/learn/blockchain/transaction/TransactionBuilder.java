package pl.kozdrun.learn.blockchain.transaction;

import pl.kozdrun.learn.blockchain.node.trader.Trader;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TransactionBuilder {

    private Trader sender;
    private Trader receiver;
    private List<TransactionOutput> utxos;
    private BigDecimal amount;

    public TransactionBuilder withSender(Trader sender) {
        this.sender = sender;
        return this;
    }

    public TransactionBuilder withReceiver(Trader receiver) {
        this.receiver = receiver;
        return this;
    }

    public TransactionBuilder withUTXOs(List<TransactionOutput> utxos) {
        this.utxos = utxos;
        return this;
    }

    public TransactionBuilder withAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public Transaction buildTransaction() {
        List<TransactionInput> inputs = new ArrayList<>();
        List<TransactionOutput> outputs = new ArrayList<>();

        BigDecimal currentTransferAmount = BigDecimal.ZERO;

        for (TransactionOutput utxo : utxos) {

            if (currentTransferAmount.compareTo(amount) < 0) {
                currentTransferAmount = currentTransferAmount.add(utxo.getAmount());

                TransactionInput transactionInput = new TransactionInput(utxo);
                inputs.add(transactionInput);

                if (currentTransferAmount.compareTo(amount) > 0) {
                    TransactionOutput transactionOutputToReceiver = new TransactionOutput(
                            utxo.getParentId(),
                            receiver.getWallet().getPublicKey(),
                            currentTransferAmount.subtract(amount));
                    TransactionOutput transactionOutputToMyself = new TransactionOutput(
                            utxo.getParentId(),
                            sender.getWallet().getPublicKey(),
                            amount.subtract(currentTransferAmount).abs());

                    outputs.add(transactionOutputToReceiver);
                    outputs.add(transactionOutputToMyself);
                } else {
                    TransactionOutput transactionOutputToReceiver = new TransactionOutput(
                            utxo.getParentId(),
                            receiver.getWallet().getPublicKey(),
                            utxo.getAmount());

                    outputs.add(transactionOutputToReceiver);
                }
            }
        }

        return new Transaction(
                sender.getWallet().getPublicKey(),
                receiver.getWallet().getPublicKey(),
                amount,
                inputs,
                outputs);
    }
}