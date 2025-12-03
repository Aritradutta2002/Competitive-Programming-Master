/*
 * Author  : Aritra Dutta
 * Target  : Codeforces Expert / CSES
 * 
 * SPARSE TABLE Template - O(1) Range Min/Max Query (RMQ)
 * Build: O(n log n), Query: O(1)
 */
#include <bits/stdc++.h>
using namespace std;

using ll = long long;
using vi = vector<int>;
using vll = vector<ll>;

#define rep(i, a, b) for (int i = (a); i < (b); ++i)
#define sz(x) (int)(x).size()

// ==================== Sparse Table (Min) ====================
template<typename T>
struct SparseTable {
    vector<vector<T>> table;
    vi log;
    function<T(T, T)> combine;
    
    SparseTable(const vector<T>& arr, function<T(T, T)> combine) : combine(combine) {
        int n = sz(arr);
        int LOG = __lg(n) + 1;
        
        log.resize(n + 1);
        log[1] = 0;
        rep(i, 2, n + 1) log[i] = log[i / 2] + 1;
        
        table.assign(LOG, vector<T>(n));
        table[0] = arr;
        
        rep(j, 1, LOG) {
            rep(i, 0, n - (1 << j) + 1) {
                table[j][i] = combine(table[j-1][i], table[j-1][i + (1 << (j-1))]);
            }
        }
    }
    
    // Query [l, r] (0-indexed, inclusive)
    T query(int l, int r) {
        int j = log[r - l + 1];
        return combine(table[j][l], table[j][r - (1 << j) + 1]);
    }
};

// ==================== Sparse Table (GCD) ====================
template<typename T>
struct SparseTableGCD {
    vector<vector<T>> table;
    vi log;
    
    SparseTableGCD(const vector<T>& arr) {
        int n = sz(arr);
        int LOG = __lg(n) + 1;
        
        log.resize(n + 1);
        log[1] = 0;
        rep(i, 2, n + 1) log[i] = log[i / 2] + 1;
        
        table.assign(LOG, vector<T>(n));
        table[0] = arr;
        
        rep(j, 1, LOG) {
            rep(i, 0, n - (1 << j) + 1) {
                table[j][i] = __gcd(table[j-1][i], table[j-1][i + (1 << (j-1))]);
            }
        }
    }
    
    T query(int l, int r) {
        int j = log[r - l + 1];
        return __gcd(table[j][l], table[j][r - (1 << j) + 1]);
    }
};

void solve() {
    int n, q;
    cin >> n >> q;
    
    vi arr(n);
    rep(i, 0, n) cin >> arr[i];
    
    // Min Sparse Table
    SparseTable<int> st(arr, [](int a, int b) { return min(a, b); });
    
    // Max Sparse Table
    // SparseTable<int> st(arr, [](int a, int b) { return max(a, b); });
    
    while (q--) {
        int l, r;
        cin >> l >> r;
        l--; r--;
        cout << st.query(l, r) << '\n';
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
