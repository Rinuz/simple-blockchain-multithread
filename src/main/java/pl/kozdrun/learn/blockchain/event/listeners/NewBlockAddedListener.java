package pl.kozdrun.learn.blockchain.event.listeners;

import pl.kozdrun.learn.blockchain.event.impl.NewBlockAddedEvent;
import pl.kozdrun.learn.blockchain.event.BlockchainEventListener;

public interface NewBlockAddedListener extends BlockchainEventListener<NewBlockAddedEvent> {

    @Override
    default void doAction(NewBlockAddedEvent event) {
        newBlockAddedAction(event);
    }

    @Override
    default Class<NewBlockAddedEvent> eventClass() {
        return NewBlockAddedEvent.class;
    }

    void newBlockAddedAction(NewBlockAddedEvent event);
}