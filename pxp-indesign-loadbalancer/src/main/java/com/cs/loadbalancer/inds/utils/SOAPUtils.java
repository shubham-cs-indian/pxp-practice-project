package com.cs.loadbalancer.inds.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.PushbackInputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cs.config.interactor.entity.indsserver.IInDesignServerInstance;
import com.cs.core.runtime.strategy.utils.ObjectMapperUtil;
import com.cs.runtime.interactor.model.indsserver.IINDSScriptArgumentsModel;
import com.cs.runtime.interactor.model.indsserver.IINDSScriptRequestModel;
import com.cs.runtime.interactor.model.indsserver.INDSScriptRequestModel;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import jakarta.xml.soap.MessageFactory;
import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPConnection;
import jakarta.xml.soap.SOAPConnectionFactory;
import jakarta.xml.soap.SOAPElement;
import jakarta.xml.soap.SOAPEnvelope;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.soap.SOAPMessage;
import jakarta.xml.soap.SOAPPart;

public class SOAPUtils {
	
	private static final int UTF8_BOM_SIZE = 3;

	/**
	 * This method constructs the SOAP message from the request model.
	 */
	public static SOAPMessage createSOAPMessage(IINDSScriptRequestModel indsScriptRequestModel) throws SOAPException, IOException {
		SOAPMessage soapMessage = MessageFactory.newInstance().createMessage();
	    
	    SOAPPart soapPart = soapMessage.getSOAPPart();
	    SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
	    
	    SOAPBody soapBody = soapMessage.getSOAPBody();
	    
	    SOAPElement runScriptElement = soapBody.addChildElement(soapEnvelope.createName("IDSP:RunScript"));
	    runScriptElement.addAttribute(soapEnvelope.createName("xmlns:IDSP"), "http://ns.adobe.com/InDesign/soap/");
	    
	    SOAPElement runScriptParametersElement = runScriptElement.addChildElement(soapEnvelope.createName("runScriptParameters"));
	    
	    SOAPElement scriptLanguage = runScriptParametersElement.addChildElement(soapEnvelope.createName("scriptLanguage"));
	    scriptLanguage.addTextNode("javascript");
	    
	    String fileName = indsScriptRequestModel.getScriptFileName();
	    String scriptContent = readScriptContents(fileName);
	    SOAPElement scriptText = runScriptParametersElement.addChildElement(soapEnvelope.createName("scriptText"));
	    scriptText.addTextNode(scriptContent);
	    
	    for(IINDSScriptArgumentsModel scriptArgsModel : indsScriptRequestModel.getScriptArguments()) {
	    	SOAPElement scriptArgs = runScriptParametersElement.addChildElement(soapEnvelope.createName("scriptArgs"));
	 	    
	 	    SOAPElement scriptArgName = scriptArgs.addChildElement(soapEnvelope.createName("name"));
	 	    scriptArgName.addTextNode(scriptArgsModel.getName());
	 	    
	 	    SOAPElement scriptArgValue = scriptArgs.addChildElement(soapEnvelope.createName("value"));
	 	    scriptArgValue.addTextNode(scriptArgsModel.getValue());
	    }
	    
	    return soapMessage;
	}
	
	/**
	 * This method extracts the soap response into a Map.
	 */
	@SuppressWarnings("unchecked")
  public static Map<String, Object> extractSOAPBodyAsMapFromResponse(SOAPMessage soapResponse) throws Exception {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		soapResponse.writeTo(outputStream);
		soapResponse.getProperty("evaluated");
		String xmlResponseInString = new String(outputStream.toByteArray(), System.getProperty("file.encoding","UTF-8"));


		XmlMapper xmlMapper = new XmlMapper();
		JsonNode jsonNode = xmlMapper.readTree(xmlResponseInString.getBytes());

		String responseInJson = ObjectMapperUtil.writeValueAsString(jsonNode);
		Map<String, Object> soapResponseMap = ObjectMapperUtil.readValue(responseInJson, HashMap.class);

		Map<String, Object> soapBody = (Map<String, Object>) soapResponseMap.get("Body");
		Map<String, Object> runScriptResponse = (Map<String, Object>) soapBody.get("RunScriptResponse");
		
		return runScriptResponse;
	}
	
	/**
	 * This method pings the INDS instances & updates their statuses accordingly. 
	 */
	public static void pingINDSInstances(List<IInDesignServerInstance> inDesignServerInstances) throws Exception {
		SOAPConnection soapConnection = null;
		try {
			soapConnection = SOAPConnectionFactory.newInstance().createConnection();

			IINDSScriptRequestModel iNDSScriptRequestModel = new INDSScriptRequestModel();
			iNDSScriptRequestModel.setScriptFileName("PingScript.jsx");

			for (IInDesignServerInstance inDesignServerInstance : inDesignServerInstances) {
				SOAPMessage soapMessage = SOAPUtils.createSOAPMessage(iNDSScriptRequestModel);
				SOAPMessage soapResponse;
				try {
				  soapResponse = soapConnection.call(soapMessage, new URL("http://" + inDesignServerInstance.getHostName() + ":" + inDesignServerInstance.getPort()));
				} catch (Exception e) {
				  inDesignServerInstance.setStatus(INDSConstants.INDS_IN_ACTIVE);
				  continue;
				}
				
				Map<String, Object> responseMap = SOAPUtils.extractSOAPBodyAsMapFromResponse(soapResponse);
				if (!(responseMap.containsKey("errorString") || responseMap.containsKey("faultstring"))) {
					inDesignServerInstance.setStatus(INDSConstants.INDS_ACTIVE);
				} else {
					inDesignServerInstance.setStatus(INDSConstants.INDS_IN_ACTIVE);
				}
			}
		} finally {
			if (soapConnection != null) {
				soapConnection.close();
			}
		}
	}

	/**
	 * This method converts the contents of the JSX file into a string. 
	 * This method has been referred from the sample Java SOAP client from Adobe.
	 */
	private static String readScriptContents(String fileName) throws IOException {
		URL url = ClassLoader.getSystemResource(fileName);
		BufferedReader in = null;

		StringWriter contents = new StringWriter();
		PrintWriter out = new PrintWriter(contents);

		try {
			/*
			 * deal with Java bug where BOM (byte order mark) in unicode files
			 * is not ignored when being read from stream into a string.
			 *
			 * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4508058
			 *
			 * this code only handles UTF-8...
			 */
			PushbackInputStream myReader = new PushbackInputStream(
					url.openStream(), UTF8_BOM_SIZE);
			byte bom[] = new byte[UTF8_BOM_SIZE];

			int numBytesRead = myReader.read(bom, 0, bom.length);

			if (numBytesRead == UTF8_BOM_SIZE && (bom[0] == (byte) 0xEF)
					&& (bom[1] == (byte) 0xBB) && (bom[2] == (byte) 0xBF)) {
				in = new BufferedReader(
						new InputStreamReader(myReader, "UTF8"));
			} else {
				myReader.unread(bom, 0, numBytesRead); // put non-bom chars back
														// in stream
				in = new BufferedReader(new InputStreamReader(myReader));
			}

			/*
			 * read the file into the contents writer
			 */
			String line;
			while ((line = in.readLine()) != null) {
				out.println(line);
			}
		} finally {
			if (in != null) {
				in.close();
			}
			out.close();
		}

		return contents.toString();
	}
	
}