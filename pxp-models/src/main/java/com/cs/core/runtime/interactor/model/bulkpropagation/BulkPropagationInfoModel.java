package com.cs.core.runtime.interactor.model.bulkpropagation;

import com.cs.core.config.interactor.model.language.IKlassInstanceDiffForLanguageInheritanceModel;
import com.cs.core.config.interactor.model.language.KlassInstanceDiffForLanguageInheritanceModel;
import com.cs.core.runtime.interactor.model.datarule.ApplyEffectModel;
import com.cs.core.runtime.interactor.model.datarule.IApplyEffectModel;
import com.cs.core.runtime.interactor.model.propagation.IPropertyInstanceUniquenessEvaluationForPropagationModel;
import com.cs.core.runtime.interactor.model.propagation.PropertyInstanceUniquenessEvaluationForPropagationModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipDataTransferInputModel;
import com.cs.core.runtime.interactor.model.relationship.RelationshipDataTransferInputModel;
import com.cs.core.runtime.interactor.model.transfer.DataTransferInputModel;
import com.cs.core.runtime.interactor.model.transfer.IDataTransferInputModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.List;

public class BulkPropagationInfoModel implements IBulkPropagationInfoModel {
  
  private static final long                                                serialVersionUID = 1L;
  protected IApplyEffectModel                                              applyEffect;
  protected List<IPropertyInstanceUniquenessEvaluationForPropagationModel> dataForUniquenessStatEvaluation;
  protected IDataTransferInputModel                                        dataForDataTransfer;
  protected IRelationshipDataTransferInputModel                            dataForRelationshipDataTransfer;
  protected IKlassInstanceDiffForLanguageInheritanceModel                  klassInstanceDiffForLanguageInheritance;
  protected List<String>                                                   instancesToUpdateIsMerged;
  
  @Override
  public IApplyEffectModel getApplyEffect()
  {
    return applyEffect;
  }
  
  @Override
  @JsonDeserialize(as = ApplyEffectModel.class)
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
  public IRelationshipDataTransferInputModel getDataForRelationshipDataTransfer()
  {
    return dataForRelationshipDataTransfer;
  }
  
  @Override
  @JsonDeserialize(as = RelationshipDataTransferInputModel.class)
  public void setDataForRelationshipDataTransfer(
      IRelationshipDataTransferInputModel dataForRelationshipDataTransfer)
  {
    this.dataForRelationshipDataTransfer = dataForRelationshipDataTransfer;
  }
  
  @Override
  public IKlassInstanceDiffForLanguageInheritanceModel getKlassInstanceDiffForLanguageInheritance()
  {
    return klassInstanceDiffForLanguageInheritance;
  }
  
  @JsonDeserialize(as = KlassInstanceDiffForLanguageInheritanceModel.class)
  @Override
  public void setKlassInstanceDiffForLanguageInheritance(
      IKlassInstanceDiffForLanguageInheritanceModel klassInstanceDiffForlanguageInheritance)
  {
    this.klassInstanceDiffForLanguageInheritance = klassInstanceDiffForlanguageInheritance;
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
}
