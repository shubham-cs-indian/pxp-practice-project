package com.cs.config.strategy.plugin.usecase.getentityconfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationResponseModel;
import com.cs.core.config.interactor.model.configdetails.IUsageSummary;
import com.cs.core.config.interactor.model.configdetails.IUsedBy;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.constants.application.EntityConfigurationConstants;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class GetEntityConfigurationUtils {
  
  /**
   * Description: set reference data and usage summary from details
   *
   * @author yogesh.mandaokar
   * @param details
   * @param referenceData
   * @param usageSummary
   */
  @SuppressWarnings("unchecked")
  public static void setReferenceDataAndUsageSummary(Map<String, Object> details,
      Map<String, Object> referenceData, List<Map<String, Object>> usageSummary)
  {
    referenceData.putAll(
        (Map<String, Object>) details.get(IGetEntityConfigurationResponseModel.REFERENCE_DATA));
    usageSummary
        .add((Map<String, Object>) details.get(IGetEntityConfigurationResponseModel.USAGE_SUMMARY));
  }
  
  public static Map<String, Object> getUsageSummary(Vertex node)
  {
    return UtilClass.getMapFromVertex(Arrays.asList(IIdLabelCodeModel.CODE, IIdLabelCodeModel.LABEL,
        CommonConstants.CODE_PROPERTY), node);
  }
  
  /**
   * Description: modify data according to response
   *
   * @author yogesh.mandaokar
   * @param vertices
   * @param referenceData
   * @param usedBy
   * @param usageSummary
   * @param entityType
   */
  public static void setConfigurationDetails(Iterable<Vertex> vertices,
      Map<String, Object> referenceData, List<Object> usedBy, Map<String, Object> usageSummary,
      String entityType)
  {
    Map<String, Object> propertyCollection = new HashMap<String, Object>();
    propertyCollection.put(IUsedBy.ENTITY_TYPE, entityType);
    Set<String> linkedIds = new HashSet<String>();
    Integer count = new Integer(0);
    for (Vertex vertex : vertices) {
      Map<String, Object> configDetail = getUsageSummary(vertex);
      String cid = (String) configDetail.get(IIdLabelCodeModel.ID);
      linkedIds.add(cid);
      referenceData.put(cid, configDetail);
      count++;
    }
    Integer totalCount = (Integer) usageSummary.get(IUsageSummary.TOTAL_COUNT);
    usageSummary.put(IUsageSummary.TOTAL_COUNT, totalCount + count);
    propertyCollection.put(IUsedBy.LINKED_IDS, linkedIds);
    usedBy.add(propertyCollection);
  }
  
  public static Map<String, Object> getIdLabelCodeFromVertex(Vertex attributeNode)
  {
    return UtilClass.getMapFromVertex(Arrays.asList(IIdLabelCodeModel.CODE, IIdLabelCodeModel.LABEL,
        CommonConstants.CODE_PROPERTY), attributeNode);
  }
  
  /**
   * Description: get the vertices using one level graph API
   *
   * @author yogesh.mandaokar
   * @param attributeNode
   * @param direction
   * @param relationshipLabel
   * @return
   */
  public static Iterable<Vertex> getVerticesByGraphAPI(Vertex attributeNode, Direction direction,
      String relationshipLabel)
  {
    return attributeNode.getVertices(direction, relationshipLabel);
  }
  
  /**
   * Description: get the vertices using one level graph API for icon entity
   *
   * @param attributeNode
   * @param direction
   * @param relationshipLabel
   * @param entityType
   * @return
   */
  public static Iterable<Vertex> getVerticesByQueryForIcon(Vertex attributeNode, Direction direction, String relationshipLabel,
      String entityType)
  {
    String vertexKlassLabel = EntityUtil.getVertexLabelByEntityTypeForIcon(entityType);
    String iconCode = attributeNode.getProperty(CommonConstants.CODE_PROPERTY);
    String query = "select from (traverse in('Has_Icon') from (Select from Icon where code = '" + iconCode +"')) where @class = '"+vertexKlassLabel + "'";
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> linkedEntityVertices = graph.command(new OCommandSQL(query)).execute();
    return linkedEntityVertices;
  }
  
  /**
   * Description: get the vertices using query
   *
   * @author yogesh.mandaokar
   * @param query
   * @return
   */
  public static Iterable<Vertex> getVerticesByQuery(String query)
  {
    return UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
  }
  
  /**
   * Description: generate two level OrientDB query using parameters
   *
   * @author yogesh.mandaokar
   * @param id
   * @param firstLevel
   * @param secondLevel
   * @param vertex
   * @return
   */
  public static String getTwoLevelQuery(String nodeId, Direction directionOne,
      Direction directionTwo, String firstLevel, String secondLevel)
  {
    return "select expand(" + directionOne + "('" + firstLevel + "')." + directionTwo + "('"
        + secondLevel + "')) from " + nodeId;
  }
  
  /**
   * Description: fetch data using graph API (single level)
   *
   * @author yogesh.mandaokar
   * @param attributeNode
   * @param direction
   * @param relationshipLabel
   * @param referenceData
   * @param usedBy
   * @param usageSummary
   * @param entityType
   */
  public static void setByGraphAPI(Vertex attributeNode, Direction direction,
      String relationshipLabel, Map<String, Object> referenceData, List<Object> usedBy,
      Map<String, Object> usageSummary, String entityType)
  {
    setConfigurationDetails(getVerticesByGraphAPI(attributeNode, direction, relationshipLabel),
        referenceData, usedBy, usageSummary, entityType);
  }
  
  /**
   * Description: fetch data using graph API (single level) for icon entity.
   *
   * @param attributeNode
   * @param direction
   * @param relationshipLabel
   * @param referenceData
   * @param usedBy
   * @param usageSummary
   * @param entityType
   */
  public static void setByGraphAPIForIcon(Vertex attributeNode, Direction direction, String relationshipLabel,
      Map<String, Object> referenceData, List<Object> usedBy, Map<String, Object> usageSummary, String entityType)
  {
    setConfigurationDetails(getVerticesByQueryForIcon(attributeNode, direction, relationshipLabel, entityType), referenceData, usedBy,
        usageSummary, entityType);
  }
  
  /**
   * Description: get query as parameter execute and serve to the response
   * modifier
   *
   * @author yogesh.mandaokar
   * @param query
   * @param referenceData
   * @param usedBy
   * @param usageSummary
   * @param entityType
   */
  public static void setByQuery(String query, Map<String, Object> referenceData,
      List<Object> usedBy, Map<String, Object> usageSummary, String entityType)
  {
    setConfigurationDetails(getVerticesByQuery(query), referenceData, usedBy, usageSummary,
        entityType);
  }
  
  /**
   * Description: create query in which end vertex two level from given vertex
   *
   * @author yogesh.mandaokar
   * @param id
   * @param referenceData
   * @param usedBy
   * @param usageSummary
   * @param firstLevel
   * @param secondLevel
   * @param vertex
   * @param entityType
   */
  public static void setTwoLevelQuery(String nodeId, Direction directionOne, Direction directionTwo,
      Map<String, Object> referenceData, List<Object> usedBy, Map<String, Object> usageSummary,
      String firstLevel, String secondLevel, String entityType)
  {
    setByQuery(getTwoLevelQuery(nodeId, directionOne, directionTwo, firstLevel, secondLevel),
        referenceData, usedBy, usageSummary, entityType);
  }
  
  public static String getThreeLevelQuery(String nodeId, Direction directionOne,
      Direction directionTwo, Direction directionThree, String firstLevel, String secondLevel,
      String thirdLevel)
  {
    return "select expand(" + directionOne + "('" + firstLevel + "')." + directionTwo + "('"
        + secondLevel + "')." + directionThree + "('" + thirdLevel + "') )from " + nodeId;
    
  }
  /**
   * Description: KPI vertex fetch who has four level
   *
   * @author yogesh.mandaokar
   * @param id
   * @param referenceData
   * @param usedBy
   * @param usageSummary
   * @param firstLevel
   * @param secondLevel
   * @param vertex
   * @param entityType
   */
  public static void setKPIConfiguration(String nodeId, Map<String, Object> referenceData,
      List<Object> usedBy, Map<String, Object> usageSummary, String firstLevel, String secondLevel,
      String vertex, String entityType)
  {
    String query = "select expand(in('" + firstLevel + "').in('" + secondLevel + "').in('"
        + RelationshipLabelConstants.HAS_GOVERNANCE_RULE + "').out('"
        + RelationshipLabelConstants.HAS_KPI + "')) from " + nodeId;
    setByQuery(query, referenceData, usedBy, usageSummary, entityType);
  }
  
  /**
   * Description: Golden record vertex fetch having three level
   *
   * @author yogesh.mandaokar
   * @param id
   * @param referenceData
   * @param usedBy
   * @param usageSummary
   * @param vertex
   * @param entityType
   */
  public static void setGoldenRecordMergeConfiguration(String nodeId,
      Map<String, Object> referenceData, List<Object> usedBy, Map<String, Object> usageSummary,
      String vertex, String entityType, String firstLevel)
  {
    String query = "select expand(in('" + firstLevel + "').in('"
        + RelationshipLabelConstants.MERGE_EFFECT_TYPE_LINK + "').in('"
        + RelationshipLabelConstants.GOLDEN_RECORD_RULE_MERGE_EFFECT_LINK + "')) from " + nodeId;
    setByQuery(query, referenceData, usedBy, usageSummary, entityType);
  }
  
  /**
   * Description: fetch all level children using id and vertex name.
   *
   * @author yogesh.mandaokar
   * @param klassId
   * @param vertex
   * @return
   * @throws Exception
   */
  public static Iterable<Vertex> getChildByIdAndVertex(String klassId, String vertex)
      throws Exception
  {
    Vertex klassNode = UtilClass.getVertexByIndexedId(klassId, vertex);
    return getChildNodesFromParentNodeId((String) klassNode.getId());
  }
  
  /**
   * Description: fetch all level children using node id
   *
   * @author yogesh.mandaokar
   * @param nodeId
   * @return
   */
  public static Iterable<Vertex> getChildNodesFromParentNodeId(String nodeId)
  {
    String query = "select from (traverse in('"
        + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF + "') from " + nodeId
        + "STRATEGY BREADTH_FIRST)";
    
    return getVerticesByQuery(query);
  }
  
  /**
   * Description: elaborate rules with their specification
   *
   * @author yogesh.mandaokar
   * @param vertices
   * @param referenceData
   * @param usedBy
   * @param usageSummary
   */
  public static void elaborateRules(Iterable<Vertex> vertices, Map<String, Object> referenceData,
      List<Object> usedBy, Map<String, Object> usageSummary)
  {
    List<Vertex> classification = new ArrayList<Vertex>();
    List<Vertex> standard = new ArrayList<Vertex>();
    List<Vertex> violation = new ArrayList<Vertex>();
    for (Vertex vertex : vertices) {
      if (vertex.getProperty("type")
          .equals(Constants.STANDARDIZATION_AND_NORMALIZATION)) {
        standard.add(vertex);
      }
      if (vertex.getProperty("type")
          .equals(Constants.CLASSIFICATION)) {
        classification.add(vertex);
      }
      if (vertex.getProperty("type")
          .equals(Constants.VIOLATION)) {
        violation.add(vertex);
      }
    }
    
    /* standard rule */
    setConfigurationDetails(standard, referenceData, usedBy, usageSummary,
        EntityConfigurationConstants.RULE_STANDARD_AND_NORMALIZATION);
    
    /* classification rule */
    setConfigurationDetails(classification, referenceData, usedBy, usageSummary,
        EntityConfigurationConstants.RULE_CLASSIFICATION);
    
    /* violation rule */
    setConfigurationDetails(violation, referenceData, usedBy, usageSummary,
        EntityConfigurationConstants.RULE_VIOLATION);
  }
  
  /**
   * Description: elaborate linked klass with their specification
   *
   * @author yogesh.mandaokar
   * @param vertices
   * @param referenceData
   * @param usedBy
   * @param usageSummary
   */
  public static void elaborateLinkedKlass(Iterable<Vertex> vertices,
      Map<String, Object> referenceData, List<Object> usedBy, Map<String, Object> usageSummary)
  {
    List<Vertex> pid = new ArrayList<Vertex>();
    List<Vertex> sop = new ArrayList<Vertex>();
    List<Vertex> fixedBundle = new ArrayList<Vertex>();
    
    for (Vertex vertex : vertices) {
      if (vertex.getProperty("natureType")
          .equals(Constants.PID)) {
        pid.add(vertex);
      }
      if (vertex.getProperty("natureType")
          .equals(Constants.SET_OF_PRODUCTS)) {
        sop.add(vertex);
      }
      if (vertex.getProperty("natureType")
          .equals(Constants.FIXED_BUNDLE)) {
        fixedBundle.add(vertex);
      }
    }
    
    /* klass(PID) */
    setConfigurationDetails(pid, referenceData, usedBy, usageSummary,
        EntityConfigurationConstants.KLASS_PID);
    
    /* klass(SOP) */
    setConfigurationDetails(sop, referenceData, usedBy, usageSummary,
        EntityConfigurationConstants.KLASS_SOP);
    
    /* fixed bundle */
    setConfigurationDetails(fixedBundle, referenceData, usedBy, usageSummary,
        EntityConfigurationConstants.KLASS_FIXED_BUNDLE);
  }
  
  public static void elaborateKlass(Iterable<Vertex> vertices, Map<String, Object> referenceData,
      List<Object> usedBy, Map<String, Object> usageSummary)
  {
    /* Embedded */
    List<Vertex> embedded = new ArrayList<Vertex>();
    
    /* Article */
    List<Vertex> fixedBundle = new ArrayList<Vertex>();
    List<Vertex> individualArticle = new ArrayList<Vertex>();
    List<Vertex> standardIdentifier = new ArrayList<Vertex>();
    List<Vertex> pid = new ArrayList<Vertex>();
    List<Vertex> sop = new ArrayList<Vertex>();
    
    /* Asset */
    List<Vertex> document = new ArrayList<Vertex>();
    List<Vertex> file = new ArrayList<Vertex>();
    List<Vertex> technicleImage = new ArrayList<Vertex>();
    List<Vertex> video = new ArrayList<Vertex>();
    List<Vertex> image = new ArrayList<Vertex>();
    
    /* Market */
    List<Vertex> market = new ArrayList<Vertex>();
    
    /* Text Asset */
    List<Vertex> textAsset = new ArrayList<Vertex>();
    
    /* Virtual Catalog */
    List<Vertex> product = new ArrayList<Vertex>();
    List<Vertex> media = new ArrayList<Vertex>();
    
    /* Non nature */
    List<Vertex> nonNature = new ArrayList<Vertex>();
    
    /* default */
    List<Vertex> klass = new ArrayList<Vertex>();
    
    for (Vertex vertex : vertices) {
      if ((boolean) vertex.getProperty("isNature")) {
        
        switch (vertex.getProperty("natureType")
            .toString()) {
          
          /* Embedded */
          case Constants.EMBEDDED:
            embedded.add(vertex);
            break;
          
          /* Article */
          case Constants.FIXED_BUNDLE:
            fixedBundle.add(vertex);
            break;
          case Constants.INDIVIDUAL_ARTICLE:
            individualArticle.add(vertex);
            break;
          case Constants.STANDARD_IDENTIFIER:
            standardIdentifier.add(vertex);
            break;
          case Constants.PID:
            pid.add(vertex);
            break;
          case Constants.SET_OF_PRODUCTS:
            sop.add(vertex);
            break;
          
          /* Asset */
          case CommonConstants.MAM_NATURE_TYPE_DOCUMENT:
            document.add(vertex);
            break;
          case CommonConstants.MAM_NATURE_TYPE_FILE:
            file.add(vertex);
            break;
          case CommonConstants.TECHNICAL_IMAGE_VARIANT_KLASS_TYPE:
            technicleImage.add(vertex);
            break;
          case CommonConstants.MAM_NATURE_TYPE_VIDEO:
            video.add(vertex);
            break;
          case CommonConstants.MAM_NATURE_TYPE_IMAGE:
            image.add(vertex);
            break;
          
          /* Market */
          case CommonConstants.MARKET_ENTITY:
            market.add(vertex);
            break;
          
          /* Text Asset */
          case CommonConstants.TEXT_ASSET_ENTITY:
            textAsset.add(vertex);
            break;
          
          /* Virtual Catalog */
          case EntityConfigurationConstants.PRODUCT_CATALOG:
            product.add(vertex);
            break;
          case EntityConfigurationConstants.MEDIA_CATALOG:
            media.add(vertex);
            break;
          
          default:
            klass.add(vertex);
            break;
        }
        
      }
      else {
        nonNature.add(vertex);
      }
    }
    
    /* Embedded */
    setConfigurationDetails(embedded, referenceData, usedBy, usageSummary,
        EntityConfigurationConstants.KLASS_EMBEDDED);
    
    /* Article */
    setConfigurationDetails(fixedBundle, referenceData, usedBy, usageSummary,
        EntityConfigurationConstants.KLASS_FIXED_BUNDLE);
    setConfigurationDetails(individualArticle, referenceData, usedBy, usageSummary,
        EntityConfigurationConstants.KLASS_INDIVIDUAL_ARTICLE);
    setConfigurationDetails(standardIdentifier, referenceData, usedBy, usageSummary,
        EntityConfigurationConstants.KLASS_STANDARD_IDENTIFIER);
    setConfigurationDetails(pid, referenceData, usedBy, usageSummary,
        EntityConfigurationConstants.KLASS_PID);
    setConfigurationDetails(sop, referenceData, usedBy, usageSummary,
        EntityConfigurationConstants.KLASS_SOP);
    
    
    /* Asset */
    setConfigurationDetails(document, referenceData, usedBy, usageSummary,
        EntityConfigurationConstants.KLASS_DOCUMENT);
    setConfigurationDetails(file, referenceData, usedBy, usageSummary,
        EntityConfigurationConstants.KLASS_FILE);
    setConfigurationDetails(technicleImage, referenceData, usedBy, usageSummary,
        EntityConfigurationConstants.KLASS_TECHNICAL_IMAGE);
    setConfigurationDetails(video, referenceData, usedBy, usageSummary,
        EntityConfigurationConstants.KLASS_VIDEO);
    setConfigurationDetails(image, referenceData, usedBy, usageSummary,
        EntityConfigurationConstants.KLASS_IMAGE);
    
    /* Market */
    setConfigurationDetails(market, referenceData, usedBy, usageSummary,
        EntityConfigurationConstants.KLASS_MARKET);
    
    /* Text Asset */
    setConfigurationDetails(textAsset, referenceData, usedBy, usageSummary,
        EntityConfigurationConstants.KLASS_TEXT_ASSET);
    
    /* Virtual Catalog */
    setConfigurationDetails(product, referenceData, usedBy, usageSummary,
        EntityConfigurationConstants.KLASS_PRODUCT);
    setConfigurationDetails(media, referenceData, usedBy, usageSummary,
        EntityConfigurationConstants.KLASS_MEDIA);
    
    /* Non nature */
    setConfigurationDetails(nonNature, referenceData, usedBy, usageSummary,
        EntityConfigurationConstants.KLASS_NON_NATURE);
    
    /* default */
    setConfigurationDetails(klass, referenceData, usedBy, usageSummary,
        EntityConfigurationConstants.KLASS);
  }
}
