/*
 * Author  : Aritra Dutta
 * Target  : Codeforces Expert / CSES
 *
 * Matrix Operations & Transforms Library
 * Includes: Matrix multiplication & exponentiation, Fibonacci, Linear Recurrence,
 *           FFT (Fast Fourier Transform), NTT (Number Theoretic Transform),
 *           Gaussian Elimination (solve, determinant)
 *
 * USAGE: Copy this file into your solution or import specific methods
 */
import java.io.*;
import java.util.*;

public class MatrixAndTransforms {
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
        // Example: Fibonacci using matrix exponentiation
        long n = nextLong();
        out.println(Matrix.fibonacci(n));
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
