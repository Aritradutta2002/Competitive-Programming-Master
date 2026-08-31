/*
 * Author  : Aritra Dutta
 * Target  : Codeforces Expert / CSES
 *
 * TREE ALGORITHMS Template - Complete Tree Processing Library
 * Includes: LCA, Tree DP, Euler Tour, HLD, Centroid Decomposition,
 *           Tree Diameter, Centers, etc.
 *
 * USAGE: Import specific classes or copy algorithms you need
 */
import java.io.*;
import java.util.*;

public class TreeAlgorithms {
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

    // ==================== LCA (Lowest Common Ancestor) ====================
    static class LCA {
        int n, LOG;
        int[][] up;
        int[] depth;

        // Binary lifting LCA - O(n log n) preprocessing, O(log n) query
        LCA(List<List<Integer>> adj, int root) {
            n = adj.size();
            LOG = 32 - Integer.numberOfLeadingZeros(n);
            up = new int[n][LOG];
            depth = new int[n];

            for (int[] row : up) Arrays.fill(row, -1);

            // BFS to avoid stack overflow
            bfs(adj, root);
            buildSparseTable();
        }

        void bfs(List<List<Integer>> adj, int root) {
            ArrayDeque<int[]> queue = new ArrayDeque<>();
            queue.add(new int[]{root, -1});
            depth[root] = 0;

            while (!queue.isEmpty()) {
                int[] curr = queue.poll();
                int u = curr[0], p = curr[1];

                up[u][0] = p;
                for (int v : adj.get(u)) {
                    if (v != p) {
                        depth[v] = depth[u] + 1;
                        queue.add(new int[]{v, u});
                    }
                }
            }
        }

        void buildSparseTable() {
            for (int j = 1; j < LOG; j++) {
                for (int i = 0; i < n; i++) {
                    if (up[i][j - 1] != -1) {
                        up[i][j] = up[up[i][j - 1]][j - 1];
                    }
                }
            }
        }

        int lca(int u, int v) {
            if (depth[u] < depth[v]) { int t = u; u = v; v = t; }
            int diff = depth[u] - depth[v];

            // Lift u to same depth as v
            for (int i = 0; i < LOG; i++) {
                if (((diff >> i) & 1) == 1) u = up[u][i];
            }
            if (u == v) return u;

            // Lift both until they meet
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

        // Check if u is ancestor of v
        boolean isAncestor(int u, int v) {
            return lca(u, v) == u;
        }
    }

    // ==================== EULER TOUR ====================
    static class EulerTour {
        int[] tin, tout;
        int[] euler; // Euler tour array
        int[] first; // First occurrence of each node
        int timer = 0;
        int n;

        EulerTour(List<List<Integer>> adj, int root) {
            n = adj.size();
            tin = new int[n];
            tout = new int[n];
            euler = new int[2 * n];
            first = new int[n];
            Arrays.fill(first, -1);

            dfs(adj, root, -1);
        }

        void dfs(List<List<Integer>> adj, int u, int p) {
            tin[u] = timer;
            euler[timer] = u;
            if (first[u] == -1) first[u] = timer;
            timer++;

            for (int v : adj.get(u)) {
                if (v != p) {
                    dfs(adj, v, u);
                    euler[timer++] = u;
                }
            }
            tout[u] = timer;
        }

        boolean isAncestor(int u, int v) {
            return tin[u] <= tin[v] && tout[v] <= tout[u];
        }

        // LCA using Euler Tour + RMQ (Sparse Table)
        int lca(int u, int v) {
            int l = first[u], r = first[v];
            if (l > r) { int t = l; l = r; r = t; }

            int minIdx = l;
            for (int i = l; i <= r; i++) {
                if (tin[euler[i]] < tin[euler[minIdx]]) {
                    minIdx = i;
                }
            }
            return euler[minIdx];
        }
    }

    // ==================== TREE DIAMETER ====================
    static class TreeDiameter {
        int diameter;
        int endpoint1, endpoint2;
        int[] distFromEnd;

        TreeDiameter(List<List<Integer>> adj) {
            int n = adj.size();
            int[] dist = new int[n];

            // First BFS from arbitrary node
            endpoint1 = bfsFarthest(0, adj, dist);
            
            // Second BFS from farthest node
            distFromEnd = new int[n];
            endpoint2 = bfsFarthest(endpoint1, adj, distFromEnd);
            diameter = distFromEnd[endpoint2];
        }

        int bfsFarthest(int start, List<List<Integer>> adj, int[] dist) {
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

        int getDiameter() { return diameter; }
        int getEndpoint1() { return endpoint1; }
        int getEndpoint2() { return endpoint2; }

        // Distance from any node to the diameter path
        int distToDiameter(int u) {
            return Math.min(distFromEnd[u], diameter - distFromEnd[u]);
        }
    }

