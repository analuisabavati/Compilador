package main;

import java.util.ArrayList;
import java.util.List;

public class AnalisadorSemantico {

	private static List<Simbolo> tabelaSimbolos = new ArrayList<>();

	public List<Simbolo> getTabelaSimbolos() {
		return tabelaSimbolos;
	}
	
	public static void insereTabelaSimbolos(String lexema, String tipo, String nivel, String rotulo, String tipoLexema) {
		Simbolo simbolo = new Simbolo();
		simbolo.setLexema(lexema);	
		simbolo.setTipo(tipo);
		simbolo.setNivel(nivel);
		simbolo.setRotulo(rotulo);
		tabelaSimbolos.add(simbolo);
	}
	
	public static void colocaTipoVariaveis(String tipo) {
		
	}
	
	public static boolean existeVariavelTabelaSimbolos(String lexema) {
		return false;
	}
	
	public static boolean existeChamadaFuncaoTabelaSimbolos(String lexema) {
		return false;
	}	
	
	public static boolean existeDeclaracaoProcedimentoTabelaSimbolos(String lexema) {
		return false;
	}
	
	public static boolean existeVariavelTabelaSimbolo(String lexema) {
		return false;
	}
	
	public static boolean naoExisteVariavelTabelaSimbolo(String lexema) {
		return !existeVariavelTabelaSimbolo(lexema);
	}

	public static boolean naoExisteDeclaracaoProcedimentoTabelaSimbolos(String lexema) {
		return !existeDeclaracaoProcedimentoTabelaSimbolos(lexema);
	}
	
}
