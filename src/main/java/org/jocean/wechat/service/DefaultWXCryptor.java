package org.jocean.wechat.service;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.jocean.idiom.ExceptionUtils;
import org.jocean.idiom.Md5;
import org.jocean.wechat.AesException;
import org.jocean.wechat.WXCryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.google.common.base.Charsets;
import com.google.common.io.BaseEncoding;

public class DefaultWXCryptor implements WXCryptor {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultWXCryptor.class);

    /**
     * 判断是否加密
     * @param signature
     * @param token
     * @param timestamp
     * @param nonce
     *
     * @return
     */
    @Override
    public boolean verifySignature(final String signature, final String timestamp, final String nonce) {
        LOG.info("###token:{};signature:{};timestamp:{};nonce:{}", this._verifyToken, signature, timestamp, nonce);
        if (signature != null && !signature.equals("") && timestamp != null && !timestamp.equals("") && nonce != null
                && !nonce.equals("")) {
            try {
                final String[] ss = new String[] { this._verifyToken, timestamp, nonce };
                Arrays.sort(ss);
                final String allstr = ss[0] + ss[1] + ss[2];
                final MessageDigest md = MessageDigest.getInstance("SHA1");
                md.update(allstr.getBytes(Charsets.UTF_8));
                final byte[] signatureBytes = md.digest();
                final String localSignature = Md5.bytesToHexString(signatureBytes);

                LOG.info("request's signature {}, calced: {}", signature, localSignature);
                return localSignature.equals(signature);
            } catch (final Exception e) {
                LOG.warn("exception when verifySignature, detail: {}", ExceptionUtils.exception2detail(e));
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 将公众平台回复用户的消息加密打包.
     * <ol>
     *  <li>对要发送的消息进行AES-CBC加密</li>
     *  <li>生成安全签名</li>
     *  <li>将消息密文和安全签名打包成xml格式</li>
     * </ol>
     *
     * @param replyMsg 公众平台待回复用户的消息，xml格式的字符串
     * @param timeStamp 时间戳，可以自己生成，也可以用URL参数的timestamp
     * @param nonce 随机串，可以自己生成，也可以用URL参数的nonce
     *
     * @return 加密后的可以直接回复用户的密文，包括msg_signature, timestamp, nonce, encrypt的xml格式的字符串
     * @throws AesException 执行失败，请查看该异常的错误码和具体的错误信息
     */
    @Override
    public ToWXEncryptedMessage encryptMsg(final String replyMsg, String timeStamp, final String nonce, final String appid) throws AesException {
        // 加密
        final String encrypt = encrypt(getRandomStr(), replyMsg, appid);

        // 生成安全签名
        if (timeStamp == "") {
            timeStamp = Long.toString(System.currentTimeMillis());
        }

        final String signature = SHA1.getSHA1(this._verifyToken, timeStamp, nonce, encrypt);

        // 生成发送的xml
        final ToWXEncryptedMessage msg = new ToWXEncryptedMessage();

        msg.setEncrypt(encrypt);
        msg.setMsgSignature(signature);
        msg.setTimeStamp(timeStamp);
        msg.setNonce(nonce);

//        final String result = XMLParse.generate(encrypt, signature, timeStamp, nonce);
        return msg;
    }

    // 随机生成16位字符串
    String getRandomStr() {
        final String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            final int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 对明文进行加密.
     *
     * @param text 需要加密的明文
     * @return 加密后base64编码的字符串
     * @throws AesException aes加密失败
     */
    String encrypt(final String randomStr, final String text, final String appId) throws AesException {
        final byte[] aesKey = BaseEncoding.base64().decode(this._encodingAesKey + "=");
        final ByteGroup byteCollector = new ByteGroup();
        final byte[] randomStrBytes = randomStr.getBytes(Charsets.UTF_8);
        final byte[] textBytes = text.getBytes(Charsets.UTF_8);
        final byte[] networkBytesOrder = getNetworkBytesOrder(textBytes.length);
        final byte[] appidBytes = appId.getBytes(Charsets.UTF_8);

        // randomStr + networkBytesOrder + text + appid
        byteCollector.addBytes(randomStrBytes);
        byteCollector.addBytes(networkBytesOrder);
        byteCollector.addBytes(textBytes);
        byteCollector.addBytes(appidBytes);

        // ... + pad: 使用自定义的填充方式对明文进行补位填充
        final byte[] padBytes = encode(byteCollector.size());
        byteCollector.addBytes(padBytes);

        // 获得最终的字节流, 未加密
        final byte[] unencrypted = byteCollector.toBytes();

        try {
            // 设置加密模式为AES的CBC模式
            final Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            final SecretKeySpec keySpec = new SecretKeySpec(aesKey, "AES");
            final IvParameterSpec iv = new IvParameterSpec(aesKey, 0, 16);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);

            // 加密
            final byte[] encrypted = cipher.doFinal(unencrypted);

            // 使用BASE64对加密后的字符串进行编码
            final String base64Encrypted = BaseEncoding.base64().encode(encrypted);

            return base64Encrypted;
        } catch (final Exception e) {
            LOG.warn("exception when encrypt, detail: {}", ExceptionUtils.exception2detail(e));
            throw new AesException(AesException.EncryptAESError);
        }
    }

    /**
     * 获得对明文进行补位填充的字节.
     *
     * @param count 需要进行填充补位操作的明文字节个数
     * @return 补齐用的字节数组
     */
    static int BLOCK_SIZE = 32;

    static byte[] encode(final int count) {
        // 计算需要填充的位数
        int amountToPad = BLOCK_SIZE - (count % BLOCK_SIZE);
        if (amountToPad == 0) {
            amountToPad = BLOCK_SIZE;
        }
        // 获得补位所用的字符
        final char padChr = chr(amountToPad);
        String tmp = new String();
        for (int index = 0; index < amountToPad; index++) {
            tmp += padChr;
        }
        return tmp.getBytes(Charsets.UTF_8);
    }

    /**
     * 将数字转化成ASCII码对应的字符，用于对明文进行补码
     *
     * @param a 需要转化的数字
     * @return 转化得到的字符
     */
    static char chr(final int a) {
        final byte target = (byte) (a & 0xFF);
        return (char) target;
    }

    /**
     * 对密文进行解密.
     *
     * @param text 需要解密的密文
     * @return 解密得到的明文
     * @throws AesException aes解密失败
     */
    @Override
    public String[] decrypt(final String text) throws AesException {
        final byte[] aesKey = BaseEncoding.base64().decode(this._encodingAesKey + "=");
        byte[] original;
        try {
            // 设置解密模式为AES的CBC模式
            final Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            final SecretKeySpec key_spec = new SecretKeySpec(aesKey, "AES");
            final IvParameterSpec iv = new IvParameterSpec(Arrays.copyOfRange(aesKey, 0, 16));
            cipher.init(Cipher.DECRYPT_MODE, key_spec, iv);

            // 使用BASE64对密文进行解码
            final byte[] encrypted = BaseEncoding.base64().decode(text);

            // 解密
            original = cipher.doFinal(encrypted);
        } catch (final Exception e) {
            LOG.warn("exception when decrypt, detail:{}", ExceptionUtils.exception2detail(e));
            throw new AesException(AesException.DecryptAESError);
        }

        String xmlContent, from_appid;
        try {
            // 去除补位字符
            final byte[] bytes = decode(original);

            // 分离16位随机字符串,网络字节序和AppId
            final byte[] networkOrder = Arrays.copyOfRange(bytes, 16, 20);

            final int xmlLength = recoverNetworkBytesOrder(networkOrder);

            xmlContent = new String(Arrays.copyOfRange(bytes, 20, 20 + xmlLength), Charsets.UTF_8);
            from_appid = new String(Arrays.copyOfRange(bytes, 20 + xmlLength, bytes.length), Charsets.UTF_8);
        } catch (final Exception e) {
            LOG.warn("exception when decrypt, detail:{}", ExceptionUtils.exception2detail(e));
            throw new AesException(AesException.IllegalBuffer);
        }

        return new String[]{xmlContent, from_appid};
    }

    /**
     * 删除解密后明文的补位字符
     *
     * @param decrypted 解密后的明文
     * @return 删除补位字符后的明文
     */
    static byte[] decode(final byte[] decrypted) {
        int pad = (int) decrypted[decrypted.length - 1];
        if (pad < 1 || pad > 32) {
            pad = 0;
        }
        return Arrays.copyOfRange(decrypted, 0, decrypted.length - pad);
    }

    // 生成4个字节的网络字节序
    byte[] getNetworkBytesOrder(final int sourceNumber) {
        final byte[] orderBytes = new byte[4];
        orderBytes[3] = (byte) (sourceNumber & 0xFF);
        orderBytes[2] = (byte) (sourceNumber >> 8 & 0xFF);
        orderBytes[1] = (byte) (sourceNumber >> 16 & 0xFF);
        orderBytes[0] = (byte) (sourceNumber >> 24 & 0xFF);
        return orderBytes;
    }

    // 还原4个字节的网络字节序
    int recoverNetworkBytesOrder(final byte[] orderBytes) {
        int sourceNumber = 0;
        for (int i = 0; i < 4; i++) {
            sourceNumber <<= 8;
            sourceNumber |= orderBytes[i] & 0xff;
        }
        return sourceNumber;
    }

    @Value("${verify.token}")
    String _verifyToken;

    @Value("${encoding.aes.key}")
    String _encodingAesKey;
}
