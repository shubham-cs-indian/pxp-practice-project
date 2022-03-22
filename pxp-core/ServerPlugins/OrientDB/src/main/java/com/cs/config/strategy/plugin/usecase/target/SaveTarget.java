package com.cs.config.strategy.plugin.usecase.target;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.klass.util.KlassGetUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.SaveKlassUtil;
import com.cs.config.strategy.plugin.usecase.target.util.TargetUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.variantcontext.util.VariantContextUtils;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPStrategyResponseModel;
import com.cs.core.config.interactor.model.klass.IKlassSaveModel;
import com.cs.core.config.interactor.model.klass.IRemoveAttributeVariantContextModel;
import com.cs.core.config.interactor.model.target.ITargetKlassSaveModel;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class SaveTarget extends AbstractOrientPlugin {
  
  public SaveTarget(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> targetADM = (Map<String, Object>) requestMap.get("target");
    ;
    Map<String, Object> returnMap = new HashMap<String, Object>();
    List<Map<String, Object>> defaultValueChangeList = new ArrayList<>();
    Map<String, List<String>> deletedPropertyMap = new HashMap<>();
    Map<String, Object> propertiesADMMap = new HashMap<>();
    propertiesADMMap
        .put(IGetKlassEntityWithoutKPStrategyResponseModel.IS_INDENTIFIER_ATTRIBUTE_CHANGED, false); // Default
                                                                                                     // vaalue
                                                                                                     // for
                                                                                                     // isIndentifierAttributeChanged
    
    UtilClass.setSectionElementIdMap(new HashMap<>());
    UtilClass.setSectionIdMap(new HashMap<>());
    UtilClass.setNodesForVersionIncrement(new HashSet<>());
    
    String targetId = (String) targetADM.get(ITargetKlassSaveModel.ID);
    Vertex targetNode = null;
    try {
      targetNode = UtilClass.getVertexById(targetId, VertexLabelConstants.ENTITY_TYPE_TARGET);
    }
    catch (Exception e) {
      throw new KlassNotFoundException();
    }
    Map<String, Object> removedAttributeVariantContexts = new HashMap<>();
    List<Long> removedContextClassifierIIDs = new ArrayList<>();
    List<Long> mandatoryPropertyUpdatedIIDs = new ArrayList<Long>();
    List<Long> propertyIIDsToEvaluateProductIdentifier = new ArrayList<Long>();
    List<Long> propertyIIDsToRemoveProductIdentifier = new ArrayList<Long>();
    List<String> addedCalculatedAttributeIds = new ArrayList<String>();
    
    removedAttributeVariantContexts.put(IRemoveAttributeVariantContextModel.REMOVED_ATTRIBUTE_ID_VS_CONTEXT_IDS, new HashMap<String, List<String>>());
    removedAttributeVariantContexts.put(IRemoveAttributeVariantContextModel.CHANGED_CLASSIFIERS_FOR_ATTRIBUTE_CONTEXTS, new ArrayList<String>());
    Map<String, Map<String, Object>> contextualDataTransfer = new HashMap<>();
  
    manageADMForTarget(targetADM, defaultValueChangeList, deletedPropertyMap, targetNode,
        propertiesADMMap, contextualDataTransfer, removedAttributeVariantContexts,
        removedContextClassifierIIDs, mandatoryPropertyUpdatedIIDs, propertyIIDsToEvaluateProductIdentifier,
        propertyIIDsToRemoveProductIdentifier, addedCalculatedAttributeIds);
    
    UtilClass.saveNode(targetADM, targetNode, propertiesToExclude);
    
    UtilClass.getGraph()
        .commit();
    
    Map<String, Object> targetMap = TargetUtils.getTargetEntityMap(targetNode, false);
    returnMap.put(IGetKlassEntityWithoutKPModel.ENTITY, targetMap);
    KlassGetUtils.fillReferencedConfigDetails(returnMap, targetNode);
    
    returnMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.DEFAULT_VALUES_DIFF,
        defaultValueChangeList);
    returnMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.DELETED_PROPERTIES_FROM_SOURCE,
        deletedPropertyMap);
    returnMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.IS_INDENTIFIER_ATTRIBUTE_CHANGED,
        propertiesADMMap
            .get(IGetKlassEntityWithoutKPStrategyResponseModel.IS_INDENTIFIER_ATTRIBUTE_CHANGED));
    requestMap.put(
        IGetKlassEntityWithoutKPStrategyResponseModel.CONTEXT_KLASS_SAVE_PROPERTIES_TO_DATA_TRANSFER,
        contextualDataTransfer.values());
    returnMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.UPDATED_MANDATORY_PROPERTY_IIDS, mandatoryPropertyUpdatedIIDs);
    returnMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.PROPERTY_IIDS_TO_EVALUATE_PRODUCT_IDENTIFIER, propertyIIDsToEvaluateProductIdentifier);
    returnMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.PROPERTY_IIDS_TO_REMOVE_PRODUCT_IDENTIFIER, propertyIIDsToRemoveProductIdentifier);
    returnMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.REMOVED_CONTEXT_CLASSIFIER_IIDS, removedContextClassifierIIDs);
    returnMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.REMOVED_ATTRIBUTE_VARIANT_CONTEXTS, removedAttributeVariantContexts);
    returnMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.ADDED_CALCULATED_ATTRIBUTE_IDS, addedCalculatedAttributeIds);
    
    AuditLogUtils.fillAuditLoginfo(returnMap, targetNode, Entities.CLASSES, Elements.MARKET);
    return returnMap;
  }
  
  private void manageADMForTarget(Map<String, Object> targetADM,
      List<Map<String, Object>> defaultValueChangeList,
      Map<String, List<String>> deletedPropertyMap, Vertex targetNode,
      Map<String, Object> propertiesADMMap, Map<String, Map<String, Object>> contextualDataTransfer,
      Map<String, Object> removedAttributeVariantContexts, List<Long> removedContextClassifierIIDs,
      List<Long> mandatoryPropertyUpdatedIIDs, List<Long> propertyIIDsToEvaluateProductIdentifier,
      List<Long> propertyIIDsToRemoveProductIdentifier, List<String> addedCalculatedAttributeIds)
      throws Exception
  {
    List<Vertex> targetAndChildNodes = KlassUtils.getKlassAndChildNodes(targetNode);
    SaveKlassUtil.manageLifeCycleStatusTag(targetADM, targetNode);
    SaveKlassUtil.manageTreeTypeOption(targetADM, targetNode, targetAndChildNodes);
    SaveKlassUtil.manageSectionsInKlass(targetNode, targetADM, targetAndChildNodes,
        VertexLabelConstants.ENTITY_TYPE_TARGET, defaultValueChangeList, deletedPropertyMap,
        propertiesADMMap, removedAttributeVariantContexts, mandatoryPropertyUpdatedIIDs,
        propertyIIDsToEvaluateProductIdentifier, propertyIIDsToRemoveProductIdentifier, addedCalculatedAttributeIds);
    KlassUtils.manageDataRules(targetADM, targetNode, VertexLabelConstants.DATA_RULE, "klass");
    KlassUtils.manageVariantContext(targetADM, targetNode, VertexLabelConstants.VARIANT_CONTEXT);
    SaveKlassUtil.manageTasks(targetADM, targetNode);
    SaveKlassUtil.updateTemplateLabelIfKlassLabelChanged(targetADM, targetNode);
    VariantContextUtils.manageLinkedLanguageKlass(targetADM, targetNode, contextualDataTransfer);
    VariantContextUtils.manageLinkedEmbeddedContextKlasses(targetADM, targetNode,
        contextualDataTransfer, removedContextClassifierIIDs);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveTarget/*" };
  }
  
  public static List<String> propertiesToExclude = Arrays.asList(IKlassSaveModel.ADDED_DATA_RULES,
      IKlassSaveModel.DELETED_DATA_RULES, CommonConstants.TREE_TYPE_OPTION_PROPERTY,
      IKlassSaveModel.IS_STANDARD, IKlassSaveModel.ADDED_CONTEXTS, IKlassSaveModel.DELETED_CONTEXTS,
      IKlassSaveModel.ADDED_RELATIONSHIPS, IKlassSaveModel.MODIFIED_RELATIONSHIPS,
      IKlassSaveModel.DELETED_RELATIONSHIPS, IKlassSaveModel.ADDED_LIFECYCLE_STATUS_TAGS,
      IKlassSaveModel.DELETED_LIFECYCLE_STATUS_TAGS,
      IKlassSaveModel.REFERENCED_TAGS, /*IKlassSaveModel.IS_TYPE_CHANGED,*/
      IKlassSaveModel.ADDED_TASKS, IKlassSaveModel.DELETED_TASKS,
      IGetKlassEntityWithoutKPStrategyResponseModel.CONTEXT_KLASS_SAVE_PROPERTIES_TO_DATA_TRANSFER,
      CommonConstants.GLOBAL_PERMISSION_PROPERTY, IKlassSaveModel.ADDED_CONTEXT_KLASSES,
      IKlassSaveModel.DELETED_CONTEXT_KLASSES, IKlassSaveModel.EMBEDDED_KLASS_IDS,
      IKlassSaveModel.MODIFIED_CONTEXT_KLASSES, IKlassSaveModel.ADDED_LANGUAGE_KLASS,
      IKlassSaveModel.MODIFIED_LANGUAGE_KLASS, IKlassSaveModel.DELETED_LANGUAGE_KLASS);
}
