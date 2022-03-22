package com.cs.core.config.interactor.model.variantcontext;

import com.cs.core.config.interactor.entity.matchandmerge.AbstractSection;
import com.cs.core.config.interactor.entity.propertycollection.ISection;
import com.cs.core.config.interactor.entity.variantcontext.DefaultTimeRange;
import com.cs.core.config.interactor.entity.variantcontext.IDefaultTimeRange;
import com.cs.core.config.interactor.model.configdetails.DeletedSectionElementModel;
import com.cs.core.config.interactor.model.configdetails.IDeletedSectionElementModel;
import com.cs.core.config.interactor.model.configdetails.IModifiedSectionModel;
import com.cs.core.config.interactor.model.configdetails.ModifiedSectionModel;
import com.cs.core.config.interactor.model.klass.AbstractModifiedSectionElementModel;
import com.cs.core.config.interactor.model.klass.AddedSectionElementModel;
import com.cs.core.config.interactor.model.klass.IAddedSectionElementModel;
import com.cs.core.config.interactor.model.klass.IModifiedSectionElementModel;
import com.cs.core.config.interactor.model.propertycollection.AddedTabModel;
import com.cs.core.config.interactor.model.propertycollection.IAddedTabModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class SaveVariantContextModel implements ISaveVariantContextModel {
  
  private static final long                              serialVersionUID          = 1L;
  protected String                                       id;
  protected String                                       label;
  protected String                                       type;
  protected String                                       icon;
  protected String                                       iconKey;
  protected Boolean                                      isAutoCreate;
  protected Boolean                                      isStandard                = false;
  protected List<IAddedVariantContextTagsModel>          addedTags;
  protected List<IVariantContextModifiedTagsModel>       modifiedTags;
  protected List<String>                                 deletedTags;
  protected List<String>                                 addedStatusTypeTags;
  protected List<String>                                 deletedStatusTypeTags;
  protected List<String>                                 addedSubContexts;
  protected List<String>                                 deletedSubContexts;
  protected List<? extends IModifiedSectionElementModel> modifiedElements          = new ArrayList<>();
  protected List<IModifiedSectionModel>                  modifiedSections          = new ArrayList<>();
  protected List<IDeletedSectionElementModel>            deletedElements           = new ArrayList<>();
  protected List<? extends ISection>                     addedSections             = new ArrayList<>();
  protected List<IAddedSectionElementModel>              addedElements             = new ArrayList<>();
  protected List<String>                                 deletedSections           = new ArrayList<>();
  protected List<String>                                 entities;
  protected List<String>                                 addedEntities;
  protected List<String>                                 deletedEntities;
  protected Boolean                                      isTimeEnabled             = false;
  protected View                                         defaultView;
  protected Boolean                                      isDuplicateVariantAllowed = false;
  protected List<IUniqueSelectorModel>                   addedUniqueSelections     = new ArrayList<>();
  protected List<String>                                 deletedUniqueSelections   = new ArrayList<>();
  protected IDefaultTimeRange                            defaultTimeRange;
  protected String                                       code;
  protected IAddedTabModel                               addedTab;
  protected String                                       deletedTab;
  protected long                                         contextIID;
  
  @Override
  public String getCode()
  {
    return code;
  }
  
  @Override
  public void setCode(String code)
  {
    this.code = code;
  }
  
  @Override
  public List<String> getEntities()
  {
    if (entities == null) {
      entities = new ArrayList<>();
    }
    return entities;
  }
  
  @Override
  public void setEntities(List<String> entities)
  {
    this.entities = entities;
  }
  
  @Override
  public String getId()
  {
    
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public List<IAddedVariantContextTagsModel> getAddedTags()
  {
    
    return addedTags;
  }
  
  @Override
  @JsonDeserialize(contentAs = AddedVariantContextTagsModel.class)
  public void setAddedTags(List<IAddedVariantContextTagsModel> addedTags)
  {
    this.addedTags = addedTags;
  }
  
  @Override
  public List<IVariantContextModifiedTagsModel> getModifiedTags()
  {
    
    return modifiedTags;
  }
  
  @Override
  @JsonDeserialize(contentAs = VariantContextModifiedTagsModel.class)
  public void setModifiedTags(List<IVariantContextModifiedTagsModel> modifiedTags)
  {
    this.modifiedTags = modifiedTags;
  }
  
  @Override
  public List<String> getDeletedTags()
  {
    
    return deletedTags;
  }
  
  @Override
  public void setDeletedTags(List<String> deletedTags)
  {
    this.deletedTags = deletedTags;
  }
  
  @Override
  public String getLabel()
  {
    
    return label;
  }
  
  @Override
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  @Override
  public String getDescription()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setDescription(String description)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getTooltip()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setTooltip(String tooltip)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Boolean getIsMandatory()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setIsMandatory(Boolean isMandatory)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Boolean getIsStandard()
  {
    return isStandard;
  }
  
  @Override
  public void setIsStandard(Boolean isStandard)
  {
    this.isStandard = isStandard;
  }
  
  @Override
  public String getPlaceholder()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setPlaceholder(String placeholder)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getIcon()
  {
    
    return icon;
  }
  
  @Override
  public void setIcon(String icon)
  {
    this.icon = icon;
  }
  
  @Override
  public String getIconKey()
  {
    
    return iconKey;
  }
  
  @Override
  public void setIconKey(String iconKey)
  {
    this.iconKey = iconKey;
  }
  
  @Override
  public String getType()
  {
    
    return type;
  }
  
  @Override
  public void setType(String type)
  {
    this.type = type;
  }
  
  @Override
  public Boolean getIsAutoCreate()
  {
    return isAutoCreate;
  }
  
  @Override
  public void setIsAutoCreate(Boolean isAutoCreate)
  {
    this.isAutoCreate = isAutoCreate;
  }
  
  @Override
  public Long getVersionId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionId(Long versionId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public Long getVersionTimestamp()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public String getLastModifiedBy()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public List<? extends ISection> getAddedSections()
  {
    return this.addedSections;
  }
  
  @JsonDeserialize(contentAs = AbstractSection.class)
  @Override
  public void setAddedSections(List<? extends ISection> addedSections)
  {
    this.addedSections = addedSections;
  }
  
  @Override
  public List<String> getDeletedSections()
  {
    return this.deletedSections;
  }
  
  @Override
  public void setDeletedSections(List<String> deletedSectionIds)
  {
    this.deletedSections = deletedSectionIds;
  }
  
  @Override
  public List<? extends IModifiedSectionElementModel> getModifiedElements()
  {
    return modifiedElements;
  }
  
  @JsonDeserialize(contentAs = AbstractModifiedSectionElementModel.class)
  @Override
  public void setModifiedElements(List<? extends IModifiedSectionElementModel> modifiedElements)
  {
    this.modifiedElements = modifiedElements;
  }
  
  @Override
  public List<IModifiedSectionModel> getModifiedSections()
  {
    if (modifiedSections == null) {
      modifiedSections = new ArrayList<>();
    }
    return modifiedSections;
  }
  
  @JsonDeserialize(contentAs = ModifiedSectionModel.class)
  @Override
  public void setModifiedSections(List<IModifiedSectionModel> modifiedSections)
  {
    this.modifiedSections = modifiedSections;
  }
  
  @Override
  public List<IDeletedSectionElementModel> getDeletedElements()
  {
    if (deletedElements == null) {
      return new ArrayList<>();
    }
    return deletedElements;
  }
  
  @JsonDeserialize(contentAs = DeletedSectionElementModel.class)
  @Override
  public void setDeletedElements(List<IDeletedSectionElementModel> deletedElements)
  {
    this.deletedElements = deletedElements;
  }
  
  @Override
  public List<IAddedSectionElementModel> getAddedElements()
  {
    if (addedElements == null) {
      addedElements = new ArrayList<>();
    }
    return addedElements;
  }
  
  @JsonDeserialize(contentAs = AddedSectionElementModel.class)
  @Override
  public void setAddedElements(List<IAddedSectionElementModel> addedElements)
  {
    this.addedElements = addedElements;
  }
  
  @Override
  public List<String> getAddedStatusTags()
  {
    if (addedStatusTypeTags == null) {
      addedStatusTypeTags = new ArrayList<>();
    }
    return addedStatusTypeTags;
  }
  
  @Override
  public void setAddedStatusTags(List<String> addedStatusTypeTags)
  {
    this.addedStatusTypeTags = addedStatusTypeTags;
  }
  
  @Override
  public List<String> getDeletedStatusTags()
  {
    if (deletedStatusTypeTags == null) {
      deletedStatusTypeTags = new ArrayList<>();
    }
    return deletedStatusTypeTags;
  }
  
  @Override
  public void setDeletedStatusTags(List<String> deletedStatusTypeTags)
  {
    this.deletedStatusTypeTags = deletedStatusTypeTags;
  }
  
  @Override
  public List<String> getAddedSubContexts()
  {
    if (addedSubContexts == null) {
      return new ArrayList<String>();
    }
    return addedSubContexts;
  }
  
  @Override
  public void setAddedSubContexts(List<String> addedSubContexts)
  {
    this.addedSubContexts = addedSubContexts;
  }
  
  @Override
  public List<String> getDeletedSubContexts()
  {
    if (deletedSubContexts == null) {
      return new ArrayList<String>();
    }
    return deletedSubContexts;
  }
  
  @Override
  public void setDeletedSubContexts(List<String> deletedSubContexts)
  {
    this.deletedSubContexts = deletedSubContexts;
  }
  
  @Override
  public List<String> getAddedEntities()
  {
    if (addedEntities == null) {
      return new ArrayList<String>();
    }
    return addedEntities;
  }
  
  @Override
  public void setAddedEntities(List<String> addedEntities)
  {
    this.addedEntities = addedEntities;
  }
  
  @Override
  public List<String> getDeletedEntities()
  {
    if (deletedEntities == null) {
      return new ArrayList<String>();
    }
    return deletedEntities;
  }
  
  @Override
  public void setDeletedEntities(List<String> deletedEntities)
  {
    this.deletedEntities = deletedEntities;
  }
  
  @Override
  public Boolean getIsTimeEnabled()
  {
    return isTimeEnabled;
  }
  
  @Override
  public void setIsTimeEnabled(Boolean isTimeEnabled)
  {
    this.isTimeEnabled = isTimeEnabled;
  }
  
  @Override
  public View getDefaultView()
  {
    return defaultView;
  }
  
  @Override
  public void setDefaultView(View defaultView)
  {
    this.defaultView = defaultView;
  }
  
  @Override
  public Boolean getIsDuplicateVariantAllowed()
  {
    return isDuplicateVariantAllowed;
  }
  
  @Override
  public void setIsDuplicateVariantAllowed(Boolean isDuplicateVariantAllowed)
  {
    this.isDuplicateVariantAllowed = isDuplicateVariantAllowed;
  }
  
  @Override
  public List<IUniqueSelectorModel> getAddedUniqueSelections()
  {
    return addedUniqueSelections;
  }
  
  @JsonDeserialize(contentAs = UniqueSelectorModel.class)
  @Override
  public void setAddedUniqueSelections(List<IUniqueSelectorModel> addedUniqueSelections)
  {
    this.addedUniqueSelections = addedUniqueSelections;
  }
  
  @Override
  public List<String> getDeletedUniqueSelections()
  {
    return deletedUniqueSelections;
  }
  
  @Override
  public void setDeletedUniqueSelections(List<String> deletedUniqueSelections)
  {
    this.deletedUniqueSelections = deletedUniqueSelections;
  }
  
  @Override
  public IDefaultTimeRange getDefaultTimeRange()
  {
    return defaultTimeRange;
  }
  
  @JsonDeserialize(as = DefaultTimeRange.class)
  @Override
  public void setDefaultTimeRange(IDefaultTimeRange defaultTimeRange)
  {
    this.defaultTimeRange = defaultTimeRange;
  }
  
  @Override
  public IAddedTabModel getAddedTab()
  {
    return addedTab;
  }
  
  @Override
  @JsonDeserialize(as = AddedTabModel.class)
  public void setAddedTab(IAddedTabModel addedTab)
  {
    this.addedTab = addedTab;
  }
  
  @Override
  public String getDeletedTab()
  {
    return deletedTab;
  }
  
  @Override
  public void setDeletedTab(String deletedTab)
  {
    this.deletedTab = deletedTab;
  }
  
  @JsonIgnore
  @Override
  public String getTabId()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setTabId(String tabId)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public long getContextIID()
  {
    return contextIID;
  }
  
  @Override
  public void setContextIID(long contextIID)
  {
    this.contextIID = contextIID;
  }
}