    // ==================== TREE CENTERS ====================
    static class TreeCenters {
        List<Integer> centers;

        TreeCenters(List<List<Integer>> adj) {
            int n = adj.size();
            int[] degree = new int[n];
            for (int u = 0; u < n; u++) {
                degree[u] = adj.get(u).size();
            }

            ArrayDeque<Integer> leaves = new ArrayDeque<>();
            for (int i = 0; i < n; i++) {
                if (degree[i] <= 1) leaves.add(i);
            }

            int remaining = n;
            while (remaining > 2) {
                int sz = leaves.size();
                remaining -= sz;

                for (int i = 0; i < sz; i++) {
                    int u = leaves.poll();
                    for (int v : adj.get(u)) {
                        if (--degree[v] == 1) {
                            leaves.add(v);
                        }
                    }
                }
            }

            centers = new ArrayList<>(leaves);
        }

        List<Integer> getCenters() { return centers; }
    }

    // ==================== SUBTREE SIZE ====================
    static class SubtreeSize {
        int[] size;
        int[] parent;
        int[] depth;
        int root;

        SubtreeSize(List<List<Integer>> adj, int root) {
            this.root = root;
            int n = adj.size();
            size = new int[n];
            parent = new int[n];
            depth = new int[n];
            Arrays.fill(parent, -1);
            Arrays.fill(size, 1);

            // BFS for order
            List<Integer> order = new ArrayList<>();
            ArrayDeque<Integer> q = new ArrayDeque<>();
            q.add(root);

            while (!q.isEmpty()) {
                int u = q.poll();
                order.add(u);
                for (int v : adj.get(u)) {
                    if (v != parent[u]) {
                        parent[v] = u;
                        depth[v] = depth[u] + 1;
                        q.add(v);
                    }
                }
            }

            // Process in reverse order
            for (int i = order.size() - 1; i >= 0; i--) {
                int u = order.get(i);
                if (parent[u] != -1) size[parent[u]] += size[u];
            }
        }

        int getSize(int u) { return size[u]; }
        int getParent(int u) { return parent[u]; }
        int getDepth(int u) { return depth[u]; }
    }

    // ==================== HEAVY-LIGHT DECOMPOSITION ====================
    static class HLD {
        int n;
        int[] parent, depth, heavy, head, pos, size;
        int curPos;

        HLD(List<List<Integer>> adj, int root) {
            n = adj.size();
            parent = new int[n];
            depth = new int[n];
            heavy = new int[n];
            head = new int[n];
            pos = new int[n];
            size = new int[n];
            Arrays.fill(heavy, -1);
            curPos = 0;

            dfsSize(adj, root, -1, 0);
            dfsHLD(adj, root, -1, root);
        }

        void dfsSize(List<List<Integer>> adj, int u, int p, int d) {
            parent[u] = p;
            depth[u] = d;
            size[u] = 1;
            int maxSubtree = 0;

            for (int v : adj.get(u)) {
                if (v != p) {
                    dfsSize(adj, v, u, d + 1);
                    size[u] += size[v];
                    if (size[v] > maxSubtree) {
                        maxSubtree = size[v];
                        heavy[u] = v;
                    }
                }
            }
        }

        void dfsHLD(List<List<Integer>> adj, int u, int p, int h) {
            head[u] = h;
            pos[u] = curPos++;

            if (heavy[u] != -1) {
                dfsHLD(adj, heavy[u], u, h);
            }

            for (int v : adj.get(u)) {
                if (v != p && v != heavy[u]) {
                    dfsHLD(adj, v, u, v);
                }
            }
        }

        // Query on path from u to v
        // combine: function to combine results, querySeg: query on segment [l, r]
        long queryPath(int u, int v, java.util.function.LongUnaryOperator combine,
                      java.util.function.LongBinaryOperator querySeg, long[] tree) {
            long result = -1;

            while (head[u] != head[v]) {
                if (depth[head[u]] < depth[head[v]]) {
                    int t = u; u = v; v = t;
                }
                long segResult = querySeg.applyAsLong(pos[head[u]], pos[u]);
                if (result == -1) result = segResult;
                else result = combine.applyAsLong(result) + segResult;
                u = parent[head[u]];
            }

            if (depth[u] > depth[v]) { int t = u; u = v; v = t; }
            long segResult = querySeg.applyAsLong(pos[u], pos[v]);
            if (result == -1) result = segResult;
            else result = combine.applyAsLong(result) + segResult;

            return result;
        }

        int lca(int u, int v) {
            while (head[u] != head[v]) {
                if (depth[head[u]] > depth[head[v]]) {
                    u = parent[head[u]];
                } else {
                    v = parent[head[v]];
                }
            }
            return depth[u] < depth[v] ? u : v;
        }

        int dist(int u, int v) {
            return depth[u] + depth[v] - 2 * depth[lca(u, v)];
        }
    }

