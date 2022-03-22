package com.cs.pim.config.klassinstance;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.config.strategy.usecase.duplicatecode.IGetConflictSourcesInformationConfigStrategy;
import com.cs.core.rdbms.entity.idao.IBaseEntityDAO;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IIdAndNameModel;
import com.cs.core.runtime.interactor.model.dynamichierarchy.IdAndNameModel;
import com.cs.core.runtime.interactor.model.klassinstance.IConflictSourcesRequestModel;
import com.cs.core.runtime.interactor.model.klassinstance.IGetConflictSourcesInformationModel;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSComponentUtils;

@Service
public class GetConflictSourcesInformationService extends
    AbstractRuntimeService<IConflictSourcesRequestModel, IGetConflictSourcesInformationModel>
    implements IGetConflictSourcesInformationService {
  
  @Autowired
  protected IGetConflictSourcesInformationConfigStrategy getConflictSourcesInformationConfigStrategy;
  
  @Autowired
  protected RDBMSComponentUtils           rdbmsComponentUtils;
  
  @Override
  public IGetConflictSourcesInformationModel executeInternal(IConflictSourcesRequestModel dataModel)
      throws Exception
  {
    IGetConflictSourcesInformationModel responseModel = getConflictSourcesInformationConfigStrategy
        .execute(dataModel);
    Map<String, IIdAndNameModel> contents = prepareRequestDataToGetconflictSourceInformation(dataModel);
    responseModel.setContents(contents);
    return responseModel;
  }

  private Map<String, IIdAndNameModel> prepareRequestDataToGetconflictSourceInformation(IConflictSourcesRequestModel dataModel)
      throws Exception
  {
    Map<String, IIdAndNameModel> contents = new HashMap<String, IIdAndNameModel>();
    for(String contentId : dataModel.getContents()) {
      IBaseEntityDAO baseEntityDAO = rdbmsComponentUtils.getBaseEntityDAO(Long.parseLong(contentId));
      IIdAndNameModel contentMap = new IdAndNameModel();
      contentMap.setId(contentId);
      contentMap.setName( baseEntityDAO.getBaseEntityDTO().getBaseEntityName());
      contents.put(contentId, contentMap);
    }
    return contents;
  }
}
