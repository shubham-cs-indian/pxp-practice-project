package com.cs.core.runtime.goldenrecord.bucket;

import java.util.List;

import com.cs.core.config.interactor.model.goldenrecord.IConfigDetailsForGetKlassInstancesToMergeModel;
import com.cs.core.rdbms.entity.idto.IBaseEntityDTO;
import com.cs.core.rdbms.process.idao.ILocaleCatalogDAO;
import com.cs.core.runtime.businessapi.base.IRuntimeService;
import com.cs.core.runtime.interactor.model.goldenrecord.IGoldenRecordRuleKlassInstancesMergeViewRequestModel;
import com.cs.core.runtime.interactor.model.goldenrecord.IGoldenRecordRuleKlassInstancesMergeViewResponseModel;

public interface IGetGoldenRecordMergeView
    extends IRuntimeService<IGoldenRecordRuleKlassInstancesMergeViewRequestModel, IGoldenRecordRuleKlassInstancesMergeViewResponseModel> {
    
     void getRecommendedPropertyDataForMergeView(IGoldenRecordRuleKlassInstancesMergeViewRequestModel dataModel,
      IConfigDetailsForGetKlassInstancesToMergeModel configDetails, IGoldenRecordRuleKlassInstancesMergeViewResponseModel responseModel,
      List<IBaseEntityDTO> baseEntitiesDTOs, ILocaleCatalogDAO localeCatlogDAO) throws Exception;
}
