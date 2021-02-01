package com.soufianekre.cashnote.helper.currency;

public class CashCurrency extends BaseModel {

    public enum Namespace { ISO4217 } //Namespace for commodities

    private Namespace mNamespace = Namespace.ISO4217;



    public static CashCurrency defaultCashCurrency = new CashCurrency("US Dollars", "USD", 100); //this value is a stub. Will be overwritten when the app is launched


    /**
     * This is the currency code for ISO4217 currencies
     */
    private String mMnemonic;
    private String mFullName;
    private String mCusip;
    private String mLocalSymbol = "";
    private int mSmallestFraction;
    private int mQuoteFlag;


    /**
     * Create a new commodity
     * @param fullName Official full name of the currency
     * @param mnemonic Official abbreviated designation for the currency
     * @param smallestFraction Number of sub-units that the basic commodity can be divided into, as power of 10. e.g. 10^&lt;number_of_fraction_digits&gt;
     */


    public CashCurrency(String fullName, String mnemonic, int smallestFraction){
        this.mFullName = fullName;
        this.mMnemonic = mnemonic;
        setSmallestFraction(smallestFraction);
    }

    public CashCurrency(String fullName, String mnemonic, int smallestFraction,String symbol){
        this.mFullName = fullName;
        this.mMnemonic = mnemonic;
        setSmallestFraction(smallestFraction);
        setLocalSymbol(symbol);
    }

    /**
     * Returns an instance of commodity for the specified currencyCode
     * @param currencyCode ISO 4217 currency code (3-letter)
     */
    public static CashCurrency getInstance(String currencyCode){
        return CurrencyHelper.getCommodity(currencyCode);
    }

    public Namespace getNamespace() {
        return mNamespace;
    }

    public void setNamespace(Namespace namespace) {
        this.mNamespace = namespace;
    }

    /**
     * Returns the mnemonic, or currency code for ISO4217 currencies
     * @return Mnemonic of the commodity
     */
    public String getMnemonic() {
        return mMnemonic;
    }

    /**
     * Alias for {@link #getMnemonic()}
     * @return ISO 4217 code for this commodity
     */
    public String getCurrencyCode(){
        return getMnemonic();
    }

    public void setMnemonic(String mMnemonic) {
        this.mMnemonic = mMnemonic;
    }

    public String getFullName() {
        return mFullName;
    }

    public void setFullName(String mFullName) {
        this.mFullName = mFullName;
    }

    public String getCusip() {
        return mCusip;
    }

    public void setCusip(String mCusip) {
        this.mCusip = mCusip;
    }

    public String getLocalSymbol() {
        return mLocalSymbol;
    }

    /**
     * Returns the symbol for this commodity.
     * <p>Normally this would be the local symbol, but in it's absence, the mnemonic (currency code)
     * is returned.</p>
     * @return
     */

    public String getSymbol(){
        if (mLocalSymbol == null || mLocalSymbol.isEmpty()){
            return mMnemonic;
        }
        return mLocalSymbol;
    }

    public void setLocalSymbol(String localSymbol) {
        this.mLocalSymbol = localSymbol;
    }

    /**
     * Returns the smallest fraction supported by the commodity as a power of 10.
     * <p>i.e. for commodities with no fractions, 1 is returned, for commodities with 2 fractions, 100 is returned</p>
     * @return Smallest fraction as power of 10
     */
    public int getSmallestFraction() {
        return mSmallestFraction;
    }

    /**
     * Returns the (minimum) number of digits that this commodity supports in its fractional part
     * <p>For any unsupported values for the smallest fraction, a default value of 2 is returned.
     * Supported values for the smallest fraction are powers of 10 i.e. 1, 10, 100 etc</p>
     * @return Number of digits in fraction
     * @see #getSmallestFraction()
     */
    public int getSmallestFractionDigits(){
        if (mSmallestFraction == 0){
            return 0;
        } else {
            return Integer.numberOfTrailingZeros(mSmallestFraction);
        }
    }

    /**
     * Sets the smallest fraction for the commodity.
     * <p>The fraction is a power of 10. So commodities with 2 fraction digits, have fraction of 10^2 = 100.<br>
     *     If the parameter is any other value, a default fraction of 100 will be set</p>
     * @param smallestFraction Smallest fraction as power of ten
     * @throws IllegalArgumentException if the smallest fraction is not a power of 10
     */
    public void setSmallestFraction(int smallestFraction) {
        this.mSmallestFraction = smallestFraction;
    }

    public int getQuoteFlag() {
        return mQuoteFlag;
    }

    public void setQuoteFlag(int quoteFlag) {
        this.mQuoteFlag = quoteFlag;
    }

    @Override
    /**
     * Returns the full name of the currency, or the currency code if there is no full name
     * @return String representation of the commodity
     */
    public String toString() {
        return mFullName == null || mFullName.isEmpty() ? mMnemonic : mFullName;
    }

    /**
     * Overrides {@link BaseModel#equals(Object)} to compare only the currency codes of the commodity.
     * <p>Two commodities are considered equal if they have the same currency code</p>
     * @param o CashCurrency instance to compare
     * @return {@code true} if both instances have same currency code, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CashCurrency cashCurrency = (CashCurrency) o;

        return mMnemonic.equals(cashCurrency.mMnemonic);

    }

    @Override
    public int hashCode() {
        return mMnemonic.hashCode();
    }
}