    // ==================== CENTROID DECOMPOSITION ====================
    static class CentroidDecomposition {
        int n;
        int[] parent;
        boolean[] removed;
        List<List<Integer>> adj;
        List<Integer>[] ancestors;

        @SuppressWarnings("unchecked")
        CentroidDecomposition(List<List<Integer>> adj, int root) {
            this.adj = adj;
            n = adj.size();
            parent = new int[n];
            removed = new boolean[n];
            ancestors = new List[n];
            for (int i = 0; i < n; i++) ancestors[i] = new ArrayList<>();

            build(root, -1);
        }

        void build(int u, int p) {
            int centroid = findCentroid(u, -1, getSubtreeSize(u, -1));
            parent[centroid] = p;
            removed[centroid] = true;

            for (int v : adj.get(centroid)) {
                if (!removed[v]) {
                    build(v, centroid);
                }
            }

            // Build ancestor list for each node
            buildAncestors(u, centroid, new ArrayList<>());
        }

        int getSubtreeSize(int u, int p) {
            int size = 1;
            for (int v : adj.get(u)) {
                if (v != p && !removed[v]) {
                    size += getSubtreeSize(v, u);
                }
            }
            return size;
        }

        int findCentroid(int u, int p, int totalSize) {
            for (int v : adj.get(u)) {
                if (v != p && !removed[v] && getSubtreeSize(v, u) > totalSize / 2) {
                    return findCentroid(v, u, totalSize);
                }
            }
            return u;
        }

        void buildAncestors(int u, int centroid, List<Integer> path) {
            path.add(centroid);
            ancestors[u] = new ArrayList<>(path);

            for (int v : adj.get(u)) {
                if (!removed[v] && v != (path.size() > 1 ? path.get(path.size() - 2) : -1)) {
                    buildAncestors(v, centroid, path);
                }
            }
            path.remove(path.size() - 1);
        }

        List<Integer> getAncestors(int u) { return ancestors[u]; }

        // Distance between two nodes using centroid decomposition
        int dist(int u, int v, int[] depth) {
            int lca = getLCA(u, v);
            return depth[u] + depth[v] - 2 * depth[lca];
        }

        int getLCA(int u, int v) {
            Set<Integer> uAncestors = new HashSet<>(ancestors[u]);
            for (int anc : ancestors[v]) {
                if (uAncestors.contains(anc)) return anc;
            }
            return -1;
        }
    }

    // ==================== TREE DP ====================
    static class TreeDP {
        int n;
        long[][] dp;
        List<List<Integer>> adj;

        // Maximum independent set on tree
        static long[] maxIndependentSet(List<List<Integer>> adj, int root) {
            int n = adj.size();
            long[][] dp = new long[n][2]; // dp[u][0] = not take, dp[u][1] = take

            dfsMaxIS(adj, root, -1, dp);
            return new long[]{dp[root][0], dp[root][1]};
        }

        static void dfsMaxIS(List<List<Integer>> adj, int u, int p, long[][] dp) {
            dp[u][0] = 0;
            dp[u][1] = 1;

            for (int v : adj.get(u)) {
                if (v != p) {
                    dfsMaxIS(adj, v, u, dp);
                    dp[u][0] += Math.max(dp[v][0], dp[v][1]);
                    dp[u][1] += dp[v][0];
                }
            }
        }

        // Tree diameter using DP
        static int[] treeDiameterDP(List<List<Integer>> adj, int root) {
            int n = adj.size();
            int[] maxDepth = new int[n];
            int[] diameter = new int[1];

            dfsDiameter(adj, root, -1, maxDepth, diameter);
            return new int[]{diameter[0]};
        }

        static int dfsDiameter(List<List<Integer>> adj, int u, int p, int[] maxDepth, int[] diameter) {
            int max1 = 0, max2 = 0;

            for (int v : adj.get(u)) {
                if (v != p) {
                    int d = dfsDiameter(adj, v, u, maxDepth, diameter) + 1;
                    if (d > max1) {
                        max2 = max1;
                        max1 = d;
                    } else if (d > max2) {
                        max2 = d;
                    }
                }
            }

            diameter[0] = Math.max(diameter[0], max1 + max2);
            maxDepth[u] = max1;
            return max1;
        }

        // Count paths of length k
        static long countPathsOfLengthK(List<List<Integer>> adj, int k) {
            int n = adj.size();
            long[] count = new long[1];
            int[] depth = new int[n];

            dfsCountPaths(adj, 0, -1, 0, k, depth, count);
            return count[0];
        }

        static void dfsCountPaths(List<List<Integer>> adj, int u, int p, int d, int k, int[] depth, long[] count) {
            depth[u] = d;
            for (int v : adj.get(u)) {
                if (v != p) {
                    dfsCountPaths(adj, v, u, d + 1, k, depth, count);
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
