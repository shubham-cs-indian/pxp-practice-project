package com.cs.core.config.interactor.model.klass;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.config.interactor.model.propertycollection.AddedTabModel;
import com.cs.core.config.interactor.model.propertycollection.IAddedTabModel;
import com.cs.core.config.interactor.model.relationship.IModifiedRelationshipPropertyModel;
import com.cs.core.config.interactor.model.relationship.ModifiedRelationshipPropertyModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class ModifiedNatureRelationshipModel implements IModifiedNatureRelationshipModel {
  
  private static final long                          serialVersionUID            = 1L;
  protected String                                   id;
  protected String                                   label;
  protected int                                      maxNoOfItems;
  protected String                                   relationshipType;
  protected String                                   addedPropertyCollection;
  protected String                                   deletedPropertyCollection;
  protected List<IModifiedRelationshipPropertyModel> addedAttributes             = new ArrayList<>();
  protected List<IModifiedRelationshipPropertyModel> modifiedAttributes          = new ArrayList<>();
  protected List<IModifiedRelationshipPropertyModel> deletedAttributes           = new ArrayList<>();
  protected List<IModifiedRelationshipPropertyModel> addedTags                   = new ArrayList<>();
  protected List<IModifiedRelationshipPropertyModel> modifiedTags                = new ArrayList<>();
  protected List<IModifiedRelationshipPropertyModel> deletedTags                 = new ArrayList<>();
  protected List<String>                             addedContextTags            = new ArrayList<>();
  protected List<String>                             deletedContextTags          = new ArrayList<>();
  protected String                                   rhythm;
  protected Boolean                                  autoCreateSettings;
  protected IAddedTabModel                           addedTab;
  protected String                                   deletedTab;
  protected List<IModifiedRelationshipPropertyModel> addedRelationshipInheritance;
  protected List<IModifiedRelationshipPropertyModel> modifiedRelationshipInheritance;
  protected List<String>                             deletedRelationshipInheritance;
  protected String                                   taxonomyInheritanceSetting;
  protected Boolean                                  enableAfterSave             = false;
  
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
  public String getAddedPropertyCollection()
  {
    
    return addedPropertyCollection;
  }
  
  @Override
  public void setAddedPropertyCollection(String addedPropertyCollection)
  {
    this.addedPropertyCollection = addedPropertyCollection;
  }
  
  @Override
  public String getDeletedPropertyCollection()
  {
    
    return deletedPropertyCollection;
  }
  
  @Override
  public void setDeletedPropertyCollection(String deletedPropertyCollection)
  {
    this.deletedPropertyCollection = deletedPropertyCollection;
  }
  
  @Override
  public int getMaxNoOfItems()
  {
    
    return maxNoOfItems;
  }
  
  @Override
  public void setMaxNoOfItems(int maxNoOfItems)
  {
    this.maxNoOfItems = maxNoOfItems;
  }
  
  @JsonIgnore
  @Override
  public Long getVersionId()
  {
    
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setVersionId(Long versionId)
  {
  }
  
  @JsonIgnore
  @Override
  public Long getVersionTimestamp()
  {
    
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
  }
  
  @JsonIgnore
  @Override
  public String getLastModifiedBy()
  {
    
    return null;
  }
  
  @JsonIgnore
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
  }
  
  @Override
  public String getRelationshipType()
  {
    
    return relationshipType;
  }
  
  @Override
  public void setRelationshipType(String relationshipType)
  {
    this.relationshipType = relationshipType;
  }
  
  @Override
  public List<String> getAddedContextTags()
  {
    return addedContextTags;
  }
  
  @Override
  public void setAddedContextTags(List<String> addedContextTags)
  {
    this.addedContextTags = addedContextTags;
  }
  
  @Override
  public List<String> getDeletedContextTags()
  {
    return deletedContextTags;
  }
  
  @Override
  public void setDeletedContextTags(List<String> deletedContextTags)
  {
    this.deletedContextTags = deletedContextTags;
  }
  
  @Override
  public String getRhythm()
  {
    return rhythm;
  }
  
  @Override
  public void setRhythm(String rhythm)
  {
    this.rhythm = rhythm;
  }
  
  @Override
  public Boolean getAutoCreateSettings()
  {
    return autoCreateSettings;
  }
  
  @Override
  public void setAutoCreateSettings(Boolean autoCreateSettings)
  {
    this.autoCreateSettings = autoCreateSettings;
  }
  
  @Override
  public List<IModifiedRelationshipPropertyModel> getAddedAttributes()
  {
    return addedAttributes;
  }
  
  @JsonDeserialize(contentAs = ModifiedRelationshipPropertyModel.class)
  @Override
  public void setAddedAttributes(List<IModifiedRelationshipPropertyModel> addedAttributes)
  {
    this.addedAttributes = addedAttributes;
  }
  
  @Override
  public List<IModifiedRelationshipPropertyModel> getModifiedAttributes()
  {
    return modifiedAttributes;
  }
  
  @JsonDeserialize(contentAs = ModifiedRelationshipPropertyModel.class)
  @Override
  public void setModifiedAttributes(List<IModifiedRelationshipPropertyModel> modifiedAttributes)
  {
    this.modifiedAttributes = modifiedAttributes;
  }
  
  @Override
  public List<IModifiedRelationshipPropertyModel> getDeletedAttributes()
  {
    return deletedAttributes;
  }
  
  @JsonDeserialize(contentAs = ModifiedRelationshipPropertyModel.class)
  @Override
  public void setDeletedAttributes(List<IModifiedRelationshipPropertyModel> deletedAttributes)
  {
    this.deletedAttributes = deletedAttributes;
  }
  
  @Override
  public List<IModifiedRelationshipPropertyModel> getAddedTags()
  {
    return addedTags;
  }
  
  @JsonDeserialize(contentAs = ModifiedRelationshipPropertyModel.class)
  @Override
  public void setAddedTags(List<IModifiedRelationshipPropertyModel> addedTags)
  {
    this.addedTags = addedTags;
  }
  
  @Override
  public List<IModifiedRelationshipPropertyModel> getModifiedTags()
  {
    return modifiedTags;
  }
  
  @JsonDeserialize(contentAs = ModifiedRelationshipPropertyModel.class)
  @Override
  public void setModifiedTags(List<IModifiedRelationshipPropertyModel> modifiedTags)
  {
    this.modifiedTags = modifiedTags;
  }
  
  @Override
  public List<IModifiedRelationshipPropertyModel> getDeletedTags()
  {
    return deletedTags;
  }
  
  @JsonDeserialize(contentAs = ModifiedRelationshipPropertyModel.class)
  @Override
  public void setDeletedTags(List<IModifiedRelationshipPropertyModel> deletedTags)
  {
    this.deletedTags = deletedTags;
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
  public List<IModifiedRelationshipPropertyModel> getAddedRelationshipInheritance()
  {
    if (addedRelationshipInheritance == null) {
      addedRelationshipInheritance = new ArrayList<>();
    }
    return addedRelationshipInheritance;
  }
  
  @Override
  @JsonDeserialize(contentAs = ModifiedRelationshipPropertyModel.class)
  public void setAddedRelationshipInheritance(
      List<IModifiedRelationshipPropertyModel> addedRelationshipInheritance)
  {
    this.addedRelationshipInheritance = addedRelationshipInheritance;
  }
  
  @Override
  public List<IModifiedRelationshipPropertyModel> getModifiedRelationshipInheritance()
  {
    if (modifiedRelationshipInheritance == null) {
      modifiedRelationshipInheritance = new ArrayList<>();
    }
    return modifiedRelationshipInheritance;
  }
  
  @Override
  @JsonDeserialize(contentAs = ModifiedRelationshipPropertyModel.class)
  public void setModifiedRelationshipInheritance(
      List<IModifiedRelationshipPropertyModel> modifiedRelationshipInheritance)
  {
    this.modifiedRelationshipInheritance = modifiedRelationshipInheritance;
  }
  
  @Override
  public List<String> getDeletedRelationshipInheritance()
  {
    if (deletedRelationshipInheritance == null) {
      deletedRelationshipInheritance = new ArrayList<>();
    }
    return deletedRelationshipInheritance;
  }
  
  @Override
  public void setDeletedRelationshipInheritance(List<String> deletedRelationshipInheritance)
  {
    this.deletedRelationshipInheritance = deletedRelationshipInheritance;
  }

  @Override
  public String getTaxonomyInheritanceSetting()
  {
    return taxonomyInheritanceSetting;
  }

  @Override
  public void setTaxonomyInheritanceSetting(String taxonomyInheritanceSetting)
  {
    this.taxonomyInheritanceSetting = taxonomyInheritanceSetting;
  }
  
  public Boolean getEnableAfterSave()
  {
    return enableAfterSave;
  }

  @Override
  public void setEnableAfterSave(Boolean enableAfterSave)
  {
    this.enableAfterSave = enableAfterSave;
  }
}
