package com.cs.core.runtime.intialize.elastic;

import com.cs.core.technical.exception.CSInitializationException;

import java.io.IOException;

public interface ICreateIndices {

  public void execute() throws IOException, CSInitializationException;

}
