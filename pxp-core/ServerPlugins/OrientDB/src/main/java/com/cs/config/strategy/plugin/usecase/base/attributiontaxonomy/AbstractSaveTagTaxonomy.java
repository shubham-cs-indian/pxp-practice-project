package com.cs.config.strategy.plugin.usecase.base.attributiontaxonomy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.attributiontaxonomy.util.AttributionTaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.base.taxonomy.util.TaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.klass.util.SaveKlassUtil;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.variantcontext.util.VariantContextUtils;
import com.cs.core.config.interactor.entity.taxonomy.IDefaultFilters;
import com.cs.core.config.interactor.exception.taxonomy.KlassTaxonomyNotFoundException;
import com.cs.core.config.interactor.model.attributiontaxonomy.IGetMasterTaxonomyWithoutKPStrategyResponseModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.ISaveMasterTaxonomyModel;
import com.cs.core.config.interactor.model.klass.IGetKlassEntityWithoutKPStrategyResponseModel;
import com.cs.core.config.interactor.model.klass.IRemoveAttributeVariantContextModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

@SuppressWarnings("unchecked")
public abstract class AbstractSaveTagTaxonomy extends AbstractOrientPlugin {
  
  public AbstractSaveTagTaxonomy(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  public abstract String getVertexType();
  
  public abstract String getTaxonomyLevelType();
  
  public abstract String getTagType();
  
  public abstract void validateTaxonomy(Map<String, Object> requestMap) throws Exception;
  
  public abstract void updateDefaultDataLanguage(Map<String, Object> requestMap) throws Exception;
  
  public abstract void updateDefaultUserInterfaceLanguage(Map<String, Object> requestMap)
      throws Exception;
  
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> defaultValueChangeList = new ArrayList<>();
    Map<String, List<String>> deletedPropertyMap = new HashMap<>();
    Map<String,Object> mapToReturn = new HashMap<>();
    Map<String, Object> propertiesADMMap = new HashMap<>();
    propertiesADMMap
        .put(IGetKlassEntityWithoutKPStrategyResponseModel.IS_INDENTIFIER_ATTRIBUTE_CHANGED, false); // Default
                                                                                                     // vaalue
                                                                                                     // for
                                                                                                     // isIndentifierAttributeChanged
    Map<String, Object> klassTaxonomyMap = (Map<String, Object>) requestMap.get("klassTaxonomy");
    String id = (String) klassTaxonomyMap.get(ISaveMasterTaxonomyModel.ID);
    List<Long> removedContextClassifierIIDs = new ArrayList<>();

    UtilClass.setNodesForVersionIncrement(new HashSet<>());
    Vertex klassTaxonomy = null;
    try {
      klassTaxonomy = UtilClass.getVertexByIndexedId(id, getVertexType());
    }
    catch (NotFoundException e) {
      throw new KlassTaxonomyNotFoundException(e);
    }
    SaveKlassUtil.updateTemplateLabelIfKlassLabelChanged(klassTaxonomyMap, klassTaxonomy);
    
    UtilClass.saveNode(klassTaxonomyMap, klassTaxonomy, fieldsToExclude);
    // manageKlasses(klassTaxonomy, klassTaxonomyMap);
    
    // manage sections..
    Map<String, Object> removedAttributeVariantContexts = new HashMap<>();
    
    List<Long> mandatoryPropertyUpdatedIIDs = new ArrayList<Long>();
    List<Long> propertyIIDsToEvaluateProductIdentifier = new ArrayList<Long>();
    List<Long> propertyIIDsToRemoveProductIdentifier = new ArrayList<Long>();
    List<String> addedCalculatedAttributeIds = new ArrayList<String>();
    removedAttributeVariantContexts.put(IRemoveAttributeVariantContextModel.REMOVED_ATTRIBUTE_ID_VS_CONTEXT_IDS, new HashMap<String, List<String>>());
    removedAttributeVariantContexts.put(IRemoveAttributeVariantContextModel.CHANGED_CLASSIFIERS_FOR_ATTRIBUTE_CONTEXTS, new ArrayList<String>());
    
    List<Vertex> taxonomyAndChildNodes = getTaxonomyAndChildNodes(klassTaxonomy);
    SaveKlassUtil.manageSectionsInKlass(klassTaxonomy, klassTaxonomyMap, taxonomyAndChildNodes,
        getVertexType(), defaultValueChangeList, deletedPropertyMap, propertiesADMMap, removedAttributeVariantContexts,
        mandatoryPropertyUpdatedIIDs, propertyIIDsToEvaluateProductIdentifier, propertyIIDsToRemoveProductIdentifier,
        addedCalculatedAttributeIds);
    
    Map<String, Map<String, Object>> contextualDataTransfer = new HashMap<>();
    VariantContextUtils.manageLinkedEmbeddedContextKlasses(klassTaxonomyMap, klassTaxonomy,
        contextualDataTransfer, removedContextClassifierIIDs);
    KlassUtils.manageDataRules(klassTaxonomyMap, klassTaxonomy, VertexLabelConstants.DATA_RULE,
        "taxonomy");
    SaveKlassUtil.manageTasks(klassTaxonomyMap, klassTaxonomy);
    addOrDeleteAttributionTaxonomyLevel(klassTaxonomyMap, klassTaxonomy);

    updateDefaultDataLanguage(requestMap);
    updateDefaultUserInterfaceLanguage(requestMap);
    
    validateTaxonomy(requestMap);
    
    AuditLogUtils.fillAuditLoginfo(mapToReturn, klassTaxonomy, Entities.TAXONOMIES, Elements.MASTER_TAXONOMY_CONFIGURATION_TITLE);
    
    UtilClass.getGraph().commit();
    Map<String, Object> taxonomyMap = AttributionTaxonomyUtil.getAttributionTaxonomy(klassTaxonomy, getTaxonomyLevelType());
    mapToReturn.put(IGetMasterTaxonomyWithoutKPStrategyResponseModel.ENTITY, taxonomyMap);
    
    AttributionTaxonomyUtil.fillAttributionTaxonomyData(id, mapToReturn, klassTaxonomy, false, true);
    mapToReturn.put(IGetMasterTaxonomyWithoutKPStrategyResponseModel.DEFAULT_VALUES_DIFF,
        defaultValueChangeList);
    mapToReturn.put(IGetMasterTaxonomyWithoutKPStrategyResponseModel.DELETED_PROPERTIES_FROM_SOURCE,
        deletedPropertyMap);
    mapToReturn.put(
        IGetMasterTaxonomyWithoutKPStrategyResponseModel.CONTEXT_KLASS_SAVE_PROPERTIES_TO_DATA_TRANSFER,
        contextualDataTransfer.values());
    mapToReturn.put(IGetKlassEntityWithoutKPStrategyResponseModel.IS_INDENTIFIER_ATTRIBUTE_CHANGED,
        propertiesADMMap
            .get(IGetKlassEntityWithoutKPStrategyResponseModel.IS_INDENTIFIER_ATTRIBUTE_CHANGED));
    mapToReturn.put(IGetMasterTaxonomyWithoutKPStrategyResponseModel.IS_IMMEDIATE_CHILD_PRESENT,
        TaxonomyUtil.isImmediateChildPresent(klassTaxonomy));
    mapToReturn.put(IGetKlassEntityWithoutKPStrategyResponseModel.UPDATED_MANDATORY_PROPERTY_IIDS, mandatoryPropertyUpdatedIIDs);
    mapToReturn.put(IGetMasterTaxonomyWithoutKPStrategyResponseModel.PROPERTY_IIDS_TO_EVALUATE_PRODUCT_IDENTIFIER, propertyIIDsToEvaluateProductIdentifier);
    mapToReturn.put(IGetMasterTaxonomyWithoutKPStrategyResponseModel.PROPERTY_IIDS_TO_REMOVE_PRODUCT_IDENTIFIER, propertyIIDsToRemoveProductIdentifier);
    mapToReturn.put(IGetMasterTaxonomyWithoutKPStrategyResponseModel.REMOVED_CONTEXT_CLASSIFIER_IIDS, removedContextClassifierIIDs);
    mapToReturn.put(IGetMasterTaxonomyWithoutKPStrategyResponseModel.REMOVED_ATTRIBUTE_VARIANT_CONTEXTS, removedAttributeVariantContexts);
    mapToReturn.put(IGetMasterTaxonomyWithoutKPStrategyResponseModel.ADDED_CALCULATED_ATTRIBUTE_IDS, addedCalculatedAttributeIds);
    
    return mapToReturn;
  }

