package com.cs.core.config.interactor.exception.textasset;

import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.exception.NotFoundException;

public class TextAssetKlassNotFoundException extends KlassNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public TextAssetKlassNotFoundException()
  {
    super();
  }
  
  public TextAssetKlassNotFoundException(NotFoundException e)
  {
    super(e);
  }
}
