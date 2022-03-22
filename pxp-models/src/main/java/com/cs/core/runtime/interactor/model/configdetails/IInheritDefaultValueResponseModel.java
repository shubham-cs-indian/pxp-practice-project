package com.cs.core.runtime.interactor.model.configdetails;

import com.cs.core.config.interactor.model.configdetails.IValueInheritancePropagationModel;
import com.cs.core.config.interactor.model.klass.ITypeInfoWithContentIdentifiersModel;
import com.cs.core.config.interactor.model.language.IKlassInstanceDiffForLanguageInheritanceModel;
import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.datarule.IApplyEffectModel;
import com.cs.core.runtime.interactor.model.propagation.IEvaluateIdentifierAttributesInstanceModel;
import com.cs.core.runtime.interactor.model.propagation.IPropertyInstanceUniquenessEvaluationForPropagationModel;
import com.cs.core.runtime.interactor.model.searchable.IUpdateSearchableInstanceModel;
import com.cs.core.runtime.interactor.model.transfer.IDataTransferInputModel;
import com.cs.core.runtime.interactor.model.variants.IPropagableContextualDataModel;

import java.util.List;
import java.util.Map;

public interface IInheritDefaultValueResponseModel extends IModel {
  
  public static final String INHERITANCE_DATA                             = "inheritanceData";
  public static final String CONTENT_INFO                                 = "contentInfo";
  public static final String APPLY_EFFECT                                 = "applyEffect";
  public static final String DATA_FOR_UNIQUENESS_STAT_EVALUATION          = "dataForUniquenessStatEvaluation";
  public static final String PROPAGABLE_CONTEXTUAL_DATA                   = "propagableContextualData";
  public static final String KLASS_INSTANCE_ID                            = "klassInstanceId";
  public static final String REMOVE_CONFLICT_DATA                         = "removeConflictData";
  public static final String INSTANCES_TO_UPDATE_IS_MERGED                = "instancesToUpdateIsMerged";
  public static final String UPDATE_SEARCHABLE_DOCUMENT_DATA              = "updateSearchableDocumentData";
  public static final String DATA_FOR_DATA_TRANSFER                       = "dataForDataTransfer";
  public static final String KLASS_INSTANCE_DIFF_FOR_LANGUAGE_INHERITANCE = "klassInstanceDiffForLanguageInheritance";
  public static final String UPDTAE_IDENTIFIER_ATTRIBUTE_INSTANCE_MODEL   = "updateIdentifierAttributeInstanceModel";
  
  public List<IPropertyInstanceUniquenessEvaluationForPropagationModel> getDataForUniquenessStatEvaluation();
  
  public void setDataForUniquenessStatEvaluation(
      List<IPropertyInstanceUniquenessEvaluationForPropagationModel> dataForUniquenessStatEvaluation);
  
  public IValueInheritancePropagationModel getInheritanceData();
  
  public void setInheritanceData(IValueInheritancePropagationModel inheritanceData);
  
  public ITypeInfoWithContentIdentifiersModel getContentInfo();
  
  public void setContentInfo(ITypeInfoWithContentIdentifiersModel contentInfo);
  
  public IApplyEffectModel getApplyEffect();
  
  public void setApplyEffect(IApplyEffectModel applyEffect);
  
  // key:contextId
  public Map<String, IPropagableContextualDataModel> getPropagableContextualData();
  
  public void setPropagableContextualData(
      Map<String, IPropagableContextualDataModel> propagableContextualData);
  
  public String getKlassInstanceId();
  
  public void setKlassInstanceId(String klassInstanceId);
  
  public List<IIdAndBaseType> getRemoveConflictData();
  
  public void setRemoveConflictData(List<IIdAndBaseType> removeConflictData);
  
  public List<String> getInstancesToUpdateIsMerged();
  
  public void setInstancesToUpdateIsMerged(List<String> instancesToUpdateIsMerged);
  
  public IUpdateSearchableInstanceModel getUpdateSearchableDocumentData();
  
  public void setUpdateSearchableDocumentData(
      IUpdateSearchableInstanceModel updateSearchableDocumentData);
  
  public IDataTransferInputModel getDataForDataTransfer();
  
  public void setDataForDataTransfer(IDataTransferInputModel dataForDataTransfer);
  
  public IKlassInstanceDiffForLanguageInheritanceModel getKlassInstanceDiffForLanguageInheritance();
  
  public void setKlassInstanceDiffForLanguageInheritance(
      IKlassInstanceDiffForLanguageInheritanceModel klassInstanceDiffForLanguageInheritanceModel);
  
  public IEvaluateIdentifierAttributesInstanceModel getUpdateIdentifierAttributeInstanceModel();
  
  public void setUpdateIdentifierAttributeInstanceModel(
      IEvaluateIdentifierAttributesInstanceModel updateIdentifierAttributeInstanceModel);
}
