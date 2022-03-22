package com.cs.core.runtime.interactor.exception.onboarding.notfound;

public class AssetComponentColumnNotFoundException extends ColumnNotFoundException {
  
  private static final long serialVersionUID = 1L;
  
  public AssetComponentColumnNotFoundException()
  {
  }
  
  public AssetComponentColumnNotFoundException(
      AssetComponentColumnNotFoundException assetComponentExceptions)
  {
    super();
  }
  
  public AssetComponentColumnNotFoundException(String message)
  {
    super(message);
  }
}
