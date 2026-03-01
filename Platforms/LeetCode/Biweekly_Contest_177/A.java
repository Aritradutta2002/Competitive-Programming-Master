package Platforms.LeetCode.Biweekly_Contest_177;

import java.util.*;

public class A {
	public static void main(String[] args) throws Exception {
		Main.main(args);
	}

	public static int[] minDistinctFreqPair(int[] nums) {
		int n = nums.length;
		Arrays.sort(nums);
		HashMap<Integer, Integer> map = new HashMap<>();
		int[] ans = new int[2];
		
		for(int i = 0; i < nums.length; i++){
			map.put(nums[i], map.getOrDefault(nums[i], 0) + 1);
		}

		int i = 0;
		int j = 1;

		while(i < n && j  < n){
			if(nums[i] < nums[j] && (map.get(nums[i]) != map.get(nums[j]))){
				return new int[] {nums[i], nums[j]};
			}
			else if(nums[i] < nums[j]){
				j++;
			}
			else{
				i++;
				j++;
			}
		}

		return new int[] {-1, -1};
    }
}
