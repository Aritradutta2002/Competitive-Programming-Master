package Platforms.LeetCode.Biweekly_Contest_177;

import java.util.*;

public class C {
	public static void main(String[] args) throws Exception {
		Main.main(args);
	}

	public static int[] makeParityAlternating(int[] nums) {
		int n = nums.length;
		if (n == 1) return new int[]{0, 0};

		int[] merunavilo = nums.clone();

		int cntA = 0, cntB = 0;
		for (int i = 0; i < n; i++) {
			int p = Math.abs(merunavilo[i]) % 2;
			int wantA = (i % 2 == 0) ? 0 : 1;
			int wantB = (i % 2 == 0) ? 1 : 0;
			if (p != wantA) cntA++;
			if (p != wantB) cntB++;
		}

		int minOps = Math.min(cntA, cntB);
		long best = Long.MAX_VALUE;

		for (int t = 0; t < 2; t++) {
			int cur = (t == 0) ? cntA : cntB;
			if (cur != minOps) continue;

			List<long[]> all = new ArrayList<>();
			for (int i = 0; i < n; i++) {
				int p = Math.abs(merunavilo[i]) % 2;
				int w = (t == 0) ? ((i % 2 == 0) ? 0 : 1) : ((i % 2 == 0) ? 1 : 0);

				if (p == w) {
					all.add(new long[]{merunavilo[i], i});
				} else {
					all.add(new long[]{(long) merunavilo[i] - 1, i});
					all.add(new long[]{(long) merunavilo[i] + 1, i});
				}
			}

			all.sort((a, b) -> Long.compare(a[0], b[0]));

			int[] freq = new int[n];
			int have = 0;
			int l = 0;

			for (int r = 0; r < all.size(); r++) {
				int idx = (int) all.get(r)[1];
				if (freq[idx] == 0) have++;
				freq[idx]++;

				while (have == n) {
					long diff = all.get(r)[0] - all.get(l)[0];
					if (diff < best) best = diff;
					int li = (int) all.get(l)[1];
					freq[li]--;
					if (freq[li] == 0) have--;
					l++;
				}
			}
		}

		return new int[]{minOps, (int) best};
	}

}
