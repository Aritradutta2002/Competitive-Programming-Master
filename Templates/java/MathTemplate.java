/*
 * Author  : Aritra Dutta
 * Target  : Codeforces Expert / CSES
 * 
 * MATH Template - Number Theory, Combinatorics, Primes
 * Uses optimized I/O for CSES acceptance
 */
import java.io.*;
import java.util.*;

public class MathTemplate {
    static BufferedReader br;
    static StringTokenizer st;
    static PrintWriter out;
    
    static final int MOD = 1_000_000_007;
    static final int MAXN = 2_000_001;
    
    static long[] fact = new long[MAXN];
    static long[] invFact = new long[MAXN];
    
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        out = new PrintWriter(new BufferedOutputStream(System.out));
        
        precomputeFactorials();
        
        int t = nextInt();
        while (t-- > 0) {
            solve();
        }
        
        out.flush();
        out.close();
    }
    
    static void solve() throws IOException {
        int n = nextInt(), r = nextInt();
        out.println(nCr(n, r));
    }
    
    // ==================== Modular Arithmetic ====================
    static long modAdd(long a, long b) {
        return ((a % MOD) + (b % MOD)) % MOD;
    }
    
    static long modSub(long a, long b) {
        return ((a % MOD) - (b % MOD) + MOD) % MOD;
    }
    
    static long modMul(long a, long b) {
        return ((a % MOD) * (b % MOD)) % MOD;
    }
    
    static long modPow(long x, long y, long m) {
        long res = 1;
        x %= m;
        while (y > 0) {
            if ((y & 1) == 1) res = res * x % m;
            x = x * x % m;
            y >>= 1;
        }
        return res;
    }
    
    static long modPow(long x, long y) {
        return modPow(x, y, MOD);
    }
    
    static long modInv(long x) {
        return modPow(x, MOD - 2, MOD);
    }
    
    // ==================== Factorials & nCr ====================
    static void precomputeFactorials() {
        fact[0] = 1;
        for (int i = 1; i < MAXN; i++) {
            fact[i] = modMul(fact[i - 1], i);
        }
        invFact[MAXN - 1] = modInv(fact[MAXN - 1]);
        for (int i = MAXN - 2; i >= 0; i--) {
            invFact[i] = modMul(invFact[i + 1], i + 1);
        }
    }
    
    static long nCr(int n, int r) {
        if (r < 0 || r > n) return 0;
        return modMul(fact[n], modMul(invFact[r], invFact[n - r]));
    }
    
    static long nPr(int n, int r) {
        if (r < 0 || r > n) return 0;
        return modMul(fact[n], invFact[n - r]);
    }
    
    // ==================== GCD, LCM ====================
    static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }
    
    static long lcm(long a, long b) {
        return a / gcd(a, b) * b;
    }
    
    // ==================== Sieve of Eratosthenes ====================
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
    
    static List<Integer> getPrimes(int n) {
        boolean[] isPrime = sieve(n);
        List<Integer> primes = new ArrayList<>();
        for (int i = 2; i <= n; i++) {
            if (isPrime[i]) primes.add(i);
        }
        return primes;
    }
    
    // ==================== Smallest Prime Factor ====================
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
    
    // ==================== Euler's Totient ====================
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
    
    // ==================== Matrix Exponentiation ====================
    static long[][] matMul(long[][] A, long[][] B) {
        int n = A.length;
        long[][] C = new long[n][n];
        for (int i = 0; i < n; i++) {
            for (int k = 0; k < n; k++) {
                for (int j = 0; j < n; j++) {
                    C[i][j] = modAdd(C[i][j], modMul(A[i][k], B[k][j]));
                }
            }
        }
        return C;
    }
    
    static long[][] matPow(long[][] A, long p) {
        int n = A.length;
        long[][] result = new long[n][n];
        for (int i = 0; i < n; i++) result[i][i] = 1;  // Identity
        
        while (p > 0) {
            if ((p & 1) == 1) result = matMul(result, A);
            A = matMul(A, A);
            p >>= 1;
        }
        return result;
    }
    
    // Fibonacci using matrix exponentiation - O(log n)
    static long fib(long n) {
        if (n <= 1) return n;
        long[][] base = {{1, 1}, {1, 0}};
        long[][] result = matPow(base, n - 1);
        return result[0][0];
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
