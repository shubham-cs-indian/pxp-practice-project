package com.cs.runtime.strategy.plugin.usecase.base.variant;

import com.cs.config.standard.IStandardConfig;
import com.cs.config.strategy.plugin.model.GetConfigDetailsHelperModel;
import com.cs.config.strategy.plugin.model.IGetConfigDetailsHelperModel;
import com.cs.config.strategy.plugin.usecase.globalpermission.util.GlobalPermissionUtils;
import com.cs.config.strategy.plugin.usecase.role.util.RoleUtils;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.variantcontext.util.VariantContextUtils;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.variantcontext.IVariantContext;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextModel;
import com.cs.core.config.interactor.model.variantcontext.IReferencedVariantContextTagsModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IBaseKlassTemplatePermissionModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.variants.IConfigDetailsForGetVariantInstancesInTableViewModel;
import com.cs.runtime.strategy.plugin.usecase.base.AbstractConfigDetails;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("unchecked")
public abstract class AbstractGetConfigDetailsForGetVariantInstance extends AbstractConfigDetails {
  
  public AbstractGetConfigDetailsForGetVariantInstance(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String userId = (String) requestMap.get(IIdParameterModel.CURRENT_USER_ID);
    String contextId = (String) requestMap.get(IIdParameterModel.ID);
    
    Map<String, Object> mapToReturn = getMapToReturn();
    IGetConfigDetailsHelperModel helperModel = new GetConfigDetailsHelperModel();
    helperModel.getEntityIds()
        .add(IStandardConfig.StandardProperty.nameattribute.toString());
    
    fillVariantContextDetails(mapToReturn, contextId, helperModel);
    
    Vertex userInRole = RoleUtils.getRoleFromUser(userId);
    
    fillReferencedPermission(mapToReturn, contextId, userInRole, helperModel);
    
    return mapToReturn;
  }
  
  /**
   * fill referenced VariantContext and referenced PC using its template
   *
   * @author Lokesh
   * @param mapToReturn
   * @param contextId
   * @param helperModel
   * @throws Exception
   */
  protected void fillVariantContextDetails(Map<String, Object> mapToReturn, String contextId,
      IGetConfigDetailsHelperModel helperModel) throws Exception
  {
    Vertex contextNode = UtilClass.getVertexById(contextId, VertexLabelConstants.VARIANT_CONTEXT);
    Iterator<Vertex> contextKlassIterator = contextNode
        .getVertices(Direction.IN, RelationshipLabelConstants.VARIANT_CONTEXT_OF)
        .iterator();
    Vertex contextKlassNode = contextKlassIterator.next();
    /*
    TODO PC
    Vertex contextTemplateNode = TemplateUtils.getTemplateFromKlassIfExist(contextKlassNode);
    
    Vertex contextSequenceNode = TemplateUtils.getSequenceFromTemplate(contextTemplateNode,
        RelationshipLabelConstants.HAS_PROPERTYCOLLECTION_SEQUENCE);
    
    fillReferencedPropertyCollectionFromTemplateNode(helperModel, contextTemplateNode, mapToReturn, null);
    */
    List<String> contextKlassIds = new ArrayList<>();
    String contextKlassId = UtilClass.getCodeNew(contextKlassNode);
    contextKlassIds.add(contextKlassId);
    
    Map<String, Object> referencedDataRuleMap = new HashMap<>();
    mapToReturn.put("referencedDataRules", referencedDataRuleMap.values());
    
    fillKlassDetailsAndGetNatureKlassNode(mapToReturn, VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS,
        contextKlassIds, referencedDataRuleMap, helperModel);
    fillReferencedElementsAndReferencedEntities(mapToReturn, contextKlassIds,
        VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS, helperModel.getEntityIds(), null, helperModel);
    
    Map<String, Object> referencedVariantContexts = (Map<String, Object>) mapToReturn
        .get(IConfigDetailsForGetVariantInstancesInTableViewModel.REFERENCED_VARIANT_CONTEXTS);
    Map<String, Object> referencedTagsMap = (Map<String, Object>) mapToReturn
        .get(IConfigDetailsForGetVariantInstancesInTableViewModel.REFERENCED_TAGS);
    
    Map<String, Object> variantContextMap = VariantContextUtils.getReferencedContexts(contextNode);
    /*
     * TODO PC
    List<String> propertyCollectionIds = contextSequenceNode.getProperty(CommonConstants.SEQUENCE_LIST);
    variantContextMap.put(IReferencedVariantContextModel.PROPERTY_COLLECTIONS, propertyCollectionIds);
    
    variantContextMap.put(IReferencedVariantContextModel.CONTEXT_KLASS_ID, contextKlassId);
    */
    for (Map<String, Object> contextTag : (List<Map<String, Object>>) variantContextMap
        .get(IReferencedVariantContextModel.TAGS)) {
      String entityId = (String) contextTag.get(IReferencedVariantContextTagsModel.TAG_ID);
      if (referencedTagsMap.containsKey(entityId)) {
        continue;
      }
      Vertex entityNode = UtilClass.getVertexById(entityId, VertexLabelConstants.ENTITY_TAG);
      Map<String, Object> entity = TagUtils.getTagMap(entityNode, false);
      referencedTagsMap.put(entityId, entity);
    }
    
    String contextType = contextNode.getProperty(IVariantContext.TYPE);
    switch (contextType) {
      case CommonConstants.IMAGE_VARIANT:
      case CommonConstants.CONTEXTUAL_VARIANT:
      case CommonConstants.LANGUAGE_VARIANT:
      case CommonConstants.GTIN_VARIANT:
        referencedVariantContexts.put(contextId, variantContextMap);
        break;
    }
  }
  
