package com.cs.core.config.interactor.model.governancerule;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.task.ITask;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelModel;

import java.util.Map;

public interface IConfigDetailsForGovernanceRuleModel extends IModel {
  
  public static final String REFERENCED_ATTRIBUTES    = "referencedAttributes";
  public static final String REFERENCED_TAGS          = "referencedTags";
  public static final String REFERENCED_ROLES         = "referencedRoles";
  public static final String REFERENCED_KLASSES       = "referencedKlasses";
  public static final String REFERENCED_TAXONOMIES    = "referencedTaxonomies";
  public static final String REFERENCED_RELATIONSHIPS = "referencedRelationships";
  public static final String REFERENCED_TASK          = "referencedTask";
  
  public Map<String, IAttribute> getReferencedAttributes();
  
  public void setReferencedAttributes(Map<String, IAttribute> referencedAttributes);
  
  public Map<String, ITag> getReferencedTags();
  
  public void setReferencedTags(Map<String, ITag> referencedTags);
  
  public Map<String, IRole> getReferencedRoles();
  
  public void setReferencedRoles(Map<String, IRole> referencedRoles);
  
  public Map<String, IIdLabelModel> getReferencedKlasses();
  
  public void setReferencedKlasses(Map<String, IIdLabelModel> referencedKlasses);
  
  public Map<String, IIdLabelModel> getReferencedTaxonomies();
  
  public void setReferencedTaxonomies(Map<String, IIdLabelModel> referencedTaxonomies);
  
  public Map<String, IIdLabelModel> getReferencedRelationships();
  
  public void setReferencedRelationships(Map<String, IIdLabelModel> referencedRelationships);
  
  public Map<String, ITask> getReferencedTask();
  
  public void setReferencedTask(Map<String, ITask> referencedTask);
}
