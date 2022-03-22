package com.cs.core.runtime.interactor.usecase.elastic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.elastic.ICreateCalculatedPropertyRecordsService;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.elastic.IDocumentRegenerationModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;

@Service
public class CreateCalculatedPropertyRecord extends AbstractRuntimeInteractor<IDocumentRegenerationModel, IModel>
    implements ICreateCalculatedPropertyRecord {
  
  @Autowired
  ICreateCalculatedPropertyRecordsService createCalculatedPropertyRecordsService;
  
  @Override
  protected IModel executeInternal(IDocumentRegenerationModel model) throws Exception
  {
    return createCalculatedPropertyRecordsService.execute(model);
  }
  
}
