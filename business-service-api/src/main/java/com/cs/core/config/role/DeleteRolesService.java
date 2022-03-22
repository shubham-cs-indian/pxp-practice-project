package com.cs.core.config.role;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractDeleteConfigService;
import com.cs.core.config.interactor.model.configdetails.IDeleteReturnModel;
import com.cs.core.config.strategy.usecase.role.IDeleteRoleStrategy;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Service
public class DeleteRolesService extends AbstractDeleteConfigService<IIdsListParameterModel, IBulkDeleteReturnModel>
    implements IDeleteRoleService {
  
  @Autowired
  IDeleteRoleStrategy neo4jDeleteRolesStrategy;
  
  @Autowired
  SessionRegistry     sessionRegistry;
  
  @Override
  public IDeleteReturnModel executeInternal(IIdsListParameterModel dataModel) throws Exception
  {
    IDeleteReturnModel deletedReturnModel = neo4jDeleteRolesStrategy.execute(dataModel);
    expireUserSessions(deletedReturnModel.getDeletedUserNames());
    return deletedReturnModel;
  }
  
  public void expireUserSessions(List<String> usernames) {
    for (String username : usernames) {
      List<Object> principals = sessionRegistry.getAllPrincipals();
      for (Object principal : principals) {
        if (principal.equals(username)) {
          for (SessionInformation information : sessionRegistry.getAllSessions(principal, true)) {
             information.expireNow();
          }
        }
      }
    }
  }

}
