/**
 * 对公众平台发送给公众账号的消息加解密示例代码.
 *
 * @copyright Copyright (c) 1998-2014 Tencent Inc.
 */

// ------------------------------------------------------------------------

package org.jocean.wechat.service;

import java.security.MessageDigest;
import java.util.Arrays;

import org.jocean.idiom.ExceptionUtils;
import org.jocean.wechat.AesException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SHA1 class
 *
 * 计算公众平台的消息签名接口.
 */
class SHA1 {
    private static final Logger LOG =
            LoggerFactory.getLogger(SHA1.class);

	/**
	 * 用SHA1算法生成安全签名
	 * @param token 票据
	 * @param timestamp 时间戳
	 * @param nonce 随机字符串
	 * @param encrypt 密文
	 * @return 安全签名
	 * @throws AesException
	 */
	public static String getSHA1(final String token, final String timestamp, final String nonce, final String encrypt) throws AesException
			  {
		try {
			final String[] array = new String[] { token, timestamp, nonce, encrypt };
			final StringBuffer sb = new StringBuffer();
			// 字符串排序
			Arrays.sort(array);
			for (int i = 0; i < 4; i++) {
				sb.append(array[i]);
			}
			final String str = sb.toString();
			// SHA1签名生成
			final MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(str.getBytes());
			final byte[] digest = md.digest();

			final StringBuffer hexstr = new StringBuffer();
			String shaHex = "";
			for (int i = 0; i < digest.length; i++) {
				shaHex = Integer.toHexString(digest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexstr.append(0);
				}
				hexstr.append(shaHex);
			}
			return hexstr.toString();
		} catch (final Exception e) {
		    LOG.warn("exception when getSHA1, detail: {}", ExceptionUtils.exception2detail(e));
			throw new AesException(AesException.ComputeSignatureError);
		}
	}
}
