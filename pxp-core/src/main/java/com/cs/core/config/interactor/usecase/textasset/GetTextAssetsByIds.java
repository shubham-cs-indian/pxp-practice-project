package com.cs.core.config.interactor.usecase.textasset;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.interactor.entity.klass.IKlass;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.textasset.IGetTextAssetsByIdsService;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;

@Component
public class GetTextAssetsByIds
    extends AbstractGetConfigInteractor<IIdsListParameterModel, IListModel<IKlass>>
    implements IGetTextAssetsByIds {
  
  @Autowired
  IGetTextAssetsByIdsService getTextAssetsByIdsService;
  
  @Override
  public IListModel<IKlass> executeInternal(IIdsListParameterModel dataModel) throws Exception
  {
    return getTextAssetsByIdsService.execute(dataModel);
  }
}
