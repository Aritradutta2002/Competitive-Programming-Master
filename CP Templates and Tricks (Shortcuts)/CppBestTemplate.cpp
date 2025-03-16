/*
NAME : ARITRA DUTTA
DATE : 05.03.2025
*/

#include <bits/stdc++.h>
using namespace std;

#ifdef LOCAL
#define DEBUG(x) cerr << #x << " = " << x << '\n'
#else
#define DEBUG(x)
#endif

typedef long long ll;
typedef unsigned long long ull;
typedef long double ld;
typedef pair<int, int> pii;
typedef pair<ll, ll> pll;
typedef vector<int> vi;
typedef vector<ll> vll;
typedef vector<pii> vpii;
typedef vector<pll> vpll;

const int INF = 1e9 + 5;
const ll LINF = 1e18 + 5;
const double EPS = 1e-9;
const ll MOD = 1e9 + 7;

#define all(x) (x).begin(), (x).end()
#define rall(x) (x).rbegin(), (x).rend()
#define rep(i, a, b) for (int i = (a); i < (b); ++i)
#define per(i, a, b) for (int i = (a); i > (b); --i)
#define trav(a, x) for (auto& a : x)
#define pb push_back
#define mp make_pair
#define fi first
#define se second
#define sz(x) (int)((x).size())

inline ll gcd(ll a, ll b) { while (b) { a %= b; swap(a, b); } return a; }
inline ll lcm(ll a, ll b) { return a / gcd(a, b) * b; }
inline ll mod_exp(ll x, ll y, ll m) { ll res = 1; x %= m; while (y > 0) { if (y & 1) res = res * x % m; x = x * x % m; y >>= 1; } return res; }
inline ll mod_inv(ll x, ll m) { return mod_exp(x, m - 2, m); } // Only for prime m
inline ll mod_add(ll a, ll b, ll m = MOD) { return (a + b) % m; }
inline ll mod_sub(ll a, ll b, ll m = MOD) { return (a - b + m) % m; }
inline ll mod_mul(ll a, ll b, ll m = MOD) { return (a * b) % m; }

void fast_io() {
    ios_base::sync_with_stdio(false);
    cin.tie(nullptr);
    cout.tie(nullptr);
}

template<typename T>
int binary_search(const vector<T>& v, T key) {
    int lo = 0, hi = v.size() - 1;
    while (lo <= hi) {
        int mid = lo + (hi - lo) / 2;
        if (v[mid] == key) return mid;
        else if (v[mid] < key) lo = mid + 1;
        else hi = mid - 1;
    }
    return -1;
}

template<typename T>
int lower_bound(const vector<T>& v, T key) {
    int lo = 0, hi = v.size();
    while (lo < hi) {
        int mid = lo + (hi - lo) / 2;
        if (v[mid] < key) lo = mid + 1;
        else hi = mid;
    }
    return lo;
}

template<typename T>
int upper_bound(const vector<T>& v, T key) {
    int lo = 0, hi = v.size();
    while (lo < hi) {
        int mid = lo + (hi - lo) / 2;
        if (v[mid] <= key) lo = mid + 1;
        else hi = mid;
    }
    return lo;
}

template<typename T>
void print_vector(const vector<T>& v) {
    for (const T& i : v) cout << i << ' ';
    cout << '\n';
}

template<typename T>
void print_matrix(const vector<vector<T>>& mat) {
    for (const auto& row : mat) {
        for (const T& val : row) cout << val << ' ';
        cout << '\n';
    }
}

void bfs(int start, const vector<vi>& adj) {
    queue<int> q;
    vector<bool> visited(adj.size(), false);
    q.push(start);
    visited[start] = true;
    while (!q.empty()) {
        int u = q.front(); q.pop();
        // Process node u here
        for (int v : adj[u]) {
            if (!visited[v]) {
                visited[v] = true;
                q.push(v);
            }
        }
    }
}

void dfs(int u, const vector<vi>& adj, vector<bool>& visited) {
    visited[u] = true;
    // Process node u here
    for (int v : adj[u]) {
        if (!visited[v]) {
            dfs(v, adj, visited);
        }
    }
}

vector<int> sieve(int n) {
    vector<int> primes, is_prime(n + 1, 1);
    is_prime[0] = is_prime[1] = 0;

    for (int i = 2; i <= n; ++i) {
        if (is_prime[i]) {
            primes.pb(i);
            for (int j = 2 * i; j <= n; j += i) {
                is_prime[j] = 0;
            }
        }
    }
    return primes;
}

void solve() {
    // Your solution code here
}

int main() {
    fast_io();

    int t;
    cin >> t;

    while (t--) {
        solve();
    }

    return 0;
}
