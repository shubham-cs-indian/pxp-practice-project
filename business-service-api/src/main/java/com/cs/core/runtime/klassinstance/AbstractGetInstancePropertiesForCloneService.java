package com.cs.core.runtime.klassinstance;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.model.GetCloneWizardForRequestStrategyModel;
import com.cs.core.config.interactor.model.IGetCloneWizardForRequestStrategyModel;
import com.cs.core.config.interactor.model.configdetails.IGetCloneWizardRequestModel;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.runtime.interactor.model.clone.IGetKlassInstancePropertiesForCloneModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.core.runtime.strategy.usecase.configdetails.IGetKlassInstancePropertiesForCloneStrategy;
import com.cs.utils.BaseEntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public abstract class AbstractGetInstancePropertiesForCloneService<P extends IGetCloneWizardRequestModel, R extends IGetKlassInstancePropertiesForCloneModel>
    extends AbstractRuntimeService<P,R> {
  
  @Autowired
  protected IGetKlassInstancePropertiesForCloneStrategy getKlassInstancePropertiesForCloneStrategy;
  
  @Autowired
  RDBMSComponentUtils                                   rdbmsComponentUtils;
  
  @SuppressWarnings("unchecked")
  @Override
  protected R executeInternal(P dataModel) throws Exception
  {

    IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(Long.parseLong(dataModel.getId()));
    List<String> klassIds = BaseEntityUtils.getAllReferenceTypeFromBaseEntity(baseEntityDAO);
    List<String> selectedTaxonomyIds = BaseEntityUtils
        .getReferenceTaxonomyIdsFromBaseEntity(baseEntityDAO.getClassifiers());
    
    
    IGetCloneWizardForRequestStrategyModel model = new GetCloneWizardForRequestStrategyModel();
    model.setTypesIdsToClone(dataModel.getSelectedTypesIds());
    model.setTaxonomyIdsToClone(dataModel.getSelectedTaxonomyIds());
    model.setTypesIds(klassIds);
    model.setTaxonomyIds(selectedTaxonomyIds);
    model.setParentNatureKlassId(dataModel.getParentNatureKlassId());
    model.setIsForLinkedVariant(dataModel.getIsForLinkedVariant());
    
    return (R) getKlassInstancePropertiesForCloneStrategy.execute(model);
  }
}