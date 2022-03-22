package com.cs.core.config.interactor.usecase.migration;

import com.cs.config.interactor.usecase.base.IConfigInteractor;
import com.cs.core.runtime.interactor.model.configuration.IVoidModel;

public interface IMigrateConfigIIDs extends IConfigInteractor<IVoidModel, IVoidModel> {

  String Q_PROPERTY = "select propertyIID, propertyCode, propertyType from pxp.propertyConfig ";
  String Q_TAG_VALUE = "select * from pxp.tagValueConfig";
  String Q_CLASSIFIER = "select * from pxp.ClassifierConfig ";
  String Q_USER = "select * from pxp.UserConfig ";
}
