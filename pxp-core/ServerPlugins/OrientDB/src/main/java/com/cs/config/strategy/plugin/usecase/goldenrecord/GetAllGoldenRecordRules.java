package com.cs.config.strategy.plugin.usecase.goldenrecord;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.goldenrecord.IGoldenRecordRule;
import com.cs.core.config.interactor.model.goldenrecord.IGetAllGoldenRecordRulesModel;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
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

public class GetAllGoldenRecordRules extends AbstractOrientPlugin {
  
  public GetAllGoldenRecordRules(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
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
    
    StringBuilder searchQuery = EntityUtil.getSearchQuery(searchText, searchColumn);
    sortBy = EntityUtil.getLanguageConvertedField(sortBy);
    
    StringBuilder conditionQuery = EntityUtil.getConditionQuery(searchQuery);
    
    String query = "select from " + VertexLabelConstants.GOLDEN_RECORD_RULE + conditionQuery
        + " order by " + sortBy + " " + sortOrder + " skip " + from + " limit " + size;
    List<Map<String, Object>> list = executeQueryAndPrepareResponse(query);
    String countQuery = "select count(*) from " + VertexLabelConstants.GOLDEN_RECORD_RULE
        + conditionQuery;
    count = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
    
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IGetAllGoldenRecordRulesModel.LIST, list);
    responseMap.put(IGetAllGoldenRecordRulesModel.COUNT, count);
    return responseMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllGoldenRecordRules/*" };
  }
  
  private List<Map<String, Object>> executeQueryAndPrepareResponse(String query) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    Iterable<Vertex> searchResults = graph.command(new OCommandSQL(query))
        .execute();
    List<Map<String, Object>> goldenRecordRuleList = new ArrayList<>();
    for (Vertex ruleNode : searchResults) {
      Map<String, Object> goldenRecordRuleMap = UtilClass
          .getMapFromVertex(Arrays.asList(CommonConstants.CODE_PROPERTY,
              IGoldenRecordRule.LABEL, IGoldenRecordRule.CODE), ruleNode);
      goldenRecordRuleList.add(goldenRecordRuleMap);
    }
    return goldenRecordRuleList;
  }
}
