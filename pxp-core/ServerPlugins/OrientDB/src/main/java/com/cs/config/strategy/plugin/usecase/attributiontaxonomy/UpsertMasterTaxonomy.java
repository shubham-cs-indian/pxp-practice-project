package com.cs.config.strategy.plugin.usecase.attributiontaxonomy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import com.cs.config.idto.IConfigJSONDTO.ConfigTag;
import com.cs.config.standard.IConfigClass;
import com.cs.config.strategy.plugin.usecase.attributiontaxonomy.util.AttributionTaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.base.attributiontaxonomy.AbstractSaveKlassAndTaxonomy;
import com.cs.config.strategy.plugin.usecase.base.taxonomy.util.TaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.importPXON.util.ImportUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.Constants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.attributiontaxonomy.IMasterTaxonomy;
import com.cs.core.config.interactor.entity.attributiontaxonomy.ITagTaxonomy;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.model.attributiontaxonomy.IGetMasterTaxonomyWithoutKPModel;
import com.cs.core.data.Text;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;

@SuppressWarnings("unchecked")
public class UpsertMasterTaxonomy extends AbstractSaveKlassAndTaxonomy {
  

  private final List<String> fieldsToExclude = Arrays.asList(REFERENCED_CLASS_IDS, CommonConstants.VALIDATOR_PROPERTY,
      REFERENCED_ATTRIBUTE_IDS, IKlass.PERMISSIONS, DELETED_STRUCTURES, IKlass.NOTIFICATION_SETTINGS, STRUCTURE_CHILDREN, 
      IKlass.CHILDREN, KLASS_VIEW_SETTING,MODIFIED_STRUCTURES, ADDED_STRUCTURES, IKlass.IS_ENFORCED_TAXONOMY, 
      CommonConstants.PARENT_PROPERTY, PARENT_CODE, STATUS_TAGS, TASKS,  ADDED_TASKS,
      EMBEDDED_CLASSES, COUPLINGS, DELETED_TASKS, REFERENCES, ADDED_RELATIONSHIPS, VERSION_CONTEXT_CODE, ADDED_CONTEXTS,
      VERSION_CONTEXTS, PRODUCT_VARIANT_CONTEXT_CODE, CommonConstants.SECTIONS_PROPERTY, CommonConstants.TREE_TYPE_OPTION_PROPERTY,
      CommonConstants.RELATIONSHIPS, LEVEL_CODE, CommonConstants.TYPE_PROPERTY, ConfigTag.subType.name(), MASTER_PARENT_TAG, LEVEL_LABELS,
      ConfigTag.isNewlyCreatedLevel.name(), ConfigTag.levelIndex.name());
  
  public UpsertMasterTaxonomy(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }

  @Override
  public String[] getNames()
  {
    return new String[] { "POST|UpsertMasterTaxonomy/*" };
  }
  
  private IExceptionModel failure;
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> taxonomyList = (List<Map<String, Object>>) requestMap.get("list");
    List<Map<String, Object>> createdTaxonomyList = new ArrayList<>();
    List<Map<String, Object>> failedTaxonomyList = new ArrayList<>();
    List<Map<String, Object>> orderedTaxonomyList = prepareTaxonomyList(taxonomyList);
    failure = new ExceptionModel();
    UtilClass.setSectionElementIdMap(new HashMap<>());
    OrientVertexType vertexType = UtilClass.getOrCreateVertexType(VertexLabelConstants.ATTRIBUTION_TAXONOMY, CommonConstants.CODE_PROPERTY);
    Map<String, Object> successMap = new HashMap<>();
    for (Map<String, Object> taxonomy : orderedTaxonomyList) {
      try {
        upsertTaxonomyData(taxonomy, vertexType);
        ImportUtils.addSuccessImportedInfo(createdTaxonomyList, taxonomy);
      }
      catch (Exception e) {
        ImportUtils.logExceptionAndFailureIDs(failure, failedTaxonomyList, taxonomy, e);
      }
    }
    
