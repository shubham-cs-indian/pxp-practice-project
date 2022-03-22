package com.cs.core.config.role;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.config.standard.IStandardConfig;
import com.cs.constants.SystemLevelIds;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.role.IRoleModel;
import com.cs.core.config.strategy.usecase.role.IGetAllRolesStrategy;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Service
public class GetAllRolesService extends AbstractGetConfigService<IRoleModel, IListModel<IRoleModel>>
    implements IGetAllRolesService {
  
  private static final List<String> rolesToExclude = Arrays.asList(SystemLevelIds.RESPONSIBLE,
      SystemLevelIds.ACCOUNTABLE, SystemLevelIds.CONSULTED, SystemLevelIds.INFORMED,
      SystemLevelIds.VERIFY, SystemLevelIds.SIGN_OFF);
  @Autowired
  IGetAllRolesStrategy              neo4jGetRolesStrategy;
  @Autowired
  ISessionContext                   context;
  
  @Override
  public IListModel<IRoleModel> executeInternal(IRoleModel dataModel) throws Exception
  {
    IListModel<IRoleModel> returnModel = neo4jGetRolesStrategy.execute(dataModel);
    String currentUserId = context.getUserId();
    
    if (!currentUserId.equals(IStandardConfig.StandardUser.admin.toString())) {
      removeRACIVSRoles(returnModel);
    }
    
    return returnModel;
  }
  
  /**
   * This method filters out RACIVS roles
   */
  @SuppressWarnings("unchecked")
  private void removeRACIVSRoles(IListModel<IRoleModel> dataModel)
  {
    List<IRoleModel> dataModelList = (List<IRoleModel>) dataModel.getList();
    Iterator<IRoleModel> iterator = dataModelList.iterator();
    
    while (iterator.hasNext()) {
      IRoleModel roleModel = iterator.next();
      if (rolesToExclude.contains(roleModel.getId())) {
        iterator.remove();
      }
    }
  }
}
