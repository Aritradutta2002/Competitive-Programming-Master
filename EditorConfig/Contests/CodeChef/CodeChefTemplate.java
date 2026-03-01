/*
 * Author  : Aritra Dutta
 * Target  : CodeChef Contests (Starters, Cook-Off, Lunchtime)
 * 
 * CODECHEF CONTEST TEMPLATE - Java
 * Optimized for: CodeChef Starters, Cook-Off, Lunchtime
 * 
 * Features:
 * - Lightning-fast I/O (BufferedReader + StringTokenizer + PrintWriter)
 * - Essential math utilities (GCD, LCM, modPow, modInv)
 * - Data structures (DSU, Pair)
 * - Graph algorithms (BFS, Dijkstra)
 * - Sorting with anti-hack protection
 * - Binary search utilities
 * - Array I/O helpers
 * 
 * Note: Change class name to match the problem code before submission
 */
import java.io.*;
import java.util.*;

public class CodeChefTemplate {
    // ==================== I/O ====================
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static StringTokenizer st;
    static PrintWriter out = new PrintWriter(new BufferedOutputStream(System.out));
    
    // ==================== CONSTANTS ====================
    static final int MOD = 1_000_000_007;
    static final int MOD2 = 998244353;
    static final long INF = (long) 1e18;
    static final Random rnd = new Random();
    
    // ==================== MAIN ====================
    public static void main(String[] args) throws IOException {
        int t = nextInt();
        while (t-- > 0) solve();
        out.flush();
    }
    
    static void solve() throws IOException {
        int n = nextInt();
        // Your solution here
    }
    
    // ==================== FAST I/O ====================
    static String next() throws IOException {
        while (st == null || !st.hasMoreTokens()) st = new StringTokenizer(br.readLine());
        return st.nextToken();
    }
    static int nextInt() throws IOException { return Integer.parseInt(next()); }
    static long nextLong() throws IOException { return Long.parseLong(next()); }
    static double nextDouble() throws IOException { return Double.parseDouble(next()); }
    static String nextLine() throws IOException { return br.readLine(); }
    static int[] nextIntArray(int n) throws IOException { 
        int[] a = new int[n]; 
        for (int i = 0; i < n; i++) a[i] = nextInt(); 
        return a; 
    }
    static long[] nextLongArray(int n) throws IOException { 
        long[] a = new long[n]; 
        for (int i = 0; i < n; i++) a[i] = nextLong(); 
        return a; 
    }
    
    // ==================== OUTPUT ====================
    static void print(int[] a) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a.length; i++) {
            if (i > 0) sb.append(' ');
            sb.append(a[i]);
        }
        out.println(sb);
    }
    static void print(long[] a) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < a.length; i++) {
            if (i > 0) sb.append(' ');
            sb.append(a[i]);
        }
        out.println(sb);
    }
    static void yes() { out.println("YES"); }
    static void no() { out.println("NO"); }
    
    // ==================== MATH ====================
    static long gcd(long a, long b) { return b == 0 ? a : gcd(b, a % b); }
    static long lcm(long a, long b) { return a / gcd(a, b) * b; }
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
    static long modInv(long x, long m) { return modPow(x, m - 2, m); }
    
    // ==================== SORTING ====================
    static void shuffleSort(int[] a) {
        for (int i = a.length - 1; i > 0; i--) {
            int j = rnd.nextInt(i + 1);
            int t = a[i]; a[i] = a[j]; a[j] = t;
        }
        Arrays.sort(a);
    }
    static void shuffleSort(long[] a) {
        for (int i = a.length - 1; i > 0; i--) {
            int j = rnd.nextInt(i + 1);
            long t = a[i]; a[i] = a[j]; a[j] = t;
        }
        Arrays.sort(a);
    }
    
    // ==================== BINARY SEARCH ====================
    static int lowerBound(int[] a, int x) {
        int lo = 0, hi = a.length;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (a[mid] < x) lo = mid + 1;
            else hi = mid;
        }
        return lo;
    }
    static int upperBound(int[] a, int x) {
        int lo = 0, hi = a.length;
        while (lo < hi) {
            int mid = (lo + hi) >>> 1;
            if (a[mid] <= x) lo = mid + 1;
            else hi = mid;
        }
        return lo;
    }
    
    // ==================== DATA STRUCTURES ====================
    static class DSU {
        int[] p, r;
        int comps;
        DSU(int n) {
            p = new int[n];
            r = new int[n];
            comps = n;
            for (int i = 0; i < n; i++) p[i] = i;
        }
        int find(int x) { return p[x] == x ? x : (p[x] = find(p[x])); }
        boolean union(int x, int y) {
            x = find(x); y = find(y);
            if (x == y) return false;
            if (r[x] < r[y]) { int t = x; x = y; y = t; }
            p[y] = x;
            if (r[x] == r[y]) r[x]++;
            comps--;
            return true;
        }
        boolean same(int x, int y) { return find(x) == find(y); }
    }
    
    static class Pair implements Comparable<Pair> {
        int f, s;
        Pair(int f, int s) { this.f = f; this.s = s; }
        public int compareTo(Pair o) {
            if (f != o.f) return Integer.compare(f, o.f);
            return Integer.compare(s, o.s);
        }
        public boolean equals(Object o) {
            if (!(o instanceof Pair)) return false;
            Pair p = (Pair) o;
            return f == p.f && s == p.s;
        }
        public int hashCode() { return 31 * f + s; }
    }
    
    // ==================== GRAPH ====================
    static int[] bfs(int src, List<List<Integer>> adj) {
        int n = adj.size();
        int[] d = new int[n];
        Arrays.fill(d, -1);
        ArrayDeque<Integer> q = new ArrayDeque<>();
        q.add(src);
        d[src] = 0;
        while (!q.isEmpty()) {
            int u = q.poll();
            for (int v : adj.get(u)) {
                if (d[v] == -1) {
                    d[v] = d[u] + 1;
                    q.add(v);
                }
            }
        }
        return d;
    }
    
    static long[] dijkstra(int src, List<List<long[]>> adj) {
        int n = adj.size();
        long[] d = new long[n];
        Arrays.fill(d, INF);
        d[src] = 0;
        PriorityQueue<long[]> pq = new PriorityQueue<>(Comparator.comparingLong(a -> a[1]));
        pq.add(new long[]{src, 0});
        while (!pq.isEmpty()) {
            long[] cur = pq.poll();
            int u = (int) cur[0];
            long du = cur[1];
            if (du > d[u]) continue;
            for (long[] e : adj.get(u)) {
                int v = (int) e[0];
                long w = e[1];
                if (d[u] + w < d[v]) {
                    d[v] = d[u] + w;
                    pq.add(new long[]{v, d[v]});
                }
            }
        }
        return d;
    }
}
