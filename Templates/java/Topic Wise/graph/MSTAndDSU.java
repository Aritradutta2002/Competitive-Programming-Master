/*
 * Author  : Aritra Dutta
 * Target  : Codeforces Expert / CSES
 *
 * MST AND DSU Template - Minimum Spanning Tree and Disjoint Set Union
 * Includes: DSU (with rank, path compression, component count),
 *           Kruskal's MST (with WeightedEdge), Prim's MST
 *
 * USAGE: Copy the Graph class or specific algorithms you need into your solution
 */
import java.io.*;
import java.util.*;

public class MSTAndDSU {
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
        List<Graph.WeightedEdge> edges = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            int u = nextInt() - 1, v = nextInt() - 1;
            long w = nextLong();
            g.addEdge(u, v, w);
            edges.add(new Graph.WeightedEdge(u, v, w));
        }
        out.println(g.kruskal(edges));
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
            int getComponents() { return components; }
        }

        // ==================== KRUSKAL'S MST ====================
        static class WeightedEdge implements Comparable<WeightedEdge> {
            int u, v;
            long weight;
            WeightedEdge(int u, int v, long w) { this.u = u; this.v = v; this.weight = w; }
            public int compareTo(WeightedEdge o) { return Long.compare(weight, o.weight); }
        }

        long kruskal(List<WeightedEdge> edges) {
            Collections.sort(edges);
            DSU dsu = new DSU(n);
            long mstWeight = 0;
            int edgesUsed = 0;

            for (WeightedEdge e : edges) {
                if (dsu.unite(e.u, e.v)) {
                    mstWeight += e.weight;
                    edgesUsed++;
                }
            }
            return edgesUsed == n - 1 ? mstWeight : -1; // -1 if not connected
        }

        // ==================== PRIMS MST ====================
        long prim() {
            boolean[] visited = new boolean[n];
            PriorityQueue<long[]> pq = new PriorityQueue<>(Comparator.comparingLong(a -> a[1]));
            pq.add(new long[]{0, 0}); // {node, weight}
            long mstWeight = 0;
            int edgesUsed = 0;

            while (!pq.isEmpty() && edgesUsed < n) {
                long[] node = pq.poll();
                int u = (int) node[0];
                long w = node[1];

                if (visited[u]) continue;
                visited[u] = true;
                mstWeight += w;
                edgesUsed++;

                for (Edge e : adj.get(u)) {
                    if (!visited[e.to]) {
                        pq.add(new long[]{e.to, e.weight});
                    }
                }
            }
            return edgesUsed == n ? mstWeight : -1;
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
