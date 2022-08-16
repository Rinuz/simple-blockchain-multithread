package pl.kozdrun.learn.blockchain.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.codec.digest.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Objects;

@UtilityClass
public class CryptographyHelper {

    public static String generateSHA256For(Object... objects) {
        return DigestUtils.sha256Hex(concatData(objects));
    }

    public static byte[] sign(PrivateKey privateKey, String data) {
        try {
            byte[] messageBytes = data.getBytes(StandardCharsets.UTF_8);

            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(messageBytes);
            return signature.sign();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verify(PublicKey publicKey, String data, byte[] signedData) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initVerify(publicKey);

            byte[] messageBytes = data.getBytes(StandardCharsets.UTF_8);

            signature.update(messageBytes);
            return signature.verify(signedData);
        } catch (Exception e) {
            throw new RuntimeException(e);

        }
    }

    public static String concatData(Object... objects) {
        StringBuilder dataBuilder = new StringBuilder();
        for (int i = 0; i < objects.length; i++) {
            dataBuilder = dataBuilder.append(Objects.toString(objects[i]));
        }
        return dataBuilder.toString();
    }
}
