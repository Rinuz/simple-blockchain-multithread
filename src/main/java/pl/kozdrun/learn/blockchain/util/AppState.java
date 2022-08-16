package pl.kozdrun.learn.blockchain.util;

import lombok.Getter;
import pl.kozdrun.learn.blockchain.event.BlockchainEventManager;
import pl.kozdrun.learn.blockchain.node.BlockchainNode;
import pl.kozdrun.learn.blockchain.node.trader.Trader;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AppState {

    private static AppState instance;

    @Getter
    private final List<BlockchainNode> blockchainNodes;

    private AppState() {
        this.blockchainNodes = new ArrayList<>();
    }

    public static AppState getInstance() {
        if (instance != null) {
            return instance;
        }

        synchronized (BlockchainEventManager.class) {
            if (instance == null) {
                instance = new AppState();
            }

            return instance;
        }
    }

    public List<Trader> getTraders() {
        return blockchainNodes
                .stream()
                .filter(node -> node instanceof Trader)
                .map(node -> (Trader) node)
                .collect(Collectors.toList());
    }
}