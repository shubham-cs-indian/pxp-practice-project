package com.cs.core.runtime.interactor.model.assetinstance;

import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;
import com.cs.core.runtime.interactor.model.klassinstance.IKlassInstanceInformationModel;

import java.util.List;

public interface IBulkAssetInstanceInformationModel extends IBulkResponseModel {
  
  public void setSuccess(List<IKlassInstanceInformationModel> success);
}
