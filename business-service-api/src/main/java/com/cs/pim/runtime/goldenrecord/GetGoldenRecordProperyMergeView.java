package com.cs.pim.runtime.goldenrecord;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.goldenrecord.IConfigDetailsForGetKlassInstancesToMergeModel;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.instancetree.AbstractGetGoldenRecordMergeView;
import com.cs.core.runtime.interactor.model.goldenrecord.IGoldenRecordRuleKlassInstancesMergeViewRequestModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IGoldenRecordRuleKlassInstancesMergeViewResponseModel;

@Component("getGoldenRecordProperyMergeView")
public class GetGoldenRecordProperyMergeView extends
    AbstractGetGoldenRecordMergeView<IGoldenRecordRuleKlassInstancesMergeViewRequestModel, IGoldenRecordRuleKlassInstancesMergeViewResponseModel>
    implements IGetGoldenRecordMergeView {
  
  @Autowired
  GetRecommendedPropertyDataForMergeView getRecommendedPropertyDataForMergeView;
  
  @Override
   public void getRecommendedPropertyDataForMergeView(IGoldenRecordRuleKlassInstancesMergeViewRequestModel dataModel,
      IConfigDetailsForGetKlassInstancesToMergeModel configDetails, IGoldenRecordRuleKlassInstancesMergeViewResponseModel responseModel,
      List<IBaseEntityDTO> baseEntitiesDTOs, ILocaleCatalogDAO localeCatlogDAO) throws Exception
  {
    try {
      getRecommendedPropertyDataForMergeView.execute(dataModel, configDetails, responseModel, baseEntitiesDTOs, localeCatlogDAO);
    }
    catch (Throwable e) {
      e.printStackTrace();
    }
    
  }

}
