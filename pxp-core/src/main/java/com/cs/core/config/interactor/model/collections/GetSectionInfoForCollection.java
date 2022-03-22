package com.cs.core.config.interactor.model.collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.klass.IGetSectionInfoForTypeRequestModel;
import com.cs.core.config.interactor.model.klass.IGetSectionInfoModel;
import com.cs.core.config.interactor.usecase.collection.IGetSectionInfoForCollection;
import com.cs.core.config.interactor.usecase.klass.AbstractGetSectionInfo;
import com.cs.core.config.strategy.usecase.collection.IGetSectionInfoForCollectionStrategy;

@Service
public class GetSectionInfoForCollection
    extends AbstractGetSectionInfo<IGetSectionInfoForTypeRequestModel, IGetSectionInfoModel>
    implements IGetSectionInfoForCollection {
  
  @Autowired
  protected IGetSectionInfoForCollectionStrategy getSectionInfoForCollectionStrategy;
  
  @Override
  public IGetSectionInfoModel execute(IGetSectionInfoForTypeRequestModel dataModel) throws Exception
  {
    return super.executeInternal(dataModel);
  }
  
  @Override
  protected IGetSectionInfoModel executeGetSectionInfo(IGetSectionInfoForTypeRequestModel model)
      throws Exception
  {
    return getSectionInfoForCollectionStrategy.execute(model);
  }
}
