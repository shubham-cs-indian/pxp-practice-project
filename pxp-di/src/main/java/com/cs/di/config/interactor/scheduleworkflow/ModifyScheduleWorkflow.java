package com.cs.di.config.interactor.scheduleworkflow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.interactor.usecase.base.AbstractSaveConfigInteractor;
import com.cs.core.config.interactor.entity.processevent.IProcessEvent;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.processevent.CreateOrSaveProcessEventResponseModel;
import com.cs.core.config.interactor.model.processevent.IBulkSaveProcessEventResponseModel;
import com.cs.core.config.interactor.model.processevent.ICreateOrSaveProcessEventResponseModel;
import com.cs.core.config.strategy.usecase.task.IGetProcessDefinitionByIdStrategy;
import com.cs.core.runtime.interactor.model.camunda.IGetCamundaProcessDefinitionResponseModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.core.runtime.strategy.cammunda.broadcast.IBulkUploadProcessToServerStrategy;
import com.cs.di.config.interactor.model.initializeworflowevent.ISaveProcessEventModel;
import com.cs.di.config.strategy.processevent.ISaveProcessEventStrategy;

/**
 * @author sangram.shelar 
 */
@Service
public class ModifyScheduleWorkflow extends AbstractSaveConfigInteractor<ISaveProcessEventModel, ICreateOrSaveProcessEventResponseModel>
    implements IModifyScheduleWorkflow {
  
  @Autowired
  protected IBulkUploadProcessToServerStrategy bulkUploadProcessesToServerStrategy;
  
  @Autowired
  protected IGetProcessDefinitionByIdStrategy  getProcessDefinitionByIdStrategy;
  
  @Autowired
  protected ISaveProcessEventStrategy          saveProcessEventStrategy;
  
  @SuppressWarnings("unchecked")
  @Override
  protected ICreateOrSaveProcessEventResponseModel executeInternal(ISaveProcessEventModel saveProcessEventModel) throws Exception
  {
    String processEventId = saveProcessEventModel.getId();
    IIdsListParameterModel processIds = new IdsListParameterModel(new ArrayList<String>(List.of(processEventId)));
    // Get processEvent information by id
    IGetCamundaProcessDefinitionResponseModel responseModel = getProcessDefinitionByIdStrategy.execute(processIds);
    Map<String, String> processEventInfo = (Map<String, String>) responseModel.getProcessDefinition().get(processEventId);
    saveProcessEventModel.setProcessDefinitionId(processEventInfo.get(IProcessEvent.PROCESS_DEFINITION_ID));
    saveProcessEventModel.setProcessDefinition(processEventInfo.get(IProcessEvent.PROCESS_DEFINITION));
    // Prepare data and upload processEvent to postgress
    IListModel<ISaveProcessEventModel> saveProcessToPostgress = new ListModel<>();
    List<ISaveProcessEventModel> bulkUploadProcessDefinitionList = new ArrayList<>();
    bulkUploadProcessDefinitionList.add(saveProcessEventModel);
    saveProcessToPostgress.setList(bulkUploadProcessDefinitionList);
    IBulkSaveProcessEventResponseModel uploadBulkResponse = bulkUploadProcessesToServerStrategy.execute(saveProcessToPostgress);
    
    // If processEvent is successfully is deployed then only save to orientDB
    ICreateOrSaveProcessEventResponseModel saveProcessToOrient = new CreateOrSaveProcessEventResponseModel();
    if (uploadBulkResponse.getSuccess() != null) {
      ISaveProcessEventModel saveProcessEventToOrient = (ISaveProcessEventModel) uploadBulkResponse.getSuccess().getProcessEventsList()
          .get(0);
      saveProcessToOrient = saveProcessEventStrategy.execute(saveProcessEventToOrient);
    }
    
    return saveProcessToOrient;
  }
}
