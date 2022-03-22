package com.cs.core.config.interactor.model.mapping;

import com.cs.core.config.interactor.entity.mapping.IMapping;
import com.cs.core.config.interactor.model.configdetails.IConfigModel;
import com.cs.core.config.interactor.model.configdetails.IConfigModelForBoarding;

import java.util.List;

public interface IMappingModel extends IConfigModel, IMapping {
  
  public static final String ATTRIBUTE_MAPPINGS              = "attributeMappings";
  public static final String TAG_MAPPINGS                    = "tagMappings";
  public static final String CLASS_MAPPINGS                  = "classMappings";
  public static final String TAXONOMY_MAPPINGS               = "taxonomyMappings";
  public static final String CONFIG_DETAILS                  = "configDetails";
  public static final String SELECTED_PROPERTY_COLLECTION_ID = "selectedPropertyCollectionId";
  public static final String PROPERTY_COLLECTION_IDS         = "propertyCollectionIds";
  public static final String RELATIONSHIP_MAPPINGS           = "relationshipMappings";
  public static final String CONTEXT_IDS                     = "contextIds";
  public static final String SELECTED_CONTEXT_ID             = "selectedContextId";
  
  public List<IConfigRuleAttributeMappingModel> getAttributeMappings();
  
  public void setAttributeMappings(List<IConfigRuleAttributeMappingModel> attributeMappings);
  
  public List<IConfigRuleTagMappingModel> getTagMappings();
  
  public void setTagMappings(List<IConfigRuleTagMappingModel> tagMappings);
  
  public List<IConfigRuleClassMappingModel> getClassMappings();
  
  public void setClassMappings(List<IConfigRuleClassMappingModel> classMappings);
  
  public List<IConfigRuleTaxonomyMappingModel> getTaxonomyMappings();
  
  public void setTaxonomyMappings(List<IConfigRuleTaxonomyMappingModel> taxonomyMappings);
  
  public IConfigModelForBoarding getConfigDetails();
  
  public void setConfigDetails(IConfigModelForBoarding configDetails);
  
  public String getSelectedPropertyCollectionId();
  
  public void setSelectedPropertyCollectionId(String selectedPropertyCollectionId);
 
  public List<String> getPropertyCollectionIds();
  
  public void setPropertyCollectionIds(List<String> propertyCollectionIds);
  
  public void setRelationshipMappings(List<IConfigRuleRelationshipMappingModel> relationshipMappings);
  
  public List<IConfigRuleRelationshipMappingModel> getRelationshipMappings();
  
  public List<String> getContextIds();
  
  public void setContextIds(List<String> contextIds);
  
  public String getSelectedContextId();
  
  public void setSelectedContextId(String selectedContextId);
}
