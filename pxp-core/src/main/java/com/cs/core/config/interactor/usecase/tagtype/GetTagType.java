package com.cs.core.config.interactor.usecase.tagtype;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractGetConfigInteractor;
import com.cs.core.config.business.tagtype.IGetTagTypeService;
import com.cs.core.config.interactor.model.tag.ITagTypeModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;

@Service
public class GetTagType extends AbstractGetConfigInteractor<IIdParameterModel, ITagTypeModel>
    implements IGetTagType {
  
  @Autowired
  IGetTagTypeService getTagTypeService;
  
  @Override
  public ITagTypeModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    return getTagTypeService.execute(dataModel);
  }
}
