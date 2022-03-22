package com.cs.core.runtime.interactor.model.gridcontent;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetContentForGridRequestModel extends IModel {
  
  public static final String KLASS_INSTANCE_IDS = "klassInstanceIds";
  public static final String MODULE_ID          = "moduleId";
  
  public List<String> getKlassInstanceIds();
  
  public void setKlassInstanceIds(List<String> klassInstanceIds);
  
  public String getModuleId();
  
  public void setModuleId(String moduleId);
}
