package com.cs.bds.config.usecase.taxonomy;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.config.standard.IConfigMap;
import com.cs.config.standard.IStandardConfig;
import com.cs.constants.CommonConstants;
import com.cs.core.config.interactor.model.attributiontaxonomy.IAddedTagModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.IAddedTaxonomyLevelModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.IGetMasterTaxonomyWithoutKPStrategyResponseModel;
import com.cs.core.config.interactor.model.attributiontaxonomy.ISaveMasterTaxonomyModel;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.klass.GetChildMajorTaxonomiesRequestModel;
import com.cs.core.config.interactor.model.klass.IGetChildMajorTaxonomiesRequestModel;
import com.cs.core.config.strategy.usecase.attributiontaxonomy.ISaveMasterTaxonomyStrategy;
import com.cs.core.config.strategy.usecase.taxonomy.IGetAllChildTaxonomiesByParentIdStrategy;
import com.cs.core.rdbms.config.idto.IPropertyDTO;
import com.cs.core.runtime.interactor.model.configuration.IIdLabelCodeModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;

@Component
public class SaveTaxonomy implements ISaveTaxonomy {
  
  @Autowired
  ISaveMasterTaxonomyStrategy              saveMasterTaxonomyStrategy;
  
  @Autowired
  IGetAllChildTaxonomiesByParentIdStrategy getAllChildTaxonomiesByParentIdStrategy;
  
  public IGetMasterTaxonomyWithoutKPStrategyResponseModel execute(ISaveMasterTaxonomyModel saveMasterTaxonomyModel) throws Exception
  {
    IGetMasterTaxonomyWithoutKPStrategyResponseModel response = null;
    
    if (saveMasterTaxonomyModel.getIsBackgroundSaveTaxonomy()) {
      IGetChildMajorTaxonomiesRequestModel requestModel = new GetChildMajorTaxonomiesRequestModel();
      requestModel.setTaxonomyId(saveMasterTaxonomyModel.getId());
      
      IListModel<IIdLabelCodeModel> allChildTaxonomiesResponse = getAllChildTaxonomiesByParentIdStrategy.execute(requestModel);
      List<IIdLabelCodeModel> allChildTaxonomiesList = (List<IIdLabelCodeModel>) allChildTaxonomiesResponse.getList();
      
      for (IIdLabelCodeModel childTaxonomy : allChildTaxonomiesList) {
        saveMasterTaxonomyModel.setId(childTaxonomy.getId());
        saveMasterTaxonomyModel.setCode(childTaxonomy.getCode());
        saveMasterTaxonomyModel.setLabel(childTaxonomy.getLabel());
        
        saveMasterTaxonomyStrategy.execute(saveMasterTaxonomyModel);
      }
    }
    else {
      IAddedTaxonomyLevelModel addedLevel = saveMasterTaxonomyModel.getAddedLevel();
      if (addedLevel != null && addedLevel.getAddedTag().getIsNewlyCreated()) {
        IAddedTagModel addedTag = addedLevel.getAddedTag();
        String code = addedTag.getCode();
        if (StringUtils.isEmpty(code)) {
          code = RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.TAG.getPrefix());
        }
        
        addedTag.setCode(code);
        addedTag.setId(code);
        IPropertyDTO property = RDBMSUtils.newConfigurationDAO()
            .createProperty(code, IConfigMap.getPropertyType(CommonConstants.MASTER_TAG_TYPE_ID));
        addedTag.setPropertyIID(property.getPropertyIID());
      }
      response = saveMasterTaxonomyStrategy.execute(saveMasterTaxonomyModel);
    }
    return response;
  }
  
}
