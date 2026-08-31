import java.util.*;

/**
 * LeetCode Java Template (Refined)
 * Features: Debug utils, Common Helpers, Local Testing setup.
 */
public class LeetCodeTemplate {
    public static void main(String[] args) {
        Solution sol = new Solution();
        // Test here: sol.solve(...);
    }
}

class Solution {
    static final int MOD = 1_000_000_007;

    public void example(int[] nums) {
        // debug(nums);
    }

    // ==================== UTILS ====================
    void debug(int[] a) { System.out.println(Arrays.toString(a)); }
    void debug(long[] a) { System.out.println(Arrays.toString(a)); }
    void debug(Object... os) { System.out.println(Arrays.deepToString(os)); }

    long gcd(long a, long b) { return b == 0 ? a : gcd(b, a % b); }
}

// ==================== LOCAL HELPERS ====================
class ListNode {
    int val; ListNode next;
    ListNode(int x) { val = x; }
    static ListNode fromArray(int[] a) {
        if(a.length==0) return null;
        ListNode head = new ListNode(a[0]), curr = head;
        for(int i=1; i<a.length; i++) { curr.next = new ListNode(a[i]); curr = curr.next; }
        return head;
    }
}

class TreeNode {
    int val; TreeNode left, right;
    TreeNode(int x) { val = x; }
}
