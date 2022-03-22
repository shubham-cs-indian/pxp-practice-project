package com.cs.dam.runtime.downloadtracker;

import org.springframework.stereotype.Service;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.rdbms.asset.dao.AssetMiscDAO;
import com.cs.core.rdbms.asset.idao.IAssetMiscDAO;
import com.cs.core.rdbms.downloadtracker.idao.IDownloadTrackerDAO;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.dam.downloadtracker.IResetDownloadCountService;
import com.cs.dam.rdbms.downloadtracker.dao.DownloadTrackerDAO;

/**
 * 
 * @author mrunali.dhenge Reset DownloadCount From AssetMisc table
 *
 */
@Service
public class ResetDownloadCountService extends AbstractRuntimeService<IIdParameterModel, IIdParameterModel>
    implements IResetDownloadCountService {
  
  @Override
  protected IIdParameterModel executeInternal(IIdParameterModel model) throws Exception
  {
    IAssetMiscDAO assetMiscDAO = AssetMiscDAO.getInstance();
    String isExist = assetMiscDAO.getAssetMiscRecordById(Long.parseLong(model.getId()));
    if (isExist != AssetMiscDAO.IS_NOT_PRESENT) {
      IDownloadTrackerDAO downloadTrackerDAO = DownloadTrackerDAO.getInstance();
      downloadTrackerDAO.resetDownloadCount(Long.parseLong(model.getId()));
    }
    return null;
  }
  
}
