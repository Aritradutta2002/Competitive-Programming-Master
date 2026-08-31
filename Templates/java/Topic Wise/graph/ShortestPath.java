/*
 * Author  : Aritra Dutta
 * Target  : Codeforces Expert / CSES
 *
 * SHORTEST PATH Template - Dijkstra, Bellman-Ford, Floyd-Warshall
 * Includes: Dijkstra (with path reconstruction), Bellman-Ford (with negative cycle check),
 *           Floyd-Warshall (all-pairs shortest path)
 *
 * USAGE: Copy the Graph class or specific algorithms you need into your solution
 */
import java.io.*;
import java.util.*;

public class ShortestPath {
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
            long w = nextLong();
            g.addEdge(u, v, w);
        }
        out.println(g.dijkstra(0)[n - 1]);
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

        // ==================== DIJKSTRA ====================
        long[] dijkstra(int src) {
            long[] dist = new long[n];
            Arrays.fill(dist, Long.MAX_VALUE);
            dist[src] = 0;

            PriorityQueue<long[]> pq = new PriorityQueue<>(Comparator.comparingLong(a -> a[1]));
            pq.add(new long[]{src, 0});

            while (!pq.isEmpty()) {
                long[] node = pq.poll();
                int u = (int) node[0];
                long d = node[1];

                if (d > dist[u]) continue;

                for (Edge e : adj.get(u)) {
                    if (dist[u] + e.weight < dist[e.to]) {
                        dist[e.to] = dist[u] + e.weight;
                        pq.add(new long[]{e.to, dist[e.to]});
                    }
                }
            }
            return dist;
        }

        // Dijkstra with path reconstruction
        long[] dijkstraWithPath(int src, int[] parent) {
            long[] dist = new long[n];
            Arrays.fill(dist, Long.MAX_VALUE);
            Arrays.fill(parent, -1);
            dist[src] = 0;

            PriorityQueue<long[]> pq = new PriorityQueue<>(Comparator.comparingLong(a -> a[1]));
            pq.add(new long[]{src, 0});

            while (!pq.isEmpty()) {
                long[] node = pq.poll();
                int u = (int) node[0];
                long d = node[1];

                if (d > dist[u]) continue;

                for (Edge e : adj.get(u)) {
                    if (dist[u] + e.weight < dist[e.to]) {
                        dist[e.to] = dist[u] + e.weight;
                        parent[e.to] = u;
                        pq.add(new long[]{e.to, dist[e.to]});
                    }
                }
            }
            return dist;
        }

        // ==================== BELLMAN-FORD ====================
        long[] bellmanFord(int src) {
            long[] dist = new long[n];
            Arrays.fill(dist, Long.MAX_VALUE);
            dist[src] = 0;

            for (int i = 0; i < n - 1; i++) {
                for (int u = 0; u < n; u++) {
                    if (dist[u] == Long.MAX_VALUE) continue;
                    for (Edge e : adj.get(u)) {
                        if (dist[u] + e.weight < dist[e.to]) {
                            dist[e.to] = dist[u] + e.weight;
                        }
                    }
                }
            }
            return dist;
        }

        // Check for negative cycle
        boolean hasNegativeCycle(long[] dist) {
            for (int u = 0; u < n; u++) {
                if (dist[u] == Long.MAX_VALUE) continue;
                for (Edge e : adj.get(u)) {
                    if (dist[u] + e.weight < dist[e.to]) return true;
                }
            }
            return false;
        }

        // ==================== FLOYD-WARSHALL ====================
        long[][] floydWarshall() {
            long[][] dist = new long[n][n];
            for (long[] row : dist) Arrays.fill(row, Long.MAX_VALUE);
            for (int i = 0; i < n; i++) dist[i][i] = 0;

            for (int u = 0; u < n; u++) {
                for (Edge e : adj.get(u)) {
                    dist[u][e.to] = Math.min(dist[u][e.to], e.weight);
                }
            }

            for (int k = 0; k < n; k++) {
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        if (dist[i][k] != Long.MAX_VALUE && dist[k][j] != Long.MAX_VALUE) {
                            dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
                        }
                    }
                }
            }
            return dist;
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
