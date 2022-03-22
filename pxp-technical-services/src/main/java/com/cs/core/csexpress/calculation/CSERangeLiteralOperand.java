package com.cs.core.csexpress.calculation;

import com.cs.core.rdbms.config.idto.IPropertyDTO.LiteralType;
import com.cs.core.technical.icsexpress.calculation.ICSERangeLiteralOperand;

public class CSERangeLiteralOperand extends CSELiteralOperand implements ICSERangeLiteralOperand {
  
  public CSERangeLiteralOperand(double lowerBound, double upperBound)
  {
    super(LiteralType.Range);
    this.lowerBound = lowerBound;
    this.upperBound = upperBound;
  }

  private double lowerBound = 0;
  private double upperBound = 0;

  @Override
  public double getLowerBound()
  {
    return lowerBound;
  }
  
  @Override
  public void setLowerBound(double lowerBound)
  {
    this.lowerBound = lowerBound;
  }
  
  @Override
  public double getUpperBound()
  {
    return upperBound;
  }
  
  @Override
  public void setUpperBound(double upperBound)
  {
    this.upperBound = upperBound;
  }
  
  @Override
  public String toString()
  {
    return "(" + lowerBound +","+ upperBound +")" ;
  } 
}
