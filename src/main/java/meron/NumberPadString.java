package meron;// Java program to implement the
// above approach

import java.util.*;
import java.lang.*;

class NumberPadString {

    final static String[] keyPad = new String[] {"0", "1", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};


    private static List<String> printWords(int[] numbers, int len, int numIndex, String prefix) {
        if (len == numIndex) {
            return new ArrayList<>(Collections.singleton(prefix));
        }

        List<String> stringList = new ArrayList<>();

        for (int i = 0; i < keyPad[numbers[numIndex]].length(); i++) {
            String sCopy = prefix + keyPad[numbers[numIndex]].charAt(i);
            stringList.addAll(printWords(numbers, len,numIndex + 1, sCopy));
        }
        return stringList;
    }

    private static void printWords(int[] numbers) {
        List<String> stringList =
                printWords(numbers, numbers.length, 0, "");
        System.out.println(stringList);
        //stringList.forEach(System.out :: println);
    }

   // Driver code
    public static void main(String[] args) {
        int[] number = {2, 3};
        printWords(number);
    }
}