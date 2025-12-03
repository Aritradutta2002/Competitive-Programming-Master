# Competitive Programming Cheatsheet
## Codeforces Expert / CSES Quick Reference

---

## Time Complexity Guide

| n | Max Complexity |
|---|----------------|
| ≤ 10 | O(n!) |
| ≤ 20 | O(2^n) |
| ≤ 500 | O(n³) |
| ≤ 5000 | O(n²) |
| ≤ 10^6 | O(n log n) |
| ≤ 10^8 | O(n) |
| > 10^8 | O(log n) or O(1) |

---

## Common Patterns

### Binary Search Template
```cpp
int lo = 0, hi = n - 1, ans = -1;
while (lo <= hi) {
    int mid = lo + (hi - lo) / 2;
    if (check(mid)) {
        ans = mid;
        lo = mid + 1;  // or hi = mid - 1
    } else {
        hi = mid - 1;  // or lo = mid + 1
    }
}
```

### Two Pointers
```cpp
int l = 0, r = n - 1;
while (l < r) {
    if (arr[l] + arr[r] == target) { /* found */ }
    else if (arr[l] + arr[r] < target) l++;
    else r--;
}
```

### Sliding Window (Fixed Size)
```cpp
int sum = 0;
for (int i = 0; i < n; i++) {
    sum += arr[i];
    if (i >= k) sum -= arr[i - k];
    if (i >= k - 1) ans = max(ans, sum);
}
```

### Sliding Window (Variable Size)
```cpp
int l = 0, sum = 0;
for (int r = 0; r < n; r++) {
    sum += arr[r];
    while (sum > target) sum -= arr[l++];
    ans = max(ans, r - l + 1);
}
```

---

## Graph Algorithms

### BFS (Shortest Path Unweighted)
```cpp
queue<int> q;
vector<int> dist(n, -1);
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
```

### DFS
```cpp
void dfs(int u, int p) {
    for (int v : adj[u]) {
        if (v != p) dfs(v, u);
    }
}
```

### Dijkstra
```cpp
priority_queue<pll, vector<pll>, greater<pll>> pq;
vector<ll> dist(n, LINF);
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
```

### DSU (Union-Find)
```cpp
int find(int x) { return p[x] == x ? x : p[x] = find(p[x]); }
bool unite(int x, int y) {
    x = find(x); y = find(y);
    if (x == y) return false;
    if (rank[x] < rank[y]) swap(x, y);
    p[y] = x;
    if (rank[x] == rank[y]) rank[x]++;
    return true;
}
```

---

## Number Theory

### GCD / LCM
```cpp
ll gcd(ll a, ll b) { return b ? gcd(b, a % b) : a; }
ll lcm(ll a, ll b) { return a / gcd(a, b) * b; }
```

### Modular Exponentiation
```cpp
ll mod_pow(ll x, ll y, ll m) {
    ll res = 1; x %= m;
    while (y > 0) {
        if (y & 1) res = res * x % m;
        x = x * x % m; y >>= 1;
    }
    return res;
}
```

### Modular Inverse (for prime mod)
```cpp
ll mod_inv(ll x, ll m) { return mod_pow(x, m - 2, m); }
```

### Sieve of Eratosthenes
```cpp
vector<bool> is_prime(n + 1, true);
is_prime[0] = is_prime[1] = false;
for (int i = 2; i * i <= n; i++) {
    if (is_prime[i]) {
        for (int j = i * i; j <= n; j += i) is_prime[j] = false;
    }
}
```

### nCr with Precomputation
```cpp
ll fact[MAXN], inv_fact[MAXN];
void precompute(int n) {
    fact[0] = 1;
    for (int i = 1; i <= n; i++) fact[i] = fact[i-1] * i % MOD;
    inv_fact[n] = mod_inv(fact[n], MOD);
    for (int i = n - 1; i >= 0; i--) inv_fact[i] = inv_fact[i+1] * (i+1) % MOD;
}
ll nCr(int n, int r) {
    if (r < 0 || r > n) return 0;
    return fact[n] * inv_fact[r] % MOD * inv_fact[n-r] % MOD;
}
```

