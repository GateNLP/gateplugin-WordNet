/*
 *  TestWordnet.java
 *
 *  Copyright (c) 1995-2012, The University of Sheffield. See the file
 *  COPYRIGHT.txt in the software or at http://gate.ac.uk/gate/COPYRIGHT.txt
 *
 *  This file is part of GATE (see http://gate.ac.uk/), and is free
 *  software, licenced under the GNU Library General Public License,
 *  Version 2, June 1991 (in the distribution as file licence.html,
 *  and also available at http://gate.ac.uk/gate/licence.html).
 *
 *  Marin Dimitrov, 17/May/02
 *
 *  $Id: TestWordNet.java 17496 2014-03-01 14:20:35Z markagreenwood $
 */

package gate.wordnet.test;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;

import java.io.File;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import gate.Gate;
import gate.GateConstants;
import gate.util.Err;
import gate.wordnet.JWNLWordNetImpl;
import gate.wordnet.SemanticRelation;
import gate.wordnet.Synset;
import gate.wordnet.WordNet;
import gate.wordnet.WordSense;

public class TestWordNet {

  private static WordNet wnMain = null;

  @Ignore
  @Test
  public void testWN_01() throws Exception {
    //test the presence of the WN files
    String wnConfigFile = (String)Gate.getUserConfig().
                          get(GateConstants.WORDNET_CONFIG_FILE);
    if(wnConfigFile == null){
      Err.prln("WordNet not present. Test aborted...");
      return;
    }
    //test synset access - read all senses for a word and compare them with the entries from the
    //WN16 index files

    //get all synsets for "cup"
    List<WordSense> senseList = wnMain.lookupWord("cup",WordNet.POS_NOUN);
    assertEquals(8,senseList.size());

    for (int i=0; i< senseList.size(); i++) {

      WordSense currSense = senseList.get(i);
      Synset currSynset = currSense.getSynset();
      assertNotNull(currSynset);

      switch(i+1) {

        case 1:
          checkSynset(currSynset,
                      "a small open container usually used for drinking; \"he put the cup back in the saucer\"; \"the handle of the cup was missing\"",
                      1);
          break;

        case 2:
          checkSynset(currSynset,
                      "the quantity a cup will hold; \"he drank a cup of coffee\"; \"he borrowed a cup of sugar\"",
                      2);
          break;

        case 3:
          checkSynset(currSynset,
                      "any cup-shaped concavity; \"bees filled the waxen cups with honey\"; \"he wore a jock strap with a metal cup\"; \"the cup of her bra\"",
                      1);
          break;

        case 4:
          checkSynset(currSynset,
                      "a United States liquid unit equal to 8 fluid ounces",
                      1);
          break;

        case 5:
          checkSynset(currSynset,
                      "cup-shaped plant organ",
                      1);
          break;

        case 6:
          checkSynset(currSynset,
                      "punch served in a pitcher instead of a punch bowl",
                      1);
          break;

        case 7:
          checkSynset(currSynset,
                      "the hole (or metal container in the hole) on a golf green; \"he swore as the ball rimmed the cup and rolled away\"; \"put the flag back in the cup\"",
                      1);
          break;

        case 8:
          checkSynset(currSynset,
                      "a large metal vessel with two handles that is awarded to the winner of a competition; \"the school kept the cups is a special glass case\"",
                      2);
          break;
      }
    }
  }

