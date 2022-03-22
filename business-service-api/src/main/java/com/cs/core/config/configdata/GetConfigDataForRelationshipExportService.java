package com.cs.core.config.configdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.configdetails.IGetConfigDataResponseModel;
import com.cs.core.config.interactor.model.klass.IGetConfigDataForRelationshipExportModel;
import com.cs.core.config.strategy.usecase.klass.IGetConfigDataForRelationshipExportStrategy;

@Service
public class GetConfigDataForRelationshipExportService
    extends AbstractGetConfigService<IGetConfigDataForRelationshipExportModel, IGetConfigDataResponseModel>
    implements IGetConfigDataForRelationshipExport {
  
  @Autowired
  IGetConfigDataForRelationshipExportStrategy getConfigDataForRelationshipExportStrategy;
  
  @Override
  public IGetConfigDataResponseModel executeInternal(IGetConfigDataForRelationshipExportModel dataModel) throws Exception
  {
    return getConfigDataForRelationshipExportStrategy.execute(dataModel);
  }
  
}
