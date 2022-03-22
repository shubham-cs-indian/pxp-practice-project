package com.cs.di.config.model.configapi;

import com.cs.core.runtime.interactor.model.configuration.IModel;

import java.util.Map;

public interface IConfigAPIResponseModel extends IModel {
    Map<String, Object> getConfigDetails();

    void setConfigDetails(Map<String, Object> configDetails);
}
