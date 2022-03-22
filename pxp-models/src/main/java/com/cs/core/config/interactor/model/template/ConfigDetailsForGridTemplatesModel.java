package com.cs.core.config.interactor.model.template;

import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IdLabelCodeModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

public class ConfigDetailsForGridTemplatesModel implements IConfigDetailsForGridTemplatesModel {
  
  private static final long                serialVersionUID = 1L;
  
  protected Map<String, IIdLabelCodeModel> referencedRelationships;
  protected Map<String, IIdLabelCodeModel> referencedPropertyCollections;
  protected Map<String, IIdLabelCodeModel> referencedNatureRelationships;
  protected Map<String, IIdLabelCodeModel> referencedContext;

  
  @Override
  public Map<String, IIdLabelCodeModel> getReferencedRelationships()
  {
    return referencedRelationships;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdLabelCodeModel.class)
  public void setReferencedRelationships(Map<String, IIdLabelCodeModel> referencedRelationships)
  {
    this.referencedRelationships = referencedRelationships;
  }
  
  @Override
  public Map<String, IIdLabelCodeModel> getReferencedPropertyCollections()
  {
    return referencedPropertyCollections;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdLabelCodeModel.class)
  public void setReferencedPropertyCollections(
      Map<String, IIdLabelCodeModel> referencedPropertyCollections)
  {
    this.referencedPropertyCollections = referencedPropertyCollections;
  }
  
  @Override
  public Map<String, IIdLabelCodeModel> getReferencedNatureRelationships()
  {
    return referencedNatureRelationships;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdLabelCodeModel.class)
  public void setReferencedNatureRelationships(
      Map<String, IIdLabelCodeModel> referencedNatureRelationships)
  {
    this.referencedNatureRelationships = referencedNatureRelationships;
  }
  
  @Override
  public Map<String, IIdLabelCodeModel> getReferencedContexts()
  {
    return referencedContext;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdLabelCodeModel.class)
  public void setReferencedContexts(Map<String, IIdLabelCodeModel> referencedContext)
  {
    this.referencedContext = referencedContext;
  }
}
