package com.cs.pim.runtime.interactor.usecase.targetinstance.market;


import com.cs.core.runtime.interactor.model.clone.ICreateKlassInstanceBulkCloneModel;
import com.cs.core.runtime.interactor.model.klassinstance.IBulkCreateKlassInstanceCloneResponseModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface ICreateMarketInstanceBulkClone extends
    IRuntimeInteractor<ICreateKlassInstanceBulkCloneModel, IBulkCreateKlassInstanceCloneResponseModel> {
  
}