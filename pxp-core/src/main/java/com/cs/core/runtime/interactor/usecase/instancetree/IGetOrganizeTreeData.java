package com.cs.core.runtime.interactor.usecase.instancetree;

import com.cs.core.runtime.interactor.model.configdetails.instancetree.IOrganizeTreeDataRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetOrganizeTreeDataResponseModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface IGetOrganizeTreeData extends
    IRuntimeInteractor<IOrganizeTreeDataRequestModel, IGetOrganizeTreeDataResponseModel> {
  
}
