/*
 * Author  : Aritra Dutta
 * Target  : Codeforces Expert / CSES
 *
 * ADVANCED MATH Template - Complete Number Theory & Combinatorics Library
 * Includes: Modular Arithmetic, Primes, Factorials, nCr, GCD, Matrix Exp,
 *           FFT, NTT, Gaussian Elimination, Linear Recurrence, etc.
 *
 * USAGE: Import specific classes or copy algorithms you need
 */
import java.io.*;
import java.util.*;

public class MathTemplate {
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
        out.println(Combinatorics.nCr(n, r));
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

    // ==================== FACTORIALS & nCr ====================
    static class Combinatorics {
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

    // ==================== MATRIX OPERATIONS ====================
    static class Matrix {
        // Matrix multiplication: O(n³)
        static long[][] multiply(long[][] A, long[][] B, long mod) {
            int n = A.length, m = B[0].length, p = B.length;
            long[][] C = new long[n][m];

            for (int i = 0; i < n; i++) {
                for (int k = 0; k < p; k++) {
                    if (A[i][k] == 0) continue;
                    for (int j = 0; j < m; j++) {
                        C[i][j] = ModMath.modAdd(C[i][j], ModMath.modMul(A[i][k], B[k][j], mod), mod);
                    }
                }
            }
            return C;
        }

        static long[][] multiply(long[][] A, long[][] B) { return multiply(A, B, MOD); }

        // Matrix exponentiation: O(n³ log p)
        static long[][] power(long[][] A, long p, long mod) {
            int n = A.length;
            long[][] result = new long[n][n];
            for (int i = 0; i < n; i++) result[i][i] = 1;

            while (p > 0) {
                if ((p & 1) == 1) result = multiply(result, A, mod);
                A = multiply(A, A, mod);
                p >>= 1;
            }
            return result;
        }

        static long[][] power(long[][] A, long p) { return power(A, p, MOD); }

        // Fibonacci using matrix exponentiation: O(log n)
        static long fibonacci(long n) {
            if (n <= 1) return n;
            long[][] base = {{1, 1}, {1, 0}};
            long[][] result = power(base, n - 1);
            return result[0][0];
        }

        // Fibonacci modulo m
        static long fibonacci(long n, long mod) {
            if (n <= 1) return n % mod;
            long[][] base = {{1, 1}, {1, 0}};
            long[][] result = power(base, n - 1, mod);
            return result[0][0] % mod;
        }

        // Linear recurrence solver using matrix exponentiation
        // rec: coefficients, init: initial values, returns n-th term
        static long linearRecurrence(long[] rec, long[] init, long n, long mod) {
            int k = rec.length;
            if (n < init.length) return init[(int) n] % mod;

            long[][] matrix = new long[k][k];
            for (int i = 0; i < k; i++) matrix[0][i] = rec[i];
            for (int i = 1; i < k; i++) matrix[i][i - 1] = 1;

            long[][] result = power(matrix, n - k + 1, mod);

            long ans = 0;
            for (int i = 0; i < k; i++) {
                ans = ModMath.modAdd(ans, ModMath.modMul(result[0][i], init[k - 1 - i], mod), mod);
            }
            return ans;
        }
    }

    // ==================== FAST FOURIER TRANSFORM ====================
    static class FFT {
        static final double PI = Math.PI;

        // Complex number class
        static class Complex {
            double real, imag;
            Complex(double r, double i) { real = r; imag = i; }
            Complex operator+(Complex o) { return new Complex(real + o.real, imag + o.imag); }
            Complex operator-(Complex o) { return new Complex(real - o.real, imag - o.imag); }
            Complex operator*(Complex o) {
                return new Complex(real * o.real - imag * o.imag, real * o.imag + imag * o.real);
            }
        }

        // FFT - O(n log n)
        static void fft(Complex[] a, boolean invert) {
            int n = a.length;

            // Bit-reversal permutation
            for (int i = 1, j = 0; i < n; i++) {
                int bit = n >> 1;
                for (; (j & bit) != 0; bit >>= 1) j ^= bit;
                j ^= bit;
                if (i < j) { Complex t = a[i]; a[i] = a[j]; a[j] = t; }
            }

            // Cooley-Tukey FFT
            for (int len = 2; len <= n; len <<= 1) {
                double ang = 2 * PI / len * (invert ? -1 : 1);
                Complex wlen = new Complex(Math.cos(ang), Math.sin(ang));

                for (int i = 0; i < n; i += len) {
                    Complex w = new Complex(1, 0);
                    for (int j = 0; j < len / 2; j++) {
                        Complex u = a[i + j], v = a[i + j + len / 2].operator*(w);
                        a[i + j] = u.operator+(v);
                        a[i + j + len / 2] = u.operator-(v);
                        w = w.operator*(wlen);
                    }
                }
            }

            if (invert) {
                for (int i = 0; i < n; i++) {
                    a[i].real /= n;
                    a[i].imag /= n;
                }
            }
        }

