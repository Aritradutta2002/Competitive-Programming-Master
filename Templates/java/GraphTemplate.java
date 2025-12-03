/*
 * Author  : Aritra Dutta
 * Target  : Codeforces Expert / CSES
 * 
 * GRAPH Template - BFS, DFS, Dijkstra, DSU, Topological Sort
 * Uses optimized I/O for CSES acceptance
 */
import java.io.*;
import java.util.*;

public class GraphTemplate {
    static BufferedReader br;
    static StringTokenizer st;
    static PrintWriter out;
    
    static final long INF = (long) 1e18;
    
    public static void main(String[] args) throws IOException {
        br = new BufferedReader(new InputStreamReader(System.in));
        out = new PrintWriter(new BufferedOutputStream(System.out));
        
        solve();
        
        out.flush();
        out.close();
    }
    
    static void solve() throws IOException {
        int n = nextInt(), m = nextInt();
        
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        
        for (int i = 0; i < m; i++) {
            int u = nextInt() - 1, v = nextInt() - 1;
            adj.get(u).add(v);
            adj.get(v).add(u);  // Remove for directed
        }
        
        // Example: BFS from node 0
        int[] dist = bfs(0, adj);
        printArray(dist);
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
    
    // ==================== DFS (Iterative - no stack overflow) ====================
    static void dfs(int start, List<List<Integer>> adj, boolean[] visited) {
        ArrayDeque<Integer> stack = new ArrayDeque<>();
        stack.push(start);
        
        while (!stack.isEmpty()) {
            int u = stack.pop();
            if (visited[u]) continue;
            visited[u] = true;
            
            for (int v : adj.get(u)) {
                if (!visited[v]) stack.push(v);
            }
        }
    }
    
    // ==================== Dijkstra ====================
    static long[] dijkstra(int src, List<List<int[]>> adj) {
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
            
            for (int[] edge : adj.get(u)) {
                int v = edge[0];
                long w = edge[1];
                if (dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    pq.add(new long[]{v, dist[v]});
                }
            }
        }
        return dist;
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
    
    // ==================== Topological Sort (Kahn's) ====================
    static int[] toposort(List<List<Integer>> adj) {
        int n = adj.size();
        int[] indeg = new int[n];
        
        for (int u = 0; u < n; u++) {
            for (int v : adj.get(u)) indeg[v]++;
        }
        
        ArrayDeque<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (indeg[i] == 0) q.add(i);
        }
        
        int[] order = new int[n];
        int idx = 0;
        
        while (!q.isEmpty()) {
            int u = q.poll();
            order[idx++] = u;
            for (int v : adj.get(u)) {
                if (--indeg[v] == 0) q.add(v);
            }
        }
        
        return idx == n ? order : null;  // null if cycle exists
    }
    
    // ==================== FAST I/O ====================
    static String next() throws IOException {
        while (st == null || !st.hasMoreTokens())
            st = new StringTokenizer(br.readLine());
        return st.nextToken();
    }
    
    static int nextInt() throws IOException { return Integer.parseInt(next()); }
    static long nextLong() throws IOException { return Long.parseLong(next()); }
    
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
}
