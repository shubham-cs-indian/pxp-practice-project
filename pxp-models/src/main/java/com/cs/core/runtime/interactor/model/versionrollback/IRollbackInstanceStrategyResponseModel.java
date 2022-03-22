package com.cs.core.runtime.interactor.model.versionrollback;

import com.cs.core.config.interactor.model.configdetails.IValueInheritancePropagationModel;
import com.cs.core.config.interactor.model.klass.ITypeInfoWithContentIdentifiersModel;
import com.cs.core.config.interactor.model.relationship.IRestoreRelationshipInstancesRequestModel;
import com.cs.core.runtime.interactor.entity.propertyinstance.IContentRelationshipInstance;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.datarule.IApplyEffectModel;
import com.cs.core.runtime.interactor.model.klassinstance.ISaveKlassInstancePluginModel;
import com.cs.core.runtime.interactor.model.propagation.IPropertyInstanceUniquenessEvaluationForPropagationModel;
import com.cs.core.runtime.interactor.model.searchable.IUpdateSearchableInstanceModel;
import com.cs.core.runtime.interactor.model.transfer.IDataTransferInputModel;
import com.cs.core.runtime.interactor.model.variants.IPropagableContextualDataModel;

import java.util.List;
import java.util.Map;

public interface IRollbackInstanceStrategyResponseModel extends IModel {
  
  public static final String VARIANT_IDS_TO_RESTORE              = "variantIdsToRestore";
  public static final String VARIANT_IDS_TO_DELETE               = "variantIdsToDelete";
  public static final String CONTENT_INFO                        = "contentInfo";
  public static final String INHERITANCE_DATA                    = "inheritanceData";
  public static final String APPLY_EFFECT                        = "applyEffect";
  public static final String DATA_FOR_UNIQUENESS_STAT_EVALUATION = "dataForUniquenessStatEvaluation";
  public static final String SAVE_KLASS_INSTANCE_PLUGIN_MODEL    = "saveKlassInstancePluginModel";
  public static final String DELETED_RELATIONSHIPS_ELEMENTS      = "deletedRelationshipsElements";
  public static final String PROPAGABLE_CONTEXTUAL_DATA          = "propagableContextualData";
  public static final String UPDATE_SEARCHABLE_DOCUMENT_DATA     = "updateSearchableDocumentData";
  public static final String RESTORE_RELATIONSHIP_INSTANCES_DATA = "restoreRelationshipInstancesData";
  public static final String DATA_FOR_DATA_TRANSFER              = "dataForDataTransfer";
  public static final String DELETED_LANGUAGE_CODES              = "deletedLanguageCodes";
  
  public List<String> getVariantIdsToRestore();
  
  public void setVariantIdsToRestore(List<String> variantIdsToRestore);
  
  public List<String> getVariantIdsToDelete();
  
  public void setVariantIdsToDelete(List<String> variantIdsToDelete);
  
  public IValueInheritancePropagationModel getInheritanceData();
  
  public void setInheritanceData(IValueInheritancePropagationModel inheritanceData);
  
  public IApplyEffectModel getApplyEffect();
  
  public void setApplyEffect(IApplyEffectModel applyEffect);
  
  public List<IPropertyInstanceUniquenessEvaluationForPropagationModel> getDataForUniquenessStatEvaluation();
  
  public void setDataForUniquenessStatEvaluation(
      List<IPropertyInstanceUniquenessEvaluationForPropagationModel> dataForUniquenessStatEvaluation);
  
  public ITypeInfoWithContentIdentifiersModel getContentInfo();
  
  public void setContentInfo(ITypeInfoWithContentIdentifiersModel contentInfo);
  
  public ISaveKlassInstancePluginModel getSaveKlassInstancePluginModel();
  
  public void setSaveKlassInstancePluginModel(
      ISaveKlassInstancePluginModel saveKlassInstancePluginModel);
  
  public List<IContentRelationshipInstance> getDeletedRelationshipsElements();
  
  public void setDeletedRelationshipsElements(
      List<IContentRelationshipInstance> deletedRelationshipsElements);
  
  // key:contextId
  public Map<String, IPropagableContextualDataModel> getPropagableContextualData();
  
  public void setPropagableContextualData(
      Map<String, IPropagableContextualDataModel> propagableContextualData);
  
  public IUpdateSearchableInstanceModel getUpdateSearchableDocumentData();
  
  public void setUpdateSearchableDocumentData(
      IUpdateSearchableInstanceModel updateSearchableDocumentData);
  
  public IRestoreRelationshipInstancesRequestModel getRestoreRelationshipInstancesData();
  
  public void setRestoreRelationshipInstancesData(
      IRestoreRelationshipInstancesRequestModel restoreRelationshipInstancesData);
  
  public IDataTransferInputModel getDataForDataTransfer();
  
  public void setDataForDataTransfer(IDataTransferInputModel dataForDataTransfer);
  
  public List<String> getDeletedLanguageCodes();
  
  public void setDeletedLanguageCodes(List<String> deletedLanguageCodes);
}
