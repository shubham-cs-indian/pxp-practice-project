package com.cs.dam.runtime.interactor.usecase.downloadtracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.dam.downloadtracker.IResetDownloadCountService;

/**
 * 
 * @author mrunali.dhenge Reset DownloadCount From AssetMisc table
 *
 */
@Service
public class ResetDownloadCount extends AbstractRuntimeInteractor<IIdParameterModel, IIdParameterModel> implements IResetDownloadCount {
  
  @Autowired
  protected IResetDownloadCountService resetDownloadCountService;
  
  @Override
  protected IIdParameterModel executeInternal(IIdParameterModel model) throws Exception
  {
    return resetDownloadCountService.execute(model);
  }
  
}
