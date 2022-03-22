package com.cs.config.strategy.plugin.usecase.taxonomy.article;

import com.cs.config.idto.IConfigJSONDTO.ConfigTag;
import com.cs.config.standard.IConfigClass;
import com.cs.config.strategy.plugin.usecase.base.taxonomy.util.TaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.entity.attributiontaxonomy.IMasterTaxonomy;
import com.cs.core.config.interactor.entity.attributiontaxonomy.ITagTaxonomy;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.model.klass.IKlassModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.summary.ISummaryInformationModel;
import com.cs.core.data.Text;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientVertexType;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class UpsertHierarchyTaxonomy extends AbstractOrientPlugin {
  
  private static final String PARENT_CODE                      = "parentCode";
  private static final String STATUS_TAGS                      = "statusTags";
  private static final String EVENTS                           = "events";
  private static final String TASKS                            = "tasks";
  private static final String ADDED_EVENTS                     = "addedEvents";
  private static final String DELETED_EVENTS                   = "deletedEvents";
  private static final String ADDED_TASKS                      = "addedTasks";
  private static final String DELETED_TASKS                    = "deletedTasks";
  private static final String EMBEDDED_CLASSES                 = "embeddedClasses";
  private static final String COUPLINGS                        = "couplings";
  private static final String REFERENCES                       = "references";
  private static final String ADDED_RELATIONSHIPS              = "addedRelationships";
  private static final String PRODUCT_VARIANT_CONTEXT_CODE     = "productVariantContextCode";
  private static final String ADDED_CONTEXTS                   = "addedContexts";
  private static final String LEVEL_CODE                       = "levelCode";
  private static final String MASTER_PARENT_TAG                = "masterParentTag";
  private static final String PROMOTIONAL_VERSION_CONTEXT_CODE = "promotionalVersionContextCode";
  private static final String LEVEL_LABELS                     = "levelLabels";
  private static final String TAXONOMY_TYPE                    = "taxonomyType";
  private static final String REFERENCED_CLASS_IDS             = "referencedClassIds";
  private static final String REFERENCED_ATTRIBUTE_IDS         = "referencedAttributeIds";
  private static final String DELETED_STRUCTURES               = "deletedStructures";
  private static final String STRUCTURE_CHILDREN               = "structureChildren";
  private static final String KLASS_VIEW_SETTING               = "klassViewSetting";
  private static final String MODIFIED_STRUCTURES              = "modifiedStructures";
  private static final String ADDED_STRUCTURES                 = "addedStructures";
  private static final String CODE_AND_IID_SEPERATOR           = ":";
  
  private List<String>        fieldsToExclude                  = Arrays.asList(REFERENCED_CLASS_IDS, CommonConstants.VALIDATOR_PROPERTY, 
      REFERENCED_ATTRIBUTE_IDS, IKlass.PERMISSIONS, DELETED_STRUCTURES, IKlass.NOTIFICATION_SETTINGS, STRUCTURE_CHILDREN, 
      IKlass.CHILDREN, KLASS_VIEW_SETTING,MODIFIED_STRUCTURES, ADDED_STRUCTURES, IKlass.IS_ENFORCED_TAXONOMY,
      CommonConstants.PARENT_PROPERTY, PARENT_CODE, STATUS_TAGS, EVENTS, TASKS, ADDED_EVENTS, ADDED_TASKS,
      EMBEDDED_CLASSES, COUPLINGS, DELETED_EVENTS, DELETED_TASKS, REFERENCES, ADDED_RELATIONSHIPS, ADDED_CONTEXTS,
      PRODUCT_VARIANT_CONTEXT_CODE, CommonConstants.SECTIONS_PROPERTY, CommonConstants.TREE_TYPE_OPTION_PROPERTY,
      CommonConstants.RELATIONSHIPS, LEVEL_CODE, CommonConstants.TYPE_PROPERTY, ConfigTag.subType.name(), MASTER_PARENT_TAG, 
      PROMOTIONAL_VERSION_CONTEXT_CODE, LEVEL_LABELS, ConfigTag.isNewlyCreatedLevel.name(), ConfigTag.levelIndex.name());
  
  public UpsertHierarchyTaxonomy(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|UpsertHierarchyTaxonomy/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> taxonomyList = (List<Map<String, Object>>) requestMap.get(CommonConstants.LIST_PROPERTY);
    List<Map<String, Object>> createdTaxonomyList = new ArrayList<>();
    List<Map<String, Object>> failedTaxonomyList = new ArrayList<>();
    IExceptionModel failure = new ExceptionModel();
    
    List<Map<String, Object>> orderedTaxonomyList = prepareTaxonomyList(taxonomyList);
    
    UtilClass.setSectionElementIdMap(new HashMap<>());
    
    for (Map<String, Object> taxonomy : orderedTaxonomyList) {
      try {
        Map<String, Object> taxonomyMap = createTaxonomyData(taxonomy);
        Map<String, Object> createdTaxonomy = new HashMap<>();
        createdTaxonomy.put(CommonConstants.CODE_PROPERTY, taxonomy.get(CommonConstants.CODE_PROPERTY));
        createdTaxonomy.put(ISummaryInformationModel.LABEL, taxonomy.get(CommonConstants.LABEL_PROPERTY));
        createdTaxonomyList.add(createdTaxonomy);
      }
      catch (Exception e) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, e, (String) taxonomy.get(CommonConstants.CODE_PROPERTY), (String) taxonomy.get(IKlassModel.LABEL));
        addToFailureIds(failedTaxonomyList, taxonomy);
      }
    }
    
    Map<String, Object> result = new HashMap<>();
    result.put(IPluginSummaryModel.SUCCESS, createdTaxonomyList);
    result.put(IPluginSummaryModel.FAILURE, failure);
    result.put(IPluginSummaryModel.FAILED_IDS, failedTaxonomyList);
    return result;
  }
  
  private Map<String, Object> createTaxonomyData(Map<String, Object> taxonomyMap) throws Exception
  {
    String type = (String) taxonomyMap.get(TAXONOMY_TYPE);
    String taxonomyCode = (String) taxonomyMap.get(CommonConstants.CODE_PROPERTY);
    Vertex taxonomyNode;
    try {
      taxonomyNode = UtilClass.getVertexByCode(taxonomyCode, CommonConstants.HIERARCHY_TAXONOMY);
    }
    catch (NotFoundException e) {
      OrientVertexType vertexType = UtilClass.getOrCreateVertexType(CommonConstants.HIERARCHY_TAXONOMY, CommonConstants.CODE_PROPERTY);
      if(StringUtils.isEmpty(type))
        type = CommonConstants.MAJOR_TAXONOMY;
      taxonomyMap.put(CommonConstants.TAXONOMY_TYPE, type);
      taxonomyMap.put(IMasterTaxonomy.BASE_TYPE, IConfigClass.ClassifierClass.HIERARCHY_KLASS_TYPE);
      taxonomyMap.put(ITaxonomy.CHILD_COUNT, 0);
      taxonomyNode = UtilClass.createNode(taxonomyMap, vertexType, fieldsToExclude);
      // Process level
      addLevels(taxonomyMap, taxonomyNode);
      
      // Process Parent
      String linkedTagCode = (String) taxonomyMap.get(MASTER_PARENT_TAG);
      String parentCode = (String) taxonomyMap.get(PARENT_CODE);
      TaxonomyUtil.manageParent(taxonomyMap, taxonomyNode, linkedTagCode, parentCode, CommonConstants.HIERARCHY_TAXONOMY);
    }

    String linkedTagCode = (String) taxonomyMap.get(MASTER_PARENT_TAG);
    String parentCode = (String) taxonomyMap.get(PARENT_CODE);
    TaxonomyUtil.manageParent(taxonomyMap, taxonomyNode, linkedTagCode, parentCode, CommonConstants.HIERARCHY_TAXONOMY);
    UtilClass.saveNode(taxonomyMap, taxonomyNode, fieldsToExclude);
    UtilClass.getGraph().commit();
    
    return taxonomyMap;
  }
  
  public void addToFailureIds(List<Map<String, Object>> failedTaxonomyList, Map<String, Object> taxonomy)
  {
    Map<String, Object> failedTaxonomyMap = new HashMap<>();
    failedTaxonomyMap.put(ISummaryInformationModel.ID, taxonomy.get(IKlassModel.ID));
    failedTaxonomyMap.put(ISummaryInformationModel.LABEL, taxonomy.get(IKlassModel.LABEL));
    failedTaxonomyList.add(failedTaxonomyMap);
  }
  
  private void addLevels(Map<String, Object> taxonomyMap, Vertex taxonomyNode) throws Exception
  {
    List<String> levelCodes = (List<String>) taxonomyMap.get(LEVEL_CODE);
    List<String> levelLables = (List<String>) taxonomyMap.get(LEVEL_LABELS);
    List<String> tagLevel = (List<String>) taxonomyMap.get(IMasterTaxonomy.TAG_LEVEL_SEQUENCE);
    String entityTag = VertexLabelConstants.ENTITY_TAG;
    OrientVertexType entityTagTypeVertex = UtilClass.getGraph().getVertexType(entityTag);
    String languageConvertedField = EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY);
    int level = 0;
    for (int i=0; i<levelCodes.size(); i++) {
      Vertex levelNode = null;
      String levelCodeWithIID = levelCodes.get(i);
      String levelCode = Text.getBefore(levelCodeWithIID, CODE_AND_IID_SEPERATOR);
      long PropertyIID = Long.parseLong(Text.getAfter(levelCodeWithIID, CODE_AND_IID_SEPERATOR));
      String levelLabel = levelLables.get(i);
      try {
        levelNode = UtilClass.getVertexByCode(levelCode, entityTag);
      }
      catch (NotFoundException e) {
        Map<String, Object> levelMap = createLevelNode(languageConvertedField, levelCode, levelLabel, PropertyIID);
        levelNode = UtilClass.createNode(levelMap, entityTagTypeVertex, fieldsToExclude);
        levelNode.setProperty(CommonConstants.TYPE_PROPERTY, IConfigClass.PropertyClass.TAG_TYPE);
      }
      if (tagLevel == null) {
        tagLevel = new ArrayList<>();
        tagLevel.add(levelCode);
        levelNode.setProperty(ITagTaxonomy.TAG_LEVEL_SEQUENCE, tagLevel);
      }
      else {
        tagLevel.add(levelCode);
        levelNode.setProperty(ITagTaxonomy.TAG_LEVEL_SEQUENCE, tagLevel);
      }
      Edge levelEdge = taxonomyNode.addEdge(RelationshipLabelConstants.TAXONOMY_LEVEL, levelNode);
      levelEdge.setProperty(CommonConstants.TAXONOMY_LEVEL, String.valueOf(++level));
    }
  }
  
  private Map<String, Object> createLevelNode(String languageConvertedField, String levelCode, String levelLabel, long propertyIID)
  {
    Map<String, Object> levelMap = new HashMap<>();
    levelMap.put(CommonConstants.CODE_PROPERTY, levelCode);
    levelMap.put(languageConvertedField, levelLabel);
    levelMap.put(CommonConstants.VERSION_ID, 0);
    levelMap.put(ITag.IS_VERSIONABLE, true);
    levelMap.put(ITag.IS_DIMENSIONAL, false);
    levelMap.put(ITag.IS_MULTI_SELECT, true);
    levelMap.put(IMasterTaxonomy.TAG_VALUES_SEQUENCE, new ArrayList<String>());
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
      if (StringUtils.isEmpty(parentCode) || parentCode.equals("-1")) {
        parentTaxonomyList.add(taxonomy);
      }else {
        childrenTaxonomyList.add(taxonomy);
      }
    }
    parentTaxonomyList.addAll(childrenTaxonomyList);
    return parentTaxonomyList;
  }
}
