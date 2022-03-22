package com.cs.core.config.interactor.usecase.iconlibrary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.iconlibrary.ISaveOrReplaceIconService;
import com.cs.core.config.interactor.model.asset.IIconResponseModel;
import com.cs.core.config.interactor.model.iconlibrary.ISaveOrReplaceIconRequestModel;

/**
 * This is service class for updating icon information.
 * 
 * @author pranav.huchche
 */

@Service
public class SaveOrReplaceIcon extends AbstractSaveConfigInteractor<ISaveOrReplaceIconRequestModel, IIconResponseModel>
    implements ISaveOrReplaceIcon {
  
  @Autowired
  protected ISaveOrReplaceIconService saveOrReplaceIconService;
  
  @Override
  protected IIconResponseModel executeInternal(ISaveOrReplaceIconRequestModel model) throws Exception
  {
    return saveOrReplaceIconService.execute(model);
  }
}
