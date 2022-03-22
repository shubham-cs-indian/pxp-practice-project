package com.cs.core.runtime.instancetree;

import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IOrganizeTreeDataRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetOrganizeTreeDataResponseModel;

public interface IGetOrganizeTreeDataService extends
    IRuntimeService<IOrganizeTreeDataRequestModel, IGetOrganizeTreeDataResponseModel> {
  
}
