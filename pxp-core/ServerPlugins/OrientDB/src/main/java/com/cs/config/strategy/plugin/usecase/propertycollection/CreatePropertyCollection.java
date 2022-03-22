package com.cs.config.strategy.plugin.usecase.propertycollection;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.propertycollection.util.CreatePropertyCollectionUtil;
import com.cs.config.strategy.plugin.usecase.propertycollection.util.PropertyCollectionUtils;
import com.cs.config.strategy.plugin.usecase.tabs.utils.TabUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.propertycollection.IGetPropertyCollectionModel;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdLabelModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class CreatePropertyCollection extends AbstractOrientPlugin {
  
  public CreatePropertyCollection(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|CreatePropertyCollection/*" };
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected Map<String, Object> execute(Map<String, Object> requestMap) throws Exception
  {
    Map<String, Object> propertyCollectionMap = (HashMap<String, Object>) requestMap
        .get(CommonConstants.PROPERTY_COLLECTION);
    Vertex propertyCollectionNode = CreatePropertyCollectionUtil
        .createPropertyCollection(propertyCollectionMap);
    
    Map<String, Object> returnMap = PropertyCollectionUtils
        .getPropertyCollection(propertyCollectionNode);
    AuditLogUtils.fillAuditLoginfo(returnMap, propertyCollectionNode,
        Entities.PROPERTY_GROUPS_MENU_ITEM_TITLE, Elements.PROPERTY_COLLECTION);
    Map<String, Object> responseTabMap = TabUtils.getMapFromConnectedTabNode(propertyCollectionNode,
        Arrays.asList(CommonConstants.CODE_PROPERTY, IIdLabelModel.LABEL));
    returnMap.put(IGetPropertyCollectionModel.TAB, responseTabMap);
    return returnMap;
  }
}
