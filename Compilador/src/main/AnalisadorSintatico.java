package main;

import static main.AnalisadorLexico.lexico;
import static main.AnalisadorSemantico.adicionaFilaPosfixo;
import static main.AnalisadorSemantico.adicionaPilhaPosfixo;
import static main.AnalisadorSemantico.analisaPosfixo;
import static main.AnalisadorSemantico.colocaTipoRetornoFuncao;
import static main.AnalisadorSemantico.colocaTipoVariaveis;
import static main.AnalisadorSemantico.desempilhaNivelTabela;
import static main.AnalisadorSemantico.desempilhaPilhaParenteses;
import static main.AnalisadorSemantico.getEnderecoVariavel;
import static main.AnalisadorSemantico.getSimboloVariavelFuncao;
import static main.AnalisadorSemantico.getTipoFuncaoVariavel;
import static main.AnalisadorSemantico.insereTabelaSimbolos;
import static main.AnalisadorSemantico.pesquisaDeclaracaoFuncaoTabela;
import static main.AnalisadorSemantico.pesquisaDeclaracaoFuncaoVariavelTabela;
import static main.AnalisadorSemantico.pesquisaDeclaracaoProcedimentoTabela;
import static main.AnalisadorSemantico.pesquisaDeclaracaoVariavelTabela;
import static main.AnalisadorSemantico.pesquisaDuplicidadeVariavelTabela;
import static main.AnalisadorSemantico.pesquisa_tabela;
import static main.AnalisadorSemantico.verificaTipoBooleano;
import static main.AnalisadorSemantico.*;
import static main.GeradorCodigo.*;

import java.util.ArrayList;
import java.util.List;

public class AnalisadorSintatico {

	private static Integer nivel = 0;
	private static Token tokenAnteriorAtribuicao = null;
	private static Token tokenAnteriorExpressao = null;

	private static List<List<Retorno>> listaFuncoesDeclaradas = new ArrayList<>();
	private static int nivelRetorno = 0;
	private static boolean inFuncao = false;
	private static boolean inProcedimento = false;
	private static int controleProcedimento = 0;
	private static int rotulo = 1;
	public static int enderecoMemoria = 1;
	
	public static void analisadorSintatico() throws Exception {

		zeraVariaveis();
		inFuncao = false;
		listaFuncoesDeclaradas.clear();
		nivel = 0;
		nivelRetorno = 0;
		tokenAnteriorAtribuicao = null;
		tokenAnteriorExpressao = null;
		rotulo = 1;
		enderecoMemoria = 1;

		GeradorCodigo.main(null);
		AnalisadorLexico.main(null);
		Token token = lexico();

		if (token.getSimbolo().equals("sprograma")) {
			gera("START");
			token = lexico();
			if (token.getSimbolo().equals("sidentificador")) {
				insereTabelaSimbolos(token.getLexema(), null, nivel, "L"+rotulo,
						"nomedeprograma", null);
				token = lexico();
				if (token.getSimbolo().equals("sponto_virgula")) {
					token = analisaBloco(token);
					if (token.getSimbolo().equals("sponto")) {
						try {
							gera("HLT");
							fechaArquivo();
							token = lexico();
							throw new Exception(
									"Não é permitido continuar o código após o 'fim.'.");
						} catch (Exception e) {
							if (e.getMessage()
									.equals("Não é permitido continuar o código após o 'fim.'.")) {
								throw new Exception(
										"Não é permitido continuar o código após o 'fim.'.");
							} else {
								throw new Exception("Arquivo lido com sucesso.");
							}
						}
					} else {
						throw new Exception(
								"Erro: Espera-se um ponto final após a palavra 'fim'");
					}
				} else {
					throw new Exception(
							"Erro na linha "
									+ token.getLinha()
									+ " . Espera-se um ponto e virgula no final após o identificador. \n Ultimo token lido: "
									+ token.getLexema());
				}
			} else {
				throw new Exception(
						"Erro na linha "
								+ token.getLinha()
								+ " . Espera-se identificador após a palavra reservada 'programa'. \n Ultimo token lido: "
								+ token.getLexema());
			}
		} else {
			throw new Exception(
					"Erro na linha "
							+ token.getLinha()
							+ " . Espera-se que um programa começe com a palavra 'programa'. \n Ultimo token lido: "
							+ token.getLexema());
		}

	}

