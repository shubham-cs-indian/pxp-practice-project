package com.cs.di.jms;

import com.cs.core.runtime.interactor.model.dataintegration.IJMSConfigModel;

public interface IJMSMessageListenerService extends IJMSListenerService {
    void replaceJMSListener(IJMSConfigModel oldJMSConfig, boolean oldIsActive, IJMSConfigModel newJMSConfig, boolean newIsActive, String processId, String deploymentId);
}