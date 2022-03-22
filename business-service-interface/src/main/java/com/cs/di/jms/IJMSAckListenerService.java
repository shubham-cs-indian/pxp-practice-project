package com.cs.di.jms;

import com.cs.core.runtime.interactor.model.dataintegration.IJMSConfigModel;

import java.util.Set;

public interface IJMSAckListenerService extends IJMSListenerService {
    void replaceJMSListeners(Set<IJMSConfigModel> oldJMSConfigList, boolean oldIsActive, Set<IJMSConfigModel> newJMSConfigList, boolean newIsActive, String processId);
}
