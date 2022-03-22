package com.cs.config.strategy.plugin.usecase.tabs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tabs.IBulkSaveTabResponseModel;
import com.cs.core.config.interactor.model.tabs.IGetGridTabsModel;
import com.cs.core.config.interactor.model.tabs.IGetTabEntityModel;
import com.cs.core.config.interactor.model.tabs.ISaveTabModel;
import com.cs.core.exception.ExceptionUtil;
import com.cs.core.exception.InvalidTabSequenceException;
import com.cs.core.exception.PluginException;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

@SuppressWarnings("unchecked")
public class BulkSaveTab extends AbstractOrientPlugin {
  
  public static final List<String> fieldToExclude = Arrays.asList(
      ISaveTabModel.MODIFIED_PROPERTY_SEQUENCE, ISaveTabModel.MODIFIED_PROPERTY_SEQUENCE,
      ISaveTabModel.MODIFIED_TAB_SEQUENCE);
  
  protected List<String>           fieldToFetch   = Arrays.asList(CommonConstants.CODE_PROPERTY,
      IGetTabEntityModel.LABEL, IGetTabEntityModel.ICON, IGetTabEntityModel.CODE);
  
  public BulkSaveTab(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|BulkSaveTab/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<Map<String, Object>> requestList = (List<Map<String, Object>>) requestMap
        .get(IListModel.LIST);
    List<Map<String, Object>> auditInfoList = new ArrayList<>();
    
    List<Map<String, Object>> responseList = new ArrayList<>();
    IExceptionModel failure = new ExceptionModel();
    for (Map<String, Object> map : requestList) {
      try {
        String tabId = (String) map.get(ISaveTabModel.ID);
        Vertex tabNode = UtilClass.getVertexById(tabId, VertexLabelConstants.TAB);
        
        updateTabSequence(map);
        UtilClass.saveNode(map, tabNode, fieldToExclude);
        
        Map<String, Object> tabMap = UtilClass.getMapFromVertex(fieldToFetch, tabNode);
        Integer tabSequence = TabUtils.getTabSequence(tabId);
        tabMap.put(IGetTabEntityModel.SEQUENCE, tabSequence);
        responseList.add(tabMap);
        AuditLogUtils.fillAuditLoginfo(auditInfoList, tabNode, Entities.PROPERTY_GROUPS_MENU_ITEM_TITLE,
            Elements.TABS_MENU_ITEM_TILE);
      }
      catch (PluginException ex) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, ex, null, null);
      }
      catch (Exception ex) {
        ExceptionUtil.addFailureDetailsToFailureObject(failure, ex, null, null);
      }
    }
    
    UtilClass.getGraph()
        .commit();
    
    Map<String, Object> successMap = new HashMap<String, Object>();
    successMap.put(IGetGridTabsModel.TAB_LIST, responseList);
    
    Map<String, Object> responseMap = new HashMap<String, Object>();
    responseMap.put(IBulkSaveTabResponseModel.SUCCESS, successMap);
    responseMap.put(IBulkSaveTabResponseModel.FAILURE, failure);
    responseMap.put(IBulkSaveTabResponseModel.AUDIT_LOG_INFO, auditInfoList);
    return responseMap;
  }
  
  private void updateTabSequence(Map<String, Object> map) throws Exception
  {
    String tabId = (String) map.get(ISaveTabModel.ID);
    Integer newTabSequence = (Integer) map.get(ISaveTabModel.MODIFIED_TAB_SEQUENCE);
    if (newTabSequence == 0) {
      throw new InvalidTabSequenceException();
    }
    Integer tabSequence = TabUtils.getTabSequence(tabId);
    if (newTabSequence != null && !newTabSequence.equals(tabSequence)) {
      TabUtils.updateTabSequence(tabId, newTabSequence);
      tabSequence = newTabSequence;
    }
  }
}
