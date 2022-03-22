package com.cs.core.config.strategy.usecase.goldenrecord;

import com.cs.core.config.interactor.model.goldenrecord.IConfigDetailsForTypeInfoModel;
import com.cs.core.runtime.interactor.model.configdetails.IMulticlassificationRequestModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IGetConfigDetailsForTypeInfoStrategy extends 
IRuntimeStrategy<IMulticlassificationRequestModel, IConfigDetailsForTypeInfoModel> {

}
