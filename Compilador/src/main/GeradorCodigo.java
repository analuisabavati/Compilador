package main;

import java.io.FileWriter;
import java.io.PrintWriter;
import static main.AnalisadorSemantico.*;

public class GeradorCodigo {
	static FileWriter arq;
	static PrintWriter gravarArq;

	public static void main(String[] args) throws Exception {
		arq = new FileWriter("d:\\tabuada.txt");
		gravarArq = new PrintWriter(arq);
		gravarArq.print("");
	}

	public static void gera(String comando) {
		gravarArq.printf(comando);
		gravarArq.printf("\n");
	}
	
	public static String geraDalloc() {
		int i = tabelaSimbolos.size() - 1;
		int contadorVariaveis = 0;
		int ultimoEndereco = 0;
		
		while (i >= 0) {
			if (tabelaSimbolos.get(i).getTipoLexema().equals(NOME_DE_VARIAVEL)) {
				contadorVariaveis++;
				ultimoEndereco = tabelaSimbolos.get(i).getEndereco();
			} else {
				break;
			}
		}
		
		gera("DALLOC "+ultimoEndereco+","+contadorVariaveis);
		
		return null;
	}
	
	

}
