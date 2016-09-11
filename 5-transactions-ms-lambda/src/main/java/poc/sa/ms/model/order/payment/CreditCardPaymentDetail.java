package poc.sa.ms.model.order.payment;

import java.math.BigDecimal;
import java.util.Date;

public class CreditCardPaymentDetail {
  
  private  BigDecimal netPaymentAmount;
  private  String creditCardProvider;
  private  String creditCardNumber;
  private  Date expiryDate;
  private  String verificationCode;
  
  public CreditCardPaymentDetail(){};
  
  public CreditCardPaymentDetail (
      String creditCardProvider,
      String creditCardNumber,
      Date expiryDate,
      String verificationCode,
      BigDecimal netPaymentAmount) {
    
    this.creditCardProvider = creditCardProvider;
    this.creditCardNumber = creditCardNumber;
    this.expiryDate = expiryDate;
    this.verificationCode = verificationCode;
    this.netPaymentAmount = netPaymentAmount;
  }
  
  public String getCreditCardProvider() {
    return creditCardProvider;
  }

  public String getCreditCardNumber() {
    return creditCardNumber;
  }

  public Date getExpiryDate() {
    return expiryDate;
  }


  public String getVerificationCode() {
    return verificationCode;
  }

  public PaymentType getPaymentType() {
    return PaymentType.CREDIT_CARD;
  }

  public BigDecimal getNetPaymentAmount() {
    return netPaymentAmount;
  }
  

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((creditCardNumber == null) ? 0 : creditCardNumber.hashCode());
    result = prime * result + ((creditCardProvider == null) ? 0 : creditCardProvider.hashCode());
    result = prime * result + ((expiryDate == null) ? 0 : expiryDate.hashCode());
    result = prime * result + ((netPaymentAmount == null) ? 0 : netPaymentAmount.hashCode());
    result = prime * result + ((verificationCode == null) ? 0 : verificationCode.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    CreditCardPaymentDetail other = (CreditCardPaymentDetail) obj;
    if (creditCardNumber == null) {
      if (other.creditCardNumber != null)
        return false;
    } else if (!creditCardNumber.equals(other.creditCardNumber))
      return false;
    if (creditCardProvider == null) {
      if (other.creditCardProvider != null)
        return false;
    } else if (!creditCardProvider.equals(other.creditCardProvider))
      return false;
    if (expiryDate == null) {
      if (other.expiryDate != null)
        return false;
    } else if (!expiryDate.equals(other.expiryDate))
      return false;
    if (netPaymentAmount == null) {
      if (other.netPaymentAmount != null)
        return false;
    } else if (!netPaymentAmount.equals(other.netPaymentAmount))
      return false;
    if (verificationCode == null) {
      if (other.verificationCode != null)
        return false;
    } else if (!verificationCode.equals(other.verificationCode))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "CreditCardPaymentDetail [netPaymentAmount=" + netPaymentAmount + ", creditCardProvider=" + creditCardProvider
        + ", creditCardNumber=" + creditCardNumber + ", expiryDate=" + expiryDate + ", verificationCode="
        + verificationCode + "]";
  }

  
  
  

}