  protected Map<String, Object> getMapToReturn()
  {
    
    Map<String, Object> mapToReturn = new HashMap<>();
    
    Map<String, Object> referencedPropertyCollectionMap = new HashMap<>();
    Map<String, Object> referencedVariantContextsMap = new HashMap<>();
    Map<String, Object> referencedSectionElementMap = new HashMap<>();
    Map<String, Object> referencedAttributeMap = new HashMap<>();
    Map<String, Object> referencedTagMap = new HashMap<>();
    Map<String, Object> referencedRoles = new HashMap<>();
    
    Map<String, Object> referencedPermissions = new HashMap<>();
    referencedPermissions.put(IBaseKlassTemplatePermissionModel.EDITABLE_PROPERTY_COLLECTION_IDS,
        new HashSet<String>());
    referencedPermissions.put(IBaseKlassTemplatePermissionModel.EXPANDABLE_PROPERTY_COLLECTION_IDS,
        new HashSet<String>());
    referencedPermissions.put(IBaseKlassTemplatePermissionModel.VISIBLE_PROPERTY_COLLECTION_IDS,
        new HashSet<String>());
    referencedPermissions.put(IBaseKlassTemplatePermissionModel.EDITABLE_PROPERTY_IDS,
        new HashSet<String>());
    referencedPermissions.put(IBaseKlassTemplatePermissionModel.VISIBLE_PROPERTY_IDS,
        new HashSet<String>());
    referencedPermissions.put(IBaseKlassTemplatePermissionModel.GLOBAL_PERMISSION, new HashMap<>());
    
    mapToReturn.put(
        IConfigDetailsForGetVariantInstancesInTableViewModel.REFERENCED_PROPERTY_COLLECTIONS,
        referencedPropertyCollectionMap);
    mapToReturn.put(
        IConfigDetailsForGetVariantInstancesInTableViewModel.REFERENCED_VARIANT_CONTEXTS,
        referencedVariantContextsMap);
    mapToReturn.put(IConfigDetailsForGetVariantInstancesInTableViewModel.REFERENCED_ELEMENTS,
        referencedSectionElementMap);
    mapToReturn.put(IConfigDetailsForGetVariantInstancesInTableViewModel.REFERENCED_ATTRIBUTES,
        referencedAttributeMap);
    mapToReturn.put(IConfigDetailsForGetVariantInstancesInTableViewModel.REFERENCED_TAGS,
        referencedTagMap);
    mapToReturn.put(IConfigDetailsForGetVariantInstancesInTableViewModel.REFERENCED_ROLES,
        referencedRoles);
    mapToReturn.put(IConfigDetailsForGetVariantInstancesInTableViewModel.REFERENCED_PERMISSIONS,
        referencedPermissions);
    
    return mapToReturn;
  }
  
  protected void fillReferencedPermission(Map<String, Object> responseMap, String contextId,
      Vertex userInRole, IGetConfigDetailsHelperModel helperModel) throws Exception
  {
    
    Map<String, Object> referencedPermissions = (Map<String, Object>) responseMap
        .get(IConfigDetailsForGetVariantInstancesInTableViewModel.REFERENCED_PERMISSIONS);
    Set<String> klassIdsHavingReadPermission = (Set<String>) referencedPermissions
        .get(IBaseKlassTemplatePermissionModel.KLASS_IDS_HAVING_RP);
    
    Vertex contextNode = UtilClass.getVertexById(contextId, VertexLabelConstants.VARIANT_CONTEXT);
    Iterator<Vertex> contextKlassIterator = contextNode
        .getVertices(Direction.IN, RelationshipLabelConstants.VARIANT_CONTEXT_OF)
        .iterator();
    Vertex contextKlassNode = contextKlassIterator.next();
    // Vertex contextTemplateNode =
    // TemplateUtils.getTemplateFromKlassIfExist(contextKlassNode);
    
    Map<String, Object> contextKlassPermissionMap = GlobalPermissionUtils
        .getDefaultGlobalPermission();
    String contextKlassId = UtilClass.getCodeNew(contextKlassNode);
    Map<String, Object> permissionMap = GlobalPermissionUtils
        .getKlassAndTaxonomyPermission(contextKlassId, UtilClass.getCodeNew(userInRole));
    contextKlassPermissionMap.putAll(permissionMap);
    /*
    fillPropertyCollectionPermissionAndEntitiesPermission(responseMap, helperModel,
        referencedPermissions, UtilClass.getCode(contextTemplateNode), UtilClass.getCode(userInRole));
        */
  }
  
  /*protected void fillPropertyCollectionPermissionAndEntitiesPermission(
        Map<String, Object> responseMap, IGetConfigDetailsHelperModel helperModel,
        Map<String, Object> referencedPermissions, String referencedTemplateId, String roleId)
        throws Exception
    {
      //get property collection id's from template and fill respective permission for them
      Map<String, Set<String>> templateIdVsAssociatedPropertyCollectionIds = helperModel.getTemplateIdVsAssociatedPropertyCollectionIds();
      Set<String> referencedPropertyCollection = templateIdVsAssociatedPropertyCollectionIds.get(referencedTemplateId);
      if (referencedPropertyCollection != null) {
        GlobalPermissionUtils.fillPropertyCollectionPermission(roleId, referencedTemplateId,
            referencedPropertyCollection, referencedPermissions);
      }
  
  
      // get property id's from template and fill respective permission for them
      Set<String> propertyIds = helperModel.getTemplateIdVsAssociatedPropertyIds().get(referencedTemplateId);
      GlobalPermissionUtils.fillPropertyPermission(roleId, referencedTemplateId, propertyIds, referencedPermissions);
    }
  */
}
