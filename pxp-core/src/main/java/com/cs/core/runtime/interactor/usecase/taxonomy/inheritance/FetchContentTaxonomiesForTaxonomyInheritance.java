package com.cs.core.runtime.interactor.usecase.taxonomy.inheritance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.interactor.model.configdetails.GetContentVsTypesTaxonomyResponseModel;
import com.cs.core.config.interactor.model.configdetails.GetInheritenceTaxonomyIdsResponseModel;
import com.cs.core.config.interactor.model.configdetails.IGetContentVsTypesTaxonomyResponseModel;
import com.cs.core.config.interactor.model.configdetails.IGetInheritanceTaxonomyIdsResponseModel;
import com.cs.core.config.interactor.model.taxonomy.IReferencedTaxonomyParentModel;
import com.cs.core.rdbms.config.idto.IClassifierDTO;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.runtime.interactor.entity.datarule.ITaxonomyInheritanceRequestModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.model.klassinstance.ITypesTaxonomiesModel;
import com.cs.core.runtime.interactor.model.klassinstance.TypesTaxonomiesModel;
import com.cs.core.runtime.interactor.usecase.klass.IGetConfigDetailForTaxonomyInheritanceStrategy;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;

@Component
public class FetchContentTaxonomiesForTaxonomyInheritance extends AbstractRuntimeService<ITaxonomyInheritanceRequestModel, IGetInheritanceTaxonomyIdsResponseModel>
    implements IFetchContentTaxonomiesForTaxonomyInheritance {
  
  @Autowired
  protected IGetConfigDetailForTaxonomyInheritanceStrategy    getConfigDetailForTaxonomyInheritanceStrategy;
  
  @Autowired
  protected RDBMSComponentUtils                              rdbmsComponentUtils;
  
  @Override
  public IGetInheritanceTaxonomyIdsResponseModel executeInternal(
      ITaxonomyInheritanceRequestModel dataModel) throws Exception
  {
    IGetInheritanceTaxonomyIdsResponseModel getInheritanceTaxonomyIdsResponseModel = new GetInheritenceTaxonomyIdsResponseModel();
    IGetContentVsTypesTaxonomyResponseModel contentVSTaxonomy = new GetContentVsTypesTaxonomyResponseModel();
    Map<String, ITypesTaxonomiesModel> contentIdVsTypesTaxonomies = contentVSTaxonomy.getContentIdVsTypesTaxonomies();
    
    IIdsListParameterModel idsModel = new IdsListParameterModel();
    List<String> allTaxonomyIds = new ArrayList<>();
    String sourceContentId = dataModel.getSourceContentId();
    fillTypesAndTaxonomies(sourceContentId, contentIdVsTypesTaxonomies, allTaxonomyIds);
    
    String contentId = dataModel.getContentId();
    fillTypesAndTaxonomies(contentId, contentIdVsTypesTaxonomies, allTaxonomyIds);
    
    idsModel.getIds().addAll(allTaxonomyIds);
    
    Map<String, IReferencedTaxonomyParentModel> referencedTaxonomies = getConfigDetailForTaxonomyInheritanceStrategy.execute(idsModel)
        .getReferencedTaxonomies();
    getInheritanceTaxonomyIdsResponseModel.setReferencedTaxonomies(referencedTaxonomies);
    
    for (Entry<String, ITypesTaxonomiesModel> taxonomiesModel : contentIdVsTypesTaxonomies.entrySet()) {
      taxonomiesModel.getValue().getTaxonomyIds().retainAll(referencedTaxonomies.keySet());
    }
    getInheritanceTaxonomyIdsResponseModel.setParentContentId(sourceContentId);
    getInheritanceTaxonomyIdsResponseModel.setContentIdVsTypesTaxonomies(contentIdVsTypesTaxonomies);
    return getInheritanceTaxonomyIdsResponseModel;
  }

  private void fillTypesAndTaxonomies(String contentId,  Map<String, ITypesTaxonomiesModel> contentIdVsTypesTaxonomies, List<String> allTaxonomyIIDs) throws NumberFormatException, Exception
  {
    IBaseEntityDTO baseEntityDTO = rdbmsComponentUtils.getBaseEntityDTO(Long.parseLong(contentId));

    ITypesTaxonomiesModel typesTaxonomyModel = new TypesTaxonomiesModel();
    
    List<String> taxonomyIds = typesTaxonomyModel.getTaxonomyIds();
    Set<String> types = (Set<String>) typesTaxonomyModel.getTypes();
    
    types.add(baseEntityDTO.getNatureClassifier().getClassifierCode());
   for(IClassifierDTO classifier : baseEntityDTO.getOtherClassifiers()) {
     ClassifierType classifierType = classifier.getClassifierType();
     String classifierCode = classifier.getClassifierCode();
    if(classifierType.equals(ClassifierType.CLASS)) {
       types.add(classifierCode);
     } else {
       taxonomyIds.add(classifierCode);
       allTaxonomyIIDs.add(classifierCode);
     }
    }
   contentIdVsTypesTaxonomies.put(contentId, typesTaxonomyModel);
  }
}

