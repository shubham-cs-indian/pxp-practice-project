package com.cs.core.config.interactor.model.relationship;

import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.cs.core.config.interactor.model.configdetails.IDeletedSectionElementModel;
import com.cs.core.config.interactor.model.configdetails.IModifiedSectionModel;
import com.cs.core.config.interactor.model.klass.IAddedSectionElementModel;
import com.cs.core.config.interactor.model.klass.IModifiedSectionElementModel;
import com.cs.core.config.interactor.model.propertycollection.IAddedTabModel;

import java.util.List;

public interface ISaveRelationshipModel extends IRelationshipModel {
  
  public static final String MODIFIED_ELEMENTS             = "modifiedElements";
  public static final String ADDED_SECTIONS                = "addedSections";
  public static final String DELETED_SECTIONS              = "deletedSections";
  public static final String MODIFIED_SECTIONS             = "modifiedSections";
  public static final String DELETED_ELEMENTS              = "deletedElements";
  public static final String ADDED_ELEMENTS                = "addedElements";
  
  public static final String ADDED_ATTRIBUTES              = "addedAttributes";
  public static final String MODIFIED_ATTRIBUTES           = "modifiedAttributes";
  public static final String DELETED_ATTRIBUTES            = "deletedAttributes";
  public static final String ADDED_TAGS                    = "addedTags";
  public static final String MODIFIED_TAGS                 = "modifiedTags";
  public static final String DELETED_TAGS                  = "deletedTags";
  
  public static final String ADDED_TAB                     = "addedTab";
  public static final String DELETED_TAB                   = "deletedTab";
  
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
  
  public List<IModifiedRelationshipPropertyModel> getAddedAttributes();
  
  public void setAddedAttributes(List<IModifiedRelationshipPropertyModel> addedAttributes);
  
  public List<IModifiedRelationshipPropertyModel> getModifiedAttributes();
  
  public void setModifiedAttributes(List<IModifiedRelationshipPropertyModel> modifiedAttributes);
  
  public List<IModifiedRelationshipPropertyModel> getDeletedAttributes();
  
  public void setDeletedAttributes(List<IModifiedRelationshipPropertyModel> deletedAttributes);
  
  public List<IModifiedRelationshipPropertyModel> getAddedTags();
  
  public void setAddedTags(List<IModifiedRelationshipPropertyModel> addedTags);
  
  public List<IModifiedRelationshipPropertyModel> getModifiedTags();
  
  public void setModifiedTags(List<IModifiedRelationshipPropertyModel> modifiedTags);
  
  public List<IModifiedRelationshipPropertyModel> getDeletedTags();
  
  public void setDeletedTags(List<IModifiedRelationshipPropertyModel> deletedTags);
  
  public IRelationshipSide getSide1();
  
  public void setSide1(IRelationshipSide side1);
  
  public IRelationshipSide getSide2();
  
  public void setSide2(IRelationshipSide side2);
  
  public IAddedTabModel getAddedTab();
  
  public void setAddedTab(IAddedTabModel addedTab);
  
  public String getDeletedTab();
  
  public void setDeletedTab(String deletedTab);
}
