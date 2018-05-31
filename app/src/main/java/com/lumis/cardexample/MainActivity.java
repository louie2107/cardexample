package com.lumis.cardexample;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.lumis.cardexample.cards.CardType;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private EditText etCreditCardNumber;
    private EditText etCreditCardCvv;
    private ImageView ivCardBrand;
    private CardType cardType = CardType.OTHER;
    private SimpleDateFormat sdfTwoDigitYear = new SimpleDateFormat("yy", Locale.getDefault());
    private InputFilter[] noFilter = new InputFilter[]{};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        setupViews();
    }

    protected void setupViews() {
        etCreditCardNumber = findViewById(R.id.et_credit_card_number);
        etCreditCardCvv = findViewById(R.id.et_credit_card_cvv);
        Spinner spExpirationMonth = findViewById(R.id.sp_expiration_month);
        Spinner spExpirationYear = findViewById(R.id.sp_expiration_year);
        Button bnSubmit = findViewById(R.id.bn_submit);
        ivCardBrand = findViewById(R.id.iv_card_brand);
        ImageView cvvHelp = findViewById(R.id.forgot_icon);

        //Load the year spinner up with years.
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                getExpirationYears(15));
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spExpirationYear.setAdapter(spinnerArrayAdapter);

        etCreditCardNumber.addTextChangedListener(this);
        bnSubmit.setOnClickListener(this);
        cvvHelp.setOnClickListener(this);
    }

    /**
     * Update the UI based on the type of card being entered.
     * Display the card image and limit the number of numbers that can be entered
     * for the card number and ccv.
     */
    protected void updateUI() {
        ivCardBrand.setImageResource(cardType.getImageResource());
        if (cardType != CardType.OTHER) {
            if (TextUtils.getTrimmedLength(etCreditCardNumber.getText()) > cardType.getCardNumberLength()) {
                etCreditCardNumber.setText(etCreditCardNumber.getText().toString().substring(0, cardType.getCardNumberLength()));
            }
            if (TextUtils.getTrimmedLength(etCreditCardCvv.getText()) > cardType.getCvvLength()) {
                etCreditCardCvv.setText(etCreditCardCvv.getText().toString().substring(0, cardType.getCvvLength()));
            }
            etCreditCardCvv.setFilters(new InputFilter[]{new InputFilter.LengthFilter(cardType.getCvvLength())});
            etCreditCardNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(cardType.getCardNumberLength())});
        } else {
            etCreditCardNumber.setFilters(noFilter);
            etCreditCardCvv.setFilters(noFilter);
        }
    }

    /**
     * Displays a dialog telling the user whether the card is valid.
     *
     * @param valid Whether the card is valid or not.
     */
    private void showSubmitDialog(boolean valid) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(valid ? getString(R.string.success) : getString(R.string.fail))
                .setTitle(getString(R.string.validation_dialog_title))
                .setPositiveButton(getString(R.string.okay), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }

    private void showCvvHelpDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.cvv_dialog_message))
                .setTitle(getString(R.string.cvv_dialog_title))
                .setPositiveButton(getString(R.string.okay), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }

    /**
     * Returns a string array of years (mm).
     *
     * @param totalYears The number of years after the current year to return.
     * @return A string array of years.
     */
    private String[] getExpirationYears(int totalYears) {
        String[] expirationYears = new String[totalYears];
        String startYear = sdfTwoDigitYear.format(Calendar.getInstance().getTime());
        for (int i = 0; i < totalYears; i++) {
            expirationYears[i] = String.valueOf(Integer.parseInt(startYear) + i);
        }
        return expirationYears;
    }

    //Run validation on card number.
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bn_submit:
                String creditCardNumber = etCreditCardNumber.getText().toString();
                showSubmitDialog(cardType.getValidator().isValid(creditCardNumber));
                break;
            case R.id.forgot_icon:
                showCvvHelpDialog();
                break;
        }

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

    }

    //Detect what kind of card is being typed in.
    @Override
    public void afterTextChanged(Editable editable) {
        CardType currentCardType = cardType;
        if (CardType.AMEX.isOfType(editable.toString())) {
            cardType = CardType.AMEX;
        } else if (CardType.DISCOVER.isOfType(editable.toString())) {
            cardType = CardType.DISCOVER;
        } else if (CardType.JCB.isOfType(editable.toString())) {
            cardType = CardType.JCB;
        } else if (CardType.MASTERCARD.isOfType(editable.toString())) {
            cardType = CardType.MASTERCARD;
        } else if (CardType.VISA.isOfType(editable.toString())) {
            cardType = CardType.VISA;
        } else {
            cardType = CardType.OTHER;
        }
        if (currentCardType != cardType) {
            updateUI();
        }
    }
}