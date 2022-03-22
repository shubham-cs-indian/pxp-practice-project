package com.cs.di.workflow.constants;

public enum MessageCode
{
  GEN000("Total Count:{0} Success Count:{1}"),
  GEN001("Unsupported message type : {0}"),
  GEN002("No files found"),
  GEN003("Input parameter {0} is null."),
  GEN004("Input parameter {0} is empty."),
  GEN005("Input parameter {0} is Invalid."),
  GEN006("Error while performing read/write operation on file: {0}"),
  GEN007("Sheet {0} not found in file: {1}"),
  GEN008("Error while getting file headers"),
  GEN009("Error occured during processing of file: {0}"),
  GEN010("Failed to get local catalog info"),
  GEN011("Transformation completed for file: {0}"),
  GEN012("Error occured during processing of file"),
  GEN013("Empty or Invalid or Multiple nature klass ids found"),
  GEN014("Error occured during transformation : {0}"),
  GEN015("Invalid Code : {0}"),
  GEN016("Parent/Top Parent {0} not found for variant: {1}"),
  GEN017("Invalid context code for {0}"),
  GEN018("Error while writing to file {0}"),
  GEN019("Shared folder path not found for writing inbound transformation files"),
  GEN020("{0} is not Present"),
  GEN500("Internal Error"),
  GEN501("Error While Sending Email Notification"),
  GEN021("Input parameter {0} is not a {1}"),
  GEN022("Error occured in Camunda Task {0} Id:{1}"),
  GEN023("File {0} imported successfully. Import Process Job ID is : {1}"),
  GEN024("File not found"),
  GEN025("Error occured during import of file {0}"),
  GEN026("Invalid language code for variant: {0}"),
  GEN027("Archival path not found"),
  GEN028("Filed to convert excel file to byte stream."),
  GEN029("Default language code not found"),
  GEN030("Error occured during transformation of attribute variant"),
  GEN031("Values of {0} parameters {1} and {2} cannot be same or overlaping. Respective values are {3} and {4}"),
  GEN032("Number of days should be greater than 0 for {0}"),
  GEN033("Housekeeping successfull for : {0}"),
  GEN034("Invalid email id: {0} for code: {1}"),
  GEN035("Task completed successfully Camunda Task {0} Id:{1}"),
  GEN036("E-Mail send Successfuuly."),
  GEN037("PXON has Invalid Config Type : {0}"),
  GEN038("Config Transformation not supported for : {0}"),
  GEN039("Unsupported Configuration Entity"),
  GEN040("Workflow not found for specified trigger criteria"),
  GEN041("Invalid value given to attribute : {0}"),
  GEN042("Execution Summary : {0}" ),
  GEN043("Transfer failed"),
  GEN044("Transfer Success for job Id : {0}"),
  GEN045("{0} successfully executed"),
  GEN046("Unauthrized Nature Class {0} , user not allowed to Export"),
  GEN047("JMS Acknowledgment sent Successfully."),
  GEN048("Can not create entity {0} as entity already exists"),
  GEN049("Can not update or delete entity {0} as entity does not exists"),
  GEN050("Invalid action {0} for entity {1}"),
  GENO51("Could not transform the property record : {0}"),
  GENO52("{0} Service unavailable");

  final String message;
  
  MessageCode(String message)
  {
    this.message = message;
  }
  
  public String getMessage()
  {
    return message;
  }
  
  public static MessageCode fromString(String messageCode)
  {
    for (MessageCode statusMessage : MessageCode.values()) {
      if (statusMessage.name().equalsIgnoreCase(messageCode)) {
        return statusMessage;
      }
    }
    return null;
  }
  
  public static String getMessage(String messageCode)
  {
    MessageCode statusMessage = fromString(messageCode);
    return statusMessage != null ? statusMessage.getMessage() : null;
  }
  
  public static String getMessageText(String messageCode, ObjectCode[] objectCodes)
  {
    for (MessageCode statusMessage : MessageCode.values()) {
      if (statusMessage.name().equalsIgnoreCase(messageCode)) {
        return statusMessage.getMessage();
      }
    }
    return null;
  }
}
