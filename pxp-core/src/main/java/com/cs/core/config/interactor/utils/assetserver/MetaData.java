package com.cs.core.config.interactor.utils.assetserver;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.

package com.cs.config.interactor.usecase.assetserver.utils;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// simple json
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

//im4java
import org.im4java.core.ETOperation;
import org.im4java.core.ExiftoolCmd;
import org.im4java.process.ArrayListOutputConsumer;
import org.json.simple.JSONArray;

public class MetaData {

    *//**
        * This method extracts all meta data info from a given file
        *
        * @param sourcePath:
        *          String source path of the video file
        * @return String
        * @throws Exception
        *//*
                          public static Map<String,Object> extractMetaDataFromFile(String sourcePath) throws Exception{
                              ETOperation op = new ETOperation();
                              List<String> list = new ArrayList<>();
                              list.add("-G");
                              op.addImage(sourcePath);
                              op.addRawArgs(list);
                              op.json();
                              ArrayListOutputConsumer output = new ArrayListOutputConsumer();
                              ExiftoolCmd et = new ExiftoolCmd();
          
                              et.setOutputConsumer(output);
                              et.run(op);
                              // dump output
                              ArrayList<String> cmdOutput = output.getOutput();
                              String jsonStr = "";
                              for (String line:cmdOutput) {
                                  jsonStr+= line;
                              }
                              JSONParser jp = new JSONParser();
                              JSONArray jsonArray =  (JSONArray)jp.parse(jsonStr);
                              JSONObject jsonMetaData = (JSONObject)jsonArray.get(0);
                              return jsonMetaData;
          
                          }
          
                      }
                      */
