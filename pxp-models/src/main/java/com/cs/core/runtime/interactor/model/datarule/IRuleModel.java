/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates and open the template
 * in the editor.
 */
package com.cs.core.runtime.interactor.model.datarule;

import com.cs.core.runtime.interactor.model.configuration.IModel;

/**
 * @author payal.pillewar
 */
public interface IRuleModel extends IModel {
  
  public static final String ID       = "id";
  public static final String NAME     = "name";
  public static final String SCRIPT   = "script";
  public static final String BYTECODE = "bytecode";
  
  public String getId();
  
  public void setId(String id);
  
  public String getName();
  
  public void setName(String name);
  
  public String getScript();
  
  public void setScript(String script);
  
  public String getBytecode();
  
  public void setBytecode(String bytecode);
}
