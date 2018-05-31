package com.lumis.cardexample.cards;


import com.lumis.cardexample.R;
import com.lumis.cardexample.validators.LuhnValidator;
import com.lumis.cardexample.validators.Validator;

import java.util.regex.Pattern;

/**
 * Card types.
 */
public enum CardType {

    AMEX("^3[47].*$", new LuhnValidator(), 15, 4, R.drawable.ic_amex),
    DISCOVER("^6.*$", new LuhnValidator(), 16, 3, R.drawable.ic_discover),
    JCB("^35.*$", new LuhnValidator(), 16, 3, R.drawable.ic_jcb),
    MASTERCARD("^5[1-5].*$", new LuhnValidator(), 16, 3, R.drawable.ic_mastercard),
    VISA("^4.*$", new LuhnValidator(), 16, 3, R.drawable.ic_visa),
    OTHER(new LuhnValidator(), R.drawable.ic_other);

    private Pattern regex;
    private Validator validator;
    private int cardNumberLength;
    private int cvvLength;
    private int imageResource;

    CardType(Validator validator, int imageResource) {
        this.validator = validator;
        this.imageResource = imageResource;
    }

    CardType(String regex, Validator validator, int cardNumberLength, int cvvLength, int imageResource) {
        this.regex = Pattern.compile(regex);
        this.validator = validator;
        this.cardNumberLength = cardNumberLength;
        this.cvvLength = cvvLength;
        this.imageResource = imageResource;
    }

    public Validator getValidator() {
        return validator;
    }

    public int getImageResource() {
        return imageResource;
    }

    public int getCardNumberLength() {
        return cardNumberLength;
    }

    public int getCvvLength() {
        return cvvLength;
    }

    public boolean isOfType(String cardNumber) {
        return regex.matcher(cardNumber).matches();
    }

}
