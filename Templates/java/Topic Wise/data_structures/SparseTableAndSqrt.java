/*
 * Author  : Aritra Dutta
 *
 * SPARSE TABLE, SQRT DECOMPOSITION & COORDINATE COMPRESSION Library
 * Includes: SparseTable (RMQ), SparseTableGCD, SqrtDecomposition, CoordinateCompression
 *
 * Time Complexities:
 *   SparseTable          - Build O(n log n), Query O(1)
 *   SparseTableGCD       - Build O(n log n), Query O(1)
 *   SqrtDecomposition    - Build O(n), Update O(1), Query O(sqrt n), Range Update O(sqrt n)
 *   CoordinateCompression - Build O(n log n), Lookup O(1)
 *
 * USAGE: Copy the inner classes you need into your solution
 */
import java.io.*;
import java.util.*;

public class SparseTableAndSqrt {
    static BufferedReader br;
    static StringTokenizer st;
    static PrintWriter out;

    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        out = new PrintWriter(new BufferedOutputStream(System.out));
        solve();
        out.flush();
        out.close();
    }

    static void solve() throws IOException {
        // Example usage
    }

    // ==================== SPARSE TABLE (RMQ) ====================
    static class SparseTable {
        int n, LOG;
        long[][] table;
        int[] logs;
        static final long INF = Long.MAX_VALUE;

        SparseTable(long[] arr) {
            n = arr.length;
            LOG = 32 - Integer.numberOfLeadingZeros(n);
            table = new long[n][LOG];
            logs = new int[n + 1];

            // Precompute logs
            logs[1] = 0;
            for (int i = 2; i <= n; i++) logs[i] = logs[i / 2] + 1;

            // Initialize
            for (int i = 0; i < n; i++) table[i][0] = arr[i];

            // Build table
            for (int j = 1; j < LOG; j++) {
                for (int i = 0; i + (1 << j) <= n; i++) {
                    table[i][j] = Math.min(table[i][j - 1], table[i + (1 << (j - 1))][j - 1]);
                }
            }
        }

        // Range Minimum Query in O(1)
        long query(int l, int r) {
            int len = r - l + 1;
            int k = logs[len];
            return Math.min(table[l][k], table[r - (1 << k) + 1][k]);
        }

        // Range Maximum Query
        long queryMax(int l, int r) {
            int len = r - l + 1;
            int k = logs[len];
            return Math.max(table[l][k], table[r - (1 << k) + 1][k]);
        }
    }

    // ==================== SPARSE TABLE FOR GCD ====================
    static class SparseTableGCD {
        int n, LOG;
        long[][] table;
        int[] logs;

        SparseTableGCD(long[] arr) {
            n = arr.length;
            LOG = 32 - Integer.numberOfLeadingZeros(n);
            table = new long[n][LOG];
            logs = new int[n + 1];

            logs[1] = 0;
            for (int i = 2; i <= n; i++) logs[i] = logs[i / 2] + 1;

            for (int i = 0; i < n; i++) table[i][0] = arr[i];

            for (int j = 1; j < LOG; j++) {
                for (int i = 0; i + (1 << j) <= n; i++) {
                    table[i][j] = gcd(table[i][j - 1], table[i + (1 << (j - 1))][j - 1]);
                }
            }
        }

        long query(int l, int r) {
            int len = r - l + 1;
            int k = logs[len];
            return gcd(table[l][k], table[r - (1 << k) + 1][k]);
        }

        long gcd(long a, long b) { return b == 0 ? a : gcd(b, a % b); }
    }

    // ==================== SQRT DECOMPOSITION ====================
    static class SqrtDecomposition {
        int n, blockSize;
        long[] arr, blockSum;

        SqrtDecomposition(long[] arr) {
            this.arr = arr.clone();
            n = arr.length;
            blockSize = (int) Math.sqrt(n) + 1;
            blockSum = new long[blockSize];

            for (int i = 0; i < n; i++) {
                blockSum[i / blockSize] += arr[i];
            }
        }

        void update(int idx, long val) {
            blockSum[idx / blockSize] -= arr[idx];
            arr[idx] = val;
            blockSum[idx / blockSize] += arr[idx];
        }

        long query(int l, int r) {
            long sum = 0;
            int startBlock = l / blockSize;
            int endBlock = r / blockSize;

            if (startBlock == endBlock) {
                for (int i = l; i <= r; i++) sum += arr[i];
            } else {
                // Left partial block
                for (int i = l; i < (startBlock + 1) * blockSize; i++) sum += arr[i];
                // Full blocks
                for (int b = startBlock + 1; b < endBlock; b++) sum += blockSum[b];
                // Right partial block
                for (int i = endBlock * blockSize; i <= r; i++) sum += arr[i];
            }
            return sum;
        }

        // Range update (add val to all elements in [l, r])
        void updateRange(int l, int r, long val) {
            int startBlock = l / blockSize;
            int endBlock = r / blockSize;

            if (startBlock == endBlock) {
                for (int i = l; i <= r; i++) {
                    blockSum[i / blockSize] -= arr[i];
                    arr[i] += val;
                    blockSum[i / blockSize] += arr[i];
                }
            } else {
                // Left partial block
                for (int i = l; i < (startBlock + 1) * blockSize; i++) {
                    blockSum[i / blockSize] -= arr[i];
                    arr[i] += val;
                    blockSum[i / blockSize] += arr[i];
                }
                // Full blocks
                for (int b = startBlock + 1; b < endBlock; b++) {
                    blockSum[b] += val * blockSize;
                }
                // Right partial block
                for (int i = endBlock * blockSize; i <= r; i++) {
                    blockSum[i / blockSize] -= arr[i];
                    arr[i] += val;
                    blockSum[i / blockSize] += arr[i];
                }
            }
        }
    }

    // ==================== COORDINATE COMPRESSION ====================
    static class CoordinateCompression {
        int[] compressed;
        Map<Integer, Integer> toCompressed;
        Map<Integer, Integer> toOriginal;

        CoordinateCompression(int[] arr) {
            int n = arr.length;
            compressed = new int[n];
            toCompressed = new HashMap<>();
            toOriginal = new HashMap<>();

            int[] sorted = arr.clone();
            Arrays.sort(sorted);

            int rank = 0;
            for (int i = 0; i < n; i++) {
                if (i == 0 || sorted[i] != sorted[i - 1]) {
                    toCompressed.put(sorted[i], rank);
                    toOriginal.put(rank, sorted[i]);
                    rank++;
                }
            }

            for (int i = 0; i < n; i++) {
                compressed[i] = toCompressed.get(arr[i]);
            }
        }

        int getCompressed(int original) { return toCompressed.get(original); }
        int getOriginal(int compressed) { return toOriginal.get(compressed); }
        int[] getCompressedArray() { return compressed; }
    }

    // ==================== FAST I/O ====================
    static String next() throws IOException {
        while (st == null || !st.hasMoreTokens())
            st = new StringTokenizer(br.readLine());
        return st.nextToken();
    }

    static int nextInt() throws IOException { return Integer.parseInt(next()); }
    static long nextLong() throws IOException { return Long.parseLong(next()); }

    static long[] nextLongArray(int n) throws IOException {
        long[] arr = new long[n];
        for (int i = 0; i < n; i++) arr[i] = nextLong();
        return arr;
    }
}
