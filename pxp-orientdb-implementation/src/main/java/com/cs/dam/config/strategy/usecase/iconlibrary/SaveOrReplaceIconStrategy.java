package com.cs.dam.config.strategy.usecase.iconlibrary;

import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.asset.IIconModel;
import com.cs.core.config.interactor.model.asset.IIconResponseModel;
import com.cs.core.config.interactor.model.asset.IconResponseModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;

@Component("saveOrReplaceIconStrategy")
public class SaveOrReplaceIconStrategy extends OrientDBBaseStrategy implements ISaveOrReplaceIconStrategy {
  
  @Override
  public IIconResponseModel execute(IIconModel model) throws Exception
  {
    return execute(SAVE_OR_REPLACE_ICON, model, IconResponseModel.class);
  }
  
}
