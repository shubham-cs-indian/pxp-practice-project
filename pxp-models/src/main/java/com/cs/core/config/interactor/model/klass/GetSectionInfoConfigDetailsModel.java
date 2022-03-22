package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

public class GetSectionInfoConfigDetailsModel implements IGetSectionInfoConfigDetailsModel {
  
  private static final long                            serialVersionUID = 1L;
  protected Map<String, IConfigEntityInformationModel> referencedContexts;
  
  @Override
  public Map<String, IConfigEntityInformationModel> getReferencedContexts()
  {
    return referencedContexts;
  }
  
  @JsonDeserialize(contentAs = ConfigEntityInformationModel.class)
  @Override
  public void setReferencedContexts(Map<String, IConfigEntityInformationModel> referencedContexts)
  {
    this.referencedContexts = referencedContexts;
  }
}
