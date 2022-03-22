package com.cs.base.saml.configuration.custom;

import java.util.List;

import org.opensaml.common.binding.SAMLMessageContext;
import org.opensaml.saml2.binding.encoding.HTTPRedirectDeflateEncoder;
import org.opensaml.saml2.core.RequestAbstractType;
import org.opensaml.saml2.core.StatusResponseType;
import org.opensaml.util.URLBuilder;
import org.opensaml.ws.message.encoder.MessageEncodingException;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.util.Pair;

import com.cs.constants.CommonConstants;



public class CustomSAMLMessageEncoder extends HTTPRedirectDeflateEncoder {
  
  /**
   * Builds the URL to redirect the client to.
   * 
   * @param messagesContext current message context
   * @param endpointURL endpoint URL to send encoded message to
   * @param message Deflated and Base64 encoded message
   * 
   * @return URL to redirect client to
   * 
   * @throws MessageEncodingException thrown if the SAML message is neither a RequestAbstractType or Response
   */
  protected String buildRedirectURL(SAMLMessageContext messagesContext, String endpointURL, String message)
          throws MessageEncodingException {
      URLBuilder urlBuilder = new URLBuilder(endpointURL);

      List<Pair<String, String>> queryParams = urlBuilder.getQueryParams();
      queryParams.clear();
      String userName = CustomSamlEntryPoint.getUserName();
      if(userName != null) {
        queryParams.add(new Pair<String, String>(CommonConstants.USER_NAME_PROPERTY, userName));
      }

      if (messagesContext.getOutboundSAMLMessage() instanceof RequestAbstractType) {
          queryParams.add(new Pair<String, String>("SAMLRequest", message));
      } else if (messagesContext.getOutboundSAMLMessage() instanceof StatusResponseType) {
          queryParams.add(new Pair<String, String>("SAMLResponse", message));
      } else {
          throw new MessageEncodingException(
                  "SAML message is neither a SAML RequestAbstractType or StatusResponseType");
      }

      String relayState = messagesContext.getRelayState();
      if (checkRelayState(relayState)) {
          queryParams.add(new Pair<String, String>("RelayState", relayState));
      }

      Credential signingCredential = messagesContext.getOuboundSAMLMessageSigningCredential();
      if (signingCredential != null) {
          String sigAlgURI = getSignatureAlgorithmURI(signingCredential, null);
          Pair<String, String> sigAlg = new Pair<String, String>("SigAlg", sigAlgURI);
          queryParams.add(sigAlg);
          String sigMaterial = urlBuilder.buildQueryString();

          queryParams.add(new Pair<String, String>("Signature", generateSignature(signingCredential, sigAlgURI,
                  sigMaterial)));
      }

      return urlBuilder.buildURL();
  }
}