  @Ignore
  @Test
  public void testWN_02() throws Exception {
    //test the presence of the WN files
    String wnConfigFile = (String)Gate.getUserConfig().
                          get(GateConstants.WORDNET_CONFIG_FILE);
    if(wnConfigFile == null){
      Err.prln("WordNet not present. Test aborted...");
      return;
    }

    //test hypernymy - traverse upwards the hierarchy starting from some word
    //compare the result with the WN16 index files
    //get all synsets for "cup"

    List<WordSense> senseList = wnMain.lookupWord("cup",WordNet.POS_NOUN);
    assertEquals(8,senseList.size());

    for (int i=0; i< senseList.size(); i++) {

      WordSense currSense = (WordSense)senseList.get(i);
      Synset currSynset = currSense.getSynset();
      assertNotNull(currSynset);

      if (false == currSynset.getGloss().equals("a small open container usually used for drinking; \"he put the cup back in the saucer\"; \"the handle of the cup was missing\"")) {
        continue;
      }

      List<SemanticRelation> semRelations = currSynset.getSemanticRelations(SemanticRelation.REL_HYPERNYM);
      assertNotNull(semRelations);
      assertEquals(2,semRelations.size());

      for (int j=0; j< semRelations.size(); j++ ) {

        SemanticRelation currHyperRel = (SemanticRelation)semRelations.get(j);

        assertTrue(currHyperRel.getType() == SemanticRelation.REL_HYPERNYM);
        assertEquals("@",currHyperRel.getSymbol());
        assertEquals(currSynset, currHyperRel.getSource());

        Synset currHypernym = currHyperRel.getTarget();
        assertNotNull(currHypernym);

        Synset hyperSynset = currHypernym;
        if (currHypernym.getGloss().equals("eating and serving dishes collectively")) {

          hyperSynset = ((SemanticRelation)hyperSynset.getSemanticRelations(SemanticRelation.REL_HYPERNYM).get(0)).getTarget();
          assertEquals("articles for use at the table",hyperSynset.getGloss());

          hyperSynset = ((SemanticRelation)hyperSynset.getSemanticRelations(SemanticRelation.REL_HYPERNYM).get(0)).getTarget();
          assertEquals("articles of the same kind or material; usually used in combination: silverware; software",hyperSynset.getGloss());

          hyperSynset = ((SemanticRelation)hyperSynset.getSemanticRelations(SemanticRelation.REL_HYPERNYM).get(0)).getTarget();
          assertEquals("one of a class of artifacts; \"an article of clothing\"",hyperSynset.getGloss());

          hyperSynset = ((SemanticRelation)hyperSynset.getSemanticRelations(SemanticRelation.REL_HYPERNYM).get(0)).getTarget();
          assertEquals("a man-made object",hyperSynset.getGloss());

          hyperSynset = ((SemanticRelation)hyperSynset.getSemanticRelations(SemanticRelation.REL_HYPERNYM).get(0)).getTarget();
          assertEquals("a physical (tangible and visible) entity; \"it was full of rackets, balls and other objects\"",hyperSynset.getGloss());

          hyperSynset = ((SemanticRelation)hyperSynset.getSemanticRelations(SemanticRelation.REL_HYPERNYM).get(0)).getTarget();
          assertEquals("anything having existence (living or nonliving)",hyperSynset.getGloss());
        }
        else if (currHypernym.getGloss().equals("something that holds things, especially for transport or storage")) {

          hyperSynset = ((SemanticRelation)hyperSynset.getSemanticRelations(SemanticRelation.REL_HYPERNYM).get(0)).getTarget();
          assertEquals("an artifact (or system of artifacts) that is instrumental in accomplishing some end",hyperSynset.getGloss());

          hyperSynset = ((SemanticRelation)hyperSynset.getSemanticRelations(SemanticRelation.REL_HYPERNYM).get(0)).getTarget();
          assertEquals("a man-made object", hyperSynset.getGloss());

          hyperSynset = ((SemanticRelation)hyperSynset.getSemanticRelations(SemanticRelation.REL_HYPERNYM).get(0)).getTarget();
          assertEquals("a physical (tangible and visible) entity; \"it was full of rackets, balls and other objects\"",hyperSynset.getGloss());

          hyperSynset = ((SemanticRelation)hyperSynset.getSemanticRelations(SemanticRelation.REL_HYPERNYM).get(0)).getTarget();
          assertEquals("anything having existence (living or nonliving)",hyperSynset.getGloss());
        }
        else {
          fail();
        }
      }

      break;
    }

  }

