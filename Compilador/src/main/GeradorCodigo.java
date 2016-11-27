package main;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import static main.AnalisadorSemantico.*;

public class GeradorCodigo {
	static FileWriter arq;
	static PrintWriter gravarArq;

	public static void main(String[] args) throws Exception {
		arq = new FileWriter("C:\\Users\\leona\\Desktop\\codigoGerado.txt");
		gravarArq = new PrintWriter(arq);
		gravarArq.print("");
	}

	public static void gera(String comando) {
		gravarArq.write(comando);
		gravarArq.write("\n");
	}
	
	public static void fechaArquivo() throws IOException {
		arq.close();
	}
	
	public static String geraDalloc(Integer nivel) {
		int i = tabelaSimbolos.size() - 1;
		int contadorVariaveis = 0;
		int ultimoEndereco = 0;

		while (i >= 0) {
			if (tabelaSimbolos.get(i).getNivel() == nivel &&
					tabelaSimbolos.get(i).getTipoLexema().equals(NOME_DE_VARIAVEL)) {
				contadorVariaveis++;
				ultimoEndereco = tabelaSimbolos.get(i).getEndereco();
			}
			i--;
		}
		
		gera("DALLOC "+ultimoEndereco+","+contadorVariaveis);
		
		return null;
	}
	
	

}
