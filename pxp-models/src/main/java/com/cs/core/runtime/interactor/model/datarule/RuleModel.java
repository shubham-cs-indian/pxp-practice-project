/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates and open the template
 * in the editor.
 */
package com.cs.core.runtime.interactor.model.datarule;

/**
 * @author payal.pillewar
 */
public class RuleModel implements IRuleModel {
  
  protected String id;
  protected String name;
  protected String script;
  protected String bytecode;
  
  @Override
  public String getId()
  {
    return id;
  }
  
  @Override
  public void setId(String id)
  {
    this.id = id;
  }
  
  @Override
  public String getName()
  {
    return name;
  }
  
  @Override
  public void setName(String name)
  {
    this.name = name;
  }
  
  @Override
  public String getScript()
  {
    return script;
  }
  
  @Override
  public void setScript(String script)
  {
    this.script = script;
  }
  
  @Override
  public String getBytecode()
  {
    return bytecode;
  }
  
  @Override
  public void setBytecode(String bytecode)
  {
    this.bytecode = bytecode;
  }
  
  @Override
  public String toString()
  {
    return "RuleModel{" + "id=" + id + ", name=" + name + ", script=" + script + ", bytecode="
        + bytecode + '}';
  }
}
