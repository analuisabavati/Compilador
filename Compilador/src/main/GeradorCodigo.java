package main;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import static main.AnalisadorSemantico.*;

public class GeradorCodigo {
	static FileWriter arq;
	static PrintWriter gravarArq;

	public static void main(String[] args) throws Exception {
		arq = new FileWriter("C:\\Users\\AnaLuisa\\Desktop\\codigoGerado.txt");
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
	
	

}
