/*
 * Author  : Aritra Dutta
 * Target  : Codeforces Expert / CSES
 *
 * Modular Arithmetic Library
 * Includes: modAdd, modSub, modMul, modPow, modInv, modDiv, extendedGCD,
 *           modInvGeneral, CRT, discreteLog
 *
 * USAGE: Copy this file into your solution or import specific methods
 */
import java.io.*;
import java.util.*;

public class ModArithmetic {
    static BufferedReader br;
    static StringTokenizer st;
    static PrintWriter out;

    static final int MOD = 1_000_000_007;
    static final int MOD2 = 998244353; // NTT-friendly prime
    static final long INF = (long) 1e18;

    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        out = new PrintWriter(new BufferedOutputStream(System.out));
        solve();
        out.flush();
        out.close();
    }

    static void solve() throws IOException {
        // Example: modular exponentiation
        long a = nextLong(), b = nextLong();
        out.println(ModMath.modPow(a, b));
    }

    // ==================== MODULAR ARITHMETIC ====================
    static class ModMath {
        // Basic operations
        static long modAdd(long a, long b, long m) { return ((a % m) + (b % m)) % m; }
        static long modSub(long a, long b, long m) { return ((a % m) - (b % m) + m) % m; }
        static long modMul(long a, long b, long m) { return ((a % m) * (b % m)) % m; }

        static long modAdd(long a, long b) { return modAdd(a, b, MOD); }
        static long modSub(long a, long b) { return modSub(a, b, MOD); }
        static long modMul(long a, long b) { return modMul(a, b, MOD); }

        // Modular exponentiation: x^y % m in O(log y)
        static long modPow(long x, long y, long m) {
            long res = 1;
            x %= m;
            while (y > 0) {
                if ((y & 1) == 1) res = modMul(res, x, m);
                x = modMul(x, x, m);
                y >>= 1;
            }
            return res;
        }

        static long modPow(long x, long y) { return modPow(x, y, MOD); }

        // Modular inverse using Fermat's little theorem (m must be prime)
        static long modInv(long x, long m) { return modPow(x, m - 2, m); }
        static long modInv(long x) { return modInv(x, MOD); }

        // Modular division
        static long modDiv(long a, long b, long m) { return modMul(a, modInv(b, m), m); }
        static long modDiv(long a, long b) { return modDiv(a, b, MOD); }

        // Extended Euclidean Algorithm: returns {gcd, x, y} such that ax + by = gcd
        static long[] extendedGCD(long a, long b) {
            if (b == 0) return new long[]{a, 1, 0};
            long[] res = extendedGCD(b, a % b);
            return new long[]{res[0], res[2], res[1] - (a / b) * res[2]};
        }

        // Modular inverse for non-prime modulus (returns -1 if doesn't exist)
        static long modInvGeneral(long a, long m) {
            long[] res = extendedGCD(a, m);
            if (res[0] != 1) return -1;
            return ((res[1] % m) + m) % m;
        }

        // Chinese Remainder Theorem
        static long crt(long[] rems, long[] mods) {
            int n = rems.length;
            long product = 1;
            for (long m : mods) product *= m;

            long result = 0;
            for (int i = 0; i < n; i++) {
                long p = product / mods[i];
                result = modAdd(result, modMul(modMul(rems[i], modInvGeneral(p, mods[i])), p, product), product);
            }
            return result;
        }

        // Discrete logarithm: find smallest x such that a^x ≡ b (mod m)
        static long discreteLog(long a, long b, long m) {
            a %= m; b %= m;
            int n = (int) Math.sqrt(m) + 1;
            Map<Long, Integer> table = new HashMap<>();

            long an = 1;
            for (int i = 0; i < n; i++) {
                an = modMul(an, a, m);
            }

            long cur = b;
            for (int i = 0; i < n; i++) {
                table.put(cur, i);
                cur = modMul(cur, a, m);
            }

            cur = 1;
            for (int i = 1; i <= n; i++) {
                cur = modMul(cur, an, m);
                if (table.containsKey(cur)) {
                    return (long) i * n - table.get(cur);
                }
            }
            return -1;
        }
    }

    // ==================== FAST I/O ====================
    static String next() throws IOException {
        while (st == null || !st.hasMoreTokens())
            st = new StringTokenizer(br.readLine());
        return st.nextToken();
    }

    static int nextInt() throws IOException { return Integer.parseInt(next()); }
    static long nextLong() throws IOException { return Long.parseLong(next()); }
}
