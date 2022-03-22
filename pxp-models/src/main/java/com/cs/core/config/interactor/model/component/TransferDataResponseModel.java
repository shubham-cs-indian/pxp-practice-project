package com.cs.core.config.interactor.model.component;

import com.cs.core.config.interactor.model.klass.ITypeInfoWithContentIdentifiersModel;
import com.cs.core.config.interactor.model.klass.TypeInfoWithContentIdentifiersModel;
import com.cs.core.config.interactor.model.transfer.IInstanceVariantsDeleteResponseModel;
import com.cs.core.config.interactor.model.transfer.ITransferDataResponseModel;
import com.cs.core.runtime.interactor.model.datarule.ApplyEffectModel;
import com.cs.core.runtime.interactor.model.datarule.IApplyEffectModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetInstanceTypesResponseModel;
import com.cs.core.runtime.interactor.model.languageinstance.DeleteTranslationRequestModel;
import com.cs.core.runtime.interactor.model.languageinstance.IDeleteTranslationRequestModel;
import com.cs.core.runtime.interactor.model.propagation.IPropertyInstanceUniquenessEvaluationForPropagationModel;
import com.cs.core.runtime.interactor.model.propagation.PropertyInstanceUniquenessEvaluationForPropagationModel;
import com.cs.core.runtime.interactor.model.searchable.IUpdateSearchableInstanceModel;
import com.cs.core.runtime.interactor.model.searchable.UpdateSearchableInstanceModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransferDataResponseModel implements ITransferDataResponseModel {
  
  private static final long                                                serialVersionUID = 1L;
  protected List<ITypeInfoWithContentIdentifiersModel>                     success;
  protected List<String>                                                   failedIds;
  protected List<IApplyEffectModel>                                        applyEffect;
  protected List<IInstanceVariantsDeleteResponseModel>                     instanceVariantsDeleteResponse;
  protected List<IPropertyInstanceUniquenessEvaluationForPropagationModel> dataForUniquenessStatEvaluation;
  protected List<String>                                                   side2InstanceIds = new ArrayList<>();
  protected Map<String, String>                                            oldAndNewIdsMap;
  protected IGetInstanceTypesResponseModel                                 typesModel;
  protected List<IUpdateSearchableInstanceModel>                           updateSearchableDocumentData;
  protected List<ITypeInfoWithContentIdentifiersModel>                     dataForVariantsStatEvaluation;
  protected IDeleteTranslationRequestModel                                 languageInstanceDeleteResponse;
  
  @Override
  public List<ITypeInfoWithContentIdentifiersModel> getSuccess()
  {
    if (success == null) {
      success = new ArrayList<>();
    }
    return success;
  }
  
  @Override
  @JsonDeserialize(contentAs = TypeInfoWithContentIdentifiersModel.class)
  public void setSuccess(List<ITypeInfoWithContentIdentifiersModel> success)
  {
    this.success = success;
  }
  
  @Override
  public List<String> getFailedIds()
  {
    if (failedIds == null) {
      failedIds = new ArrayList<>();
    }
    return failedIds;
  }
  
  @Override
  public void setFailedIds(List<String> failedIds)
  {
    this.failedIds = failedIds;
  }
  
  @Override
  public List<IApplyEffectModel> getApplyEffect()
  {
    return applyEffect;
  }
  
  @JsonDeserialize(contentAs = ApplyEffectModel.class)
  @Override
  public void setApplyEffect(List<IApplyEffectModel> applyEffect)
  {
    this.applyEffect = applyEffect;
  }
  
  @Override
  public List<IInstanceVariantsDeleteResponseModel> getInstanceVariantsDeleteResponse()
  {
    return instanceVariantsDeleteResponse;
  }
  
  @JsonDeserialize(contentAs = InstanceVariantsDeleteResponseModel.class)
  @Override
  public void setInstanceVariantsDeleteResponse(
      List<IInstanceVariantsDeleteResponseModel> instanceVariantsDeleteResponse)
  {
    this.instanceVariantsDeleteResponse = instanceVariantsDeleteResponse;
  }
  
  @Override
  public List<IPropertyInstanceUniquenessEvaluationForPropagationModel> getDataForUniquenessStatEvaluation()
  {
    if (dataForUniquenessStatEvaluation == null) {
      dataForUniquenessStatEvaluation = new ArrayList<>();
    }
    return dataForUniquenessStatEvaluation;
  }
  
  @Override
  @JsonDeserialize(contentAs = PropertyInstanceUniquenessEvaluationForPropagationModel.class)
  public void setDataForUniquenessStatEvaluation(
      List<IPropertyInstanceUniquenessEvaluationForPropagationModel> dataForUniquenessStatEvaluation)
  {
    this.dataForUniquenessStatEvaluation = dataForUniquenessStatEvaluation;
  }
  
  @Override
  public List<String> getSide2InstanceIds()
  {
    return side2InstanceIds;
  }
  
  @Override
  public void setSide2InstanceIds(List<String> side2InstanceIds)
  {
    this.side2InstanceIds = side2InstanceIds;
  }
  
  @Override
  public Map<String, String> getOldAndNewIdsMap()
  {
    if (oldAndNewIdsMap == null) {
      oldAndNewIdsMap = new HashMap<String, String>();
    }
    return oldAndNewIdsMap;
  }
  
  @Override
  public void setOldAndNewIdsMap(Map<String, String> oldAndNewIdsMap)
  {
    this.oldAndNewIdsMap = oldAndNewIdsMap;
  }
  
  public IGetInstanceTypesResponseModel getTypesModel()
  {
    return typesModel;
  }
  
  @Override
  public void setTypesModel(IGetInstanceTypesResponseModel typesModel)
  {
    this.typesModel = typesModel;
  }
  
  @Override
  public List<IUpdateSearchableInstanceModel> getUpdateSearchableDocumentData()
  {
    return updateSearchableDocumentData;
  }
  
  @Override
  @JsonDeserialize(contentAs = UpdateSearchableInstanceModel.class)
  public void setUpdateSearchableDocumentData(
      List<IUpdateSearchableInstanceModel> updateSearchableDocumentData)
  {
    this.updateSearchableDocumentData = updateSearchableDocumentData;
  }
  
  @Override
  public List<ITypeInfoWithContentIdentifiersModel> getDataForVariantsStatEvaluation()
  {
    return dataForVariantsStatEvaluation;
  }
  
  @Override
  @JsonDeserialize(contentAs = TypeInfoWithContentIdentifiersModel.class)
  public void setDataForVariantsStatEvaluation(
      List<ITypeInfoWithContentIdentifiersModel> dataForVariantsStatEvaluation)
  {
    this.dataForVariantsStatEvaluation = dataForVariantsStatEvaluation;
  }
  
  @Override
  public IDeleteTranslationRequestModel getLanguageInstanceDeleteResponse()
  {
    return languageInstanceDeleteResponse;
  }
  
  @Override
  @JsonDeserialize(as = DeleteTranslationRequestModel.class)
  public void setLanguageInstanceDeleteResponse(
      IDeleteTranslationRequestModel languageInstanceDeleteResponse)
  {
    this.languageInstanceDeleteResponse = languageInstanceDeleteResponse;
  }
}
