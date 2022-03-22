package com.cs.core.runtime.interactor.model.gridcontent;

import com.cs.core.config.interactor.model.klass.AbstractReferencedSectionElementModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.runtime.interactor.entity.klassinstance.AbstractContentInstance;
import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.Map;

public class GridContentInstanceModel implements IGridContentInstanceModel {
  
  private static final long                             serialVersionUID = 1L;
  protected Map<String, IReferencedSectionElementModel> referencedElements;
  protected IContentInstance                            klassInstance;
  protected Boolean                                     isNameVisible    = false;
  protected Boolean                                     isNameEditable   = false;
  
  @Override
  public Map<String, IReferencedSectionElementModel> getReferencedElements()
  {
    return referencedElements;
  }
  
  @Override
  @JsonDeserialize(contentAs = AbstractReferencedSectionElementModel.class)
  public void setReferencedElements(Map<String, IReferencedSectionElementModel> referencedElements)
  {
    if (referencedElements == null) {
      referencedElements = new HashMap<>();
    }
    this.referencedElements = referencedElements;
  }
  
  @Override
  public IContentInstance getKlassInstance()
  {
    return klassInstance;
  }
  
  @Override
  @JsonDeserialize(as = AbstractContentInstance.class)
  public void setKlassInstance(IContentInstance klassInstance)
  {
    this.klassInstance = klassInstance;
  }
  
  @Override
  public Boolean getIsNameEditable()
  {
    return isNameEditable;
  }
  
  @Override
  public void setIsNameEditable(Boolean isNameEditable)
  {
    this.isNameEditable = isNameEditable;
  }
  
  @Override
  public Boolean getIsNameVisible()
  {
    return isNameVisible;
  }
  
  @Override
  public void setIsNameVisible(Boolean isNameVisible)
  {
    this.isNameVisible = isNameVisible;
  }
}
