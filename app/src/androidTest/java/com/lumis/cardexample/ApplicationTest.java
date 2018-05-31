package com.lumis.cardexample;

import android.test.suitebuilder.annotation.SmallTest;

import com.lumis.cardexample.cards.CardType;
import com.lumis.cardexample.validators.LuhnValidator;
import com.lumis.cardexample.validators.Validator;

import junit.framework.TestCase;

public class ApplicationTest extends TestCase {

    @SmallTest
    public void testCardBrands() {
        assertEquals(true, CardType.AMEX.isOfType("371449635398431"));
        assertEquals(true, CardType.DISCOVER.isOfType("6011111111111117"));
        assertEquals(true, CardType.JCB.isOfType("3530111333300000"));
        assertEquals(true, CardType.MASTERCARD.isOfType("5555555555554444"));
        assertEquals(true, CardType.VISA.isOfType("4111111111111111"));

        assertEquals(false, CardType.AMEX.isOfType("4111111111111111"));
        assertEquals(false, CardType.DISCOVER.isOfType("3530111333300000"));
        assertEquals(false, CardType.JCB.isOfType("5555555555554444"));
        assertEquals(false, CardType.MASTERCARD.isOfType("6011111111111117"));
        assertEquals(false, CardType.VISA.isOfType("371449635398431"));

        assertEquals(false, CardType.AMEX.isOfType("f231sdfg#$#@111111"));
        assertEquals(false, CardType.DISCOVER.isOfType("f231sdfg#$#@111111"));
        assertEquals(false, CardType.JCB.isOfType("f231sdfg#$#@111111"));
        assertEquals(false, CardType.MASTERCARD.isOfType("f231sdfg#$#@111111"));
        assertEquals(false, CardType.VISA.isOfType("f231sdfg#$#@111111"));
    }

    @SmallTest
    public void testValidations() {
        Validator luhnValidator = new LuhnValidator();

        assertEquals(true, CardType.AMEX.getValidator().isValid("49927398716"));
        assertEquals(true, CardType.DISCOVER.getValidator().isValid("6011111111111117"));
        assertEquals(true, CardType.JCB.getValidator().isValid("3530111333300000"));
        assertEquals(true, CardType.MASTERCARD.getValidator().isValid("5555555555554444"));
        assertEquals(true, CardType.VISA.getValidator().isValid("4111111111111111"));

        assertEquals(false, luhnValidator.isValid("0"));
        assertEquals(false, luhnValidator.isValid("000"));
        assertEquals(false, luhnValidator.isValid("12543452546"));
        assertEquals(false, luhnValidator.isValid("0-+=23=-3"));
    }
}