package com.cs.core.runtime.interactor.exception.promotioninstance;


import com.cs.core.config.interactor.model.configdetails.ConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.promotioninstance.IPromotionContextModel;
import java.util.ArrayList;
import java.util.List;

public class PromotionContextModel extends ConfigEntityInformationModel
    implements IPromotionContextModel {
  
  private static final long serialVersionUID = 1L;
  protected List<String>    contextTagIds;
  protected List<String>    entities;
  
  @Override
  public List<String> getContextTagIds()
  {
    if (contextTagIds == null) {
      contextTagIds = new ArrayList<String>();
    }
    return contextTagIds;
  }
  
  @Override
  public void setContextTagIds(List<String> contextTagIds)
  {
    this.contextTagIds = contextTagIds;
  }
  
  @Override
  public List<String> getEntities()
  {
    if (entities == null) {
      entities = new ArrayList<String>();
    }
    return entities;
  }
  
  @Override
  public void setEntities(List<String> entities)
  {
    this.entities = entities;
  }
}
