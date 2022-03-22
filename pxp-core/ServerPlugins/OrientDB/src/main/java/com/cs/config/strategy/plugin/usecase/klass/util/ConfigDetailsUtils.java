package com.cs.config.strategy.plugin.usecase.klass.util;

import com.cs.config.strategy.plugin.usecase.datarule.util.GetDataRuleUtils;
import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.entity.tag.ITag;
import com.cs.core.config.interactor.exception.taxonomy.KlassTaxonomyNotFoundException;
import com.cs.core.config.interactor.model.klass.IGetMultiClassificationKlassDetailsModel;
import com.cs.core.exception.KlassNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForInstanceTreeGetModel;
import com.cs.core.runtime.interactor.model.templating.IGetConfigDetailsForCustomTabModel;
import com.cs.repository.relationship.RelationshipRepository;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class ConfigDetailsUtils {
  
  /**
   * fills referencedKlasses in mapToReturn & also natureKlassNode from all
   * klasses in klassIds in klassNodesList and taxonomyNodesList respectively
   * Also fill referencedDataRules, referencedEventsIds and lifeCycleStatusTags
   * associated to klasses. Also fill numberOfVersionsToMaintain
   *
   * @author Lokesh
   * @param mapToReturn
   * @param nodeLabel
   * @param klassIds
   * @param natureKlassNode
   * @throws Exception
   */
  public static Vertex fillKlassDetailsAndGetNatureKlassNode(Map<String, Object> mapToReturn,
      String nodeLabel, List<String> klassIds, Map<String, Object> referencedDataRuleMap)
      throws Exception
  {
    Vertex natureKlassNode = null;
    Map<String, Object> referencedKlassMap = (Map<String, Object>) mapToReturn
        .get(IGetConfigDetailsForCustomTabModel.REFERENCED_KLASSES);
    
    for (String klassId : klassIds) {
      Vertex klassVertex = null;
      try {
        klassVertex = UtilClass.getVertexById(klassId, nodeLabel);
      }
      catch (NotFoundException e) {
        throw new KlassNotFoundException();
      }
      String natureType = klassVertex.getProperty(IKlass.NATURE_TYPE);
      if (natureType != null && !natureType.isEmpty()) {
        natureKlassNode = klassVertex;
      }
      List<String> fieldsToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
          IKlass.NUMBER_OF_VERSIONS_TO_MAINTAIN, IKlass.ICON, IKlass.LABEL, IKlass.TYPE,
          IKlass.NATURE_TYPE, IKlass.IS_NATURE);
      Map<String, Object> klassMap = UtilClass.getMapFromVertex(fieldsToFetch, klassVertex);
      
      Integer numberOfVersionsToMaintain = (Integer) klassMap
          .get(IKlass.NUMBER_OF_VERSIONS_TO_MAINTAIN);
      Integer existingNumberOfVersionsToMaintain = (Integer) mapToReturn
          .get(IGetConfigDetailsForCustomTabModel.NUMBER_OF_VERSIONS_TO_MAINTAIN);
      if (numberOfVersionsToMaintain > existingNumberOfVersionsToMaintain) {
        mapToReturn.put(IGetConfigDetailsForCustomTabModel.NUMBER_OF_VERSIONS_TO_MAINTAIN,
            numberOfVersionsToMaintain);
      }
      
      fillReferencedTagsAndLifeCycleStatusTags(mapToReturn, klassVertex);
      fillDataRulesOfKlass(klassVertex, referencedDataRuleMap);
      
      referencedKlassMap.put(klassId, klassMap);
    }
    return natureKlassNode;
  }
  
  public static void fillDataRulesOfKlass(Vertex klassNode,
      Map<String, Object> referencedDataRuleMap) throws Exception
  {
    Iterable<Edge> dataRuleEdges = klassNode.getEdges(Direction.OUT,
        RelationshipLabelConstants.DATA_RULES);
    for (Edge dataRuleEdge : dataRuleEdges) {
      Vertex dataRuleNode = dataRuleEdge.getVertex(Direction.IN);
      String dataRuleId = UtilClass.getCodeNew(dataRuleNode);
      if (referencedDataRuleMap.get(dataRuleId) != null) {
        continue;
      }
      
      Map<String, Object> dataRuleMap = GetDataRuleUtils.getDataRuleFromNode(dataRuleNode, true);
      referencedDataRuleMap.put(dataRuleId, dataRuleMap);
    }
  }
    
  public static void fillReferencedTagsAndLifeCycleStatusTags(Map<String, Object> mapToReturn,
      Vertex klassVertex) throws Exception
  {
    List<String> referencedLifeCycleStatusTags = (List<String>) mapToReturn
        .get(IGetMultiClassificationKlassDetailsModel.REFERENCED_LIFECYCLE_STATUS_TAGS);
    Map<String, Object> referencedTagMap = (Map<String, Object>) mapToReturn
        .get(IGetMultiClassificationKlassDetailsModel.REFERENCED_TAGS);
    Iterable<Vertex> linkedLifeCycleStatusTags = klassVertex.getVertices(Direction.OUT,
        RelationshipLabelConstants.KLASS_LIFECYCLE_STATUS_TAG_LINK);
    for (Vertex linkedLifeCycleStatusTag : linkedLifeCycleStatusTags) {
      String id = linkedLifeCycleStatusTag.getProperty(CommonConstants.CODE_PROPERTY);
      if (!referencedLifeCycleStatusTags.contains(id)) {
        referencedLifeCycleStatusTags.add(id);
      }
      Vertex linkedTagNode = UtilClass.getVertexById(id, VertexLabelConstants.ENTITY_TAG);
      Map<String, Object> referencedTag = TagUtils.getTagMap(linkedTagNode, true);
      String tagId = (String) referencedTag.get(ITag.ID);
      referencedTagMap.put(tagId, referencedTag);
    }
  }
  
  /**
   * fill referencedTemplates. fill all templates associated with
   * klassNode(klassIds) and taxonomyNodes(taxonomyIds)
   *
   * @param userId
   * @param klassIds
   * @param taxonomyIds
   * @param mapToReturn
   * @param nodeLabel
   * @throws Exception
   */
  /*
  public static void fillReferencedTemplates(List<String> collectionIds, List<String> klassIds, List<String> taxonomyIds, Map<String, Object> mapToReturn, String nodeLabel) throws Exception{
  
    List<Map<String,Object>> templates = (List<Map<String, Object>>) mapToReturn.get(IGetConfigDetailsModel.REFERENCED_TEMPLATES);
    templates.clear();
    for (String klassId : klassIds) {
      Vertex klassNode = null;
      try {
        klassNode = UtilClass.getVertexById(klassId, nodeLabel);
      } catch (NotFoundException e) {
        throw new KlassNotFoundException();
      }
      Vertex templateNode = TemplateUtils.getTemplateFromKlass(klassNode);
      Map<String, Object> templateMap = UtilClass.getMapFromNode(templateNode);
      templates.add(templateMap);
    }
  
    for (String collectionId : collectionIds) {
      Vertex collectionNode = null;
      try {
        collectionNode = UtilClass.getVertexById(collectionId, VertexLabelConstants.COLLECTION);
      } catch (NotFoundException e) {
        throw new CollectionNodeNotFoundException();
      }
      Vertex templateNode = TemplateUtils.getTemplateFromKlassIfExist(collectionNode);
      if (templateNode == null) {
        continue;
      }
      Map<String, Object> templateMap = UtilClass.getMapFromNode(templateNode);
      templates.add(templateMap);
    }
  
    for (String taxonomyId : taxonomyIds) {
      Vertex taxonomyNode = null;
      try {
        taxonomyNode = UtilClass.getVertexById(taxonomyId, VertexLabelConstants.KLASS_TAXONOMY);
      } catch (NotFoundException e) {
        throw new KlassTaxonomyNotFoundException();
      }
      Vertex templateNode = TemplateUtils.getTemplateFromKlassIfExist(taxonomyNode);
      if(templateNode == null ) {
        continue;
      }
      Map<String, Object> templateMap = UtilClass.getMapFromNode(templateNode);
      if(templates.contains(templateMap)) {
        continue;
      }
      templates.add(templateMap);
    }
  
    String query = "select from " + VertexLabelConstants.TEMPLATE + " where " + ITemplate.TYPE
        + " = '" + CommonConstants.CUSTOM_TEMPLATE + "'";
    Iterable<Vertex> templateVertices =  UtilClass.getGraph().command(new OCommandSQL(query)).execute();
    for (Vertex templateNode : templateVertices) {
      Map<String, Object> templateMap = UtilClass.getMapFromNode(templateNode);
      templates.add(templateMap);
    }
  }
  */
  
  public static List<String> getContextTagIds(Vertex natureRelationshipNode,
      Map<String, Object> referencedTagsMap) throws Exception
  {
    List<String> contextTagIds = new ArrayList<>();
    Iterable<Vertex> contextTagVertices = natureRelationshipNode.getVertices(Direction.OUT,
        RelationshipLabelConstants.HAS_RELATIONSHIP_CONTEXT_TAG);
    for (Vertex contextTagVertex : contextTagVertices) {
      String contextTagId = UtilClass.getCodeNew(contextTagVertex);
      contextTagIds.add(contextTagId);
      if (!referencedTagsMap.containsKey(contextTagId)) {
        Map<String, Object> referencedTag = TagUtils.getTagMap(contextTagVertex, true);
        referencedTagsMap.put(contextTagId, referencedTag);
      }
    }
    return contextTagIds;
  }
  
  public static void fillSide2LinkedVariantKrIds(Map<String, Object> mapToReturn)
  {
    mapToReturn.put(IConfigDetailsForInstanceTreeGetModel.SIDE2_LINKED_VARIANT_KR_IDS,
        RelationshipRepository.fetchAllSide2LinkedVariantKrIds());
  }
  
  public static void fillLinkedVariantsConfigInfo(Map<String, Object> mapToReturn,
      List<String> klassIds)
  {
    String quotedKlassIds = EntityUtil.quoteIt(klassIds);
    String subQuery = "(select from " + VertexLabelConstants.ENTITY_TYPE_ROOT_KLASS
        + " where code in " + quotedKlassIds + ")";
    List<String> linkedVariantCodes = RelationshipRepository
        .fetchApplicableLinkedVariantCodes(subQuery);
    mapToReturn.put(IGetConfigDetailsForCustomTabModel.LINKED_VARIANT_CODES,
        linkedVariantCodes);
  }
  
  public static void fillLinkedVariantPropertyCodes(Map<String, Object> mapToReturn)
  {
    mapToReturn.put(IConfigDetailsForInstanceTreeGetModel.LINKED_VARIANT_CODES,
        RelationshipRepository.fetchAllLinkedVariantCodes());
  }
}
