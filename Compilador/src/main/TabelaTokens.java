package main;
import java.util.HashMap;
import java.util.Map;

public class TabelaTokens {

	private static Map<String, String> tabelaTokens = new HashMap<String, String>();
	static {
		tabelaTokens.put("programa", "sprograma");
		tabelaTokens.put("inicio", "sinicio");
		tabelaTokens.put("fim", "sfim");
		tabelaTokens.put("procedimento", "sprocedimento");
		tabelaTokens.put("funcao", "sfuncao");
		tabelaTokens.put("se", "sse");
		tabelaTokens.put("entao", "sentao");
		tabelaTokens.put("senao", "ssenao");
		tabelaTokens.put("enquanto", "senquanto");
		tabelaTokens.put("faca", "sfaca");
		tabelaTokens.put(":=", "satribuicao");
		tabelaTokens.put("escreva", "sescreva");
		tabelaTokens.put("leia", "sleia");
		tabelaTokens.put("var", "svar");
		tabelaTokens.put("inteiro", "sinteiro");
		tabelaTokens.put("booleano", "sbooleano");
		tabelaTokens.put("identificador", "sidentificador");
		tabelaTokens.put("numero", "snumero");
		tabelaTokens.put(".", "sponto");
		tabelaTokens.put(";", "sponto_virgula");
		tabelaTokens.put(",", "svirgula");
		tabelaTokens.put("(", "sabre_parenteses");
		tabelaTokens.put(")", "sfecha_parenteses");
		tabelaTokens.put(">", "smaior");
		tabelaTokens.put(">=", "smaiorig");
		tabelaTokens.put("=", "sig");
		tabelaTokens.put("<", "smenor");
		tabelaTokens.put("<=", "smenorig");
		tabelaTokens.put("!=", "sdif");
		tabelaTokens.put("+", "smais");
		tabelaTokens.put("-", "smenos");
		tabelaTokens.put("*", "smult");
		tabelaTokens.put("div", "sdiv");
		tabelaTokens.put("e", "se");
		tabelaTokens.put("ou", "sou");
		tabelaTokens.put("nao", "snao");
		tabelaTokens.put(":", "sdoispontos");
	}
	
	public static String retornaSimbolo(String string) {
		return tabelaTokens.get(string);
	}

}
