package com.cs.core.runtime.interactor.model.gridcontent;

import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.runtime.interactor.entity.klassinstance.IContentInstance;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IGridContentInstanceModel extends IModel {
  
  public static final String REFERENCED_ELEMENTS = "referencedElements";
  public static final String KLASS_INSTANCE      = "klassInstance";
  public static final String IS_NAME_EDITABLE    = "isNameEditable";
  public static final String IS_NAME_VISIBLE     = "isNameVisible";
  
  public Map<String, IReferencedSectionElementModel> getReferencedElements();
  
  public void setReferencedElements(Map<String, IReferencedSectionElementModel> referencedElements);
  
  public IContentInstance getKlassInstance();
  
  public void setKlassInstance(IContentInstance klassInstance);
  
  public Boolean getIsNameEditable();
  
  public void setIsNameEditable(Boolean isNameEditable);
  
  public Boolean getIsNameVisible();
  
  public void setIsNameVisible(Boolean isNameVisible);
}
