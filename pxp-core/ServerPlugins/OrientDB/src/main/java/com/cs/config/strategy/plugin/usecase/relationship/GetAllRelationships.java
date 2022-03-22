package com.cs.config.strategy.plugin.usecase.relationship;

import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.relationship.IConfigDetailsForRelationshipModel;
import com.cs.core.config.interactor.model.relationship.IGetAllRelationshipsResponseModel;
import com.cs.core.config.interactor.model.relationship.IRelationshipInformationModel;
import com.cs.core.config.interactor.model.tabs.ITabModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetAllRelationships extends AbstractOrientPlugin {
  
  public GetAllRelationships(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    Long from = Long.valueOf(requestMap.get(IConfigGetAllRequestModel.FROM)
        .toString());
    Long size = Long.valueOf(requestMap.get(IConfigGetAllRequestModel.SIZE)
        .toString());
    String sortBy = requestMap.get(IConfigGetAllRequestModel.SORT_BY)
        .toString();
    String sortOrder = requestMap.get(IConfigGetAllRequestModel.SORT_ORDER)
        .toString();
    String searchText = requestMap.get(IConfigGetAllRequestModel.SEARCH_TEXT)
        .toString();
    String searchColumn = requestMap.get(IConfigGetAllRequestModel.SEARCH_COLUMN)
        .toString();
    Long count = new Long(0);
    Map<String, Object> referencedTabs = new HashMap<>();
    
    if (sortBy.equals(IRelationshipInformationModel.IS_LITE)) {
      sortOrder = sortOrder.equals("asc") ? "desc" : "asc";
    }
    
    StringBuilder searchQuery = EntityUtil.getSearchQuery(searchText, searchColumn);
    sortBy = EntityUtil.getLanguageConvertedField(sortBy);
    StringBuilder condition = EntityUtil.getConditionQuery(searchQuery);
    
    String query = "select from " + VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP + condition
        + " order by " + sortBy + " " + sortOrder + " skip " + from + " limit " + size;
    List<Map<String, Object>> list = executeQueryAndPrepareResponse(query, referencedTabs);
    String countQuery = "select count(*) from " + VertexLabelConstants.ENTITY_TYPE_RELATIONSHIP
        + condition;
    count = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
    
    Map<String, Object> configMap = new HashMap<>();
    configMap.put(IConfigDetailsForRelationshipModel.REFERENCED_TABS, referencedTabs);
    
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IGetAllRelationshipsResponseModel.LIST, list);
    responseMap.put(IGetAllRelationshipsResponseModel.COUNT, count);
    responseMap.put(IGetAllRelationshipsResponseModel.CONFIG_DETAILS, configMap);
    
    return responseMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllRelationships/*" };
  }
  
  /**
   * @author Ajit
   * @param query
   * @param referencedTabs
   * @return
   * @throws Exception
   */
  private List<Map<String, Object>> executeQueryAndPrepareResponse(String query,
      Map<String, Object> referencedTabs) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> searchResults = graph.command(new OCommandSQL(query))
        .execute();
    List<Map<String, Object>> relationshipsToReturn = new ArrayList<>();
    for (Vertex relationshipNode : searchResults) {
      Map<String, Object> relationshipMap = UtilClass.getMapFromVertex(
          Arrays.asList(IRelationshipInformationModel.ID, IRelationshipInformationModel.CODE,
              IRelationshipInformationModel.LABEL, IRelationshipInformationModel.TYPE,
              IRelationshipInformationModel.ICON, IRelationshipInformationModel.IS_STANDARD,
              IRelationshipInformationModel.IS_LITE
              ),
          relationshipNode);
      Map<String, Object> referencedTab = TabUtils.getMapFromConnectedTabNode(relationshipNode,
          Arrays.asList(CommonConstants.CODE_PROPERTY, IIdLabelModel.LABEL,
              CommonConstants.CODE_PROPERTY));
      relationshipMap.put(IRelationshipInformationModel.TAB_ID, referencedTab.get(ITabModel.ID));
      relationshipsToReturn.add(relationshipMap);
      referencedTabs.put((String) referencedTab.get(ITabModel.ID), referencedTab);
    }
    return relationshipsToReturn;
  }
}
