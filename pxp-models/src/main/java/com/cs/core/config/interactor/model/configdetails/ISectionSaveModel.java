package com.cs.core.config.interactor.model.configdetails;

import java.util.List;

import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.cs.core.config.interactor.entity.propertycollection.ISectionElement;

public interface ISectionSaveModel extends ISection, IConfigModel {
  
  public List<? extends ISectionElement> getAddedElements();
  
  public void setAddedElements(List<? extends ISectionElement> addedElements);
  
  public List<? extends ISectionElement> getModifiedElements();
  
  public void setModifiedElements(List<? extends ISectionElement> updatedElements);
  
  public List<String> getDeletedElements();
  
  public void setDeletedElements(List<String> deletedElementIds);
}
