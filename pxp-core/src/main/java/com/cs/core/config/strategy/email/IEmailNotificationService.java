package com.cs.core.config.strategy.email;

import com.cs.core.runtime.interactor.model.mail.IMailNotificationModel;
import com.cs.core.runtime.strategy.configuration.base.IRuntimeStrategy;

public interface IEmailNotificationService extends
IRuntimeStrategy<IMailNotificationModel, IMailNotificationModel> {
  
}