---

## Data Structures

### Segment Tree (Point Update, Range Sum)
```cpp
void update(int node, int start, int end, int idx, ll val) {
    if (start == end) tree[node] = val;
    else {
        int mid = (start + end) / 2;
        if (idx <= mid) update(2*node, start, mid, idx, val);
        else update(2*node+1, mid+1, end, idx, val);
        tree[node] = tree[2*node] + tree[2*node+1];
    }
}
ll query(int node, int start, int end, int l, int r) {
    if (r < start || end < l) return 0;
    if (l <= start && end <= r) return tree[node];
    int mid = (start + end) / 2;
    return query(2*node, start, mid, l, r) + query(2*node+1, mid+1, end, l, r);
}
```

### BIT (Fenwick Tree)
```cpp
void update(int i, ll val) {
    for (; i <= n; i += i & (-i)) tree[i] += val;
}
ll query(int i) {
    ll sum = 0;
    for (; i > 0; i -= i & (-i)) sum += tree[i];
    return sum;
}
```

---

## String Algorithms

### Z-Function
```cpp
vector<int> z(n);
int l = 0, r = 0;
for (int i = 1; i < n; i++) {
    if (i < r) z[i] = min(r - i, z[i - l]);
    while (i + z[i] < n && s[z[i]] == s[i + z[i]]) z[i]++;
    if (i + z[i] > r) { l = i; r = i + z[i]; }
}
```

### KMP (Prefix Function)
```cpp
vector<int> pi(n);
for (int i = 1; i < n; i++) {
    int j = pi[i - 1];
    while (j > 0 && s[i] != s[j]) j = pi[j - 1];
    if (s[i] == s[j]) j++;
    pi[i] = j;
}
```

---

## DP Patterns

### Coin Change (Min Coins)
```cpp
dp[0] = 0;
for (int i = 1; i <= target; i++) {
    for (int c : coins) {
        if (c <= i) dp[i] = min(dp[i], dp[i - c] + 1);
    }
}
```

### 0/1 Knapsack
```cpp
for (int i = 0; i < n; i++) {
    for (int w = capacity; w >= weights[i]; w--) {
        dp[w] = max(dp[w], dp[w - weights[i]] + values[i]);
    }
}
```

### LIS (O(n log n))
```cpp
vector<int> dp;
for (int x : arr) {
    auto it = lower_bound(dp.begin(), dp.end(), x);
    if (it == dp.end()) dp.push_back(x);
    else *it = x;
}
// LIS length = dp.size()
```

---

## Useful Tricks

### Coordinate Compression
```cpp
vector<int> vals = arr;
sort(vals.begin(), vals.end());
vals.erase(unique(vals.begin(), vals.end()), vals.end());
for (int& x : arr) x = lower_bound(vals.begin(), vals.end(), x) - vals.begin();
```

### Prefix Sum
```cpp
vector<ll> prefix(n + 1);
for (int i = 0; i < n; i++) prefix[i + 1] = prefix[i] + arr[i];
// Sum of [l, r] = prefix[r + 1] - prefix[l]
```

### Difference Array (Range Update)
```cpp
diff[l] += val;
diff[r + 1] -= val;
// After all updates, compute prefix sum to get final array
```

### Bit Manipulation
```cpp
x & (x - 1)  // Remove lowest set bit
x & (-x)     // Get lowest set bit
__builtin_popcount(x)  // Count set bits
__builtin_clz(x)       // Count leading zeros
__lg(x)                // Floor of log2(x)
```

---

## Edge Cases to Remember

1. **n = 0 or n = 1**
2. **All elements same**
3. **Already sorted / reverse sorted**
4. **Negative numbers**
5. **Integer overflow** → use `long long`
6. **Empty input**
7. **Maximum constraints**

---

Good luck! 🚀
