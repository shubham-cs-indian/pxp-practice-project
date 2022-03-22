package com.cs.core.runtime.interactor.model.configdetails;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.klass.IReferencedSectionElementModel;
import com.cs.core.runtime.interactor.model.relationship.IReferencedRelationshipPropertiesModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;

import java.util.List;
import java.util.Map;

public interface IConfigDetailsForSwitchTypeResponseModel extends IGetConfigDetailsModel {
  
  public static final String REFERENCED_ELEMENTS_FOR_SWITCH_TYPE = "referencedElementsForSwitchType";
  public static final String REFERENCED_ATTRIBUTES               = "referencedAttributes";
  public static final String REFERENCED_TAGS                     = "referencedTags";
  public static final String REFERENCED_ROLES                    = "referencedRoles";
  public static final String NUMBER_OF_VERSIONS_TO_MAINTAIN      = "numberOfVersionsToMaintain";
  public static final String TAXONOMY_HIERARCHIES                = "taxonomiesHierarchies";
  public static final String REFERENCED_DATA_RULES               = "referencedDataRules";
  public static final String REFERENCED_RELATIONSHIPS_PROPERTIES = "referencedRelationshipProperties";
  public static final String MINOR_TAXONOMY_ID_TO_REMOVE         = "minorTaxonomyIdToRemove";
  public static final String KLASS_IDS_TO_ADD                    = "klassIdsToAdd";
  public static final String TAXONOMY_IDS_TO_ADD                 = "taxonomyIdsToAdd";
  
  public Map<String, List<String>> getTaxonomiesHierarchies();
  
  public void setTaxonomiesHierarchies(Map<String, List<String>> taxonomiesHierarchies);
  
  public Map<String, List<IReferencedSectionElementModel>> getReferencedElementsForSwitchType();
  
  public void setReferencedElementsForSwitchType(
      Map<String, List<IReferencedSectionElementModel>> referencedElementsForSwitchType);
  
  public Map<String, ITag> getReferencedTags();
  
  public void setReferencedTags(Map<String, ITag> referencedTags);
  
  public Map<String, IAttribute> getReferencedAttributes();
  
  public void setReferencedAttributes(Map<String, IAttribute> referencedAttributes);
  
  public Integer getNumberOfVersionsToMaintain();
  
  public void setNumberOfVersionsToMaintain(Integer numberOfVersionsToMaintain);
  
  public List<IDataRuleModel> getReferencedDataRules();
  
  public void setReferencedDataRules(List<IDataRuleModel> referencedDataRules);
  
  public Map<String, IRole> getReferencedRoles();
  
  public void setReferencedRoles(Map<String, IRole> referencedRoles);
  
  // key:relationshipId
  public Map<String, IReferencedRelationshipPropertiesModel> getReferencedRelationshipProperties();
  
  public void setReferencedRelationshipProperties(
      Map<String, IReferencedRelationshipPropertiesModel> referencedRelationshipProperties);
  
  public String getMinorTaxonomyIdToRemove();
  
  public void setMinorTaxonomyIdToRemove(String minorTaxonomyIdToRemove);
  
  public List<String> getKlassIdsToAdd();
  
  public void setKlassIdsToAdd(List<String> klassIdsToAdd);
  
  public List<String> getTaxonomyIdsToAdd();
  
  public void setTaxonomyIdsToAdd(List<String> taxonomyIdsToAdd);
}
