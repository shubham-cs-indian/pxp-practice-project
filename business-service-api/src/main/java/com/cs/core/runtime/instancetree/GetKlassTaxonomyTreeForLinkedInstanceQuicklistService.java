package com.cs.core.runtime.instancetree;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cs.core.runtime.interactor.model.configdetails.instancetree.ConfigDetailsForGetKlassTaxonomyTreeRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsForGetKlassTaxonomyTreeRequestModel;
import com.cs.core.runtime.interactor.model.configdetails.instancetree.IConfigDetailsKlassTaxonomyTreeResponseModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetKlassTaxonomyTreeForLIQRequestModel;
import com.cs.core.runtime.interactor.model.instancetree.IGetKlassTaxonomyTreeResponseModel;

@Service
public class GetKlassTaxonomyTreeForLinkedInstanceQuicklistService extends
    AbstractKlassTaxonomyTree<IGetKlassTaxonomyTreeForLIQRequestModel, IGetKlassTaxonomyTreeResponseModel>
    implements IGetKlassTaxonomyTreeForLinkedInstanceQuicklistService {
  
  @Override
  protected List<String> getModuleEntities(IGetKlassTaxonomyTreeForLIQRequestModel dataModel)
      throws Exception
  {
    return dataModel.getModuleEntities();
  }
  
  @Override
  protected ConfigDetailsForGetKlassTaxonomyTreeRequestModel getConfigDetailsRequestModel()
  {
    return new ConfigDetailsForGetKlassTaxonomyTreeRequestModel();
  }
  
  @Override
  protected IConfigDetailsKlassTaxonomyTreeResponseModel executeConfigDetailsStrategy(
      IConfigDetailsForGetKlassTaxonomyTreeRequestModel configRequsetModel) throws Exception
  {
    return getConfigDetailForKlassTaxonomyTreeStrategy.execute(configRequsetModel);
  }
  
  @Override
  protected IGetKlassTaxonomyTreeResponseModel executeRuntimeStrategy(
      IGetKlassTaxonomyTreeForLIQRequestModel dataModel,
      IConfigDetailsKlassTaxonomyTreeResponseModel configData) throws Exception
  {
    /* String selectionType = dataModel.getSelectedTypes().get(0);
        IGetKlassTaxonomyTreeResponseModel returnModel = new GetKlassTaxonomyTreeResponseModel();
        if (dataModel.getClickedTaxonomyId() == null) {
          List<ICategoryInformationModel> klassTaxonomyInfo = configData.getListBySelectionType(selectionType);
          if (!klassTaxonomyInfo.isEmpty()) {
            returnModel.getListBySelectionType(selectionType).add(getKlassContentCount(dataModel, klassTaxonomyInfo.get(0), false));
            if (klassTaxonomyInfo.size() > 1) {
              List<ICategoryInformationModel> subList = klassTaxonomyInfo.subList(1, klassTaxonomyInfo.size());
              returnModel.getListBySelectionType(selectionType).addAll(
                  getTaxonomyParentList(subList, generateSearchExpressionForTaxonomy(dataModel)));
            }
          }
        }
        else {
          List<String> rootTaxonomyIds = new ArrayList<>();
          getAllUtils.getFlatIdsList(dataModel.getKlassTaxonomyInfo(), rootTaxonomyIds);
          if (rootTaxonomyIds.isEmpty()) {
            return returnModel;
          }
          dataModel.setModuleId(InstanceTreeUtils.getModuleIdByEntityId(dataModel.getModuleEntities()
              .get(0)));
          returnModel.getListBySelectionType(selectionType)
              .addAll(fillTaxonomyCountInfo(false, generateSearchExpression(dataModel),
                  rootTaxonomyIds, dataModel.getKlassTaxonomyInfo()));
        }
        return returnModel;*/
    return getKlassTaxonomyTreeData(dataModel, configData);
  }
  
  @Override
  protected void additionalInformationForRelationshipFilter(
      IGetKlassTaxonomyTreeForLIQRequestModel model,
      IConfigDetailsKlassTaxonomyTreeResponseModel configData)
  {
      model.setModuleId(InstanceTreeUtils.getModuleIdByEntityId(model.getModuleEntities().get(0)));
  }
}
