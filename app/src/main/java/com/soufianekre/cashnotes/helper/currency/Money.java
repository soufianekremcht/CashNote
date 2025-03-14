package com.soufianekre.cashnotes.helper.currency;

import androidx.annotation.NonNull;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import timber.log.Timber;

public final class Money implements Comparable<Money>{

    /**
     * Currency of the account
     */
    private CashCurrency mCashCurrency;

    /**
     * Amount value held by this object
     */
    private BigDecimal mAmount;

    /**
     * Rounding mode to be applied when performing operations
     * Defaults to {@link RoundingMode#HALF_EVEN}
     */
    protected RoundingMode ROUNDING_MODE = RoundingMode.HALF_EVEN;

    /**
     * Default currency code (according ISO 4217)
     * This is typically initialized to the currency of the device default locale,
     * otherwise US dollars are used
     */
    public static String DEFAULT_CURRENCY_CODE 	= "USD";

    /**
     * A zero instance with the currency of the default locale.
     * This can be used anywhere where a starting amount is required without having to create a new object
     */
    private static Money sDefaultZero;

    /**
     * Returns a Money instance initialized to the local currency and value 0
     * @return Money instance of value 0 in locale currency
     */
    public static Money getZeroInstance(){
        if (sDefaultZero == null) {
            sDefaultZero = new Money(BigDecimal.ZERO, CashCurrency.defaultCashCurrency);
        }
        return sDefaultZero;
    }

    /**
     * Returns the {@link BigDecimal} from the {@code numerator} and {@code denominator}
     * @param numerator Number of the fraction
     * @param denominator Denominator of the fraction
     * @return BigDecimal representation of the number
     */
    public static BigDecimal getBigDecimal(long numerator, long denominator) {
        int scale;
        if (numerator == 0 && denominator == 0) {
            denominator = 1;
        }

        scale = Integer.numberOfTrailingZeros((int)denominator);
        return new BigDecimal(BigInteger.valueOf(numerator), scale);
    }

    /**
     * Creates a new money amount
     * @param amount Value of the amount
     * @param cashCurrency CashCurrency of the money
     */
    public Money(BigDecimal amount, CashCurrency cashCurrency){
        this.mCashCurrency = cashCurrency;
        setAmount(amount); //cashCurrency has to be set first. Because we use it's scale
    }

    /**
     * Overloaded constructor.
     * Accepts strings as arguments and parses them to create the Money object
     * @param amount Numerical value of the Money
     * @param currencyCode Currency code as specified by ISO 4217
     */
    public Money(String amount, String currencyCode){
        //commodity has to be set first
        mCashCurrency = CashCurrency.getInstance(currencyCode);
        setAmount(new BigDecimal(amount));
    }

    /**
     * Constructs a new money amount given the numerator and denominator of the amount.
     * The rounding mode used for the division is {@link BigDecimal#ROUND_HALF_EVEN}
     * @param numerator Numerator as integer
     * @param denominator Denominator as integer
     * @param currencyCode 3-character currency code string
     */
    public Money(long numerator, long denominator, String currencyCode){
        mAmount = getBigDecimal(numerator, denominator);
        setCommodity(currencyCode);
    }

    /**
     * Copy constructor.
     * Creates a new Money object which is a clone of <code>money</code>
     * @param money Money instance to be cloned
     */
    public Money(Money money){
        setCommodity(money.getCommodity());
        setAmount(money.asBigDecimal());
    }

    /**
     * Creates a new Money instance with 0 amount and the <code>currencyCode</code>
     * @param currencyCode Currency to use for this money instance
     * @return Money object with value 0 and currency <code>currencyCode</code>
     */
    public static Money createZeroInstance(@NonNull String currencyCode){
        CashCurrency cashCurrency = CashCurrency.getInstance(currencyCode);
        return new Money(BigDecimal.ZERO, cashCurrency);
    }

    /**
     * Returns the commodity used by the Money
     * @return Instance of commodity
     */
    public CashCurrency getCommodity(){
        return mCashCurrency;
    }

