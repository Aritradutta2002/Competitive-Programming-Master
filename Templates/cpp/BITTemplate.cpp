/*
 * Author  : Aritra Dutta
 * Target  : Codeforces Expert / CSES
 * 
 * BIT (Fenwick Tree) Template - Point Update, Prefix Query
 * Faster and simpler than Segment Tree for many problems
 */
#include <bits/stdc++.h>
using namespace std;

using ll = long long;
using vi = vector<int>;
using vll = vector<ll>;

#define rep(i, a, b) for (int i = (a); i < (b); ++i)

// ==================== BIT (Fenwick Tree) ====================
template<typename T>
struct BIT {
    int n;
    vector<T> tree;
    
    BIT(int n) : n(n), tree(n + 1, 0) {}
    
    // Add val to index i (1-indexed)
    void update(int i, T val) {
        for (; i <= n; i += i & (-i)) tree[i] += val;
    }
    
    // Sum of [1, i] (1-indexed)
    T query(int i) {
        T sum = 0;
        for (; i > 0; i -= i & (-i)) sum += tree[i];
        return sum;
    }
    
    // Sum of [l, r] (1-indexed)
    T query(int l, int r) {
        return query(r) - query(l - 1);
    }
    
    // Find smallest index with prefix sum >= val (for counting inversions)
    int lower_bound(T val) {
        int pos = 0;
        T sum = 0;
        for (int pw = 1 << __lg(n); pw > 0; pw >>= 1) {
            if (pos + pw <= n && sum + tree[pos + pw] < val) {
                pos += pw;
                sum += tree[pos];
            }
        }
        return pos + 1;
    }
};

// ==================== 2D BIT ====================
template<typename T>
struct BIT2D {
    int n, m;
    vector<vector<T>> tree;
    
    BIT2D(int n, int m) : n(n), m(m), tree(n + 1, vector<T>(m + 1, 0)) {}
    
    void update(int x, int y, T val) {
        for (int i = x; i <= n; i += i & (-i))
            for (int j = y; j <= m; j += j & (-j))
                tree[i][j] += val;
    }
    
    T query(int x, int y) {
        T sum = 0;
        for (int i = x; i > 0; i -= i & (-i))
            for (int j = y; j > 0; j -= j & (-j))
                sum += tree[i][j];
        return sum;
    }
    
    // Sum of rectangle [(x1,y1), (x2,y2)]
    T query(int x1, int y1, int x2, int y2) {
        return query(x2, y2) - query(x1 - 1, y2) - query(x2, y1 - 1) + query(x1 - 1, y1 - 1);
    }
};

// ==================== Count Inversions ====================
ll countInversions(vi& arr) {
    int n = arr.size();
    
    // Coordinate compression
    vi sorted_arr = arr;
    sort(sorted_arr.begin(), sorted_arr.end());
    sorted_arr.erase(unique(sorted_arr.begin(), sorted_arr.end()), sorted_arr.end());
    
    map<int, int> compress;
    rep(i, 0, (int)sorted_arr.size()) compress[sorted_arr[i]] = i + 1;
    
    BIT<ll> bit(sorted_arr.size());
    ll inversions = 0;
    
    for (int i = n - 1; i >= 0; i--) {
        int pos = compress[arr[i]];
        inversions += bit.query(pos - 1);
        bit.update(pos, 1);
    }
    return inversions;
}

void solve() {
    int n, q;
    cin >> n >> q;
    
    BIT<ll> bit(n);
    
    rep(i, 0, n) {
        ll x; cin >> x;
        bit.update(i + 1, x);
    }
    
    while (q--) {
        int type;
        cin >> type;
        if (type == 1) {
            int idx; ll val;
            cin >> idx >> val;
            // For point update (set value), need to track original values
            bit.update(idx, val);
        } else {
            int l, r;
            cin >> l >> r;
            cout << bit.query(l, r) << '\n';
        }
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
