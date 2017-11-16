/*
 * SemanticRelation.java
 * 
 * Copyright (c) 1995-2012, The University of Sheffield. See the file
 * COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 * 
 * This file is part of GATE (see http://gate.ac.uk/), and is free software,
 * licenced under the GNU Library General Public License, Version 2, June 1991
 * (in the distribution as file licence.html, and also available at
 * http://gate.ac.uk/gate/licence.html).
 * 
 * Marin Dimitrov, 16/May/2002
 * 
 * $Id: SemanticRelationImpl.java 17496 2014-03-01 14:20:35Z markagreenwood $
 */

package gate.wordnet;

public class SemanticRelationImpl extends RelationImpl implements
  SemanticRelation {

  private SynsetImpl source;

  private SynsetImpl target;

  public SemanticRelationImpl(int _type, SynsetImpl _src, SynsetImpl _target) {

    super(_type);

    assert _src != null;
    assert _target != null;
    assert WNHelper.isValidSemanticPointer(_type);

    this.source = _src;
    this.target = _target;
  }

  /** returns the source (Synset) of this lexical relation */
  public Synset getSource() {
    return this.source;
  }

  /** returns the source (Synset) of this lexical relation */
  public Synset getTarget() {
    return this.target;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((source == null) ? 0 : source.hashCode());
    result = prime * result + ((target == null) ? 0 : target.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if(this == obj) return true;
    if(!super.equals(obj)) return false;
    if(getClass() != obj.getClass()) return false;
    SemanticRelationImpl other = (SemanticRelationImpl)obj;
    if(source == null) {
      if(other.source != null) return false;
    } else if(!source.equals(other.source)) return false;
    if(target == null) {
      if(other.target != null) return false;
    } else if(!target.equals(other.target)) return false;
    return true;
  }
}