package com.cs.config.strategy.plugin.usecase.governancerule;

import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetKeyPerformanceIndexForMigration extends AbstractOrientPlugin {


  private static final String CODE        = "code";
  private static final String TYPE        = "type";
  private static final String LIST        = "list";
  private static final String COUNT       = "count";
  private static final String VERTEX_TYPE = "vertexType";
  private static final String FROM        = "from";
  private static final String SIZE        = "size";

  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetKeyPerformanceIndexForMigration/*" };
  }

  public GetKeyPerformanceIndexForMigration(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public Object execute(Map<String, Object> request) throws Exception
  {
    int from = (int) request.get("from");
    int size = (int) request.get("size");
    Map<String, Object> returnMap = new HashMap<>();

    fillInfo(from, size,returnMap);
    return returnMap;
  }

  private void fillInfo(int from, int size, Map<String, Object> returnMap) throws Exception
  {
    long count;
    List<Object> list = new ArrayList<>();
    String countQuery = "select count(*) from " + VertexLabelConstants.GOVERNANCE_RULE_KPI;
    count = EntityUtil.executeCountQueryToGetTotalCount(countQuery);
    returnMap.put(COUNT, count);

    Iterable<Vertex> searchResults = UtilClass.getGraph()
        .command(new OCommandSQL("select from " + VertexLabelConstants.GOVERNANCE_RULE_KPI + " skip  " + from + " limit " + size)).execute();

    for (Vertex kpiVertex : searchResults) {
      list.add(GovernanceRuleUtil.getKPIFromNode(kpiVertex));
    }
    returnMap.put(LIST, list);
  }
}
