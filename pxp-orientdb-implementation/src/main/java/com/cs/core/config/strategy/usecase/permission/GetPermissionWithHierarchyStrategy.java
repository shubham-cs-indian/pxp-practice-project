package com.cs.core.config.strategy.usecase.permission;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.permission.GetPermissionWithHierarchyModel;
import com.cs.core.config.interactor.model.permission.IGetPermissionWithHierarchyModel;
import com.cs.core.config.interactor.model.permission.IPermissionWithHierarchyRequestModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

@Component
public class GetPermissionWithHierarchyStrategy extends OrientDBBaseStrategy
    implements IGetPermissionWithHierarchyStrategy {
  
  @Override
  public IListModel<IGetPermissionWithHierarchyModel> execute(
      IPermissionWithHierarchyRequestModel model) throws Exception
  {
    return execute(GET_PERMISSION_WITH_HIERARCHY, model,
        new TypeReference<ListModel<GetPermissionWithHierarchyModel>>()
        {
          
        });
  }
}
