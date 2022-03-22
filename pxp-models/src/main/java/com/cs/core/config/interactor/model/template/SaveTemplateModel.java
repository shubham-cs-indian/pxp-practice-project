package com.cs.core.config.interactor.model.template;

import com.cs.core.config.interactor.entity.template.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class SaveTemplateModel implements ISaveTemplateModel {
  
  private static final long        serialVersionUID           = 1L;
  
  protected ITemplate              entity                     = new Template();
  protected List<ITemplateTab>     modifiedTemplateTabs       = new ArrayList<>();
  protected List<String>           addedPropertyCollections   = new ArrayList<>();
  protected List<String>           deletedPropertyCollections = new ArrayList<>();
  protected List<String>           addedRelationships         = new ArrayList<>();
  protected List<String>           deletedRelationships       = new ArrayList<>();
  protected IModifiedSequenceModel modifiedPropertyCollection;
  protected IModifiedSequenceModel modifiedRelationship;
  
  @Override
  public String getCode()
  {
    return entity.getCode();
  }
  
  @Override
  public void setCode(String code)
  {
    entity.setCode(code);
  }
  
  @Override
  public String getLabel()
  {
    return entity.getLabel();
  }
  
  @Override
  public void setLabel(String label)
  {
    entity.setLabel(label);
  }
  
  @Override
  public String getIcon()
  {
    return entity.getIcon();
  }
  
  @Override
  public void setIcon(String icon)
  {
    entity.setIcon(icon);
  }
  
  @Override
  public String getIconKey()
  {
    return entity.getIconKey();
  }
  
  @Override
  public void setIconKey(String iconKey)
  {
    entity.setIconKey(iconKey);
  }
  
  @Override
  public String getId()
  {
    return entity.getId();
  }
  
  @Override
  public void setId(String id)
  {
    entity.setId(id);
  }
  
  @Override
  public String getType()
  {
    return entity.getType();
  }
  
  @Override
  public void setType(String type)
  {
    entity.setType(type);
  }
  
  @Override
  public ITemplateHeaderVisibility getHeaderVisibility()
  {
    return entity.getHeaderVisibility();
  }
  
  @JsonDeserialize(as = TemplateHeaderVisibility.class)
  @Override
  public void setHeaderVisibility(ITemplateHeaderVisibility headerVisibility)
  {
    entity.setHeaderVisibility(headerVisibility);
  }
  
  @Override
  public List<ITemplateTab> getModifiedTemplateTabs()
  {
    return modifiedTemplateTabs;
  }
  
  @JsonDeserialize(contentAs = AbstractTemplateTab.class)
  @Override
  public void setModifiedTemplateTabs(List<ITemplateTab> modifiedTemplateTabs)
  {
    this.modifiedTemplateTabs = modifiedTemplateTabs;
  }
  
  @Override
  public List<String> getAddedPropertyCollections()
  {
    return addedPropertyCollections;
  }
  
  @Override
  public void setAddedPropertyCollections(List<String> addedPropertyCollections)
  {
    this.addedPropertyCollections = addedPropertyCollections;
  }
  
  @Override
  public List<String> getDeletedPropertyCollections()
  {
    return deletedPropertyCollections;
  }
  
  @Override
  public void setDeletedPropertyCollections(List<String> deletedPropertyCollections)
  {
    this.deletedPropertyCollections = deletedPropertyCollections;
  }
  
  @Override
  public List<String> getAddedRelationships()
  {
    return addedRelationships;
  }
  
  @Override
  public void setAddedRelationships(List<String> addedRelationships)
  {
    this.addedRelationships = addedRelationships;
  }
  
  @Override
  public List<String> getDeletedRelationships()
  {
    return deletedRelationships;
  }
  
  @Override
  public void setDeletedRelationships(List<String> deletedRelationships)
  {
    this.deletedRelationships = deletedRelationships;
  }
  
  @Override
  public IModifiedSequenceModel getModifiedPropertyCollection()
  {
    return modifiedPropertyCollection;
  }
  
  @JsonDeserialize(as = ModifiedSequenceModel.class)
  @Override
  public void setModifiedPropertyCollection(IModifiedSequenceModel modifiedPropertyCollection)
  {
    this.modifiedPropertyCollection = modifiedPropertyCollection;
  }
  
  @Override
  public IModifiedSequenceModel getModifiedRelationship()
  {
    return modifiedRelationship;
  }
  
  @JsonDeserialize(as = ModifiedSequenceModel.class)
  @Override
  public void setModifiedRelationship(IModifiedSequenceModel modifiedRelationship)
  {
    this.modifiedRelationship = modifiedRelationship;
  }
  
  @Override
  public List<String> getContextIds()
  {
    return entity.getContextIds();
  }
  
  @Override
  public void setContextIds(List<String> contextIds)
  {
    entity.setContextIds(contextIds);
  }
  
  /**
   * ******************* ignored properties *************************
   */
  @JsonIgnore
  @Override
  public Long getVersionId()
  {
    return entity.getVersionId();
  }
  
  @JsonIgnore
  @Override
  public void setVersionId(Long versionId)
  {
    entity.setVersionId(versionId);
  }
  
  @JsonIgnore
  @Override
  public Long getVersionTimestamp()
  {
    return entity.getVersionTimestamp();
  }
  
  @JsonIgnore
  @Override
  public void setVersionTimestamp(Long versionTimestamp)
  {
    entity.setVersionTimestamp(versionTimestamp);
  }
  
  @JsonIgnore
  @Override
  public String getLastModifiedBy()
  {
    return entity.getLastModifiedBy();
  }
  
  @JsonIgnore
  @Override
  public void setLastModifiedBy(String lastModifiedBy)
  {
    entity.setLastModifiedBy(lastModifiedBy);
  }
  
  /**
   * ******************* ignored properties *********************
   */
  @JsonIgnore
  @Override
  public List<ITemplateTab> getTabs()
  {
    return entity.getTabs();
  }
  
  @JsonIgnore
  @Override
  public void setTabs(List<ITemplateTab> tabs)
  {
    entity.setTabs(tabs);
  }
}
