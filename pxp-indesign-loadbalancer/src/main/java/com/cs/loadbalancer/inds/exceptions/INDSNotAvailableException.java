package com.cs.loadbalancer.inds.exceptions;

/**
 * This Exception is thrown when load balancer is configured and indesign server
 * is not running
 */
public class INDSNotAvailableException extends Exception {
  
  private static final long serialVersionUID = 1L;
  
  public INDSNotAvailableException()
  {
    
  }
}
