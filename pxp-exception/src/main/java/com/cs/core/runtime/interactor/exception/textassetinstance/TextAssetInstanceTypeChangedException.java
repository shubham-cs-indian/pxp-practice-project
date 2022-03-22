package com.cs.core.runtime.interactor.exception.textassetinstance;

import com.cs.core.runtime.interactor.exception.configuration.TypeChangedException;

public class TextAssetInstanceTypeChangedException extends TypeChangedException {
  
  private static final long serialVersionUID = 1L;
  
  public TextAssetInstanceTypeChangedException()
  {
    super();
  }
  
  public TextAssetInstanceTypeChangedException(TypeChangedException e)
  {
    super(e);
  }
}
