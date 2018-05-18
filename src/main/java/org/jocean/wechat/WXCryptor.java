package org.jocean.wechat;

public interface WXCryptor {

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
    public String encryptMsg(final String replyMsg, String timeStamp, final String nonce, final String appid) throws AesException;

    /**
     * 对密文进行解密.
     *
     * @param text 需要解密的密文
     * @return 解密得到的明文
     * @throws AesException aes解密失败
     */
    public String[] decrypt(final String text) throws AesException;
}
