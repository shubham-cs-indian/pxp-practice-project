package com.cs.dam.runtime.assetinstance.linksharing;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.rdbms.asset.dao.AssetMiscDAO;
import com.cs.core.rdbms.asset.idao.IAssetMiscDAO;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;

@Service
public class GetAssetInstanceLinkService extends AbstractRuntimeService<IIdParameterModel, IIdParameterModel>
    implements IGetAssetInstanceLinkService {
  
  @Value(value = "${linksharing.asset.shareurl}")
  protected String shareUrl;
  
  @Override
  protected IIdParameterModel executeInternal(IIdParameterModel model) throws Exception
  {
    IIdParameterModel idParameterModel = new IdParameterModel();
    IAssetMiscDAO assetMiscDao = AssetMiscDAO.getInstance();
    
    String sharedObjectId = assetMiscDao.getSharedAssetLink(Long.parseLong(model.getId()));
    if (StringUtils.isNotBlank(sharedObjectId)) {
      idParameterModel.setId(shareUrl + sharedObjectId);
    }
    return idParameterModel;
  }
  
}
