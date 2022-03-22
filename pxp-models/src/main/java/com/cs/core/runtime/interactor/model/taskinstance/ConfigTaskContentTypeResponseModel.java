package com.cs.core.runtime.interactor.model.taskinstance;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;
import java.util.Map;

public class ConfigTaskContentTypeResponseModel implements IConfigTaskContentTypeResponseModel {
  
  private static final long             serialVersionUID = 1L;
  protected Map<String, List<String>>   contentTypes;
  protected IConfigTaskContentTypeModel configDetails;
  
  @Override
  public Map<String, List<String>> getContentTypes()
  {
    return this.contentTypes;
  }
  
  @Override
  public void setContentTypes(Map<String, List<String>> contentTypes)
  {
    this.contentTypes = contentTypes;
  }
  
  @Override
  public IConfigTaskContentTypeModel getconfigDetails()
  {
    return this.configDetails;
  }
  
  @JsonDeserialize(as = ConfigTaskContentTypeModel.class)
  @Override
  public void setconfigDetails(IConfigTaskContentTypeModel configDetails)
  {
    this.configDetails = configDetails;
  }
}
