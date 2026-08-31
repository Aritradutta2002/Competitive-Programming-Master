/*
 * Author  : Aritra Dutta
 * Target  : Codeforces Expert / CSES
 *
 * Primes & Sieve Library
 * Includes: Sieve of Eratosthenes, getPrimes, computeSPF, factorize,
 *           isPrime, Miller-Rabin, Pollard's Rho, Euler's Totient (phi,
 *           computePhi, sumPhi)
 *
 * USAGE: Copy this file into your solution or import specific methods
 */
import java.io.*;
import java.util.*;

public class PrimesAndSieve {
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
        // Example: check if a number is prime
        long n = nextLong();
        out.println(Primes.isPrimeMillerRabin(n));
    }

    // ==================== MODULAR ARITHMETIC (needed by Miller-Rabin) ====================
    static class ModMath {
        static long modMul(long a, long b, long m) { return ((a % m) * (b % m)) % m; }

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
    }

    // ==================== PRIME NUMBERS ====================
    static class Primes {
        // Sieve of Eratosthenes: returns boolean array where isPrime[i] = true if i is prime
        static boolean[] sieve(int n) {
            boolean[] isPrime = new boolean[n + 1];
            Arrays.fill(isPrime, true);
            isPrime[0] = isPrime[1] = false;

            for (int i = 2; i * i <= n; i++) {
                if (isPrime[i]) {
                    for (int j = i * i; j <= n; j += i) {
                        isPrime[j] = false;
                    }
                }
            }
            return isPrime;
        }

        // Returns list of primes up to n
        static List<Integer> getPrimes(int n) {
            boolean[] isPrime = sieve(n);
            List<Integer> primes = new ArrayList<>();
            for (int i = 2; i <= n; i++) {
                if (isPrime[i]) primes.add(i);
            }
            return primes;
        }

        // Smallest Prime Factor for each number up to n
        static int[] computeSPF(int n) {
            int[] spf = new int[n + 1];
            for (int i = 0; i <= n; i++) spf[i] = i;

            for (int i = 2; i * i <= n; i++) {
                if (spf[i] == i) {
                    for (int j = i * i; j <= n; j += i) {
                        if (spf[j] == j) spf[j] = i;
                    }
                }
            }
            return spf;
        }

        // Prime factorization using SPF - O(log n)
        static List<int[]> factorize(int n, int[] spf) {
            List<int[]> factors = new ArrayList<>();
            while (n > 1) {
                int p = spf[n], cnt = 0;
                while (n % p == 0) { n /= p; cnt++; }
                factors.add(new int[]{p, cnt});
            }
            return factors;
        }

        // Prime factorization without precomputation - O(sqrt(n))
        static List<int[]> factorize(int n) {
            List<int[]> factors = new ArrayList<>();
            for (int i = 2; i * i <= n; i++) {
                if (n % i == 0) {
                    int cnt = 0;
                    while (n % i == 0) { n /= i; cnt++; }
                    factors.add(new int[]{i, cnt});
                }
            }
            if (n > 1) factors.add(new int[]{n, 1});
            return factors;
        }

        // Check if n is prime in O(sqrt(n))
        static boolean isPrime(int n) {
            if (n < 2) return false;
            for (int i = 2; i * i <= n; i++) {
                if (n % i == 0) return false;
            }
            return true;
        }

        // Miller-Rabin primality test - O(k log³n)
        static boolean isPrimeMillerRabin(long n) {
            if (n < 2) return false;
            if (n == 2 || n == 3) return true;
            if (n % 2 == 0) return false;

            long d = n - 1;
            int s = 0;
            while (d % 2 == 0) { d /= 2; s++; }

            // Witnesses for deterministic test up to 2^64
            long[] witnesses = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37};

            for (long a : witnesses) {
                if (a >= n) break;
                if (!millerRabinTest(a, n, d, s)) return false;
            }
            return true;
        }

        static boolean millerRabinTest(long a, long n, long d, int s) {
            long x = ModMath.modPow(a, d, n);
            if (x == 1 || x == n - 1) return true;

            for (int r = 1; r < s; r++) {
                x = ModMath.modMul(x, x, n);
                if (x == n - 1) return true;
            }
            return false;
        }

        // Pollard's rho algorithm for factorization
        static long pollardRho(long n) {
            if (n % 2 == 0) return 2;
            long x = 2, y = 2, d = 1;

            while (d == 1) {
                x = (ModMath.modMul(x, x, n) + 1) % n;
                y = (ModMath.modMul(y, y, n) + 1) % n;
                y = (ModMath.modMul(y, y, n) + 1) % n;
                d = gcd(Math.abs(x - y), n);
            }
            return d;
        }

        static long gcd(long a, long b) { return b == 0 ? a : gcd(b, a % b); }
    }

    // ==================== EULER'S TOTIENT ====================
    static class Totient {
        // Euler's totient function φ(n) - O(sqrt(n))
        static long phi(long n) {
            long result = n;
            for (long i = 2; i * i <= n; i++) {
                if (n % i == 0) {
                    while (n % i == 0) n /= i;
                    result -= result / i;
                }
            }
            if (n > 1) result -= result / n;
            return result;
        }

        // Precompute totient for all numbers up to n - O(n log log n)
        static int[] computePhi(int n) {
            int[] phi = new int[n + 1];
            for (int i = 0; i <= n; i++) phi[i] = i;

            for (int i = 2; i <= n; i++) {
                if (phi[i] == i) { // i is prime
                    for (int j = i; j <= n; j += i) {
                        phi[j] -= phi[j] / i;
                    }
                }
            }
            return phi;
        }

        // Sum of φ(i) for i from 1 to n
        static long sumPhi(int n) {
            int[] phi = computePhi(n);
            long sum = 0;
            for (int i = 1; i <= n; i++) sum += phi[i];
            return sum;
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
