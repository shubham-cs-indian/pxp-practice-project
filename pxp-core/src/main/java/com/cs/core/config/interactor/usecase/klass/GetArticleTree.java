package com.cs.core.config.interactor.usecase.klass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.cs.core.config.klass.IGetArticleTreeService;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetArticleTree
    extends AbstractGetConfigInteractor<IIdParameterModel, IConfigEntityTreeInformationModel>
    implements IGetArticleTree {
  
  @Autowired
  IGetArticleTreeService getArticleTreeService;
  
  @Override
  public IConfigEntityTreeInformationModel executeInternal(IIdParameterModel klassModel) throws Exception
  {
    return getArticleTreeService.execute(klassModel);
  }
}
