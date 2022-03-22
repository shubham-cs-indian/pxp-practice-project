package com.cs.config.strategy.plugin.usecase.governancerule;

import com.cs.config.strategy.plugin.usecase.tag.util.TagUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.governancerule.IDrillDown;
import com.cs.core.config.interactor.entity.governancerule.IGovernanceRuleBlock;
import com.cs.core.config.interactor.exception.tag.TagNotFoundException;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.statistics.ICategoryModel;
import com.cs.core.runtime.interactor.model.statistics.IGetStatisticsRequestModel;
import com.cs.core.runtime.interactor.model.statistics.IGetStatisticsResponseModel;
import com.cs.core.runtime.interactor.model.statistics.IKPIStatisticsModel;
import com.cs.core.runtime.interactor.model.statistics.IPathModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.IteratorUtils;

public class GetConfigDetailsForGetStatistics extends AbstractOrientPlugin {
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetConfigDetailsForGetStatistics/*" };
  }
  
  public GetConfigDetailsForGetStatistics(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> request) throws Exception
  {
    String kpiId = (String) request.get(IGetStatisticsRequestModel.KPI_ID);
    String levelId = (String) request.get(IGetStatisticsRequestModel.LEVEL_ID);
    List<Map<String, Object>> path = (List<Map<String, Object>>) request.get(IGetStatisticsRequestModel.PATH);
    List<Map<String, Object>> categories = new ArrayList<>();
    Vertex kpiNode = UtilClass.getVertexById(kpiId, VertexLabelConstants.GOVERNANCE_RULE_KPI);
    Iterator<Vertex> drillDownIterator = kpiNode.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_DRILL_DOWN).iterator();
    
    // changes for kpidrill down
    
    Map<String, String> ruleBlockVsCode = getRuleBlockCodes(kpiNode);
    
    if (!drillDownIterator.hasNext()) {
      throw new NotFoundException();
    }
    Vertex drillDown = drillDownIterator.next();
    Iterable<Vertex> levelVertices = drillDown.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_DRILL_DOWN_LEVEL);
    Vertex currentlevelVertex = null;
    String currentLevelId = null;
    Boolean isLevelFound = false;
    String categoryType = null;
    Map<String, Object> referencedTags = new HashMap<>();
    Map<String, Object> referencedTaxonomies = new HashMap<>();
    Map<String, Object> referencedKlasses = new HashMap<>();
    
    List<String> typeIds = new ArrayList<>();
    Iterator<Vertex> klassIterator = kpiNode.getVertices(Direction.OUT, RelationshipLabelConstants.LINK_KLASS).iterator();
    List<Vertex> klassVerticesList = IteratorUtils.toList(klassIterator);
    for (Vertex klass : klassVerticesList) {
      typeIds.add(UtilClass.getCodeNew(klass));
    }
    
    if (levelId == null) {
      for (Vertex level : levelVertices) {
        // when levelId is null consider first level
        currentlevelVertex = level;
        break;
      }
    }
    else if (levelId.equals("0")) {
      Iterator<Vertex> taxonomyVertices = kpiNode.getVertices(Direction.OUT, RelationshipLabelConstants.LINK_TAXONOMY).iterator();
      List<Vertex> taxonomyVerticesList = IteratorUtils.toList(taxonomyVertices);
      for (Vertex taxonomyVertex : taxonomyVerticesList) {
        Map<String, Object> taxonomyMap = new HashMap<>();
        String taxonomyId = UtilClass.getCodeNew(taxonomyVertex);
        taxonomyMap.put(ICategoryModel.TYPE_ID, taxonomyId);
        taxonomyMap.put(ICategoryModel.TYPE, "taxonomy");
        taxonomyMap.put(ICategoryModel.PARENT_ID, null);
        categories.add(taxonomyMap);
        fillReferencedTaxonomy(taxonomyVertex, referencedTaxonomies);
      }
      if (taxonomyVerticesList == null || taxonomyVerticesList.isEmpty()) {
        typeIds = new ArrayList<>();
        for (Vertex klass : klassVerticesList) {
          Map<String, Object> klassMap = new HashMap<>();
          String taxonomyId = UtilClass.getCodeNew(klass);
          klassMap.put(ICategoryModel.TYPE_ID, taxonomyId);
          klassMap.put(ICategoryModel.TYPE, "klass");
          klassMap.put(ICategoryModel.PARENT_ID, null);
          categories.add(klassMap);
          fillReferencedKlass(klass, referencedKlasses);
        }
      }
    }
    else {
      for (Vertex level : levelVertices) {
        if (isLevelFound) {
          currentlevelVertex = level;
          break;
        }
        if (UtilClass.getCodeNew(level).equals(levelId)) {
          isLevelFound = true;
        }
      }
    }
    
    if (currentlevelVertex != null) {
      currentLevelId = UtilClass.getCodeNew(currentlevelVertex);
      String type = currentlevelVertex.getProperty(IDrillDown.TYPE);
      if (type.equals("tag")) {
        Iterator<Vertex> tagIterator = currentlevelVertex.getVertices(Direction.OUT, RelationshipLabelConstants.HAS_LEVEL_TAG).iterator();
        if (!tagIterator.hasNext()) {
          throw new TagNotFoundException();
        }
        Vertex tagVertex = tagIterator.next();
        fillReferencedTag(tagVertex, referencedTags);
        Iterable<Vertex> childVertices = tagVertex.getVertices(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
        String parentId = UtilClass.getCodeNew(tagVertex);
        for (Vertex child : childVertices) {
          String childId = UtilClass.getCodeNew(child);
          Map<String, Object> category = new HashMap<>();
          category.put(ICategoryModel.TYPE_ID, childId);
          category.put(ICategoryModel.TYPE, "tag");
          category.put(ICategoryModel.PARENT_ID, parentId);
          categories.add(category);
        }
      }
      else {
        Integer size = path.size();
        Map<String, Object> lastTaxonomyEntry = new HashMap<>();
        for (int i = size - 1; i >= 0; i--) {
          Map<String, Object> pathEntry = path.get(i);
          if (pathEntry.get(IPathModel.TYPE).equals("taxonomy")) {
            lastTaxonomyEntry = pathEntry;
            break;
          }
        }
        
        if (!lastTaxonomyEntry.isEmpty()) {
          String taxonomyId = (String) lastTaxonomyEntry.get(IPathModel.TYPE_ID);
          Vertex taxonomy = UtilClass.getVertexById(taxonomyId, VertexLabelConstants.ROOT_KLASS_TAXONOMY);
          Iterable<Vertex> childVertices = taxonomy.getVertices(Direction.IN, RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF);
          for (Vertex child : childVertices) {
            String childId = UtilClass.getCodeNew(child);
            Map<String, Object> category = new HashMap<>();
            category.put(ICategoryModel.TYPE_ID, childId);
            category.put(ICategoryModel.TYPE, "taxonomy");
            category.put(ICategoryModel.PARENT_ID, null);
            categories.add(category);
            fillReferencedTaxonomy(child, referencedTaxonomies);
          }
        }
      }
    }
    if (categories.size() > 0) {
      categoryType = (String) categories.get(0).get(ICategoryModel.TYPE);
    }
    
    Map<String, Object> returnMap = new HashMap<>();
    Map<String, Object> kpiStatistics = new HashMap<>();
    kpiStatistics.put(IKPIStatisticsModel.KPI_ID, kpiId);
    kpiStatistics.put(IKPIStatisticsModel.LEVEL_ID, currentLevelId);
    kpiStatistics.put(IKPIStatisticsModel.CATEGORIES, categories);
    kpiStatistics.put(IKPIStatisticsModel.PATH, path);
    kpiStatistics.put(IKPIStatisticsModel.CATEGORY_TYPE, categoryType);
    kpiStatistics.put(IKPIStatisticsModel.TYPE_IDS, typeIds);
    returnMap.put(IGetStatisticsResponseModel.KPI_STATISTICS, kpiStatistics);
    returnMap.put(IGetStatisticsResponseModel.REFERENCED_TAGS, referencedTags);
    returnMap.put(IGetStatisticsResponseModel.REFERENCED_TAXONOMIES, referencedTaxonomies);
    returnMap.put(IGetStatisticsResponseModel.REFERENCED_KLASSES, referencedKlasses);
    returnMap.put(IGetStatisticsResponseModel.RULE_BLOCK_VS_CODE, ruleBlockVsCode);
    return returnMap;
  }
  
  private Map<String, String> getRuleBlockCodes(Vertex kpiNode)
  {
    Map<String, String> referencedKpi = new HashMap<>();
    Iterable<Vertex> ruleBlocks = kpiNode.getVertices(Direction.IN, RelationshipLabelConstants.HAS_KPI);
    for (Vertex ruleBlock : ruleBlocks) {
      String type = ruleBlock.getProperty(IGovernanceRuleBlock.TYPE);
      referencedKpi.put(type, ruleBlock.getProperty(CommonConstants.CODE_PROPERTY));
    }
    return referencedKpi;
  }
  
  protected void fillReferencedTag(Vertex tagVertex, Map<String, Object> referencedTags)
      throws Exception
  {
    referencedTags.put(UtilClass.getCodeNew(tagVertex), TagUtils.getTagMap(tagVertex, true));
  }
  
  protected void fillReferencedTaxonomy(Vertex taxonomy, Map<String, Object> referencedTaxonomy)
      throws Exception
  {
    referencedTaxonomy.put(UtilClass.getCodeNew(taxonomy), UtilClass.getMapFromVertex(
        Arrays.asList(CommonConstants.CODE_PROPERTY, CommonConstants.LABEL_PROPERTY, CommonConstants.CODE_PROPERTY), taxonomy));
  }
  
  protected void fillReferencedKlass(Vertex klass, Map<String, Object> referencedklass)
      throws Exception
  {
    referencedklass.put(UtilClass.getCodeNew(klass),
        UtilClass.getMapFromVertex(Arrays.asList(CommonConstants.CODE_PROPERTY, CommonConstants.LABEL_PROPERTY), klass));
  }
}
