package com.cs.core.exception;

public class PromotionEntityNotFoundInTargetPriceContextException extends NotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public PromotionEntityNotFoundInTargetPriceContextException()
  {
  }
  
  public PromotionEntityNotFoundInTargetPriceContextException(NotFoundException e)
  {
    super(e);
  }
}