    /**
     * Returns a new <code>Money</code> object the currency specified by <code>currency</code>
     * and the same value as this one. No value exchange between the currencies is performed.
     * @param cashCurrency {@link CashCurrency} to assign to new <code>Money</code> object
     * @return {@link Money} object with same value as current object, but with new <code>currency</code>
     */
    public Money withCurrency(@NonNull CashCurrency cashCurrency){
        return new Money(mAmount, cashCurrency);
    }

    /**
     * Sets the cashCurrency for the Money
     * <p>No currency conversion is performed</p>
     * @param cashCurrency CashCurrency instance
     */
    private void setCommodity(@NonNull CashCurrency cashCurrency){
        this.mCashCurrency = cashCurrency;
    }

    /**
     * Sets the commodity for the Money
     * @param currencyCode ISO 4217 currency code
     */
    private void setCommodity(@NonNull String currencyCode){
        mCashCurrency = CashCurrency.getInstance(currencyCode);
    }

    /**
     * Returns the GnuCash format numerator for this amount.
     * <p>Example: Given an amount 32.50$, the numerator will be 3250</p>
     * @return GnuCash numerator for this amount
     */
    public long getNumerator() {
        try {
            return mAmount.scaleByPowerOfTen(getScale()).longValueExact();
        } catch (ArithmeticException e) {
            String msg = "Currency " + mCashCurrency.getCurrencyCode() +
                    " with scale " + getScale() +
                    " has amount " + mAmount.toString();
            //Crashlytics.log(msg);
            Timber.tag(getClass().getName()).e(msg);
            throw e;
        }
    }

    /**
     * Returns the GnuCash amount format denominator for this amount
     * <p>The denominator is 10 raised to the power of number of fractional digits in the currency</p>
     * @return GnuCash format denominator
     */
    public long getDenominator() {
        int scale = getScale();
        return BigDecimal.ONE.scaleByPowerOfTen(scale).longValueExact();
    }

    /**
     * Returns the scale (precision) used for the decimal places of this amount.
     * <p>The scale used depends on the commodity</p>
     * @return Scale of amount as integer
     */
    private int getScale() {
        int scale = mCashCurrency.getSmallestFractionDigits();
        if (scale < 0) {
            scale = mAmount.scale();
        }
        if (scale < 0) {
            scale = 0;
        }
        return scale;
    }

    /**
     * Returns the amount represented by this Money object
     * <p>The scale and rounding mode of the returned value are set to that of this Money object</p>
     * @return {@link BigDecimal} valure of amount in object
     */
    public BigDecimal asBigDecimal() {
        return mAmount.setScale(mCashCurrency.getSmallestFractionDigits(), RoundingMode.HALF_EVEN);
    }

    /**
     * Returns the amount this object
     * @return Double value of the amount in the object
     */
    public double asDouble(){
        return mAmount.doubleValue();
    }

    /**
     * An alias for {@link #toPlainString()}
     * @return Money formatted as a string (excludes the currency)
     */
    public String asString(){
        return toPlainString();
    }

    /**
     * Returns a string representation of the Money object formatted according to
     * the <code>locale</code> and includes the currency symbol.
     * The output precision is limited to the number of fractional digits supported by the currency
     * @param locale Locale to use when formatting the object
     * @return String containing formatted Money representation
     */
    public String formattedString(Locale locale){

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(locale);

        String symbol;
        //if we want to show US Dollars for locales which also use Dollars, for example, Canada
        if (mCashCurrency.equals(CashCurrency.defaultCashCurrency) && !locale.equals(Locale.US)) {
            symbol = "US$";
        } else {
            symbol = mCashCurrency.getSymbol();
        }
        DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat)currencyFormat).getDecimalFormatSymbols();
        decimalFormatSymbols.setCurrencySymbol(symbol);
        ((DecimalFormat)currencyFormat).setDecimalFormatSymbols(decimalFormatSymbols);
        currencyFormat.setMinimumFractionDigits(mCashCurrency.getSmallestFractionDigits());
        currencyFormat.setMaximumFractionDigits(mCashCurrency.getSmallestFractionDigits());

        return currencyFormat.format(asDouble());
