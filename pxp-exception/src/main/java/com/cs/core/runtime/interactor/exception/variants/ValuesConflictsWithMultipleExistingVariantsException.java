package com.cs.core.runtime.interactor.exception.variants;

import com.cs.core.exception.PluginException;

public class ValuesConflictsWithMultipleExistingVariantsException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public ValuesConflictsWithMultipleExistingVariantsException()
  {
  }
  
  public ValuesConflictsWithMultipleExistingVariantsException(
      ValuesConflictsWithMultipleExistingVariantsException e)
  {
    super(e);
  }
}
