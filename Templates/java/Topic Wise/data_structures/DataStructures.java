/*
 * Author  : Aritra Dutta
 * Target  : Codeforces Expert / CSES
 *
 * ADVANCED DATA STRUCTURES Template - Complete Library
 * Includes: Segment Tree, Lazy Segment Tree, Fenwick Tree, Sparse Table,
 *           Sqrt Decomposition, Treap, Disjoint Sparse Table, etc.
 *
 * USAGE: Import specific classes or copy algorithms you need
 */
import java.io.*;
import java.util.*;

public class DataStructures {
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
        int n = nextInt(), q = nextInt();
        long[] arr = nextLongArray(n);

        // Segment Tree for range sum queries
        SegTree st = new SegTree(arr, (a, b) -> a + b, 0L);

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

    // ==================== FUNCTIONAL INTERFACE ====================
    @FunctionalInterface
    interface Combiner<T> {
        T combine(T a, T b);
    }

    // ==================== SEGMENT TREE (Generic) ====================
    static class SegTree<T> {
        int n;
        T[] tree;
        T identity;
        Combiner<T> combine;

        @SuppressWarnings("unchecked")
        SegTree(int n, Combiner<T> combine, T identity) {
            this.n = n;
            this.combine = combine;
            this.identity = identity;
            tree = (T[]) new Object[4 * n];
            Arrays.fill(tree, identity);
        }

        SegTree(T[] arr, Combiner<T> combine, T identity) {
            this(arr.length, combine, identity);
            build(arr, 1, 0, n - 1);
        }

        void build(T[] arr, int node, int start, int end) {
            if (start == end) {
                tree[node] = arr[start];
            } else {
                int mid = (start + end) / 2;
                build(arr, 2 * node, start, mid);
                build(arr, 2 * node + 1, mid + 1, end);
                tree[node] = combine.combine(tree[2 * node], tree[2 * node + 1]);
            }
        }

        void update(int idx, T val) { update(1, 0, n - 1, idx, val); }

        void update(int node, int start, int end, int idx, T val) {
            if (start == end) {
                tree[node] = val;
            } else {
                int mid = (start + end) / 2;
                if (idx <= mid) update(2 * node, start, mid, idx, val);
                else update(2 * node + 1, mid + 1, end, idx, val);
                tree[node] = combine.combine(tree[2 * node], tree[2 * node + 1]);
            }
        }

        T query(int l, int r) { return query(1, 0, n - 1, l, r); }

        T query(int node, int start, int end, int l, int r) {
            if (r < start || end < l) return identity;
            if (l <= start && end <= r) return tree[node];
            int mid = (start + end) / 2;
            return combine.combine(
                query(2 * node, start, mid, l, r),
                query(2 * node + 1, mid + 1, end, l, r)
            );
        }

        // Find first position where prefix condition is met
        int findFirst(java.util.function.Predicate<T> predicate) {
            return findFirst(1, 0, n - 1, 0, n - 1, predicate);
        }

        int findFirst(int node, int start, int end, int l, int r, java.util.function.Predicate<T> predicate) {
            if (r < start || end < l) return -1;
            if (predicate.test(tree[node])) {
                if (start == end) return start;
                int mid = (start + end) / 2;
                int leftResult = findFirst(2 * node, start, mid, l, r, predicate);
                if (leftResult != -1) return leftResult;
                return findFirst(2 * node + 1, mid + 1, end, l, r, predicate);
            }
            return -1;
        }
    }

    // ==================== LAZY SEGMENT TREE (Range Update) ====================
    static class LazySegTree {
        int n;
        long[] tree, lazy;
        boolean[] hasLazy;
        Combiner<Long> combine;
        long identity;

        LazySegTree(int n, Combiner<Long> combine, long identity) {
            this.n = n;
            this.combine = combine;
            this.identity = identity;
            tree = new long[4 * n];
            lazy = new long[4 * n];
            hasLazy = new boolean[4 * n];
            Arrays.fill(tree, identity);
        }

        LazySegTree(long[] arr, Combiner<Long> combine, long identity) {
            this(arr.length, combine, identity);
            build(arr, 1, 0, n - 1);
        }

        void build(long[] arr, int node, int start, int end) {
            if (start == end) {
                tree[node] = arr[start];
            } else {
                int mid = (start + end) / 2;
                build(arr, 2 * node, start, mid);
                build(arr, 2 * node + 1, mid + 1, end);
                tree[node] = combine.combine(tree[2 * node], tree[2 * node + 1]);
            }
        }

        void push(int node, int start, int end) {
            if (hasLazy[node]) {
                // Apply lazy value to children
                int mid = (start + end) / 2;
                int left = 2 * node, right = 2 * node + 1;

                // For sum: add lazy * count
                tree[left] += lazy[node] * (mid - start + 1);
                tree[right] += lazy[node] * (end - mid);

                if (start != end) {
                    lazy[left] += lazy[node];
                    lazy[right] += lazy[node];
                    hasLazy[left] = true;
                    hasLazy[right] = true;
                }

                lazy[node] = 0;
                hasLazy[node] = false;
            }
        }

        void updateRange(int l, int r, long val) { updateRange(1, 0, n - 1, l, r, val); }