	private static Token analisaBloco(Token token) throws Exception {
		token = lexico();
		token = analisaEtapaVariaveis(token);
		token = analisaSubrotinas(token);
		token = analisaComandos(token);

		desempilhaNivelTabela(nivel);
		nivel--;
		return token;
	}

	private static Token analisaEtapaVariaveis(Token token) throws Exception {
		if (token.getSimbolo().equals("svar")) {
			token = lexico();
			if (token.getSimbolo().equals("sidentificador")) {
				do {
					token = analisaVariaveis(token);
					if (token.getSimbolo().equals("sponto_virgula")) {
						token = lexico();
					} else {
						throw new Exception(
								"Erro na linha "
										+ token.getLinha()
										+ " . Espera-se um ponto e virgula após o último identificador. \n Ultimo token lido: "
										+ token.getLexema());
					}
				} while (token.getSimbolo().equals("sidentificador"));
			} else {
				throw new Exception(
						"Erro na linha "
								+ token.getLinha()
								+ " . Espera-se um identificador após a palavra 'var'. \n Ultimo token lido: "
								+ token.getLexema());
			}
		}
		return token;
	}

	private static Token analisaVariaveis(Token token) throws Exception {
		
		int enderecoInicioAlloc = enderecoMemoria;
		int contadorVariaveis = 0;
		
		do {
			if (token.getSimbolo().equals("sidentificador")) {
				if (!pesquisaDuplicidadeVariavelTabela(token.getLexema(), nivel)) {
					contadorVariaveis++;
					insereTabelaSimbolos(token.getLexema(), null, nivel, null,
							"nomedevariavel", enderecoMemoria);	
					enderecoMemoria++;
					token = lexico();
					if (token.getSimbolo().equals("svirgula") || token.getSimbolo().equals("sdoispontos")) {
						if (token.getSimbolo().equals("svirgula")) {
							token = lexico();
							if (token.getSimbolo().equals("sdoispontos")) {
								throw new Exception(
										"Erro na linha "
												+ token.getLinha()
												+ " . Espera-se um identificador após uma virgula.\n Ultimo token lido: "
												+ token.getLexema());
							}
						}
					} else {
						throw new Exception(
								"Erro na linha "
										+ token.getLinha()
										+ " . Espera-se uma virgula ou dois pontos após um identificador.\n Ultimo token lido: "
										+ token.getLexema());
					}
				} else {
					throw new Exception(
							"Erro na linha "
									+ token.getLinha()
									+ ". Já existe uma variavel, função ou procedimento com esse nome.");
				}
			} else {
				throw new Exception(
						"Erro na linha "
								+ token.getLinha()
								+ " . Espera-se um identificador.\n Ultimo token lido: "
								+ token.getLexema());
			}
		} while (!token.getSimbolo().equals("sdoispontos"));	
		gera("ALLOC "+enderecoInicioAlloc+","+contadorVariaveis);
		return analisaTipo(lexico());
	}

	private static Token analisaTipo(Token token) throws Exception {
		if (!token.getSimbolo().equals("sinteiro") && !token.getSimbolo().equals("sbooleano")) {
			throw new Exception(
					"Erro na linha "
							+ token.getLinha()
							+ ". Apenas é permitido tipo inteiro ou booleano. \n Token lido: "
							+ token.getLexema());
		}
		colocaTipoVariaveis(token.getSimbolo());
		return lexico();
	}

	private static Token analisaComandos(Token token) throws Exception {
		if (token.getSimbolo().equals("sinicio")) {
			token = lexico();
			token = analisaComandoSimples(token);
			while (!token.getSimbolo().equals("sfim")) {
				if (token.getSimbolo().equals("sponto_virgula")) {
					token = lexico();
					if (!token.getSimbolo().equals("sfim")) {
						token = analisaComandoSimples(token);
					}
				} else {
					throw new Exception(
							"Erro na linha "
									+ token.getLinha()
									+ ". Espera-se um ponto e vírgula após um comando. \nÚltimo token lido: "
									+ token.getLexema());
				}
			}
			return lexico();
		} else {
			throw new Exception(
					"Erro na linha "
							+ token.getLinha()
							+ ". Espera-se a palavra 'inicio' para iniciar um comando. \nÚltimo token lido: "
							+ token.getLexema());
		}
	}

