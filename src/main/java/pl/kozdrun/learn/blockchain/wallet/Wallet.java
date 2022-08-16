package pl.kozdrun.learn.blockchain.wallet;

import lombok.Data;
import pl.kozdrun.learn.blockchain.blockchain.Blockchain;
import pl.kozdrun.learn.blockchain.util.LocalKeyStore;
import pl.kozdrun.learn.blockchain.transaction.TransactionOutput;

import java.math.BigDecimal;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

@Data
public class Wallet {

    private Blockchain blockchain;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public Wallet(Blockchain blockchain) {
        KeyPair keyPair = new LocalKeyStore().getKeyPair();
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
        this.blockchain = blockchain;
    }

    public BigDecimal currentBalance() {
        List<TransactionOutput> myTransactions = blockchain.getUtxoPool().getOwnedBy(publicKey);
        return myTransactions.stream().map(TransactionOutput::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}