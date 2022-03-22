/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates and open the template
 * in the editor.
 */
package com.cs.core.technical.asset;

import java.util.Map;

public interface IExtractMetadata {

  /**
   * This method extracts all meta data info from a given file
   *
   * @param sourcePath: String source path of the video file
   * @return String
   * @throws Exception
   */
  public Map<String, Object> extractMetadataFromFile(String sourcePath) throws Exception;

}
