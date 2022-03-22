package com.cs.core.runtime.interactor.model.configdetails;

import com.cs.core.config.interactor.model.configdetails.IValueInheritancePropagationModel;
import com.cs.core.config.interactor.model.klass.ITypeInfoWithContentIdentifiersModel;
import com.cs.core.config.interactor.model.klass.TypeInfoWithContentIdentifiersModel;
import com.cs.core.config.interactor.model.language.IKlassInstanceDiffForLanguageInheritanceModel;
import com.cs.core.config.interactor.model.language.KlassInstanceDiffForLanguageInheritanceModel;
import com.cs.core.runtime.interactor.entity.idandtype.IIdAndBaseType;
import com.cs.core.runtime.interactor.entity.idandtype.IdAndBaseType;
import com.cs.core.runtime.interactor.model.bulkpropagation.ValueInheritancePropagationModel;
import com.cs.core.runtime.interactor.model.datarule.ApplyEffectModel;
import com.cs.core.runtime.interactor.model.datarule.IApplyEffectModel;
import com.cs.core.runtime.interactor.model.propagation.EvaluateIdentifierAttributesInstanceModel;
import com.cs.core.runtime.interactor.model.propagation.IEvaluateIdentifierAttributesInstanceModel;
import com.cs.core.runtime.interactor.model.propagation.IPropertyInstanceUniquenessEvaluationForPropagationModel;
import com.cs.core.runtime.interactor.model.propagation.PropertyInstanceUniquenessEvaluationForPropagationModel;
import com.cs.core.runtime.interactor.model.searchable.IUpdateSearchableInstanceModel;
import com.cs.core.runtime.interactor.model.searchable.UpdateSearchableInstanceModel;
import com.cs.core.runtime.interactor.model.transfer.DataTransferInputModel;
import com.cs.core.runtime.interactor.model.transfer.IDataTransferInputModel;
import com.cs.core.runtime.interactor.model.variants.IPropagableContextualDataModel;
import com.cs.core.runtime.interactor.model.variants.PropagableContextualDataModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InheritDefaultValueResponseModel implements IInheritDefaultValueResponseModel {
  
  private static final long                                                serialVersionUID = 1L;
  protected IValueInheritancePropagationModel                              inheritanceData;
  protected ITypeInfoWithContentIdentifiersModel                           contentInfo;
  protected IApplyEffectModel                                              applyEffect;
  protected List<IPropertyInstanceUniquenessEvaluationForPropagationModel> dataForUniquenessStatEvaluation;
  protected Map<String, IPropagableContextualDataModel>                    propagableContextualData;
  protected String                                                         klassInstanceId;
  protected List<IIdAndBaseType>                                           removeConflictData;
  protected List<String>                                                   instancesToUpdateIsMerged;
  protected IUpdateSearchableInstanceModel                                 updateSearchableDocumentData;
  protected IDataTransferInputModel                                        dataForDataTransfer;
  protected IKlassInstanceDiffForLanguageInheritanceModel                  klassInstanceDiffForLanguageInheritanceModel;
  protected IEvaluateIdentifierAttributesInstanceModel                     updateIdentifierAttributeInstanceModel;
  
  @Override
  public IValueInheritancePropagationModel getInheritanceData()
  {
    return inheritanceData;
  }
  
  @Override
  @JsonDeserialize(as = ValueInheritancePropagationModel.class)
  public void setInheritanceData(IValueInheritancePropagationModel inheritanceData)
  {
    this.inheritanceData = inheritanceData;
  }
  
  @Override
  public ITypeInfoWithContentIdentifiersModel getContentInfo()
  {
    return contentInfo;
  }
  
  @Override
  @JsonDeserialize(as = TypeInfoWithContentIdentifiersModel.class)
  public void setContentInfo(ITypeInfoWithContentIdentifiersModel contentInfo)
  {
    this.contentInfo = contentInfo;
  }
  
  @Override
  public IApplyEffectModel getApplyEffect()
  {
    return applyEffect;
  }
  
  @JsonDeserialize(as = ApplyEffectModel.class)
  @Override
  public void setApplyEffect(IApplyEffectModel applyEffect)
  {
    this.applyEffect = applyEffect;
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
  public Map<String, IPropagableContextualDataModel> getPropagableContextualData()
  {
    return propagableContextualData;
  }
  
  @Override
  @JsonDeserialize(contentAs = PropagableContextualDataModel.class)
  public void setPropagableContextualData(
      Map<String, IPropagableContextualDataModel> propagableContextualData)
  {
    this.propagableContextualData = propagableContextualData;
  }
  
  @Override
  public String getKlassInstanceId()
  {
    return klassInstanceId;
  }
  
  @Override
  public void setKlassInstanceId(String klassInstanceId)
  {
    this.klassInstanceId = klassInstanceId;
  }
  
  @Override
  public List<IIdAndBaseType> getRemoveConflictData()
  {
    if (removeConflictData == null) {
      removeConflictData = new ArrayList<>();
    }
    return removeConflictData;
  }
  
  @JsonDeserialize(contentAs = IdAndBaseType.class)
  @Override
  public void setRemoveConflictData(List<IIdAndBaseType> removeConflictData)
  {
    this.removeConflictData = removeConflictData;
  }
  
  @Override
  public List<String> getInstancesToUpdateIsMerged()
  {
    if (instancesToUpdateIsMerged == null) {
      instancesToUpdateIsMerged = new ArrayList<>();
    }
    return instancesToUpdateIsMerged;
  }
  
  @Override
  public void setInstancesToUpdateIsMerged(List<String> instancesToUpdateIsMerged)
  {
    this.instancesToUpdateIsMerged = instancesToUpdateIsMerged;
  }
  
  @Override
  public IUpdateSearchableInstanceModel getUpdateSearchableDocumentData()
  {
    return updateSearchableDocumentData;
  }
  
  @Override
  @JsonDeserialize(as = UpdateSearchableInstanceModel.class)
  public void setUpdateSearchableDocumentData(
      IUpdateSearchableInstanceModel updateSearchableDocumentData)
  {
    this.updateSearchableDocumentData = updateSearchableDocumentData;
  }
  
  @Override
  public IDataTransferInputModel getDataForDataTransfer()
  {
    return dataForDataTransfer;
  }
  
  @Override
  @JsonDeserialize(as = DataTransferInputModel.class)
  public void setDataForDataTransfer(IDataTransferInputModel dataForDataTransfer)
  {
    this.dataForDataTransfer = dataForDataTransfer;
  }
  
  @Override
  public IKlassInstanceDiffForLanguageInheritanceModel getKlassInstanceDiffForLanguageInheritance()
  {
    return klassInstanceDiffForLanguageInheritanceModel;
  }
  
  @JsonDeserialize(as = KlassInstanceDiffForLanguageInheritanceModel.class)
  @Override
  public void setKlassInstanceDiffForLanguageInheritance(
      IKlassInstanceDiffForLanguageInheritanceModel klassInstanceDiffForLanguageInheritanceModel)
  {
    this.klassInstanceDiffForLanguageInheritanceModel = klassInstanceDiffForLanguageInheritanceModel;
  }
  
  @Override
  public IEvaluateIdentifierAttributesInstanceModel getUpdateIdentifierAttributeInstanceModel()
  {
    return updateIdentifierAttributeInstanceModel;
  }
  
  @Override
  @JsonDeserialize(as = EvaluateIdentifierAttributesInstanceModel.class)
  public void setUpdateIdentifierAttributeInstanceModel(
      IEvaluateIdentifierAttributesInstanceModel updateIdentifierAttributeInstanceModel)
  {
    this.updateIdentifierAttributeInstanceModel = updateIdentifierAttributeInstanceModel;
  }
}