	private static Token analisaComandoSimples(Token token) throws Exception {
		if (token.getSimbolo().equals("sidentificador")) {
			tokenAnteriorAtribuicao = token;
			return analisaAtribuicaoChamadaProcedimento(token);
		} else if (token.getSimbolo().equals("sse")) {
			if (inFuncao) {
				nivelRetorno++;
				listaFuncoesDeclaradas.get(listaFuncoesDeclaradas.size() - 1).add(new Retorno(token.getLexema(), false, nivelRetorno));
			}
			return analisaSe(token);
		} else if (token.getSimbolo().equals("senquanto")) {
			if (inFuncao && listaFuncoesDeclaradas.get(listaFuncoesDeclaradas.size() - 1).get(listaFuncoesDeclaradas.get(listaFuncoesDeclaradas.size() - 1).size() - 1).isRetornado()) {
				throw new Exception("Erro na linha " + token.getLinha()
						+ ". Comando " + token.getLexema()
						+ " inalcançável. Já existe um retorno.");
			}
			return analisaEnquanto(token);
		} else if (token.getSimbolo().equals("sleia")) {
			if (inFuncao && listaFuncoesDeclaradas.get(listaFuncoesDeclaradas.size() - 1).get(listaFuncoesDeclaradas.get(listaFuncoesDeclaradas.size() - 1).size() - 1).isRetornado()) {
				throw new Exception("Erro na linha " + token.getLinha()
						+ ". Comando " + tokenAnteriorAtribuicao.getLexema()
						+ " inalcançável. Já existe um retorno.");
			}
			return analisaLeia(token);
		} else if (token.getSimbolo().equals("sescreva")) {
			if (inFuncao && listaFuncoesDeclaradas.get(listaFuncoesDeclaradas.size() - 1).get(listaFuncoesDeclaradas.get(listaFuncoesDeclaradas.size() - 1).size() - 1).isRetornado()) {
				throw new Exception("Erro na linha " + token.getLinha()
						+ ". Comando " + token.getLexema()
						+ " inalcançável. Já existe um retorno.");
			}
			return analisaEscreva(token);
		} else {
			return analisaComandos(token);
		}
	}

	private static Token analisaAtribuicaoChamadaProcedimento(Token token) throws Exception {
		token = lexico();
		if (token.getSimbolo().equals("satribuicao")) {
			if (pesquisaDeclaracaoFuncaoVariavelTabela(tokenAnteriorAtribuicao.getLexema())) {
				if (inFuncao && listaFuncoesDeclaradas.get(listaFuncoesDeclaradas.size() - 1).get(0).getComando().equals(tokenAnteriorAtribuicao.getLexema())) {
					if (nivelRetorno > 0 && (listaFuncoesDeclaradas.get(listaFuncoesDeclaradas.size() - 1).get(0).isRetornado()
							|| listaFuncoesDeclaradas.get(listaFuncoesDeclaradas.size() - 1).get(listaFuncoesDeclaradas.get(listaFuncoesDeclaradas.size() - 1).size() - 1).isRetornado())) {
							throw new Exception("Erro na linha " + token.getLinha()
									+ ". Comando "
									+ tokenAnteriorAtribuicao.getLexema()
									+ " inalcançável. Já existe um retorno.");
					} else if (nivelRetorno == 0 && listaFuncoesDeclaradas.get(listaFuncoesDeclaradas.size() - 1).get(0).isRetornado()) {
							throw new Exception("Erro na linha " + token.getLinha()
									+ ". Comando "
									+ tokenAnteriorAtribuicao.getLexema()
									+ " inalcançável. Já existe um retorno.");
					}
					colocaTrueNiveisAcimaTabelaRetorno(nivelRetorno);
				} else if (inFuncao && !listaFuncoesDeclaradas.get(listaFuncoesDeclaradas.size() - 1).get(0).getComando().equals(tokenAnteriorAtribuicao.getLexema())
						&& pesquisaDeclaracaoVariavelTabela(tokenAnteriorAtribuicao.getLexema())) {
					if (nivelRetorno > 0 && (listaFuncoesDeclaradas.get(listaFuncoesDeclaradas.size() - 1).get(0).isRetornado()
											|| listaFuncoesDeclaradas.get(listaFuncoesDeclaradas.size() - 1).get(listaFuncoesDeclaradas.get(listaFuncoesDeclaradas.size() - 1).size() - 1).isRetornado())) {
							throw new Exception("Erro na linha " + token.getLinha() + ". Comando "+ tokenAnteriorAtribuicao.getLexema()	
									+ " inalcançável. Já existe um retorno.");
					} else if (nivelRetorno == 0 && listaFuncoesDeclaradas.get(listaFuncoesDeclaradas.size() - 1).get(0).isRetornado()) {
							throw new Exception("Erro na linha " + token.getLinha() + ". Comando "+ tokenAnteriorAtribuicao.getLexema()	
									+ " inalcançável. Já existe um retorno.");
					}
				} else if (inFuncao && !listaFuncoesDeclaradas.get(listaFuncoesDeclaradas.size() - 1).get(0).getComando().equals(tokenAnteriorAtribuicao.getLexema())
						&& pesquisaDeclaracaoFuncaoTabela(tokenAnteriorAtribuicao.getLexema())) {
					throw new Exception("Erro na linha " + token.getLinha()	+ ". Retorno para uma função incorreta.");
				} else if (inProcedimento && pesquisaDeclaracaoFuncaoTabela(tokenAnteriorAtribuicao.getLexema())) {
					throw new Exception("Erro na linha " + token.getLinha() + ". Não pode haver retorno de função em procedimento.");
				} else if (!inFuncao && !inProcedimento && pesquisaDeclaracaoFuncaoTabela(tokenAnteriorAtribuicao.getLexema())) {
					throw new Exception("Erro na linha " + token.getLinha() + ". Não pode haver retorno de função no programa principal.");
				}
				return analisaAtribuicao(token);
			} else {
				throw new Exception("Erro na linha " + token.getLinha() + ". A variável ou função "
						+ tokenAnteriorAtribuicao.getLexema() + " não foi declarada.");
			}
		} else {
			return analisaChamadaProcedimento(token);
		}
	}

