package com.cs.core.config.assetserver;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.idto.IBGProcessDTO.BGPPriority;
import com.cs.core.bgprocess.idto.IBulkAssetUploadDTO;
import com.cs.core.config.interactor.model.asset.IAssetFileModel;
import com.cs.core.config.interactor.model.asset.IUploadAssetModel;
import com.cs.core.config.interactor.model.asset.IUploadSmartDocumentModel;
import com.cs.core.config.interactor.model.configdetails.IBulkUploadResponseAssetModel;
import com.cs.core.json.JSONContent;
import com.cs.core.technical.exception.CSFormatException;
import com.cs.core.technical.exception.CSInitializationException;
import com.cs.di.workflow.trigger.standard.IApplicationTriggerModel.ApplicationActionType;

@Service("uploadMultipleSmartDocumentsToServerService")
public class UploadMultipleSmartDocumentsToServerService extends UploadMultipleAssetsToServerService implements IUploadMultipleAssetsToServerService {
  
  @Override
  protected void initiateUpload(IUploadAssetModel model, List<IAssetFileModel> fileModelList, IBulkUploadResponseAssetModel responseModel, Boolean detectDuplicate)
      throws CSInitializationException, Exception, CSFormatException
  {
    IBulkAssetUploadDTO bulkSmartDocumentUploadDTO = super.prepareBulkAssetUploadDTO(fileModelList, model, detectDuplicate);
    bulkSmartDocumentUploadDTO.setLocaleID(((IUploadSmartDocumentModel) model).getPresetLanguageCode());
    bulkSmartDocumentUploadDTO.setBaseType(((IUploadSmartDocumentModel) model).getBaseType());
    BGPDriverDAO.instance().submitBGPProcess(transactionThread.getTransactionData().getUserName(),
        ApplicationActionType.SMART_DOCUMENT_UPLOAD.toString(), null, BGPPriority.MEDIUM, new JSONContent(bulkSmartDocumentUploadDTO.toJSON()));
  }
}
