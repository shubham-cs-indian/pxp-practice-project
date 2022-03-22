package com.cs.core.config.interactor.model.imprt;

public class EntityContextTalendModel implements IEntityContextTalendModel {
  
  private static final long serialVersionUID = 1L;
  protected String[]        context;
  
  @Override
  public String[] getContext()
  {
    return context;
  }
  
  @Override
  public void setContext(String[] context)
  {
    if (context == null) {
      context = new String[0];
    }
    this.context = context;
  }
}