	private static Token analisaAtribuicao(Token token) throws Exception {
		token = lexico();
		token = analisaExpressao(token);

		String tipoExpressao = analisaPosfixo();
		String tipoTokenAnterior = getSimboloVariavelFuncao(tokenAnteriorAtribuicao.getLexema()).getTipo();

		if (!tipoExpressao.equals(tipoTokenAnterior)) {
			throw new Exception(
					"Erro na linha "
							+ token.getLinha()
							+ " . Incompatibilidade do tipo de retorno da expressão com o tipo da variável "
							+ tokenAnteriorAtribuicao.getLexema());
		}
		
		if (inFuncao && 
				listaFuncoesDeclaradas.get(listaFuncoesDeclaradas.size() - 1).get(0).getComando().equals(tokenAnteriorAtribuicao.getLexema())) {
			gera("STR 0");
		} else {
			int enderecoVariavel = getEnderecoVariavel(tokenAnteriorAtribuicao.getLexema());
			gera("STR "+enderecoVariavel);
		}

		tokenAnteriorExpressao = null;
		return token;
	}

	private static Token analisaLeia(Token token) throws Exception {
		
		gera("RD");
		
		token = lexico();
		if (token.getSimbolo().equals("sabre_parenteses")) {
			token = lexico();
			if (token.getSimbolo().equals("sidentificador")) {
				if (pesquisaDeclaracaoVariavelTabela(token.getLexema())) {
					int enderecoVariavel = getEnderecoVariavel(token.getLexema());
					gera("STR "+enderecoVariavel);
					token = lexico();
					if (token.getSimbolo().equals("sfecha_parenteses")) {
						token = lexico();
						return token;
					} else {
						throw new Exception(
								"Erro na linha "
										+ token.getLinha()
										+ ". Espera-se um fecha parenteses após uma variável ou função. \nÚltimo token lido: "
										+ token.getLexema());
					}
				} else {
					throw new Exception("Erro na linha " + token.getLinha()
							+ ". Não foi encontrada a variável para leitura.");
				}
			} else {
				throw new Exception(
						"Erro na linha "
								+ token.getLinha()
								+ ". Espera-se uma variável ou função após abertura dos parenteses. \nÚltimo token lido: "
								+ token.getLexema());
			}
		} else {
			throw new Exception(
					"Erro na linha "
							+ token.getLinha()
							+ ". Espera-se um abre parenteses após a palavra leia. \nÚltimo token lido: "
							+ token.getLexema());
		}
	}

