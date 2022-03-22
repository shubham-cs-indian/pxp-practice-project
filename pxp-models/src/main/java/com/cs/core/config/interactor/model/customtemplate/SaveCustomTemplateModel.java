package com.cs.core.config.interactor.model.customtemplate;

import java.util.ArrayList;
import java.util.List;

public class SaveCustomTemplateModel implements ISaveCustomTemplateModel {
  
  private static final long serialVersionUID = 1L;
  
  protected String          label;
  protected String          icon;
  protected String          iconKey;
  protected String          type;
  protected String          Id;
  protected String          code;
  protected List<String>    addedPropertyCollections;
  protected List<String>    deletedPropertyCollections;
  protected List<String>    addedRelationships;
  protected List<String>    deletedRelationships;
  protected List<String>    addedNatureRelationships;
  protected List<String>    deletedNatureRelationships;
  protected List<String>    addedContexts;
  protected List<String>    deletedContexts;
 
  public String getLabel()
  {
    return label;
  }
  
  public void setLabel(String label)
  {
    this.label = label;
  }
  
  public String getIcon()
  {
    return icon;
  }
  
  public void setIcon(String icon)
  {
    this.icon = icon;
  }
  
  public String getIconKey()
  {
    return iconKey;
  }
  
  public void setIconKey(String iconKey)
  {
    this.iconKey = iconKey;
  }
  
  public String getType()
  {
    return type;
  }
  
  public void setType(String type)
  {
    this.type = type;
  }
  
  public String getId()
  {
    return Id;
  }
  
  public void setId(String id)
  {
    Id = id;
  }
  
  public List<String> getAddedPropertyCollections()
  {
    if (addedPropertyCollections == null) {
      addedPropertyCollections = new ArrayList<String>();
    }
    return addedPropertyCollections;
  }
  
  public void setAddedPropertyCollections(List<String> addedPropertyCollections)
  {
    this.addedPropertyCollections = addedPropertyCollections;
  }
  
  public List<String> getDeletedPropertyCollections()
  {
    if (deletedPropertyCollections == null) {
      deletedPropertyCollections = new ArrayList<String>();
    }
    return deletedPropertyCollections;
  }
  
  public void setDeletedPropertyCollections(List<String> deletedPropertyCollections)
  {
    this.deletedPropertyCollections = deletedPropertyCollections;
  }
  
  public List<String> getAddedRelationships()
  {
    if (addedRelationships == null) {
      addedRelationships = new ArrayList<String>();
    }
    return addedRelationships;
  }
  
  public void setAddedRelationships(List<String> addedRelationships)
  {
    this.addedRelationships = addedRelationships;
  }
  
  public List<String> getDeletedRelationships()
  {
    if (deletedRelationships == null) {
      deletedRelationships = new ArrayList<String>();
    }
    return deletedRelationships;
  }
  
  public void setDeletedRelationships(List<String> deletedRelationships)
  {
    this.deletedRelationships = deletedRelationships;
  }
  
  public List<String> getAddedNatureRelationships()
  {
    if (addedNatureRelationships == null) {
      addedNatureRelationships = new ArrayList<String>();
    }
    return addedNatureRelationships;
  }
  
  public void setAddedNatureRelationships(List<String> addedNatureRelationships)
  {
    this.addedNatureRelationships = addedNatureRelationships;
  }
  
  public List<String> getDeletedNatureRelationships()
  {
    if (deletedNatureRelationships == null) {
      deletedNatureRelationships = new ArrayList<String>();
    }
    return deletedNatureRelationships;
  }
  
  public void setDeletedNatureRelationships(List<String> deletedNatureRelationships)
  {
    this.deletedNatureRelationships = deletedNatureRelationships;
  }
  
  public List<String> getAddedContexts()
  {
    if (addedContexts == null) {
      addedContexts = new ArrayList<String>();
    }
    return addedContexts;
  }
  
  public void setAddedContexts(List<String> addedContexts)
  {
    this.addedContexts = addedContexts;
  }
  
  public List<String> getDeletedContexts()
  {
    if (deletedContexts == null) {
      deletedContexts = new ArrayList<String>();
    }
    return deletedContexts;
  }
  
  public void setDeletedContexts(List<String> deletedContexts)
  {
    this.deletedContexts = deletedContexts;
  }
  
 
  
  /**
   * ************************* igored properties ******************
   */
  @Override
  public List<String> getPropertyCollectionIds()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setPropertyCollectionIds(List<String> propertyCollectionIds)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public List<String> getRelationshipIds()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setRelationshipIds(List<String> relationshipIds)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public List<String> getNatureRelationshipIds()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setNatureRelationshipIds(List<String> natureRelationshipIds)
  {
    // TODO Auto-generated method stub
    
  }
  
  @Override
  public List<String> getContextIds()
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  @Override
  public void setContextIds(List<String> contextIds)
  {
    // TODO Auto-generated method stub
    
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
  
  public String getCode()
  {
    return code;
  }
  
  public void setCode(String code)
  {
    this.code = code;
  }

}
