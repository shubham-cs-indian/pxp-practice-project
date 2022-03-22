package com.cs.config.strategy.plugin.usecase.export;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import com.cs.config.idto.IConfigJSONDTO.ConfigTag;
import com.cs.config.strategy.plugin.usecase.klass.util.KlassUtils;
import com.cs.config.strategy.plugin.usecase.relationship.util.RelationshipUtils;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.attributiontaxonomy.IMasterTaxonomy;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.klass.IKlassContext;
import com.cs.core.config.interactor.model.attributiontaxonomy.IGetAttributionTaxonomyModel;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public abstract class AbstractGetClassifierTaxonomyList extends AbstractOrientPlugin {
  
  private static final String CONTEXT_CODE = "contextCode";
  private static final String CONTEXT_ID = "contextID";

  public AbstractGetClassifierTaxonomyList(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  public abstract String getKlassVertexType();
  
  public abstract List<Map<String, Object>> executeInternal(Map<String, Object> requestMap) throws Exception;
  
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> responseMap = new HashMap<>();
    List<Map<String, Object>> klassTaxonomylist = executeInternal(requestMap);
    responseMap.put("list", klassTaxonomylist);
    return responseMap;
  }
  
  protected void getAllChildren(Vertex klassNode, List<Map<String, Object>> klasslist, int index)
      throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    
    String parentId = klassNode.getProperty(CommonConstants.CODE_PROPERTY);
    String query = "SELECT EXPAND( IN('CHILD_OF')) FROM " + getKlassVertexType() + " WHERE code=\""
        + parentId + "\"";
    Iterable<Vertex> resultIterable = graph.command(new OCommandSQL(query))
        .execute();
    index = index + 1;
    for (Vertex childklassNode : resultIterable) {
      Map<String, Object> childEntityMap = null;
      if (childklassNode != null) {
        //Skip Attribution Taxonomy which is tag and not taxonomy
        if (BooleanUtils.isTrue(klassNode.getProperty(IGetAttributionTaxonomyModel.IS_TAG))
            && BooleanUtils.isFalse(klassNode.getProperty(IGetAttributionTaxonomyModel.IS_TAXONOMY)))
          continue;

        childEntityMap = new HashMap<>();
        childEntityMap.putAll(UtilClass.getMapFromNode(childklassNode));
        getKlassInfo(childklassNode, childEntityMap);
        fillParentAndMasterTagId(childklassNode, childEntityMap);
        childEntityMap.put(ConfigTag.levelIndex.name(), index);
        klasslist.add(childEntityMap);
        getAllChildren(childklassNode, klasslist, index);
      }
    }
  }
  
  protected Map<String, Object> getKlassEntityMap(Vertex klassNode) throws Exception
  {
    Map<String, Object> klassEntityMap = new HashMap<>();
    if (klassNode != null) {
      klassEntityMap = UtilClass.getMapFromNode(klassNode);
      getKlassInfo(klassNode, klassEntityMap);
      KlassUtils.getParentInfoToKlassEntityMap(klassNode, klassEntityMap);
    }
    return klassEntityMap;
  }
  
  private void getKlassInfo(Vertex klassNode, Map<String, Object> klassEntityMap)
      throws Exception
  {
    String contextId = (String) klassEntityMap.remove(CONTEXT_ID);
    klassEntityMap.put(CONTEXT_CODE, contextId);
    KlassUtils.addTaxonomySettingToEntityMap(klassNode, klassEntityMap);
    KlassUtils.addAllowedTypes(klassNode, klassEntityMap);
    KlassUtils.addVariantContexts(klassNode, klassEntityMap);
    KlassUtils.addKlassNature(klassNode, klassEntityMap);
    KlassUtils.addKlassTasks(klassNode, klassEntityMap);
    KlassUtils.addSectionsToKlassEntityMapForExport(klassNode, klassEntityMap);
    KlassUtils.fillPropagableContextualData(klassEntityMap, klassNode);
    addLifeCycleStatusTags(klassNode, klassEntityMap);
    List<Map<String, Object>> relationships = (List<Map<String, Object>>) klassEntityMap
        .get(CommonConstants.RELATIONSHIPS);
    if (relationships != null) {
      for (Map<String, Object> relationship : relationships) {
        prepareRelationship(relationship);
      }
    }
    getType(klassEntityMap);
    prepareContext(klassEntityMap);
  }

  protected void prepareRelationship(Map<String, Object> relationship)
  {
    RelationshipUtils.prepareRelationshipSideInfoForExport(relationship);
  }
  
  private void getType(Map<String, Object> klassEntityMap)
  {
    String baseType = (String) klassEntityMap.get("baseType");
    if (baseType != null && !baseType.isEmpty()) {
      klassEntityMap.put("type", baseType);
      klassEntityMap.remove("baseType");
    }
  }
  
  private void prepareContext(Map<String, Object> klassEntityMap)
  {
    Map<String, Object> variantContexts = (Map<String, Object>) klassEntityMap.get(IKlass.CONTEXTS);
    klassEntityMap.put("promotionalVersionContextCode",
        variantContexts.get(IKlassContext.PROMOTIONAL_VERSION_CONTEXTS));
    klassEntityMap.put("productVariantContextCode",
        variantContexts.get(IKlassContext.PRODUCT_VARIANT_CONTEXTS));
    klassEntityMap.remove(IKlass.CONTEXTS);
  }
  
  private void addLifeCycleStatusTags(Vertex klassNode, Map<String, Object> klassEntityMap)
  {
    List<String> lifeCycleStatusTags = new ArrayList<>();
    Iterable<Vertex> linkedLifeCycleStatusTags = klassNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.KLASS_LIFECYCLE_STATUS_TAG_LINK);
    for (Vertex linkedLifeCycleStatusTag : linkedLifeCycleStatusTags) {
      String id = linkedLifeCycleStatusTag.getProperty(CommonConstants.CODE_PROPERTY);
      lifeCycleStatusTags.add(id);
    }
    klassEntityMap.put("statusTags", lifeCycleStatusTags);
  }
  
  protected void fillParentAndMasterTagId(Vertex klassTaxonomyVertex, Map<String, Object> klassTaxonomyMap) throws Exception
  {
    Iterable<Vertex> parentVertices = klassTaxonomyVertex.getVertices(Direction.OUT, RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
    for (Vertex vertex : parentVertices) {
      String parentCode = (String) vertex.getProperty(CommonConstants.CODE_PROPERTY);
      Vertex masterTagParentNode = TagUtils.getParentTag(klassTaxonomyVertex);
      if (masterTagParentNode != null) {
      String masterTagId = (String) masterTagParentNode.getProperty(CommonConstants.CODE_PROPERTY);
      if (!parentCode.equals(masterTagId)) {
        klassTaxonomyMap.put(ConfigTag.parentCode.name(), parentCode);
        klassTaxonomyMap.put(ConfigTag.masterParentTag.name(), masterTagId);
      }
    }
    }
  }

  protected void fillTagLevels(Vertex taxonomyNode, Map<String, Object> klassTaxonomyMap) throws Exception
  {
    List<String> tagLevelSequence = taxonomyNode.getProperty(IMasterTaxonomy.TAG_LEVEL_SEQUENCE);
    if (tagLevelSequence == null) {
      return;
    }
    List<String> levelCode = new ArrayList<>();
    List<String> levelLabel = new ArrayList<>();
    for (String tagCode : tagLevelSequence) {
      Vertex levelNode = UtilClass.getVertexById(tagCode, VertexLabelConstants.ATTRIBUTION_TAXONOMY_LEVEL);
      Vertex tagNode = levelNode
          .getVertices(Direction.IN, RelationshipLabelConstants.LEVEL_TAGGROUP_OF)
          .iterator()
          .next();
      HashMap<String, Object> levelMap = UtilClass.getMapFromNode(tagNode);
      String code = (String) levelMap.get(CommonConstants.CODE_PROPERTY);
      levelCode.add(code);
      String label = (String) levelMap.get(CommonConstants.LABEL_PROPERTY);
      if (StringUtils.isEmpty(label))
        label = code;
      levelLabel.add(label);
    }
    klassTaxonomyMap.put(ConfigTag.levelCode.name(), levelCode);
    klassTaxonomyMap.put(ConfigTag.levelLabels.name(), levelLabel);
    klassTaxonomyMap.put(ConfigTag.isNewlyCreatedLevel.toString(), new ArrayList<>(Collections.nCopies(levelCode.size(),  false)));
  }
  
  protected String getVertexLabelByEntityType(String entityType)
  {
    switch (entityType) {
      case CommonConstants.ARTICLE_ENTITY:
        return VertexLabelConstants.ENTITY_TYPE_KLASS;
      case CommonConstants.ASSET_ENTITY:
        return VertexLabelConstants.ENTITY_TYPE_ASSET;
      case CommonConstants.TARGET:
        return VertexLabelConstants.ENTITY_TYPE_TARGET;
      case CommonConstants.TEXT_ASSET_ENTITY:
        return VertexLabelConstants.ENTITY_TYPE_TEXT_ASSET;
      case CommonConstants.SUPPLIER_ENTITY:
        return VertexLabelConstants.ENTITY_TYPE_SUPPLIER;
      case CommonConstants.ATTRIBUTION_TAXONOMY:
        return VertexLabelConstants.ATTRIBUTION_TAXONOMY;
    }
    return getKlassVertexType();
  }
}
