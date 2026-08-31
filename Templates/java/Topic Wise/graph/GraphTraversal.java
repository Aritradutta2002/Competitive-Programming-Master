/*
 * Author  : Aritra Dutta
 * Target  : Codeforces Expert / CSES
 *
 * GRAPH TRAVERSAL Template - BFS, DFS, Bipartite, Topological Sort
 * Includes: BFS (with path reconstruction), DFS (iterative), Bipartite check,
 *           Topological Sort (Kahn's algorithm + DFS-based)
 *
 * USAGE: Copy the Graph class or specific algorithms you need into your solution
 */
import java.io.*;
import java.util.*;

public class GraphTraversal {
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
        int n = nextInt(), m = nextInt();
        Graph g = new Graph(n);
        for (int i = 0; i < m; i++) {
            int u = nextInt() - 1, v = nextInt() - 1;
            g.addEdge(u, v);
        }
        int[] dist = g.bfs(0);
        out.println(dist[n - 1]);
    }

    // ==================== GRAPH CLASS ====================
    static class Graph {
        int n;
        List<List<Edge>> adj;
        boolean directed;

        static class Edge {
            int to;
            long weight;
            Edge(int to, long weight) { this.to = to; this.weight = weight; }
        }

        Graph(int n, boolean directed) {
            this.n = n;
            this.directed = directed;
            adj = new ArrayList<>();
            for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        }

        Graph(int n) { this(n, false); }

        void addEdge(int u, int v, long w) {
            adj.get(u).add(new Edge(v, w));
            if (!directed) adj.get(v).add(new Edge(u, w));
        }

        void addEdge(int u, int v) { addEdge(u, v, 1); }

        // ==================== BFS ====================
        int[] bfs(int start) {
            int[] dist = new int[n];
            Arrays.fill(dist, -1);
            ArrayDeque<Integer> q = new ArrayDeque<>();
            q.add(start);
            dist[start] = 0;

            while (!q.isEmpty()) {
                int u = q.poll();
                for (Edge e : adj.get(u)) {
                    if (dist[e.to] == -1) {
                        dist[e.to] = dist[u] + 1;
                        q.add(e.to);
                    }
                }
            }
            return dist;
        }

        // BFS returning parent array for path reconstruction
        int[] bfsWithParent(int start) {
            int[] parent = new int[n];
            Arrays.fill(parent, -1);
            ArrayDeque<Integer> q = new ArrayDeque<>();
            q.add(start);

            while (!q.isEmpty()) {
                int u = q.poll();
                for (Edge e : adj.get(u)) {
                    if (parent[e.to] == -1 && e.to != start) {
                        parent[e.to] = u;
                        q.add(e.to);
                    }
                }
            }
            return parent;
        }

        List<Integer> bfsPath(int start, int end) {
            int[] parent = bfsWithParent(start);
            if (parent[end] == -1 && start != end) return null;

            List<Integer> path = new ArrayList<>();
            for (int cur = end; cur != -1; cur = parent[cur]) {
                path.add(cur);
            }
            Collections.reverse(path);
            return path;
        }

        // ==================== DFS (Iterative) ====================
        boolean[] dfs(int start) {
            boolean[] visited = new boolean[n];
            ArrayDeque<Integer> stack = new ArrayDeque<>();
            stack.push(start);

            while (!stack.isEmpty()) {
                int u = stack.pop();
                if (visited[u]) continue;
                visited[u] = true;
                for (Edge e : adj.get(u)) {
                    if (!visited[e.to]) stack.push(e.to);
                }
            }
            return visited;
        }

        // ==================== BIPARTITE CHECK ====================
        boolean isBipartite() {
            int[] color = new int[n];
            Arrays.fill(color, -1);

            for (int i = 0; i < n; i++) {
                if (color[i] == -1 && !bipartiteBFS(i, color)) return false;
            }
            return true;
        }

        boolean bipartiteBFS(int start, int[] color) {
            ArrayDeque<Integer> q = new ArrayDeque<>();
            q.add(start);
            color[start] = 0;

            while (!q.isEmpty()) {
                int u = q.poll();
                for (Edge e : adj.get(u)) {
                    if (color[e.to] == -1) {
                        color[e.to] = 1 - color[u];
                        q.add(e.to);
                    } else if (color[e.to] == color[u]) {
                        return false;
                    }
                }
            }
            return true;
        }

        // ==================== TOPOLOGICAL SORT ====================
        int[] topologicalSort() {
            int[] indeg = new int[n];
            for (int u = 0; u < n; u++) {
                for (Edge e : adj.get(u)) indeg[e.to]++;
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
                for (Edge e : adj.get(u)) {
                    if (--indeg[e.to] == 0) q.add(e.to);
                }
            }
            return idx == n ? order : null; // null if cycle exists
        }

        // DFS-based topological sort
        int[] topologicalSortDFS() {
            boolean[] visited = new boolean[n];
            List<Integer> order = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                if (!visited[i]) topoDFS(i, visited, order);
            }

            Collections.reverse(order);
            int[] result = new int[n];
            for (int i = 0; i < n; i++) result[i] = order.get(i);
            return result;
        }

        void topoDFS(int u, boolean[] visited, List<Integer> order) {
            // Iterative to avoid stack overflow
            ArrayDeque<Integer> stack = new ArrayDeque<>();
            stack.push(u);

            while (!stack.isEmpty()) {
                int node = stack.peek();
                if (!visited[node]) {
                    visited[node] = true;
                    for (Edge e : adj.get(node)) {
                        if (!visited[e.to]) stack.push(e.to);
                    }
                } else {
                    stack.pop();
                    order.add(node);
                }
            }
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
