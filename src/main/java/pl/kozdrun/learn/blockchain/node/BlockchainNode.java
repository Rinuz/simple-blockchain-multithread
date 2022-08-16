package pl.kozdrun.learn.blockchain.node;

import lombok.Getter;
import org.apache.commons.lang3.RandomStringUtils;
import pl.kozdrun.learn.blockchain.blockchain.Blockchain;

import java.util.concurrent.TimeUnit;

public abstract class BlockchainNode extends Thread {

    @Getter
    private final Blockchain blockchain;
    @Getter
    private final String nodeId = RandomStringUtils.randomAlphanumeric(64);

    protected BlockchainNode(Blockchain blockchain) {
        this.blockchain = blockchain;
    }

    @Override
    public void run() {
        while (true) {
            waitSeconds(1);
            work();
        }
    }

    protected abstract void work();

    protected void waitSeconds(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
