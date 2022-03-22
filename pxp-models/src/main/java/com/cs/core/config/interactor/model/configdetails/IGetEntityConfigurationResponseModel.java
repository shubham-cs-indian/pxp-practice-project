package com.cs.core.config.interactor.model.configdetails;

import java.util.List;
import java.util.Map;

import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetEntityConfigurationResponseModel extends IModel {
  
  public static final String Config_Details       = "configDetails";
  public static final String REFERENCE_DATA       = "referenceData";
  public static final String USAGE_SUMMARY        = "usageSummary";
  public static final String HAS_CHILD_DEPENDENCY = "hasChildDependency";
  
  public Map<String, Object> getConfigDetails();
  
  public void setConfigDetails(Map<String, Object> configDetails);
  
  public Map<String, IIdLabelCodeModel> getReferenceData();
  
  public void setReferenceData(Map<String, IIdLabelCodeModel> referenceData);
  
  public List<IUsageSummary> getUsageSummary();
  
  public void setUsageSummary(List<IUsageSummary> usageSummary);
  
  public boolean isHasChildDependency();
  
  public void setHasChildDependency(boolean hasChildDependency);
}
