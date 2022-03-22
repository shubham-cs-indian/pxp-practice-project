package com.cs.core.elastic.iservices;

import com.cs.core.elastic.Index;
import com.cs.core.technical.exception.CSInitializationException;

import java.io.IOException;
import java.util.Collection;

public interface IIndexServices {

  /**
   * This method is used to create elastic indices.
   *
   * @param indices BaseTypes For which indexes need to be created
   * @throws IOException produced by failed or interrupted I/O operations.
   */
  void createIndices(Collection<Index> indices) throws IOException;

}
