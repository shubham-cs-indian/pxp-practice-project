package com.cs.config.strategy.plugin.usecase.taxonomy.article;

import com.cs.config.strategy.plugin.usecase.attributiontaxonomy.util.AttributionTaxonomyUtil;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.taxonomy.ITaxonomy;
import com.cs.core.config.interactor.exception.taxonomy.KlassTaxonomyNotFoundException;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.exception.NotFoundException;
import com.cs.core.runtime.interactor.constants.application.RelationshipLabelConstants;
import com.cs.core.runtime.interactor.model.taxonomy.ITaxonomySearchStrategyModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractSearchTaxonomyByLabel extends AbstractOrientPlugin {
  
  public AbstractSearchTaxonomyByLabel(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  public abstract String getVertexType();
  
  public abstract String getKlassVertexType();
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String searchText = (String) requestMap.get(ITaxonomySearchStrategyModel.SEARCH_TEXT);
    
    Map<String, Object> mapToReturn = new HashMap<>();
    
    String parentTaxonomyId = (String) requestMap
        .get(ITaxonomySearchStrategyModel.PARENT_TAXONOMY_ID);
    Vertex parentTaxonomyNode = null;
    try {
      parentTaxonomyNode = UtilClass.getVertexById(parentTaxonomyId, getKlassVertexType());
    }
    catch (NotFoundException e) {
      throw new KlassTaxonomyNotFoundException(e);
    }
    
    if (searchText.isEmpty()) {
      Map<String, Object> parentTaxonomyMap = UtilClass.getMapFromVertex(
          Arrays.asList(CommonConstants.CODE_PROPERTY, ITaxonomy.LABEL), parentTaxonomyNode);
      parentTaxonomyMap.put(ITaxonomy.CHILDREN, new ArrayList<>());
      mapToReturn.put(IListModel.LIST, Arrays.asList(parentTaxonomyMap));
      return mapToReturn;
    }
    
    String parentTaxonomyRID = parentTaxonomyNode.getId()
        .toString();
    
    String queryToExecute = "select from (traverse in('"
        + RelationshipLabelConstants.RELATIONSHIPLABEL_CHILD_OF + "') from " + parentTaxonomyRID
        + ") where " + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY)
        + " like '%" + searchText + "%'";
    Iterable<Vertex> vertices = UtilClass.getGraph()
        .command(new OCommandSQL(queryToExecute))
        .execute();
    
    Map<String, Object> masterTaxonomyNodesList = new HashMap<>();
    
    for (Vertex taxonomyVertex : vertices) {
      generateTaxonomyTree(taxonomyVertex, masterTaxonomyNodesList);
    }
    
    List<Object> listToReturn = new ArrayList<>();
    if (masterTaxonomyNodesList.containsKey(parentTaxonomyId)) {
      listToReturn.add(masterTaxonomyNodesList.get(parentTaxonomyId));
    }
    
    mapToReturn.put(IListModel.LIST, listToReturn);
    return mapToReturn;
  }
  
  private void generateTaxonomyTree(Vertex taxonomyVertex, Map<String, Object> masterTaxonomyNodes)
      throws Exception
  {
    String taxonomyId = UtilClass.getCodeNew(taxonomyVertex);
    if (masterTaxonomyNodes.containsKey(taxonomyId)) {
      return;
    }
    Map<String, Object> taxonomyMap = UtilClass.getMapFromVertex(
        Arrays.asList(CommonConstants.CODE_PROPERTY, ITaxonomy.LABEL), taxonomyVertex);
    taxonomyMap.put(ITaxonomy.CHILDREN, new ArrayList<>());
    masterTaxonomyNodes.put(taxonomyId, taxonomyMap);
    
    Vertex parentTaxonomyVertex = AttributionTaxonomyUtil.getParentTaxonomy(taxonomyVertex);
    
    if (parentTaxonomyVertex == null) {
      return;
    }
    String parentTaxonomyId = UtilClass.getCodeNew(parentTaxonomyVertex);
    Map<String, Object> parentTaxonomyMap = (Map<String, Object>) masterTaxonomyNodes
        .get(parentTaxonomyId);
    if (parentTaxonomyMap == null) {
      generateTaxonomyTree(parentTaxonomyVertex, masterTaxonomyNodes);
    }
    parentTaxonomyMap = (Map<String, Object>) masterTaxonomyNodes.get(parentTaxonomyId);
    ((List<Map<String, Object>>) parentTaxonomyMap.get(ITaxonomy.CHILDREN)).add(taxonomyMap);
  }
}
