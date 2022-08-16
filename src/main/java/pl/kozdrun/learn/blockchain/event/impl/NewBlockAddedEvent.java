package pl.kozdrun.learn.blockchain.event.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.kozdrun.learn.blockchain.blockchain.Block;
import pl.kozdrun.learn.blockchain.event.BlockchainEvent;

@AllArgsConstructor
@Getter
public class NewBlockAddedEvent implements BlockchainEvent {

    private Block newBlock;
    private String minerId;
}