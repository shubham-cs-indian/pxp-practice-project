package com.cs.core.runtime.interactor.model.collections;

import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.cs.core.config.interactor.model.configdetails.IDeletedSectionElementModel;
import com.cs.core.config.interactor.model.configdetails.IModifiedSectionModel;
import com.cs.core.config.interactor.model.klass.IAddedSectionElementModel;
import com.cs.core.config.interactor.model.klass.IModifiedSectionElementModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ISaveStaticCollectionDetailsModel extends IModel {
  
  public static final String ID                = "id";
  public static final String LABEL             = "label";
  public static final String MODIFIED_ELEMENTS = "modifiedElements";
  public static final String ADDED_SECTIONS    = "addedSections";
  public static final String DELETED_SECTIONS  = "deletedSections";
  public static final String MODIFIED_SECTIONS = "modifiedSections";
  public static final String DELETED_ELEMENTS  = "deletedElements";
  public static final String ADDED_ELEMENTS    = "addedElements";
  
  public String getId();
  
  public void setId(String id);
  
  public String getLabel();
  
  public void setLabel(String label);
  
  public List<? extends ISection> getAddedSections();
  
  public void setAddedSections(List<? extends ISection> addedSections);
  
  public List<String> getDeletedSections();
  
  public void setDeletedSections(List<String> deletedSectionIds);
  
  public List<? extends IModifiedSectionElementModel> getModifiedElements();
  
  public void setModifiedElements(List<? extends IModifiedSectionElementModel> updatedElements);
  
  public List<IModifiedSectionModel> getModifiedSections();
  
  public void setModifiedSections(List<IModifiedSectionModel> modifiedSections);
  
  public List<IDeletedSectionElementModel> getDeletedElements();
  
  public void setDeletedElements(List<IDeletedSectionElementModel> deletedElements);
  
  public List<IAddedSectionElementModel> getAddedElements();
  
  public void setAddedElements(List<IAddedSectionElementModel> addedElements);
}
