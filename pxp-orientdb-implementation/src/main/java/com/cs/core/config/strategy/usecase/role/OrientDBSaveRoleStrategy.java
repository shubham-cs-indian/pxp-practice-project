package com.cs.core.config.strategy.usecase.role;

import com.cs.core.config.interactor.model.role.GetRoleStrategyModel;
import com.cs.core.config.interactor.model.role.IGetRoleStrategyModel;
import com.cs.core.config.interactor.model.role.IRoleSaveModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component("saveRoleStrategy")
public class OrientDBSaveRoleStrategy extends OrientDBBaseStrategy implements ISaveRoleStrategy {
  
  @Override
  public IGetRoleStrategyModel execute(IRoleSaveModel model) throws Exception
  {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("role", model);
    return execute(SAVE_ROLE, requestMap, GetRoleStrategyModel.class);
  }
}
