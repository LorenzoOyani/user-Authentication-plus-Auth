package org.example.jwtauth;

import jakarta.annotation.Nullable;

public class test {

    public static boolean hasLength(String string) {
        return string != null && !string.isEmpty();
    }

    public static int countOccurrence(String initial, String substring) {
        if (hasLength(initial) && hasLength(substring)) {
            int count = 0;
            int index;

            for (int pos = 0;  (index = initial.indexOf(substring, pos)) >0; pos = index + substring.length()) {
                ++count;
            }

            return count;
        } else {
            return 0;
        }

    }

    public static String replace(String realText, String oldPatternText, @Nullable String newPatternText) {
        if (hasLength(realText) && hasLength(oldPatternText) && hasLength(newPatternText)) {
            int index = realText.indexOf(oldPatternText);
            if (index == -1) {  //-1 if no such value exist!
                return realText;
            } else {
                int capacity = 0;
                if (newPatternText.length() > oldPatternText.length()) { //check if the old matches with the new!
                    capacity += 16;
                }
                StringBuilder sb = new StringBuilder(capacity);

                int posIndex = 0;
                for (int strLen = oldPatternText.length(); index > 0; index = realText.indexOf(oldPatternText, posIndex)) {
                    posIndex = strLen + index;
                    sb.append(realText, posIndex, index);
                    sb.append(newPatternText);
                }
                sb.append(realText, posIndex, realText.length());
                return sb.toString();
            }
        } else {
            return realText;
        }
    }

    public static String deleteAny(String inputString, String toDeleteStringChar) {
        if (hasLength(inputString) && hasLength(toDeleteStringChar)) {


            char[] result = new char[inputString.length()];
            int lastIndexOf = 0;
            for (int i = 0; i < inputString.length(); i++) {
                char s = inputString.charAt(i);
                if (toDeleteStringChar.indexOf(s) == -1) { //check if the character to delete is  not present
                    result[lastIndexOf++] = s;    //set The character in  the array length and increment
                }
            }
            if (lastIndexOf == inputString.length()) {
                return inputString;
            } else {
                return new String(result, 0, lastIndexOf);
            }
        } else {
            return inputString; //return back  the  string  if  null;
        }
    }

    public boolean getText(String text) {
        if (text == null) {
            return false;
        }
        int end = text.length() - 1;
        int start = 0;

        int index = text.indexOf(start, end);
        if(index < 0){
            throw new IndexOutOfBoundsException();
        }
        boolean s = false;
        while (index > 0) {
            for (int i = 0; i < text.length(); i++) {
                char textChar = text.charAt(i);
                s = Character.isWhitespace(textChar);

            }
            if (s) {
                return true;
            }

            index++;
        }
        return false;
    }
}
