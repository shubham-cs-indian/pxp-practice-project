package com.cs.dam.runtime.interactor.usecase.assetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.assetinstance.IAssetInstanceExportRequestModel;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetInstanceExportResponseModel;
import com.cs.core.runtime.interactor.usecase.assetinstance.IGetAssetInstanceToExport;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.dam.runtime.assetinstance.IGetAssetInstanceToExportService;

@Service
public class GetAssetInstanceToExport extends AbstractRuntimeInteractor<IAssetInstanceExportRequestModel, IAssetInstanceExportResponseModel>
    implements IGetAssetInstanceToExport {
  
  @Autowired
  protected IGetAssetInstanceToExportService getAssetInstanceToExportService;
  
  @Override
  protected IAssetInstanceExportResponseModel executeInternal(IAssetInstanceExportRequestModel requestModel) 
      throws Exception
  {
    return getAssetInstanceToExportService.execute(requestModel);
  }
}
