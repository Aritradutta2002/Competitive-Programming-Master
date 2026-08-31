/*
 * Author  : Aritra Dutta
 * Target  : Codeforces Expert / CSES
 *
 * ADVANCED GRAPH Template - SCC, Bridges, Articulation Points, Max Flow
 * Includes: Kosaraju SCC, Bridges (findBridges), Articulation Points,
 *           Dinic MaxFlow (full Dinic class), MinCut
 *
 * USAGE: Copy the Graph class or specific algorithms you need into your solution
 */
import java.io.*;
import java.util.*;

public class AdvancedGraph {
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
        Graph g = new Graph(n, true);
        for (int i = 0; i < m; i++) {
            int u = nextInt() - 1, v = nextInt() - 1;
            long w = nextLong();
            g.addEdge(u, v, w);
        }
        int[] scc = g.kosarajuSCC();
        for (int i = 0; i < n; i++) out.print(scc[i] + " ");
        out.println();
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
            Set<Integer> points = new HashSet<>();
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

        void dfsAP(int u, int[] disc, int[] low, int[] parent, int[] time, Set<Integer> points) {
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
