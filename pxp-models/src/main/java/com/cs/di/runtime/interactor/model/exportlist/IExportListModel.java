package com.cs.di.runtime.interactor.model.exportlist;

// TODO :: shud be in base pkg..
import java.util.List;
import java.util.Map;

import com.cs.core.runtime.interactor.model.configuration.IModel;

public interface IExportListModel<T> extends IModel {

  public static final String LIST = "list";

  public List<Map<String, Object>> getList();

  public void setList(List<Map<String, Object>> list);

}
