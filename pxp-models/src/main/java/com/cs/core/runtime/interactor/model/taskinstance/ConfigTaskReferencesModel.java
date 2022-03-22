package com.cs.core.runtime.interactor.model.taskinstance;

import java.util.List;
import java.util.Map;

public class ConfigTaskReferencesModel implements IConfigTaskReferencesModel {
  
  private static final long           serialVersionUID = 1L;
  protected Map<String, List<String>> contentTypes;
  
  @Override
  public Map<String, List<String>> getContentTypes()
  {
    return contentTypes;
  }
  
  @Override
  public void setContentTypes(Map<String, List<String>> contentTypes)
  {
    this.contentTypes = contentTypes;
  }
}