	private static Token analisaEscreva(Token token) throws Exception {
		token = lexico();
		if (token.getSimbolo().equals("sabre_parenteses")) {
			token = lexico();
			if (token.getSimbolo().equals("sidentificador")) {
				if (pesquisaDeclaracaoFuncaoVariavelTabela(token.getLexema())) {
					
					if(pesquisaDeclaracaoFuncaoTabela(token.getLexema())) {
						String rotuloFuncao = getRotuloFuncao(token.getLexema());
						gera("CALL "+rotuloFuncao);
						gera("LDV 0");
					} else {
						int enderecoVariavel = getEnderecoVariavel(token.getLexema());
						gera("LDV "+enderecoVariavel);
					}
					
					gera("PRN");
									
					token = lexico();
					if (token.getSimbolo().equals("sfecha_parenteses")) {
						token = lexico();
						return token;
					} else {
						throw new Exception(
								"Erro na linha "
										+ token.getLinha()
										+ ". Não foi encontrado um fecha parenteses após o identificador. \n Último token lido: "
										+ token.getLexema());
					}
				} else {
					throw new Exception(
							"Erro na linha "
									+ token.getLinha()
									+ ". Não foi encontrada a variável ou função para a escrita.");
				}
			} else {
				throw new Exception(
						"Erro na linha "
								+ token.getLinha()
								+ ". Está faltando um identificador após abertura dos parenteses. \n Último token lido: "
								+ token.getLexema());
			}
		} else {
			throw new Exception(
					"Erro na linha "
							+ token.getLinha()
							+ ". Está faltando um abre parenteses após a palavra escreve. \n Último token lido: "
							+ token.getLexema());
		}
	}

	private static Token analisaEnquanto(Token token) throws Exception {
		
		int rotuloAuxiliar1 = 0;
		int rotuloAuxiliar2 = 0;
		
		rotuloAuxiliar1 = rotulo;
		
		gera("L"+rotulo+" NULL");
		rotulo++;
		
		token = lexico();
		token = analisaExpressao(token);

		verificaTipoBooleano(analisaPosfixo(), token);
		tokenAnteriorExpressao = null;

		if (token.getSimbolo().equals("sfaca")) {
			rotuloAuxiliar2 = rotulo;
			gera("JMPF L"+rotulo);
			rotulo++;
			token = lexico();
			token = analisaComandoSimples(token);

			gera("JMP L"+rotuloAuxiliar1);
			gera("L"+rotuloAuxiliar2+" NULL");
			
			return token;
		} else {
			throw new Exception(
					"Erro na linha "
							+ token.getLinha()
							+ ". Está faltando a palavra 'faca' ou condição inválida. \n Último token lido: "
							+ token.getLexema());
		}
	}

	private static Token analisaSe(Token token) throws Exception {
		token = lexico();
		token = analisaExpressao(token);
		
		verificaTipoBooleano(analisaPosfixo(), token);
		tokenAnteriorExpressao = null;
		
		gera("JMPF L"+rotulo);
		int rotuloAux1 = rotulo;
		rotulo++;
		if (token.getSimbolo().equals("sentao")) {
			token = lexico();
			token = analisaComandoSimples(token);
			if (inFuncao) {
				nivelRetorno--;
			}
			
			gera("JMP L"+rotulo);
			int rotuloAux2 = rotulo;
			rotulo++;
			gera("L"+rotuloAux1+" NULL");

			if (token.getSimbolo().equals("ssenao")) {
				if (inFuncao) {
					nivelRetorno++;
					listaFuncoesDeclaradas.get(listaFuncoesDeclaradas.size() - 1).add(new Retorno(token.getLexema(), false, nivelRetorno));
				}
				
				token = lexico();
				token = analisaComandoSimples(token);
				if (inFuncao) {
					if (listaFuncoesDeclaradas.get(listaFuncoesDeclaradas.size() - 1).get(listaFuncoesDeclaradas.get(listaFuncoesDeclaradas.size() - 1).size() - 1).isRetornado()) {
						validaNivelIgualInferior(nivelRetorno);
					}
					nivelRetorno--;
				}
			}
			gera("L"+rotuloAux2+" NULL");
			
			return token;
		} else {
			throw new Exception("Erro na linha "
					+ token.getLinha()
					+ ". Está faltando a palavra 'entao'. \n Último token lido: "
					+ token.getLexema());
		}
	}

