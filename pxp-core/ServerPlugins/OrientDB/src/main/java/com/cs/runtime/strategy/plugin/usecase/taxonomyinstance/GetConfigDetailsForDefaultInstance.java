package com.cs.runtime.strategy.plugin.usecase.taxonomyinstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.strategy.plugin.model.GetConfigDetailsHelperModel;
import com.cs.config.strategy.plugin.model.IGetConfigDetailsHelperModel;
import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.globalpermissions.IGlobalPermission;
import com.cs.core.config.interactor.model.globalpermissions.IReferencedTemplatePermissionModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedContextModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsModel;
import com.cs.runtime.strategy.plugin.usecase.base.AbstractConfigDetailsForCustomTab;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class GetConfigDetailsForDefaultInstance extends AbstractConfigDetailsForCustomTab {
  
  public GetConfigDetailsForDefaultInstance(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForDefaultInstance/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String userId = (String) requestMap.get("userId");
    IGetConfigDetailsHelperModel helperModel = new GetConfigDetailsHelperModel();
    helperModel.setUserId(userId);
    Vertex userInRole = RoleUtils.getRoleFromUser(userId);
    Map<String, Object> referencedDataRuleMap = new HashMap<>();
    Map<String, Object> mapToReturn = getMapToReturn(referencedDataRuleMap);
    
    fillEntityIdsHavingReadPermission(userInRole, mapToReturn);
    String requestedTypeId = (String) requestMap.get("requestedTypeId");
    requestedTypeId = requestedTypeId == null ? "" : requestedTypeId;
    
    List<String> klassIds = (List<String>) requestMap.get(IMulticlassificationRequestModel.KLASS_IDS);
    helperModel.setInstanceKlassIds(new HashSet<>(klassIds));
    fillKlassDetails(mapToReturn, klassIds, referencedDataRuleMap, helperModel, requestedTypeId, UtilClass.getCodeNew(userInRole));
    
    List<String> taxonomyIds = (List<String>) requestMap.get(IMulticlassificationRequestModel.SELECTED_TAXONOMY_IDS);
    
    fillTaxonomyDetails(mapToReturn, taxonomyIds, referencedDataRuleMap, helperModel, requestedTypeId);
    fillReferencedPermission(mapToReturn, CommonConstants.CUSTOM_TEMPLATE, userInRole, helperModel);
    fillReferencedElementInRespectiveMap(mapToReturn, helperModel);
    fillMandatoryReferencedAttributes(mapToReturn);
    
    return mapToReturn;
  }
  
  protected Map<String, Object> getMapToReturn(Map<String, Object> referencedDataRuleMap)
  {
    
    Map<String, Object> mapToReturn = super.getMapToReturn(referencedDataRuleMap);
    Map<String, Object> referencedNatureRelationships = new HashMap<>();
    Map<String, Object> referencedRelationshipProperties = new HashMap<>();
    Map<String, Object> referencedPropertyCollectionMap = new HashMap<>();
    Map<String, Object> referencedRoleMap = new HashMap<>();
    Map<String, String> referencedRelationshipsMapping = new HashMap<>();
    Map<String, Object> referencedVariantContextsMap = new HashMap<>();
    Map<String, Object> embeddedVariantContexts = new HashMap<>();
    Map<String, Object> languageVariantContexts = new HashMap<>();
    referencedVariantContextsMap.put(IReferencedContextModel.EMBEDDED_VARIANT_CONTEXTS, embeddedVariantContexts);
    referencedVariantContextsMap.put(IReferencedContextModel.LANGUAGE_VARIANT_CONTEXTS, languageVariantContexts);
    Map<String, Object> productVariantContexts = new HashMap<>();
    
    Map<String, String> relationshipReferencedElements = new HashMap<>();
    referencedVariantContextsMap.put(IReferencedContextModel.PRODUCT_VARIANT_CONTEXTS, productVariantContexts);
    
    mapToReturn.put(IGetConfigDetailsModel.TECHNICAL_IMAGE_VARIANT_CONTEXT_WITH_AUTO_CREATE_ENABLE, new ArrayList<>());
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.REFERENCED_RELATIONSHIPS_PROPERTIES, referencedRelationshipProperties);
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.REFERENCED_PROPERTY_COLLECTIONS, referencedPropertyCollectionMap);
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.REFERENCED_ROLES, referencedRoleMap);
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.REFERENCED_RELATIONSHIPS_MAPPING, referencedRelationshipsMapping);
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.REFERENCED_NATURE_RELATIONSHIPS, referencedNatureRelationships);
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.REFERENCED_VARIANT_CONTEXTS, referencedVariantContextsMap);
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.RELATIONSHIP_REFERENCED_ELEMENTS, relationshipReferencedElements);
    Map<String, Object> referencedRelationshipMap = new HashMap<>();
    Map<String, Object> relationshipVariantContexts = new HashMap<>();
    referencedVariantContextsMap.put(IReferencedContextModel.RELATIONSHIP_VARIANT_CONTEXTS, relationshipVariantContexts);
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.REFERENCED_RELATIONSHIPS, referencedRelationshipMap);
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.VERSIONABLE_TAGS, new ArrayList<>());
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.VERSIONABLE_ATTRIBUTES, new ArrayList<>());
    
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.MANDATORY_ATTRIBUTE_IDS, new ArrayList<>());
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.MANDATORY_TAG_IDS, new ArrayList<>());
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.SHOULD_TAG_IDS, new ArrayList<>());
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.SHOULD_ATTRIBUTE_IDS, new ArrayList<>());
    return mapToReturn;
    
  }
  
  protected void fillReferencedPermission(Map<String, Object> responseMap, String tabType, Vertex userInRole,
      IGetConfigDetailsHelperModel helperModel) throws Exception
  {
    Set<Vertex> roles = new HashSet<Vertex>();
    // Get all roles & pass to GlobalPermissionUtil to get task class Ids that
    OrientGraph graphDB = UtilClass.getGraph();
    Iterable<Vertex> roleVertices = graphDB.getVerticesOfClass(VertexLabelConstants.ENTITY_TYPE_ROLE);
    for (Vertex roleVertex : roleVertices) {
      roles.add(roleVertex);
    }
    fillRoleIdsAndTaskIdsHavingReadPermission(responseMap, userInRole, roles);
    
    fillKlassIdsAndTaxonomyIdsHavingReadPermission(userInRole, responseMap);
    Map<String, Object> referencedPermissions = (Map<String, Object>) responseMap.get(IGetConfigDetailsModel.REFERENCED_PERMISSIONS);
    String roleId = UtilClass.getCodeNew(userInRole);
    
    fillGlobalPermissionDetails(roleId, responseMap, helperModel);
    
    if (helperModel.getNatureNode() != null)
      fillNatureKlassPermissions(responseMap, helperModel, referencedPermissions, roleId);
    
    fillNonNatureKlassPermissions(responseMap, helperModel, referencedPermissions, roleId);
    
    fillTaxonomyPermissions(responseMap, helperModel, referencedPermissions, roleId);
    
    fillCollectionPermissions(responseMap, helperModel, referencedPermissions, roleId);
    
    filterContextAccordingToKlassPermission(responseMap, helperModel, userInRole);
    
    fillFunctionPermissionDetails(userInRole, responseMap);
  }
  
  protected void fillGlobalPermissionDetails(String roleId, Map<String, Object> configDetails, IGetConfigDetailsHelperModel helperModel)
      throws Exception
  {
    Map<String, Object> referencedPermission = (Map<String, Object>) configDetails.get(IGetConfigDetailsModel.REFERENCED_PERMISSIONS);
    
    Map<String, Object> globalPermissionMap = (Map<String, Object>) referencedPermission
        .get(IReferencedTemplatePermissionModel.GLOBAL_PERMISSION);
    if (globalPermissionMap == null) {
      globalPermissionMap = GlobalPermissionUtils.getDefaultGlobalPermission();
      referencedPermission.put(IReferencedTemplatePermissionModel.GLOBAL_PERMISSION, globalPermissionMap);
    }
    
    boolean isKlassReadPermission = (boolean) globalPermissionMap.get(IGlobalPermission.CAN_READ);
    
    Map<String, Object> globalReadPermissionForTaxonomyMap = new HashMap<>();
    globalReadPermissionForTaxonomyMap.put(IGlobalPermission.CAN_READ, false);
    for (Vertex taxonomyVertex : helperModel.getTaxonomyVertices()) {
      String taxonomyId = UtilClass.getCodeNew(taxonomyVertex);
      Map<String, Object> permissionMap = GlobalPermissionUtils.getKlassAndTaxonomyPermission(taxonomyId, roleId);
      Set<String> taxonomyIdsHavingRP = (Set<String>) referencedPermission.get(IReferencedTemplatePermissionModel.TAXONOMY_IDS_HAVING_RP);
      Set<String> allTaxonomyIdsHavingRP = (Set<String>) referencedPermission
          .get(IReferencedTemplatePermissionModel.ALL_TAXONOMY_IDS_HAVING_RP);
      if (allTaxonomyIdsHavingRP.contains(taxonomyId) || taxonomyIdsHavingRP.isEmpty()) {
        permissionMap.put(IGlobalPermission.CAN_READ, true);
      }
      else {
        permissionMap.put(IGlobalPermission.CAN_READ, false);
      }
      Boolean canRead = (Boolean) permissionMap.get(IGlobalPermission.CAN_READ)
          || (Boolean) globalReadPermissionForTaxonomyMap.get(IGlobalPermission.CAN_READ);
      globalReadPermissionForTaxonomyMap.put(IGlobalPermission.CAN_READ, canRead);
    }
    
    boolean isTaxonomyReadPermission = (boolean) globalReadPermissionForTaxonomyMap.get(IGlobalPermission.CAN_READ)
        || helperModel.getTaxonomyVertices().isEmpty();
    if (isKlassReadPermission == true && isTaxonomyReadPermission == true) {
      globalPermissionMap.put(IGlobalPermission.CAN_READ, true);
    }
    else {
      globalPermissionMap.put(IGlobalPermission.CAN_READ, false);
    }
    
    if ((Boolean) globalPermissionMap.get(IGlobalPermission.CAN_CREATE) && (Boolean) globalPermissionMap.get(IGlobalPermission.CAN_READ)
        && (Boolean) globalPermissionMap.get(IGlobalPermission.CAN_EDIT)
        && (Boolean) globalPermissionMap.get(IGlobalPermission.CAN_DELETE)) {
      return;
    }
  }
  
  @Override
  protected void fillNumberOfVersionsToMaintain(Map<String, Object> mapToReturn, Map<String, Object> klassMap)
  {
    // for default instance we are not going to create any versions.
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.NUMBER_OF_VERSIONS_TO_MAINTAIN, 0);
  }
  
}
