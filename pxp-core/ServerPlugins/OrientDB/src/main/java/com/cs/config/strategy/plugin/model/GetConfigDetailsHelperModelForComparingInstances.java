package com.cs.config.strategy.plugin.model;

import com.tinkerpop.blueprints.Vertex;
import java.util.HashSet;
import java.util.Set;

public class GetConfigDetailsHelperModelForComparingInstances
    implements IGetConfigDetailsHelperModelForComparingInstances {
  
  Set<Vertex> natureNodes;
  Set<Vertex> nonNatureNodes;
  Set<Vertex> collectionNodes;
  Set<Vertex> taxonomyVertices;
  
  @Override
  public void setNatureNodes(Set<Vertex> natureNodes)
  {
    this.natureNodes = natureNodes;
  }
  
  @Override
  public Set<Vertex> getNatureNodes()
  {
    if (natureNodes == null) {
      natureNodes = new HashSet<>();
    }
    return natureNodes;
  }
  
  @Override
  public void setNonNatureNodes(Set<Vertex> nonNatureNodes)
  {
    this.nonNatureNodes = nonNatureNodes;
  }
  
  @Override
  public Set<Vertex> getNonNatureNodes()
  {
    if (nonNatureNodes == null) {
      nonNatureNodes = new HashSet<>();
    }
    return nonNatureNodes;
  }
  
  @Override
  public void setCollectionNodes(Set<Vertex> collectionNodes)
  {
    this.collectionNodes = collectionNodes;
  }
  
  @Override
  public Set<Vertex> getCollectionNodes()
  {
    if (collectionNodes == null) {
      collectionNodes = new HashSet<>();
    }
    return collectionNodes;
  }
  
  @Override
  public void setTaxonomyVertices(Set<Vertex> taxonomyVertices)
  {
    this.taxonomyVertices = taxonomyVertices;
  }
  
  @Override
  public Set<Vertex> getTaxonomyVertices()
  {
    if (taxonomyVertices == null) {
      taxonomyVertices = new HashSet<>();
    }
    return taxonomyVertices;
  }
}
