/*
 * Author  : Aritra Dutta
 * Target  : Codeforces Expert / CSES
 *
 * ADVANCED GRAPH Template - Complete Graph Algorithms Library
 * Includes: BFS, DFS, Dijkstra, Bellman-Ford, Floyd-Warshall, DSU, MST,
 *           Topological Sort, SCC, Bridges, Articulation Points, Max Flow, etc.
 *
 * USAGE: Import the Graph class or copy specific algorithms you need
 */
import java.io.*;
import java.util.*;

public class GraphAlgorithms {
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

        // ==================== STRONGLY CONNECTED COMPONENTS (Kosaraju) ====================
        int[] kosarajuSCC() {
            // Step 1: DFS on original graph
            boolean[] visited = new boolean[n];
            List<Integer> order = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                if (!visited[i]) dfsOrder(i, visited, order);
            }

            // Step 2: DFS on reversed graph
            Graph reversed = new Graph(n, true);
            for (int u = 0; u < n; u++) {
                for (Edge e : adj.get(u)) {
                    reversed.addEdge(e.to, u, e.weight);
                }
            }

            Arrays.fill(visited, false);
            int[] component = new int[n];
            int compId = 0;

            Collections.reverse(order);
            for (int u : order) {
                if (!visited[u]) {
                    dfsComponent(u, visited, component, compId++, reversed);
                }
            }
            return component;
        }

        void dfsOrder(int u, boolean[] visited, List<Integer> order) {
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

        void dfsComponent(int u, boolean[] visited, int[] component, int compId, Graph g) {
            ArrayDeque<Integer> stack = new ArrayDeque<>();
            stack.push(u);
            visited[u] = true;

            while (!stack.isEmpty()) {
                int node = stack.pop();
                component[node] = compId;
                for (Edge e : g.adj.get(node)) {
                    if (!visited[e.to]) {
                        visited[e.to] = true;
                        stack.push(e.to);
                    }
                }
            }
        }

        // ==================== BRIDGES ====================
        List<int[]> findBridges() {
            List<int[]> bridges = new ArrayList<>();
            int[] disc = new int[n];
            int[] low = new int[n];
            int[] parent = new int[n];
            Arrays.fill(disc, -1);
            Arrays.fill(parent, -1);
            int[] time = {0};

            for (int i = 0; i < n; i++) {
                if (disc[i] == -1) dfsBridges(i, disc, low, parent, time, bridges);
            }
            return bridges;
        }

        void dfsBridges(int u, int[] disc, int[] low, int[] parent, int[] time, List<int[]> bridges) {
            disc[u] = low[u] = time[0]++;
            
            for (Edge e : adj.get(u)) {
                int v = e.to;
                if (disc[v] == -1) {
                    parent[v] = u;
                    dfsBridges(v, disc, low, parent, time, bridges);
                    low[u] = Math.min(low[u], low[v]);

                    if (low[v] > disc[u]) {
                        bridges.add(new int[]{u, v});
                    }
                } else if (v != parent[u]) {
                    low[u] = Math.min(low[u], disc[v]);
                }
            }
        }

        // ==================== ARTICULATION POINTS ====================
        List<Integer> findArticulationPoints() {
            List<Integer> points = new HashSet<>();
            int[] disc = new int[n];
            int[] low = new int[n];
            int[] parent = new int[n];
            Arrays.fill(disc, -1);
            Arrays.fill(parent, -1);
            int[] time = {0};

            for (int i = 0; i < n; i++) {
                if (disc[i] == -1) dfsAP(i, disc, low, parent, time, points);
            }
            return new ArrayList<>(points);
        }

        void dfsAP(int u, int[] disc, int[] low, int[] parent, int[] time, List<Integer> points) {
            disc[u] = low[u] = time[0]++;
            int children = 0;
            boolean isAP = false;

            for (Edge e : adj.get(u)) {
                int v = e.to;
                if (disc[v] == -1) {
                    children++;
                    parent[v] = u;
                    dfsAP(v, disc, low, parent, time, points);
                    low[u] = Math.min(low[u], low[v]);

                    if (parent[u] != -1 && low[v] >= disc[u]) isAP = true;
                } else if (v != parent[u]) {
                    low[u] = Math.min(low[u], disc[v]);
                }
            }

            if (parent[u] == -1 && children > 1) isAP = true;
            if (isAP) points.add(u);
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

        // ==================== MAXIMUM FLOW (Dinic) ====================
        static class Dinic {
            int n, s, t;
            List<List<FlowEdge>> adj;
            int[] level, ptr;

            static class FlowEdge {
                int to, rev;
                long cap, flow;
                FlowEdge(int to, int rev, long cap) {
                    this.to = to; this.rev = rev;
                    this.cap = cap; this.flow = 0;
                }
            }

            Dinic(int n, int s, int t) {
                this.n = n; this.s = s; this.t = t;
                adj = new ArrayList<>();
                for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
                level = new int[n];
                ptr = new int[n];
            }

            void addEdge(int from, int to, long cap) {
                adj.get(from).add(new FlowEdge(to, adj.get(to).size(), cap));
                adj.get(to).add(new FlowEdge(from, adj.get(from).size() - 1, 0));
            }

            boolean bfs() {
                Arrays.fill(level, -1);
                level[s] = 0;
                ArrayDeque<Integer> q = new ArrayDeque<>();
                q.add(s);

                while (!q.isEmpty()) {
                    int u = q.poll();
                    for (FlowEdge e : adj.get(u)) {
                        if (e.cap - e.flow > 0 && level[e.to] == -1) {
                            level[e.to] = level[u] + 1;
                            q.add(e.to);
                        }
                    }
                }
                return level[t] != -1;
            }

            long dfs(int u, long pushed) {
                if (pushed == 0 || u == t) return pushed;

                for (; ptr[u] < adj.get(u).size(); ptr[u]++) {
                    FlowEdge e = adj.get(u).get(ptr[u]);
                    if (level[u] + 1 != level[e.to] || e.cap - e.flow == 0) continue;

                    long tr = dfs(e.to, Math.min(pushed, e.cap - e.flow));
                    if (tr == 0) continue;

                    e.flow += tr;
                    adj.get(e.to).get(e.rev).flow -= tr;
                    return tr;
                }
                return 0;
            }

            long maxFlow() {
                long flow = 0;
                while (bfs()) {
                    Arrays.fill(ptr, 0);
                    while (true) {
                        long pushed = dfs(s, Long.MAX_VALUE);
                        if (pushed == 0) break;
                        flow += pushed;
                    }
                }
                return flow;
            }
        }

        // ==================== MINIMUM CUT ====================
        long minCut(int s, int t) {
            Dinic dinic = new Dinic(n, s, t);
            for (int u = 0; u < n; u++) {
                for (Edge e : adj.get(u)) {
                    dinic.addEdge(u, e.to, e.weight);
                }
            }
            return dinic.maxFlow();
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
