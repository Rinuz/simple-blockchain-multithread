package pl.kozdrun.learn.blockchain.transaction;

import lombok.Data;

@Data
public class TransactionInput {

    private String id;
    private TransactionOutput UTXO;

    public TransactionInput(TransactionOutput UTXO) {
        this.id = UTXO.getId();
        this.UTXO = UTXO;
    }
}