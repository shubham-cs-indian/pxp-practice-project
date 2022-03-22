package com.cs.core.config.strategy.usecase.textasset;

import com.cs.core.config.interactor.entity.textasset.ITextAsset;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.textasset.ITextAssetModel;
import com.cs.core.config.interactor.model.textasset.TextAssetModel;
import com.cs.core.config.strategy.configuration.embeddedorientdb.OrientDBBaseStrategy;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class OrientDBGetAllMasterTextAssetStrategy extends OrientDBBaseStrategy
    implements IGetAllMasterTextAssetsStrategy {
  
  public static final String useCase = "GetAllTextAssets";
  
  @Override
  public IListModel<ITextAssetModel> execute(ITextAssetModel model) throws Exception
  {
    List<ITextAssetModel> targetModelsList = new ArrayList<ITextAssetModel>();
    Map<String, Object> requestMap = new HashMap<>();
    ListModel<ITextAsset> targetList = execute(useCase, requestMap,
        new TypeReference<ListModel<ITextAsset>>()
        {
          
        });
    for (ITextAsset textasset : targetList.getList()) {
      targetModelsList.add(new TextAssetModel(textasset));
    }
    IListModel<ITextAssetModel> returnModel = new ListModel<>();
    returnModel.setList(targetModelsList);
    
    return returnModel;
  }
}
