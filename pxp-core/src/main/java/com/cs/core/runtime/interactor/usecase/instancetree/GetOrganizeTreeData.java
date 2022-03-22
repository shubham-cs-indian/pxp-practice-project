package com.cs.core.runtime.interactor.usecase.instancetree;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.instancetree.IGetOrganizeTreeDataService;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IOrganizeTreeDataRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetOrganizeTreeDataResponseModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;

@Service
public class GetOrganizeTreeData extends
    AbstractRuntimeInteractor<IOrganizeTreeDataRequestModel, IGetOrganizeTreeDataResponseModel>
    implements IGetOrganizeTreeData {
  
  @Autowired
  protected IGetOrganizeTreeDataService getOrganizeScreenTreeDataService;
  
  @Override
  protected IGetOrganizeTreeDataResponseModel executeInternal(
      IOrganizeTreeDataRequestModel model) throws Exception
  {
    return getOrganizeScreenTreeDataService.execute(model);
  }
  
}