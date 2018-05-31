package com.lumis.cardexample.validators;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Luhn validator for verifying number patterns.
 */
public class LuhnValidator implements Validator {

    /**
     * Regex for checking if string is all zero's.
     */
    private Pattern zeroCheckRegex = Pattern.compile("^(0)\\1*$");

    private List<Integer> convertStringToIntArray(String number) {
        List<Integer> digits = new ArrayList<>();

        for (int i = 0; i < number.length(); i++) {
            digits.add(Character.getNumericValue(number.charAt(i)));
        }

        return digits;
    }

    /**
     * Run Luhn's algorithm to check if number is valid.
     */
    @Override
    public boolean isValid(String cardNumber) {
        //Strip out all non-numeric characters.
        cardNumber = cardNumber.replaceAll("[^\\d.]", "");
        //Check if the string is empty, or full of zero's.
        if (cardNumber.length() == 0 || zeroCheckRegex.matcher(cardNumber).matches()) {
            return false;
        }

        List<Integer> number = convertStringToIntArray(cardNumber);
        int luhmTotal = 0;
        int length = number.size();
        for (int i = 0; i < length; i++) {
            int digit = number.get(length - i - 1);

            if (i % 2 == 1) {
                digit *= 2;
            }

            if (digit > 9) {
                luhmTotal += digit - 9;
            } else {
                luhmTotal += digit;
            }
        }
        return luhmTotal % 10 == 0;
    }
}