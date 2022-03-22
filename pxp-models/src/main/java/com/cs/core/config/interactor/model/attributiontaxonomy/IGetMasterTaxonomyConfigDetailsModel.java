package com.cs.core.config.interactor.model.attributiontaxonomy;

import java.util.Map;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.klass.IPropagableContextKlassInformationModel;
import com.cs.core.config.interactor.model.taxonomyhierarchy.IConfigTaxonomyHierarchyInformationModel;
import com.cs.core.runtime.interactor.model.configdetails.IConfigEntityInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IGetMasterTaxonomyConfigDetailsModel extends IModel {
  
  public static final String REFERENCED_KLASSES    = "referencedKlasses";
  public static final String REFERENCED_CONTEXTS   = "referencedContexts";
  public static final String REFERENCED_TAGS       = "referencedTags";
  public static final String REFERENCED_TASKS      = "referencedTasks";
  public static final String REFERENCED_DATARULES  = "referencedDataRules";
  public static final String REFERENCED_ATTRIBUTES = "referencedAttributes";
  public static final String REFERENCED_TAXONOMIES = "referencedTaxonomies";
  
  public Map<String, IPropagableContextKlassInformationModel> getReferencedKlasses();
  
  public void setReferencedKlasses(
      Map<String, IPropagableContextKlassInformationModel> ReferencedKlasses);
  
  public Map<String, IConfigEntityInformationModel> getReferencedContexts();
  
  public void setReferencedContexts(Map<String, IConfigEntityInformationModel> referencedContexts);
  
  // key:tagId
  public Map<String, ITag> getReferencedTags();
  
  public void setReferencedTags(Map<String, ITag> referencedTags);
  
  public Map<String, IConfigEntityInformationModel> getReferencedTasks();
  
  public void setReferencedTasks(Map<String, IConfigEntityInformationModel> referencedTasks);
  
  public Map<String, String> getReferencedDataRules();
  
  public void setReferencedDataRules(Map<String, String> referencedDataRules);
  
  public Map<String, ? extends IAttribute> getReferencedAttributes();
  
  public void setReferencedAttributes(Map<String, ? extends IAttribute> referencedAttributes);
  
  public Map<String, IConfigTaxonomyHierarchyInformationModel> getReferencedTaxonomies();  
  
  public void setReferencedTaxonomies(Map<String, IConfigTaxonomyHierarchyInformationModel> referencedTaxonomies);
}
