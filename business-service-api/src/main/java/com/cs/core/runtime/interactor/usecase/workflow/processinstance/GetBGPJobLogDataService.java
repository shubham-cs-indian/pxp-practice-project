package com.cs.core.runtime.interactor.usecase.workflow.processinstance;

import java.io.File;
import java.io.FileInputStream;

import org.springframework.stereotype.Component;

import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.model.asset.GetAssetDetailsResponseModel;
import com.cs.core.runtime.interactor.model.assetinstance.IGetAssetDetailsResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;


@Component
public class GetBGPJobLogDataService
    extends AbstractRuntimeService<IIdParameterModel, IGetAssetDetailsResponseModel>
    implements IGetBGPJobLogDataService {

  @Override
  public IGetAssetDetailsResponseModel executeInternal(IIdParameterModel dataModel) throws Exception
  {
    File logs = BGPDriverDAO.instance().loadJobLogFile(Long.valueOf(dataModel.getId()));
    
    IGetAssetDetailsResponseModel fileModel = new GetAssetDetailsResponseModel();
    fileModel.setContentType("application/octet-stream");
    fileModel.setContentDisposition("attachment; filename=\"logs-"+dataModel.getId()+".txt\"");
    fileModel.setInputStream(new FileInputStream(logs));
    return fileModel;
  }
}
