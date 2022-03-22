package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.klass.IKlassRelationshipSide;
import com.cs.core.config.interactor.entity.klass.KlassRelationshipSide;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ReferencedSectionRelationshipModel extends AbstractReferencedSectionElementModel
    implements IReferencedSectionRelationshipModel {
  
  private static final long        serialVersionUID = 1L;
  
  protected IKlassRelationshipSide klassRelationshipSide;
  protected String                 defaultValue     = "";
  protected Boolean                isLinked;
  protected Boolean                canAdd           = true;
  protected Boolean                canDelete        = true;
  protected Boolean                isNature;
  protected String                 side     = "";

  
  @Override
  public String getDefaultValue()
  {
    return defaultValue;
  }
  
  @Override
  public void setDefaultValue(String defaultValue)
  {
    this.defaultValue = defaultValue;
  }
  
  
  
  @Override
  public IKlassRelationshipSide getRelationshipSide()
  {
    return klassRelationshipSide;
  }
  
  @Override
  @JsonDeserialize(as = KlassRelationshipSide.class)
  public void setRelationshipSide(IKlassRelationshipSide relationshipSide)
  {
    this.klassRelationshipSide = relationshipSide;
  }
  
  /**
   * ************************* ignored properties
   * ********************************
   */
  @JsonIgnore
  @Override
  public IRelationship getRelationship()
  {
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setRelationship(IRelationship relationship)
  {
  }
  
  @Override
  public Boolean getIsLinked()
  {
    return isLinked;
  }
  
  @Override
  public void setIsLinked(Boolean isLinked)
  {
    this.isLinked = isLinked;
  }
  
  @Override
  public Boolean getCanAdd()
  {
    return canAdd;
  }
  
  @Override
  public void setCanAdd(Boolean canAdd)
  {
    this.canAdd = canAdd;
  }
  
  @Override
  public Boolean getCanDelete()
  {
    return canDelete;
  }
  
  @Override
  public void setCanDelete(Boolean canDelete)
  {
    this.canDelete = canDelete;
  }
  
  @Override
  public Boolean getIsNature()
  {
    return isNature;
  }
  
  @Override
  public void setIsNature(Boolean isNature)
  {
    this.isNature = isNature;
  }
  
  @Override
  public String getSide()
  {
    return side;
  }
  
  @Override
  public void setSide(String side)
  {
    this.side = side;
  }
}
