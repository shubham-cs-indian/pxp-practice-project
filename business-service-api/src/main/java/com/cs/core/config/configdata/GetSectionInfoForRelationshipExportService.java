package com.cs.core.config.configdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractGetConfigService;
import com.cs.core.config.interactor.model.klass.IGetSectionInfoForRelationshipExportModel;
import com.cs.core.config.interactor.model.klass.IGetSectionInfoForRelationshipExportResponseModel;
import com.cs.core.config.strategy.usecase.klass.IGetSectionInfoForRelationshipExportStrategy;

@Service
public class GetSectionInfoForRelationshipExportService extends AbstractGetConfigService<IGetSectionInfoForRelationshipExportModel, IGetSectionInfoForRelationshipExportResponseModel>
    implements IGetSectionInfoForRelationshipExportService {
  
  @Autowired
  protected IGetSectionInfoForRelationshipExportStrategy getRelDataStrategy;
  
  @Override
  public IGetSectionInfoForRelationshipExportResponseModel executeInternal(IGetSectionInfoForRelationshipExportModel dataModel) throws Exception
  { 
    return getRelDataStrategy.execute(dataModel);
  }
  
}