	private static Token analisaSubrotinas(Token token) throws Exception {
		int rotuloAuxiliar = 0;
		int flag = 0;
		
		if (token.getSimbolo().equals("sprocedimento") || token.getSimbolo().equals("sfuncao")) {
			rotuloAuxiliar = rotulo;
			gera("JMP L"+rotulo);
			rotulo++;
			flag = 1;
		}
		while (token.getSimbolo().equals("sprocedimento") || token.getSimbolo().equals("sfuncao")) {
			if (token.getSimbolo().equals("sprocedimento")) {
				token = analisaDeclaracaoProcedimento(token);
			} else {
				token = analisaDeclaracaoFuncao(token);
			}
			if (token.getSimbolo().equals("sponto_virgula")) {
				token = lexico();
			} else {
				throw new Exception(
						"Erro na linha "
								+ token.getLinha()
								+ ". Está faltando um ponto e vírgula após declaração de subrotinas. \n Último token lido: "
								+ token.getLexema());
			}
		}
		
		if (flag == 1) {
			gera("L"+rotuloAuxiliar+" NULL");
		}
		
		return token;
	}

	private static Token analisaDeclaracaoProcedimento(Token token) throws Exception {
		
		token = lexico();
		if (token.getSimbolo().equals("sidentificador")) {
			if (!pesquisaDeclaracaoProcedimentoTabela(token.getLexema())) {

				inProcedimento = true;
				if(inFuncao) {
					inFuncao = false;
				}
				controleProcedimento++;
				nivel++;
				insereTabelaSimbolos(token.getLexema(), null, nivel, "L"+rotulo, "nomedeprocedimento", null);
				gera("L"+rotulo+" NULL");
				rotulo++;

				token = lexico();
				if (token.getSimbolo().equals("sponto_virgula")) {
					analisaBloco(token);
					controleProcedimento--;
					if(controleProcedimento == 0) {
						inProcedimento = false;
					}
					if(listaFuncoesDeclaradas.size() > 0) {
						inFuncao = true;
					}
					
					gera("RETURN");
					
					return token;
				} else {
					throw new Exception(
							"Erro na linha"
									+ token.getLinha()
									+ ". Está faltando um ponto e vírgula. \n Último token lido: "
									+ token.getLexema());
				}
			} else {
				throw new Exception("Erro na linha " + token.getLinha()
						+ ". Já existe um procedimento com esse nome");
			}
		} else {
			throw new Exception(
					"Erro na linha "
							+ token.getLinha()
							+ " identificador inválido. \n Último token lido: "
							+ token.getLexema());
		}
	}

	private static Token analisaDeclaracaoFuncao(Token token) throws Exception {
		token = lexico();
		if (token.getSimbolo().equals("sidentificador")) {
			if (!pesquisaDeclaracaoFuncaoTabela(token.getLexema())) {
				nivel++;
				insereTabelaSimbolos(token.getLexema(), null, nivel, "L"+rotulo, "nomedefuncao", null);

				gera("L"+rotulo+" NULL");
				rotulo++;
				
				inFuncao = true;
				if(inProcedimento) {
					inProcedimento = false;
				}
				List<Retorno> listaFuncao = new ArrayList<>();
				listaFuncao.add(new Retorno(token.getLexema(), false, nivelRetorno));
				listaFuncoesDeclaradas.add(listaFuncao);

				token = lexico();
				if (token.getSimbolo().equals("sdoispontos")) {
					token = lexico();
					if ((token.getSimbolo().equals("sinteiro") || token.getSimbolo().equals("sbooleano"))) {
						if (token.getSimbolo().equals("sinteiro")) {
							colocaTipoRetornoFuncao("sinteiro");
						} else if (token.getSimbolo().equals("sbooleano")) {
							colocaTipoRetornoFuncao("sbooleano");
						}
						token = lexico();
						if (token.getSimbolo().equals("sponto_virgula")) {
							analisaBloco(token);
							
							gera("RETURN");
						}
					} else {
						throw new Exception(
								"Erro na linha "
										+ token.getLinha()
										+ ". O tipo de retorno é inválido, é permitido apenas inteiro ou booleano. \n Último token lido: "
										+ token.getLexema());
					}
				} else {
					throw new Exception(
							"Erro na linha "
									+ token.getLinha()
									+ ". Está faltando dois pontos após o identificador. \n Último token lido: "
									+ token.getLexema());
				}
			} else {
				throw new Exception("Erro na linha " + token.getLinha()
						+ ". Já existe uma função com esse nome");
			}
		} else {
			throw new Exception(
					"Erro na linha "
							+ token.getLinha()
							+ ". Está faltando um identificador. \n Último token lido: "
							+ token.getLexema());
		}
		
		System.out.println("Lista Retorno: ");
		for (Retorno retorno : listaFuncoesDeclaradas.get(listaFuncoesDeclaradas.size() - 1)) {
			System.out.println(retorno.toString());
		}
		
		if (!listaFuncoesDeclaradas.get(listaFuncoesDeclaradas.size() - 1).get(0).isRetornado()) {
			throw new Exception("Erro na função: "
					+ listaFuncoesDeclaradas.get(listaFuncoesDeclaradas.size() - 1).get(0).getComando() + " . Retorno inválido!");
		}
		
		listaFuncoesDeclaradas.remove(listaFuncoesDeclaradas.get(listaFuncoesDeclaradas.size() - 1));
	
		if(listaFuncoesDeclaradas.size() == 0) {
			inFuncao = false;
		}
		if(controleProcedimento > 0) {
			inProcedimento = true;
		}
		return token;
	}

