package com.cs.core.config.interactor.model.klass;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface IGetSectionInfoForRelationshipExportModel extends IModel {
  
  public static final String KLASS_ID    = "klassId";
  public static final String ENTITY_TYPE = "entityType";
  
  public String getKlassId();
  public void setKlassId(String klassId);
  
  public String getEntityType();
  public void setEntityType(String entityType);
}