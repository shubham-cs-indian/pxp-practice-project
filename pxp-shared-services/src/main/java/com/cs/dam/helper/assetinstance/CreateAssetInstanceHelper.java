package com.cs.dam.helper.assetinstance;

/**
 * @author vannya.kalani
 * This class is helper class for creating asset instances.
 */
public class CreateAssetInstanceHelper {
  
  private static CreateAssetInstanceHelper instance = null;
  
  private CreateAssetInstanceHelper() {
    // private constructor for Singleton class.
  }
  
  /**
   * Use this method to get CreateAssetInstanceHelper instance
   * @return CreateAssetInstanceHelper instance
   */
  public static CreateAssetInstanceHelper getInstance() {
    if(instance == null) {
      instance = new CreateAssetInstanceHelper();
    }
    return instance;
  }
  
  
}
