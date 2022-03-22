package com.cs.core.config.interactor.usecase.attributiontaxonomy;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.cs.base.interactor.model.audit.IConfigResponseWithAuditLogModel;
import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.model.klass.IDefaultValueChangeModel;
import com.cs.core.runtime.interactor.model.bulkpropagation.ContentDiffModelToPrepareDataForBulkPropagation;
import com.cs.core.runtime.interactor.model.bulkpropagation.IContentDiffModelToPrepareDataForBulkPropagation;
import com.cs.core.runtime.interactor.model.bulkpropagation.IPropertyDiffModelForBulkPropagation;
import com.cs.core.runtime.interactor.model.bulkpropagation.PropertyDiffModelForBulkPropagation;
import com.cs.core.runtime.interactor.model.configuration.IModel;

@Component
public abstract class AbstractSaveTagTaxonomy<P extends IModel, R extends IConfigResponseWithAuditLogModel>
    extends AbstractSaveConfigInteractor<P, R> {
  
  protected abstract R executeSaveArticleTaxonomy(P model) throws Exception;
  
  @Override
  protected R executeInternal(P model) throws Exception
  {
    return (R) executeSaveArticleTaxonomy(model);
  }
  
  public void manageInheritance(List<IDefaultValueChangeModel> defaultValuesDiff,
      Map<String, List<String>> deletedPropertiesFromSource, Boolean isIdentifierAttributeChanged)
      throws Exception
  {
    IPropertyDiffModelForBulkPropagation propertyDiffModelForBulkPropagation = new PropertyDiffModelForBulkPropagation();
    IContentDiffModelToPrepareDataForBulkPropagation contentDiff = new ContentDiffModelToPrepareDataForBulkPropagation();
    
    Boolean isPropagationNeeded = false;
    if (!defaultValuesDiff.isEmpty()) {
      propertyDiffModelForBulkPropagation.setDefaultValueDiffList(defaultValuesDiff);
      contentDiff.setPropertyDiffModelForBulkPropagation(propertyDiffModelForBulkPropagation);
      isPropagationNeeded = true;
    }
    
    if (!deletedPropertiesFromSource.isEmpty()) {
      contentDiff.setDeletedPropertiesFromSource(deletedPropertiesFromSource);
      isPropagationNeeded = true;
    }
    
    if (isIdentifierAttributeChanged.equals(true)) {
      contentDiff.setIsIdentifierAttributeEvaluationNeeded(isIdentifierAttributeChanged);
      isPropagationNeeded = true;
    }
    
    //in-case propagation needed is true, add event into BGP.
    /*if (isPropagationNeeded) {
    }*/
  }
}
