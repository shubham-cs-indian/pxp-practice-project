package com.cs.core.config.interactor.model.transfer;

import com.cs.core.config.interactor.model.klass.ITypeInfoWithContentIdentifiersModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.datarule.IApplyEffectModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetInstanceTypesResponseModel;
import com.cs.core.runtime.interactor.model.languageinstance.IDeleteTranslationRequestModel;
import com.cs.core.runtime.interactor.model.propagation.IPropertyInstanceUniquenessEvaluationForPropagationModel;
import com.cs.core.runtime.interactor.model.searchable.IUpdateSearchableInstanceModel;

import java.util.List;
import java.util.Map;

public interface ITransferDataResponseModel extends IModel {
  
  public static final String SUCCESS                             = "success";
  public static final String FAILED_IDS                          = "failedIds";
  public static final String APPLY_EFFECT                        = "applyEffect";
  public static final String INSTANCE_VARIANTS_DELETE_RESPONSE   = "instanceVariantsDeleteResponse";
  public static final String DATA_FOR_UNIQUENESS_STAT_EVALUATION = "dataForUniquenessStatEvaluation";
  public static final String SIDE2_INSTANCE_IDS                  = "side2InstanceIds";
  public static final String OLD_AND_NEW_IDS_MAP                 = "oldAndNewIdsMap";
  public static final String TYPES_MODEL                         = "typesModel";
  public static final String UPDATE_SEARCHABLE_DOCUMENT_DATA     = "updateSearchableDocumentData";
  public static final String DATA_FOR_VARIANTS_STAT_EVALUATION   = "dataForVariantsStatEvaluation";
  public static final String LANGUAGE_INSTANCE_DELETE_RESPONSE   = "languageInstanceDeleteResponse";
  
  public List<ITypeInfoWithContentIdentifiersModel> getSuccess();
  
  public void setSuccess(List<ITypeInfoWithContentIdentifiersModel> success);
  
  public List<String> getFailedIds();
  
  public void setFailedIds(List<String> failedIds);
  
  public List<IApplyEffectModel> getApplyEffect();
  
  public void setApplyEffect(List<IApplyEffectModel> applyEffect);
  
  public List<IInstanceVariantsDeleteResponseModel> getInstanceVariantsDeleteResponse();
  
  public void setInstanceVariantsDeleteResponse(
      List<IInstanceVariantsDeleteResponseModel> instanceVariantsDeleteResponse);
  
  public List<IPropertyInstanceUniquenessEvaluationForPropagationModel> getDataForUniquenessStatEvaluation();
  
  public void setDataForUniquenessStatEvaluation(
      List<IPropertyInstanceUniquenessEvaluationForPropagationModel> dataForUniquenessStatEvaluation);
  
  public List<String> getSide2InstanceIds();
  
  public void setSide2InstanceIds(List<String> side2InstanceIds);
  
  public Map<String, String> getOldAndNewIdsMap();
  
  public void setOldAndNewIdsMap(Map<String, String> oldAndNewIdsMap);
  
  public IGetInstanceTypesResponseModel getTypesModel();
  
  public void setTypesModel(IGetInstanceTypesResponseModel typesModel);
  
  public List<IUpdateSearchableInstanceModel> getUpdateSearchableDocumentData();
  
  public void setUpdateSearchableDocumentData(
      List<IUpdateSearchableInstanceModel> updateSearchableDocumentData);
  
  public List<ITypeInfoWithContentIdentifiersModel> getDataForVariantsStatEvaluation();
  
  public void setDataForVariantsStatEvaluation(List<ITypeInfoWithContentIdentifiersModel> success);
  
  public IDeleteTranslationRequestModel getLanguageInstanceDeleteResponse();
  
  public void setLanguageInstanceDeleteResponse(
      IDeleteTranslationRequestModel languageInstanceDeleteResponse);
}
