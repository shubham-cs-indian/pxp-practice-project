package com.cs.core.config.interactor.model.variantcontext;

import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.cs.core.config.interactor.entity.variantcontext.IDefaultTimeRange;
import com.cs.core.config.interactor.entity.variantcontext.IVariantContext;
import com.cs.core.config.interactor.model.configdetails.IDeletedSectionElementModel;
import com.cs.core.config.interactor.model.configdetails.IModifiedSectionModel;
import com.cs.core.config.interactor.model.klass.IAddedSectionElementModel;
import com.cs.core.config.interactor.model.klass.IModifiedSectionElementModel;
import com.cs.core.config.interactor.model.propertycollection.IAddedTabModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.List;

public interface ISaveVariantContextModel extends IVariantContext, IModel {
  
  public static final String ADDED_TAGS                = "addedTags";
  public static final String MODIFIED_TAGS             = "modifiedTags";
  public static final String DELETED_TAGS              = "deletedTags";
  public static final String ADDED_STATUS_TAGS         = "addedStatusTags";
  public static final String DELETED_STATUS_TAGS       = "deletedStatusTags";
  public static final String MODIFIED_ELEMENTS         = "modifiedElements";
  public static final String ADDED_SECTIONS            = "addedSections";
  public static final String DELETED_SECTIONS          = "deletedSections";
  public static final String MODIFIED_SECTIONS         = "modifiedSections";
  public static final String DELETED_ELEMENTS          = "deletedElements";
  public static final String ADDED_ELEMENTS            = "addedElements";
  public static final String ADDED_SUB_CONTEXTS        = "addedSubContexts";
  public static final String DELETED_SUB_CONTEXTS      = "deletedSubContexts";
  public static final String ADDED_ENTITIES            = "addedEntities";
  public static final String DELETED_ENTITIES          = "deletedEntities";
  public static final String ADDED_UNIQUE_SELECTIONS   = "addedUniqueSelections";
  public static final String DELETED_UNIQUE_SELECTIONS = "deletedUniqueSelections";
  public static final String ADDED_TAB                 = "addedTab";
  public static final String DELETED_TAB               = "deletedTab";
  
  public List<IAddedVariantContextTagsModel> getAddedTags();
  
  public void setAddedTags(List<IAddedVariantContextTagsModel> addedTags);
  
  public List<IVariantContextModifiedTagsModel> getModifiedTags();
  
  public void setModifiedTags(List<IVariantContextModifiedTagsModel> modifiedTags);
  
  public List<String> getDeletedTags();
  
  public void setDeletedTags(List<String> deletedTags);
  
  public List<String> getAddedStatusTags();
  
  public void setAddedStatusTags(List<String> addedStatusTypeTags);
  
  public List<String> getDeletedStatusTags();
  
  public void setDeletedStatusTags(List<String> deletedStatusTypeTags);
  
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
  
  public List<String> getAddedSubContexts();
  
  public void setAddedSubContexts(List<String> addedSubContexts);
  
  public List<String> getDeletedSubContexts();
  
  public void setDeletedSubContexts(List<String> deletedSubContexts);
  
  public List<String> getAddedEntities();
  
  public void setAddedEntities(List<String> addedEntities);
  
  public List<String> getDeletedEntities();
  
  public void setDeletedEntities(List<String> deletedEntities);
  
  public List<IUniqueSelectorModel> getAddedUniqueSelections();
  
  public void setAddedUniqueSelections(List<IUniqueSelectorModel> addedUniqueSelections);
  
  public List<String> getDeletedUniqueSelections();
  
  public void setDeletedUniqueSelections(List<String> deletedUniqueSelections);
  
  public IDefaultTimeRange getDefaultTimeRange();
  
  public void setDefaultTimeRange(IDefaultTimeRange defaultTimeRange);
  
  public IAddedTabModel getAddedTab();
  
  public void setAddedTab(IAddedTabModel addedTab);
  
  public String getDeletedTab();
  
  public void setDeletedTab(String deletedTab);
}
