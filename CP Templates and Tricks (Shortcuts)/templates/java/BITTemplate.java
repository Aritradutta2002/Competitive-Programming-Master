/*
 * Author  : Aritra Dutta
 * Target  : Codeforces Expert / CSES
 * 
 * BIT (Fenwick Tree) Template - Point Update, Prefix Query
 * Uses optimized I/O for CSES acceptance
 */
import java.io.*;
import java.util.*;

public class BITTemplate {
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
        int n = nextInt(), q = nextInt();
        
        BIT bit = new BIT(n);
        for (int i = 1; i <= n; i++) {
            bit.update(i, nextLong());
        }
        
        StringBuilder sb = new StringBuilder();
        while (q-- > 0) {
            int type = nextInt();
            if (type == 1) {
                int idx = nextInt();
                long val = nextLong();
                // For point update (add val)
                bit.update(idx, val);
            } else {
                int l = nextInt(), r = nextInt();
                sb.append(bit.query(l, r)).append('\n');
            }
        }
        out.print(sb);
    }
    
    // ==================== BIT (Fenwick Tree) ====================
    static class BIT {
        int n;
        long[] tree;
        
        BIT(int n) {
            this.n = n;
            tree = new long[n + 1];
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
        
        // Sum of rectangle [(x1,y1), (x2,y2)]
        long query(int x1, int y1, int x2, int y2) {
            return query(x2, y2) - query(x1 - 1, y2) - query(x2, y1 - 1) + query(x1 - 1, y1 - 1);
        }
    }
    
    // ==================== Count Inversions ====================
    static long countInversions(int[] arr) {
        int n = arr.length;
        
        // Coordinate compression
        int[] sorted = arr.clone();
        Arrays.sort(sorted);
        Map<Integer, Integer> compress = new HashMap<>();
        int rank = 0;
        for (int x : sorted) {
            if (!compress.containsKey(x)) compress.put(x, ++rank);
        }
        
        BIT bit = new BIT(rank);
        long inversions = 0;
        
        for (int i = n - 1; i >= 0; i--) {
            int pos = compress.get(arr[i]);
            inversions += bit.query(pos - 1);
            bit.update(pos, 1);
        }
        return inversions;
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
