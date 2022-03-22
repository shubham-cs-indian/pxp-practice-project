package com.cs.core.config.interactor.model.governancerule;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.task.ITask;
import com.cs.core.config.interactor.entity.task.Task;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IdLabelModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Map;

public class ConfigDetailsForGovernanceRuleModel implements IConfigDetailsForGovernanceRuleModel {
  
  private static final long            serialVersionUID = 1L;
  
  protected Map<String, IAttribute>    referencedAttributes;
  protected Map<String, ITag>          referencedTags;
  protected Map<String, IRole>         referencedRoles;
  protected Map<String, IIdLabelModel> referencedKlasses;
  protected Map<String, IIdLabelModel> referencedTaxonomies;
  protected Map<String, IIdLabelModel> referencedRelationships;
  protected Map<String, ITask>         referencedTask;
  
  @Override
  public Map<String, IAttribute> getReferencedAttributes()
  {
    return referencedAttributes;
  }
  
  @Override
  public void setReferencedAttributes(Map<String, IAttribute> referencedAttributes)
  {
    this.referencedAttributes = referencedAttributes;
  }
  
  @Override
  public Map<String, ITag> getReferencedTags()
  {
    return referencedTags;
  }
  
  @Override
  public void setReferencedTags(Map<String, ITag> referencedTags)
  {
    this.referencedTags = referencedTags;
  }
  
  @Override
  public Map<String, IRole> getReferencedRoles()
  {
    return referencedRoles;
  }
  
  @Override
  public void setReferencedRoles(Map<String, IRole> referencedRoles)
  {
    this.referencedRoles = referencedRoles;
  }
  
  @Override
  public Map<String, IIdLabelModel> getReferencedKlasses()
  {
    return referencedKlasses;
  }
  
  @JsonDeserialize(contentAs = IdLabelModel.class)
  @Override
  public void setReferencedKlasses(Map<String, IIdLabelModel> referencedKlasses)
  {
    this.referencedKlasses = referencedKlasses;
  }
  
  @Override
  public Map<String, IIdLabelModel> getReferencedTaxonomies()
  {
    return referencedTaxonomies;
  }
  
  @JsonDeserialize(contentAs = IdLabelModel.class)
  @Override
  public void setReferencedTaxonomies(Map<String, IIdLabelModel> referencedTaxonomies)
  {
    this.referencedTaxonomies = referencedTaxonomies;
  }
  
  @Override
  public Map<String, IIdLabelModel> getReferencedRelationships()
  {
    return referencedRelationships;
  }
  
  @JsonDeserialize(contentAs = IdLabelModel.class)
  @Override
  public void setReferencedRelationships(Map<String, IIdLabelModel> referencedRelationships)
  {
    this.referencedRelationships = referencedRelationships;
  }
  
  @Override
  public Map<String, ITask> getReferencedTask()
  {
    return referencedTask;
  }
  
  @JsonDeserialize(contentAs = Task.class)
  @Override
  public void setReferencedTask(Map<String, ITask> referencedTask)
  {
    this.referencedTask = referencedTask;
  }
}
