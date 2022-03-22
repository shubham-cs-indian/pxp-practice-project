package com.cs.core.config.configdata;


import com.cs.core.config.interactor.model.configdetails.IGetConfigDataResponseModel;
import com.cs.core.config.interactor.model.klass.IGetConfigDataForRelationshipExportModel;
import com.cs.core.config.strategy.configuration.base.IConfigStrategy;

public interface IGetConfigDataForRelationshipExport extends
IConfigStrategy<IGetConfigDataForRelationshipExportModel, IGetConfigDataResponseModel> {
  
}
