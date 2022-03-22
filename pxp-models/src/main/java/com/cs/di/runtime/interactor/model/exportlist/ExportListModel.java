package com.cs.di.runtime.interactor.model.exportlist;

import java.util.List;
import java.util.Map;

public class ExportListModel implements IExportListModel<Map> {
  
  private static final long           serialVersionUID = 1L;
  
  protected List<Map<String, Object>> list;

  public ExportListModel()
  {
    // TODO Auto-generated constructor stub
  }

  public ExportListModel(List<Map<String, Object>> list)
  {
    this.list = list;
  }

  @Override public List<Map<String, Object>> getList()
  {
    return list;
  }

  @Override public void setList(List<Map<String, Object>> list)
  {
    this.list = list;
  }

}
