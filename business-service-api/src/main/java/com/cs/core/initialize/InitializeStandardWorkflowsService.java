package com.cs.core.initialize;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.constants.CommonConstants;
import com.cs.constants.initialize.InitializeDataConstants;
import com.cs.core.config.interactor.exception.processevent.ProcessEventNotFoundException;
import com.cs.core.config.interactor.model.processevent.IGetProcessEventModel;
import com.cs.core.config.interactor.model.processevent.IProcessEventModel;
import com.cs.core.config.interactor.model.processevent.ProcessEventModel;
import com.cs.core.config.interactor.model.translations.ISaveTranslationsRequestModel;
import com.cs.core.config.strategy.usecase.translations.ISavePropertiesTranslationsStrategy;
import com.cs.core.exception.NotFoundException;
import com.cs.core.initialize.IInitializeStandardWorkflowsService;
import com.cs.core.interactor.usecase.initialize.utils.GetTranslationsForInitialization;
import com.cs.core.runtime.interactor.constants.application.ProcessConstants;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IIdsListParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IdsListParameterModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.di.config.businessapi.processevent.ICreateProcessEventService;
import com.cs.di.config.businessapi.processevent.IDeleteProcessEventService;
import com.cs.di.config.businessapi.processevent.IGetProcessEventService;
import com.cs.di.config.businessapi.processevent.ISaveProcessEventService;
import com.cs.di.config.interactor.model.initializeworflowevent.ISaveProcessEventModel;
import com.cs.di.config.interactor.model.initializeworflowevent.SaveProcessEventModel;
import com.cs.di.config.model.initializeevent.IInitializeWorkflowEventModel;
import com.cs.di.config.model.initializeevent.InitializeWorkflowEventModel;
import com.cs.workflow.base.WorkflowType;
import com.fasterxml.jackson.core.type.TypeReference;

@Component
public class InitializeStandardWorkflowsService implements IInitializeStandardWorkflowsService {

  @Autowired
  protected IGetProcessEventService                    getProcessEventService;

  @Autowired
  protected ICreateProcessEventService                 createProcessEventService;

  @Autowired
  protected ISaveProcessEventService                   saveProcessEventService;

  @Autowired
  protected ISavePropertiesTranslationsStrategy savePropertiesTranslationsStrategy;

  @Autowired
  protected IDeleteProcessEventService                 deleteProcessEventService;

  @Override
  public void execute() throws Exception
  {
    //TODO add standard transfer workflow in file if whenever required
    InputStream stream = this.getClass().getClassLoader().getResourceAsStream(InitializeDataConstants.STANDARD_BGP_PROCESS_JSON);
    List<IInitializeWorkflowEventModel> listInitializeProcessEventModel = ObjectMapperUtil.readValue(stream, new TypeReference<List<InitializeWorkflowEventModel>>(){});
    stream.close();
    
    initializeStandardWorkflows(listInitializeProcessEventModel);
  }

  private void initializeStandardWorkflows(
      List<IInitializeWorkflowEventModel> listInitializeProcessEventModel) throws Exception
  {
    for (IInitializeWorkflowEventModel initializeProcessEventModel : listInitializeProcessEventModel) {
      IIdParameterModel processIdModel = new IdParameterModel(initializeProcessEventModel.getId());
      IGetProcessEventModel existingProcessEventModel = null;
      try {
        if (initializeProcessEventModel.getIsXMLModified()) {
            deleteExistingProcess(processIdModel);
        }
        else {
          existingProcessEventModel = getProcessEventService.execute(processIdModel);
        }
      }
      catch (ProcessEventNotFoundException e) {
        System.out.println("Process not found. Creation is in progress!");
      }
      catch (NotFoundException e) {
        System.out.println("Process not found. Creation is in progress!");
      }
      catch (Exception e) {
        System.out.println("Unable to delete existing process. Creation process stopped!");
        continue;
      }
      if (existingProcessEventModel == null) {
        IProcessEventModel newprocessEventModel = new ProcessEventModel();
        newprocessEventModel.setId(initializeProcessEventModel.getId());
        newprocessEventModel.setCode(initializeProcessEventModel.getId());
        newprocessEventModel.setLabel(initializeProcessEventModel.getLabel());
        newprocessEventModel.setEventType(initializeProcessEventModel.getEventType());
        newprocessEventModel.setProcessType(initializeProcessEventModel.getProcessType());
        newprocessEventModel.setWorkflowType(WorkflowType.HIDDEN_WORKFLOW.name());
        newprocessEventModel.setTriggeringType(initializeProcessEventModel.getId());
        IGetProcessEventModel getProcessEventModel = createProcessEventService.execute(newprocessEventModel);
        
        ISaveProcessEventModel saveProcessEventModel = new SaveProcessEventModel();
        saveProcessEventModel.setCode(getProcessEventModel.getCode());
        saveProcessEventModel.setId(getProcessEventModel.getId());
        saveProcessEventModel.setLabel(getProcessEventModel.getLabel()); 
        saveProcessEventModel.setEventType(getProcessEventModel.getEventType());
        saveProcessEventModel.setProcessType(getProcessEventModel.getProcessType());
        saveProcessEventModel.setWorkflowType(getProcessEventModel.getWorkflowType());
        saveProcessEventModel.setIsXMLModified(true);
        saveProcessEventModel.setIsExecutable(true);
        saveProcessEventModel.setTriggeringType(getProcessEventModel.getTriggeringType());
        try {
        BpmnModelInstance modelInstance = Bpmn.readModelFromFile(getProcessDefinition(initializeProcessEventModel.getFileName()));
        String processDefinition = Bpmn.convertToString(modelInstance);
        saveProcessEventModel.setProcessDefinition(processDefinition);
       
        saveProcessEventService.execute(saveProcessEventModel);
        
        ISaveTranslationsRequestModel translationRequestDataModel = GetTranslationsForInitialization.getSaveTranslationsRequestModel(
            InitializeDataConstants.STANDARD_BGP_TRANSLATIONS_JSON, CommonConstants.PROCESS_EVENT);
            savePropertiesTranslationsStrategy.execute(translationRequestDataModel);
        }
        catch(Exception e) {
          System.out.println("File name not exist!");
          deleteExistingProcess(processIdModel);
        }
      }
    }
  }

  private void deleteExistingProcess(IIdParameterModel processIdModel) throws Exception
  {
    IIdsListParameterModel deleteOldProcessEvent = new IdsListParameterModel();
    ArrayList<String> listIds = new ArrayList<String>();
    listIds.add(processIdModel.getId());
    deleteOldProcessEvent.setIds(listIds);
    deleteProcessEventService.execute(deleteOldProcessEvent);
  }

  public File getProcessDefinition(String fileName)
  {
    String filePath = ProcessConstants.RELATIVE_PATH_FOR_BPMN_FILES + fileName;
    URL fileUrl = getClass().getResource(filePath);
    File file = new File(fileUrl.getFile());
    return file;
  }
}

