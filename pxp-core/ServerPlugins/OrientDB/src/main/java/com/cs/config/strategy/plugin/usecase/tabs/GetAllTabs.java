package com.cs.config.strategy.plugin.usecase.tabs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.util.EntityUtil;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.model.grid.IConfigGetAllRequestModel;
import com.cs.core.config.interactor.model.tabs.IGetGridTabsModel;
import com.cs.core.config.interactor.model.tabs.IGetTabEntityModel;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class GetAllTabs extends AbstractOrientPlugin {
  
  protected List<String> fieldToFetch = Arrays.asList(CommonConstants.CODE_PROPERTY,
      IGetTabEntityModel.LABEL, IGetTabEntityModel.ICON, IGetTabEntityModel.CODE,
      IGetTabEntityModel.IS_STANDARD);
  
  public GetAllTabs(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|GetAllTabs/*" };
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
    
    String sortParameterSequence = "sequence";
    Vertex tabSequenceNode = TabUtils.getOrCreateTabSequenceNode();
    List<String> sequencedTabCidList = tabSequenceNode.getProperty(CommonConstants.SEQUENCE_LIST);
    sequencedTabCidList.remove(SystemLevelIds.OVERVIEW_TAB);
    
    StringBuilder searchQuery = EntityUtil.getSearchQuery(searchText, searchColumn);
    StringBuilder excludeTabQuery = getExcludeTabQuery();
    sortBy = EntityUtil.getLanguageConvertedField(sortBy);
    StringBuilder conditionQuery = EntityUtil.getConditionQuery(searchQuery, excludeTabQuery);
    
    String query = "select from " + VertexLabelConstants.TAB;
    if (!sortBy.equals(sortParameterSequence)) {
      query += conditionQuery + " order by " + sortBy + " " + sortOrder + " skip " + from
          + " limit " + size;
    }
    else {
      query = updateQueryToSortAccordingToSequence(from, size, sortOrder, sequencedTabCidList,
          query);
    }
    
    Iterable<Vertex> tabs = UtilClass.getGraph()
        .command(new OCommandSQL(query))
        .execute();
    
    List<Map<String, Object>> list = new ArrayList<>();
    for (Vertex tab : tabs) {
      Map<String, Object> tabMap = UtilClass.getMapFromVertex(fieldToFetch, tab);
      String tabId = (String) tabMap.get(IGetTabEntityModel.ID);
      tabMap.put(IGetTabEntityModel.SEQUENCE, (sequencedTabCidList.indexOf(tabId) + 1));
      list.add(tabMap);
    }
    
    if (sortBy.equals(sortParameterSequence)) {
      
      Collections.sort(list, (tab1, tab2) -> {
        Integer comparisonResult = 0;
        if (sortOrder.equals("asc")) {
          comparisonResult = (((Integer) tab1.get(IGetTabEntityModel.SEQUENCE))
              .compareTo((Integer) tab2.get(IGetTabEntityModel.SEQUENCE)));
        }
        else if (sortOrder.equals("desc")) {
          comparisonResult = (((Integer) tab2.get(IGetTabEntityModel.SEQUENCE))
              .compareTo((Integer) tab1.get(IGetTabEntityModel.SEQUENCE)));
        }
        return comparisonResult;
      });
    }
    
    String countQuery = "select count(*) from " + VertexLabelConstants.TAB + conditionQuery;
    
    Long count = (EntityUtil.executeCountQueryToGetTotalCount(countQuery));
    
    Map<String, Object> returnMap = new HashMap<String, Object>();
    returnMap.put(IGetGridTabsModel.TAB_LIST, list);
    returnMap.put(IGetGridTabsModel.COUNT, count);
    return returnMap;
  }
  
  private String updateQueryToSortAccordingToSequence(Long from, Long size, String sortOrder,
      List<String> sequencedTabCidList, String query)
  {
    int to = 0;
    List<String> subSequenceList = null;
    if (sortOrder.equals("asc")) {
      if ((from.intValue() + size.intValue()) > sequencedTabCidList.size()) {
        to = sequencedTabCidList.size();
      }
      else {
        to = (from.intValue() + size.intValue());
      }
      subSequenceList = sequencedTabCidList.subList(from.intValue(), to);
    }
    else if (sortOrder.equals("desc")) {
      from = sequencedTabCidList.size() - from;
      if ((from.intValue() - size.intValue()) < 0) {
        to = 0;
      }
      else {
        to = (from.intValue() - size.intValue());
      }
      subSequenceList = sequencedTabCidList.subList(to, from.intValue());
    }
    query += " where code in " + EntityUtil.quoteIt(subSequenceList);
    return query;
  }
  
  public static StringBuilder getExcludeTabQuery()
  {
    StringBuilder query = new StringBuilder();
    query.append(" code !='" + SystemLevelIds.OVERVIEW_TAB + "' ");
    return query;
  }
}
