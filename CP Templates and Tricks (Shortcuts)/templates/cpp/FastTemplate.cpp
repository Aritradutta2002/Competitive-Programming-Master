/*
 * Author  : Aritra Dutta
 * Target  : Codeforces Expert / CSES
 * 
 * FAST I/O Template - Use for most problems
 * Compile: g++ -std=c++17 -O2 -o sol FastTemplate.cpp
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
