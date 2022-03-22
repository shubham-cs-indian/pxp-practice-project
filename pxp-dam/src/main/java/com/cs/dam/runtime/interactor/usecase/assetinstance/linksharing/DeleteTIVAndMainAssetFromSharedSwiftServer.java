package com.cs.dam.runtime.interactor.usecase.assetinstance.linksharing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;
import com.cs.dam.runtime.assetinstance.linksharing.IDeleteTIVAndMainAssetFromSharedSwiftServerService;

/**
 * 
 * @author mrunali.dhenge
 * 
 *         Delete main asset instance and its technical image variants From
 *         shared container of swift server and their shared object id from
 *         assetmisc table
 * 
 */

@Service
public class DeleteTIVAndMainAssetFromSharedSwiftServer
    extends AbstractRuntimeInteractor<IIdsListParameterModel, IIdParameterModel>
    implements IDeleteTIVAndMainAssetFromSharedSwiftServer {
  
  @Autowired
  protected IDeleteTIVAndMainAssetFromSharedSwiftServerService deleteTIVAndMainAssetFromSharedSwiftServerService;
  
  @Override
  protected IIdParameterModel executeInternal(IIdsListParameterModel model) throws Exception
  {
    return deleteTIVAndMainAssetFromSharedSwiftServerService.execute(model);
  }
  
}
