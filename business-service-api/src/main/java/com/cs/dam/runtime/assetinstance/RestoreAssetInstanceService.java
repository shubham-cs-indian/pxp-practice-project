package com.cs.dam.runtime.assetinstance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.abstrct.versions.AbstractRestoreInstanceService;
import com.cs.core.runtime.interactor.constants.application.Constants;
import com.cs.core.runtime.interactor.model.bulkpropagation.IBulkResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.dam.runtime.assetinstance.linksharing.IDeleteAssetWithoutTIVsFromSharedSwiftServerService;

/**
 * Service class to restore asset instances
 * @author pranav.huchche
 *
 */
@Service
public class RestoreAssetInstanceService
    extends AbstractRestoreInstanceService<IIdsListParameterModel, IBulkResponseModel>
    implements IRestoreAssetInstanceService {
  
  @Autowired
  protected IDeleteAssetWithoutTIVsFromSharedSwiftServerService deleteAssetWithoutTIVsFromSharedSwiftServerService;
  
  /**
   * Abstract method to return base type.
   */
  @Override
  protected String getBaseType()
  {
    return Constants.ASSET_INSTANCE_BASE_TYPE;
  }
  
}
