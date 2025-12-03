/*
 * Author  : Aritra Dutta
 * Target  : Codeforces Expert / CSES
 * 
 * STRING Template - KMP, Z-function, Hashing, Trie
 */
#include <bits/stdc++.h>
using namespace std;

using ll = long long;
using vi = vector<int>;
using vll = vector<ll>;

constexpr ll MOD = 1e9 + 7;
constexpr ll BASE = 31;

#define rep(i, a, b) for (int i = (a); i < (b); ++i)
#define sz(x) (int)(x).size()

// ==================== Z-Function ====================
vi zFunction(const string& s) {
    int n = sz(s);
    vi z(n, 0);
    int l = 0, r = 0;
    rep(i, 1, n) {
        if (i < r) z[i] = min(r - i, z[i - l]);
        while (i + z[i] < n && s[z[i]] == s[i + z[i]]) z[i]++;
        if (i + z[i] > r) { l = i; r = i + z[i]; }
    }
    return z;
}

// Pattern matching using Z-function
vi findPattern(const string& text, const string& pattern) {
    string combined = pattern + "$" + text;
    vi z = zFunction(combined);
    vi matches;
    int plen = sz(pattern);
    rep(i, plen + 1, sz(combined)) {
        if (z[i] == plen) matches.push_back(i - plen - 1);
    }
    return matches;
}

// ==================== KMP (Prefix Function) ====================
vi prefixFunction(const string& s) {
    int n = sz(s);
    vi pi(n, 0);
    rep(i, 1, n) {
        int j = pi[i - 1];
        while (j > 0 && s[i] != s[j]) j = pi[j - 1];
        if (s[i] == s[j]) j++;
        pi[i] = j;
    }
    return pi;
}

// KMP pattern matching
vi kmpSearch(const string& text, const string& pattern) {
    string combined = pattern + "#" + text;
    vi pi = prefixFunction(combined);
    vi matches;
    int plen = sz(pattern);
    rep(i, plen + 1, sz(combined)) {
        if (pi[i] == plen) matches.push_back(i - 2 * plen);
    }
    return matches;
}

// ==================== String Hashing ====================
struct StringHash {
    vll hash, pw;
    ll base, mod;
    
    StringHash(const string& s, ll base = BASE, ll mod = MOD) : base(base), mod(mod) {
        int n = sz(s);
        hash.resize(n + 1);
        pw.resize(n + 1);
        pw[0] = 1;
        hash[0] = 0;
        rep(i, 0, n) {
            hash[i + 1] = (hash[i] * base + s[i] - 'a' + 1) % mod;
            pw[i + 1] = pw[i] * base % mod;
        }
    }
    
    // Get hash of substring [l, r] (0-indexed, inclusive)
    ll getHash(int l, int r) {
        return (hash[r + 1] - hash[l] * pw[r - l + 1] % mod + mod) % mod;
    }
};

// ==================== Trie ====================
struct Trie {
    struct Node {
        int children[26] = {};
        int cnt = 0;  // words ending here
        int prefix_cnt = 0;  // words with this prefix
    };
    
    vector<Node> nodes;
    
    Trie() { nodes.emplace_back(); }
    
    void insert(const string& s) {
        int cur = 0;
        for (char c : s) {
            int idx = c - 'a';
            if (!nodes[cur].children[idx]) {
                nodes[cur].children[idx] = sz(nodes);
                nodes.emplace_back();
            }
            cur = nodes[cur].children[idx];
            nodes[cur].prefix_cnt++;
        }
        nodes[cur].cnt++;
    }
    
    int countWord(const string& s) {
        int cur = 0;
        for (char c : s) {
            int idx = c - 'a';
            if (!nodes[cur].children[idx]) return 0;
            cur = nodes[cur].children[idx];
        }
        return nodes[cur].cnt;
    }
    
    int countPrefix(const string& s) {
        int cur = 0;
        for (char c : s) {
            int idx = c - 'a';
            if (!nodes[cur].children[idx]) return 0;
            cur = nodes[cur].children[idx];
        }
        return nodes[cur].prefix_cnt;
    }
};

// ==================== Manacher's Algorithm (Palindromes) ====================
vi manacher(const string& s) {
    string t = "#";
    for (char c : s) { t += c; t += '#'; }
    int n = sz(t);
    vi p(n, 0);
    int c = 0, r = 0;
    rep(i, 0, n) {
        if (i < r) p[i] = min(r - i, p[2 * c - i]);
        while (i - p[i] - 1 >= 0 && i + p[i] + 1 < n && t[i - p[i] - 1] == t[i + p[i] + 1]) p[i]++;
        if (i + p[i] > r) { c = i; r = i + p[i]; }
    }
    return p;
}

// Longest palindromic substring length
int longestPalindrome(const string& s) {
    vi p = manacher(s);
    return *max_element(p.begin(), p.end());
}

void solve() {
    string text, pattern;
    cin >> text >> pattern;
    
    vi matches = findPattern(text, pattern);
    cout << sz(matches) << '\n';
    for (int pos : matches) cout << pos << ' ';
    cout << '\n';
}

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int t = 1;
    // cin >> t;
    while (t--) solve();
    
    return 0;
}
