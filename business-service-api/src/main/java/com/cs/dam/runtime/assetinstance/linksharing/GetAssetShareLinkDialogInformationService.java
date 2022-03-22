package com.cs.dam.runtime.assetinstance.linksharing;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.strategy.usecase.asset.IGetAssetShareDialogInformationConfigDetailsStrategy;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.runtime.interactor.model.assetinstance.IAssetShareDialogInformationModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListWithUserModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListWithUserModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.dam.runtime.assetinstance.linksharing.IGetAssetShareLinkDialogInformationService;
import com.cs.utils.BaseEntityUtils;

/***
 * This service will return the information for the link sharing dialog.
 * 
 * @author vannya.kalani
 *
 */
@Service
public class GetAssetShareLinkDialogInformationService
    extends AbstractRuntimeService<IIdsListParameterModel, IAssetShareDialogInformationModel>
    implements IGetAssetShareLinkDialogInformationService {
  
  @Autowired
  RDBMSComponentUtils                                  rdbmsComponentUtils;
  
  @Autowired
  IGetAssetShareDialogInformationConfigDetailsStrategy getAssetShareDialogInformationConfigDetailsStrategy;
  
  @Override
  protected IAssetShareDialogInformationModel executeInternal(IIdsListParameterModel model)
      throws Exception
  {
    List<String> allTypesByIds = getAllTypesByIdsStrategy(model);
    IIdsListWithUserModel idsListWithUserModel = new IdsListWithUserModel();
    idsListWithUserModel.setIds(allTypesByIds);
    idsListWithUserModel.setUserId(rdbmsComponentUtils.getUserID());
    IAssetShareDialogInformationModel returnModel = getAssetShareDialogInformationConfigDetailsStrategy.execute(idsListWithUserModel);
    returnModel.setMasterAssetIdsList(model.getIds());
    return returnModel;
  }
  
  /***
   * This method will return all classifier codes for the given instance iids.
   * 
   * @param dataModel
   * @return
   * @throws Exception
   */
  private List<String> getAllTypesByIdsStrategy(IIdsListParameterModel dataModel) throws Exception
  {
    List<String> assetClassifiers = new ArrayList<>();
    List<String> ids = dataModel.getIds();
    for (String id : ids) {
      IBaseEntityDAO assetInstance = rdbmsComponentUtils.getBaseEntityDAO(Long.parseLong(id));
      assetClassifiers.addAll(BaseEntityUtils.getAllReferenceTypeFromBaseEntity(assetInstance));
    }
    return assetClassifiers;
  }
  
}
