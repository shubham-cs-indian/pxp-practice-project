package com.cs.core.runtime.interactor.model.bulkpropagation;

import com.cs.core.config.interactor.model.language.IKlassInstanceDiffForLanguageInheritanceModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.datarule.IApplyEffectModel;
import com.cs.core.runtime.interactor.model.propagation.IPropertyInstanceUniquenessEvaluationForPropagationModel;
import com.cs.core.runtime.interactor.model.relationship.IRelationshipDataTransferInputModel;
import com.cs.core.runtime.interactor.model.transfer.IDataTransferInputModel;

import java.util.List;

public interface IBulkPropagationInfoModel extends IModel {
  
  public static final String APPLY_EFFECT                                 = "applyEffect";
  public static final String DATA_FOR_UNIQUENESS_STAT_EVALUATION          = "dataForUniquenessStatEvaluation";
  public static final String DATA_FOR_DATA_TRANSFER                       = "dataForDataTransfer";
  public static final String DATA_FOR_RELATIONSHIP_DATA_TRANSFER          = "dataForRelationshipDataTransfer";
  public static final String KLASS_INSTANCE_DIFF_FOR_LANGUAGE_INHERITANCE = "klassInstanceDiffForLanguageInheritance";
  public static final String INSTANCES_TO_UPDATE_IS_MERGED                = "instancesToUpdateIsMerged";
  
  public IApplyEffectModel getApplyEffect();
  
  public void setApplyEffect(IApplyEffectModel applyEffect);
  
  public List<IPropertyInstanceUniquenessEvaluationForPropagationModel> getDataForUniquenessStatEvaluation();
  
  public void setDataForUniquenessStatEvaluation(
      List<IPropertyInstanceUniquenessEvaluationForPropagationModel> dataForUniquenessStatEvaluation);
  
  public IDataTransferInputModel getDataForDataTransfer();
  
  public void setDataForDataTransfer(IDataTransferInputModel dataForDataTransfer);
  
  public IRelationshipDataTransferInputModel getDataForRelationshipDataTransfer();
  
  public void setDataForRelationshipDataTransfer(
      IRelationshipDataTransferInputModel dataForRelationshipDataTransfer);
  
  public IKlassInstanceDiffForLanguageInheritanceModel getKlassInstanceDiffForLanguageInheritance();
  
  public void setKlassInstanceDiffForLanguageInheritance(
      IKlassInstanceDiffForLanguageInheritanceModel klassInstanceDiffForlanguageInheritance);
  
  public List<String> getInstancesToUpdateIsMerged();
  
  public void setInstancesToUpdateIsMerged(List<String> instancesToUpdateIsMerged);
}
