package com.cs.config.strategy.plugin.usecase.user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.strategy.plugin.usecase.user.util.UserUtils;
import com.cs.config.strategy.plugin.usecase.util.AuditLogUtils;
import com.cs.config.strategy.plugin.usecase.util.UtilClass;
import com.cs.config.strategy.plugin.usecase.util.ValidationUtils;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Elements;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.Entities;
import com.cs.core.runtime.interactor.constants.application.VertexLabelConstants;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.strategy.plugin.base.AbstractOrientPlugin;
import com.orientechnologies.orient.server.config.OServerCommandConfiguration;
import com.tinkerpop.blueprints.Vertex;

public class DeleteUsers extends AbstractOrientPlugin {
  
  public DeleteUsers(final OServerCommandConfiguration iConfiguration)
  {
    super(iConfiguration);
  }
  
  @SuppressWarnings("unchecked")
  @Override
  public Object execute(Map<String, Object> requestMap) throws Exception
  {
    List<String> userIds = new ArrayList<String>();
    userIds = (List<String>) requestMap.get("ids");
    List<Map<String, Object>> auditInfoList = new ArrayList<>();
    
    List<String> deletedIds = new ArrayList<>();
    for (String id : userIds) {
      Vertex userNode = UtilClass.getVertexByIndexedId(id, VertexLabelConstants.ENTITY_TYPE_USER);
      if(ValidationUtils.vaildateIfStandardEntity(userNode))
        continue;
      if (userNode != null) {
        UserUtils.deleteUserNode(userNode);
        AuditLogUtils.fillAuditLoginfo(auditInfoList, userNode, Entities.USERS, Elements.UNDEFINED);
        userNode.remove();
      }
      deletedIds.add(id);
    }
    Map<String, Object> responseMap = new HashMap<>();
    responseMap.put(IBulkResponseModel.SUCCESS, deletedIds);
    responseMap.put(IBulkDeleteReturnModel.AUDIT_LOG_INFO, auditInfoList);
    
    UtilClass.getGraph()
        .commit();
    return responseMap;
  }
  
  @Override
  public String[] getNames()
  {
    return new String[] { "POST|DeleteUsers/*" };
  }
}
