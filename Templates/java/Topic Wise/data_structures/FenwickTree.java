/*
 * Author  : Aritra Dutta
 *
 * FENWICK TREE (Binary Indexed Tree) Library
 * Includes: BIT (1D Fenwick Tree), BIT2D (2D Fenwick Tree)
 *
 * Time Complexities:
 *   BIT   - Update O(log n), Query O(log n), LowerBound O(log n), FindKth O(log n)
 *   BIT2D - Update O(log n * log m), Query O(log n * log m)
 *
 * USAGE: Copy the inner classes you need into your solution
 */
import java.io.*;
import java.util.*;

public class FenwickTree {
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

    // ==================== BIT (Fenwick Tree) ====================
    static class BIT {
        int n;
        long[] tree;

        BIT(int n) {
            this.n = n;
            tree = new long[n + 1];
        }

        BIT(long[] arr) {
            this(arr.length);
            for (int i = 0; i < n; i++) update(i + 1, arr[i]);
        }

        // Add val to index i (1-indexed)
        void update(int i, long val) {
            for (; i <= n; i += i & (-i)) tree[i] += val;
        }

        // Sum of [1, i] (1-indexed)
        long query(int i) {
            long sum = 0;
            for (; i > 0; i -= i & (-i)) sum += tree[i];
            return sum;
        }

        // Sum of [l, r] (1-indexed)
        long query(int l, int r) {
            return query(r) - query(l - 1);
        }

        // Sum of [0, i] (0-indexed)
        long query0(int i) { return query(i + 1); }

        // Sum of [l, r] (0-indexed)
        long query0(int l, int r) { return query(l + 1, r + 1); }

        // Find smallest index with prefix sum >= val
        int lowerBound(long val) {
            int pos = 0;
            long sum = 0;
            for (int pw = Integer.highestOneBit(n); pw > 0; pw >>= 1) {
                if (pos + pw <= n && sum + tree[pos + pw] < val) {
                    pos += pw;
                    sum += tree[pos];
                }
            }
            return pos + 1;
        }

        // Find k-th smallest element (for frequency BIT)
        int findKth(int k) {
            int pos = 0;
            for (int pw = Integer.highestOneBit(n); pw > 0; pw >>= 1) {
                if (pos + pw <= n && tree[pos + pw] < k) {
                    pos += pw;
                    k -= tree[pos];
                }
            }
            return pos + 1;
        }
    }

    // ==================== 2D BIT ====================
    static class BIT2D {
        int n, m;
        long[][] tree;

        BIT2D(int n, int m) {
            this.n = n;
            this.m = m;
            tree = new long[n + 1][m + 1];
        }

        void update(int x, int y, long val) {
            for (int i = x; i <= n; i += i & (-i))
                for (int j = y; j <= m; j += j & (-j))
                    tree[i][j] += val;
        }

        long query(int x, int y) {
            long sum = 0;
            for (int i = x; i > 0; i -= i & (-i))
                for (int j = y; j > 0; j -= j & (-j))
                    sum += tree[i][j];
            return sum;
        }

        // Sum of rectangle [(x1,y1), (x2,y2)] (1-indexed)
        long query(int x1, int y1, int x2, int y2) {
            return query(x2, y2) - query(x1 - 1, y2) - query(x2, y1 - 1) + query(x1 - 1, y1 - 1);
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

    static long[] nextLongArray(int n) throws IOException {
        long[] arr = new long[n];
        for (int i = 0; i < n; i++) arr[i] = nextLong();
        return arr;
    }
}
