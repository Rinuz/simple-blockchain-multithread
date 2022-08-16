package pl.kozdrun.learn.blockchain.event;

import java.util.ArrayList;
import java.util.List;

public class BlockchainEventManager {

    private static BlockchainEventManager instance;

    private final List<BlockchainEventListener> listeners;

    private BlockchainEventManager() {
        this.listeners = new ArrayList<>();
    }

    public void registerListener(BlockchainEventListener listener) {
        listeners.add(listener);
    }

    public void publishEvent(BlockchainEvent blockchainEvent) {
        listeners.forEach(listener -> {
            if (listener.eventClass() == blockchainEvent.getClass()) {
                listener.doAction(blockchainEvent);
            }
        });
    }

    public static BlockchainEventManager getInstance() {
        if (instance != null) {
            return instance;
        }

        synchronized (BlockchainEventManager.class) {
            if (instance == null) {
                instance = new BlockchainEventManager();
            }

            return instance;
        }
    }
}