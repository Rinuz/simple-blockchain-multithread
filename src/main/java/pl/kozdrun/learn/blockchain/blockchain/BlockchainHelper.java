package pl.kozdrun.learn.blockchain.blockchain;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import pl.kozdrun.learn.blockchain.config.Configuration;

import java.util.Objects;

@UtilityClass
public class BlockchainHelper {

    public static boolean isGoldenHasFoundFor(Block block) {
        String blockHash = block.getHash();
        String expectedLeadingZeros = StringUtils.repeat('0', Configuration.DIFFICULTY);
        String currentLeadingZeros = blockHash.substring(0, Configuration.DIFFICULTY);
        return Objects.equals(currentLeadingZeros, expectedLeadingZeros);
    }
}
