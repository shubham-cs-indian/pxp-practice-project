package com.cs.core.config.user;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.entity.module.IScreenModuleMapping;
import com.cs.core.config.interactor.model.user.CurrentUserModel;
import com.cs.core.config.interactor.model.user.IGetCurrentUserModel;
import com.cs.core.config.interactor.model.user.IUserInformationModel;
import com.cs.core.config.strategy.usecase.user.IGetCurrentUserStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.strategy.utils.ModuleMappingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetCurrentUserService
    extends AbstractGetConfigService<IIdParameterModel, IUserInformationModel>
    implements IGetCurrentUserService {
  
  @Autowired
  IGetCurrentUserStrategy getCurrentUserStrategy;
  
  @Autowired
  ModuleMappingUtil       moduleMappingUtil;
  
  @Override
  public IUserInformationModel executeInternal(IIdParameterModel userIdModel) throws Exception
  {
    IGetCurrentUserModel currentUserModel = getCurrentUserStrategy.execute(userIdModel);
    List<String> allowedEntities = currentUserModel.getEntities();
    List<String> allowedPhysicalCatalogIds = currentUserModel.getAllowedPhysicalCatalogIds();
    List<String> allowedPortalIds = currentUserModel.getAllowedPortalIds();
    Boolean isDashboardEnable = currentUserModel.getIsDashboardEnable();
    String landingScreen = currentUserModel.getLandingScreen();
    IScreenModuleMapping screenModuleMapping = moduleMappingUtil
        .getScreenModuleMappingToReturnForCurrentUser(allowedEntities, allowedPhysicalCatalogIds,
            allowedPortalIds, isDashboardEnable, landingScreen);
    
    IUserInformationModel modelToReturn = new CurrentUserModel(currentUserModel.getUser());
    modelToReturn.setScreenModuleMapping(screenModuleMapping);
    modelToReturn.setIsSettingAllowed(currentUserModel.getIsSettingAllowed());
    modelToReturn.setAllowedPhysicalCatalogIds(allowedPhysicalCatalogIds);
    modelToReturn.setAllowedPortalIds(allowedPortalIds);
    modelToReturn.setOrganizationId(currentUserModel.getOrganizationId());
    modelToReturn.setRoleType(currentUserModel.getRoleType());
    modelToReturn.setIsReadOnly(currentUserModel.getIsReadOnly());
    modelToReturn.setPreferredDataLanguage(currentUserModel.getPreferredDataLanguage());
    modelToReturn.setPreferredUILanguage(currentUserModel.getPreferredUILanguage());
    return modelToReturn;
  }
}
