package com.cs.di.config.strategy.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cs.core.config.interactor.model.configdetails.IListModel;
import com.cs.core.config.interactor.model.tabs.CreateTabModel;
import com.cs.core.config.interactor.model.tabs.IBulkSaveTabResponseModel;
import com.cs.core.config.interactor.model.tabs.ICreateTabModel;
import com.cs.core.config.interactor.model.tabs.IGetGridTabsModel;
import com.cs.core.config.interactor.model.tabs.IGetTabEntityModel;
import com.cs.core.config.interactor.model.tabs.IGetTabModel;
import com.cs.core.config.interactor.model.tabs.ISaveTabModel;
import com.cs.core.config.interactor.model.tabs.SaveTabModel;
import com.cs.core.config.interactor.usecase.tab.IBulkSaveTab;
import com.cs.core.config.interactor.usecase.tab.ICreateTab;
import com.cs.core.config.interactor.usecase.tab.IGetTab;
import com.cs.core.config.interactor.usecase.tab.ISaveTab;
import com.cs.core.rdbms.auditlog.idto.IAuditLogDTO.ServiceType;
import com.cs.core.runtime.interactor.model.configuration.IIdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.core.runtime.interactor.model.configuration.IdParameterModel;
import com.cs.core.runtime.interactor.model.configuration.ListModel;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.di.config.model.configapi.IConfigAPIRequestModel;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class TabAPIStrategy extends AbstractConfigurationAPIStrategy
    implements IConfigurationAPIStrategy {
  
  @Autowired
  IGetTab      getTab;
  
  @Autowired
  ICreateTab   createTab;
  
  @Autowired
  ISaveTab     saveTab;
  
  @Autowired
  IBulkSaveTab bulkSaveTab;
  
  private static final ObjectMapper mapper = new ObjectMapper()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  
  @Override
  public ServiceType getServiceType()
  {
    return null;
  }
  
  @Override
  protected IModel executeRead(String code, IConfigAPIRequestModel configModel) throws Exception
  {
    IIdParameterModel model = new IdParameterModel();
    model.setId(code);
    return getTab.execute(model);
  }
  
  @Override
  protected IModel executeCreate(Map<String, Object> inputData, IConfigAPIRequestModel configModel)
      throws Exception
  {
    ICreateTabModel createTabModel = mapper.readValue(configModel.getData(),
        CreateTabModel.class);
    IGetTabModel response = createTab.execute(createTabModel);
    return response.getTab();
  }
  
  @SuppressWarnings("unchecked")
  @Override
  protected IModel executeUpdate(Map<String, Object> inputData, Map<String, Object> getData,
      IConfigAPIRequestModel configModel) throws Exception
  {
    Map<String, Object> tab = (Map<String, Object>) getData.get(IGetTabModel.TAB);
    IGetTabEntityModel returnModel = null;
    
    if (inputData.containsKey(ISaveTabModel.LABEL) || inputData.containsKey(ISaveTabModel.ICON)) {
      IListModel<ISaveTabModel> listModel = new ListModel<>();
      List<ISaveTabModel> serializedModelsList = new ArrayList<ISaveTabModel>();
      ISaveTabModel saveTabModel = mapper.readValue(configModel.getData(),
          SaveTabModel.class);
      saveTabModel.setId((String) tab.get(IGetTabEntityModel.ID));
      saveTabModel.setCode((String) tab.get(IGetTabEntityModel.CODE));
      String label = (String) inputData.get(ISaveTabModel.LABEL);
      label = label == null ? (String) tab.get(IGetTabEntityModel.LABEL) : label;
      saveTabModel.setLabel(label);
      String icon = (String) inputData.get(ISaveTabModel.ICON);
      icon = icon == null ? (String) tab.get(IGetTabEntityModel.ICON) : icon;
      saveTabModel.setIcon(icon);
      saveTabModel.setModifiedTabSequence((Integer) tab.get(IGetTabEntityModel.SEQUENCE));
      serializedModelsList.add(saveTabModel);
      listModel.setList(serializedModelsList);
      IBulkSaveTabResponseModel response = bulkSaveTab.execute(listModel);
      IGetGridTabsModel sucess = (IGetGridTabsModel) response.getSuccess();
      returnModel = sucess.getTabList().get(0);
    }
    
    if (inputData.containsKey(ISaveTabModel.MODIFIED_PROPERTY_SEQUENCE)) {
      ISaveTabModel saveTabModel = ObjectMapperUtil.readValue(configModel.getData(),
          SaveTabModel.class);
      saveTabModel.setId((String) tab.get(IGetTabEntityModel.ID));
      IGetTabModel response = saveTab.execute(saveTabModel);
      returnModel = response.getTab();
    }
    
    return returnModel;
  }
  
}