  public void addOrDeleteAttributionTaxonomyLevel(Map<String, Object> requestMap, Vertex taxonomyNode) throws Exception
  {
    Map<String, Object> addedLevel = (Map<String, Object>) requestMap.get(ISaveMasterTaxonomyModel.ADDED_LEVEL);

    String deletedLevel = (String) requestMap.get(ISaveMasterTaxonomyModel.DELETED_LEVEL);

    TaxonomyUtil.manageDeletedLevelNode(taxonomyNode, deletedLevel, getTaxonomyLevelType(), getVertexType());
    TaxonomyUtil.manageAddedLevel(taxonomyNode, addedLevel, getTaxonomyLevelType(), getTagType());
  }
  /*private void manageKlasses(Vertex klassTaxonomy, Map<String, Object> requestMap) throws Exception
  {
    String taxonomyId = (String) requestMap.get(IAttributionTaxonomy.ID);
    List<String> addedKlasses = (List<String>)requestMap.get(ISaveAttributionTaxonomyModel.ADDED_APPLIED_KLASSES);
    List<String> deletedKlasses = (List<String>)requestMap.get(ISaveAttributionTaxonomyModel.DELETED_APPLIED_KLASSES);
  
    List<String> tempAddedKlassesList = new ArrayList<>(addedKlasses);
    for (String klassId : tempAddedKlassesList) {
      Vertex klassVertex = UtilClass.getVertexById(klassId, getKlassVertexType());
      if (TaxonomyUtil.isParentLinkedToTaxonomy(klassVertex, taxonomyId)) {
        addedKlasses.remove(klassId);
      }
      else {
        Set<String> childrenKlassIds = TaxonomyUtil.getOwnAndAllChildrenKlassIdsSet(klassVertex);
  
         * childrenKlassIds includes id of current klass as well
         * so if link exists between this klass and taxonomy, it is deleted in manageDeletedKlasses()
         * so no need to check if link already exists in manageAddedKlasses()
  
        deletedKlasses.addAll(childrenKlassIds);
      }
    }
  
    manageDeletedKlasses(klassTaxonomy, deletedKlasses);
    manageAddedKlasses(klassTaxonomy, addedKlasses);
  }*/
  
