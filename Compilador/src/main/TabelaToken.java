package main;
import java.util.HashMap;
import java.util.Map;

public class TabelaToken {

	private static Map<String, String> tabelaToken = new HashMap<String, String>();
	static {
		tabelaToken.put("programa", "sprograma");
		tabelaToken.put("inicio", "sinicio");
		tabelaToken.put("fim", "sfim");
		tabelaToken.put("procedimento", "sprocedimento");
		tabelaToken.put("funcao", "sfuncao");
		tabelaToken.put("se", "sse");
		tabelaToken.put("entao", "sentao");
		tabelaToken.put("senao", "ssenao");
		tabelaToken.put("enquanto", "senquanto");
		tabelaToken.put("faca", "sfaca");
		tabelaToken.put(":=", "satribuicao");
		tabelaToken.put("escreva", "sescreva");
		tabelaToken.put("leia", "sleia");
		tabelaToken.put("var", "svar");
		tabelaToken.put("inteiro", "sinteiro");
		tabelaToken.put("booleano", "sbooleano");
		tabelaToken.put("identificador", "sidentificador");
		tabelaToken.put("numero", "snumero");
		tabelaToken.put(".", "sponto");
		tabelaToken.put(";", "sponto_virgula");
		tabelaToken.put(",", "svirgula");
		tabelaToken.put("(", "sabre_parenteses");
		tabelaToken.put(")", "sfecha_parenteses");
		tabelaToken.put(">", "smaior");
		tabelaToken.put(">=", "smaiorig");
		tabelaToken.put("=", "sig");
		tabelaToken.put("<", "smenor");
		tabelaToken.put("<=", "smenorig");
		tabelaToken.put("!=", "sdif");
		tabelaToken.put("+", "smais");
		tabelaToken.put("-", "smenos");
		tabelaToken.put("*", "smult");
		tabelaToken.put("div", "sdiv");
		tabelaToken.put("e", "se");
		tabelaToken.put("ou", "sou");
		tabelaToken.put("nao", "snao");
		tabelaToken.put(":", "sdoispontos");
	}
	
	public static String retornaSimbolo(String string) {
		return tabelaToken.get(string);
	}

}
