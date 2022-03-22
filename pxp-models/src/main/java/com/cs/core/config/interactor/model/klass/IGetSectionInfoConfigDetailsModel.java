package com.cs.core.config.interactor.model.klass;

import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IGetSectionInfoConfigDetailsModel extends IModel {
  
  public static final String REFERENCED_CONTEXTS = "referencedContexts";
  
  public Map<String, IConfigEntityInformationModel> getReferencedContexts();
  
  public void setReferencedContexts(Map<String, IConfigEntityInformationModel> referencedContexts);
}