  /* private void manageAddedKlasses(Vertex klassTaxonomy, List<String> addedKlasses) throws Exception
  {
    if(addedKlasses.size() == 0) {
      return;
    }
    for (String addedKlassId : addedKlasses) { //create klass_taxonomy_link
      Vertex klass = null;
      try {
        klass = UtilClass.getVertexById(addedKlassId, getKlassVertexType());
      }
      catch (NotFoundException e) {
        throw new KlassNotFoundException(e);
      }
      klassTaxonomy.addEdge(RelationshipLabelConstants.KLASS_TAXONOMY_LINK, klass);
    }
  }*/
  
  /* private void manageDeletedKlasses(Vertex klassTaxonomy, List<String> deletedKlasses)
  {
    if (deletedKlasses.size() == 0) {
      return;
    }
  
    Iterable<Edge> klassTaxonomyLinks = klassTaxonomy.getEdges(Direction.OUT, RelationshipLabelConstants.KLASS_TAXONOMY_LINK);
    for (Edge klassTaxonomyLink : klassTaxonomyLinks) {
      Vertex klass = klassTaxonomyLink.getVertex(Direction.IN);
      String klassId = klass.getProperty(CommonConstants.CODE_PROPERTY);
      if (deletedKlasses.contains(klassId)) {
        klassTaxonomyLink.remove();
        deletedKlasses.remove(klassId);
        if (deletedKlasses.size() == 0) {
          break;
        }
      }
    }
  }*/
  
  private List<Vertex> getTaxonomyAndChildNodes(Vertex klassTaxonomyNode)
  {
    OrientGraph graph = UtilClass.getGraph();
    String rid = klassTaxonomyNode.getId()
        .toString();
    Iterable<Vertex> i = graph
        .command(new OCommandSQL(
            "select from(traverse in('Child_Of') from " + rid + " strategy BREADTH_FIRST)"))
        .execute();
    List<Vertex> taxonomyAndChildNodes = new ArrayList<>();
    for (Vertex node : i) {
      taxonomyAndChildNodes.add(node);
    }
    return taxonomyAndChildNodes;
  }
  
  protected static final List<String> fieldsToExclude                          = Arrays.asList(
      ISaveMasterTaxonomyModel.PARENT, ISaveMasterTaxonomyModel.ADDED_APPLIED_KLASSES,
      ISaveMasterTaxonomyModel.DELETED_APPLIED_KLASSES, ISaveMasterTaxonomyModel.ADDED_SECTIONS,
      ISaveMasterTaxonomyModel.DELETED_SECTIONS, ISaveMasterTaxonomyModel.MODIFIED_SECTIONS,
      ISaveMasterTaxonomyModel.MODIFIED_ELEMENTS, ISaveMasterTaxonomyModel.SECTIONS,
      ISaveMasterTaxonomyModel.ADDED_ELEMENTS, ISaveMasterTaxonomyModel.DELETED_ELEMENTS,
      ISaveMasterTaxonomyModel.ADDED_CONTEXT_KLASSES,
      ISaveMasterTaxonomyModel.DELETED_CONTEXT_KLASSES, ISaveMasterTaxonomyModel.EMBEDDED_KLASS_IDS,
      ISaveMasterTaxonomyModel.ADDED_DATA_RULES, ISaveMasterTaxonomyModel.DELETED_DATA_RULES,
      ISaveMasterTaxonomyModel.DATA_RULES,
      ISaveMasterTaxonomyModel.ADDED_TASKS, ISaveMasterTaxonomyModel.DELETED_TASKS,
      ISaveMasterTaxonomyModel.TASKS, ISaveMasterTaxonomyModel.ADDED_LEVEL,
      ISaveMasterTaxonomyModel.DELETED_LEVEL, ISaveMasterTaxonomyModel.TAG_LEVELS,
      ISaveMasterTaxonomyModel.TAG_LEVEL_SEQUENCE, ISaveMasterTaxonomyModel.BASE_TYPE,
      ISaveMasterTaxonomyModel.IS_TAXONOMY, ISaveMasterTaxonomyModel.IS_TAG,
      ISaveMasterTaxonomyModel.IS_BACKGROUND_SAVE_TAXONOMY);
  
  protected static final List<String> fieldsToExcludeForDefaultFilters         = Arrays
      .asList(IDefaultFilters.TAG_VALUES, IDefaultFilters.TAG_ID);
  protected static final List<String> fieldsToExcludeForDefaultFilterTagValues = Arrays
      .asList(IDefaultFilters.TAG_ID);
}
