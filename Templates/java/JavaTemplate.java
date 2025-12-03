/*
 * Author  : Aritra Dutta
 * Target  : Codeforces Expert / CSES
 * 
 * ULTIMATE Java Template - BufferedReader + StringTokenizer + PrintWriter
 * This is the FASTEST Java I/O for competitive programming
 * 
 * Key optimizations:
 * 1. BufferedReader instead of Scanner (10x faster)
 * 2. StringTokenizer for parsing (faster than split)
 * 3. PrintWriter with StringBuilder for output
 * 4. Shuffle before sort to avoid O(n²) worst case
 */
import java.io.*;
import java.util.*;

public class JavaTemplate {
    static BufferedReader br;
    static StringTokenizer st;
    static PrintWriter out;
    
    static final int MOD = 1_000_000_007;
    static final int MOD2 = 998244353;
    static final long INF = (long) 1e18;
    static final Random random = new Random();
    
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        out = new PrintWriter(new BufferedOutputStream(System.out));
        
        int t = nextInt();
        while (t-- > 0) {
            solve();
        }
        
        out.flush();
        out.close();
    }
    
    static void solve() throws IOException {
        int n = nextInt();
        // Your solution here
        
    }
    
    // ==================== FAST I/O ====================
    static String next() throws IOException {
        while (st == null || !st.hasMoreTokens())
            st = new StringTokenizer(br.readLine());
        return st.nextToken();
    }
    
    static int nextInt() throws IOException {
        return Integer.parseInt(next());
    }
    
    static long nextLong() throws IOException {
        return Long.parseLong(next());
    }
    
    static double nextDouble() throws IOException {
        return Double.parseDouble(next());
    }
    
    static String nextLine() throws IOException {
        return br.readLine();
    }
    
    static int[] nextIntArray(int n) throws IOException {
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) arr[i] = nextInt();
        return arr;
    }
    
    static long[] nextLongArray(int n) throws IOException {
        long[] arr = new long[n];
        for (int i = 0; i < n; i++) arr[i] = nextLong();
        return arr;
    }
    
    // ==================== OUTPUT HELPERS ====================
    static void printArray(int[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) sb.append(' ');
            sb.append(arr[i]);
        }
        out.println(sb);
    }
    
    static void printArray(long[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) sb.append(' ');
            sb.append(arr[i]);
        }
        out.println(sb);
    }
    
    static void yes() { out.println("YES"); }
    static void no() { out.println("NO"); }
    
    // ==================== MATH UTILITIES ====================
    static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }
    
    static long lcm(long a, long b) {
        return a / gcd(a, b) * b;
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
    
    static long modInv(long x, long m) {
        return modPow(x, m - 2, m);
    }
    
    static long modAdd(long a, long b, long m) {
        return ((a % m) + (b % m)) % m;
    }
    
    static long modSub(long a, long b, long m) {
        return ((a % m) - (b % m) + m) % m;
    }
    
    static long modMul(long a, long b, long m) {
        return ((a % m) * (b % m)) % m;
    }
    
    // ==================== SORTING (Anti-hack) ====================
    // IMPORTANT: Always shuffle before sorting to avoid O(n²) worst case
    static void shuffleSort(int[] arr) {
        int n = arr.length;
        for (int i = n - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
        Arrays.sort(arr);
    }
    
    static void shuffleSort(long[] arr) {
        int n = arr.length;
        for (int i = n - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            long temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
        Arrays.sort(arr);
    }
    
    // ==================== DSU (Union-Find) ====================
    static class DSU {
        int[] parent, rank;
        int components;
        
        DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            components = n;
            for (int i = 0; i < n; i++) parent[i] = i;
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
            if (rank[x] == rank[y]) rank[x]++;
            components--;
            return true;
        }
        
        boolean same(int x, int y) { return find(x) == find(y); }
    }
    
    // ==================== BFS ====================
    static int[] bfs(int start, List<List<Integer>> adj) {
        int n = adj.size();
        int[] dist = new int[n];
        Arrays.fill(dist, -1);
        
        ArrayDeque<Integer> q = new ArrayDeque<>();
        q.add(start);
        dist[start] = 0;
        
        while (!q.isEmpty()) {
            int u = q.poll();
            for (int v : adj.get(u)) {
                if (dist[v] == -1) {
                    dist[v] = dist[u] + 1;
                    q.add(v);
                }
            }
        }
        return dist;
    }
    
    // ==================== Dijkstra ====================
    static long[] dijkstra(int src, List<List<long[]>> adj) {
        int n = adj.size();
        long[] dist = new long[n];
        Arrays.fill(dist, INF);
        dist[src] = 0;
        
        PriorityQueue<long[]> pq = new PriorityQueue<>(Comparator.comparingLong(a -> a[1]));
        pq.add(new long[]{src, 0});
        
        while (!pq.isEmpty()) {
            long[] node = pq.poll();
            int u = (int) node[0];
            long d = node[1];
            
            if (d > dist[u]) continue;
            
            for (long[] edge : adj.get(u)) {
                int v = (int) edge[0];
                long w = edge[1];
                if (dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    pq.add(new long[]{v, dist[v]});
                }
            }
        }
        return dist;
    }
}
