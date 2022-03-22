package com.cs.dam.runtime.interactor.usecase.assetinstance.linksharing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.dam.runtime.assetinstance.linksharing.IDeleteAssetWithoutTIVsFromSharedSwiftServerService;

/**
 * 
 * @author mrunali.dhenge
 * 
 *         Delete asset from shared container of swift server and shared object
 *         id from assetmisctable
 *         
 */
@Service
public class DeleteAssetWithoutTIVsFromSharedSwiftServer
    extends AbstractRuntimeInteractor<IIdParameterModel, IIdParameterModel>
    implements IDeleteAssetWithoutTIVsFromSharedSwiftServer {
  
  @Autowired
  protected IDeleteAssetWithoutTIVsFromSharedSwiftServerService deleteAssetWithoutTIVsFromSharedSwiftServerService;
  
  @Override
  protected IIdParameterModel executeInternal(IIdParameterModel model) throws Exception
  {
    return deleteAssetWithoutTIVsFromSharedSwiftServerService.execute(model);
  }
}
