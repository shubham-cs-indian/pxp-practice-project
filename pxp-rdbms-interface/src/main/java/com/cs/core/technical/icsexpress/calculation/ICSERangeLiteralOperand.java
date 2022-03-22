package com.cs.core.technical.icsexpress.calculation;

public interface ICSERangeLiteralOperand extends ICSELiteralOperand {
  
  /**
   * @return lowerbound of range
   */
  public double getLowerBound();
  
  /**
   * 
   * @param lowerBound : lowerBound of given range
   */
  public void setLowerBound(double lowerBound);
  
  /**
   * @return upperBound of Range
   */
  public double getUpperBound();
  
  /**
   * @param upperBound : upperBound of given range
   */
  public void setUpperBound(double upperBound);
  
}
