package com.cs.core.config.interactor.model.attributiontaxonomy;

import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.tag.Tag;
import com.cs.core.config.interactor.model.klass.DefaultValueChangeModel;
import com.cs.core.config.interactor.model.klass.IDefaultValueChangeModel;
import com.cs.core.config.interactor.model.klass.IKlassInformationModel;
import com.cs.core.config.interactor.model.klass.KlassInformationModel;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonTypeInfo(use = Id.NONE)
public class GetAttributionTaxonomyModel extends MasterTaxonomyModel
    implements IGetAttributionTaxonomyModel {
  
  private static final long                     serialVersionUID = 1L;
  protected Map<String, IKlassInformationModel> referencedKlasses;
  protected List<IDefaultValueChangeModel>      defaultValuesDiff;
  protected Map<String, List<String>>           deletedPropertiesFromSource;
  protected Map<String, String>                 referencedContexts;
  protected Map<String, ITag>                   referencedTags;
  protected Map<String, String>                 referencedEvents;
  protected Map<String, String>                 referencedTasks;
  protected Map<String, String>                 referencedDataRules;
  
  @Override
  public Map<String, IKlassInformationModel> getReferencedKlasses()
  {
    if (referencedKlasses == null) {
      referencedKlasses = new HashMap<>();
    }
    return referencedKlasses;
  }
  
  @JsonDeserialize(contentAs = KlassInformationModel.class)
  @Override
  public void setReferencedKlasses(Map<String, IKlassInformationModel> referencedKlasses)
  {
    this.referencedKlasses = referencedKlasses;
  }
  
  @Override
  public List<IDefaultValueChangeModel> getDefaultValuesDiff()
  {
    if (defaultValuesDiff == null) {
      defaultValuesDiff = new ArrayList<>();
    }
    return defaultValuesDiff;
  }
  
  @JsonDeserialize(contentAs = DefaultValueChangeModel.class)
  @Override
  public void setDefaultValuesDiff(List<IDefaultValueChangeModel> defaultValuesDiff)
  {
    this.defaultValuesDiff = defaultValuesDiff;
  }
  
  public Map<String, List<String>> getDeletedPropertiesFromSource()
  {
    if (deletedPropertiesFromSource == null) {
      deletedPropertiesFromSource = new HashMap<>();
    }
    return deletedPropertiesFromSource;
  }
  
  @Override
  public void setDeletedPropertiesFromSource(Map<String, List<String>> deletedPropertiesFromSource)
  {
    this.deletedPropertiesFromSource = deletedPropertiesFromSource;
  }
  
  // Map of context id vs context label
  @Override
  public Map<String, String> getReferencedContexts()
  {
    return referencedContexts;
  }
  
  @Override
  public void setReferencedContexts(Map<String, String> referencedContexts)
  {
    this.referencedContexts = referencedContexts;
  }
  
  @Override
  public Map<String, ITag> getReferencedTags()
  {
    return referencedTags;
  }
  
  @Override
  @JsonDeserialize(contentAs = Tag.class)
  public void setReferencedTags(Map<String, ITag> referencedTags)
  {
    this.referencedTags = referencedTags;
  }
  
  @Override
  public Map<String, String> getReferencedEvents()
  {
    return referencedEvents;
  }
  
  @Override
  public void setReferencedEvents(Map<String, String> referencedEvents)
  {
    this.referencedEvents = referencedEvents;
  }
  
  @Override
  public Map<String, String> getReferencedTasks()
  {
    return referencedTasks;
  }
  
  @Override
  public void setReferencedTasks(Map<String, String> referencedTasks)
  {
    this.referencedTasks = referencedTasks;
  }
  
  @Override
  public Map<String, String> getReferencedDataRules()
  {
    return referencedDataRules;
  }
  
  @Override
  public void setReferencedDataRules(Map<String, String> referencedDataRules)
  {
    this.referencedDataRules = referencedDataRules;
  }
}