  @Ignore
  @Test
  public void testWN_03() throws Exception {
    //test the presence of the WN files
    String wnConfigFile = (String)Gate.getUserConfig().
                          get(GateConstants.WORDNET_CONFIG_FILE);
    if(wnConfigFile == null){
      Err.prln("WordNet not present. Test aborted...");
      return;
    }

    //test hyponymy - check all direct hyponyms of a word
    //compare the result with the WN16 index files

    List<WordSense> senseList = wnMain.lookupWord("cup",WordNet.POS_NOUN);
    assertEquals(8,senseList.size());

    for (int i=0; i< senseList.size(); i++) {

      WordSense currSense = (WordSense)senseList.get(i);
      Synset currSynset = currSense.getSynset();
      assert currSynset != null;

      if (false == currSynset.getGloss().equals("a small open container usually used for drinking; \"he put the cup back in the saucer\"; \"the handle of the cup was missing\"")) {
        continue;
      }

      List<SemanticRelation> semRelations = currSynset.getSemanticRelations(SemanticRelation.REL_HYPONYM);
      assertNotNull(semRelations);
      assertEquals(9,semRelations.size());

      for (int j=0; j< semRelations.size(); j++ ) {
        SemanticRelation currHypoRel = (SemanticRelation)semRelations.get(j);

        assertEquals(SemanticRelation.REL_HYPONYM,currHypoRel.getType());
        assertEquals("~",currHypoRel.getSymbol());
        assertEquals(currSynset,currHypoRel.getSource());

        Synset currHyponym = currHypoRel.getTarget();
        assertNotNull(currHyponym);

        switch(j) {

          case 0:
            checkSynset(currHyponym,
                        "usually without a handle",
                        1);
            break;

          case 1:
            checkSynset(currHyponym,
                        "a bowl-shaped drinking vessel; especially the Eucharistic cup",
                        2);
            break;

          case 2:
            checkSynset(currHyponym,
                        "a cup from which coffee is drunk",
                        1);
            break;

          case 3:
            checkSynset(currHyponym,
                        "a paper cup for holding drinks",
                        3);
            break;

          case 4:
            checkSynset(currHyponym,
                        "cup to be passed around for the final toast after a meal",
                        1);
            break;

          case 5:
            checkSynset(currHyponym,
                        "a graduated cup used for measuring ingredients",
                        1);
            break;

          case 6:
            checkSynset(currHyponym,
                        "a drinking cup with a bar inside the rim to keep a man's mustache out of the drink",
                        2);
            break;

          case 7:
            checkSynset(currHyponym,
                        "an ancient Greek drinking cup; two handles and footed base",
                        1);
            break;

          case 8:
            checkSynset(currHyponym,
                        "a cup from which tea is drunk",
                        1);
            break;
        }
      }

    }
  }

  private void checkSynset(Synset s, String gloss, int numWords) {

    assertEquals(gloss,s.getGloss());

    List<WordSense> wordSenses = s.getWordSenses();
    assertEquals(numWords,wordSenses.size());
  }

/*
  public void testWN_01() throws Exception {

    IndexFileWordNetImpl wnMain = new IndexFileWordNetImpl();
    wnMain.setPropertyFile(new File("D:/PRJ/jwnl/file_properties.xml"));
    wnMain.init();

    Dictionary dict = wnMain.getJWNLDictionary();
    Assert.assertNotNull(dict);

    IndexWordSet iSet = dict.lookupAllIndexWords("cup");
    IndexWord[] arr =  iSet.getIndexWordArray();
    for (int i=0; i< arr.length; i++) {
      IndexWord iw = arr[i];
      net.didion.jwnl.data.Synset[] synsets = iw.getSenses();
      for (int j=0; j< synsets.length; j++) {
        net.didion.jwnl.data.Synset s = synsets[j];
//System.out.println("synset: "+s.toString());
//net.didion.jwnl.data.Word firstWord = s.getWord(0);
//System.out.println("0th word index is " + firstWord.getIndex());
        Synset ss = new SynsetImpl(s,wnMain.getJWNLDictionary());
        List rel = ss.getSemanticRelations();
      }
    }


System.out.println(iSet.size());
System.out.println(iSet);
  }
*/

  protected void setUp() throws Exception {

    String wnConfigFile = (String)Gate.getUserConfig().
                          get(GateConstants.WORDNET_CONFIG_FILE);
    if(wnConfigFile == null) return;
    if (null == wnMain) {
      wnMain = new JWNLWordNetImpl();
      wnMain.setPropertyUrl(new File(wnConfigFile).toURI().toURL());
      wnMain.init();
    }
  }

}