/*
// 	old currency formatting code
		NumberFormat formatter = NumberFormat.getInstance(locale);
		formatter.setMinimumFractionDigits(mCashCurrency.getSmallestFractionDigits());
		formatter.setMaximumFractionDigits(mCashCurrency.getSmallestFractionDigits());
		Currency currency = Currency.getInstance(mCashCurrency.getCurrencyCode());
		return formatter.format(asDouble()) + " " + currency.getSymbol(locale);
*/
    }

    /**
     * Equivalent to calling formattedString(Locale.getDefault())
     * @return String formatted Money representation in default locale
     */
    public String formattedString(){
        return formattedString(Locale.getDefault());
    }

    /**
     * Returns a new Money object whose amount is the negated value of this object amount.
     * The original <code>Money</code> object remains unchanged.
     * @return Negated <code>Money</code> object
     */
    public Money negate(){
        return new Money(mAmount.negate(), mCashCurrency);
    }

    /**
     * Sets the amount value of this <code>Money</code> object
     * @param amount {@link BigDecimal} amount to be set
     */
    private void setAmount(@NonNull BigDecimal amount) {
        mAmount = amount.setScale(mCashCurrency.getSmallestFractionDigits(), ROUNDING_MODE);
    }

    /**
     * Returns a new <code>Money</code> object whose value is the sum of the values of
     * this object and <code>addend</code>.
     *
     * @param addend Second operand in the addition.
     * @return Money object whose value is the sum of this object and <code>money</code>
     * @throws CurrencyMismatchException if the <code>Money</code> objects to be added have different Currencies
     */
    public Money add(Money addend){
        if (!mCashCurrency.equals(addend.mCashCurrency))
            throw new CurrencyMismatchException();

        BigDecimal bigD = mAmount.add(addend.mAmount);
        return new Money(bigD, mCashCurrency);
    }

    /**
     * Returns a new <code>Money</code> object whose value is the difference of the values of
     * this object and <code>subtrahend</code>.
     * This object is the minuend and the parameter is the subtrahend
     * @param subtrahend Second operand in the subtraction.
     * @return Money object whose value is the difference of this object and <code>subtrahend</code>
     * @throws CurrencyMismatchException if the <code>Money</code> objects to be added have different Currencies
     */
    public Money subtract(Money subtrahend){
        if (!mCashCurrency.equals(subtrahend.mCashCurrency))
            throw new CurrencyMismatchException();

        BigDecimal bigD = mAmount.subtract(subtrahend.mAmount);
        return new Money(bigD, mCashCurrency);
    }

    /**
     * Returns a new <code>Money</code> object whose value is the quotient of the values of
     * this object and <code>divisor</code>.
     * This object is the dividend and <code>divisor</code> is the divisor
     * <p>This method uses the rounding mode {@link BigDecimal#ROUND_HALF_EVEN}</p>
     * @param divisor Second operand in the division.
     * @return Money object whose value is the quotient of this object and <code>divisor</code>
     * @throws CurrencyMismatchException if the <code>Money</code> objects to be added have different Currencies
     */
    public Money divide(Money divisor){
        if (!mCashCurrency.equals(divisor.mCashCurrency))
            throw new CurrencyMismatchException();

        BigDecimal bigD = mAmount.divide(divisor.mAmount, mCashCurrency.getSmallestFractionDigits(), ROUNDING_MODE);
        return new Money(bigD, mCashCurrency);
    }

    /**
     * Returns a new <code>Money</code> object whose value is the quotient of the division of this objects
     * value by the factor <code>divisor</code>
     * @param divisor Second operand in the addition.
     * @return Money object whose value is the quotient of this object and <code>divisor</code>
     */
    public Money divide(int divisor){
        Money moneyDiv = new Money(new BigDecimal(divisor), mCashCurrency);
        return divide(moneyDiv);
    }

    /**
     * Returns a new <code>Money</code> object whose value is the product of the values of
     * this object and <code>money</code>.
     *
     * @param money Second operand in the multiplication.
     * @return Money object whose value is the product of this object and <code>money</code>
     * @throws CurrencyMismatchException if the <code>Money</code> objects to be added have different Currencies
     */
    public Money multiply(Money money){
        if (!mCashCurrency.equals(money.mCashCurrency))
            throw new CurrencyMismatchException();

        BigDecimal bigD = mAmount.multiply(money.mAmount);
        return new Money(bigD, mCashCurrency);
    }

    /**
     * Returns a new <code>Money</code> object whose value is the product of this object
     * and the factor <code>multiplier</code>
     * <p>The currency of the returned object is the same as the current object</p>
     * @param multiplier Factor to multiply the amount by.
     * @return Money object whose value is the product of this objects values and <code>multiplier</code>
     */
    public Money multiply(int multiplier){
        Money moneyFactor = new Money(new BigDecimal(multiplier), mCashCurrency);
        return multiply(moneyFactor);
    }

    /**
     * Returns a new <code>Money</code> object whose value is the product of this object
     * and the factor <code>multiplier</code>
     * @param multiplier Factor to multiply the amount by.
     * @return Money object whose value is the product of this objects values and <code>multiplier</code>
     */
    public Money multiply(@NonNull BigDecimal multiplier){
        return new Money(mAmount.multiply(multiplier), mCashCurrency);
    }

    /**
     * Returns true if the amount held by this Money object is negative
     * @return <code>true</code> if the amount is negative, <code>false</code> otherwise.
     */
    public boolean isNegative(){
        return mAmount.compareTo(BigDecimal.ZERO) < 0;
    }

    /**
     * Returns the string representation of the amount (without currency) of the Money object.
     *
     * <p>This string is not locale-formatted. The decimal operator is a period (.)
     * For a locale-formatted version, see the method overload {@link #
     * toLocaleString(Locale)}</p>
     * @return String representation of the amount (without currency) of the Money object
     */
    public String toPlainString(){
        return mAmount.setScale(mCashCurrency.getSmallestFractionDigits(), ROUNDING_MODE).toPlainString();
    }

    /**
     * Returns a locale-specific representation of the amount of the Money object (excluding the currency)
     *
     * @return String representation of the amount (without currency) of the Money object
     */
    public String toLocaleString(){
        return String.format(Locale.getDefault(), "%.2f", asDouble());
    }

    /**
     * Returns the string representation of the Money object (value + currency) formatted according
     * to the default locale
     * @return String representation of the amount formatted with default locale
     */
    @Override
    public String toString() {
        return formattedString(Locale.getDefault());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (mAmount.hashCode());
        result = prime * result + (mCashCurrency.hashCode());
        return result;
    }

    /** //FIXME: equality failing for money objects
     * Two Money objects are only equal if their amount (value) and currencies are equal
     * @param obj Object to compare with
     * @return <code>true</code> if the objects are equal, <code>false</code> otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Money other = (Money) obj;
        if (!mAmount.equals(other.mAmount))
            return false;
        return mCashCurrency.equals(other.mCashCurrency);
    }

    @Override
    public int compareTo(@NonNull Money another) {
        if (!mCashCurrency.equals(another.mCashCurrency))
            throw new CurrencyMismatchException();
        return mAmount.compareTo(another.mAmount);
    }

    /**
     * Returns a new instance of {@link Money} object with the absolute value of the current object
     * @return Money object with absolute value of this instance
     */
    public Money abs() {
        return new Money(mAmount.abs(), mCashCurrency);
    }

    /**
     * Checks if the value of this amount is exactly equal to zero.
     * @return {@code true} if this money amount is zero, {@code false} otherwise
     */
    public boolean isAmountZero() {
        return mAmount.compareTo(BigDecimal.ZERO) == 0;
    }

    public static class CurrencyMismatchException extends IllegalArgumentException{
        @Override
        public String getMessage() {
            return "Cannot perform operation on Money instances with different currencies";
        }
    }
}
