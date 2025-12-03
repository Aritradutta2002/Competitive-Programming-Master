/*
 * Author  : Aritra Dutta
 * Target  : Codeforces Expert / CSES
 * 
 * TREE Template - LCA, Tree DP, Euler Tour
 * Uses optimized I/O for CSES acceptance
 */
import java.io.*;
import java.util.*;

public class TreeTemplate {
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
        int n = nextInt();
        
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        
        for (int i = 0; i < n - 1; i++) {
            int u = nextInt() - 1, v = nextInt() - 1;
            adj.get(u).add(v);
            adj.get(v).add(u);
        }
        
        LCA lca = new LCA(adj, 0);
        
        int q = nextInt();
        StringBuilder sb = new StringBuilder();
        while (q-- > 0) {
            int u = nextInt() - 1, v = nextInt() - 1;
            sb.append(lca.dist(u, v)).append('\n');
        }
        out.print(sb);
    }
    
    // ==================== LCA (Binary Lifting) ====================
    static class LCA {
        int n, LOG;
        int[][] up;
        int[] depth;
        
        LCA(List<List<Integer>> adj, int root) {
            n = adj.size();
            LOG = 32 - Integer.numberOfLeadingZeros(n);
            up = new int[n][LOG];
            depth = new int[n];
            
            for (int[] row : up) Arrays.fill(row, -1);
            
            // BFS to avoid stack overflow
            ArrayDeque<int[]> queue = new ArrayDeque<>();
            queue.add(new int[]{root, -1});
            
            while (!queue.isEmpty()) {
                int[] curr = queue.poll();
                int u = curr[0], p = curr[1];
                
                up[u][0] = p;
                for (int i = 1; i < LOG; i++) {
                    if (up[u][i-1] != -1) up[u][i] = up[up[u][i-1]][i-1];
                }
                
                for (int v : adj.get(u)) {
                    if (v != p) {
                        depth[v] = depth[u] + 1;
                        queue.add(new int[]{v, u});
                    }
                }
            }
        }
        
        int lca(int u, int v) {
            if (depth[u] < depth[v]) { int t = u; u = v; v = t; }
            int diff = depth[u] - depth[v];
            
            for (int i = 0; i < LOG; i++) {
                if (((diff >> i) & 1) == 1) u = up[u][i];
            }
            if (u == v) return u;
            
            for (int i = LOG - 1; i >= 0; i--) {
                if (up[u][i] != up[v][i]) {
                    u = up[u][i];
                    v = up[v][i];
                }
            }
            return up[u][0];
        }
        
        int dist(int u, int v) {
            return depth[u] + depth[v] - 2 * depth[lca(u, v)];
        }
        
        int kthAncestor(int u, int k) {
            for (int i = 0; i < LOG; i++) {
                if (((k >> i) & 1) == 1) {
                    u = up[u][i];
                    if (u == -1) return -1;
                }
            }
            return u;
        }
    }
    
    // ==================== Euler Tour ====================
    static class EulerTour {
        int[] tin, tout;
        int timer = 0;
        
        EulerTour(List<List<Integer>> adj, int root) {
            int n = adj.size();
            tin = new int[n];
            tout = new int[n];
            
            // Iterative DFS to avoid stack overflow
            ArrayDeque<int[]> stack = new ArrayDeque<>();
            stack.push(new int[]{root, -1, 0});  // node, parent, state
            
            while (!stack.isEmpty()) {
                int[] curr = stack.pop();
                int u = curr[0], p = curr[1], state = curr[2];
                
                if (state == 0) {
                    tin[u] = timer++;
                    stack.push(new int[]{u, p, 1});
                    for (int v : adj.get(u)) {
                        if (v != p) stack.push(new int[]{v, u, 0});
                    }
                } else {
                    tout[u] = timer;
                }
            }
        }
        
        boolean isAncestor(int u, int v) {
            return tin[u] <= tin[v] && tout[v] <= tout[u];
        }
    }
    
    // ==================== Tree Diameter ====================
    static int[] treeDiameter(List<List<Integer>> adj) {
        int n = adj.size();
        int[] dist = new int[n];
        
        int u = bfsFarthest(0, adj, dist);
        int v = bfsFarthest(u, adj, dist);
        
        return new int[]{dist[v], v};  // {diameter, endpoint}
    }
    
    static int bfsFarthest(int start, List<List<Integer>> adj, int[] dist) {
        Arrays.fill(dist, -1);
        ArrayDeque<Integer> q = new ArrayDeque<>();
        q.add(start);
        dist[start] = 0;
        int farthest = start;
        
        while (!q.isEmpty()) {
            int u = q.poll();
            for (int v : adj.get(u)) {
                if (dist[v] == -1) {
                    dist[v] = dist[u] + 1;
                    q.add(v);
                    if (dist[v] > dist[farthest]) farthest = v;
                }
            }
        }
        return farthest;
    }
    
    // ==================== Subtree Size ====================
    static int[] subtreeSize(List<List<Integer>> adj, int root) {
        int n = adj.size();
        int[] sz = new int[n];
        int[] parent = new int[n];
        Arrays.fill(sz, 1);
        Arrays.fill(parent, -1);
        
        // BFS to get order
        List<Integer> order = new ArrayList<>();
        ArrayDeque<Integer> q = new ArrayDeque<>();
        q.add(root);
        
        while (!q.isEmpty()) {
            int u = q.poll();
            order.add(u);
            for (int v : adj.get(u)) {
                if (v != parent[u]) {
                    parent[v] = u;
                    q.add(v);
                }
            }
        }
        
        // Process in reverse order
        for (int i = order.size() - 1; i >= 0; i--) {
            int u = order.get(i);
            if (parent[u] != -1) sz[parent[u]] += sz[u];
        }
        
        return sz;
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
