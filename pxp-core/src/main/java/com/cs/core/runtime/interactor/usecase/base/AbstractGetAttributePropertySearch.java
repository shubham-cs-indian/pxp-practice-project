package com.cs.core.runtime.interactor.usecase.base;

import com.cs.core.config.interactor.model.attribute.IAttributeModel;
import com.cs.core.config.interactor.model.role.IGetRolesAndKlassesForUserModel;
import com.cs.core.config.interactor.model.taxonomy.IApplicableFilterModel;
import com.cs.core.config.strategy.usecase.attribute.IGetAttributeStrategy;
import com.cs.core.config.strategy.usecase.user.IGetRolesAndKlassesForUserStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetKlassInstancePropertySearchStrategyModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractGetAttributePropertySearch<P extends IGetKlassInstancePropertySearchStrategyModel, R extends IApplicableFilterModel>
    extends AbstractRuntimeInteractor<P, R> {
  
  @Autowired
  ISessionContext                              context;
  
  @Autowired
  protected IGetRolesAndKlassesForUserStrategy getRolesAndKlassesForUserStrategy;
  
  @Autowired
  protected IGetAttributeStrategy              getAttributeStrategy;
  
  protected abstract IApplicableFilterModel getApplicableFilters(
      IGetKlassInstancePropertySearchStrategyModel getModel) throws Exception;
  
  @SuppressWarnings("unchecked")
  @Override
  protected R executeInternal(P getKlassInstanceTreeStrategyModel) throws Exception
  {
    String userId = context.getUserId();
    getKlassInstanceTreeStrategyModel.setCurrentUserId(userId);
    setKlassIdsAndRolesWithReadPermission(getKlassInstanceTreeStrategyModel, userId);
    
    IIdParameterModel dataModel = new IdParameterModel();
    dataModel.setId(getKlassInstanceTreeStrategyModel.getAttributePropertySearch()
        .getAttributeId());
    
    IAttributeModel attribute = getAttributeStrategy.execute(dataModel);
    
    getKlassInstanceTreeStrategyModel.setAttribute(attribute);
    
    return (R) getApplicableFilters(getKlassInstanceTreeStrategyModel);
  }
  
  protected void setKlassIdsAndRolesWithReadPermission(P getKlassInstanceTreeStrategyModel,
      String loginUserId) throws Exception
  {
    IIdParameterModel idParameterModel = new IdParameterModel(loginUserId);
    IGetRolesAndKlassesForUserModel rolesAndKlassesForUser = getRolesAndKlassesForUserStrategy
        .execute(idParameterModel);
    getKlassInstanceTreeStrategyModel.setRoles(rolesAndKlassesForUser.getRoles());
  }
}
