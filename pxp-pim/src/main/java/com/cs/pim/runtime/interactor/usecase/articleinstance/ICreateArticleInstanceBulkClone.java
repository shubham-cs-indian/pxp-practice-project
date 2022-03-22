package com.cs.pim.runtime.interactor.usecase.articleinstance;


import com.cs.core.runtime.interactor.model.clone.ICreateKlassInstanceBulkCloneModel;
import com.cs.core.runtime.interactor.model.klassinstance.IBulkCreateKlassInstanceCloneResponseModel;
import com.cs.core.runtime.interactor.usecase.base.IRuntimeInteractor;

public interface ICreateArticleInstanceBulkClone extends
    IRuntimeInteractor<ICreateKlassInstanceBulkCloneModel, IBulkCreateKlassInstanceCloneResponseModel> {
  
}