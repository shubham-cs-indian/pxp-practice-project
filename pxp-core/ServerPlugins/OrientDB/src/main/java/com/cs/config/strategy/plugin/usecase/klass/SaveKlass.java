package com.cs.config.strategy.plugin.usecase.klass;

import com.cs.config.strategy.plugin.usecase.klass.util.KlassGetUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.SaveKlassUtil;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.variantcontext.util.VariantContextUtils;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.klass.IKlassContext;
import com.cs.core.config.interactor.entity.relationship.IRelationship;
import com.cs.core.config.interactor.entity.relationship.IRelationshipSide;
import com.cs.core.config.interactor.model.klass.*;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import org.apache.commons.lang.mutable.MutableBoolean;

import java.util.*;

@SuppressWarnings("unchecked")
public class SaveKlass extends AbstractOrientPlugin {
  
  public SaveKlass(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    Map<String, Object> klassADM = new HashMap<String, Object>();
    Map<String, Object> returnKlassMap = null;
    List<Map<String,Object>> defaultValueChangeList = new ArrayList<>();
    Map<String,List<String>> deletedPropertyMap = new HashMap<>();
    Map<String, Object> propertiesADMMap = new HashMap<>();
    List<Long> removedContextClassifierIIDs = new ArrayList<>();
    propertiesADMMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.IS_INDENTIFIER_ATTRIBUTE_CHANGED, false); //Default vaalue for isIndentifierAttributeChanged
     
    klassADM = (Map<String, Object>) map.get("klass");
    OrientGraph graph = UtilClass.getGraph();
    UtilClass.setNodesForVersionIncrement(new HashSet<>());
    UtilClass.setSectionElementIdMap(new HashMap<>());
    UtilClass.setSectionIdMap(new HashMap<>());
    
    String klassId = (String) klassADM.get(IKlassSaveModel.ID);
    Vertex klassNode = null;
    try{
      klassNode = UtilClass.getVertexByIndexedId(klassId, VertexLabelConstants.ENTITY_TYPE_KLASS);
    }catch(Exception e)
    {
      throw new KlassNotFoundException();
    }
    
    Map<String, Map<String, Object>> sideInfoForDataTransfer = new HashMap<>();
    Map<String, Map<String, Object>> contextualDataTransfer = new HashMap<>(); 
    Map<String, Map<String, Object>> relationshipInheritance = new HashMap<>();
    MutableBoolean isRemoveTaxonomyConflictsRequired = new MutableBoolean();
    Map<String, Object> removedAttributeVariantContexts = new HashMap<>();
    List<Long> mandatoryPropertyUpdatedIIDs = new ArrayList<Long>();
    List<Long> propertyIIDsToEvaluateProductIdentifier = new ArrayList<Long>();
    List<Long> propertyIIDsToRemoveProductIdentifier = new ArrayList<Long>();
    List<String> addedCalculatedAttributeIds = new ArrayList<String>();
    
    removedAttributeVariantContexts.put(IRemoveAttributeVariantContextModel.REMOVED_ATTRIBUTE_ID_VS_CONTEXT_IDS, new HashMap<String, List<String>>());
    removedAttributeVariantContexts.put(IRemoveAttributeVariantContextModel.CHANGED_CLASSIFIERS_FOR_ATTRIBUTE_CONTEXTS, new ArrayList<String>());
    
    manageADMForKlass(klassADM, defaultValueChangeList, deletedPropertyMap, graph, klassNode,
        propertiesADMMap, contextualDataTransfer, sideInfoForDataTransfer, relationshipInheritance,
        isRemoveTaxonomyConflictsRequired, removedAttributeVariantContexts, removedContextClassifierIIDs,
        mandatoryPropertyUpdatedIIDs, propertyIIDsToEvaluateProductIdentifier,
        propertyIIDsToRemoveProductIdentifier, addedCalculatedAttributeIds);
    
    graph.commit();

    returnKlassMap = KlassGetUtils.getKlassEntityReferencesMap(klassNode, false);
    KlassGetUtils.fillReferencedConfigDetails(returnKlassMap, klassNode);
    
    AuditLogUtils.fillAuditLoginfo(returnKlassMap, klassNode , Entities.CLASSES, Elements.ARTICLE);
    
