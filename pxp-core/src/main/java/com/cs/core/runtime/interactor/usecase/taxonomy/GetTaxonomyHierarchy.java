package com.cs.core.runtime.interactor.usecase.taxonomy;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.attributiontaxonomy.ITaxonomyInformationModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.TaxonomyInformationModel;
import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.cs.core.config.interactor.usecase.assembler.SearchAssembler;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.runtime.interactor.model.datarule.ICategoryInformationModel;
import com.cs.core.runtime.interactor.model.filter.IPropertyInstanceFilterModel;
import com.cs.core.runtime.interactor.model.searchable.InstanceSearchModel;
import com.cs.core.runtime.interactor.model.taxonomy.IGetTaxonomyTreeModel;
import com.cs.core.runtime.interactor.util.getall.GetAllUtils;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;

@Service
public class GetTaxonomyHierarchy extends AbstractGetTaxonomyHierarchy<IGetTaxonomyTreeModel, ICategoryInformationModel> 
  implements IGetTaxonomyHierarchy {
  
  @Autowired
  SearchAssembler searchAssembler;
  
  @Autowired
  GetAllUtils     getALLUtils;
  
  @Override
  public ICategoryInformationModel executeInternal(IGetTaxonomyTreeModel dataModel) throws Exception
  {
    return super.executeInternal(dataModel);
  }
  
  @Override
  protected ITaxonomyInformationModel getTaxonomyContentCount(IGetTaxonomyTreeModel dataModel) throws Exception
  {
    Boolean isKlassTaxonomy = dataModel.getIsKlassTaxonomy();
    ClassifierType classifierType = isKlassTaxonomy ? ClassifierType.CLASS : ClassifierType.TAXONOMY;
    List<IConfigEntityTreeInformationModel> categoryInfo = dataModel.getCategoryInfo();
    List<String> classifierIds = new ArrayList<>();
    getALLUtils.getFlatIdsList(categoryInfo, classifierIds);
    Boolean allowChildren = false;
    String generateSearchExpression = generateSearchExpression((InstanceSearchModel) dataModel);

    List<ICategoryInformationModel> classifierInfo = null;//getALLUtils.fillClassifierCountInfo(allowChildren, generateSearchExpression, classifierIds, classifierType, categoryInfo);
    return new TaxonomyInformationModel(classifierInfo.get(0));
  }
  
  public String generateSearchExpression(InstanceSearchModel dataModel)
  {
    String allSearch = dataModel.getAllSearch();
    String moduleId = dataModel.getModuleId();
    StringBuilder searchExpression = searchAssembler.getBaseQuery(searchAssembler.getBaseTypeByModule(moduleId));
    List<? extends IPropertyInstanceFilterModel> attributes = dataModel.getAttributes();
    List<? extends IPropertyInstanceFilterModel> tags = dataModel.getTags();
    String evaluationExpression = searchAssembler.getEvaluationExpression(attributes, tags, allSearch);
    if (!evaluationExpression.isEmpty()) {
      searchExpression.append(" where ").append(evaluationExpression);
    }
    return searchExpression.toString();
  }
  
  
}
