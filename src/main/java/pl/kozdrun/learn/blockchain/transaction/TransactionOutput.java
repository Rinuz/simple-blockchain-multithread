package pl.kozdrun.learn.blockchain.transaction;

import lombok.Data;

import java.math.BigDecimal;
import java.security.PublicKey;

import static pl.kozdrun.learn.blockchain.util.CryptographyHelper.generateSHA256For;

@Data
public class TransactionOutput {

    private String id;
    private String parentId;
    private PublicKey receiver;
    private BigDecimal amount;

    public TransactionOutput(String parentId, PublicKey receiver, BigDecimal amount) {
        this.parentId = parentId;
        this.receiver = receiver;
        this.amount = amount;
        generateId();
    }

    private void generateId() {
        id = generateSHA256For(parentId, receiver, amount);
    }

    public boolean checkBelongsTo(PublicKey publicKey) {
        return publicKey.equals(receiver);
    }
}
