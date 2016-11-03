package main;

import java.util.ArrayList;
import java.util.List;

public class AnalisadorSemantico {

	private List<Simbolo> tabelaSimbolos = new ArrayList<>();

	public List<Simbolo> getTabelaSimbolos() {
		return tabelaSimbolos;
	}
	
	public void insereTabelaSimbolos(String lexema) {
		Simbolo simbolo = new Simbolo();
		simbolo.setSimbolo(lexema);	
	}
	
	public void colocaTipoVariaveis(String tipo) {
		
	}
	
	public boolean existeVariavelNaTabelaSimbolos(String lexema) {
		return false;
	}
	
	public boolean existeChamadaFuncaoNaTabelaSimbolos(String lexema) {
		return false;
	}
	
	public boolean naoExisteDeclaracaoProcedimentoNatabelaDeSimbolos(String lexema) {
		return !existeDeclaracaoProcedimentoNatabelaDeSimbolos(lexema);
	}
	
	public boolean existeDeclaracaoProcedimentoNatabelaDeSimbolos(String lexema) {
		return false;
	}
	
	public boolean naoExisteVariavelNaTabelaSimbolo(String lexema) {
		return !existeVariavelNaTabelaSimbolo(lexema);
	}

	public boolean existeVariavelNaTabelaSimbolo(String lexema) {
		return false;
	}
	
}
