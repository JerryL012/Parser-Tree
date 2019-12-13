import computation.contextfreegrammar.*;

import java.util.ArrayList;
import java.util.HashSet;

public class MyGrammar {
	public static ContextFreeGrammar makeGrammar() {
		// You can write your code here to make the context-free grammar from the assignment
		// varibales
		Variable E0 = new Variable("E0");
		Variable E = new Variable('E');
		Variable T = new Variable('T');
		Variable F = new Variable('F');
		Variable Z = new Variable('Z');
		Variable Y = new Variable('Y');
		Variable A = new Variable('A');
		// variables to terminal by 1 step
		Variable D = new Variable('D');
		Variable Q = new Variable('Q');
		Variable M = new Variable('M');
		Variable L = new Variable('L');
		Variable R = new Variable('R');

		HashSet<Variable> variables = new HashSet<>();
		variables.add(E0);
		variables.add(E);
		variables.add(T);
		variables.add(F);
		variables.add(Z);
		variables.add(Y);
		variables.add(A);
		variables.add(D);
		variables.add(Q);
		variables.add(M);
		variables.add(L);
		variables.add(R);


		// terminals
		Terminal lBracket = new Terminal('(');
		Terminal rBracket = new Terminal(')');
		Terminal minus = new Terminal('-');
		Terminal plus = new Terminal('+');
		Terminal and = new Terminal('&');
		Terminal p = new Terminal('p');
		Terminal r = new Terminal('r');

		HashSet<Terminal> terminals = new HashSet<>();
		terminals.add(lBracket);
		terminals.add(rBracket);
		terminals.add(minus);
		terminals.add(plus);
		terminals.add(and);
		terminals.add(p);
		terminals.add(r);

		// rules
		// E0
		ArrayList<Rule> rules = new ArrayList<>();
		rules.add(new Rule(E0, new Word(E, A)));
		rules.add(new Rule(E0, new Word(Y, F)));
		rules.add(new Rule(E0, new Word(Z, R)));
		rules.add(new Rule(E0, new Word(M, F)));
		rules.add(new Rule(E0, new Word(p)));
		rules.add(new Rule(E0, new Word(r)));
		// E
		rules.add(new Rule(E, new Word(E, A)));
		rules.add(new Rule(E, new Word(Y, F)));
		rules.add(new Rule(E, new Word(Z, R)));
		rules.add(new Rule(E, new Word(M, F)));
		rules.add(new Rule(E, new Word(p)));
		rules.add(new Rule(E, new Word(r)));
		// T
		rules.add(new Rule(T, new Word(Y, F)));
		rules.add(new Rule(T, new Word(Z, R)));
		rules.add(new Rule(T, new Word(M, F)));
		rules.add(new Rule(T, new Word(p)));
		rules.add(new Rule(T, new Word(r)));
		// F
		rules.add(new Rule(F, new Word(Z, R)));
		rules.add(new Rule(F, new Word(M, F)));
		rules.add(new Rule(F, new Word(p)));
		rules.add(new Rule(F, new Word(r)));
		// Z
		rules.add(new Rule(Z, new Word(L, E)));
		// Y
		rules.add(new Rule(Y, new Word(T, Q)));
		// A
		rules.add(new Rule(A, new Word(D, T)));
		// D
		rules.add(new Rule(D, new Word(plus)));
		// Q
		rules.add(new Rule(Q, new Word(and)));
		// M
		rules.add(new Rule(M, new Word(minus)));
		// L
		rules.add(new Rule(L, new Word(lBracket)));
		// R
		rules.add(new Rule(R, new Word(rBracket)));

		ContextFreeGrammar cfg = new ContextFreeGrammar(variables, terminals, rules, E0);
		return cfg;
	}
}
