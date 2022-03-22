package com.cs.config.strategy.plugin.usecase.export;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.entity.tabs.ITab;
import com.cs.core.config.interactor.model.tabs.IGetTabEntityModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;

public class ExportTabList extends AbstractOrientPlugin {
  
  public ExportTabList(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    OrientGraph graph = UtilClass.getGraph();
    HashMap<String, Object> responseMap = new HashMap<>();
    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    
    List<String> tabCodes = (List<String>) requestMap.get("itemCodes");
    StringBuilder codeQuery = UtilClass.getTypeQueryWithoutANDOperator(tabCodes, ITab.CODE);
    StringBuilder condition = EntityUtil.getConditionQuery(codeQuery);
    
    String query = "select from " + VertexLabelConstants.TAB + condition + " order by "
        + EntityUtil.getLanguageConvertedField(CommonConstants.LABEL_PROPERTY) + " asc";
    Iterable<Vertex> resultIterable = graph.command(new OCommandSQL(query)).execute();
    
    for (Vertex tabNode : resultIterable) {
      list.add(prepareTabResponseMap(tabNode));
    }
    
    responseMap.put("list", list);
    
    return responseMap;
  }
  
  private static final Map<String, Object> prepareTabResponseMap(Vertex tabNode) throws Exception
  {
    List<String> fieldToFetch = Arrays.asList(IGetTabEntityModel.LABEL, IGetTabEntityModel.ICON,
        IGetTabEntityModel.CODE, IGetTabEntityModel.PROPERTY_SEQUENCE_LIST);
    Map<String, Object> responseMap = UtilClass.getMapFromVertex(fieldToFetch, tabNode);
    
    String tabId = (String) responseMap.get(IGetTabEntityModel.ID);
    
    Integer sequenceNumber = TabUtils.getTabSequence(tabId);
    responseMap.put(IGetTabEntityModel.SEQUENCE, sequenceNumber);
    
    return responseMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|ExportTabList/*" };
  }
}
