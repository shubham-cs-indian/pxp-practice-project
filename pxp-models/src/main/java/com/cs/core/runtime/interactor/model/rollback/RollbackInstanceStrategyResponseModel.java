package com.cs.core.runtime.interactor.model.rollback;

import com.cs.core.config.interactor.model.configdetails.IValueInheritancePropagationModel;
import com.cs.core.config.interactor.model.klass.ITypeInfoWithContentIdentifiersModel;
import com.cs.core.config.interactor.model.klass.TypeInfoWithContentIdentifiersModel;
import com.cs.core.config.interactor.model.relationship.IRestoreRelationshipInstancesRequestModel;
import com.cs.core.config.interactor.model.relationship.RestoreRelationshipInstancesRequestModel;
import com.cs.core.runtime.interactor.entity.datarule.ContentRelationshipInstance;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentRelationshipInstance;
import com.cs.core.runtime.interactor.model.bulkpropagation.ValueInheritancePropagationModel;
import com.cs.core.runtime.interactor.model.datarule.ApplyEffectModel;
import com.cs.core.runtime.interactor.model.datarule.IApplyEffectModel;
import com.cs.core.runtime.interactor.model.klassinstance.ISaveKlassInstancePluginModel;
import com.cs.core.runtime.interactor.model.klassinstance.SaveKlassInstancePluginModel;
import com.cs.core.runtime.interactor.model.propagation.IPropertyInstanceUniquenessEvaluationForPropagationModel;
import com.cs.core.runtime.interactor.model.propagation.PropertyInstanceUniquenessEvaluationForPropagationModel;
import com.cs.core.runtime.interactor.model.searchable.IUpdateSearchableInstanceModel;
import com.cs.core.runtime.interactor.model.searchable.UpdateSearchableInstanceModel;
import com.cs.core.runtime.interactor.model.transfer.DataTransferInputModel;
import com.cs.core.runtime.interactor.model.transfer.IDataTransferInputModel;
import com.cs.core.runtime.interactor.model.variants.IPropagableContextualDataModel;
import com.cs.core.runtime.interactor.model.variants.PropagableContextualDataModel;
import com.cs.core.runtime.interactor.model.versionrollback.IRollbackInstanceStrategyResponseModel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RollbackInstanceStrategyResponseModel
    implements IRollbackInstanceStrategyResponseModel {
  
  private static final long                                                serialVersionUID                = 1L;
  protected IValueInheritancePropagationModel                              inheritanceData;
  protected IApplyEffectModel                                              applyEffect;
  protected List<IPropertyInstanceUniquenessEvaluationForPropagationModel> dataForUniquenessStatEvaluation = new ArrayList<>();
  protected ITypeInfoWithContentIdentifiersModel                           contentInfo;
  protected ISaveKlassInstancePluginModel                                  saveKlassInstancePluginModel;
  protected List<String>                                                   variantIdsToRestore             = new ArrayList<>();
  protected List<String>                                                   variantIdsToDelete              = new ArrayList<>();
  protected List<IContentRelationshipInstance>                             deletedRelationshipsElements    = new ArrayList<>();
  protected Map<String, IPropagableContextualDataModel>                    propagableContextualData;
  protected IUpdateSearchableInstanceModel                                 updateSearchableDocumentData;
  protected IRestoreRelationshipInstancesRequestModel                      restoreRelationshipInstancesData;
  protected IDataTransferInputModel                                        dataForDataTransfer;
  protected List<String>                                                   deletedLanguageCodes;
  
  @Override
  public IValueInheritancePropagationModel getInheritanceData()
  {
    return inheritanceData;
  }
  
  @JsonDeserialize(as = ValueInheritancePropagationModel.class)
  @Override
  public void setInheritanceData(IValueInheritancePropagationModel inheritanceData)
  {
    this.inheritanceData = inheritanceData;
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
  public ITypeInfoWithContentIdentifiersModel getContentInfo()
  {
    return contentInfo;
  }
  
  @JsonDeserialize(as = TypeInfoWithContentIdentifiersModel.class)
  @Override
  public void setContentInfo(ITypeInfoWithContentIdentifiersModel contentInfo)
  {
    this.contentInfo = contentInfo;
  }
  
  @Override
  public List<String> getVariantIdsToRestore()
  {
    return variantIdsToRestore;
  }
  
  @Override
  public void setVariantIdsToRestore(List<String> variantIdsToRestore)
  {
    this.variantIdsToRestore = variantIdsToRestore;
  }
  
  @Override
  public ISaveKlassInstancePluginModel getSaveKlassInstancePluginModel()
  {
    return saveKlassInstancePluginModel;
  }
  
  @JsonDeserialize(as = SaveKlassInstancePluginModel.class)
  @Override
  public void setSaveKlassInstancePluginModel(
      ISaveKlassInstancePluginModel saveKlassInstancePluginModel)
  {
    this.saveKlassInstancePluginModel = saveKlassInstancePluginModel;
  }
  
  @Override
  public List<String> getVariantIdsToDelete()
  {
    return variantIdsToDelete;
  }
  
  @Override
  public void setVariantIdsToDelete(List<String> variantIdsToDelete)
  {
    this.variantIdsToDelete = variantIdsToDelete;
  }
  
  @Override
  public List<IContentRelationshipInstance> getDeletedRelationshipsElements()
  {
    return deletedRelationshipsElements;
  }
  
  @JsonDeserialize(contentAs = ContentRelationshipInstance.class)
  @Override
  public void setDeletedRelationshipsElements(
      List<IContentRelationshipInstance> deletedRelationshipsElements)
  {
    this.deletedRelationshipsElements = deletedRelationshipsElements;
  }
  
  @Override
  public Map<String, IPropagableContextualDataModel> getPropagableContextualData()
  {
    if (propagableContextualData == null) {
      propagableContextualData = new HashMap<>();
    }
    return propagableContextualData;
  }
  
  @JsonDeserialize(contentAs = PropagableContextualDataModel.class)
  @Override
  public void setPropagableContextualData(
      Map<String, IPropagableContextualDataModel> propagableContextualData)
  {
    this.propagableContextualData = propagableContextualData;
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
  public IRestoreRelationshipInstancesRequestModel getRestoreRelationshipInstancesData()
  {
    return restoreRelationshipInstancesData;
  }
  
  @Override
  @JsonDeserialize(as = RestoreRelationshipInstancesRequestModel.class)
  public void setRestoreRelationshipInstancesData(
      IRestoreRelationshipInstancesRequestModel restoreRelationshipInstancesData)
  {
    this.restoreRelationshipInstancesData = restoreRelationshipInstancesData;
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
  public List<String> getDeletedLanguageCodes()
  {
    if (deletedLanguageCodes == null) {
      deletedLanguageCodes = new ArrayList<>();
    }
    return deletedLanguageCodes;
  }
  
  @Override
  public void setDeletedLanguageCodes(List<String> deletedLanguageCodes)
  {
    this.deletedLanguageCodes = deletedLanguageCodes;
  }
}
