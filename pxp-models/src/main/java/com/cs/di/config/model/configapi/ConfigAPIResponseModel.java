package com.cs.di.config.model.configapi;

import java.util.Map;

public class ConfigAPIResponseModel implements IConfigAPIResponseModel {
    private static final long serialVersionUID = 1L;
    protected Map<String, Object> configDetails;

    public ConfigAPIResponseModel() {
        super();
    }

    public ConfigAPIResponseModel(Map<String, Object> configDetails)
    {
        super();
        this.configDetails = configDetails;
    }

    @Override
    public Map<String, Object> getConfigDetails() {
        return this.configDetails;
    }

    @Override
    public void setConfigDetails(Map<String, Object> configDetails) {
        this.configDetails = configDetails;
    }
}
