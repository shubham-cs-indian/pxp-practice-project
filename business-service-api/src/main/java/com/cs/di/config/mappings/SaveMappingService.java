package com.cs.di.config.mappings;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cs.config.businessapi.base.AbstractSaveConfigService;
import com.cs.config.standard.IStandardConfig;
import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.endpoint.IEndpointModel;
import com.cs.core.config.interactor.model.endpoint.IGetEndpointModel;
import com.cs.core.config.interactor.model.endpoint.ISaveEndpointModel;
import com.cs.core.config.interactor.model.endpoint.SaveEndpointModel;
import com.cs.core.config.interactor.model.mapping.ICreateOrSaveMappingModel;
import com.cs.core.config.interactor.model.mapping.IMappingModel;
import com.cs.core.config.interactor.model.mapping.ISaveMappingModel;
import com.cs.core.config.interactor.model.mapping.MappingModel;
import com.cs.core.config.strategy.usecase.endpoint.IGetEndpointStrategy;
import com.cs.core.config.strategy.usecase.endpoint.ISaveEndpointStrategy;
import com.cs.core.config.strategy.usecase.mapping.ICreateMappingStrategy;
import com.cs.core.config.strategy.usecase.mapping.ISaveMappingStrategy;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.core.runtime.interactor.model.logger.TransactionData;
import com.cs.core.runtime.interactor.model.logger.TransactionThreadData;
import com.cs.core.runtime.interactor.utils.klassinstance.RDBMSUtils;


@Service
public class SaveMappingService
    extends AbstractSaveConfigService<ISaveMappingModel, ICreateOrSaveMappingModel>
    implements ISaveMappingService {
  
  @Autowired
  protected TransactionThreadData  data;
  
  @Autowired
  protected ICreateMappingStrategy createMappingStrategy;
  
  @Autowired
  protected ISaveMappingStrategy   saveMappingStrategy;
  
  @Autowired
  protected IGetEndpointStrategy   getEndpointStrategy;
  
  @Autowired
  protected ISaveEndpointStrategy  saveEndpointStrategy;
  
  @Autowired
  protected Long                   articleKlassCounter;
  
  @Override
  public ICreateOrSaveMappingModel executeInternal(ISaveMappingModel saveProfileModel) throws Exception
  {
    if (saveProfileModel.getId() != null) {
      return saveMappingStrategy.execute(saveProfileModel);
    }
    else {
      return createNewMappingAndAssignToEndpoint(saveProfileModel);
    }
  }
  private ICreateOrSaveMappingModel createNewMappingAndAssignToEndpoint(ISaveMappingModel saveProfileModel)
      throws Exception
  {
    TransactionData transactiondata = data.getTransactionData();
    String endpointId = transactiondata.getEndpointId();

    IMappingModel createModel = new MappingModel();
    createModel.setId(RDBMSUtils.newUniqueID(IStandardConfig.UniquePrefix.MAPPING.getPrefix()));
    createModel.setLabel("Neus" + articleKlassCounter++);
    createModel.setIsDefault(false);
    createModel.setAttributeMappings(saveProfileModel.getAddedAttributeMappings());
    createModel.setTagMappings(saveProfileModel.getAddedTagMappings());
    createModel.setClassMappings(saveProfileModel.getAddedClassMappings());
    createModel.setTaxonomyMappings(saveProfileModel.getAddedTaxonomyMappings());
    ICreateOrSaveMappingModel returnMapping = createMappingStrategy.execute(createModel);

    IGetEndpointModel endpointModel = getEndpointStrategy.execute(new IdParameterModel(endpointId));
    IEndpointModel endpoint = endpointModel.getEndpoint();
    List<String> addedMappings = new ArrayList<>();
    addedMappings.add(createModel.getId());
    ISaveEndpointModel saveEndpointModel = new SaveEndpointModel();
    saveEndpointModel.setId(endpoint.getId());
    saveEndpointModel.setCode(endpoint.getCode());
    saveEndpointModel.setEndpointType(endpoint.getEndpointType());
    saveEndpointModel.setIsRuntimeMappingEnabled(endpoint.getIsRuntimeMappingEnabled());
    saveEndpointModel.setLabel(endpoint.getLabel());
    saveEndpointModel.setSystemId(endpoint.getSystemId());
    saveEndpointModel.setAddedProcesses(new ArrayList<>());
    saveEndpointModel.setDeletedProcesses(new ArrayList<>());
    
    List<ISaveEndpointModel> list = new ArrayList<>();
    list.add(saveEndpointModel);
    IListModel<ISaveEndpointModel> saveListModel = new ListModel<>();
    saveListModel.setList(list);
    saveEndpointStrategy.execute(saveListModel);
    return returnMapping;
  }
}
