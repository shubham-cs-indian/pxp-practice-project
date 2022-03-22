package com.cs.di.config.strategy.api;

import com.cs.core.runtime.interactor.model.configuration.IModel;
import com.cs.di.base.IDiStrategy;
import com.cs.di.config.model.configapi.ConfigAPIResponseModel;
import com.cs.di.config.model.configapi.IConfigAPIRequestModel;
import com.cs.di.config.model.configapi.IConfigAPIResponseModel;
import com.cs.di.workflow.constants.EntityAction;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public abstract class AbstractConfigurationAPIStrategy implements IDiStrategy<IConfigAPIRequestModel, IConfigAPIResponseModel> {
    /**
     *
     * @param configModel input model to depict the action to take on the node
     * @return response with map of the latest data of the configuration entity
     * @throws Exception
     */
    @Override
    public IConfigAPIResponseModel execute(IConfigAPIRequestModel configModel) throws Exception
    {
        Map<String, Object> inputData = new ObjectMapper().readValue(configModel.getData(), new TypeReference<Map<String, Object>>()
        {});
        if(configModel.getAction()==EntityAction.CREATE) {
            return new ConfigAPIResponseModel(convertToMap(this.executeCreate(inputData, configModel)));
        }
        Map<String, Object> getData = convertToMap(executeRead((String) inputData.get("code") == null ? (String) inputData.get("id"):  
          (String) inputData.get("code"), configModel));
        if (configModel.getAction() == EntityAction.UPDATE) {
            getData = convertToMap(this.executeUpdate(inputData, getData, configModel));
        }
        return new ConfigAPIResponseModel(getData);
    }

    Map convertToMap(Object object) {
        if(object==null)
            return null;

        return new ObjectMapper().convertValue(object, Map.class);
    }

    /**
     * @param code the code of the requested configuration entities
     * @return response model containing the data map of the requested configuration entity
     */
    protected abstract IModel executeRead(String code, IConfigAPIRequestModel configModel) throws Exception;

    /**
     * @param inputData This is data provided as input on the workflow task
     * @return Returns updated map of all data of the configuration entity
     * @throws Exception 
     */
    protected abstract IModel executeCreate(Map<String, Object> inputData, IConfigAPIRequestModel configModel) throws Exception;

    /**
     *
     * @param inputData This is data provided as input on the workflow task
     * @param getData This is data retrieved using get API for configuration entity
     * @return Returns updated map of all data of the configuration entity
     * @throws Exception 
     */
  protected abstract IModel executeUpdate(Map<String, Object> inputData, Map<String, Object> getData, IConfigAPIRequestModel configModel)
      throws Exception;
}
