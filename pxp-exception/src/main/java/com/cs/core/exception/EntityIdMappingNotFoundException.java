package com.cs.core.exception;

public class EntityIdMappingNotFoundException extends PluginException {
  
  private static final long serialVersionUID = 1L;
  
  public EntityIdMappingNotFoundException()
  {
    super();
  }
  
  public EntityIdMappingNotFoundException(Exception e)
  {
    super(e);
  }
}
