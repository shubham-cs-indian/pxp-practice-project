package com.cs.core.config.interactor.model.klass;

import com.cs.core.config.interactor.entity.attribute.IAttribute;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.role.IRole;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.model.datarule.IDataRuleModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedArticleTaxonomyModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.runtime.interactor.model.configdetails.IXRayConfigDetailsModel;
import com.cs.core.runtime.interactor.model.relationship.IGetReferencedNatureRelationshipModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IGetMultiClassificationKlassDetailsModel extends IGetConfigDetailsModel {
  
  public static final String REFRENCED_KLASSES                = "referencedKlasses";
  public static final String REFERENCED_PROPERTY_COLLECTIONS  = "referencedPropertyCollections";
  public static final String REFERENCED_ELEMENTS              = "referencedElements";
  public static final String REFERENCED_ATTRIBUTES            = "referencedAttributes";
  public static final String REFERENCED_TAGS                  = "referencedTags";
  public static final String REFERENCED_ROLES                 = "referencedRoles";
  public static final String REFERENCED_DATA_RULES            = "referencedDataRules";
  public static final String REFERENCED_PERMISSION            = "referencedPermission";
  public static final String NUMBER_OF_VERSIONS_TO_MAINTAIN   = "numberOfVersionsToMaintain";
  public static final String NOTIFICATION_SETTINGS            = "notificationSettings";
  public static final String REFERENCED_RELATIONSHIPS         = "referencedRelationships";
  public static final String REFERENCED_VARIANT_CONTEXTS      = "referencedVariantContexts";
  public static final String REFERENCED_TAXONOMIES            = "referencedTaxonomies";
  public static final String GLOBAL_PERMISSION                = "globalPermission";
  public static final String REFERENCED_NATURE_RELATIONSHIPS  = "referencedNatureRelationships";
  public static final String REFERENCED_LIFECYCLE_STATUS_TAGS = "referencedLifeCycleStatusTags";
  public static final String REFERENCED_RELATIONSHIPS_MAPPING = "referencedRelationshipsMapping";
  public static final String TAXONOMY_HIERARCHIES             = "taxonomiesHierarchies";
  public static final String CAN_CREATE_NATURE                = "canCreateNature";
  public static final String X_RAY_CONFIG_DETAILS             = "xrayConfigDetails";
  
  // key:klassId
  public Map<String, IReferencedKlassDetailStrategyModel> getReferencedKlasses();
  
  public void setReferencedKlasses(Map<String, IReferencedKlassDetailStrategyModel> klasses);
  
  // key:propertyCollectionId
  public Map<String, IReferencedPropertyCollectionModel> getReferencedPropertyCollections();
  
  public void setReferencedPropertyCollections(
      Map<String, IReferencedPropertyCollectionModel> referencedSections);
  
  // key:propertyId[attributeId, tagId, roleId]
  public Map<String, IReferencedSectionElementModel> getReferencedElements();
  
  public void setReferencedElements(Map<String, IReferencedSectionElementModel> referencedElements);
  
  // key:attributeId
  public Map<String, IAttribute> getReferencedAttributes();
  
  public void setReferencedAttributes(Map<String, IAttribute> referencedElements);
  
  // key:tagId
  public Map<String, ITag> getReferencedTags();
  
  public void setReferencedTags(Map<String, ITag> referencedTags);
  
  // key:roleId
  public Map<String, IRole> getReferencedRoles();
  
  public void setReferencedRoles(Map<String, IRole> referencedRoles);
  
  public List<IDataRuleModel> getReferencedDataRules();
  
  public void setReferencedDataRules(List<IDataRuleModel> referencedDataRules);
  
  public IReferencedRolePermissionModel getReferencedPermission();
  
  public void setReferencedPermission(IReferencedRolePermissionModel referencedPermission);
  
  public Integer getNumberOfVersionsToMaintain();
  
  public void setNumberOfVersionsToMaintain(Integer numberOfVersionsToMaintain);
  
  // key:roleId
  // value: array of property id's to notify
  public Map<String, Set<String>> getNotificationSettings();
  
  public void setNotificationSettings(Map<String, Set<String>> referencedPermission);
  
  // key:relationshipId
  public Map<String, IRelationship> getReferencedRelationships();
  
  public void setReferencedRelationships(Map<String, IRelationship> referencedRelationships);
  
  public IReferencedContextModel getReferencedVariantContexts();
  
  public void setReferencedVariantContexts(IReferencedContextModel referencedVariantContexts);
  
  public Map<String, IReferencedArticleTaxonomyModel> getReferencedTaxonomies();
  
  public void setReferencedTaxonomies(
      Map<String, IReferencedArticleTaxonomyModel> referencedTaxonomies);
  
  public IGlobalPermission getGlobalPermission();
  
  public void setGlobalPermission(IGlobalPermission globalPermission);
  
  public Map<String, IGetReferencedNatureRelationshipModel> getReferencedNatureRelationships();
  
  public void setReferencedNatureRelationships(
      Map<String, IGetReferencedNatureRelationshipModel> referencedNatureRelationships);
  
  public List<String> getReferencedLifeCycleStatusTags();
  
  public void setReferencedLifeCycleStatusTags(List<String> referencedLifeCycleStatusTags);
  
  public Map<String, String> getReferencedRelationshipsMapping();
  
  public void setReferencedRelationshipsMapping(Map<String, String> referencedRelationshipsMapping);
  
  public Map<String, List<String>> getTaxonomiesHierarchies();
  
  public void setTaxonomiesHierarchies(Map<String, List<String>> taxonomiesHierarchies);
  
  public Boolean getCanCreateNature();
  
  public void setCanCreateNature(Boolean canCreateNature);
  
  public IXRayConfigDetailsModel getXRayConfigDetails();
  
  public void setXRayConfigDetails(IXRayConfigDetailsModel xRayConfigDetails);
}
