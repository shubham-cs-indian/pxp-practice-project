package com.cs.core.runtime.interactor.usecase.instance;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.role.IGetRolesAndKlassesForUserModel;
import com.cs.core.config.strategy.usecase.klass.IGetAllowedTypesStrategy;
import com.cs.core.config.strategy.usecase.tag.IGetDisplayAndRelevanceTagIdsStrategy;
import com.cs.core.config.strategy.usecase.user.IGetRolesAndKlassesForUserStrategy;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ISessionContext;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.datarule.IGetAllDisplayAndRelevanceTagIdsModel;
import com.cs.core.runtime.interactor.model.filter.IKlassInstanceQuickListModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;
import com.cs.core.runtime.interactor.model.typeswitch.GetAllowedTypesModel;
import com.cs.core.runtime.interactor.model.typeswitch.IGetAllowedTypesModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractInstanceQuickList<P extends IKlassInstanceQuickListModel, R extends IListModel<? extends IKlassInstanceInformationModel>>
    extends AbstractRuntimeInteractor<P, R> {
  
  @Autowired
  ISessionContext                       context;
  
  @Autowired
  IGetDisplayAndRelevanceTagIdsStrategy getDisplayAndRelevanceTagIdsStrategy;
  
  @Autowired
  IGetRolesAndKlassesForUserStrategy    getRolesAndKlassesForUserStrategy;
  
  @Autowired
  IGetAllowedTypesStrategy              getAllowedTypesStrategy;
  
  // TODO - Cosmetic : Change the method name to getModule()
  /**
   * This method will returns the module for specific entity (eg: PIM / MAM /
   * Target and so on)
   *
   * @return module
   */
  protected abstract String getMode();
  
  protected abstract IListModel<IKlassInstanceInformationModel> executeGetQuickListElements(
      IKlassInstanceQuickListModel klassInstanceQuickListModel) throws Exception;
  
  @SuppressWarnings("unchecked")
  protected R executeInternal(P dataModel) throws Exception
  {
    String loginUserId = context.getUserId();
    dataModel.setCurrentUserId(loginUserId);
    
    IGetAllDisplayAndRelevanceTagIdsModel displayAndRelevanceModel = getDisplayAndRelevanceTagIdsStrategy
        .execute(null);
    dataModel.setDimensionalTagIds(displayAndRelevanceModel.getDisplayTagIds());
    dataModel.setRelevanceTagIds(displayAndRelevanceModel.getRelevanceTagIds());
    
    IIdParameterModel idParameterModel = new IdParameterModel(loginUserId);
    IGetRolesAndKlassesForUserModel rolesAndKlassesForUser = getRolesAndKlassesForUserStrategy
        .execute(idParameterModel);
    dataModel.setRoles(rolesAndKlassesForUser.getRoles());
    
    IGetAllowedTypesModel allowedTypesModel = new GetAllowedTypesModel();
    allowedTypesModel.setId(dataModel.getTypeKlassId());
    allowedTypesModel.setMode(getMode());
    IListModel<String> allowedTypes = getAllowedTypesStrategy.execute(allowedTypesModel);
    dataModel.setAllowedTypes((List<String>) allowedTypes.getList());
    
    IListModel<IKlassInstanceInformationModel> klassInstances = executeGetQuickListElements(
        dataModel);
    
    return (R) klassInstances;
  }
}
