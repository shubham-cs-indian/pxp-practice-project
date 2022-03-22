package com.cs.core.runtime.interactor.usecase.workflow.processinstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.bgprocess.dao.BGPDriverDAO;
import com.cs.core.bgprocess.dto.BGProcessDTO;
import com.cs.core.bgprocess.idto.IBGProcessDTO;
import com.cs.core.config.interactor.entity.processevent.IProcessEvent;
import com.cs.core.config.strategy.usecase.task.IGetProcessDefinitionByIdStrategy;
import com.cs.core.runtime.interactor.model.camunda.IGetCamundaProcessDefinitionResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.model.process.BGPProcessModel;
import com.cs.core.runtime.interactor.model.process.IBGPProcessModel;

@Component
@SuppressWarnings("unchecked")
public class GetBGPProcessInstanceService extends AbstractGetProcessInstanceService<IIdParameterModel, IBGPProcessModel>
    implements IGetBGPProcessInstanceService {
  
  /* 
  @Autowired
  IGetProcessInstanceStrategy getAllProcessInstancesStrategy;
  */
  
  @Autowired
  protected IGetProcessDefinitionByIdStrategy getProcessDefinitionByIdStrategy;
  
  @Override
  protected IBGPProcessModel executeInternal(IIdParameterModel model) throws Exception
  {
    Long jobId = Long.valueOf(model.getId());
    IBGProcessDTO bgpProcess = BGPDriverDAO.instance().getBGPProcess(jobId);
    
    IBGPProcessModel jobData = new BGPProcessModel();
    jobData.setJobId(bgpProcess.getJobIID());
    jobData.setStatus(bgpProcess.getStatus().toString());
    String service = bgpProcess.getService();
    jobData.setService(service);
    // TODO : move getLog method to IBGProcessDTO
    jobData.setLogData(((BGProcessDTO) bgpProcess).getLog().toString());
    jobData.setProcessDefination(getProcessDefination(service));
    
    return jobData;
  }
  
  /**
   * @param service
   * @return the ProcessInstance Defination from config using service
   * @throws Exception
   */
  @Override
  protected String getProcessDefination(String service) throws Exception
  {
    IIdsListParameterModel model = new IdsListParameterModel();
    List<String> ids = new ArrayList<>();
    ids.add(service);
    model.setIds(ids);
    IGetCamundaProcessDefinitionResponseModel processDefinition = getProcessDefinitionByIdStrategy.execute(model);
    
    Map<String, Object> internalProcessMap = (Map<String, Object>) ((Map<String, Object>) processDefinition.getProcessDefinition())
        .get(service);
    
    String processDefination = "";
    
    // To allow log window for BGP process we send empty process definition.
    if (internalProcessMap != null)
      processDefination = (String) internalProcessMap.get(IProcessEvent.PROCESS_DEFINITION);
    
    return processDefination;
  }
  
}
