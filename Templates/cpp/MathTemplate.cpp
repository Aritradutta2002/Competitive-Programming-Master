/*
 * Author  : Aritra Dutta
 * Target  : Codeforces Expert / CSES
 * 
 * MATH Template - Number Theory, Combinatorics, Primes
 */
#include <bits/stdc++.h>
using namespace std;

using ll = long long;
using vll = vector<ll>;
using vi = vector<int>;

constexpr ll MOD = 1e9 + 7;
constexpr ll MOD2 = 998244353;
constexpr int MAXN = 2e6 + 5;

#define rep(i, a, b) for (int i = (a); i < (b); ++i)

// ==================== Modular Arithmetic ====================
ll mod_add(ll a, ll b, ll m = MOD) { return ((a % m) + (b % m)) % m; }
ll mod_sub(ll a, ll b, ll m = MOD) { return ((a % m) - (b % m) + m) % m; }
ll mod_mul(ll a, ll b, ll m = MOD) { return ((a % m) * (b % m)) % m; }

ll mod_pow(ll x, ll y, ll m = MOD) {
    ll res = 1; x %= m;
    while (y > 0) {
        if (y & 1) res = res * x % m;
        x = x * x % m; y >>= 1;
    }
    return res;
}

ll mod_inv(ll x, ll m = MOD) { return mod_pow(x, m - 2, m); }

// ==================== Factorials & nCr ====================
ll fact[MAXN], inv_fact[MAXN];

void precompute_factorials(int n = MAXN - 1) {
    fact[0] = 1;
    rep(i, 1, n + 1) fact[i] = mod_mul(fact[i-1], i);
    inv_fact[n] = mod_inv(fact[n]);
    for (int i = n - 1; i >= 0; i--) inv_fact[i] = mod_mul(inv_fact[i+1], i+1);
}

ll nCr(int n, int r) {
    if (r < 0 || r > n) return 0;
    return mod_mul(fact[n], mod_mul(inv_fact[r], inv_fact[n-r]));
}

ll nPr(int n, int r) {
    if (r < 0 || r > n) return 0;
    return mod_mul(fact[n], inv_fact[n-r]);
}

// ==================== GCD, LCM, Extended GCD ====================
ll gcd(ll a, ll b) { return b ? gcd(b, a % b) : a; }
ll lcm(ll a, ll b) { return a / gcd(a, b) * b; }

// Returns {gcd, x, y} such that ax + by = gcd(a, b)
tuple<ll, ll, ll> extgcd(ll a, ll b) {
    if (b == 0) return {a, 1, 0};
    auto [g, x, y] = extgcd(b, a % b);
    return {g, y, x - (a / b) * y};
}

// ==================== Sieve of Eratosthenes ====================
vi sieve(int n) {
    vi is_prime(n + 1, 1), primes;
    is_prime[0] = is_prime[1] = 0;
    for (int i = 2; i <= n; i++) {
        if (is_prime[i]) {
            primes.push_back(i);
            for (ll j = (ll)i * i; j <= n; j += i) is_prime[j] = 0;
        }
    }
    return primes;
}

// ==================== Smallest Prime Factor (SPF) ====================
vi spf;
void compute_spf(int n) {
    spf.assign(n + 1, 0);
    iota(spf.begin(), spf.end(), 0);
    for (int i = 2; i * i <= n; i++) {
        if (spf[i] == i) {
            for (int j = i * i; j <= n; j += i) {
                if (spf[j] == j) spf[j] = i;
            }
        }
    }
}

// Prime factorization using SPF - O(log n)
vector<pair<int, int>> factorize(int n) {
    vector<pair<int, int>> factors;
    while (n > 1) {
        int p = spf[n], cnt = 0;
        while (n % p == 0) { n /= p; cnt++; }
        factors.push_back({p, cnt});
    }
    return factors;
}

// ==================== Euler's Totient ====================
ll phi(ll n) {
    ll result = n;
    for (ll i = 2; i * i <= n; i++) {
        if (n % i == 0) {
            while (n % i == 0) n /= i;
            result -= result / i;
        }
    }
    if (n > 1) result -= result / n;
    return result;
}

// ==================== Matrix Exponentiation ====================
using Matrix = vector<vll>;

Matrix multiply(const Matrix& A, const Matrix& B, ll m = MOD) {
    int n = A.size();
    Matrix C(n, vll(n, 0));
    rep(i, 0, n) rep(k, 0, n) rep(j, 0, n) {
        C[i][j] = mod_add(C[i][j], mod_mul(A[i][k], B[k][j], m), m);
    }
    return C;
}

Matrix matrix_pow(Matrix A, ll p, ll m = MOD) {
    int n = A.size();
    Matrix result(n, vll(n, 0));
    rep(i, 0, n) result[i][i] = 1;  // Identity
    while (p > 0) {
        if (p & 1) result = multiply(result, A, m);
        A = multiply(A, A, m);
        p >>= 1;
    }
    return result;
}

// Fibonacci using matrix exponentiation - O(log n)
ll fib(ll n, ll m = MOD) {
    if (n <= 1) return n;
    Matrix base = {{1, 1}, {1, 0}};
    Matrix result = matrix_pow(base, n - 1, m);
    return result[0][0];
}

void solve() {
    precompute_factorials();
    
    int n, r;
    cin >> n >> r;
    cout << nCr(n, r) << '\n';
}

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int t = 1;
    // cin >> t;
    while (t--) solve();
    
    return 0;
}
