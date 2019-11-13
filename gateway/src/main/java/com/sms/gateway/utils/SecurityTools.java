package com.sms.gateway.utils;

import java.io.UnsupportedEncodingException;
import java.security.DigestException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;

/**
 * @author chenglutao on 2019/10/25
 */
public class SecurityTools {
	private static final byte[] salt = "webplat".getBytes();

	public static String digest(String str) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("SHA");
			md5.update(salt);
			return Base64.encode(md5.digest(str.getBytes()));
		} catch (NoSuchAlgorithmException ex) {
			throw new UnsupportedOperationException(ex.toString());
		}
	}

	public static void md5(byte[] data, int offset, int length, byte[] digest, int dOffset) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(data, offset, length);
			md5.digest(digest, dOffset, 16);
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		} catch (DigestException ex) {
			ex.printStackTrace();
		}
	}

	public static byte[] md5(byte[] data, int offset, int length) {
		byte[] arrayOfByte;
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(data, offset, length);
			return md5.digest();
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
			arrayOfByte = null;
		}
		return arrayOfByte;
	}

	public static byte[] encrypt(byte[] key, byte[] src) {
		try {
			return getCipher(key, 1).doFinal(src);
		} catch (BadPaddingException ex) {
			throw new UnsupportedOperationException(ex.toString());
		} catch (IllegalBlockSizeException ex) {
			throw new UnsupportedOperationException(ex.toString());
		}
	}

	public static String encrypt(String key, String src) {
		try {
			return Base64.encode(getCipher(key.getBytes("UTF8"), 1).doFinal(src.getBytes("UTF8")));
		} catch (UnsupportedEncodingException ex) {
			throw new UnsupportedOperationException(ex.toString());
		} catch (BadPaddingException ex) {
			throw new UnsupportedOperationException(ex.toString());
		} catch (IllegalBlockSizeException ex) {
			throw new UnsupportedOperationException(ex.toString());
		}
	}

	public static byte[] decrypt(byte[] key, byte[] src) {
		try {
			return getCipher(key, 2).doFinal(src);
		} catch (IllegalBlockSizeException ex) {
			throw new UnsupportedOperationException(ex.toString());
		} catch (BadPaddingException ex) {
			throw new UnsupportedOperationException(ex.toString());
		}
	}

	public static String decrypt(String key, String src) {
		try {
			return new String(getCipher(key.getBytes("UTF8"), 2).doFinal(Base64.decode(src)), "UTF8");
		} catch (UnsupportedEncodingException ex) {
			throw new UnsupportedOperationException(ex.toString());
		} catch (BadPaddingException ex) {
			throw new UnsupportedOperationException(ex.toString());
		} catch (IllegalBlockSizeException ex) {
			throw new UnsupportedOperationException(ex.toString());
		}
	}

	public static Cipher getCipher(byte[] key, int mode) {
		try {
			if (key.length < 8) {
				byte[] oldkey = key;
				key = new byte[8];
				System.arraycopy(oldkey, 0, key, 0, oldkey.length);
			}
			Cipher c;
			SecretKeyFactory keyFactory;
			KeySpec keySpec;
			if (key.length >= 24) {
				keyFactory = SecretKeyFactory.getInstance("DESede");
				keySpec = new DESedeKeySpec(key);
				c = Cipher.getInstance("DESede");
			} else {
				keyFactory = SecretKeyFactory.getInstance("DES");
				keySpec = new DESKeySpec(key);
				c = Cipher.getInstance("DES");
			}
			SecretKey k = keyFactory.generateSecret(keySpec);
			c.init(mode, k);
			return c;
		} catch (NoSuchAlgorithmException ex) {
			throw new UnsupportedOperationException(ex.toString());
		} catch (InvalidKeyException ex) {
			throw new UnsupportedOperationException(ex.toString());
		} catch (NoSuchPaddingException ex) {
			throw new UnsupportedOperationException(ex.toString());
		} catch (InvalidKeySpecException ex) {
			throw new UnsupportedOperationException(ex.toString());
		}
	}

	public static void main(String[] args) {
		Debug.dump(digest("hello world"));
		for (int i = 0; i < 1000; i++) {
			decrypt("key", encrypt("key", "hello world"));
		}
		Debug.dump(digest("hello world"));
	}
}