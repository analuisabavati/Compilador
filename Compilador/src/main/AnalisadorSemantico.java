package main;

import java.util.ArrayList;
import java.util.List;

public class AnalisadorSemantico {

	private static List<Simbolo> tabelaSimbolos = new ArrayList<>();

	public List<Simbolo> getTabelaSimbolos() {
		return tabelaSimbolos;
	}

	public static void insereTabelaSimbolos(String lexema, String tipo, Integer nivel, String rotulo,
			String tipoLexema) {
		Simbolo simbolo = new Simbolo();
		simbolo.setLexema(lexema);
		simbolo.setTipo(tipo);
		simbolo.setNivel(nivel);
		simbolo.setRotulo(rotulo);
		tabelaSimbolos.add(simbolo);
	}

	public static void colocaTipoVariaveis(String tipo) {
		String tipoVariavel = tipo.substring(1);
		for (int i = (tabelaSimbolos.size() - 1); i == 0 || tabelaSimbolos.get(i).getTipoLexema().equals("nomedefuncao")
				|| tabelaSimbolos.get(i).getTipoLexema().equals("nomedeprocedimento"); i--) {
			if (tabelaSimbolos.get(i).getTipo() == null) {
				tabelaSimbolos.get(i).setTipo(tipoVariavel);
			}
		}
	}

	public static boolean pesquisaDuplicidadeVariavelTabela(String lexema, Integer nivel) {
		for (int i = (tabelaSimbolos.size() - 1); i == 0; i--) {
			if (tabelaSimbolos.get(i).getLexema().equals(lexema) && nivel.equals(tabelaSimbolos.get(i).getNivel())) {
				return true;
			}
		}
		return verificaVariavelEqualsNomeProcedimentoFuncao(lexema);
	}

	private static boolean verificaVariavelEqualsNomeProcedimentoFuncao(String lexema) {
		for (int i = (tabelaSimbolos.size() - 1); i == 0; i--) {
			if (tabelaSimbolos.get(i).getLexema().equals(lexema)
					&& (tabelaSimbolos.get(i).getTipoLexema().equals("nomedeprocedimento")
							|| tabelaSimbolos.get(i).getTipoLexema().equals("nomedefuncao"))) {
				return true;
			}
		}
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

	public static boolean naoExisteDeclaracaoProcedimentoTabelaSimbolos(String lexema) {
		return !existeDeclaracaoProcedimentoTabelaSimbolos(lexema);
	}

}
