package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.propertycollection.ISectionRelationship;

public interface IReferencedSectionRelationshipModel
    extends IReferencedSectionElementModel, ISectionRelationship {
  
  public static final String IS_LINKED  = "isLinked";
  public static final String CAN_ADD    = "canAdd";
  public static final String CAN_DELETE = "canDelete";
  public static final String SIDE       = "side";
  
  public String getSide();
  
  public void setSide(String side);
  
  public Boolean getIsLinked();
  
  public void setIsLinked(Boolean isLinked);
  
  public Boolean getCanAdd();
  
  public void setCanAdd(Boolean canAdd);
  
  public Boolean getCanDelete();
  
  public void setCanDelete(Boolean canDelete);
}
