package com.cs.core.config.interactor.model.articleimportcomponent;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IKlassInstanceExportAllResponseModel extends IModel {
  
  public static final String KLASS_INSTANCE_IDS = "klassInstanceIds";
  public static final String COUNT              = "count";
  
  public List<String> getKlassInstanceIds();
  
  public void setKlassInstanceIds(List<String> klassInstanceIds);
  
  public Long getCount();
  
  public void setCount(Long count);
}