	private static Token analisaExpressao(Token token) throws Exception {
		token = analisaExpressaoSimples(token);
		if (token.getSimbolo().equals("smaior") || token.getSimbolo().equals("smaiorig") || token.getSimbolo().equals("sig")
				|| token.getSimbolo().equals("smenor") || token.getSimbolo().equals("smenorig") || token.getSimbolo().equals("sdif")) {
			tokenAnteriorExpressao = token;
			token.setUnario(false);
			adicionaPilhaPosfixo(token);
			token = lexico();
			token = analisaExpressaoSimples(token);
		}
		return token;
	}

	private static Token analisaExpressaoSimples(Token token) throws Exception {
		if (token.getSimbolo().equals("smais") || token.getSimbolo().equals("smenos")) {
			token.setUnario(true);
			adicionaPilhaPosfixo(token);
			tokenAnteriorExpressao = token;
			token = lexico();
		}
		token = analisaTermo(token);
		while (token.getSimbolo().equals("smais") || token.getSimbolo().equals("smenos") || token.getSimbolo().equals("sou")) {
			token.setUnario(false);
			adicionaPilhaPosfixo(token);
			tokenAnteriorExpressao = token;
			token = lexico();
			token = analisaTermo(token);
		}
		return token;
	}

	private static Token analisaTermo(Token token) throws Exception {
		token = analisaFator(token);
		while (token.getSimbolo().equals("smult") || token.getSimbolo().equals("sdiv") || token.getSimbolo().equals("se")) {
			token.setUnario(false);
			adicionaPilhaPosfixo(token);
			tokenAnteriorExpressao = token;
			token = lexico();
			token = analisaFator(token);
		}
		return token;
	}

	private static Token analisaFator(Token token) throws Exception {
		if (token.getSimbolo().equals("sidentificador")) {
			if (pesquisa_tabela(token.getLexema())) {
				String tipo = getTipoFuncaoVariavel(token.getLexema());
				if (tipo.equals("inteiro") || tipo.equals("booleano")) {
					adicionaFilaPosfixo(token);
					token = analisaChamadaFuncao();
				}
			} else {
				if (pesquisaDeclaracaoVariavelTabela(token.getLexema())) {
					adicionaFilaPosfixo(token);
					tokenAnteriorExpressao = token;
					token = lexico();
				} else {
					throw new Exception("Erro na linha " + token.getLinha()
							+ ". A variável " + token.getLexema()
							+ " não foi declarada ou não está visível.");
				}
			}
			return token;
		} else if (token.getSimbolo().equals("snumero")) {
			adicionaFilaPosfixo(token);
			tokenAnteriorExpressao = token;
			return lexico();
		} else if (token.getSimbolo().equals("snao")) {
			token.setUnario(true);
			adicionaPilhaPosfixo(token);
			tokenAnteriorExpressao = token;
			token = lexico();
			return analisaFator(token);
		} else if (token.getSimbolo().equals("sabre_parenteses")) {
			token.setUnario(false);
			adicionaPilhaPosfixo(token);
			tokenAnteriorExpressao = token;
			token = lexico();
			token = analisaExpressao(token);
			if (token.getSimbolo().equals("sfecha_parenteses")) {
				desempilhaPilhaParenteses();
				tokenAnteriorExpressao = token;
				return lexico();
			} else {
				throw new Exception(
						"Erro na linha "
								+ token.getLinha()
								+ ". Está faltando um fecha parenteses após a expressão. \n Último token lido: "
								+ token.getLexema());
			}
		} else if (token.getLexema().equals("verdadeiro") || token.getLexema().equals("falso")) {
			adicionaFilaPosfixo(token);
			tokenAnteriorExpressao = token;
			return lexico();
		} else {
			throw new Exception(
					"Erro na linha "
							+ token.getLinha()
							+ ". O fator é inválido. Apenas são permitidos identificadores, números, expressões, a palavra 'verdadeiro', a palavra 'falso' ou a palavra 'nao' mais um fator. \n Último token lido: "
							+ token.getLexema());
		}
	}

