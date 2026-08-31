/*
 * Author  : Aritra Dutta
 *
 * SEGMENT TREE Library
 * Includes: Combiner interface, Generic Segment Tree, Lazy Segment Tree,
 *           Min Segment Tree, Max Segment Tree, Order Statistic Tree (via BIT)
 *
 * Time Complexities:
 *   SegTree       - Build O(n), Update O(log n), Query O(log n)
 *   LazySegTree   - Build O(n), Range Update O(log n), Query O(log n)
 *   MinSegTree    - Build O(n), Update O(log n), Query O(log n)
 *   MaxSegTree    - Build O(n), Update O(log n), Query O(log n)
 *   OrderStatTree - Insert/Remove O(log n), FindKth O(log n), GetRank O(log n)
 *
 * USAGE: Copy the inner classes you need into your solution
 */
import java.io.*;
import java.util.*;

public class SegmentTree {
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

    // Internal BIT used by OrderStatisticTree
    static class BIT {
        int n;
        long[] tree;

        BIT(int n) {
            this.n = n;
            tree = new long[n + 1];
        }

        void update(int i, long val) {
            for (; i <= n; i += i & (-i)) tree[i] += val;
        }

        long query(int i) {
            long sum = 0;
            for (; i > 0; i -= i & (-i)) sum += tree[i];
            return sum;
        }

        long query(int l, int r) {
            return query(r) - query(l - 1);
        }

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