    //Fields needed for bulk propagation.
    returnKlassMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.REMOVED_ATTRIBUTE_VARIANT_CONTEXTS, removedAttributeVariantContexts);
    returnKlassMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.REMOVED_CONTEXT_CLASSIFIER_IIDS, removedContextClassifierIIDs);
    returnKlassMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.DEFAULT_VALUES_DIFF, defaultValueChangeList);
    returnKlassMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.DELETED_PROPERTIES_FROM_SOURCE, deletedPropertyMap);
    returnKlassMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.DELETED_NATURE_RELATIONSHIP_IDS, klassADM.get(IKlassSaveModel.DELETED_RELATIONSHIPS));
    returnKlassMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.ADDED_ELEMENTS, propertiesADMMap.get(IGetKlassWithGlobalPermissionModel.ADDED_ELEMENTS));
    returnKlassMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.MODIFIED_ELEMENTS, propertiesADMMap.get(IGetKlassWithGlobalPermissionModel.MODIFIED_ELEMENTS));
    returnKlassMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.DELETED_ELEMENTS, propertiesADMMap.get(IGetKlassWithGlobalPermissionModel.DELETED_ELEMENTS));
    returnKlassMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.IS_INDENTIFIER_ATTRIBUTE_CHANGED, propertiesADMMap.get(IGetKlassEntityWithoutKPStrategyResponseModel.IS_INDENTIFIER_ATTRIBUTE_CHANGED));
    returnKlassMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.CONTEXT_KLASS_SAVE_PROPERTIES_TO_DATA_TRANSFER, contextualDataTransfer.values());
    returnKlassMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.RELATIONSHIP_DATA_FOR_TRANSFER, sideInfoForDataTransfer.values());
    returnKlassMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.RELATIONSHIP_DATA_FOR_RELATIONSHIP_INHERITANCE, relationshipInheritance.values());
    returnKlassMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.IS_REMOVE_TAXONOMY_CONFLICTS_REQUIRED, isRemoveTaxonomyConflictsRequired.booleanValue());
    returnKlassMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.UPDATED_MANDATORY_PROPERTY_IIDS, mandatoryPropertyUpdatedIIDs);
    returnKlassMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.PROPERTY_IIDS_TO_EVALUATE_PRODUCT_IDENTIFIER, propertyIIDsToEvaluateProductIdentifier);
    returnKlassMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.PROPERTY_IIDS_TO_REMOVE_PRODUCT_IDENTIFIER, propertyIIDsToRemoveProductIdentifier);
    returnKlassMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.ADDED_CALCULATED_ATTRIBUTE_IDS, addedCalculatedAttributeIds);
    
    return returnKlassMap;
  }

  private void manageADMForKlass(Map<String, Object> klassADM, List<Map<String, Object>> defaultValueChangeList,
      Map<String, List<String>> deletedPropertyMap, OrientGraph graph, Vertex klassNode, Map<String, Object> propertiesADMMap,
      Map<String, Map<String, Object>> contextualDataTransfer, Map<String, Map<String, Object>> sideInfoForDataTransfer, 
      Map<String, Map<String, Object>> relationshipInheritance, MutableBoolean isRemoveTaxonomyConflictsRequired,
      Map<String, Object> removedAttributeVariantContexts, List<Long> removedContextClassifierIIDs,
      List<Long> mandatoryPropertyUpdatedIIDs, List<Long> propertyIIDsToEvaluateProductIdentifier,
      List<Long> propertyIIDsToRemoveProductIdentifier, List<String> addedCalculatedAttributeIds) throws Exception
  {
    Map<String, Object> relationshipExport = (Map<String, Object>) klassADM.remove(IKlassSaveModel.RELATIONSHIP_EXPORT);
    List<Vertex> klassAndChildNodes = KlassUtils.getKlassAndChildNodes(klassNode);
    SaveKlassUtil.manageLifeCycleStatusTag(klassADM, klassNode);
    SaveKlassUtil.manageSectionsInKlass(klassNode, klassADM, klassAndChildNodes,
        VertexLabelConstants.ENTITY_TYPE_KLASS, defaultValueChangeList, deletedPropertyMap,
        propertiesADMMap,removedAttributeVariantContexts, mandatoryPropertyUpdatedIIDs, propertyIIDsToEvaluateProductIdentifier,
        propertyIIDsToRemoveProductIdentifier, addedCalculatedAttributeIds);
        
    
    VariantContextUtils.manageLinkedLanguageKlass(klassADM, klassNode, contextualDataTransfer);
    manageLinkedGtinKlass(klassADM, klassNode, contextualDataTransfer, removedContextClassifierIIDs);
    VariantContextUtils.manageLinkedEmbeddedContextKlasses(klassADM, klassNode, contextualDataTransfer, removedContextClassifierIIDs);
    
    SaveKlassUtil.updateTemplateLabelIfKlassLabelChanged(klassADM, klassNode);
    
    List<String> addedProductVariants = new ArrayList<>();
    Map<String, List<String>> addedContexts = (Map<String, List<String>>) klassADM.get(IKlassSaveModel.ADDED_CONTEXTS);
    if (addedContexts != null) {
      addedProductVariants = addedContexts.remove(IKlassContext.PRODUCT_VARIANT_CONTEXTS);
    }
    List<String> deletedProductVariants = new ArrayList<>();
    Map<String, List<String>> deletedContexts = (Map<String, List<String>>) klassADM.get(IKlassSaveModel.DELETED_CONTEXTS);
    if (deletedContexts != null) {
      deletedProductVariants = deletedContexts.remove(IKlassContext.PRODUCT_VARIANT_CONTEXTS);
    }

    propertiesADMMap
        .putAll(SaveKlassUtil.manageKlassNatureInKlass(klassNode, klassADM, addedProductVariants,
            deletedProductVariants, sideInfoForDataTransfer, relationshipInheritance, isRemoveTaxonomyConflictsRequired));
    
    updateNatureRelationshipSideLabel(klassADM, klassNode);
    
    UtilClass.saveNode(klassADM, klassNode, propertiesToExclude);
    
    KlassUtils.manageDataRules(klassADM, klassNode, VertexLabelConstants.DATA_RULE, "klass");
    KlassUtils.manageVariantContext(klassADM, klassNode, VertexLabelConstants.VARIANT_CONTEXT);
    SaveKlassUtil.manageTasks(klassADM, klassNode);
    
    //Handling for relationships properties export
    if (relationshipExport != null) {
      KlassUtils.manageADMForPropertiesToExportForRelationship(klassNode, relationshipExport);
    }
  }

  /**
   * 
   * @author Lokesh
   * @param klassADM
   * @param klassNode
   * @param contextualDataTransfer 
   * @param removedContextClassifierIIDs 
   * @throws Exception
   */
  private void manageLinkedGtinKlass(Map<String, Object> klassADM, Vertex klassNode/*, Vertex templateNode*/, Map<String, Map<String, Object>> contextualDataTransfer, List<Long> removedContextClassifierIIDs) throws Exception
  {
    Map<String, Object> addedGtinKlass = (Map<String, Object>) klassADM.get(IProjectKlassSaveModel.ADDED_GTIN_KLASS);
    String deletedGtinKlass = (String) klassADM.get(IProjectKlassSaveModel.DELETED_GTIN_KLASS);
    Map<String, Object> modifiedGtinKlass = (Map<String, Object>) klassADM.get(IProjectKlassSaveModel.MODIFIED_GTIN_KLASS);
    
    List<Map<String, Object>> addedGtinKlassList = new ArrayList<>();
    if(addedGtinKlass != null && !addedGtinKlass.isEmpty()) {
      addedGtinKlassList.add(addedGtinKlass);
    }
    
    List<String> deletedGtinKlassList = new ArrayList<>();
    if(deletedGtinKlass != null && !deletedGtinKlass.isEmpty()) {
      deletedGtinKlassList.add(deletedGtinKlass);
    }
    
    List<Map<String, Object>> modifiedGtinKlassList = new ArrayList<>();
    if(modifiedGtinKlass != null && !modifiedGtinKlass.isEmpty()) {
      modifiedGtinKlassList.add(modifiedGtinKlass);
    }
    
    VariantContextUtils.manageLinkedContextKlasses(klassADM, klassNode, addedGtinKlassList, modifiedGtinKlassList,
        deletedGtinKlassList, contextualDataTransfer, removedContextClassifierIIDs);
    
  }

  /**
   * 
   * @author Lokesh
   * @param klassADM
   * @param klassNode
   */
  private void updateNatureRelationshipSideLabel(Map<String, Object> klassADM, Vertex klassNode)
  {
    String klassId = (String) klassADM.get(IKlassSaveModel.ID);
    String newLabel = (String) klassADM.get(IKlassSaveModel.LABEL);
    String oldLabel = (String) UtilClass.getValueByLanguage(klassNode, IKlass.LABEL);
    if(newLabel.equals(oldLabel))
    {
      return;
    }
    
    String query = "select from " + VertexLabelConstants.KLASS_RELATIONSHIP + " Where "
        + "in('" + RelationshipLabelConstants.HAS_KLASS_PROPERTY + "').cid = '" + klassId + "'"
        + "and out('" + RelationshipLabelConstants.HAS_PROPERTY + "').@class contains '" + VertexLabelConstants.NATURE_RELATIONSHIP + "'";
    
    Iterable<Vertex> kRVertices = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    
    for (Vertex kR : kRVertices) {
      Vertex natureRelarionshipNode = kR.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY).iterator().next();
      Map<String, Object> side2 = natureRelarionshipNode.getProperty(IRelationship.SIDE2);
      side2.put(IRelationshipSide.LABEL, newLabel);
      natureRelarionshipNode.setProperty(IRelationship.SIDE2, side2);
      Iterator<Vertex> kRIterator = natureRelarionshipNode.getVertices(Direction.IN, RelationshipLabelConstants.HAS_PROPERTY).iterator();
      Vertex kNR = kRIterator.next();
      if(kNR.equals(kR)) {
        kNR = kRIterator.next();
      }
      Map<String, Object> relationshipSide = kNR.getProperty(CommonConstants.RELATIONSHIP_SIDE);
      relationshipSide.put(IRelationshipSide.LABEL, newLabel);
      kNR.setProperty(CommonConstants.RELATIONSHIP_SIDE, relationshipSide);
    }
    
    Iterable<Vertex> kNRVertices = klassNode.getVertices(Direction.OUT, RelationshipLabelConstants.KLASS_NATURE_RELATIONSHIP_OF);
    
    for (Vertex kNR : kNRVertices) {
      Vertex natureRelarionshipNode = kNR.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_PROPERTY).iterator().next();
      Map<String, Object> side1 = natureRelarionshipNode.getProperty(IRelationship.SIDE1);
      side1.put(IRelationshipSide.LABEL, newLabel);
      natureRelarionshipNode.setProperty(IRelationship.SIDE1, side1);
      Iterator<Vertex> kRIterator = natureRelarionshipNode.getVertices(Direction.IN, RelationshipLabelConstants.HAS_PROPERTY).iterator();
      if (!kRIterator.hasNext()) {
        continue;
      }
      Vertex kR = kRIterator.next();
      if(kR.equals(kNR)) {
        kR = kRIterator.next();
      }
      Map<String, Object> relationshipSide = kR.getProperty(CommonConstants.RELATIONSHIP_SIDE);
      relationshipSide.put(IRelationshipSide.LABEL, newLabel);
      kR.setProperty(CommonConstants.RELATIONSHIP_SIDE, relationshipSide);
    }
  }
 
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveKlass/*" };
  }
  
  public static List<String> propertiesToExclude = Arrays.asList(IKlassSaveModel.PERMISSIONS,
      IKlassSaveModel.ADDED_DATA_RULES, IKlassSaveModel.DELETED_DATA_RULES,
      IKlassSaveModel.CHILDREN, IKlassSaveModel.NOTIFICATION_SETTINGS,
      CommonConstants.TREE_TYPE_OPTION_PROPERTY, IKlassSaveModel.IS_STANDARD,
      IKlassSaveModel.ADDED_CONTEXTS, IKlassSaveModel.DELETED_CONTEXTS,
      IKlassSaveModel.RELATIONSHIPS, IKlassSaveModel.ADDED_RELATIONSHIPS,
      IKlassSaveModel.MODIFIED_RELATIONSHIPS, IKlassSaveModel.DELETED_RELATIONSHIPS,
      IKlassSaveModel.ADDED_LIFECYCLE_STATUS_TAGS, IKlassSaveModel.DELETED_LIFECYCLE_STATUS_TAGS,
      IKlassSaveModel.REFERENCED_TAGS, IKlassSaveModel.ADDED_TASKS, IKlassSaveModel.DELETED_TASKS,
      CommonConstants.GLOBAL_PERMISSION_PROPERTY, IKlassSaveModel.ADDED_CONTEXT_KLASSES,
      IKlassSaveModel.DELETED_CONTEXT_KLASSES, IKlass.EMBEDDED_KLASS_IDS,IKlassSaveModel.MODIFIED_CONTEXT_KLASSES,
      IGetKlassEntityWithoutKPStrategyResponseModel.CONTEXT_KLASS_SAVE_PROPERTIES_TO_DATA_TRANSFER,
      IProjectKlassSaveModel.ADDED_GTIN_KLASS, IProjectKlassSaveModel.DELETED_GTIN_KLASS,
      IKlassSaveModel.ADDED_LANGUAGE_KLASS,IKlassSaveModel.MODIFIED_LANGUAGE_KLASS,IKlassSaveModel.DELETED_LANGUAGE_KLASS);
}
