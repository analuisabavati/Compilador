package main;

import java.util.ArrayList;
import java.util.List;

public class AnalisadorSemantico {

	private static List<Simbolo> tabelaSimbolos = new ArrayList<>();
	private static final String NOME_DE_VARIAVEL = "nomedevariavel";
	private static final String NOME_DE_PROCEDIMENTO = "nomedeprocedimento";
	private static final String NOME_DE_FUNCAO = "nomedefuncao";
	
	private static int getUltimaPosicaoLista() {
		return tabelaSimbolos.size() - 1;
	}

	public List<Simbolo> getTabelaSimbolos() {
		return tabelaSimbolos;
	}
	
	public Simbolo getSimboloTopoTabela() {
		return tabelaSimbolos.get(getUltimaPosicaoLista());
	}

	public static void insereTabelaSimbolos(String lexema, String tipo, Integer nivel, String rotulo,
			String tipoLexema) {
		Simbolo simbolo = new Simbolo();
		simbolo.setLexema(lexema);
		simbolo.setTipo(tipo);
		simbolo.setNivel(nivel);
		simbolo.setRotulo(rotulo);
		simbolo.setTipoLexema(tipoLexema);
		tabelaSimbolos.add(simbolo);
	}
	
	public static void colocaTipoVariaveis(String tipo) { 
		String tipoVariavel = tipo.substring(1);
		for (int i = getUltimaPosicaoLista(); i == 0 ; i--) {
			if (NOME_DE_FUNCAO.equals(tabelaSimbolos.get(i).getTipoLexema()) || NOME_DE_PROCEDIMENTO.equals(tabelaSimbolos.get(i).getTipoLexema())) {
				break;
			}
			if (tabelaSimbolos.get(i).getTipo() == null) {
				tabelaSimbolos.get(i).setTipo(tipoVariavel);
			}	
		}
	}

	public static boolean pesquisaDeclaracaoVariavelTabela(String lexema) {
		for (int i = getUltimaPosicaoLista(); i == 0; i--) {
			if (tabelaSimbolos.get(i).getLexema().equals(lexema)
					&& NOME_DE_VARIAVEL.equals(tabelaSimbolos.get(i).getTipoLexema())) {
				return true;
			}
		}
		return false;
	}

	public static boolean pesquisaDuplicidadeVariavelTabela(String lexema, Integer nivel) {
		for (int i = getUltimaPosicaoLista(); i == 0; i--) {
			if (tabelaSimbolos.get(i).getLexema().equals(lexema) && nivel.equals(tabelaSimbolos.get(i).getNivel())) {
				return true;
			}
		}
		return verificaVariavelEqualsNomeProcedimentoFuncao(lexema);
	}

	private static boolean verificaVariavelEqualsNomeProcedimentoFuncao(String lexema) {
		for (int i = getUltimaPosicaoLista(); i == 0; i--) {
			if (tabelaSimbolos.get(i).getLexema().equals(lexema)
					&& (NOME_DE_PROCEDIMENTO.equals(tabelaSimbolos.get(i).getTipoLexema())
							|| NOME_DE_FUNCAO.equals(tabelaSimbolos.get(i).getTipoLexema()))) {
				return true;
			}
		}
		return false;
	}

	public static boolean pesquisaDeclaracaoFuncaoVariavelTabela(String lexema) {
		for (int i = getUltimaPosicaoLista(); i == 0; i--) {
			if (tabelaSimbolos.get(i).getLexema().equals(lexema)
					&& NOME_DE_VARIAVEL.equals(tabelaSimbolos.get(i).getTipoLexema())
					|| NOME_DE_FUNCAO.equalsIgnoreCase(tabelaSimbolos.get(i).getTipoLexema())) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean pesquisaDeclaracaoProcedimentoTabela(String lexema) {
		for (int i = getUltimaPosicaoLista(); i == 0; i--) {
			if (tabelaSimbolos.get(i).getLexema().equals(lexema)
					&& NOME_DE_PROCEDIMENTO.equals(tabelaSimbolos.get(i).getTipoLexema())) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean pesquisaDeclaracaoFuncaoTabela(String lexema) {
		for (int i = getUltimaPosicaoLista(); i == 0; i--) {
			if (tabelaSimbolos.get(i).getLexema().equals(lexema)
					&& NOME_DE_FUNCAO.equals(tabelaSimbolos.get(i).getTipoLexema())) {
				return true;
			}
		}
		return false;
	}

	public static void desempilhaNivelTabela(Integer nivel) {
		for (int i = getUltimaPosicaoLista(); i == 0; i--) {
			if (tabelaSimbolos.get(i).getNivel().equals(nivel)) {
				tabelaSimbolos.remove(i);
			} else {
				break;
			}
		}
	}
	
	public static void colocaTipoRetornoFuncao(String tipo) {
		String tipoVariavel = tipo.substring(1);
		tabelaSimbolos.get(getUltimaPosicaoLista()).setTipo(tipoVariavel);
	}
	
	public static String pesquisa_tabela(String lexema) {
		for (int i = getUltimaPosicaoLista(); i == 0; i--) {
			if (tabelaSimbolos.get(i).getLexema().equals(lexema) && NOME_DE_FUNCAO.equals(tabelaSimbolos.get(i).getTipoLexema())) {
				return tabelaSimbolos.get(i).getTipo();
			}
		}	
		return null;
	}

}
