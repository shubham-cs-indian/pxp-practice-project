package com.cs.config.strategy.plugin.usecase.klass.supplier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.klass.supplier.util.SupplierUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassGetUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.SaveKlassUtil;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.variantcontext.util.VariantContextUtils;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPStrategyResponseModel;
import com.cs.core.config.interactor.model.klass.IKlassSaveModel;
import com.cs.core.config.interactor.model.klass.IRemoveAttributeVariantContextModel;
import com.cs.core.config.interactor.model.supplier.ISupplierKlassSaveModel;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

@SuppressWarnings("unchecked")
public class SaveSupplier extends AbstractOrientPlugin {
  
  public SaveSupplier(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> map) throws Exception
  {
    HashMap<String, Object> supplierADM = new HashMap<String, Object>();
    Map<String, Object> returnKlassMap = new HashMap<String, Object>();
    List<Map<String, Object>> defaultValueChangeList = new ArrayList<>();
    Map<String, List<String>> deletedPropertyMap = new HashMap<>();
    Map<String, Object> propertiesADMMap = new HashMap<>();
    propertiesADMMap
        .put(IGetKlassEntityWithoutKPStrategyResponseModel.IS_INDENTIFIER_ATTRIBUTE_CHANGED, false); // Default
                                                                                                     // vaalue
                                                                                                     // for
                                                                                                     // isIndentifierAttributeChanged
    OrientGraph graph = UtilClass.getGraph();
    supplierADM = (HashMap<String, Object>) map.get("supplier");
    
    UtilClass.setSectionElementIdMap(new HashMap<>());
    UtilClass.setSectionIdMap(new HashMap<>());
    UtilClass.setNodesForVersionIncrement(new HashSet<>());
    
    String klassId = (String) supplierADM.get(CommonConstants.ID_PROPERTY);
    Vertex supplierNode = null;
    try {
      supplierNode = UtilClass.getVertexById(klassId, VertexLabelConstants.ENTITY_TYPE_SUPPLIER);
    }
    catch (Exception e) {
      throw new KlassNotFoundException();
    }
    
    Map<String, Map<String, Object>> contextualDataTransfer = new HashMap<>();
    Map<String, Object> removedAttributeVariantContexts = new HashMap<>();
    List<Long> removedContextClassifierIIDs = new ArrayList<>();
    List<Long> mandatoryPropertyUpdatedIIDs = new ArrayList<Long>();
    List<Long> propertyIIDsToEvaluateProductIdentifier = new ArrayList<Long>();
    List<Long> propertyIIDsToRemoveProductIdentifier = new ArrayList<Long>();
    List<String> addedCalculatedAttributeIds = new ArrayList<String>();
    
    removedAttributeVariantContexts.put(IRemoveAttributeVariantContextModel.REMOVED_ATTRIBUTE_ID_VS_CONTEXT_IDS, new HashMap<String, List<String>>());
    removedAttributeVariantContexts.put(IRemoveAttributeVariantContextModel.CHANGED_CLASSIFIERS_FOR_ATTRIBUTE_CONTEXTS, new ArrayList<String>());
    
    manageADMForSupplier(supplierADM, defaultValueChangeList, deletedPropertyMap, supplierNode,
        propertiesADMMap, contextualDataTransfer, removedAttributeVariantContexts,
        removedContextClassifierIIDs, mandatoryPropertyUpdatedIIDs, propertyIIDsToEvaluateProductIdentifier,
        propertyIIDsToRemoveProductIdentifier, addedCalculatedAttributeIds);
    
    UtilClass.saveNode(supplierADM, supplierNode, propertiesToExclude);
    
    graph.commit();
    Map<String, Object> classMap = SupplierUtils.getSupplierEntityMap(supplierNode, false);
    returnKlassMap.put(IGetKlassEntityWithoutKPModel.ENTITY, classMap);
    
    KlassGetUtils.fillReferencedConfigDetails(returnKlassMap, supplierNode);
    
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
    AuditLogUtils.fillAuditLoginfo(returnKlassMap, supplierNode, Entities.CLASSES, Elements.SUPPLIER);

    returnKlassMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.UPDATED_MANDATORY_PROPERTY_IIDS, mandatoryPropertyUpdatedIIDs);
    returnKlassMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.PROPERTY_IIDS_TO_EVALUATE_PRODUCT_IDENTIFIER, propertyIIDsToEvaluateProductIdentifier);
    returnKlassMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.PROPERTY_IIDS_TO_REMOVE_PRODUCT_IDENTIFIER, propertyIIDsToRemoveProductIdentifier);
    returnKlassMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.REMOVED_CONTEXT_CLASSIFIER_IIDS, removedContextClassifierIIDs);
    returnKlassMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.REMOVED_ATTRIBUTE_VARIANT_CONTEXTS, removedAttributeVariantContexts);
    returnKlassMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.ADDED_CALCULATED_ATTRIBUTE_IDS, addedCalculatedAttributeIds);
    
    return returnKlassMap;
  }
  
  private void manageADMForSupplier(HashMap<String, Object> supplierADM,
      List<Map<String, Object>> defaultValueChangeList,
      Map<String, List<String>> deletedPropertyMap, Vertex supplierNode,
      Map<String, Object> propertiesADMMap, Map<String, Map<String, Object>> contextualDataTransfer,
      Map<String, Object> removedAttributeVariantContexts, List<Long> removedContextClassifierIIDs,
      List<Long> mandatoryPropertyUpdatedIIDs, List<Long> propertyIIDsToEvaluateProductIdentifier,
      List<Long> propertyIIDsToRemoveProductIdentifier, List<String> addedCalculatedAttributeIds)
      throws Exception
  {
    List<Vertex> klassAndChildNodes = KlassUtils.getKlassAndChildNodes(supplierNode);
    
    SaveKlassUtil.manageLifeCycleStatusTag(supplierADM, supplierNode);
    SaveKlassUtil.manageTreeTypeOption(supplierADM, supplierNode, klassAndChildNodes);
    SaveKlassUtil.manageSectionsInKlass(supplierNode, supplierADM, klassAndChildNodes, VertexLabelConstants.ENTITY_TYPE_SUPPLIER,
        defaultValueChangeList, deletedPropertyMap, propertiesADMMap, removedAttributeVariantContexts, mandatoryPropertyUpdatedIIDs,
        propertyIIDsToEvaluateProductIdentifier, propertyIIDsToRemoveProductIdentifier, addedCalculatedAttributeIds);
    
    VariantContextUtils.manageLinkedLanguageKlass(supplierADM, supplierNode,
        contextualDataTransfer);
    VariantContextUtils.manageLinkedEmbeddedContextKlasses(supplierADM, supplierNode,
        contextualDataTransfer, removedContextClassifierIIDs);
    
    KlassUtils.manageDataRules(supplierADM, supplierNode, VertexLabelConstants.DATA_RULE, "klass");
    KlassUtils.manageVariantContext(supplierADM, supplierNode,
        VertexLabelConstants.VARIANT_CONTEXT);
    SaveKlassUtil.manageTasks(supplierADM, supplierNode);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveSupplier/*" };
  }
  
  public static List<String> propertiesToExclude = Arrays.asList(
      ISupplierKlassSaveModel.ADDED_DATA_RULES, ISupplierKlassSaveModel.DELETED_DATA_RULES,
      CommonConstants.TREE_TYPE_OPTION_PROPERTY, ISupplierKlassSaveModel.IS_STANDARD,
      "deletedAllowedTypes", "addedAllowedTypes", IKlassSaveModel.ADDED_CONTEXTS,
      IKlassSaveModel.DELETED_CONTEXTS, IKlassSaveModel.ADDED_RELATIONSHIPS,
      IKlassSaveModel.MODIFIED_RELATIONSHIPS, IKlassSaveModel.DELETED_RELATIONSHIPS,IKlassSaveModel.REFERENCED_TAGS,
      IKlassSaveModel.DELETED_LIFECYCLE_STATUS_TAGS, IKlassSaveModel.ADDED_LIFECYCLE_STATUS_TAGS,
      /*IKlassSaveModel.IS_TYPE_CHANGED,*/ IKlassSaveModel.ADDED_TASKS,
      IGetKlassEntityWithoutKPStrategyResponseModel.CONTEXT_KLASS_SAVE_PROPERTIES_TO_DATA_TRANSFER,
      IKlassSaveModel.DELETED_TASKS, CommonConstants.GLOBAL_PERMISSION_PROPERTY,
      IKlassSaveModel.ADDED_CONTEXT_KLASSES, IKlassSaveModel.DELETED_CONTEXT_KLASSES,
      IKlassSaveModel.EMBEDDED_KLASS_IDS, IKlassSaveModel.MODIFIED_CONTEXT_KLASSES,
      IKlassSaveModel.ADDED_LANGUAGE_KLASS, IKlassSaveModel.MODIFIED_LANGUAGE_KLASS,
      IKlassSaveModel.DELETED_LANGUAGE_KLASS);
}
