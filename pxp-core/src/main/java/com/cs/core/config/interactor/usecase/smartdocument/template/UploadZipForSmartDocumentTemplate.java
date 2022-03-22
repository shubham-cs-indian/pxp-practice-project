package com.cs.core.config.interactor.usecase.smartdocument.template;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractCreateConfigInteractor;
import com.cs.core.config.interactor.model.asset.IAssetUploadDataModel;
import com.cs.core.config.interactor.model.asset.IUploadZipForSmartDocumentTemplateResponseModel;
import com.cs.core.config.smartdocument.template.IUploadZipForSmartDocumentTemplateService;

/**
 * This is service class uploading zip for Smart Document Template.
 * 
 * @author vannya.kalani
 *
 */
@Service
public class UploadZipForSmartDocumentTemplate
    extends AbstractCreateConfigInteractor<IAssetUploadDataModel, IUploadZipForSmartDocumentTemplateResponseModel>
    implements IUploadZipForSmartDocumentTemplate {
  
  @Autowired
  protected IUploadZipForSmartDocumentTemplateService uploadZipForSmartDocumentTemplateService;
  
  @Override
  protected IUploadZipForSmartDocumentTemplateResponseModel executeInternal(IAssetUploadDataModel dataModel) throws Exception
  {
    return uploadZipForSmartDocumentTemplateService.execute(dataModel);
  }
}
