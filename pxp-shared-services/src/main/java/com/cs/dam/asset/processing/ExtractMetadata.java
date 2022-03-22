/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates and open the template
 * in the editor.
 */
package com.cs.dam.asset.processing;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.im4java.core.ETOperation;
import org.im4java.core.ExiftoolCmd;
import org.im4java.process.ArrayListOutputConsumer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.cs.dam.asset.exceptions.ExifToolException;

/**
 * This is utility class for extract metadata related methods.
 * @author pranav.huchche
 *
 */
public class ExtractMetadata {

  /**
   * This method extracts all meta data info from a given file
   *
   * @param sourcePath: String source path of the video file
   * @return String
   * @throws ParseException 
   * @throws ExifToolException 
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public static Map<String, Object> extractMetadataFromFile(String sourcePath) throws ParseException, ExifToolException {
    ETOperation op = new ETOperation();
    List<String> list = new ArrayList<>();
    list.add("-G");
    op.addImage(sourcePath);
    op.addRawArgs(list);
    op.json();
    ArrayListOutputConsumer output = new ArrayListOutputConsumer();
    ExiftoolCmd et = new ExiftoolCmd();

    et.setOutputConsumer(output);
    try {
      et.run(op);
    } catch (Exception e) {
      throw new ExifToolException(e);
    }
    // dump output
    ArrayList<String> cmdOutput = output.getOutput();
    String jsonStr = "";
    for (String line : cmdOutput) {
      jsonStr += line;
    }
    JSONParser jp = new JSONParser();
    JSONArray jsonArray = (JSONArray) jp.parse(jsonStr);
    JSONObject jsonMetaData = (JSONObject) jsonArray.get(0);

    return jsonMetaData;
  }
}
