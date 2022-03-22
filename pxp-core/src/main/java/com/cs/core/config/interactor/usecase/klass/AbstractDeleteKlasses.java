package com.cs.core.config.interactor.usecase.klass;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.cs.config.interactor.usecase.base.AbstractDeleteConfigInteractor;
import com.cs.core.config.interactor.exception.entityconfiguration.EntityConfigurationDependencyException;
import com.cs.core.config.interactor.model.configdetails.BulkDeleteReturnModel;
import com.cs.core.config.interactor.model.configdetails.GetEntityConfigurationRequestModel;
import com.cs.core.config.interactor.model.configdetails.IGetEntityConfigurationResponseModel;
import com.cs.core.config.interactor.model.klass.IBulkDeleteKlassResponseModel;
import com.cs.core.config.interactor.model.klass.IBulkDeleteSuccessKlassResponseModel;
import com.cs.core.config.interactor.model.klass.IGetChildKlassIdsInHierarchyModel;
import com.cs.core.config.strategy.usecase.entityconfiguration.IGetEntityConfigurationStrategy;
import com.cs.core.config.strategy.usecase.klass.IGetChildKlassIdsInHierarchyStrategy;
import com.cs.core.runtime.interactor.exception.delete.InstanceExistsForTypesException;
import com.cs.core.runtime.interactor.model.bulkpropagation.ContentDiffModelToPrepareDataForBulkPropagation;
import com.cs.core.runtime.interactor.model.bulkpropagation.IContentDiffModelToPrepareDataForBulkPropagation;
import com.cs.core.runtime.interactor.model.configuration.IBulkDeleteReturnModel;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;

@SuppressWarnings("unchecked")
public abstract class AbstractDeleteKlasses<P extends IIdsListParameterModel, R extends IBulkDeleteReturnModel>
    extends AbstractDeleteConfigInteractor<P, R> {
  
  @Autowired
  protected IGetChildKlassIdsInHierarchyStrategy getChildKlassIdsInHierarchyStrategy;
  
  @Autowired
  protected IGetEntityConfigurationStrategy      getKlassEntityConfigurationStrategy;

  protected abstract String getKlassType();
  
  protected abstract IBulkDeleteKlassResponseModel executeBulkDelete(IIdsListParameterModel model)
      throws Exception;
  
  @Override
  protected R executeInternal(P model) throws Exception
  {

    IGetEntityConfigurationResponseModel getEntityResponse = getKlassEntityConfigurationStrategy
        .execute(new GetEntityConfigurationRequestModel(model.getIds()));
    Map<String, IIdLabelCodeModel> referenceData = getEntityResponse.getReferenceData();
    boolean hasChildDependency = getEntityResponse.isHasChildDependency();

    if (!referenceData.keySet()
        .isEmpty() || hasChildDependency) {
      throw new EntityConfigurationDependencyException();
    }


    IGetChildKlassIdsInHierarchyModel chilKlassHierarchyList = getChildKlassIdsInHierarchyStrategy.execute(model);
    List<Long> klassIds = new ArrayList<Long>();
    chilKlassHierarchyList.getTypeIdVsChildKlassIds().forEach((key, values) -> {
      values.forEach((value) -> {
        klassIds.add(value);
      });
    });
    if (!klassIds.isEmpty()) {
      List<Long> baseentityIIDs = RDBMSUtils.getBaseentityIIDsForTypes(klassIds);
      if (!baseentityIIDs.isEmpty()) {
        throw new InstanceExistsForTypesException();
      }
    }
    
    IBulkDeleteKlassResponseModel responseFromStrategy = executeBulkDelete(model);
    IExceptionModel failure = responseFromStrategy.getFailure();
    
    /*typeIdsHavingInstances.forEach(typeId ->
      ExceptionUtil.addFailureDetailsToFailureObject(failure, new InstanceExistsForTypesException(), typeId, null)
    );*/
    
    IBulkDeleteSuccessKlassResponseModel success = (IBulkDeleteSuccessKlassResponseModel) responseFromStrategy
        .getSuccess();
    IBulkDeleteReturnModel modelToReturn = new BulkDeleteReturnModel();
    List<String> deletedKlassIds = success.getIds();
    if (!deletedKlassIds.isEmpty()) {
      manageValueInheritance(success);
    }
        
    //TODO: BGP
    
    List<String> successIds = new ArrayList<>(deletedKlassIds);
    successIds.retainAll(model.getIds());
    modelToReturn.setSuccess(successIds);
    modelToReturn.setFailure(failure);
    modelToReturn.setAuditLogInfo(responseFromStrategy.getAuditLogInfo());
    deletedKlassIds.retainAll(model.getIds());
    return (R) modelToReturn;
  }
  
  private void manageValueInheritance(IBulkDeleteSuccessKlassResponseModel success) throws Exception
  {
    IContentDiffModelToPrepareDataForBulkPropagation contentDiffModelToPrepareDataForBulkPropagation = new ContentDiffModelToPrepareDataForBulkPropagation();
    contentDiffModelToPrepareDataForBulkPropagation.setDeletedKlassIds(success.getIds());
    contentDiffModelToPrepareDataForBulkPropagation
        .setDeletedRelationshipIds(success.getDeletedRelationsipIds());
    contentDiffModelToPrepareDataForBulkPropagation
        .setDeletedRelationshipIds(success.getDeletedNatureRelationshipIds());
    
    //TODO: BGP
  }
}
