package meron;

import java.util.*;

public class LetterCombinationsPhoneNumber {
    final static String[] keyPad = new String[] {"0", "1", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};

    public static List<String> letterCombinations(String digits) {
        List<String> list = new ArrayList<>();
        if(digits == null || digits.length() == 0)
            return list;

        list.add("");
        for(int i = 0; i < digits.length(); i++) {
            while(list.get(0).length() == i) {
                String prefix = list.remove(0);
                int digit = Character.getNumericValue(digits.charAt(i));// - "0";
                for(char character : keyPad[digit].toCharArray())
                    list.add(prefix + character);
            }
        }
        return list;
    }
  public static  List<String> combination(String digits, int n) {
        List<String> list = new ArrayList<>();
        Queue<String> q = new PriorityQueue<>();
        q.add("");
        while (!q.isEmpty()) {
            String s = q.remove();

            // If complete word is generated push it in the list
            if (s.length() == n)
                list.add(s);
            else
                //int digit = Character.getNumericValue(digits.charAt(s.length()));// - "0";
            for(char letter : keyPad[s.length()].toCharArray())
                q.add(s+letter);
        }
        return list;
    }
    public static void main(String[] args) {
        System.out.println(letterCombinations("234"));
        System.out.println(combination("234",4));
    }
}