    Map<String, Object> result = ImportUtils.prepareImportResponseMap(failure, createdTaxonomyList, failedTaxonomyList);
    return result;
  }
  
  private Map<String, Object> upsertTaxonomyData(Map<String, Object> taxonomyMap, OrientVertexType vertexType) throws Exception
  {
    String code = (String) taxonomyMap.get(CommonConstants.CODE_PROPERTY);
    Vertex taxonomyNode;
    try {
      taxonomyNode = UtilClass.getVertexByCode(code, VertexLabelConstants.ATTRIBUTION_TAXONOMY);
      UtilClass.saveNode(taxonomyMap, taxonomyNode, fieldsToExclude);
      UtilClass.getGraph().commit();
    }
    catch (NotFoundException e) {
      String taxonomyType = (String) taxonomyMap.get(TAXONOMY_TYPE);
      if (StringUtils.isEmpty(taxonomyType)) {
        taxonomyType = CommonConstants.MAJOR_TAXONOMY;
      }
      UtilClass.validateIconExistsForImport(taxonomyMap);
      prepareMasterTaxonomyMap(taxonomyMap, taxonomyType);
      taxonomyNode = UtilClass.createNode(taxonomyMap, vertexType, fieldsToExclude);
      taxonomyNode.setProperty(ITaxonomy.CHILD_COUNT, 0);
    }
    // Event & Task
    manageTasks(taxonomyMap, taxonomyNode, failure);
    
    // Embedded Classes
    handleEmbeddedClasses(taxonomyMap, taxonomyNode, failure);
    
    // Process level
    addLevels(taxonomyMap, taxonomyNode);
    
    // Process Parent
    String linkedTagCode = (String) taxonomyMap.get(MASTER_PARENT_TAG);
    String parentCode = (String) taxonomyMap.get(PARENT_CODE);
    TaxonomyUtil.manageParent(taxonomyMap, taxonomyNode, linkedTagCode, parentCode, VertexLabelConstants.ATTRIBUTION_TAXONOMY);
    
    // Section processing
    processSections(taxonomyMap, taxonomyNode, VertexLabelConstants.ATTRIBUTION_TAXONOMY, failure);
    UtilClass.saveNode(taxonomyMap, taxonomyNode, fieldsToExclude);
    UtilClass.getGraph().commit();

    Map<String, Object> mapToReturn = new HashMap<>();
    Map<String, Object> configDetails = new HashMap<>();
    Map<String, Object> attributionTaxonomy = AttributionTaxonomyUtil.getAttributionTaxonomy(taxonomyNode,
        VertexLabelConstants.ATTRIBUTION_TAXONOMY_LEVEL);
    mapToReturn.put(IGetMasterTaxonomyWithoutKPModel.ENTITY, attributionTaxonomy);
    mapToReturn.put(IGetMasterTaxonomyWithoutKPModel.CONFIG_DETAILS, configDetails);
    TaxonomyUtil.fillReferencedAttributeContextDetails(attributionTaxonomy, configDetails);
    mapToReturn.remove(IKlass.PERMISSIONS);
    return mapToReturn;
  }

  private void addLevels(Map<String, Object> taxonomyMap, Vertex taxonomyNode) throws Exception
  {
    List<String> levelCodes = (List<String>) taxonomyMap.get(LEVEL_CODE);
    List<String> levelLables = (List<String>) taxonomyMap.get(LEVEL_LABELS);
    List<String> tagLevel = (List<String>) taxonomyMap.get(IMasterTaxonomy.TAG_LEVEL_SEQUENCE);
    String entityTag = VertexLabelConstants.ENTITY_TAG;
    OrientVertexType entityTagTypeVertex = UtilClass.getGraph().getVertexType(entityTag);
    String languageConvertedField = EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY);
    for (int i = 0; i < levelCodes.size(); i++) {
      Vertex levelNode = null;
      String levelCodeWithIID = levelCodes.get(i);
      String levelCode = Text.getBefore(levelCodeWithIID, CODE_AND_IID_SEPERATOR);
      long PropertyIID = Long.parseLong(Text.getAfter(levelCodeWithIID, CODE_AND_IID_SEPERATOR));
      String levelLabel = levelLables.get(i);
      try {
        levelNode = UtilClass.getVertexByCode(levelCode, entityTag);
      }
      catch (NotFoundException e) {
        Map<String, Object> levelMap = createMasterTag(languageConvertedField, levelCode, levelLabel,PropertyIID);
        levelNode = UtilClass.createNode(levelMap, entityTagTypeVertex, fieldsToExclude);
        levelNode.setProperty(ITag.TYPE, CommonConstants.TAG_TYPE);
      }
      if (levelNode != null) {
        Map<String, Object> attributionMap = new HashMap<>();
        OrientVertexType vertexType = UtilClass.getGraph().getVertexType(VertexLabelConstants.ATTRIBUTION_TAXONOMY_LEVEL);
        Vertex attributionNode = UtilClass.createNode(attributionMap, vertexType, fieldsToExclude);
        levelNode.addEdge(RelationshipLabelConstants.LEVEL_TAGGROUP_OF, attributionNode);
        taxonomyNode.addEdge(RelationshipLabelConstants.HAS_TAXONOMY_LEVEL, attributionNode);
        String code = attributionNode.getProperty(CommonConstants.CODE_PROPERTY);
        if (tagLevel == null) {
          tagLevel = new ArrayList<>();
          tagLevel.add(code);
          taxonomyNode.setProperty(ITagTaxonomy.TAG_LEVEL_SEQUENCE, tagLevel);
        }
        else {
          tagLevel.add(code);
          taxonomyNode.setProperty(ITagTaxonomy.TAG_LEVEL_SEQUENCE, tagLevel);
        }
      }
    }
  }
  
  private Map<String, Object> createMasterTag(String languageConvertedField, String levelCode, String levelLabel, long propertyIID)
  {
    Map<String, Object> levelMap = new HashMap<>();
    levelMap.put(ITag.ID, levelCode);
    levelMap.put(ITag.LABEL, levelLabel);
    levelMap.put(ITag.IS_DIMENSIONAL, false);
    levelMap.put(ITag.IS_FOR_RELEVANCE, false);
    levelMap.put(ITag.IS_MULTI_SELECT, true);
    levelMap.put(ITag.IS_MANDATORY, false);
    levelMap.put(ITag.IS_STANDARD, false);
    levelMap.put(ITag.IS_VERSIONABLE, true);
    levelMap.put(ITag.IS_GRID_EDITABLE, false);
    levelMap.put(ITag.IS_FILTERABLE, false);
    levelMap.put(ITag.TAG_VALUES_SEQUENCE, new ArrayList<String>());
    levelMap.put(ITag.CODE, levelCode);
    levelMap.put(CommonConstants.VERSION_ID, 0);
    levelMap.put(ITag.TAG_TYPE, SystemLevelIds.MASTER_TAG_TYPE_ID);
    levelMap.put(ITag.PROPERTY_IID, propertyIID);
    return levelMap;
  }

  private List<Map<String, Object>> prepareTaxonomyList(List<Map<String, Object>> taxonomyList)
  {
    List<Map<String, Object>> parentTaxonomyList = new ArrayList<>();
    List<Map<String, Object>> childrenTaxonomyList = new ArrayList<>();
    for (Map<String, Object> taxonomy : taxonomyList) {
      String parentCode = (String) taxonomy.get(PARENT_CODE);
      if (StringUtils.isEmpty(parentCode) || parentCode.equals(Constants.STANDARD_ORGANIZATION)) {
        parentTaxonomyList.add(taxonomy);
      }else {
        childrenTaxonomyList.add(taxonomy);
      }
    }
    parentTaxonomyList.addAll(childrenTaxonomyList);
    return parentTaxonomyList;
  }
  
  private void prepareMasterTaxonomyMap(Map<String, Object> taxonomyMap, String taxonomyType)
  {
    taxonomyMap.put(ITaxonomy.TAXONOMY_TYPE, taxonomyType);
    taxonomyMap.put(ITaxonomy.BASE_TYPE, IConfigClass.ClassifierClass.TAXONOMY_KLASS_TYPE);
    taxonomyMap.put(ITagTaxonomy.TAG_LEVEL_SEQUENCE, new ArrayList<String>());
    taxonomyMap.put(IMasterTaxonomy.IS_DIMENSIONAL, false);
    taxonomyMap.put(IMasterTaxonomy.IS_FOR_RELEVANCE, false);
    taxonomyMap.put(IMasterTaxonomy.IS_MULTI_SELECT, false);
    taxonomyMap.put(IMasterTaxonomy.IS_MANDATORY, false);
    taxonomyMap.put(IMasterTaxonomy.IS_STANDARD, false);
    taxonomyMap.put(ITagTaxonomy.IS_TAXONOMY, true);
  }
}
