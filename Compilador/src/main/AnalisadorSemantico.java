package main;

import java.util.ArrayList;
import java.util.List;
import static main.Operadores.*;

public class AnalisadorSemantico {
	
	private static final String NOME_DE_VARIAVEL = "nomedevariavel";
	private static final String NOME_DE_PROCEDIMENTO = "nomedeprocedimento";
	private static final String NOME_DE_FUNCAO = "nomedefuncao";
	
	private static List<Simbolo> tabelaSimbolos = new ArrayList<>();
	
	private static List<String> pilhaPosfixo = new ArrayList<>();
	private static List<String> filaPosfixo = new ArrayList<>();

	private static int getUltimaPosicaoLista() {
		return tabelaSimbolos.size() - 1;
	}

	public static List<Simbolo> getTabelaSimbolos() {
		return tabelaSimbolos;
	}
	
	public static void desempilhaPilhaParenteses() {
		int i = pilhaPosfixo.size() - 1;
		while (i >= 0) {
			if (!pilhaPosfixo.get(i).equals("(")){
				adicionaFilaPosfixo(pilhaPosfixo.remove(i));	
			} else {
				pilhaPosfixo.remove(i);
				break;
			}
			i--;
		}
	}
	
	public static  Simbolo getSimboloTopoTabela() {
		return tabelaSimbolos.get(getUltimaPosicaoLista());
	}
	
	public static void adicionaFilaPosfixo(String lexema) {
		filaPosfixo.add(lexema);
	}
	
	public static void adicionaPilhaPosfixo(String lexemaParametro) {
		int i = pilhaPosfixo.size() - 1;
		int predenciaParametro = getPrecedenciaOperadores(lexemaParametro);
		int predenciaPilha;
		while (i >= 0) {
			predenciaPilha = getPrecedenciaOperadores(pilhaPosfixo.get(i));
			if(predenciaPilha >= predenciaParametro){
				adicionaFilaPosfixo(pilhaPosfixo.remove(i));	
			}else {
				break;
			}
			i--;
		}
		pilhaPosfixo.add(lexemaParametro);
	}

	public static Simbolo getSimbolo(String lexema) {
		for (int i = getUltimaPosicaoLista(); i == 0; i--) {
			if (tabelaSimbolos.get(i).getLexema().equals(lexema)
					&& NOME_DE_VARIAVEL.equals(tabelaSimbolos.get(i).getTipoLexema())) {
				return tabelaSimbolos.get(i);
			}
		}
		return null;
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
			if (NOME_DE_FUNCAO.equals(tabelaSimbolos.get(i).getTipoLexema()) 
					|| NOME_DE_PROCEDIMENTO.equals(tabelaSimbolos.get(i).getTipoLexema())) {
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
			if (tabelaSimbolos.get(i).getNivel().equals(nivel) && (NOME_DE_PROCEDIMENTO.equals(tabelaSimbolos.get(i).getTipoLexema())
					|| NOME_DE_FUNCAO.equals(tabelaSimbolos.get(i).getTipoLexema()))) {
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

	/*
	 * Retorna o tipo da expressao
	 */
	public static String analisaPosfixo() {
		//TODO:  print fila pos fixo
		
		return null;
	}
	

	public static void verificaTipoBooleano(String tipo, Token token) throws Exception {
		if(!"booleano".equals(tipo)) {
			throw new Exception("Erro na linha " + token.getLinha()
			+ " . Espera-se que o tipo de retorno da expressao seja booleano.");
		}
	}
}
