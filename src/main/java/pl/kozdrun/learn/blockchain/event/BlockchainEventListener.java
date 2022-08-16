package pl.kozdrun.learn.blockchain.event;

public interface BlockchainEventListener<E> {

    Class<E> eventClass();

    void doAction(E event);
}