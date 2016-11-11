package main;

import java.util.ArrayList;
import java.util.List;

public class AnalisadorSemantico {

	private List<Simbolo> tabelaSimbolos = new ArrayList<>();

	public List<Simbolo> getTabelaSimbolos() {
		return tabelaSimbolos;
	}
	
	public void insereTabelaSimbolos(String lexema, String tipo, String nivel, String rotulo) {
		Simbolo simbolo = new Simbolo();
		simbolo.setLexema(lexema);	
		simbolo.setTipo(tipo);
		simbolo.setNivel(nivel);
		simbolo.setRotulo(rotulo);
		tabelaSimbolos.add(simbolo);
	}
	
	public void colocaTipoVariaveis(String tipo) {
		
	}
	
	public boolean existeVariavelTabelaSimbolos(String lexema) {
		return false;
	}
	
	public boolean existeChamadaFuncaoTabelaSimbolos(String lexema) {
		return false;
	}	
	
	public boolean existeDeclaracaoProcedimentoTabelaSimbolos(String lexema) {
		return false;
	}
	
	public boolean existeVariavelTabelaSimbolo(String lexema) {
		return false;
	}
	
	public boolean naoExisteVariavelTabelaSimbolo(String lexema) {
		return !existeVariavelTabelaSimbolo(lexema);
	}

	public boolean naoExisteDeclaracaoProcedimentoTabelaSimbolos(String lexema) {
		return !existeDeclaracaoProcedimentoTabelaSimbolos(lexema);
	}
	
}
