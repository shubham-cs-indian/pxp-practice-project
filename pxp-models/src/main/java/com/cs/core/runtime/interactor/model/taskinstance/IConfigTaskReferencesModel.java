package com.cs.core.runtime.interactor.model.taskinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IConfigTaskReferencesModel extends IModel {
  
  public static final String CONTENT_TYPES = "contentTypes";
  
  public Map<String, List<String>> getContentTypes();
  
  public void setContentTypes(Map<String, List<String>> contentTypes);
}
