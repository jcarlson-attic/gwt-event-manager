package dojox.encoding.digests;

public class SHA1 {

	private static final int chrsz = 8;
	private static final int mask = (1 << chrsz) - 1;

	private SHA1() {

	}

	public static String hmac(String data, String key) {
		int[] wa = SHA1.toWord(key);

		if (wa.length > 16) {
			wa = SHA1.core(wa, key.length() * chrsz);
		}

		int[] ipad = new int[16];
		int[] opad = new int[16];
		for (int i = 0; i < 16; i++) {
			ipad[i] = (i < wa.length ? wa[i] : 0) ^ 0x36363636;
			opad[i] = (i < wa.length ? wa[i] : 0) ^ 0x5c5c5c5c;
		}

		int[] i = SHA1.toWord(data);
		int len = 512 + data.length() * chrsz;
		int[] idest = new int[((len + 64 >> 9) << 4) + 16];
		System.arraycopy(ipad, 0, idest, 0, ipad.length);
		System.arraycopy(i, 0, idest, ipad.length, i.length);
		int[] hash = SHA1.core(idest, 512 + data.length() * chrsz);

		int[] odest = new int[((512 + 160 + 64 >> 9) << 4) + 16];
		System.arraycopy(opad, 0, odest, 0, opad.length);
		System.arraycopy(hash, 0, odest, opad.length, hash.length);
		int[] hmac = SHA1.core(odest, 512 + 160);

		return SHA1.toBase64(hmac); // string
	}

	private static int[] toWord(String key) {
		int l = key.length() * SHA1.chrsz;
			int[] wa = new int[(l >> 5) + 1];

		for (int i = 0; i < l; i += SHA1.chrsz) {
			wa[i >> 5] |= (key.charAt(i / chrsz) & mask) << (32 - chrsz - i % 32);
		}
		return wa; // word[]
	}

	private static int r(int n, int c) {
		return (n << c) | (n >>> (32 - c));
	}

	private static int ft(int t, int b, int c, int d) {
		if (t < 20) {
			return (b & c) | ((~b) & d);
		}
		if (t < 40) {
			return b ^ c ^ d;
		}
		if (t < 60) {
			return (b & c) | (b & d) | (c & d);
		}
		return b ^ c ^ d;
	}

	private static int kt(int t) {
		return (t < 20) ? 1518500249 : (t < 40) ? 1859775393
				: (t < 60) ? -1894007588 : -899497514;
	}

	private static int[] core(int[] x, int len) {
		x[len >> 5] |= 0x80 << (24 - len % 32);
		x[((len + 64 >> 9) << 4) + 15] = len;

		int[] w = new int[80];
		int[] ret = new int[5];
		ret[0] = 1732584193;
		ret[1] = -271733879;
		ret[2] = -1732584194;
		ret[3] = 271733878;
		ret[4] = -1009589776;

		
		for (int i = 0; i < x.length; i += 16) {
			int olda = ret[0];
			int oldb = ret[1];
			int oldc = ret[2];
			int oldd = ret[3];
			int olde = ret[4];
			for (int j = 0; j < 80; j++) {
				if (j < 16) {
					w[j] = x[i + j];
				} else {
					w[j] = SHA1.r(w[j - 3] ^ w[j - 8] ^ w[j - 14] ^ w[j - 16],
							1);
				}
				int t = SHA1.addWords(SHA1.addWords(SHA1.r(ret[0], 5), SHA1.ft(j, ret[1],
						ret[2], ret[3])), SHA1.addWords(SHA1.addWords(ret[4], w[j]), SHA1
						.kt(j)));
				ret[4] = ret[3];
				ret[3] = ret[2];
				ret[2] = SHA1.r(ret[1], 30);
				ret[1] = ret[0];
				ret[0] = t;
			}
			ret[0] = SHA1.addWords(ret[0], olda);
			ret[1] = SHA1.addWords(ret[1], oldb);
			ret[2] = SHA1.addWords(ret[2], oldc);
			ret[3] = SHA1.addWords(ret[3], oldd);
			ret[4] = SHA1.addWords(ret[4], olde);
		}
		return ret;
	}

	private static int addWords(int a, int b) {
		int l = (a & 0xFFFF) + (b & 0xFFFF);
		int m = (a >> 16) + (b >> 16) + (l >> 16);
		return (m << 16) | (l & 0xFFFF); // word
	}

	private static String toBase64(int[] wa) {
		char p = '=';
		String tab = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
		StringBuffer s = new StringBuffer();
		int l = wa.length * 4;
		for (int i = 0; i < l; i += 3) {
			int t1 = i >> 2 < wa.length ? (((wa[i >> 2] >> 8 * (3 - i % 4)) & 0xFF) << 16) : 0;
			int t2 = i + 1 >> 2 < wa.length ? (((wa[i + 1 >> 2] >> 8 * (3 - (i + 1) % 4)) & 0xFF) << 8) : 0;
			int t3 = i + 2 >> 2 < wa.length ? ((wa[i + 2 >> 2] >> 8 * (3 - (i + 2) % 4)) & 0xFF) : 0;
			int t = t1 | t2 | t3;
			for (int j = 0; j < 4; j++) {
				if (i * 8 + j * 6 > wa.length * 32) {
					s.append(p);
				} else {
					s.append(tab.charAt((t >> 6 * (3 - j)) & 0x3F));
				}
			}
		}
		return s.toString(); // string
	}

}
