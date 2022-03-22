package com.cs.core.services;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.cs.core.csexpress.definition.CSEObject;
/**
 * Singleton to access cache system Before going to a more elaborate Apache Common JCS system, a simple memory hashTable is used on first
 * instance In the meantime, objects to store in cache must implement Serializable.
 */
public final class CSCache {

  private static final CSCache INSTANCE = new CSCache();
  private Map<String, Serializable> cache = new ConcurrentHashMap<>();

  // Singleton implementation
  private CSCache() {
  }

  public static CSCache instance() {
    return INSTANCE;
  }

  /**
   * Keep a content in cache
   *
   * @param id the storage key attached to the content
   * @param content the content itself
   */
  public void keep(String id, Serializable content) {
    synchronized (INSTANCE) {
      if(!isKept(id)) {
        cache.put(id, content);
      }
    }
  }

  /**
   * @param id a the storage key
   * @return true when an attached content is defined for this key
   */
  public boolean isKept(String id) {
    return cache.containsKey(id);
  }

  /**
   * @param id the storage key attached to the content when kept
   * @return the content or null
   */
  public Serializable get(String id) {
    return cache.get(id);
  }

  /**
   * @return the current size of the cache
   */
  public int size() {
    return cache.size();
  }

  /**
   * Remove a content from cache
   *
   * @param id      the storage key attached to the content
   */
  public void remove(String id)
  {
    synchronized (INSTANCE) {
      if (isKept(id)) {
        cache.remove(id);
      }
    }
  }

  /**
   * This method will delete the property (attribute/tag/relationship) code from cache.
   * 
   * @param propertyCode
   */
  public static void deletePropertyCache(String propertyCode)
  {
    String cachePropertyCode = String.format("%c:%s", CSEObject.CSEObjectType.Property.letter(),
        propertyCode);
    if (CSCache.instance().isKept(cachePropertyCode)) {
      CSCache.instance().remove(cachePropertyCode);
    }
  }
}
