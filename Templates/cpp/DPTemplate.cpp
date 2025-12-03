/*
 * Author  : Aritra Dutta
 * Target  : Codeforces Expert / CSES
 * 
 * DP Template - Common DP patterns for CSES/CF
 */
#include <bits/stdc++.h>
using namespace std;

using ll = long long;
using vi = vector<int>;
using vll = vector<ll>;
using vvi = vector<vi>;
using vvll = vector<vll>;

constexpr ll MOD = 1e9 + 7;
constexpr ll INF = 1e18;

#define rep(i, a, b) for (int i = (a); i < (b); ++i)
#define per(i, a, b) for (int i = (a); i >= (b); --i)
#define sz(x) (int)(x).size()

// ==================== Coin Change (Min Coins) ====================
int minCoins(const vi& coins, int target) {
    vi dp(target + 1, 1e9);
    dp[0] = 0;
    rep(i, 1, target + 1) {
        for (int c : coins) {
            if (c <= i && dp[i - c] + 1 < dp[i]) {
                dp[i] = dp[i - c] + 1;
            }
        }
    }
    return dp[target] >= 1e9 ? -1 : dp[target];
}

// ==================== Coin Change (Count Ways) ====================
ll countWays(const vi& coins, int target) {
    vll dp(target + 1, 0);
    dp[0] = 1;
    for (int c : coins) {
        rep(i, c, target + 1) {
            dp[i] = (dp[i] + dp[i - c]) % MOD;
        }
    }
    return dp[target];
}

// ==================== 0/1 Knapsack ====================
ll knapsack01(const vi& weights, const vll& values, int capacity) {
    int n = sz(weights);
    vll dp(capacity + 1, 0);
    rep(i, 0, n) {
        per(w, capacity, weights[i]) {
            dp[w] = max(dp[w], dp[w - weights[i]] + values[i]);
        }
    }
    return dp[capacity];
}

// ==================== LIS (Longest Increasing Subsequence) ====================
int lis(const vi& arr) {
    vi dp;
    for (int x : arr) {
        auto it = lower_bound(dp.begin(), dp.end(), x);
        if (it == dp.end()) dp.push_back(x);
        else *it = x;
    }
    return sz(dp);
}

// ==================== LCS (Longest Common Subsequence) ====================
int lcs(const string& a, const string& b) {
    int n = sz(a), m = sz(b);
    vvi dp(n + 1, vi(m + 1, 0));
    rep(i, 1, n + 1) rep(j, 1, m + 1) {
        if (a[i-1] == b[j-1]) dp[i][j] = dp[i-1][j-1] + 1;
        else dp[i][j] = max(dp[i-1][j], dp[i][j-1]);
    }
    return dp[n][m];
}

// ==================== Edit Distance ====================
int editDistance(const string& a, const string& b) {
    int n = sz(a), m = sz(b);
    vvi dp(n + 1, vi(m + 1, 0));
    rep(i, 0, n + 1) dp[i][0] = i;
    rep(j, 0, m + 1) dp[0][j] = j;
    rep(i, 1, n + 1) rep(j, 1, m + 1) {
        if (a[i-1] == b[j-1]) dp[i][j] = dp[i-1][j-1];
        else dp[i][j] = 1 + min({dp[i-1][j], dp[i][j-1], dp[i-1][j-1]});
    }
    return dp[n][m];
}

// ==================== Subset Sum ====================
bool subsetSum(const vi& arr, int target) {
    vector<bool> dp(target + 1, false);
    dp[0] = true;
    for (int x : arr) {
        per(i, target, x) {
            dp[i] = dp[i] || dp[i - x];
        }
    }
    return dp[target];
}

// ==================== Grid Paths (Count ways) ====================
ll gridPaths(int n, int m, const vector<string>& grid) {
    vvll dp(n, vll(m, 0));
    dp[0][0] = (grid[0][0] == '.') ? 1 : 0;
    
    rep(i, 0, n) rep(j, 0, m) {
        if (grid[i][j] == '*') { dp[i][j] = 0; continue; }
        if (i > 0) dp[i][j] = (dp[i][j] + dp[i-1][j]) % MOD;
        if (j > 0) dp[i][j] = (dp[i][j] + dp[i][j-1]) % MOD;
    }
    return dp[n-1][m-1];
}

void solve() {
    // Example: Coin Change
    int n, target;
    cin >> n >> target;
    vi coins(n);
    rep(i, 0, n) cin >> coins[i];
    
    cout << countWays(coins, target) << '\n';
}

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int t = 1;
    // cin >> t;
    while (t--) solve();
    
    return 0;
}
