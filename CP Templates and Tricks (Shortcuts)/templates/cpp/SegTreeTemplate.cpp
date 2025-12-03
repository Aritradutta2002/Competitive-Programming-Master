/*
 * Author  : Aritra Dutta
 * Target  : Codeforces Expert / CSES
 * 
 * SEGMENT TREE Template - Point Update, Range Query
 * Supports: Sum, Min, Max, GCD (change combine function)
 */
#include <bits/stdc++.h>
using namespace std;

using ll = long long;
using vi = vector<int>;
using vll = vector<ll>;

#define rep(i, a, b) for (int i = (a); i < (b); ++i)

// ==================== Segment Tree ====================
template<typename T>
struct SegTree {
    int n;
    vector<T> tree;
    T identity;
    function<T(T, T)> combine;
    
    SegTree(int n, T identity, function<T(T, T)> combine) 
        : n(n), tree(4 * n, identity), identity(identity), combine(combine) {}
    
    void build(const vector<T>& arr, int node, int start, int end) {
        if (start == end) {
            tree[node] = arr[start];
        } else {
            int mid = (start + end) / 2;
            build(arr, 2*node, start, mid);
            build(arr, 2*node+1, mid+1, end);
            tree[node] = combine(tree[2*node], tree[2*node+1]);
        }
    }
    
    void build(const vector<T>& arr) { build(arr, 1, 0, n-1); }
    
    void update(int node, int start, int end, int idx, T val) {
        if (start == end) {
            tree[node] = val;
        } else {
            int mid = (start + end) / 2;
            if (idx <= mid) update(2*node, start, mid, idx, val);
            else update(2*node+1, mid+1, end, idx, val);
            tree[node] = combine(tree[2*node], tree[2*node+1]);
        }
    }
    
    void update(int idx, T val) { update(1, 0, n-1, idx, val); }
    
    T query(int node, int start, int end, int l, int r) {
        if (r < start || end < l) return identity;
        if (l <= start && end <= r) return tree[node];
        int mid = (start + end) / 2;
        return combine(query(2*node, start, mid, l, r),
                      query(2*node+1, mid+1, end, l, r));
    }
    
    T query(int l, int r) { return query(1, 0, n-1, l, r); }
};

// ==================== Lazy Segment Tree (Range Update) ====================
template<typename T>
struct LazySegTree {
    int n;
    vector<T> tree, lazy;
    T identity;
    
    LazySegTree(int n, T identity = 0) : n(n), tree(4*n, identity), lazy(4*n, 0), identity(identity) {}
    
    void push(int node, int start, int end) {
        if (lazy[node] != 0) {
            tree[node] += lazy[node] * (end - start + 1);
            if (start != end) {
                lazy[2*node] += lazy[node];
                lazy[2*node+1] += lazy[node];
            }
            lazy[node] = 0;
        }
    }
    
    void updateRange(int node, int start, int end, int l, int r, T val) {
        push(node, start, end);
        if (r < start || end < l) return;
        if (l <= start && end <= r) {
            lazy[node] += val;
            push(node, start, end);
            return;
        }
        int mid = (start + end) / 2;
        updateRange(2*node, start, mid, l, r, val);
        updateRange(2*node+1, mid+1, end, l, r, val);
        tree[node] = tree[2*node] + tree[2*node+1];
    }
    
    void updateRange(int l, int r, T val) { updateRange(1, 0, n-1, l, r, val); }
    
    T query(int node, int start, int end, int l, int r) {
        push(node, start, end);
        if (r < start || end < l) return identity;
        if (l <= start && end <= r) return tree[node];
        int mid = (start + end) / 2;
        return query(2*node, start, mid, l, r) + query(2*node+1, mid+1, end, l, r);
    }
    
    T query(int l, int r) { return query(1, 0, n-1, l, r); }
};

void solve() {
    int n, q;
    cin >> n >> q;
    
    vll arr(n);
    rep(i, 0, n) cin >> arr[i];
    
    // Sum Segment Tree
    SegTree<ll> st(n, 0LL, [](ll a, ll b) { return a + b; });
    st.build(arr);
    
    // Min Segment Tree: SegTree<ll> st(n, LLONG_MAX, [](ll a, ll b) { return min(a, b); });
    // Max Segment Tree: SegTree<ll> st(n, LLONG_MIN, [](ll a, ll b) { return max(a, b); });
    
    while (q--) {
        int type;
        cin >> type;
        if (type == 1) {
            int idx; ll val;
            cin >> idx >> val;
            idx--;
            st.update(idx, val);
        } else {
            int l, r;
            cin >> l >> r;
            l--; r--;
            cout << st.query(l, r) << '\n';
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
