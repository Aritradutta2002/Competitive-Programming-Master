/*
 * Author  : Aritra Dutta
 * Target  : Codeforces Expert / CSES
 * 
 * GRAPH Template - BFS, DFS, Dijkstra, DSU, Topological Sort
 */
#include <bits/stdc++.h>
using namespace std;

using ll = long long;
using pii = pair<int, int>;
using pll = pair<ll, ll>;
using vi = vector<int>;
using vll = vector<ll>;
using vvi = vector<vi>;

constexpr int INF = 1e9 + 5;
constexpr ll LINF = 1e18 + 5;

#define all(x) (x).begin(), (x).end()
#define sz(x) (int)(x).size()
#define pb push_back
#define fi first
#define se second
#define rep(i, a, b) for (int i = (a); i < (b); ++i)

// ==================== DSU (Union-Find) ====================
struct DSU {
    vi p, rank_;
    int components;
    
    DSU(int n) : p(n), rank_(n, 0), components(n) {
        iota(all(p), 0);
    }
    
    int find(int x) {
        return p[x] == x ? x : p[x] = find(p[x]);
    }
    
    bool unite(int x, int y) {
        x = find(x); y = find(y);
        if (x == y) return false;
        if (rank_[x] < rank_[y]) swap(x, y);
        p[y] = x;
        if (rank_[x] == rank_[y]) rank_[x]++;
        components--;
        return true;
    }
    
    bool same(int x, int y) { return find(x) == find(y); }
};

// ==================== BFS ====================
vi bfs(int start, const vvi& adj) {
    int n = sz(adj);
    vi dist(n, -1);
    queue<int> q;
    q.push(start);
    dist[start] = 0;
    
    while (!q.empty()) {
        int u = q.front(); q.pop();
        for (int v : adj[u]) {
            if (dist[v] == -1) {
                dist[v] = dist[u] + 1;
                q.push(v);
            }
        }
    }
    return dist;
}

// ==================== DFS ====================
void dfs(int u, int p, const vvi& adj, vi& vis) {
    vis[u] = 1;
    for (int v : adj[u]) {
        if (v != p && !vis[v]) {
            dfs(v, u, adj, vis);
        }
    }
}

// ==================== Dijkstra ====================
vll dijkstra(int src, const vector<vector<pll>>& adj) {
    int n = sz(adj);
    vll dist(n, LINF);
    priority_queue<pll, vector<pll>, greater<pll>> pq;
    
    dist[src] = 0;
    pq.push({0, src});
    
    while (!pq.empty()) {
        auto [d, u] = pq.top(); pq.pop();
        if (d > dist[u]) continue;
        
        for (auto [v, w] : adj[u]) {
            if (dist[u] + w < dist[v]) {
                dist[v] = dist[u] + w;
                pq.push({dist[v], v});
            }
        }
    }
    return dist;
}

// ==================== Topological Sort (Kahn's) ====================
vi toposort(const vvi& adj) {
    int n = sz(adj);
    vi indeg(n, 0), order;
    
    rep(u, 0, n) for (int v : adj[u]) indeg[v]++;
    
    queue<int> q;
    rep(i, 0, n) if (indeg[i] == 0) q.push(i);
    
    while (!q.empty()) {
        int u = q.front(); q.pop();
        order.pb(u);
        for (int v : adj[u]) {
            if (--indeg[v] == 0) q.push(v);
        }
    }
    return sz(order) == n ? order : vi(); // empty if cycle exists
}

// ==================== Cycle Detection (Directed) ====================
bool hasCycle(const vvi& adj) {
    int n = sz(adj);
    vi color(n, 0); // 0=white, 1=gray, 2=black
    
    function<bool(int)> dfs = [&](int u) -> bool {
        color[u] = 1;
        for (int v : adj[u]) {
            if (color[v] == 1) return true;
            if (color[v] == 0 && dfs(v)) return true;
        }
        color[u] = 2;
        return false;
    };
    
    rep(i, 0, n) if (color[i] == 0 && dfs(i)) return true;
    return false;
}

void solve() {
    int n, m;
    cin >> n >> m;
    
    vvi adj(n);
    // vector<vector<pll>> wadj(n); // for weighted graphs
    
    rep(i, 0, m) {
        int u, v;
        cin >> u >> v;
        u--; v--;  // 0-indexed
        adj[u].pb(v);
        adj[v].pb(u);  // remove for directed
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
