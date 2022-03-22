package com.cs.config.strategy.plugin.usecase.asset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.mutable.MutableBoolean;

import com.cs.config.strategy.plugin.usecase.asset.util.AssetUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassGetUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.SaveKlassUtil;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.variantcontext.util.VariantContextUtils;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.klass.IAsset;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.klass.IKlassContext;
import com.cs.core.config.interactor.model.asset.IAssetExtensionConfigurationModel;
import com.cs.core.config.interactor.model.asset.IAssetKlassSaveModel;
import com.cs.core.config.interactor.model.asset.IAssetModel;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPModel;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPStrategyResponseModel;
import com.cs.core.config.interactor.model.klass.IKlassSaveModel;
import com.cs.core.config.interactor.model.klass.IRemoveAttributeVariantContextModel;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.exception.assetserver.ExtensionAlreadyExistsException;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

@SuppressWarnings("unchecked")
public class SaveAsset extends AbstractOrientPlugin {
  
  public SaveAsset(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> map) throws Exception
  {
    
    HashMap<String, Object> assetADM = new HashMap<String, Object>();
    HashMap<String, Object> returnMap = new HashMap<String, Object>();
    List<Map<String, Object>> defaultValueChangeList = new ArrayList<>();
    Map<String, List<String>> deletedPropertyMap = new HashMap<>();
    List<Long> removedContextClassifierIIDs = new ArrayList<>();

    
    Map<String, Object> propertiesADMMap = new HashMap<>();
    propertiesADMMap
        .put(IGetKlassEntityWithoutKPStrategyResponseModel.IS_INDENTIFIER_ATTRIBUTE_CHANGED, false); // Default
                                                                                                     // vaalue
                                                                                                     // for
                                                                                                     // isIndentifierAttributeChanged
    Map<String, Map<String, Object>> sideInfoForDataTransfer = new HashMap<>();
    
    assetADM = (HashMap<String, Object>) map.get("asset");
    
    OrientGraph graph = UtilClass.getGraph();
    UtilClass.setSectionElementIdMap(new HashMap<>());
    UtilClass.setSectionIdMap(new HashMap<>());
    UtilClass.setNodesForVersionIncrement(new HashSet<>());
    
    String assetId = (String) assetADM.get(IAssetKlassSaveModel.ID);
    Vertex assetNode = null;
    try {
      assetNode = UtilClass.getVertexById(assetId, VertexLabelConstants.ENTITY_TYPE_ASSET);
    }
    catch (Exception e) {
      throw new KlassNotFoundException();
    }
    Map<String, Map<String, Object>> contextualDataTransfer = new HashMap<>();
    Map<String, Map<String, Object>> relationshipInheritance = new HashMap<>();
    Map<String, Object> removedAttributeVariantContexts = new HashMap<>();
    MutableBoolean isRemoveTaxonomyConflictsRequired = new MutableBoolean();
    List<Long> mandatoryPropertyUpdatedIIDs = new ArrayList<Long>();
    List<Long> propertyIIDsToEvaluateProductIdentifier = new ArrayList<Long>();
    List<Long> propertyIIDsToRemoveProductIdentifier = new ArrayList<Long>();
    List<String> addedCalculatedAttributeIds = new ArrayList<String>();
    
    removedAttributeVariantContexts.put(IRemoveAttributeVariantContextModel.REMOVED_ATTRIBUTE_ID_VS_CONTEXT_IDS, new HashMap<String, List<String>>());
    removedAttributeVariantContexts.put(IRemoveAttributeVariantContextModel.CHANGED_CLASSIFIERS_FOR_ATTRIBUTE_CONTEXTS, new ArrayList<String>());
    
    manageADMForAsset(assetADM, defaultValueChangeList, deletedPropertyMap, propertiesADMMap,
        assetNode, contextualDataTransfer, sideInfoForDataTransfer, relationshipInheritance, isRemoveTaxonomyConflictsRequired,
        removedAttributeVariantContexts, removedContextClassifierIIDs, mandatoryPropertyUpdatedIIDs,
        propertyIIDsToEvaluateProductIdentifier, propertyIIDsToRemoveProductIdentifier, addedCalculatedAttributeIds);
    
    AuditLogUtils.fillAuditLoginfo(returnMap, assetNode, Entities.CLASSES, Elements.ASSET);
    graph.commit();
    Map<String, Object> assetMap = AssetUtils.getAssetEntityMap(assetNode, false);
    returnMap.put(IGetKlassEntityWithoutKPModel.ENTITY, assetMap);
    KlassGetUtils.fillReferencedConfigDetails(returnMap, assetNode);
    
    returnMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.DEFAULT_VALUES_DIFF,
        defaultValueChangeList);
    returnMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.DELETED_PROPERTIES_FROM_SOURCE,
        deletedPropertyMap);
    returnMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.IS_INDENTIFIER_ATTRIBUTE_CHANGED,
        propertiesADMMap
            .get(IGetKlassEntityWithoutKPStrategyResponseModel.IS_INDENTIFIER_ATTRIBUTE_CHANGED));
    returnMap.put(
        IGetKlassEntityWithoutKPStrategyResponseModel.CONTEXT_KLASS_SAVE_PROPERTIES_TO_DATA_TRANSFER,
        contextualDataTransfer.values());
    returnMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.RELATIONSHIP_DATA_FOR_TRANSFER,
        sideInfoForDataTransfer.values());
    returnMap.put(
        IGetKlassEntityWithoutKPStrategyResponseModel.RELATIONSHIP_DATA_FOR_RELATIONSHIP_INHERITANCE,
        relationshipInheritance.values());
    returnMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.UPDATED_MANDATORY_PROPERTY_IIDS, mandatoryPropertyUpdatedIIDs);
    returnMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.PROPERTY_IIDS_TO_EVALUATE_PRODUCT_IDENTIFIER, propertyIIDsToEvaluateProductIdentifier);
    returnMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.PROPERTY_IIDS_TO_REMOVE_PRODUCT_IDENTIFIER, propertyIIDsToRemoveProductIdentifier);
    returnMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.DELETED_NATURE_RELATIONSHIP_IDS, assetADM.get(IKlassSaveModel.DELETED_RELATIONSHIPS));
    returnMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.REMOVED_CONTEXT_CLASSIFIER_IIDS, removedContextClassifierIIDs);
    returnMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.REMOVED_ATTRIBUTE_VARIANT_CONTEXTS, removedAttributeVariantContexts);
    returnMap.put(IGetKlassEntityWithoutKPStrategyResponseModel.ADDED_CALCULATED_ATTRIBUTE_IDS, addedCalculatedAttributeIds);
    
    return returnMap;
  }
  
  private void manageADMForAsset(HashMap<String, Object> assetADM,
      List<Map<String, Object>> defaultValueChangeList,
      Map<String, List<String>> deletedPropertyMap, Map<String, Object> propertiesADMMap,
      Vertex assetNode, Map<String, Map<String, Object>> contextualDataTransfer,
      Map<String, Map<String, Object>> sideInfoForDataTransfer,
      Map<String, Map<String, Object>> relationshipInheritance, MutableBoolean isRemoveTaxonomyConflictsRequired,
      Map<String, Object> removedAttributeVariantContexts, List<Long> removedContextClassifierIIDs,
      List<Long> mandatoryPropertyUpdatedIIDs, List<Long> propertyIIDsToEvaluateProductIdentifier,
      List<Long> propertyIIDsToRemoveProductIdentifier, List<String> addedCalculatedAttributeIds) throws Exception
  {
    List<Vertex> assetAndChildNodes = KlassUtils.getKlassAndChildNodes(assetNode);
    
    List<String> addedProductVariants = new ArrayList<>();
    List<String> deletedProductVariants = new ArrayList<>();
    Map<String, List<String>> addedContexts = (Map<String, List<String>>) assetADM
        .get(IKlassSaveModel.ADDED_CONTEXTS);
    if (addedContexts != null) {
      addedProductVariants = addedContexts.remove(IKlassContext.PRODUCT_VARIANT_CONTEXTS);
    }
    Map<String, List<String>> deletedContexts = (Map<String, List<String>>) assetADM
        .get(IKlassSaveModel.DELETED_CONTEXTS);
    if (deletedContexts != null) {
      deletedProductVariants = deletedContexts.remove(IKlassContext.PRODUCT_VARIANT_CONTEXTS);
    }
    propertiesADMMap
        .putAll(SaveKlassUtil.manageKlassNatureInKlass(assetNode, assetADM, addedProductVariants,
            deletedProductVariants, sideInfoForDataTransfer, relationshipInheritance, isRemoveTaxonomyConflictsRequired));
    
    SaveKlassUtil.manageLifeCycleStatusTag(assetADM, assetNode);
    SaveKlassUtil.manageTreeTypeOption(assetADM, assetNode, assetAndChildNodes);
    SaveKlassUtil.manageSectionsInKlass(assetNode, assetADM, assetAndChildNodes,
        VertexLabelConstants.ENTITY_TYPE_ASSET, defaultValueChangeList, deletedPropertyMap,
        propertiesADMMap, removedAttributeVariantContexts, mandatoryPropertyUpdatedIIDs, 
        propertyIIDsToEvaluateProductIdentifier, propertyIIDsToRemoveProductIdentifier, addedCalculatedAttributeIds);
    
    KlassUtils.manageDataRules(assetADM, assetNode, VertexLabelConstants.DATA_RULE, "klass");
    KlassUtils.manageVariantContext(assetADM, assetNode, VertexLabelConstants.VARIANT_CONTEXT);
    VariantContextUtils.manageLinkedLanguageKlass(assetADM, assetNode, contextualDataTransfer);
    VariantContextUtils.manageLinkedEmbeddedContextKlasses(assetADM, assetNode,
        contextualDataTransfer, removedContextClassifierIIDs);
    
    SaveKlassUtil.manageTasks(assetADM, assetNode);
    
    SaveKlassUtil.updateTemplateLabelIfKlassLabelChanged(assetADM, assetNode);
    
    manageExtensions(assetADM, assetNode);
    
    UtilClass.saveNode(assetADM, assetNode, propertiesToExclude);
  }
  
  private void manageExtensions(HashMap<String, Object> assetADM, Vertex assetNode) throws Exception
  {
    List<String> deletedExtensionList = (List<String>) assetADM
        .get(IAssetKlassSaveModel.DELETED_EXTENSION_CONFIGURATION);
    List<Map<String, Object>> modifiedExtensionList = (List<Map<String, Object>>) assetADM
        .get(IAssetKlassSaveModel.MODIFIED_EXTENSION_CONFIGURATION);
    List<Map<String, Object>> addedExtensionList = (List<Map<String, Object>>) assetADM
        .get(IAssetKlassSaveModel.ADDED_EXTENSION_CONFIGURATION);
    
    manageDeletedExtensions(assetNode, deletedExtensionList);
    manageModifiedExtensions(assetNode, modifiedExtensionList);
    manageAddedExtensions(assetNode, addedExtensionList);
  }
  
  private void manageDeletedExtensions(Vertex assetNode, List<String> deletedExtensionList)
      throws Exception
  {
    for (String deleteExtension : deletedExtensionList) {
      Vertex deleteNode = UtilClass.getVertexById(deleteExtension,
          VertexLabelConstants.ASSET_EXTENSION);
      Iterable<Edge> deleteEdges = deleteNode.getEdges(Direction.IN,
          RelationshipLabelConstants.ASSET_EXTENSION_LINK);
      deleteEdges.iterator()
          .next()
          .remove();
      deleteNode.remove();
    }
  }
  
  private void manageModifiedExtensions(Vertex assetNode,
      List<Map<String, Object>> modifiedExtensionList) throws Exception
  {
    for (Map<String, Object> modifiedExtension : modifiedExtensionList) {
      String extension = (String) modifiedExtension
          .get(IAssetExtensionConfigurationModel.EXTENSION);
      if (!extension.isEmpty()) {
        String extensionId = (String) modifiedExtension.get(IAssetExtensionConfigurationModel.ID);
        Vertex extensionVertex = UtilClass.getVertexById(extensionId,
            VertexLabelConstants.ASSET_EXTENSION);
        String existingExtension = extensionVertex
            .getProperty(IAssetExtensionConfigurationModel.EXTENSION);
        Boolean isValidExtension = isValidExtension(extension);
        if ((existingExtension.equals(extension)) || isValidExtension) {
          UtilClass.saveNode(modifiedExtension, extensionVertex,
              propertiesToExcludeForAssetExtension);
        }
        else if (!isValidExtension) {
          throw new ExtensionAlreadyExistsException(getExtentionKlassName(extension));
        }
      }
      else {
        throw new ExtensionAlreadyExistsException(getExtentionKlassName(extension));
      }
    }
  }
  
  private void manageAddedExtensions(Vertex assetNode, List<Map<String, Object>> addedExtensionList)
      throws Exception
  {
    for (Map<String, Object> addedExtension : addedExtensionList) {
      String extension = (String) addedExtension.get(IAssetExtensionConfigurationModel.EXTENSION);
      if (!extension.isEmpty() && isValidExtension(extension)) {
        AssetUtils.createAndLinkExtension(assetNode, extension,
            (Boolean) addedExtension.get(IAssetExtensionConfigurationModel.EXTRACT_METADATA),
            (Boolean) addedExtension.get(IAssetExtensionConfigurationModel.EXTRACT_RENDITION));
      }
      else {
        throw new ExtensionAlreadyExistsException(getExtentionKlassName(extension));
      }
    }
  }
  
  private Boolean isValidExtension(String extension)
  {
    OrientGraph graph = UtilClass.getGraph();
    String query = "SELECT FROM " + VertexLabelConstants.ASSET_EXTENSION + " WHERE "
        + IAssetExtensionConfigurationModel.EXTENSION + " ='" + extension + "'";
    Iterable<Vertex> vertices = graph.command(new OCommandSQL(query))
        .execute();
    return !vertices.iterator()
        .hasNext();
  }
  
  private String getExtentionKlassName(String extension)
  {
    OrientGraph graph = UtilClass.getGraph();
    String query = "SELECT FROM " + VertexLabelConstants.ENTITY_TYPE_ASSET + " WHERE out('"
        + RelationshipLabelConstants.ASSET_EXTENSION_LINK + "')."
        + IAssetExtensionConfigurationModel.EXTENSION + " contains ['" + extension + "']";
    Iterable<Vertex> vertices = graph.command(new OCommandSQL(query))
        .execute();
    while (vertices.iterator()
        .hasNext()) {
      Vertex klassVertex = vertices.iterator()
          .next();
      Map<String, Object> klassMap = UtilClass.getMapFromVertex(Arrays.asList(IAsset.LABEL),
          klassVertex);
      return (String) klassMap.get(IAsset.LABEL);
    }
    return null;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveAsset/*" };
  }
  
  public static List<String> propertiesToExclude                  = Arrays.asList(
      IKlassSaveModel.ADDED_DATA_RULES, IKlassSaveModel.DELETED_DATA_RULES,
      CommonConstants.TREE_TYPE_OPTION_PROPERTY, IKlassSaveModel.IS_STANDARD,
      IKlassSaveModel.ADDED_CONTEXTS, IKlassSaveModel.DELETED_CONTEXTS,
      IKlassSaveModel.ADDED_RELATIONSHIPS, IKlassSaveModel.MODIFIED_RELATIONSHIPS,
      IKlassSaveModel.DELETED_RELATIONSHIPS, IKlassSaveModel.ADDED_LIFECYCLE_STATUS_TAGS,
      IKlassSaveModel.DELETED_LIFECYCLE_STATUS_TAGS,
      IKlassSaveModel.REFERENCED_TAGS,                                                             /* IKlassSaveModel.IS_TYPE_CHANGED,*/
      IGetKlassEntityWithoutKPStrategyResponseModel.CONTEXT_KLASS_SAVE_PROPERTIES_TO_DATA_TRANSFER,
      IKlassSaveModel.ADDED_TASKS, IKlassSaveModel.DELETED_TASKS, IAssetModel.REFERENCED_CONTEXTS,
      IAssetModel.DATA_RULES_OF_KLASS, CommonConstants.GLOBAL_PERMISSION_PROPERTY,
      IKlassSaveModel.ADDED_CONTEXT_KLASSES, IKlassSaveModel.DELETED_CONTEXT_KLASSES,
      IKlass.EMBEDDED_KLASS_IDS, IKlassSaveModel.ADDED_LANGUAGE_KLASS,
      IKlassSaveModel.MODIFIED_LANGUAGE_KLASS, IKlassSaveModel.DELETED_LANGUAGE_KLASS,
      IKlassSaveModel.MODIFIED_CONTEXT_KLASSES,
      IAssetKlassSaveModel.ADDED_EXTENSION_CONFIGURATION,
      IAssetKlassSaveModel.DELETED_EXTENSION_CONFIGURATION,
      IAssetKlassSaveModel.MODIFIED_EXTENSION_CONFIGURATION,
      IAssetKlassSaveModel.EXTENSION_CONFIGURATION, IAsset.IS_DETECTDUPLICATE_MODIFIED);
  
  public static List<String> propertiesToExcludeForAssetExtension = Arrays.asList(
      IAssetExtensionConfigurationModel.LAST_MODIFIED_BY,
      IAssetExtensionConfigurationModel.VERSION_ID,
      IAssetExtensionConfigurationModel.VERSION_TIMESTAMP);
}