        void updateRange(int node, int start, int end, int l, int r, long val) {
            if (r < start || end < l) return;
            if (l <= start && end <= r) {
                tree[node] += val * (end - start + 1);
                if (start != end) {
                    lazy[2 * node] += val;
                    lazy[2 * node + 1] += val;
                    hasLazy[2 * node] = true;
                    hasLazy[2 * node + 1] = true;
                }
                return;
            }
            push(node, start, end);
            int mid = (start + end) / 2;
            updateRange(2 * node, start, mid, l, r, val);
            updateRange(2 * node + 1, mid + 1, end, l, r, val);
            tree[node] = tree[2 * node] + tree[2 * node + 1];
        }

        long query(int l, int r) { return query(1, 0, n - 1, l, r); }

        long query(int node, int start, int end, int l, int r) {
            if (r < start || end < l) return identity;
            push(node, start, end);
            if (l <= start && end <= r) return tree[node];
            int mid = (start + end) / 2;
            return combine.combine(
                query(2 * node, start, mid, l, r),
                query(2 * node + 1, mid + 1, end, l, r)
            );
        }
    }

    // ==================== MIN SEGMENT TREE ====================
    static class MinSegTree {
        int n;
        long[] tree;
        static final long INF = Long.MAX_VALUE;

        MinSegTree(long[] arr) {
            n = arr.length;
            tree = new long[4 * n];
            Arrays.fill(tree, INF);
            if (n > 0) build(arr, 1, 0, n - 1);
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

        void update(int idx, long val) { update(1, 0, n - 1, idx, val); }

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

        long query(int l, int r) { return query(1, 0, n - 1, l, r); }

        long query(int node, int start, int end, int l, int r) {
            if (r < start || end < l) return INF;
            if (l <= start && end <= r) return tree[node];
            int mid = (start + end) / 2;
            return Math.min(
                query(2 * node, start, mid, l, r),
                query(2 * node + 1, mid + 1, end, l, r)
            );
        }

        // Find index of minimum element in range
        int queryIndex(int l, int r) { return queryIndex(1, 0, n - 1, l, r); }

        int queryIndex(int node, int start, int end, int l, int r) {
            if (r < start || end < l) return -1;
            if (l <= start && end <= r) {
                // Find index of min in this range
                if (start == end) return start;
                int mid = (start + end) / 2;
                int leftIdx = queryIndex(2 * node, start, mid, l, r);
                int rightIdx = queryIndex(2 * node + 1, mid + 1, end, l, r);
                if (leftIdx == -1) return rightIdx;
                if (rightIdx == -1) return leftIdx;
                return tree[2 * node] <= tree[2 * node + 1] ? 
                    queryIndex(2 * node, start, mid, l, r) : 
                    queryIndex(2 * node + 1, mid + 1, end, l, r);
            }
            int mid = (start + end) / 2;
            int leftIdx = queryIndex(2 * node, start, mid, l, r);
            int rightIdx = queryIndex(2 * node + 1, mid + 1, end, l, r);
            if (leftIdx == -1) return rightIdx;
            if (rightIdx == -1) return leftIdx;
            return tree[leftIdx] <= tree[rightIdx] ? leftIdx : rightIdx;
        }
    }

    // ==================== MAX SEGMENT TREE ====================
    static class MaxSegTree {
        int n;
        long[] tree;
        static final long NINF = Long.MIN_VALUE;

        MaxSegTree(long[] arr) {
            n = arr.length;
            tree = new long[4 * n];
            Arrays.fill(tree, NINF);
            if (n > 0) build(arr, 1, 0, n - 1);
        }

        void build(long[] arr, int node, int start, int end) {
            if (start == end) {
                tree[node] = arr[start];
            } else {
                int mid = (start + end) / 2;
                build(arr, 2 * node, start, mid);
                build(arr, 2 * node + 1, mid + 1, end);
                tree[node] = Math.max(tree[2 * node], tree[2 * node + 1]);
            }
        }

        void update(int idx, long val) { update(1, 0, n - 1, idx, val); }

        void update(int node, int start, int end, int idx, long val) {
            if (start == end) {
                tree[node] = val;
            } else {
                int mid = (start + end) / 2;
                if (idx <= mid) update(2 * node, start, mid, idx, val);
                else update(2 * node + 1, mid + 1, end, idx, val);
                tree[node] = Math.max(tree[2 * node], tree[2 * node + 1]);
            }
        }

        long query(int l, int r) { return query(1, 0, n - 1, l, r); }

        long query(int node, int start, int end, int l, int r) {
            if (r < start || end < l) return NINF;
            if (l <= start && end <= r) return tree[node];
            int mid = (start + end) / 2;
            return Math.max(
                query(2 * node, start, mid, l, r),
                query(2 * node + 1, mid + 1, end, l, r)
            );
        }
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

    // ==================== ORDER STATISTIC TREE (using BIT) ====================
    static class OrderStatisticTree {
        BIT bit;
        int maxVal;
        boolean[] present;

        OrderStatisticTree(int maxVal) {
            this.maxVal = maxVal;
            bit = new BIT(maxVal + 1);
            present = new boolean[maxVal + 1];
        }

        void insert(int val) {
            if (!present[val]) {
                present[val] = true;
                bit.update(val + 1, 1);
            }
        }

        void remove(int val) {
            if (present[val]) {
                present[val] = false;
                bit.update(val + 1, -1);
            }
        }

        // Find k-th smallest element (0-indexed)
        int findKth(int k) {
            return bit.findKth(k + 1) - 1;
        }

        // Get rank of element (number of elements smaller)
        int getRank(int val) {
            return (int) bit.query(1, val);
        }

        boolean contains(int val) { return present[val]; }
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
