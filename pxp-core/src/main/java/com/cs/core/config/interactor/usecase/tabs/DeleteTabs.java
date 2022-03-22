package com.cs.core.config.interactor.usecase.tabs;

import java.util.Map;

import com.cs.core.config.businessapi.tabs.IDeleteTabsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractDeleteConfigInteractor;
import com.cs.core.config.interactor.exception.entityconfiguration.EntityConfigurationDependencyException;
import com.cs.core.config.interactor.model.configdetails.GetEntityConfigurationRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationResponseModel;
import com.cs.core.config.interactor.usecase.tab.IDeleteTabs;
import com.cs.core.config.strategy.usecase.entityconfiguration.IGetEntityConfigurationStrategy;
import com.cs.core.config.strategy.usecase.tabs.IDeleteTabsStrategy;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

import javax.naming.CannotProceedException;

@Service
public class DeleteTabs
    extends AbstractDeleteConfigInteractor<IIdsListParameterModel, IBulkDeleteReturnModel>
    implements IDeleteTabs {

  @Autowired
  protected IDeleteTabsService deleteTabsAPI;

  @Override
  public IBulkDeleteReturnModel executeInternal(IIdsListParameterModel model) throws Exception
  {
      return deleteTabsAPI.execute(model);
  }
}
