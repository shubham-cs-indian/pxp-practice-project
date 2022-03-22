package com.cs.core.runtime.interactor.model.configuration;

import java.util.List;

public interface IMigrationResponseModel extends IModel {
  
  public static String IDS                         = "ids";
  public static String TECHNICAL_VARIANT_KLASS_IDS = "technicalVariantKlassIds";
  
  public List<String> getIds();
  
  public void setIds(List<String> ids);
  
  public List<String> getTechnicalVariantKlassIds();
  
  public void setTechnicalVariantKlassIds(List<String> technicalVariantKlassIds);
}
