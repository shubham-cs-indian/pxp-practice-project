package com.cs.core.runtime.interactor.model.configdetails.instancetree;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.instancetree.INewApplicableFilterModel;

/**
 * Generic model - All instance tree config Details should extend this
 * 
 * @author Lokesh.Saboo
 *
 */
public interface IConfigDetailsForNewInstanceTreeModel extends IModel {
  
  public static final String ALLOWED_ENTITIES            = "allowedEntities";
  public static final String KLASS_IDS_HAVING_RP         = "klassIdsHavingRP";
  public static final String TAXONOMY_IDS_HAVING_RP      = "taxonomyIdsHavingRP";
  public static final String SEARCHABLE_ATTRIBUTE_IDS    = "searchableAttributeIds";
  public static final String TRANSLATABLE_ATTRIBUTE_IDS  = "translatableAttributeIds";
  public static final String REFERENCED_ATTRIBUTES       = "referencedAttributes";
  public static final String REFERENCED_TAGS             = "referencedTags";
  public static final String FILTER_DATA                 = "filterData";
  public static final String MAJOR_TAXONOMY_IDS          = "majorTaxonomyIds";
  
  public List<String> getAllowedEntities();
  public void setAllowedEntities(List<String> allowedEntites);
  
  public Set<String> getKlassIdsHavingRP();
  public void setKlassIdsHavingRP(Set<String> klassIdsWithRP);
 
  public Set<String> getTaxonomyIdsHavingRP();
  public void setTaxonomyIdsHavingRP(Set<String> taxonomyIdsHavingRP);

  public List<String> getSearchableAttributeIds();
  public void setSearchableAttributeIds(List<String> searchableAttributes);
  
  public List<String> getTranslatableAttributeIds();
  public void setTranslatableAttributeIds(List<String> translatableAttributes);
  
  public Map<String, IAttribute> getReferencedAttributes();
  public void setReferencedAttributes(Map<String, IAttribute> referencedElements);
  
  public Map<String, ITag> getReferencedTags();
  public void setReferencedTags(Map<String, ITag> referencedTags);
  
  public List<INewApplicableFilterModel> getFilterData();
  public void setFilterData(List<INewApplicableFilterModel> filterData);
  
  public List<String> getMajorTaxonomyIds();
  public void setMajorTaxonomyIds(List<String> majorTaxonomyIds);
}
