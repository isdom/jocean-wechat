package org.jocean.wechat;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

public interface WXCryptor {

    public boolean verifySignature(final String signature, final String timestamp, final String nonce);

//    "<xml>\n" + "<Encrypt><![CDATA[%1$s]]></Encrypt>\n"
//    + "<MsgSignature><![CDATA[%2$s]]></MsgSignature>\n"
//    + "<TimeStamp>%3$s</TimeStamp>\n" + "<Nonce><![CDATA[%4$s]]></Nonce>\n" + "</xml>";
    @JacksonXmlRootElement(localName="xml")
    public static class ToWXEncryptedMessage {

        @JacksonXmlProperty(localName="Encrypt")
        public String getEncrypt() {
            return this._encrypt;
        }

        @JacksonXmlProperty(localName="Encrypt")
        public void setEncrypt(final String encrypt) {
            this._encrypt = encrypt;
        }

        @JacksonXmlProperty(localName="MsgSignature")
        public String getMsgSignature() {
            return this._msgSignature;
        }

        @JacksonXmlProperty(localName="MsgSignature")
        public void setMsgSignature(final String msgSignature) {
            this._msgSignature = msgSignature;
        }

        @JacksonXmlProperty(localName="TimeStamp")
        public String getTimeStamp() {
            return this._timeStamp;
        }

        @JacksonXmlProperty(localName="TimeStamp")
        public void setTimeStamp(final String timeStamp) {
            this._timeStamp = timeStamp;
        }

        @JacksonXmlProperty(localName="Nonce")
        public String getNonce() {
            return this._nonce;
        }

        @JacksonXmlProperty(localName="Nonce")
        public void setNonce(final String nonce) {
            this._nonce = nonce;
        }


        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder();
            builder.append("ToWXEncryptedMessage [encrypt=").append(_encrypt).append(", msgSignature=")
                    .append(_msgSignature).append(", timeStamp=").append(_timeStamp).append(", nonce=").append(_nonce)
                    .append("]");
            return builder.toString();
        }


        private String _nonce;

        private String _timeStamp;

        private String _msgSignature;

        private String _encrypt;
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
    public ToWXEncryptedMessage encryptMsg(final String replyMsg, String timeStamp, final String nonce, final String appid) throws AesException;

    /**
     * 对密文进行解密.
     *
     * @param text 需要解密的密文
     * @return 解密得到的明文
     * @throws AesException aes解密失败
     */
    public String[] decrypt(final String text) throws AesException;
}
