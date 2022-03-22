package com.cs.core.runtime.interactor.model.variants;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetVariantInfoForDataTransferRequestModel extends IModel {
  
  public static final String KLASS_INSTANCE_ID         = "klassInstanceId";
  public static final String PARENT_VARANT_INSTANCE_ID = "parentVariantInstanceId";
  public static final String BASETYPE                  = "baseType";
  public static final String KLASS_IDS                 = "klassIds";
  
  public String getKlassInstanceId();
  
  public void setKlassInstanceId(String klassInstanceId);
  
  public String getParentVariantInstanceId();
  
  public void setParentVariantInstanceId(String parentVariantInstanceId);
  
  public String getBaseType();
  
  public void setBaseType(String baseType);
  
  public List<String> getKlassIds();
  
  public void setKlassIds(List<String> klassIds);
}
