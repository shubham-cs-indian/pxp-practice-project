package com.cs.pim.runtime.interactor.usecase.textassetinstance;


import com.cs.core.runtime.interactor.model.clone.ICreateKlassInstanceBulkCloneModel;
import com.cs.core.runtime.interactor.model.klassinstance.IBulkCreateKlassInstanceCloneResponseModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface ICreateTextAssetInstanceBulkClone extends
    IRuntimeInteractor<ICreateKlassInstanceBulkCloneModel, IBulkCreateKlassInstanceCloneResponseModel> {
  
}