	private static Token analisaChamadaProcedimento(Token token) throws Exception {
		if (!pesquisaDeclaracaoProcedimentoTabela(tokenAnteriorAtribuicao.getLexema())) {
			throw new Exception("Erro na linha "
					+ tokenAnteriorAtribuicao.getLinha() + ". Procedimento "
					+ tokenAnteriorAtribuicao.getLexema() + " não declarado.");
		}
		
		String rotuloProcedimento = getRotuloProcedimento(tokenAnteriorAtribuicao.getLexema());
		
		gera("CALL "+rotuloProcedimento);
		
		return token;
	}

	private static Token analisaChamadaFuncao() throws Exception {
		return lexico();
	}

	public static void colocaTrueNiveisAcimaTabelaRetorno(int nivel) {
		int i = listaFuncoesDeclaradas.get(listaFuncoesDeclaradas.size() - 1).size() - 1;
		while (i >= 0) {
			if (listaFuncoesDeclaradas.get(listaFuncoesDeclaradas.size() - 1).get(i).getNivel() > nivel) {
				listaFuncoesDeclaradas.get(listaFuncoesDeclaradas.size() - 1).get(i).setRetornado(true);
			} else if (listaFuncoesDeclaradas.get(listaFuncoesDeclaradas.size() - 1).get(i).getNivel() == nivel) {
				listaFuncoesDeclaradas.get(listaFuncoesDeclaradas.size() - 1).get(i).setRetornado(true);
				break;
			}
			i--;
		}
	}

	private static void validaNivelIgualInferior(int nivelRetorno) throws Exception {
		int i = listaFuncoesDeclaradas.get(listaFuncoesDeclaradas.size() - 1).size() - 1;
		boolean seSenaoTrue = false;
		while (i >= 0) {
			if (listaFuncoesDeclaradas.get(listaFuncoesDeclaradas.size() - 1).get(i).getNivel() == nivelRetorno
					&& listaFuncoesDeclaradas.get(listaFuncoesDeclaradas.size() - 1).get(i).getComando().equals("se")
					&& listaFuncoesDeclaradas.get(listaFuncoesDeclaradas.size() - 1).get(i).isRetornado()) {
				seSenaoTrue = true;
			}
			if (seSenaoTrue
					&& listaFuncoesDeclaradas.get(listaFuncoesDeclaradas.size() - 1).get(i).getNivel() == (nivelRetorno - 1)
					&& !listaFuncoesDeclaradas.get(listaFuncoesDeclaradas.size() - 1).get(i).isRetornado()) {
				listaFuncoesDeclaradas.get(listaFuncoesDeclaradas.size() - 1).get(i).setRetornado(true);
				int tamListaRetorno = listaFuncoesDeclaradas.get(listaFuncoesDeclaradas.size() - 1).size() - 1;
				while (tamListaRetorno > i) {
					listaFuncoesDeclaradas.get(listaFuncoesDeclaradas.size() - 1).remove(tamListaRetorno);
					tamListaRetorno = listaFuncoesDeclaradas.get(listaFuncoesDeclaradas.size() - 1).size() - 1;
				}
				break;

			} else if (seSenaoTrue
					&& listaFuncoesDeclaradas.get(listaFuncoesDeclaradas.size() - 1).get(i).getNivel() == (nivelRetorno - 1)
					&& listaFuncoesDeclaradas.get(listaFuncoesDeclaradas.size() - 1).get(i).isRetornado()) {
				throw new Exception("Erro na linha "
						+ tokenAnteriorAtribuicao.getLinha() + ". Comando "
						+ tokenAnteriorAtribuicao.getLexema()
						+ " inalcançável. Já existe um retorno.");
			}
			i--;
		}
	}
}