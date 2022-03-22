package com.cs.core.runtime.bgp;

import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.businessapi.base.AbstractRuntimeService;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class GetAllBGPServicesService extends AbstractRuntimeService<IModel, IIdsListParameterModel>
    implements IGetAllBGPServicesService {

  @Override
  public IIdsListParameterModel executeInternal(IModel dataModel) throws Exception

  {
    List<String> bgpServices = BGPDriverDAO.instance().getServices();
    Collections.sort(bgpServices);
    IIdsListParameterModel response = new IdsListParameterModel();
    response.setIds(bgpServices);
    
    return response;
  }
  
}
