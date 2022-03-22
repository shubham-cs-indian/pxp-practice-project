package com.cs.core.runtime.interactor.model.contextdatatransfer;

import com.cs.core.runtime.interactor.model.bulkpropagation.IContentTransferHelperModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IContetxtualDataTransferOnConfigChangeInputModel extends IModel {
  
  public static final String CONTEXT_INFO_FOR_CONTEXTUAL_DATA_TRANSFER = "contextInfoForContextualDataTransfer";
  public static final String CONTENT_TRANSFER_MAPPING                  = "contentTransferMapping";
  
  public IContextInfoForContextualDataTransferModel getContextInfoForContextualDataTransfer();
  
  public void setContextInfoForContextualDataTransfer(
      IContextInfoForContextualDataTransferModel contextInfoForContextualDataTransfer);
  
  public IContentTransferHelperModel getContentTransferMapping();
  
  public void setContentTransferMapping(IContentTransferHelperModel contentTransferMapping);
}
