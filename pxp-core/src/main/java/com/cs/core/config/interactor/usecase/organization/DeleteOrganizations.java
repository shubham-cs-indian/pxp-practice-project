package com.cs.core.config.interactor.usecase.organization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractDeleteConfigInteractor;
import com.cs.core.config.organization.IDeleteOrganizationService;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Service
public class DeleteOrganizations
    extends AbstractDeleteConfigInteractor<IIdsListParameterModel, IBulkDeleteReturnModel>
    implements IDeleteOrganizations {

  @Autowired
  protected IDeleteOrganizationService deleteOrganizationService;
  

  @Override
  public IBulkDeleteReturnModel executeInternal(IIdsListParameterModel templateModel) throws Exception
  {
    return deleteOrganizationService.execute(templateModel);
  }

}
