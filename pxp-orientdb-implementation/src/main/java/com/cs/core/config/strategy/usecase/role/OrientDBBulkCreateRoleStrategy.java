package com.cs.core.config.strategy.usecase.role;

import com.cs.core.config.interactor.model.role.IBulkCreateRoleModel;
import com.cs.core.config.interactor.model.summary.IPluginSummaryModel;
import com.cs.core.config.interactor.model.summary.PluginSummaryModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("bulkCreateRoleStrategy")
public class OrientDBBulkCreateRoleStrategy extends OrientDBBaseStrategy
    implements IBulkCreateRoleStrategy {
  
  public static final String useCase = "BulkCreateRoles";
  
  @Override
  public IPluginSummaryModel execute(IBulkCreateRoleModel dataModel) throws Exception
  {
    Map<String, Object> map = new HashMap<>();
    map.put("roles", dataModel.getRoles());
    map.put("roleUsers", dataModel.getRoleUsers());
    return execute(useCase, map, PluginSummaryModel.class);
  }
}
