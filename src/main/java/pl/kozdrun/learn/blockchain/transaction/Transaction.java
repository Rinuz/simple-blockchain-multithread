package pl.kozdrun.learn.blockchain.transaction;

import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;

import java.math.BigDecimal;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

import static pl.kozdrun.learn.blockchain.util.CryptographyHelper.*;

@Data
public class Transaction {

    private String id;
    private PublicKey sender;
    private PublicKey receiver;
    private BigDecimal amount;
    private byte[] signature;
    private List<TransactionInput> inputs;
    private List<TransactionOutput> outputs;

    public Transaction(PublicKey sender, PublicKey receiver, BigDecimal amount, List<TransactionInput> inputs, List<TransactionOutput> outputs) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.inputs = inputs;
        this.outputs = outputs;
        this.id = generateSHA256For(sender, receiver, amount);
    }

    public void generateSignature(PrivateKey privateKey) {
        signature = sign(privateKey, concatData(sender, receiver, amount));
    }

    public boolean verifySignature() {
        return verify(sender, concatData(sender, receiver, amount), signature);
    }

    private BigDecimal getInputsAmount() {
        BigDecimal inputsSum = BigDecimal.ZERO;
        for (TransactionInput transactionInput : inputs) {
            inputsSum = inputsSum.add(transactionInput.getUTXO().getAmount());
        }
        return inputsSum;
    }


    public boolean allTxRequirementsFilled() {
        boolean anyObjectNull = !ObjectUtils.allNotNull(sender, receiver, inputs, outputs, amount);
        if (anyObjectNull || (inputs.isEmpty() || outputs.isEmpty())) {
            System.out.println("Transaction does not fill all requirements");
            return false;
        }
        return true;
    }
}