        // Polynomial multiplication using FFT - O(n log n)
        static long[] multiply(long[] A, long[] B) {
            int n = 1;
            while (n < A.length + B.length) n <<= 1;

            Complex[] fa = new Complex[n], fb = new Complex[n];
            for (int i = 0; i < n; i++) {
                fa[i] = new Complex(i < A.length ? A[i] : 0, 0);
                fb[i] = new Complex(i < B.length ? B[i] : 0, 0);
            }

            fft(fa, false);
            fft(fb, false);

            for (int i = 0; i < n; i++) fa[i] = fa[i].operator*(fb[i]);

            fft(fa, true);

            long[] result = new long[A.length + B.length - 1];
            for (int i = 0; i < result.length; i++) {
                result[i] = Math.round(fa[i].real);
            }
            return result;
        }
    }

    // ==================== NUMBER THEORETIC TRANSFORM ====================
    static class NTT {
        static final int MOD = 998244353;
        static final int G = 3;

        static long power(long x, long y) {
            return ModMath.modPow(x, y, MOD);
        }

        static long modInv(long x) {
            return power(x, MOD - 2);
        }

        static void ntt(long[] a, boolean invert) {
            int n = a.length;

            // Bit-reversal
            for (int i = 1, j = 0; i < n; i++) {
                int bit = n >> 1;
                for (; (j & bit) != 0; bit >>= 1) j ^= bit;
                j ^= bit;
                if (i < j) { long t = a[i]; a[i] = a[j]; a[j] = t; }
            }

            for (int len = 2; len <= n; len <<= 1) {
                long wlen = power(G, (MOD - 1) / len);
                if (invert) wlen = modInv(wlen);

                for (int i = 0; i < n; i += len) {
                    long w = 1;
                    for (int j = 0; j < len / 2; j++) {
                        long u = a[i + j], v = a[i + j + len / 2] * w % MOD;
                        a[i + j] = (u + v) % MOD;
                        a[i + j + len / 2] = (u - v + MOD) % MOD;
                        w = w * wlen % MOD;
                    }
                }
            }

            if (invert) {
                long nInv = modInv(n);
                for (int i = 0; i < n; i++) {
                    a[i] = a[i] * nInv % MOD;
                }
            }
        }

        static long[] multiply(long[] A, long[] B) {
            int n = 1;
            while (n < A.length + B.length) n <<= 1;

            long[] fa = Arrays.copyOf(A, n);
            long[] fb = Arrays.copyOf(B, n);

            ntt(fa, false);
            ntt(fb, false);

            for (int i = 0; i < n; i++) fa[i] = fa[i] * fb[i] % MOD;

            ntt(fa, true);

            long[] result = new long[A.length + B.length - 1];
            System.arraycopy(fa, 0, result, 0, result.length);
            return result;
        }
    }

    // ==================== GAUSSIAN ELIMINATION ====================
    static class GaussianElimination {
        // Solve system of linear equations Ax = b
        // Returns null if no unique solution
        static double[] solve(double[][] A, double[] b) {
            int n = A.length;
            double[][] augmented = new double[n][n + 1];

            for (int i = 0; i < n; i++) {
                System.arraycopy(A[i], 0, augmented[i], 0, n);
                augmented[i][n] = b[i];
            }

            // Forward elimination
            for (int i = 0; i < n; i++) {
                // Find pivot
                int pivot = i;
                for (int j = i + 1; j < n; j++) {
                    if (Math.abs(augmented[j][i]) > Math.abs(augmented[pivot][i])) {
                        pivot = j;
                    }
                }

                // Swap rows
                double[] temp = augmented[i];
                augmented[i] = augmented[pivot];
                augmented[pivot] = temp;

                if (Math.abs(augmented[i][i]) < 1e-10) return null; // No unique solution

                // Eliminate column
                for (int j = i + 1; j < n; j++) {
                    double factor = augmented[j][i] / augmented[i][i];
                    for (int k = i; k <= n; k++) {
                        augmented[j][k] -= factor * augmented[i][k];
                    }
                }
            }

            // Back substitution
            double[] x = new double[n];
            for (int i = n - 1; i >= 0; i--) {
                x[i] = augmented[i][n];
                for (int j = i + 1; j < n; j++) {
                    x[i] -= augmented[i][j] * x[j];
                }
                x[i] /= augmented[i][i];
            }

            return x;
        }

        // Calculate determinant
        static double determinant(double[][] A) {
            int n = A.length;
            double[][] mat = new double[n][n];
            for (int i = 0; i < n; i++) mat[i] = A[i].clone();

            double det = 1;
            for (int i = 0; i < n; i++) {
                int pivot = i;
                for (int j = i + 1; j < n; j++) {
                    if (Math.abs(mat[j][i]) > Math.abs(mat[pivot][i])) pivot = j;
                }

                if (pivot != i) {
                    double[] temp = mat[i];
                    mat[i] = mat[pivot];
                    mat[pivot] = temp;
                    det *= -1;
                }

                if (Math.abs(mat[i][i]) < 1e-10) return 0;

                det *= mat[i][i];

                for (int j = i + 1; j < n; j++) {
                    double factor = mat[j][i] / mat[i][i];
                    for (int k = i + 1; k < n; k++) {
                        mat[j][k] -= factor * mat[i][k];
                    }
                }
            }
            return det;
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
