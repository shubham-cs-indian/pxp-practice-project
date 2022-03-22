package com.cs.config.strategy.plugin.usecase.tabs;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.core.config.interactor.entity.tabs.ITab;
import com.cs.core.config.interactor.exception.tab.PropertyNotFoundInTabException;
import com.cs.core.config.interactor.model.tabs.IModifiedTabPropertySequenceModel;
import com.cs.core.config.interactor.model.tabs.ISaveTabModel;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

@SuppressWarnings("unchecked")
public class SaveTab extends AbstractOrientPlugin {
  
  public static final List<String> fieldToExclude = Arrays.asList(
      ISaveTabModel.MODIFIED_PROPERTY_SEQUENCE, ISaveTabModel.MODIFIED_PROPERTY_SEQUENCE,
      ISaveTabModel.MODIFIED_TAB_SEQUENCE);
  
  public SaveTab(OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|SaveTab/*" };
  }
  
  @Override
  protected Object execute(Map<String, Object> requestMap) throws Exception
  {
    String tabId = (String) requestMap.get(IIdParameterModel.ID);
    Vertex tabNode = UtilClass.getVertexById(tabId, VertexLabelConstants.TAB);
    
    // update property sequence in tab
    List<String> propertySequenceList = tabNode.getProperty(ITab.PROPERTY_SEQUENCE_LIST);
    List<Map<String, Object>> modifiedPropertySequence = (List<Map<String, Object>>) requestMap
        .get(ISaveTabModel.MODIFIED_PROPERTY_SEQUENCE);
    
    for (Map<String, Object> sequenceMap : modifiedPropertySequence) {
      String propertyId = (String) sequenceMap.get(IModifiedTabPropertySequenceModel.ID);
      Integer newSequence = (Integer) sequenceMap
          .get(IModifiedTabPropertySequenceModel.NEW_SEQUENCE);
      
      Boolean isRemoved = propertySequenceList.remove(propertyId);
      if (!isRemoved) {
        throw new PropertyNotFoundInTabException();
      }
      propertySequenceList.add(newSequence, propertyId);
    }
    
    UtilClass.saveNode(requestMap, tabNode, fieldToExclude);
    tabNode.setProperty(ITab.PROPERTY_SEQUENCE_LIST, propertySequenceList);
    
    UtilClass.getGraph()
        .commit();
    
    Map<String, Object> responseMap = TabUtils.prepareTabResponseMap(tabNode);
    AuditLogUtils.fillAuditLoginfo(responseMap, tabNode, Entities.PROPERTY_GROUPS_MENU_ITEM_TITLE, Elements.TABS_MENU_ITEM_TILE);
    return responseMap;
  }
}
