package com.sms.gateway.utils;

/**
 * @author chenglutao on 2019/10/25
 */
public class TypeConvert {
	public static int byte2int(byte[] b, int offset) {
		return b[(offset + 3)] & 0xFF | (b[(offset + 2)] & 0xFF) << 8 | (b[(offset + 1)] & 0xFF) << 16
				| (b[offset] & 0xFF) << 24;
	}

	public static int byte2int(byte[] b) {
		return b[3] & 0xFF | (b[2] & 0xFF) << 8 | (b[1] & 0xFF) << 16 | (b[0] & 0xFF) << 24;
	}

	public static long byte2long(byte[] b) {
		return b[7] & 'ÿ' | (b[6] & 'ÿ') << 8 | (b[5] & 'ÿ') << 16 | (b[4] & 'ÿ') << 24 | (b[3] & 'ÿ') << 32
				| (b[2] & 'ÿ') << 40 | (b[1] & 'ÿ') << 48 | b[0] << 56;
	}

	public static long byte2long(byte[] b, int offset) {
		return b[(offset + 7)] & 'ÿ' | (b[(offset + 6)] & 'ÿ') << 8 | (b[(offset + 5)] & 'ÿ') << 16
				| (b[(offset + 4)] & 'ÿ') << 24 | (b[(offset + 3)] & 'ÿ') << 32 | (b[(offset + 2)] & 'ÿ') << 40
				| (b[(offset + 1)] & 'ÿ') << 48 | b[offset] << 56;
	}

	public static byte[] int2byte(int n) {
		byte[] b = new byte[4];
		b[0] = ((byte) (n >> 24));
		b[1] = ((byte) (n >> 16));
		b[2] = ((byte) (n >> 8));
		b[3] = ((byte) n);
		return b;
	}

	public static void int2byte(int n, byte[] buf, int offset) {
		buf[offset] = ((byte) (n >> 24));
		buf[(offset + 1)] = ((byte) (n >> 16));
		buf[(offset + 2)] = ((byte) (n >> 8));
		buf[(offset + 3)] = ((byte) n);
	}

	public static byte[] short2byte(int n) {
		byte[] b = new byte[2];
		b[0] = ((byte) (n >> 8));
		b[1] = ((byte) n);
		return b;
	}

	public static void short2byte(int n, byte[] buf, int offset) {
		buf[offset] = ((byte) (n >> 8));
		buf[(offset + 1)] = ((byte) n);
	}

	public static byte[] long2byte(long n) {
		byte[] b = new byte[8];

		b[0] = ((byte) (int) (n >> 56));
		b[1] = ((byte) (int) (n >> 48));
		b[2] = ((byte) (int) (n >> 40));
		b[3] = ((byte) (int) (n >> 32));
		b[4] = ((byte) (int) (n >> 24));
		b[5] = ((byte) (int) (n >> 16));
		b[6] = ((byte) (int) (n >> 8));
		b[7] = ((byte) (int) n);
		return b;
	}

	public static void long2byte(long n, byte[] buf, int offset) {
		buf[offset] = ((byte) (int) (n >> 56));
		buf[(offset + 1)] = ((byte) (int) (n >> 48));
		buf[(offset + 2)] = ((byte) (int) (n >> 40));
		buf[(offset + 3)] = ((byte) (int) (n >> 32));
		buf[(offset + 4)] = ((byte) (int) (n >> 24));
		buf[(offset + 5)] = ((byte) (int) (n >> 16));
		buf[(offset + 6)] = ((byte) (int) (n >> 8));
		buf[(offset + 7)] = ((byte) (int) n);
	}

	/**
	 * @param byteArray
	 * @return
	 */
	public static String toHexString(byte[] byteArray) {
		final StringBuilder hexString = new StringBuilder("");
		if (byteArray == null || byteArray.length <= 0)
			return null;
		for (int i = 0; i < byteArray.length; i++) {
			int v = byteArray[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				hexString.append(0);
			}
			hexString.append(hv);
		}
		return hexString.toString().toLowerCase();
	}

	/**
	 * convert byte[] to HexString
	 * 
	 * @param bArray
	 * @param length
	 * @return
	 */
	public static String bytesToHexString(byte[] bArray, int length) {
		StringBuffer sb = new StringBuffer(length);
		String sTemp;
		for (int i = 0; i < length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}

	public static long byteToLong(byte[] b) {
		long s = 0;
		long s0 = b[0] & 0xff;// 最低位
		long s1 = b[1] & 0xff;
		long s2 = b[2] & 0xff;
		long s3 = b[3] & 0xff;
		long s4 = b[4] & 0xff;// 最低位
		long s5 = b[5] & 0xff;
		long s6 = b[6] & 0xff;
		long s7 = b[7] & 0xff;
		// s0不变
		s1 <<= 8;
		s2 <<= 16;
		s3 <<= 24;
		s4 <<= 8 * 4;
		s5 <<= 8 * 5;
		s6 <<= 8 * 6;
		s7 <<= 8 * 7;
		s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
		return s;
	}

	public static byte[] longToByte(long number) {
		long temp = number;
		byte[] b = new byte[8];
		for (int i = 0; i < b.length; i++) {
			b[i] = new Long(temp & 0xff).byteValue();// 将最低位保存在最低位
			temp = temp >> 8; // 向右移8位
		}
		return b;
	}

	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase();
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}

	public static void printHexString(byte[] b) {
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			System.out.print(hex.toUpperCase());
		}
	}
}