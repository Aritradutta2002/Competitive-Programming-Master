/*
 * Author  : Aritra Dutta
 * Target  : Codeforces Expert / CSES
 *
 * Combinatorics Library
 * Includes: Factorial precomputation, nCr, nPr, nCrLucas, catalan,
 *           stirling2, bell
 *
 * USAGE: Copy this file into your solution or import specific methods
 */
import java.io.*;
import java.util.*;

public class Combinatorics {
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
        // Example: nCr calculation
        int n = nextInt(), r = nextInt();
        out.println(Comb.nCr(n, r));
    }

    // DEPENDENCY: ModMath class included for convenience
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
    }

    // ==================== FACTORIALS & nCr ====================
    static class Comb {
        static final int MAXN = 2_000_001;
        static long[] fact = new long[MAXN];
        static long[] invFact = new long[MAXN];
        static boolean precomputed = false;

        static void precompute() {
            if (precomputed) return;
            fact[0] = 1;
            for (int i = 1; i < MAXN; i++) {
                fact[i] = Math.multiplyExact(fact[i - 1], i) % MOD;
            }
            invFact[MAXN - 1] = Math.pow(fact[MAXN - 1], MOD - 2) % MOD;
            invFact[MAXN - 1] = ModMath.modInv(fact[MAXN - 1]);
            for (int i = MAXN - 2; i >= 0; i--) {
                invFact[i] = ModMath.modMul(invFact[i + 1], i + 1);
            }
            precomputed = true;
        }

        // nCr in O(1) after precomputation
        static long nCr(int n, int r) {
            if (r < 0 || r > n) return 0;
            precompute();
            return ModMath.modMul(fact[n], ModMath.modMul(invFact[r], invFact[n - r]));
        }

        // nPr in O(1) after precomputation
        static long nPr(int n, int r) {
            if (r < 0 || r > n) return 0;
            precompute();
            return ModMath.modMul(fact[n], invFact[n - r]);
        }

        // nCr for large n using Lucas theorem (p must be prime)
        static long nCrLucas(int n, int r, int p) {
            if (r == 0) return 1;
            return ModMath.modMul(nCrLucas(n / p, r / p, p), nCrSmall(n % p, r % p, p), p);
        }

        static long nCrSmall(int n, int r, int p) {
            if (r > n) return 0;
            long num = 1, den = 1;
            for (int i = 0; i < r; i++) {
                num = ModMath.modMul(num, n - i, p);
                den = ModMath.modMul(den, i + 1, p);
            }
            return ModMath.modMul(num, ModMath.modInv(den, p), p);
        }

        // Catalan number: C(n) = (2n)! / ((n+1)! * n!)
        static long catalan(int n) {
            return ModMath.modMul(nCr(2 * n, n), ModMath.modInv(n + 1));
        }

        // Number of ways to partition n items into k non-empty sets (Stirling numbers of 2nd kind)
        static long stirling2(int n, int k) {
            if (k > n || k < 0) return 0;
            if (k == 0) return n == 0 ? 1 : 0;
            precompute();

            long result = 0;
            for (int i = 0; i <= k; i++) {
                long term = ModMath.modMul(ModMath.modPow(-1, k - i), nCr(k, i));
                term = ModMath.modMul(term, ModMath.modPow(i, n));
                result = ModMath.modAdd(result, term);
            }
            return ModMath.modMul(result, invFact[k]);
        }

        // Bell number: total number of partitions of n elements
        static long bell(int n) {
            precompute();
            long[] bell = new long[n + 1];
            bell[0] = 1;

            for (int i = 1; i <= n; i++) {
                for (int j = 0; j < i; j++) {
                    bell[i] = ModMath.modAdd(bell[i], ModMath.modMul(nCr(i - 1, j), bell[j]));
                }
            }
            return bell[n];
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
