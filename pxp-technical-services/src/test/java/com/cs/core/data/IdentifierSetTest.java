package com.cs.core.data;

import com.cs.core.json.JSONBuilder;
import com.cs.core.json.JSONContentParser;
import com.cs.core.printer.QuickPrinter;
import com.cs.core.technical.exception.CSFormatException;
import java.util.*;
import org.junit.Test;

/**
 * @author vallee
 */
public class IdentifierSetTest extends QuickPrinter {

  private static final String JSONA = "{ \"A\":[200, 300, 400, 500], \"test\":\"A\"}";
  private static final String JSONB = "{ \"B\":[\"two\", \"trhee\", \"four\", \"five\"], \"test\":\"B\"}";

  @Test
  public void writeIdentifierSet() {
    printTestTitle("writeIdentifierSet");
    IdentifierSet set = new IdentifierSet("A", "B", "C");
    println("Empty: " + JSONBuilder.assembleJSON(set.toJSONField()));
    set.define("A", Arrays.asList(200, 300, 400, 500));
    println("Long identifiers A: " + JSONBuilder.assembleJSON(set.toJSONField()));
    set.define("B", Arrays.asList("two", "trhee", "four", "five"));
    println("String identifiers B: " + JSONBuilder.assembleJSON(set.toJSONField()));
  }

  @Test
  public void readIdentifierSet() throws CSFormatException {
    printTestTitle("readIdentifierSet");
    IdentifierSet set = new IdentifierSet("A", "B", "C");
    JSONContentParser json = new JSONContentParser(JSONA);
    set.fromJSON(json);
    println("content Key " + set.getKey());
    println("A JSON array: " + JSONBuilder.assembleJSON(set.toJSONField()));
    println("A array: " + set.getByKey("A"));
    json = new JSONContentParser(JSONB);
    set.fromJSON(json);
    println("content Key " + set.getKey());
    println("B JSON array: " + JSONBuilder.assembleJSON(set.toJSONField()));
    println("A array: " + set.getByKey("A"));
    println("B array: " + set.getByKey("B"));
    println("C array: " + set.getByKey("C"));
  }

  @Test
  public void printWithCollections() {
    printTestTitle("printWithCollections");
    Long[] content1 = {10L, 20L, 30L};
    printf("this.toString = %s\n", content1.toString());
    printf("Arrays.toString = %s\n", Arrays.toString(content1));
    Set<Long> content2 = new HashSet();
    content2.addAll(Arrays.asList(content1));
    printf("Set this.toString = %s\n", content2.toString());
    List<Long> content3 = new ArrayList();
    content3.addAll(Arrays.asList(content1));
    printf("List this.toString = %s\n", content3.toString());
  }
}
