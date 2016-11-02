package main;
import java.util.HashMap;
import java.util.Map;

public class TabelaToken {

	private static Map<String, String> tabelaSimbolos = new HashMap<String, String>();
	static {
		tabelaSimbolos.put("programa", "sprograma");
		tabelaSimbolos.put("inicio", "sinicio");
		tabelaSimbolos.put("fim", "sfim");
		tabelaSimbolos.put("procedimento", "sprocedimento");
		tabelaSimbolos.put("funcao", "sfuncao");
		tabelaSimbolos.put("se", "sse");
		tabelaSimbolos.put("entao", "sentao");
		tabelaSimbolos.put("senao", "ssenao");
		tabelaSimbolos.put("enquanto", "senquanto");
		tabelaSimbolos.put("faca", "sfaca");
		tabelaSimbolos.put(":=", "satribuicao");
		tabelaSimbolos.put("escreva", "sescreva");
		tabelaSimbolos.put("leia", "sleia");
		tabelaSimbolos.put("var", "svar");
		tabelaSimbolos.put("inteiro", "sinteiro");
		tabelaSimbolos.put("booleano", "sbooleano");
		tabelaSimbolos.put("identificador", "sidentificador");
		tabelaSimbolos.put("numero", "snumero");
		tabelaSimbolos.put(".", "sponto");
		tabelaSimbolos.put(";", "sponto_virgula");
		tabelaSimbolos.put(",", "svirgula");
		tabelaSimbolos.put("(", "sabre_parenteses");
		tabelaSimbolos.put(")", "sfecha_parenteses");
		tabelaSimbolos.put(">", "smaior");
		tabelaSimbolos.put(">=", "smaiorig");
		tabelaSimbolos.put("=", "sig");
		tabelaSimbolos.put("<", "smenor");
		tabelaSimbolos.put("<=", "smenorig");
		tabelaSimbolos.put("!=", "sdif");
		tabelaSimbolos.put("+", "smais");
		tabelaSimbolos.put("-", "smenos");
		tabelaSimbolos.put("*", "smult");
		tabelaSimbolos.put("div", "sdiv");
		tabelaSimbolos.put("e", "se");
		tabelaSimbolos.put("ou", "sou");
		tabelaSimbolos.put("nao", "snao");
		tabelaSimbolos.put(":", "sdoispontos");
	}
	
	public static String retornaSimbolo(String string) {
		return tabelaSimbolos.get(string);
	}

}
