package com.cs.core.runtime.interactor.model.offboarding;

import java.util.List;

public interface ICustomExportComponentRequestModel extends ICustomExportComponentConfigModel {
  
  public static String KLASS_INSTANCE_IDS = "klassInstanceIds";
  
  public List<String> getKlassInstanceIds();
  
  public void setKlassInstanceIds(List<String> klassInstanceIds);
}
