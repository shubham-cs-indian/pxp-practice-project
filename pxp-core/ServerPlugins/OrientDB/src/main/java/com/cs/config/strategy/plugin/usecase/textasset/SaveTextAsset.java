package com.cs.config.strategy.plugin.usecase.textasset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.klass.util.KlassGetUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.SaveKlassUtil;
import com.cs.config.strategy.plugin.usecase.textasset.util.TextAssetUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.variantcontext.util.VariantContextUtils;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPStrategyResponseModel;
import com.cs.core.config.interactor.model.klass.IKlassSaveModel;
import com.cs.core.config.interactor.model.klass.IRemoveAttributeVariantContextModel;
import com.cs.core.config.interactor.model.textasset.ITextAssetKlassSaveModel;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class SaveTextAsset extends AbstractOrientPlugin {
  
  public SaveTextAsset(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    Map<String, Object> textAssetADM = (Map<String, Object>) requestMap.get("textasset");
    Map<String, Object> returnKlassMap = new HashMap<String, Object>();
    List<Map<String, Object>> defaultValueChangeList = new ArrayList<>();
    Map<String, List<String>> deletedPropertyMap = new HashMap<>();
    List<Long> removedContextClassifierIIDs = new ArrayList<>();

    Map<String, Object> propertiesADMMap = new HashMap<>();
    propertiesADMMap
        .put(IGetKlassEntityWithoutKPStrategyResponseModel.IS_INDENTIFIER_ATTRIBUTE_CHANGED, false); // Default
                                                                                                     // vaalue
                                                                                                     // for
                                                                                                     // isIndentifierAttributeChanged
    
    UtilClass.setSectionElementIdMap(new HashMap<>());
    UtilClass.setSectionIdMap(new HashMap<>());
    UtilClass.setNodesForVersionIncrement(new HashSet<>());
    
    String klassId = (String) textAssetADM.get(CommonConstants.ID_PROPERTY);
    Vertex textAssetNode = null;
    try {
      textAssetNode = UtilClass.getVertexById(klassId, VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET);
    }
    catch (Exception e) {
      throw new KlassNotFoundException();
    }
    
    Map<String, Map<String, Object>> contextualDataTransfer = new HashMap<>();
    Map<String, Object> removedAttributeVariantContexts = new HashMap<>();
    List<Long> mandatoryPropertyUpdatedIIDs = new ArrayList<Long>();
    List<Long> propertyIIDsToEvaluateProductIdentifier = new ArrayList<Long>();
    List<Long> propertyIIDsToRemoveProductIdentifier = new ArrayList<Long>();
    List<String> addedCalculatedAttributeIds = new ArrayList<String>();
    
    removedAttributeVariantContexts.put(IRemoveAttributeVariantContextModel.REMOVED_ATTRIBUTE_ID_VS_CONTEXT_IDS, new HashMap<String, List<String>>());
    removedAttributeVariantContexts.put(IRemoveAttributeVariantContextModel.CHANGED_CLASSIFIERS_FOR_ATTRIBUTE_CONTEXTS, new ArrayList<String>()); 
    
    manageADMForTextAsset(textAssetADM, defaultValueChangeList, deletedPropertyMap, textAssetNode,
        propertiesADMMap, contextualDataTransfer, removedAttributeVariantContexts,
        removedContextClassifierIIDs, mandatoryPropertyUpdatedIIDs, propertyIIDsToEvaluateProductIdentifier,
        propertyIIDsToRemoveProductIdentifier, addedCalculatedAttributeIds);
    
    AuditLogUtils.fillAuditLoginfo(returnKlassMap, textAssetNode, Entities.CLASSES, Elements.TEXT_ASSET);

    UtilClass.getGraph()
        .commit();
    
    Map<String, Object> classMap = TextAssetUtils.getTextAssetEntityMap(textAssetNode, false);
    returnKlassMap.put(IGetKlassEntityWithoutKPModel.ENTITY, classMap);
    KlassGetUtils.fillReferencedConfigDetails(returnKlassMap, textAssetNode);
   
    returnKlassMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.REMOVED_ATTRIBUTE_VARIANT_CONTEXTS, removedAttributeVariantContexts);
    returnKlassMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.DEFAULT_VALUES_DIFF,
        defaultValueChangeList);
    returnKlassMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.DELETED_PROPERTIES_FROM_SOURCE,
        deletedPropertyMap);
    returnKlassMap.put(
        IGetKlassEntityWithoutKPStrategyResponseModel.IS_INDENTIFIER_ATTRIBUTE_CHANGED,
        propertiesADMMap
            .get(IGetKlassEntityWithoutKPStrategyResponseModel.IS_INDENTIFIER_ATTRIBUTE_CHANGED));
    returnKlassMap.put(
        IGetKlassEntityWithoutKPStrategyResponseModel.CONTEXT_KLASS_SAVE_PROPERTIES_TO_DATA_TRANSFER,
        contextualDataTransfer.values());
    returnKlassMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.UPDATED_MANDATORY_PROPERTY_IIDS, mandatoryPropertyUpdatedIIDs);
    returnKlassMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.PROPERTY_IIDS_TO_EVALUATE_PRODUCT_IDENTIFIER, propertyIIDsToEvaluateProductIdentifier);
    returnKlassMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.PROPERTY_IIDS_TO_REMOVE_PRODUCT_IDENTIFIER, propertyIIDsToRemoveProductIdentifier);
    returnKlassMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.REMOVED_CONTEXT_CLASSIFIER_IIDS, removedContextClassifierIIDs);
    returnKlassMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.REMOVED_ATTRIBUTE_VARIANT_CONTEXTS, removedAttributeVariantContexts);
    returnKlassMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.ADDED_CALCULATED_ATTRIBUTE_IDS, addedCalculatedAttributeIds);
    
    return returnKlassMap;
  }
  
  private void manageADMForTextAsset(Map<String, Object> textAssetADM,
      List<Map<String, Object>> defaultValueChangeList,
      Map<String, List<String>> deletedPropertyMap, Vertex textAssetNode,
      Map<String, Object> propertiesADMMap, Map<String, Map<String, Object>> contextualDataTransfer, Map<String, Object> removedAttributeVariantContexts,
      List<Long> removedContextClassifierIIDs, List<Long> mandatoryPropertyUpdatedIIDs, List<Long> propertyIIDsToEvaluateProductIdentifier,
      List<Long> propertyIIDsToRemoveProductIdentifier, List<String> addedCalculatedAttributeIds)
      throws Exception
  {
    List<Vertex> klassAndChildNodes = KlassUtils.getKlassAndChildNodes(textAssetNode);
    
    SaveKlassUtil.manageLifeCycleStatusTag(textAssetADM, textAssetNode);
    SaveKlassUtil.manageTreeTypeOption(textAssetADM, textAssetNode, klassAndChildNodes);
    SaveKlassUtil.manageSectionsInKlass(textAssetNode, textAssetADM, klassAndChildNodes,
        VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET, defaultValueChangeList, deletedPropertyMap,
        propertiesADMMap, removedAttributeVariantContexts, mandatoryPropertyUpdatedIIDs,
        propertyIIDsToEvaluateProductIdentifier, propertyIIDsToRemoveProductIdentifier, addedCalculatedAttributeIds);
    
    SaveKlassUtil.updateTemplateLabelIfKlassLabelChanged(textAssetADM, textAssetNode);
    KlassUtils.manageDataRules(textAssetADM, textAssetNode, VertexLabelConstants.DATA_RULE,
        "klass");
    KlassUtils.manageVariantContext(textAssetADM, textAssetNode,
        VertexLabelConstants.VARIANT_CONTEXT);
    SaveKlassUtil.manageTasks(textAssetADM, textAssetNode);
    VariantContextUtils.manageLinkedLanguageKlass(textAssetADM, textAssetNode,
        contextualDataTransfer);
    VariantContextUtils.manageLinkedEmbeddedContextKlasses(textAssetADM, textAssetNode,
        contextualDataTransfer,removedContextClassifierIIDs);
    
    UtilClass.saveNode(textAssetADM, textAssetNode, propertiesToExclude);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveTextAsset/*" };
  }
  
  public static List<String> propertiesToExclude = Arrays.asList(
      ITextAssetKlassSaveModel.ADDED_DATA_RULES, ITextAssetKlassSaveModel.DELETED_DATA_RULES,
      CommonConstants.TREE_TYPE_OPTION_PROPERTY, ITextAssetKlassSaveModel.IS_STANDARD,
      "deletedAllowedTypes", "addedAllowedTypes", IKlassSaveModel.ADDED_CONTEXTS,
      IKlassSaveModel.DELETED_CONTEXTS, IKlassSaveModel.ADDED_RELATIONSHIPS,
      IKlassSaveModel.MODIFIED_RELATIONSHIPS, IKlassSaveModel.DELETED_RELATIONSHIPS,
      IKlassSaveModel.ADDED_LIFECYCLE_STATUS_TAGS, IKlassSaveModel.DELETED_LIFECYCLE_STATUS_TAGS,
      IKlassSaveModel.REFERENCED_TAGS, /*IKlassSaveModel.IS_TYPE_CHANGED,*/
      IKlassSaveModel.ADDED_TASKS, IKlassSaveModel.DELETED_TASKS,
      IGetKlassEntityWithoutKPStrategyResponseModel.CONTEXT_KLASS_SAVE_PROPERTIES_TO_DATA_TRANSFER,
      CommonConstants.GLOBAL_PERMISSION_PROPERTY, IKlassSaveModel.ADDED_CONTEXT_KLASSES,
      IKlassSaveModel.DELETED_CONTEXT_KLASSES, IKlassSaveModel.EMBEDDED_KLASS_IDS,
      IKlassSaveModel.MODIFIED_CONTEXT_KLASSES, IKlassSaveModel.ADDED_LANGUAGE_KLASS,
      IKlassSaveModel.MODIFIED_LANGUAGE_KLASS, IKlassSaveModel.DELETED_LANGUAGE_KLASS);
}
