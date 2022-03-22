package com.cs.core.config.interactor.model.customtemplate;

import java.util.Map;

import com.cs.base.interactor.model.audit.ConfigResponseWithAuditLogModel;
import com.cs.core.config.interactor.entity.customtemplate.CustomTemplate;
import com.cs.core.config.interactor.entity.customtemplate.ICustomTemplate;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IdLabelModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class GetCustomTemplateModel extends ConfigResponseWithAuditLogModel implements IGetCustomTemplateModel {
  
  private static final long            serialVersionUID = 1L;
  protected ICustomTemplate            template;
  protected Map<String, IIdLabelModel> referencedRelationships;
  protected Map<String, IIdLabelModel> referencedPropertyCollections;
  protected Map<String, IIdLabelModel> referencedNatureRelationships;
  protected Map<String, IIdLabelModel> referencedContext;
  
  @Override
  public ICustomTemplate getTemplate()
  {
    return template;
  }
  
  @Override
  @JsonDeserialize(as = CustomTemplate.class)
  public void setTemplate(ICustomTemplate template)
  {
    this.template = template;
  }
  
  @Override
  public Map<String, IIdLabelModel> getReferencedRelationships()
  {
    return referencedRelationships;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdLabelModel.class)
  public void setReferencedRelationships(Map<String, IIdLabelModel> referencedRelationships)
  {
    this.referencedRelationships = referencedRelationships;
  }
  
  @Override
  public Map<String, IIdLabelModel> getReferencedPropertyCollections()
  {
    return referencedPropertyCollections;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdLabelModel.class)
  public void setReferencedPropertyCollections(
      Map<String, IIdLabelModel> referencedPropertyCollections)
  {
    this.referencedPropertyCollections = referencedPropertyCollections;
  }
  
  @Override
  public Map<String, IIdLabelModel> getReferencedNatureRelationships()
  {
    return referencedNatureRelationships;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdLabelModel.class)
  public void setReferencedNatureRelationships(
      Map<String, IIdLabelModel> referencedNatureRelationships)
  {
    this.referencedNatureRelationships = referencedNatureRelationships;
  }
  
  @Override
  public Map<String, IIdLabelModel> getReferencedContexts()
  {
    return referencedContext;
  }
  
  @Override
  @JsonDeserialize(contentAs = IdLabelModel.class)
  public void setReferencedContexts(Map<String, IIdLabelModel> referencedContext)
  {
    this.referencedContext = referencedContext;
  }
}
