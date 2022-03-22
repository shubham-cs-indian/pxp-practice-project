package com.cs.core.runtime.klassinstance;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.runtime.interactor.model.klassinstance.IReferencedTypesAndTaxonomiesModel;
import com.cs.core.runtime.interactor.model.klassinstance.ITypesTaxonomiesModel;
import com.cs.core.runtime.interactor.model.klassinstance.TypesTaxonomiesModel;
import com.cs.core.runtime.interactor.model.typeswitch.IAllowedTypesRequestModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailsForTypesAndTaxonomiesOfContentStrategy;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;
import com.cs.utils.BaseEntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetTypesAndTaxonomiesOfContentService extends AbstractRuntimeService<IAllowedTypesRequestModel, IReferencedTypesAndTaxonomiesModel>
    implements IGetTypesAndTaxonomiesOfContentService {
  
  @Autowired
  protected IGetConfigDetailsForTypesAndTaxonomiesOfContentStrategy getConfigDetailsForTypesAndTaxonomiesOfContentStrategy;

  @Autowired
  protected RDBMSComponentUtils                                              rdbmsComponentUtils;
  
  @Override
  public IReferencedTypesAndTaxonomiesModel executeInternal(IAllowedTypesRequestModel model)
      throws Exception
  {
    
    IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(Long.parseLong(model.getId()));
    
    List<String> referenceType = BaseEntityUtils.getAllReferenceTypeFromBaseEntity(baseEntityDAO);
    List<String> referenceTaxonomyIds = BaseEntityUtils.getReferenceTaxonomyIdsFromBaseEntity(baseEntityDAO.getClassifiers());
    
    ITypesTaxonomiesModel typesAndSelectedTaxonomies = new TypesTaxonomiesModel();
    typesAndSelectedTaxonomies.setTaxonomyIds(referenceTaxonomyIds);
    typesAndSelectedTaxonomies.setTypes(referenceType);
    
    List<String> types = new ArrayList<>();
    typesAndSelectedTaxonomies.getTypes().removeAll(types);
    
    IReferencedTypesAndTaxonomiesModel referencedTypesAndTaxonomies = getConfigDetailsForTypesAndTaxonomiesOfContentStrategy
        .execute(typesAndSelectedTaxonomies);
    
    return referencedTypesAndTaxonomies;
  }
  
}