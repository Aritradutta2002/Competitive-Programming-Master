/*
 * Author  : Aritra Dutta
 * Target  : Codeforces Expert / CSES
 * 
 * TREE Template - LCA, Tree DP, Euler Tour, Centroid Decomposition
 */
#include <bits/stdc++.h>
using namespace std;

using ll = long long;
using vi = vector<int>;
using vll = vector<ll>;
using vvi = vector<vi>;

constexpr int LOG = 20;

#define rep(i, a, b) for (int i = (a); i < (b); ++i)
#define sz(x) (int)(x).size()

// ==================== LCA (Binary Lifting) ====================
struct LCA {
    int n, LOG;
    vvi up;
    vi depth;
    
    LCA(const vvi& adj, int root = 0) {
        n = sz(adj);
        LOG = __lg(n) + 1;
        up.assign(n, vi(LOG, -1));
        depth.assign(n, 0);
        
        function<void(int, int)> dfs = [&](int u, int p) {
            up[u][0] = p;
            rep(i, 1, LOG) {
                if (up[u][i-1] != -1) up[u][i] = up[up[u][i-1]][i-1];
            }
            for (int v : adj[u]) {
                if (v != p) {
                    depth[v] = depth[u] + 1;
                    dfs(v, u);
                }
            }
        };
        dfs(root, -1);
    }
    
    int lca(int u, int v) {
        if (depth[u] < depth[v]) swap(u, v);
        int diff = depth[u] - depth[v];
        
        rep(i, 0, LOG) if ((diff >> i) & 1) u = up[u][i];
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
        rep(i, 0, LOG) {
            if ((k >> i) & 1) {
                u = up[u][i];
                if (u == -1) return -1;
            }
        }
        return u;
    }
};

// ==================== Euler Tour (for subtree queries) ====================
struct EulerTour {
    int n, timer = 0;
    vi tin, tout, euler;
    
    EulerTour(const vvi& adj, int root = 0) {
        n = sz(adj);
        tin.resize(n);
        tout.resize(n);
        
        function<void(int, int)> dfs = [&](int u, int p) {
            tin[u] = timer++;
            euler.push_back(u);
            for (int v : adj[u]) {
                if (v != p) dfs(v, u);
            }
            tout[u] = timer;
        };
        dfs(root, -1);
    }
    
    // Subtree of u is [tin[u], tout[u])
    bool isAncestor(int u, int v) {
        return tin[u] <= tin[v] && tout[v] <= tout[u];
    }
};

// ==================== Tree Diameter ====================
pair<int, int> treeDiameter(const vvi& adj) {
    int n = sz(adj);
    vi dist(n, -1);
    
    auto bfs = [&](int start) -> int {
        fill(dist.begin(), dist.end(), -1);
        queue<int> q;
        q.push(start);
        dist[start] = 0;
        int farthest = start;
        
        while (!q.empty()) {
            int u = q.front(); q.pop();
            for (int v : adj[u]) {
                if (dist[v] == -1) {
                    dist[v] = dist[u] + 1;
                    q.push(v);
                    if (dist[v] > dist[farthest]) farthest = v;
                }
            }
        }
        return farthest;
    };
    
    int u = bfs(0);
    int v = bfs(u);
    return {dist[v], v};  // {diameter, endpoint}
}

// ==================== Subtree Size ====================
vi subtreeSize(const vvi& adj, int root = 0) {
    int n = sz(adj);
    vi sz(n, 1);
    
    function<void(int, int)> dfs = [&](int u, int p) {
        for (int v : adj[u]) {
            if (v != p) {
                dfs(v, u);
                sz[u] += sz[v];
            }
        }
    };
    dfs(root, -1);
    return sz;
}

// ==================== Tree DP (Max Independent Set) ====================
pair<ll, ll> treeDP(int u, int p, const vvi& adj, const vll& val) {
    ll take = val[u], skip = 0;
    
    for (int v : adj[u]) {
        if (v != p) {
            auto [t, s] = treeDP(v, u, adj, val);
            take += s;       // If we take u, we must skip children
            skip += max(t, s);  // If we skip u, children can be taken or skipped
        }
    }
    return {take, skip};
}

void solve() {
    int n;
    cin >> n;
    
    vvi adj(n);
    rep(i, 0, n - 1) {
        int u, v;
        cin >> u >> v;
        u--; v--;
        adj[u].push_back(v);
        adj[v].push_back(u);
    }
    
    LCA lca(adj);
    
    int q;
    cin >> q;
    while (q--) {
        int u, v;
        cin >> u >> v;
        u--; v--;
        cout << lca.dist(u, v) << '\n';
    }
}

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int t = 1;
    // cin >> t;
    while (t--) solve();
    
    return 0;
}
