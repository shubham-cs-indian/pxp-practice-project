package com.cs.core.runtime.dashboard;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.entity.module.IModule;
import com.cs.core.runtime.interactor.model.configdetails.IConfigDetailsForInstanceTreeGetModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.dashboard.IDashboardInformationRequestModel;
import com.cs.core.runtime.interactor.model.dashboard.IDashboardInformationResponseModel;
import com.cs.core.runtime.strategy.utils.ModuleMappingUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

public abstract class AbstractGetDashboardTileRuleViolationInfoService<P extends IDashboardInformationRequestModel, R extends IDashboardInformationResponseModel>
    extends AbstractRuntimeService<P, R> {
  
  @Autowired
  protected ISessionContext   context;
  
  @Autowired
  protected ModuleMappingUtil moduleMappingUtil;
  
  protected abstract IDashboardInformationResponseModel getDashboardTileRuleViolationInfo(
      P dashboardInformationRequestModel) throws Exception;
  
  protected abstract IConfigDetailsForInstanceTreeGetModel getConfigDetails(IIdParameterModel model)
      throws Exception;
  
  @SuppressWarnings({ "unchecked" })
  @Override
  protected R executeInternal(P dashboardInformationRequestModel) throws Exception
  {
    String loginUserId = context.getUserId();
    String moduleId = dashboardInformationRequestModel.getModuleId();
    IIdParameterModel model = new IdParameterModel();
    model.setCurrentUserId(loginUserId);
    
    IConfigDetailsForInstanceTreeGetModel configDetails = getConfigDetails(model);
    IModule module = getModule(configDetails.getAllowedEntities(), moduleId);
    
    dashboardInformationRequestModel.setCurrentUserId(loginUserId);
    
    dashboardInformationRequestModel.setKlassIdsHavingRP(configDetails.getKlassIdsHavingRP());
    dashboardInformationRequestModel.setTaxonomyIdsHavingRP(configDetails.getTaxonomyIdsHavingRP());
    dashboardInformationRequestModel.setMajorTaxonomyIds(configDetails.getMajorTaxonomyIds());
    dashboardInformationRequestModel.setModuleEntities(module.getEntities());
    
    IDashboardInformationResponseModel returnModel = getDashboardTileRuleViolationInfo(
        dashboardInformationRequestModel);
    
    return (R) returnModel;
  }
  
  protected IModule getModule(Set<String> allowedEntities, String moduleId) throws Exception
  {
    
    List<IModule> modules = moduleMappingUtil.getRuntimeModule();
    
    for (IModule iModule : modules) {
      if (iModule.getId()
          .equals(moduleId)) {
        iModule.getEntities()
            .retainAll(allowedEntities);
        return iModule;
      }
    }
    
    return null;
  }
}
