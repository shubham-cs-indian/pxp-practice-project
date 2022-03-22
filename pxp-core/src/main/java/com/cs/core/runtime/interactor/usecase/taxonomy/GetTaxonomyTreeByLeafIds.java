package com.cs.core.runtime.interactor.usecase.taxonomy;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.config.interactor.model.configdetails.IConfigEntityTreeInformationModel;
import com.cs.core.config.interactor.usecase.assembler.SearchAssembler;
import com.cs.core.data.Text;
import com.cs.core.rdbms.config.idto.IClassifierDTO.ClassifierType;
import com.cs.core.runtime.interactor.model.datarule.ICategoryInformationModel;
import com.cs.core.runtime.interactor.model.filter.IPropertyInstanceFilterModel;
import com.cs.core.runtime.interactor.model.searchable.InstanceSearchModel;
import com.cs.core.runtime.interactor.model.taxonomy.IGetTaxonomyTreeModel;
import com.cs.core.runtime.interactor.util.getall.GetAllUtils;

@Service
public class GetTaxonomyTreeByLeafIds extends AbstractGetTaxonomyTreeByLeafIds<IGetTaxonomyTreeModel, ICategoryInformationModel>
    implements IGetTaxonomyTreeByLeafIds {
  
  @Autowired
  SearchAssembler searchAssembler;
  
  @Autowired
  GetAllUtils getAllUtils;
  
  @Override
  public ICategoryInformationModel executeInternal(IGetTaxonomyTreeModel model) throws Exception
  {
    return super.executeInternal(model);
  }
  
  @Override
  protected ICategoryInformationModel getTaxonomyContentCount(IGetTaxonomyTreeModel dataModel) throws Exception
  {
    Boolean isKlassTaxonomy = dataModel.getIsKlassTaxonomy();
    ClassifierType classifierType = isKlassTaxonomy ? ClassifierType.CLASS : ClassifierType.TAXONOMY;
    List<IConfigEntityTreeInformationModel> categoryInfo = dataModel.getCategoryInfo();
    List<String> classifierIds = new ArrayList<>();
    getAllUtils.getFlatIdsList(categoryInfo, classifierIds);
    Boolean allowChildren = false;
    String generateSearchExpression = generateSearchExpression((InstanceSearchModel) dataModel);

    List<ICategoryInformationModel> classifierInfo = null;//getAllUtils.fillClassifierCountInfo(allowChildren, generateSearchExpression, classifierIds, classifierType, categoryInfo);
    return classifierInfo.get(0);
  }
  
  public String generateSearchExpression(InstanceSearchModel dataModel)
  {
    String allSearch = dataModel.getAllSearch();
    String moduleId = dataModel.getModuleId();
    StringBuilder searchExpression = searchAssembler.getScope(dataModel.getSelectedTypes(), dataModel.getSelectedTaxonomyIds(), searchAssembler.getBaseTypeByModule(moduleId));
    
    List<? extends IPropertyInstanceFilterModel> attributes = dataModel.getAttributes();
    List<? extends IPropertyInstanceFilterModel> tags = dataModel.getTags();
    String evaluationExpression = searchAssembler.getEvaluationExpression(attributes, tags, allSearch);
    if(!allSearch.isEmpty()) {
      String join = String.format(" [P>search] contains %s ", Text.escapeStringWithQuotes(allSearch));
      String searchEpxression = String.format("(%s)", join);
      
      if(!join.isEmpty() && !evaluationExpression.isEmpty()) {
        evaluationExpression = evaluationExpression + " and "+ searchEpxression;
      }
      else if(!join.isEmpty()) {
        evaluationExpression =  searchEpxression;
      }
    }
    if (!evaluationExpression.isEmpty()) {
      searchExpression.append(" where ").append(evaluationExpression);
    }
    return searchExpression.toString();
  }
}
