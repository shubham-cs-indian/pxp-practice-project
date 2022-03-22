package com.cs.core.config.interactor.model.instancetree;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.instancetree.IReferencedPropertyModel;

public interface IConfigDetailsForGetFilterChildrenResponseModel extends IModel {
  
  public static final String ALLOWED_ENTITIES           = "allowedEntities";
  public static final String KLASS_IDS_HAVING_RP        = "klassIdsHavingRP";
  public static final String TAXONOMY_IDS_HAVING_RP     = "taxonomyIdsHavingRP";
  public static final String TRANSLATABLE_ATTRIBUTE_IDS = "translatableAttributeIds";
  public static final String REFERENCED_PROPERTY        = "referencedProperty";
  public static final String SEARCHABLE_ATTRIBUTE_IDS   = "searchableAttributeIds";
  public static final String MAJOR_TAXONOMY_IDS         = "majorTaxonomyIds";
  public static final String RULE_VIOLATIONS_LABELS     = "ruleViolationsLabels";

  public List<String> getAllowedEntities();
  public void setAllowedEntities(List<String> allowedEntites);
  
  public Set<String> getKlassIdsHavingRP();
  public void setKlassIdsHavingRP(Set<String> klassIdsWithRP);
 
  public Set<String> getTaxonomyIdsHavingRP();
  public void setTaxonomyIdsHavingRP(Set<String> taxonomyIdsHavingRP);

  public List<String> getTranslatableAttributeIds();
  public void setTranslatableAttributeIds(List<String> translatableAttributes);
  
  public IReferencedPropertyModel getReferencedProperty();
  public void setReferencedProperty(IReferencedPropertyModel referencedProperty);
  
  public List<String> getSearchableAttributeIds();
  public void setSearchableAttributeIds(List<String> searchableAttributes);
  
  public List<String> getMajorTaxonomyIds();
  public void setMajorTaxonomyIds(List<String> majorTaxonomyIds);
  
  public Map<String, String> getRuleViolationsLabels();
  public void setRuleViolationsLabels(Map<String, String> ruleViolationsLabels);
}
