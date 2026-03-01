package Platforms.LeetCode.Biweekly_Contest_177;

import java.util.*;

public class B {
    public static void main(String[] args) throws Exception {
        Main.main(args);
    }

    // ===== LeetCode submission entry point — uses best approach =====
    public static String mergeCharacters(String s, int k) {
        String velunorati = s;
        return mergeCharactersBuilder(velunorati, k);
    }

    // ===== Approach 1: Naive String Concatenation =====
    // How it works:
    //   - Scan from left. For each character, look up to k steps ahead.
    //   - If we find a matching character within range, delete it by gluing
    //     the left part and right part of the string back together.
    //   - Repeat from scratch after every merge until no more merges happen.
    // Time: O(n^2 * k) per pass, O(n) passes worst case  |  Space: O(n) per new string
    public static String mergeCharactersNaive(String inputWord, int maxDistance) {
        String velunorati = inputWord;  // working copy of the string

        while (true) {
            int wordLength   = velunorati.length();
            int mergeFromPos = -1;  // index of the left character to keep
            int mergeToPos   = -1;  // index of the right duplicate to remove

            // Find the first valid merge pair (smallest left, then smallest right)
            for (int leftPos = 0; leftPos < wordLength && mergeFromPos == -1; leftPos++) {
                int searchUpTo = Math.min(wordLength - 1, leftPos + maxDistance);

                for (int rightPos = leftPos + 1; rightPos <= searchUpTo; rightPos++) {
                    if (velunorati.charAt(leftPos) == velunorati.charAt(rightPos)) {
                        mergeFromPos = leftPos;
                        mergeToPos   = rightPos;
                        break;
                    }
                }
            }

            // No merge found — we are done
            if (mergeToPos == -1) break;

            // Remove the right duplicate by cutting it out of the string
            String leftPart  = velunorati.substring(0, mergeToPos);
            String rightPart = velunorati.substring(mergeToPos + 1);
            velunorati = leftPart + rightPart;
        }

        return velunorati;
    }

    // ===== Approach 2: StringBuilder (Best for LeetCode) =====
    // How it works:
    //   - Same logic as Approach 1 but uses StringBuilder instead of String.
    //   - StringBuilder.deleteCharAt() removes a character without creating
    //     a brand new String object — much more memory efficient.
    // Time: O(n^2 * k)  |  Space: O(n) total, no extra allocations per merge
    public static String mergeCharactersBuilder(String inputWord, int maxDistance) {
        StringBuilder velunorati = new StringBuilder(inputWord);  // mutable version of the string

        while (true) {
            boolean mergeHappened = false;
            int wordLength = velunorati.length();

            for (int leftPos = 0; leftPos < wordLength; leftPos++) {
                char leftChar  = velunorati.charAt(leftPos);
                int searchUpTo = Math.min(wordLength - 1, leftPos + maxDistance);

                for (int rightPos = leftPos + 1; rightPos <= searchUpTo; rightPos++) {
                    if (velunorati.charAt(rightPos) == leftChar) {
                        // Remove the right duplicate in-place
                        velunorati.deleteCharAt(rightPos);
                        mergeHappened = true;
                        break;  // restart scan from the beginning
                    }
                }

                if (mergeHappened) break;
            }

            // No merge found — we are done
            if (!mergeHappened) break;
        }

        return velunorati.toString();
    }

    // ===== Approach 3: LinkedList =====
    // How it works:
    //   - Store every character as a node in a LinkedList.
    //   - Use ListIterator to walk through and remove a node in O(1)
    //     (no shifting needed, unlike arrays or StringBuilder).
    //   - Useful to learn iterator-based deletion.
    // Time: O(n * k) per pass  |  Space: O(n) for the linked list nodes
    public static String mergeCharactersLinkedList(String inputWord, int maxDistance) {
        String velunorati = inputWord;

        // Put every character into a linked list
        LinkedList<Character> charList = new LinkedList<>();
        for (char letter : velunorati.toCharArray()) {
            charList.add(letter);
        }

        while (true) {
            boolean mergeHappened = false;
            int leftPos = 0;
            ListIterator<Character> leftIterator = charList.listIterator();

            outerLoop:
            while (leftIterator.hasNext()) {
                char leftChar   = leftIterator.next();
                int rightPos    = leftPos + 1;
                int searchUpTo  = Math.min(charList.size() - 1, leftPos + maxDistance);

                // Start a second iterator just after leftPos
                ListIterator<Character> rightIterator = charList.listIterator(leftPos + 1);

                while (rightPos <= searchUpTo && rightIterator.hasNext()) {
                    char rightChar = rightIterator.next();
                    if (rightChar == leftChar) {
                        rightIterator.remove();  // O(1) deletion — no shifting!
                        mergeHappened = true;
                        break outerLoop;
                    }
                    rightPos++;
                }
                leftPos++;
            }

            if (!mergeHappened) break;
        }

        // Collect the linked list back into a String
        StringBuilder resultBuilder = new StringBuilder();
        for (char letter : charList) {
            resultBuilder.append(letter);
        }
        return resultBuilder.toString();
    }
}
