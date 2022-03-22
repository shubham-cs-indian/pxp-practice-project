package com.cs.core.runtime.interactor.model.promotioninstance;

import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;

import java.util.List;

public interface IPromotionContextModel extends IConfigEntityInformationModel {
  
  public static final String CONTEXT_TAGS_IDS = "contextTagIds";
  public static final String ENTITIES         = "entities";
  
  public List<String> getContextTagIds();
  
  public void setContextTagIds(List<String> contextTagIds);
  
  public List<String> getEntities();
  
  public void setEntities(List<String> entities);
}
