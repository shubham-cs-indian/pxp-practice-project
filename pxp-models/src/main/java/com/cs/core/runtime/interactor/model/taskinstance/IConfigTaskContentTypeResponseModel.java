package com.cs.core.runtime.interactor.model.taskinstance;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;
import java.util.Map;

public interface IConfigTaskContentTypeResponseModel extends IModel {
  
  public static final String CONTENT_TYPES  = "contentTypes";
  public static final String CONFIG_DETAILS = "configDetails";
  
  public Map<String, List<String>> getContentTypes();
  
  public void setContentTypes(Map<String, List<String>> contentTypes);
  
  public IConfigTaskContentTypeModel getconfigDetails();
  
  public void setconfigDetails(IConfigTaskContentTypeModel configDetails);
}
