package pl.kozdrun.learn.blockchain.transaction;

import lombok.Getter;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static pl.kozdrun.learn.blockchain.logging.Logger.blockchainLog;

public final class UtxoPool {

    @Getter
    private List<TransactionOutput> utxos;

    public UtxoPool() {
        this.utxos = new ArrayList<>();
    }

    public void add(TransactionOutput utxo) {
        utxos.add(utxo);
        blockchainLog("New UTXO was added to the pool: " + utxo.getId(), Thread.currentThread());
    }

    public void remove(String id) {
        TransactionOutput utxo = utxos.stream()
                .filter(transactionOutput -> transactionOutput.getId().equals(id))
                        .findFirst().get();
        utxos.remove(utxo);
        blockchainLog("UTXO was removed from the pool: " + utxo.getId(), Thread.currentThread());
    }

    public List<TransactionOutput> getOwnedBy(PublicKey owner) {
        return utxos.stream()
                .filter(transaction -> transaction.getReceiver().equals(owner))
                .collect(Collectors.toList());
    }
}