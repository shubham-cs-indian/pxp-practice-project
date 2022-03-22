package com.cs.core.runtime.strategy.usecase.goldenrecord;

import com.cs.core.config.interactor.model.goldenrecord.IConfigDetailsForGetKlassInstancesToMergeModel;
import com.cs.core.config.interactor.model.goldenrecord.IGetConfigDetailsForComparisonRequestModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IGetConfigDetailsForGetKlassInstancesToMergeStrategy  extends 
IRuntimeStrategy<IGetConfigDetailsForComparisonRequestModel, IConfigDetailsForGetKlassInstancesToMergeModel>{

}
