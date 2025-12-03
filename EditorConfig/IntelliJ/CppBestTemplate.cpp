/*
 * Author  : Aritra Dutta
 * Target  : Codeforces Expert / CSES
 * 
 * ULTIMATE C++ Template - All-in-one for competitive programming
 * Compile: g++ -std=c++17 -O2 -o sol CppBestTemplate.cpp
 */
#include <bits/stdc++.h>
using namespace std;

// ==================== TYPE ALIASES ====================
using ll = long long;
using ull = unsigned long long;
using ld = long double;
using pii = pair<int, int>;
using pll = pair<ll, ll>;
using vi = vector<int>;
using vll = vector<ll>;
using vvi = vector<vi>;
using vvll = vector<vll>;
using vpii = vector<pii>;
using vpll = vector<pll>;

// ==================== CONSTANTS ====================
constexpr int INF = 1e9 + 5;
constexpr ll LINF = 1e18 + 5;
constexpr ll MOD = 1e9 + 7;
constexpr ll MOD2 = 998244353;
constexpr double EPS = 1e-9;
constexpr double PI = acos(-1.0);

// ==================== MACROS ====================
#define all(x) (x).begin(), (x).end()
#define rall(x) (x).rbegin(), (x).rend()
#define sz(x) (int)(x).size()
#define pb push_back
#define eb emplace_back
#define mp make_pair
#define fi first
#define se second
#define rep(i, a, b) for (int i = (a); i < (b); ++i)
#define per(i, a, b) for (int i = (a); i >= (b); --i)
#define trav(a, x) for (auto& a : x)

// ==================== DEBUG (Local Only) ====================
#ifdef LOCAL
#define dbg(x) cerr << #x << " = " << (x) << '\n'
#define dbgv(v) { cerr << #v << " = ["; for(auto& x : v) cerr << x << " "; cerr << "]\n"; }
#else
#define dbg(x)
#define dbgv(v)
#endif

// ==================== UTILITY FUNCTIONS ====================
template<typename T> T gcd(T a, T b) { return b ? gcd(b, a % b) : a; }
template<typename T> T lcm(T a, T b) { return a / gcd(a, b) * b; }

ll mod_pow(ll x, ll y, ll m = MOD) {
    ll res = 1; x %= m;
    while (y > 0) {
        if (y & 1) res = res * x % m;
        x = x * x % m; y >>= 1;
    }
    return res;
}
ll mod_inv(ll x, ll m = MOD) { return mod_pow(x, m - 2, m); }
ll mod_add(ll a, ll b, ll m = MOD) { return ((a % m) + (b % m)) % m; }
ll mod_sub(ll a, ll b, ll m = MOD) { return ((a % m) - (b % m) + m) % m; }
ll mod_mul(ll a, ll b, ll m = MOD) { return ((a % m) * (b % m)) % m; }

void yes() { cout << "YES\n"; }
void no() { cout << "NO\n"; }

template<typename T> void print_vec(const vector<T>& v) {
    for (int i = 0; i < sz(v); i++) cout << v[i] << " \n"[i == sz(v) - 1];
}

// ==================== DSU (Union-Find) ====================
struct DSU {
    vi p, rank_;
    int components;
    
    DSU(int n) : p(n), rank_(n, 0), components(n) { iota(all(p), 0); }
    
    int find(int x) { return p[x] == x ? x : p[x] = find(p[x]); }
    
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

// ==================== Dijkstra ====================
vll dijkstra(int src, const vector<vpll>& adj) {
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

// ==================== Sieve ====================
vi sieve(int n) {
    vi is_prime(n + 1, 1), primes;
    is_prime[0] = is_prime[1] = 0;
    for (int i = 2; i <= n; i++) {
        if (is_prime[i]) {
            primes.pb(i);
            for (ll j = (ll)i * i; j <= n; j += i) is_prime[j] = 0;
        }
    }
    return primes;
}

// ==================== Factorials & nCr ====================
constexpr int MAXN = 2e6 + 5;
ll fact[MAXN], inv_fact[MAXN];

void precompute_factorials(int n = MAXN - 1) {
    fact[0] = 1;
    rep(i, 1, n + 1) fact[i] = mod_mul(fact[i-1], i);
    inv_fact[n] = mod_inv(fact[n]);
    per(i, n - 1, 0) inv_fact[i] = mod_mul(inv_fact[i+1], i+1);
}

ll nCr(int n, int r) {
    if (r < 0 || r > n) return 0;
    return mod_mul(fact[n], mod_mul(inv_fact[r], inv_fact[n-r]));
}

// ==================== SOLVE ====================
void solve() {
    
}

// ==================== MAIN ====================
int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int t = 1;
    cin >> t;  // Comment out for single test case
    while (t--) solve();
    
    return 0;
}
