package com.cs.core.runtime.interactor.model.bulkpropagation;

import com.cs.core.config.interactor.model.klass.IContentsDeleteFromRelationshipDataPreparationModel;

import java.util.ArrayList;
import java.util.List;

public class ContentsDeleteFromRelationshipDataPreparationModel
    implements IContentsDeleteFromRelationshipDataPreparationModel {
  
  private static final long serialVersionUID = 1L;
  protected String          sourceContentId;
  protected String          relationshipId;
  protected String          baseType;
  protected List<String>    deletedElements;
  protected List<String>    propertyIds;
  
  public List<String> getPropertyIds()
  {
    return propertyIds;
  }
  
  public void setPropertyIds(List<String> propertyIds)
  {
    this.propertyIds = propertyIds;
  }
  
  @Override
  public String getSourceContentId()
  {
    return sourceContentId;
  }
  
  @Override
  public void setSourceContentId(String sourceContentId)
  {
    this.sourceContentId = sourceContentId;
  }
  
  @Override
  public String getRelationshipId()
  {
    return relationshipId;
  }
  
  @Override
  public void setRelationshipId(String relationshipId)
  {
    this.relationshipId = relationshipId;
  }
  
  @Override
  public String getBaseType()
  {
    return baseType;
  }
  
  @Override
  public void setBaseType(String baseType)
  {
    this.baseType = baseType;
  }
  
  @Override
  public List<String> getDeletedElements()
  {
    if (deletedElements == null) {
      deletedElements = new ArrayList<>();
    }
    return deletedElements;
  }
  
  @Override
  public void setDeletedElements(List<String> deletedElements)
  {
    this.deletedElements = deletedElements;
  }
}
