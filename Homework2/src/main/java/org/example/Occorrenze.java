package org.example;

// Java program to count the number of occurrence of a word in the given string

import static java.lang.Character.*;

public class Occorrenze {

    public static int countOccurrences(String str, String word)
    {
        
        if (str == null){
            throw new NullPointerException();
        }

        if (str.isEmpty()){
            System.out.println("La stringa e' vuota");
            return 0;
        }

        int lenght = str.length();
        char[] temp = new char[lenght];
        for (int k=0; k < str.length(); k++){
            temp[k] = str.charAt(k);
            if (isDigit(temp[k])){
                temp[k] = 'X';
            }
        }
        str = String.valueOf(temp);
        System.out.println(str);

        // split the string by spaces in a
        String a[] = str.split(" ");

        // search for pattern in a
        int count = 0;
        for (int i = 0; i < a.length; i++)
        {
            // if match found increase count
            if (word.equals(a[i]))
                count++;
        }

        return count;
    }

}