package com.cs.core.runtime.interactor.usecase.instance;

import java.util.ArrayList;
import java.util.List;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.runtime.interactor.entity.klassinstance.IKlassInstance;
import com.cs.core.runtime.interactor.exception.configuration.DeleteInstanceFailedException;
import com.cs.core.runtime.interactor.exception.permission.UserNotHaveDeletePermission;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.model.klassinstance.DeleteInstancesResponseModel;
import com.cs.core.runtime.interactor.model.klassinstance.IDeleteInstancesResponseModel;
import com.cs.core.runtime.interactor.model.klassinstance.IDeleteKlassInstanceResponseModel;
import com.cs.core.runtime.interactor.model.pluginexception.ExceptionModel;
import com.cs.core.runtime.interactor.model.pluginexception.IExceptionModel;
import com.cs.core.runtime.interactor.usecase.base.AbstractRuntimeInteractor;

public abstract class AbstractDeleteInstances<P extends IIdsListParameterModel, R extends IDeleteKlassInstanceResponseModel>
    extends AbstractRuntimeInteractor<P, R> {

  protected abstract IListModel<IKlassInstance> getKlassInstanceByIdsForPermissionCheck(
      IIdsListParameterModel idsListParameterModel) throws Exception;
  
  protected abstract IDeleteInstancesResponseModel executeDeleteInstances(
      IIdsListParameterModel idsToDeleteListParameterModel) throws Exception;
  
  protected abstract UserNotHaveDeletePermission getUserNotHaveDeletePermissionException();
  
  @Override
  @SuppressWarnings("unchecked")
  protected R executeInternal(P deleteModel) throws Exception
  {
    List<String> idsToDelete = new ArrayList<>();
    /*List<String> idsAlreadyDeleted = new ArrayList<>();*/
    IExceptionModel failure = new ExceptionModel();
    
    /* IIdsListParameterModel idsListParameterModel = new IdsListParameterModel();
      idsListParameterModel.setIds(deleteModel.getIds());
      IListModel<IKlassInstance> klassInstances = getKlassInstanceByIdsForPermissionCheck(idsListParameterModel);
    
      if (klassInstances.getList().size() != deleteModel.getIds().size()) {
        idsAlreadyDeleted.addAll(deleteModel.getIds());
      }
    
      for (IKlassInstance iKlassInstance : klassInstances.getList()) {
        try {
          if (idsAlreadyDeleted.contains(iKlassInstance.getId())) {
            idsAlreadyDeleted.remove(iKlassInstance.getId());
          }
    
          IGlobalPermission globalPermission = permissionUtils.getGlobalPermission(iKlassInstance);
          if (!globalPermission.getCanDelete()) {
            throw new UserNotHaveDeletePermission();
          }
          idsToDelete.add(iKlassInstance.getId());
        }
        catch (UserNotHaveDeletePermission e) {
          ExceptionUtil.addFailureDetailsToFailureObject(failure,
              getUserNotHaveDeletePermissionException(), iKlassInstance.getId(), null);
        }
        catch (Exception e) {
          ExceptionUtil.addFailureDetailsToFailureObject(failure, e, iKlassInstance.getId(), null);
        }
      }
    */
    IDeleteInstancesResponseModel deleteResponseModel = new DeleteInstancesResponseModel();
    try {
      if (idsToDelete.size() > 0) {
        IIdsListParameterModel idsToDeleteListParameterModel = new IdsListParameterModel();
        idsToDeleteListParameterModel.setIds(idsToDelete);
        deleteResponseModel = executeDeleteInstances(idsToDeleteListParameterModel);
      }
    }
    catch (DeleteInstanceFailedException e) {
      e.getExceptionDetails()
          .addAll(failure.getExceptionDetails());
      e.getDevExceptionDetails()
          .addAll(failure.getDevExceptionDetails());
      throw e;
    }
    /*List<String> success = (List<String>) deleteResponseModel.getSuccess();
    List<String> variantInstanceIds = deleteResponseModel.getDeletedVariantInstanceIds();
    List<String> deletedContentAndTheirVariantIds = new ArrayList<>();
    deletedContentAndTheirVariantIds.addAll(success);
    deletedContentAndTheirVariantIds.addAll(variantInstanceIds);
    List<IDeletedContentTypeIdsInfoModel> klassInstancesToUpdate = deleteResponseModel.getKlassInstancesToUpdate();
    
    IContentDiffModelToPrepareDataForBulkPropagation diffModelToPrepareData = new ContentDiffModelToPrepareDataForBulkPropagation();
    IDeleteContentsDataPreparationModel deletedContentsInfo = new DeleteContentsDataPreparationModel();
    deletedContentsInfo.setDeletedContentIds(deletedContentAndTheirVariantIds);
    deletedContentsInfo.setKlassInstancesToUpdate(klassInstancesToUpdate);
    diffModelToPrepareData.setDeletedContentsInfo(deletedContentsInfo);
    kafkaUtils.prepareMessageData(diffModelToPrepareData, PrepareDataForBulkPropagationTask.class.getName());
    
    success.addAll(idsAlreadyDeleted);
    deleteResponseModel.setSuccess(success);
    IExceptionModel elasticResponseFailure = deleteResponseModel.getFailure();
    if(elasticResponseFailure != null) {
      elasticResponseFailure.getExceptionDetails().addAll(failure.getExceptionDetails());
      elasticResponseFailure.getDevExceptionDetails().addAll(failure.getDevExceptionDetails());
    }
    IDeleteKlassInstanceResponseModel responseModel = new DeleteKlassInstanceResponseModel();
    responseModel.setSuccess(success);
    responseModel.setFailure(elasticResponseFailure);*/
    return (R) deleteResponseModel;
  }
}
