package com.cs.config.strategy.plugin.usecase.grideditablepropertylist;

import com.cs.config.standard.IStandardConfig;
import com.cs.config.strategy.plugin.usecase.grideditablepropertylist.util.GridEditUtil;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.grideditpropertylist.ISaveGridEditablePropertyListModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;
import java.util.List;
import java.util.Map;

public class SaveGridEditablePropertyList extends AbstractOrientPlugin {
  
  public SaveGridEditablePropertyList(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveGridEditablePropertyList/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    
    /*  Long from = Long.valueOf(requestMap.get(IConfigGetAllRequestModel.FROM).toString());
    Long size = Long.valueOf(requestMap.get(IConfigGetAllRequestModel.SIZE).toString());
    String searchText = requestMap.get(IConfigGetAllRequestModel.SEARCH_TEXT).toString();
    String searchColumn = requestMap.get(IConfigGetAllRequestModel.SEARCH_COLUMN).toString();*/
    
    List<String> newGridEditSequenceList = (List<String>) requestMap
        .get(ISaveGridEditablePropertyListModel.SEQUENCE_LIST);
    return GridEditUtil.fetchGridEditSequenceData(newGridEditSequenceList);
  }
}
