/*
 * Author  : Aritra Dutta
 * Target  : Codeforces Expert / CSES
 *
 * UTILITY CLASSES Template - Common Data Structures & Helpers
 * Includes: Pair, Triple, Quad, Custom Comparators, Range, etc.
 *
 * USAGE: Import specific classes or copy as needed
 */
import java.io.*;
import java.util.*;

public class Utilities {
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
        Pair<Integer, Integer> p = new Pair<>(1, 2);
        out.println(p);
    }

    // ==================== PAIR CLASS ====================
    static class Pair<F, S> implements Comparable<Pair<F, S>> {
        F first;
        S second;

        Pair(F first, S second) {
            this.first = first;
            this.second = second;
        }

        public int compareTo(Pair<F, S> o) {
            if (first instanceof Comparable && o.first instanceof Comparable) {
                @SuppressWarnings("unchecked")
                int cmp = ((Comparable<F>) first).compareTo(o.first);
                if (cmp != 0) return cmp;
            }
            if (second instanceof Comparable && o.second instanceof Comparable) {
                @SuppressWarnings("unchecked")
                return ((Comparable<S>) second).compareTo(o.second);
            }
            return 0;
        }

        public String toString() { return "(" + first + ", " + second + ")"; }

        public boolean equals(Object o) {
            if (!(o instanceof Pair)) return false;
            Pair<?, ?> p = (Pair<?, ?>) o;
            return Objects.equals(first, p.first) && Objects.equals(second, p.second);
        }

        public int hashCode() { return Objects.hash(first, second); }

        static <F, S> Pair<F, S> of(F f, S s) { return new Pair<>(f, s); }
    }

    // Primitive specializations for performance
    static class IntPair implements Comparable<IntPair> {
        int first, second;

        IntPair(int f, int s) { first = f; second = s; }

        public int compareTo(IntPair o) {
            if (first != o.first) return Integer.compare(first, o.first);
            return Integer.compare(second, o.second);
        }

        public String toString() { return "(" + first + ", " + second + ")"; }

        public boolean equals(Object o) {
            if (!(o instanceof IntPair)) return false;
            IntPair p = (IntPair) o;
            return first == p.first && second == p.second;
        }

        public int hashCode() { return Objects.hash(first, second); }

        static IntPair of(int f, int s) { return new IntPair(f, s); }
    }

    static class LongPair implements Comparable<LongPair> {
        long first, second;

        LongPair(long f, long s) { first = f; second = s; }

        public int compareTo(LongPair o) {
            if (first != o.first) return Long.compare(first, o.first);
            return Long.compare(second, o.second);
        }

        public String toString() { return "(" + first + ", " + second + ")"; }

        public boolean equals(Object o) {
            if (!(o instanceof LongPair)) return false;
            LongPair p = (LongPair) o;
            return first == p.first && second == p.second;
        }

        public int hashCode() { return Objects.hash(first, second); }

        static LongPair of(long f, long s) { return new LongPair(f, s); }
    }

    // ==================== TRIPLE CLASS ====================
    static class Triple<F, S, T> implements Comparable<Triple<F, S, T>> {
        F first;
        S second;
        T third;

        Triple(F first, S second, T third) {
            this.first = first;
            this.second = second;
            this.third = third;
        }

        public int compareTo(Triple<F, S, T> o) {
            if (first instanceof Comparable && o.first instanceof Comparable) {
                @SuppressWarnings("unchecked")
                int cmp = ((Comparable<F>) first).compareTo(o.first);
                if (cmp != 0) return cmp;
            }
            if (second instanceof Comparable && o.second instanceof Comparable) {
                @SuppressWarnings("unchecked")
                int cmp = ((Comparable<S>) second).compareTo(o.second);
                if (cmp != 0) return cmp;
            }
            if (third instanceof Comparable && o.third instanceof Comparable) {
                @SuppressWarnings("unchecked")
                return ((Comparable<T>) third).compareTo(o.third);
            }
            return 0;
        }

        public String toString() { return "(" + first + ", " + second + ", " + third + ")"; }

        public boolean equals(Object o) {
            if (!(o instanceof Triple)) return false;
            Triple<?, ?, ?> t = (Triple<?, ?, ?>) o;
            return Objects.equals(first, t.first) &&
                   Objects.equals(second, t.second) &&
                   Objects.equals(third, t.third);
        }

        public int hashCode() { return Objects.hash(first, second, third); }

        static <F, S, T> Triple<F, S, T> of(F f, S s, T t) { return new Triple<>(f, s, t); }
    }

    static class IntTriple implements Comparable<IntTriple> {
        int first, second, third;

        IntTriple(int f, int s, int t) { first = f; second = s; third = t; }

        public int compareTo(IntTriple o) {
            if (first != o.first) return Integer.compare(first, o.first);
            if (second != o.second) return Integer.compare(second, o.second);
            return Integer.compare(third, o.third);
        }

        public String toString() { return "(" + first + ", " + second + ", " + third + ")"; }

        public boolean equals(Object o) {
            if (!(o instanceof IntTriple)) return false;
            IntTriple t = (IntTriple) o;
            return first == t.first && second == t.second && third == t.third;
        }

        public int hashCode() { return Objects.hash(first, second, third); }

        static IntTriple of(int f, int s, int t) { return new IntTriple(f, s, t); }
    }

    static class LongTriple implements Comparable<LongTriple> {
        long first, second, third;

        LongTriple(long f, long s, long t) { first = f; second = s; third = t; }

        public int compareTo(LongTriple o) {
            if (first != o.first) return Long.compare(first, o.first);
            if (second != o.second) return Long.compare(second, o.second);
            return Long.compare(third, o.third);
        }

        public String toString() { return "(" + first + ", " + second + ", " + third + ")"; }

        public boolean equals(Object o) {
            if (!(o instanceof LongTriple)) return false;
            LongTriple t = (LongTriple) o;
            return first == t.first && second == t.second && third == t.third;
        }

        public int hashCode() { return Objects.hash(first, second, third); }

        static LongTriple of(long f, long s, long t) { return new LongTriple(f, s, t); }
    }

    // ==================== QUAD CLASS ====================
    static class Quad<A, B, C, D> {
        A first;
        B second;
        C third;
        D fourth;

        Quad(A a, B b, C c, D d) {
            first = a; second = b; third = c; fourth = d;
        }

        public String toString() {
            return "(" + first + ", " + second + ", " + third + ", " + fourth + ")";
        }

        public boolean equals(Object o) {
            if (!(o instanceof Quad)) return false;
            Quad<?, ?, ?, ?> q = (Quad<?, ?, ?, ?>) o;
            return Objects.equals(first, q.first) &&
                   Objects.equals(second, q.second) &&
                   Objects.equals(third, q.third) &&
                   Objects.equals(fourth, q.fourth);
        }

        public int hashCode() { return Objects.hash(first, second, third, fourth); }

        static <A, B, C, D> Quad<A, B, C, D> of(A a, B b, C c, D d) {
            return new Quad<>(a, b, c, d);
        }
    }

    // ==================== RANGE CLASS ====================
    static class Range implements Comparable<Range> {
        int l, r;

        Range(int l, int r) { this.l = l; this.r = r; }

        // Check if range contains a value
        boolean contains(int x) { return l <= x && x <= r; }

        // Check if this range contains another range
        boolean contains(Range o) { return l <= o.l && o.r <= r; }

        // Check if ranges intersect
        boolean intersects(Range o) { return Math.max(l, o.l) <= Math.min(r, o.r); }

        // Intersection of two ranges
        Range intersection(Range o) {
            int nl = Math.max(l, o.l), nr = Math.min(r, o.r);
            return nl <= nr ? new Range(nl, nr) : null;
        }

        // Union of two ranges (if they intersect or touch)
        Range union(Range o) {
            if (!intersects(o) && l > o.r + 1 && r < o.l - 1) return null;
            return new Range(Math.min(l, o.l), Math.max(r, o.r));
        }

        int length() { return r - l + 1; }

        public int compareTo(Range o) {
            if (l != o.l) return Integer.compare(l, o.l);
            return Integer.compare(r, o.r);
        }

        public String toString() { return "[" + l + ", " + r + "]"; }

        public boolean equals(Object o) {
            if (!(o instanceof Range)) return false;
            Range r = (Range) o;
            return l == r.l && this.r == r.r;
        }

        public int hashCode() { return Objects.hash(l, r); }
    }

    // ==================== CUSTOM COMPARATORS ====================
    static class Comparators {
        // Reverse order comparator
        static <T> Comparator<T> reverse() {
            return Collections.reverseOrder();
        }

        // Comparator for pairs by first element
        static <F extends Comparable<F>, S> Comparator<Pair<F, S>> pairByFirst() {
            return (a, b) -> a.first.compareTo(b.first);
        }

        // Comparator for pairs by second element
        static <F, S extends Comparable<S>> Comparator<Pair<F, S>> pairBySecond() {
            return (a, b) -> a.second.compareTo(b.second);
        }

        // Comparator for triples by first element
        static <F extends Comparable<F>, S, T> Comparator<Triple<F, S, T>> tripleByFirst() {
            return (a, b) -> a.first.compareTo(b.first);
        }

        // Comparator by absolute value
        static Comparator<Integer> byAbsValue() {
            return (a, b) -> Integer.compare(Math.abs(a), Math.abs(b));
        }

        // Comparator by digit sum
        static Comparator<Integer> byDigitSum() {
            return (a, b) -> Integer.compare(digitSum(a), digitSum(b));
        }

        static int digitSum(int n) {
            int sum = 0;
            n = Math.abs(n);
            while (n > 0) {
                sum += n % 10;
                n /= 10;
            }
            return sum;
        }

        // Comparator for strings by length
        static Comparator<String> byLength() {
            return (a, b) -> Integer.compare(a.length(), b.length());
        }

        // Lexicographical comparator for arrays
        static <T extends Comparable<T>> Comparator<T[]> arrayLex() {
            return (a, b) -> {
                int n = Math.min(a.length, b.length);
                for (int i = 0; i < n; i++) {
                    int cmp = a[i].compareTo(b[i]);
                    if (cmp != 0) return cmp;
                }
                return Integer.compare(a.length, b.length);
            };
        }
    }

    // ==================== DISJOINT SET UNION (DSU) ====================
    static class DSU {
        int[] parent, rank, size;
        int components;

        DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            size = new int[n];
            components = n;
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int find(int x) {
            if (parent[x] != x) parent[x] = find(parent[x]);
            return parent[x];
        }

        boolean unite(int x, int y) {
            x = find(x); y = find(y);
            if (x == y) return false;

            if (rank[x] < rank[y]) { int t = x; x = y; y = t; }
            parent[y] = x;
            size[x] += size[y];
            if (rank[x] == rank[y]) rank[x]++;
            components--;
            return true;
        }

        boolean same(int x, int y) { return find(x) == find(y); }
        int getSize(int x) { return size[find(x)]; }
        int getComponents() { return components; }
    }

    // ==================== SEGMENT TREE (Point Update, Range Query) ====================
    static class SegmentTree {
        int n;
        long[] tree;

        SegmentTree(int n) {
            this.n = n;
            tree = new long[4 * n];
        }

        SegmentTree(long[] arr) {
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

        void update(int idx, long val) { update(1, 0, n - 1, idx, val); }

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

        long query(int l, int r) { return query(1, 0, n - 1, l, r); }

        long query(int node, int start, int end, int l, int r) {
            if (r < start || end < l) return 0;
            if (l <= start && end <= r) return tree[node];
            int mid = (start + end) / 2;
            return query(2 * node, start, mid, l, r) +
                   query(2 * node + 1, mid + 1, end, l, r);
        }
    }

    // ==================== FENWICK TREE (BIT) ====================
    static class FenwickTree {
        int n;
        long[] tree;

        FenwickTree(int n) {
            this.n = n;
            tree = new long[n + 1];
        }

        FenwickTree(long[] arr) {
            n = arr.length;
            tree = new long[n + 1];
            for (int i = 0; i < n; i++) {
                update(i, arr[i]);
            }
        }

        void update(int i, long delta) {
            for (++i; i <= n; i += i & (-i)) tree[i] += delta;
        }

        long query(int i) {
            long sum = 0;
            for (++i; i > 0; i -= i & (-i)) sum += tree[i];
            return sum;
        }

        long query(int l, int r) { return query(r) - query(l - 1); }
    }

    // ==================== RMQ (Sparse Table) ====================
    static class RMQ {
        int n, LOG;
        int[][] sparse;
        int[] logs;

        RMQ(int[] arr) {
            n = arr.length;
            LOG = 32 - Integer.numberOfLeadingZeros(n);
            sparse = new int[n][LOG];
            logs = new int[n + 1];

            logs[1] = 0;
            for (int i = 2; i <= n; i++) logs[i] = logs[i / 2] + 1;

            for (int i = 0; i < n; i++) sparse[i][0] = arr[i];

            for (int j = 1; j < LOG; j++) {
                for (int i = 0; i + (1 << j) <= n; i++) {
                    sparse[i][j] = Math.min(sparse[i][j - 1], sparse[i + (1 << (j - 1))][j - 1]);
                }
            }
        }

        int query(int l, int r) {
            int len = r - l + 1;
            int k = logs[len];
            return Math.min(sparse[l][k], sparse[r - (1 << k) + 1][k]);
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
