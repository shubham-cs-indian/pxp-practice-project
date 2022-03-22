package com.cs.core.config.interactor.model.mapping;

import java.util.List;

public interface IOutBoundMappingModel extends IAbstractMappingModel {
  
  public static final String IS_ALL_CLASSES_SELECTED             = "isAllClassesSelected";
  public static final String IS_ALL_PROPERTY_COLLECTION_SELECTED = "isAllPropertyCollectionSelected";
  public static final String IS_ALL_TAXONOMIES_SELECTED          = "isAllTaxonomiesSelected";
  public static final String IS_ALL_RELATIONSHIPS_SELECTED       = "isAllrelationshipsSelected";
  public static final String IS_ALL_CONTEXTS_SELECTED            = "isAllContextsSelected";
  public static final String PROPERTY_COLLECTION_IDS             = "propertyCollectionIds";
  public static final String ATTRIBUTE_MAPPINGS                  = "attributeMappings";
  public static final String TAG_MAPPINGS                        = "tagMappings";
  public static final String SELECTED_PROPERTY_COLLECTION_ID     = "selectedPropertyCollectionId";
  public static final String SELECTED_CONTEXT_ID                 = "selectedContextId";
  public static final String CONTEXT_IDS                         = "contextIds";
  public static final String RELATIONSHIP_MAPPINGS               = "relationshipMappings";
  
  public void setAttributeMappings(
      List<IConfigRuleAttributeOutBoundMappingModel> attributeMappings);
  
  public List<IConfigRuleAttributeOutBoundMappingModel> getAttributeMappings();
  
  public void setTagMappings(List<IConfigRuleTagOutBoundMappingModel> tagMappings);
  
  public List<IConfigRuleTagOutBoundMappingModel> getTagMappings();
  
  public Boolean getIsAllTaxonomiesSelected();
  
  public void setIsAllTaxonomiesSelected(Boolean isAllTaxonomiesSelected);
  
  public Boolean getIsAllPropertyCollectionSelected();
  
  public void setIsAllPropertyCollectionSelected(Boolean isAllPropertyCollectionSelected);
  
  public Boolean getIsAllClassesSelected();
  
  public void setIsAllClassesSelected(Boolean isAllClassesSelected);
  
  public void setPropertyCollectionIds(List<String> propertyCollectionIds);
  
  public List<String> getPropertyCollectionIds();
  
  public String getSelectedPropertyCollectionId();
  
  public void setSelectedPropertyCollectionId(String selectedPropertyCollectionId);
  
  public Boolean getIsAllContextsSelected();
  
  public void setIsAllContextsSelected(Boolean isAllContextsSelected);
  
  public String getSelectedContextId();

  public void setSelectedContextId(String selectedContextId);
  
  public List<String> getContextIds();
  
  public void setContextIds(List<String> contextIds);
  
  public Boolean getIsAllRelationshipsSelected();
  
  public void setIsAllRelationshipsSelected(Boolean isAllRelationshipsSelected);
  
  public void setRelationshipMappings(List<IConfigRuleRelationshipMappingModel> relationshipMappings);
  
  public List<IConfigRuleRelationshipMappingModel> getRelationshipMappings();
}
