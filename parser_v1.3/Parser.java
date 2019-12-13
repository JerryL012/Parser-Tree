import computation.contextfreegrammar.*;
import computation.parser.*;
import computation.parsetree.*;
import computation.derivation.*;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Parser implements IParser {
  @Override
  // get all the derivarions in 2n-1 steps
  public Derivation getDerivarion(ContextFreeGrammar cfg, Word w){
    int derivationLength;
    if (w.length() == 0) {
      derivationLength = 1;
    }else {
      derivationLength = 2*w.length()-1;
    }


    List<Derivation> derivations = new ArrayList<>();
    Derivation start = new Derivation(new Word(cfg.getStartVariable()));
    derivations.add(new Derivation(start));
    int steps = 0;

    while (steps<derivationLength) {
      List<Derivation>newDerivations = new ArrayList<>();
      for (Derivation derivation: derivations){
        Word lastWord = derivation.getLatestWord();
        for (Rule rules: cfg.getRules()){
          Variable leftVariable = rules.getVariable();
          int count = lastWord.count(leftVariable);
          for (int i=0;i<count;i++){
            int index = lastWord.indexOfNth(leftVariable,i);
            Word newWord = lastWord.replace(index, rules.getExpansion());
            Derivation newDerivation = new Derivation(derivation);
            newDerivation.addStep(newWord, rules, index);
            newDerivations.add(newDerivation);
          }
        }
      }
      steps++;
      derivations = newDerivations;
    }

    // aim to return only the final words which match the rules in n steps
    List<Word> finalWords = derivations.stream().map(Derivation::getLatestWord).collect(Collectors.toList());
    int index = finalWords.indexOf(w);
    if(index == -1){
      return null;
    }
    return derivations.get(index);
  }



  @Override
  public boolean isInLanguage(ContextFreeGrammar cfg, Word w){
    Derivation derivations = getDerivarion(cfg, w);
    // print out to see
    if (derivations != null){
//      for (Step item: derivations){
//        System.out.println(item);
//      }
      return true;
    }else{
      return false;
    }
  }

  @Override
  public ParseTreeNode generateParseTree(ContextFreeGrammar cfg, Word w) {
    Derivation derivation = getDerivarion(cfg, w);
    if (derivation == null){
      return null;
    }else{
      return createParseTree(derivation);
    }
  }


  private ParseTreeNode createParseTree(Derivation derivation){
    Word finalWord = derivation.getLatestWord();  // finalWord is "p+r"
    LinkedList<ParseTreeNode> backwardsDerive = new LinkedList<>();
    List<Word> noChildNodes = new ArrayList<>();
    List<ParseTreeNode> oneChildNodes = new ArrayList<>();
    List<ParseTreeNode> twoChildNodes = new ArrayList<>();
    List<ParseTreeNode> Nodes = new ArrayList<>();

    // These nodes have no children
    for(int i = 0; i < finalWord.length(); i++){
      backwardsDerive.add(new ParseTreeNode(finalWord.get(i)));
      noChildNodes.add(new Word(finalWord.get(i)));
    }
    noChildNodes=null;
//    System.out.println(backwardsDerive);
    for(Step s : derivation) {
      if(s.isStartSymbol()) {
        break;
      }else if(s.getRule().getExpansion().length() == 0){
        assert(finalWord.equals(Word.emptyWord));
        return ParseTreeNode.emptyParseTree(s.getRule().getVariable());
      }else{
//        System.out.println(s);
        // These nodes have exactly one child
        int len = s.getRule().getExpansion().length();
        if (len != 2){
          for (ParseTreeNode noChildNode: backwardsDerive){
            if (s.getRule().getExpansion().equals(new Word(noChildNode.getSymbol()))){
              oneChildNodes.add(new ParseTreeNode(s.getRule().getVariable(), noChildNode));
            }
          }
        }
        if (len == 2) {
          // These nodes have exactly two children
          for (ParseTreeNode oneChild : oneChildNodes) {
            Word one = new Word(s.getRule().getExpansion().get(0).toString());
            Word two = new Word(s.getRule().getExpansion().get(1).toString());
            if (one.equals(new Word(oneChild.getSymbol()))) {
              for (ParseTreeNode oneChildt : oneChildNodes) {
                if (two.equals(new Word(oneChildt.getSymbol()))) {
                  twoChildNodes.add(new ParseTreeNode(s.getRule().getVariable(), oneChild, oneChildt));
                }
              }
            }
          }
        }
        // start symbol
        if (s.getRule().getVariable().equals(new Variable("E0"))){
          Nodes.addAll(backwardsDerive);
          Nodes.addAll(oneChildNodes);
          Nodes.addAll(twoChildNodes);

          backwardsDerive=null;
          oneChildNodes=null;
          twoChildNodes=null;

          Word left = new Word(s.getRule().getExpansion().get(0).toString());
          Word right = new Word(s.getRule().getExpansion().get(1).toString());
          for (ParseTreeNode each: Nodes){
            if (left.equals(new Word(each.getSymbol()))){
              for (ParseTreeNode eacht: Nodes){
                if(right.equals(new Word((eacht.getSymbol())))){
                  Nodes.add(new ParseTreeNode(s.getRule().getVariable(), each, eacht));
                  return Nodes.get(Nodes.size()-1);
                }
              }
            }
          }
        }
      }
    }
    return null;
  }

//  private void printParseTree(ContextFreeGrammar cfg, Word w){
//    ParseTreeNode tree = this.generateParseTree(cfg, w);
//    tree.print();
//  }


}