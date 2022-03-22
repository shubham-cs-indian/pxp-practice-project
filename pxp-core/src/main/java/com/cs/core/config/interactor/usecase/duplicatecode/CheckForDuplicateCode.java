package com.cs.core.config.interactor.usecase.duplicatecode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.entity.datarule.ICheckForDuplicateCodeModel;
import com.cs.core.config.interactor.entity.datarule.ICheckForDuplicateCodeReturnModel;
import com.cs.core.config.strategy.usecase.duplicatecode.ICheckForDuplicateCodeStrategy;

@Service
public class CheckForDuplicateCode extends
    AbstractGetConfigInteractor<ICheckForDuplicateCodeModel, ICheckForDuplicateCodeReturnModel>
    implements ICheckForDuplicateCode {
  
  @Autowired
  ICheckForDuplicateCodeStrategy checkForDuplicateCodeStrategy;
  
  @Override
  public ICheckForDuplicateCodeReturnModel executeInternal(ICheckForDuplicateCodeModel dataModel)
      throws Exception
  {
    return checkForDuplicateCodeStrategy.execute(dataModel);
  }
}
