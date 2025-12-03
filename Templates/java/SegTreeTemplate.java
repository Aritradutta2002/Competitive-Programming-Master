/*
 * Author  : Aritra Dutta
 * Target  : Codeforces Expert / CSES
 * 
 * SEGMENT TREE Template - Point Update, Range Query, Lazy Propagation
 * Uses optimized I/O for CSES acceptance
 */
import java.io.*;
import java.util.*;

public class SegTreeTemplate {
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
        long[] arr = nextLongArray(n);
        
        SegTree st = new SegTree(arr);
        
        StringBuilder sb = new StringBuilder();
        while (q-- > 0) {
            int type = nextInt();
            if (type == 1) {
                int idx = nextInt() - 1;
                long val = nextLong();
                st.update(idx, val);
            } else {
                int l = nextInt() - 1, r = nextInt() - 1;
                sb.append(st.query(l, r)).append('\n');
            }
        }
        out.print(sb);
    }
    
    // ==================== Segment Tree (Sum) ====================
    static class SegTree {
        int n;
        long[] tree;
        
        SegTree(long[] arr) {
            n = arr.length;
            tree = new long[4 * n];
            build(arr, 1, 0, n - 1);
        }
        
        void build(long[] arr, int node, int start, int end) {
            if (start == end) {
                tree[node] = arr[start];
            } else {
                int mid = (start + end) / 2;
                build(arr, 2 * node, start, mid);
                build(arr, 2 * node + 1, mid + 1, end);
                tree[node] = tree[2 * node] + tree[2 * node + 1];
            }
        }
        
        void update(int idx, long val) {
            update(1, 0, n - 1, idx, val);
        }
        
        void update(int node, int start, int end, int idx, long val) {
            if (start == end) {
                tree[node] = val;
            } else {
                int mid = (start + end) / 2;
                if (idx <= mid) update(2 * node, start, mid, idx, val);
                else update(2 * node + 1, mid + 1, end, idx, val);
                tree[node] = tree[2 * node] + tree[2 * node + 1];
            }
        }
        
        long query(int l, int r) {
            return query(1, 0, n - 1, l, r);
        }
        
        long query(int node, int start, int end, int l, int r) {
            if (r < start || end < l) return 0;
            if (l <= start && end <= r) return tree[node];
            int mid = (start + end) / 2;
            return query(2 * node, start, mid, l, r) + 
                   query(2 * node + 1, mid + 1, end, l, r);
        }
    }
    
    // ==================== Lazy Segment Tree (Range Update) ====================
    static class LazySegTree {
        int n;
        long[] tree, lazy;
        
        LazySegTree(int n) {
            this.n = n;
            tree = new long[4 * n];
            lazy = new long[4 * n];
        }
        
        LazySegTree(long[] arr) {
            this(arr.length);
            build(arr, 1, 0, n - 1);
        }
        
        void build(long[] arr, int node, int start, int end) {
            if (start == end) {
                tree[node] = arr[start];
            } else {
                int mid = (start + end) / 2;
                build(arr, 2 * node, start, mid);
                build(arr, 2 * node + 1, mid + 1, end);
                tree[node] = tree[2 * node] + tree[2 * node + 1];
            }
        }
        
        void push(int node, int start, int end) {
            if (lazy[node] != 0) {
                tree[node] += lazy[node] * (end - start + 1);
                if (start != end) {
                    lazy[2 * node] += lazy[node];
                    lazy[2 * node + 1] += lazy[node];
                }
                lazy[node] = 0;
            }
        }
        
        void updateRange(int l, int r, long val) {
            updateRange(1, 0, n - 1, l, r, val);
        }
        
        void updateRange(int node, int start, int end, int l, int r, long val) {
            push(node, start, end);
            if (r < start || end < l) return;
            if (l <= start && end <= r) {
                lazy[node] += val;
                push(node, start, end);
                return;
            }
            int mid = (start + end) / 2;
            updateRange(2 * node, start, mid, l, r, val);
            updateRange(2 * node + 1, mid + 1, end, l, r, val);
            tree[node] = tree[2 * node] + tree[2 * node + 1];
        }
        
        long query(int l, int r) {
            return query(1, 0, n - 1, l, r);
        }
        
        long query(int node, int start, int end, int l, int r) {
            push(node, start, end);
            if (r < start || end < l) return 0;
            if (l <= start && end <= r) return tree[node];
            int mid = (start + end) / 2;
            return query(2 * node, start, mid, l, r) + 
                   query(2 * node + 1, mid + 1, end, l, r);
        }
    }
    
    // ==================== Min Segment Tree ====================
    static class MinSegTree {
        int n;
        long[] tree;
        static final long INF = Long.MAX_VALUE;
        
        MinSegTree(long[] arr) {
            n = arr.length;
            tree = new long[4 * n];
            Arrays.fill(tree, INF);
            build(arr, 1, 0, n - 1);
        }
        
        void build(long[] arr, int node, int start, int end) {
            if (start == end) {
                tree[node] = arr[start];
            } else {
                int mid = (start + end) / 2;
                build(arr, 2 * node, start, mid);
                build(arr, 2 * node + 1, mid + 1, end);
                tree[node] = Math.min(tree[2 * node], tree[2 * node + 1]);
            }
        }
        
        void update(int idx, long val) {
            update(1, 0, n - 1, idx, val);
        }
        
        void update(int node, int start, int end, int idx, long val) {
            if (start == end) {
                tree[node] = val;
            } else {
                int mid = (start + end) / 2;
                if (idx <= mid) update(2 * node, start, mid, idx, val);
                else update(2 * node + 1, mid + 1, end, idx, val);
                tree[node] = Math.min(tree[2 * node], tree[2 * node + 1]);
            }
        }
        
        long query(int l, int r) {
            return query(1, 0, n - 1, l, r);
        }
        
        long query(int node, int start, int end, int l, int r) {
            if (r < start || end < l) return INF;
            if (l <= start && end <= r) return tree[node];
            int mid = (start + end) / 2;
            return Math.min(query(2 * node, start, mid, l, r),
                           query(2 * node + 1, mid + 1, end, l, r));
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
