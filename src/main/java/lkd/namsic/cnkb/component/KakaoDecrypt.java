package lkd.namsic.cnkb.component;

import lkd.namsic.cnkb.constant.Constants;
import lkd.namsic.cnkb.dto.KakaoUserInfo;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.generators.PKCS12ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.util.Base64;

@Component
public class KakaoDecrypt {

    private static final String[] prefixes = new String[] {
        "", "", "12", "24", "18", "30", "36", "12", "48", "7", "35", "40", "17", "23", "29", "isabel", "kale", "sulli",
        "van", "merry", "kyle", "james", "maddux", "tony", "hayden", "paul", "elijah", "dorothy", "sally", "bran",
        "extr.ursra", "veil"
    };

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public String decrypt(int enc, String msg, long userId) {
        byte[] key = new byte[] { 0x16, 0x08, 0x09, 0x6f, 0x02, 0x17, 0x2b, 0x08, 0x21, 0x21, 0x0a, 0x10, 0x03, 0x03, 0x07, 0x06 };
        byte[] iv = new byte[] { 0x0f, 0x08, 0x01, 0x00, 0x19, 0x47, 0x25, (byte) 0xdc, 0x15, (byte) 0xf5, 0x17, (byte) 0xe0, (byte) 0xe1, 0x15, 0x0c, 0x35 };
        byte[] salt = this.genSalt(userId, enc);
        key = deriveKey(key, salt, 2, 32);

        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));

            byte[] cipherText = Base64.getDecoder().decode(msg);
            if (cipherText.length == 0) {
                return new String(cipherText);
            }

            byte[] padded = cipher.doFinal(cipherText);
            return new String(padded, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] deriveKey(byte[] key, byte[] salt, int iterations, int dkeySize) {
        try {
            byte[] password = (new String(key, StandardCharsets.UTF_8) + "\0").getBytes(StandardCharsets.UTF_16BE);

            PKCS12ParametersGenerator generator = new PKCS12ParametersGenerator(new SHA1Digest());
            generator.init(password, salt, iterations);

            return ((KeyParameter) generator.generateDerivedParameters(dkeySize * 8)).getKey();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] genSalt(long userId, int encType) {
        if (userId <= 0) {
            return new byte[16];
        }

        StringBuilder salt = new StringBuilder(prefixes[encType] + userId);
        salt = new StringBuilder(salt.substring(0, Math.min(16, salt.length())));
        while (salt.length() < 16) {
            salt.append("\0");
        }

        return salt.toString().getBytes(StandardCharsets.UTF_8);
    }

    public KakaoUserInfo getUserInfo(AdbClient adbClient, long chatId, long userId) {
        String sender;
        if (userId == Constants.BOT_ID) {
            sender = Constants.BOT_NAME;
        } else {
            adbClient.write("SELECT name,enc FROM db2.friends WHERE id=" + userId + ";");
            String[] friendDatas;
            while(true) {
                friendDatas = adbClient.read().split("\\|");
                if (friendDatas.length == 14) {
                    adbClient.waitingMessageQueue.add(friendDatas);
                    continue;
                }

                break;
            }

            sender = this.decrypt(Integer.parseInt(friendDatas[1]), friendDatas[0], Constants.BOT_ID);
        }

        String baseQuery = "SELECT name FROM db2.open_link WHERE id = (SELECT link_id FROM chat_rooms WHERE id = " + chatId + ")";
        adbClient.write(baseQuery + " UNION ALL SELECT null WHERE NOT EXISTS (" + baseQuery + ");");

        String room = adbClient.read();
        boolean isGroupChat = true;
        if (StringUtils.isBlank(room)) {
            room = null;
            isGroupChat = false;
        }

        return new KakaoUserInfo(room, sender, isGroupChat, userId, chatId);
    }
}
