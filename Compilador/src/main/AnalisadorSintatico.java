package main;

import static main.AnalisadorLexico.lexico;
import static main.AnalisadorSemantico.adicionaFilaPosfixo;
import static main.AnalisadorSemantico.adicionaPilhaPosfixo;
import static main.AnalisadorSemantico.analisaPosfixo;
import static main.AnalisadorSemantico.colocaTipoRetornoFuncao;
import static main.AnalisadorSemantico.colocaTipoVariaveis;
import static main.AnalisadorSemantico.desempilhaNivelTabela;
import static main.AnalisadorSemantico.desempilhaPilhaParenteses;
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
import static main.AnalisadorSemantico.zeraVariaveis;

import java.util.ArrayList;
import java.util.List;

public class AnalisadorSintatico {

	private static Integer nivel = 0;
	private static Token tokenAnteriorAtribuicao = null;
	private static Token tokenAnteriorExpressao = null;

	private static List<Retorno> listaRetorno = new ArrayList<>();
	private static int nivelRetorno = 0;
	private static boolean inFuncao = false;

	public static void analisadorSintatico() throws Exception {

		zeraVariaveis();
		inFuncao = false;
		listaRetorno.clear();
		nivel = 0;
		nivelRetorno = 0;
		tokenAnteriorAtribuicao = null;
		tokenAnteriorExpressao = null;

		AnalisadorLexico.main(null);
		Token token = lexico();

		if (token.getSimbolo().equals("sprograma")) {
			token = lexico();
			if (token.getSimbolo().equals("sidentificador")) {
				insereTabelaSimbolos(token.getLexema(), null, nivel, null,
						"nomedeprograma");
				token = lexico();
				if (token.getSimbolo().equals("sponto_virgula")) {
					token = analisaBloco(token);
					if (token.getSimbolo().equals("sponto")) {
						try {
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
		do {
			if (token.getSimbolo().equals("sidentificador")) {
				if (!pesquisaDuplicidadeVariavelTabela(token.getLexema(), nivel)) {
					insereTabelaSimbolos(token.getLexema(), null, nivel, null,
							"nomedevariavel");
					token = lexico();
					if (token.getSimbolo().equals("svirgula")
							|| token.getSimbolo().equals("sdoispontos")) {
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
							"Erro na linha"
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
		return analisaTipo(lexico());
	}

	private static Token analisaTipo(Token token) throws Exception {
		if (!token.getSimbolo().equals("sinteiro")
				&& !token.getSimbolo().equals("sbooleano")) {
			throw new Exception(
					"Erro na linha "
							+ token.getLinha()
							+ ". Apenas é permitido apenas tipo inteiro ou booleano. \n Token lido: "
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
									+ ". Espera-se um ponto e virgula após um comando. \nUltimo token lido: "
									+ token.getLexema());
				}
			}
			return lexico();
		} else {
			throw new Exception(
					"Erro na linha "
							+ token.getLinha()
							+ ". Espera-se a palavra 'inicio' para iniciar um comando. \nUltimo token lido: "
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
				listaRetorno.add(new Retorno(token.getLexema(), false,
						nivelRetorno));
			}
			return analisaSe(token);
		} else if (token.getSimbolo().equals("senquanto")) {
			if (inFuncao
					&& listaRetorno.get(listaRetorno.size() - 1).isRetornado()) {
				throw new Exception("Erro na linha " + token.getLinha()
						+ ". Comando " + token.getLexema()
						+ " inalcançável. Já existe um retorno.");
			}
			return analisaEnquanto(token);
		} else if (token.getSimbolo().equals("sleia")) {
			if (inFuncao
					&& listaRetorno.get(listaRetorno.size() - 1).isRetornado()) {
				throw new Exception("Erro na linha " + token.getLinha()
						+ ". Comando " + tokenAnteriorAtribuicao.getLexema()
						+ " inalcançável. Já existe um retorno.");
			}
			return analisaLeia(token);
		} else if (token.getSimbolo().equals("sescreva")) {
			if (inFuncao
					&& listaRetorno.get(listaRetorno.size() - 1).isRetornado()) {
				throw new Exception("Erro na linha " + token.getLinha()
						+ ". Comando " + token.getLexema()
						+ " inalcançável. Já existe um retorno.");
			}
			return analisaEscreva(token);
		} else {
			return analisaComandos(token);
		}
	}

	private static Token analisaAtribuicaoChamadaProcedimento(Token token)
			throws Exception {
		token = lexico();
		if (token.getSimbolo().equals("satribuicao")) {
			if (pesquisaDeclaracaoFuncaoVariavelTabela(tokenAnteriorAtribuicao
					.getLexema())) {
				if (inFuncao
						&& listaRetorno.get(0).getComando()
								.equals(tokenAnteriorAtribuicao.getLexema())) {
					if (listaRetorno.get(listaRetorno.size() - 1).isRetornado()) {
						throw new Exception("Erro na linha " + token.getLinha()
								+ ". Comando "
								+ tokenAnteriorAtribuicao.getLexema()
								+ " inalcançável. Já existe um retorno.");
					}
					colocaTrueNiveisAcimaTabelaRetorno(nivelRetorno);
				} else if (inFuncao && !listaRetorno.get(0).getComando().equals(tokenAnteriorAtribuicao.getLexema())
						&& listaRetorno.get(0).isRetornado() && pesquisaDeclaracaoVariavelTabela(tokenAnteriorAtribuicao.getLexema())) {
					throw new Exception("Erro na linha " + token.getLinha() + ". Comando "+ tokenAnteriorAtribuicao.getLexema()	
							+ " inalcançável. Já existe um retorno.");
				} else if (inFuncao && !listaRetorno.get(0).getComando().equals(tokenAnteriorAtribuicao.getLexema())
						&& pesquisaDeclaracaoFuncaoTabela(tokenAnteriorAtribuicao.getLexema())) {
					throw new Exception("Erro na linha " + token.getLinha()	+ ". Retorno para uma função incorreta.");
				}
				return analisaAtribuicao(token);
			} else {
				throw new Exception("Erro na linha " + token.getLinha() + ". A variavel ou função "
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
		String tipoTokenAnterior = getSimboloVariavelFuncao(
				tokenAnteriorAtribuicao.getLexema()).getTipo();

		if (!tipoExpressao.equals(tipoTokenAnterior)) {
			throw new Exception(
					"Erro na linha "
							+ token.getLinha()
							+ " . Incompatibilidade do tipo de retorno da expressao com o tipo da variavel "
							+ tokenAnteriorAtribuicao.getLexema());
		}

		tokenAnteriorExpressao = null;
		return token;
	}

	private static Token analisaLeia(Token token) throws Exception {
		token = lexico();
		if (token.getSimbolo().equals("sabre_parenteses")) {
			token = lexico();
			if (token.getSimbolo().equals("sidentificador")) {
				if (pesquisaDeclaracaoVariavelTabela(token.getLexema())) {
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
							+ ". Não foi encontrada a variavel para leitura.");
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
					token = lexico();
					if (token.getSimbolo().equals("sfecha_parenteses")) {
						token = lexico();
						return token;
					} else {
						throw new Exception(
								"Erro no método analisaEscreva(). Na linha "
										+ token.getLinha()
										+ " está faltando um fecha parenteses após identificador. \n Token lido: "
										+ token.getLexema());
					}
				} else {
					throw new Exception(
							"Erro na linha "
									+ token.getLinha()
									+ ". Não foi encontrada a variavel ou funcao para escrita.");
				}
			} else {
				throw new Exception(
						"Erro no método analisaEscreva(). Na linha "
								+ token.getLinha()
								+ " está faltando um identificador após abertura dos parenteses. \n Token lido: "
								+ token.getLexema());
			}
		} else {
			throw new Exception(
					"Erro no método analisaEscreva(). Na linha "
							+ token.getLinha()
							+ " está faltando abre parenteses após a palavra escreve. \n Token lido: "
							+ token.getLexema());
		}
	}

	private static Token analisaEnquanto(Token token) throws Exception {
		token = lexico();
		token = analisaExpressao(token);

		verificaTipoBooleano(analisaPosfixo(), token);
		tokenAnteriorExpressao = null;

		if (token.getSimbolo().equals("sfaca")) {
			token = lexico();
			token = analisaComandoSimples(token);

			return token;
		} else {
			throw new Exception(
					"Erro no método analisaEnquanto(). Na linha "
							+ token.getLinha()
							+ " está faltando a palavra 'faca' ou condição invalida. \n Token lido: "
							+ token.getLexema());
		}
	}

	private static Token analisaSe(Token token) throws Exception {
		token = lexico();
		token = analisaExpressao(token);

		verificaTipoBooleano(analisaPosfixo(), token);
		tokenAnteriorExpressao = null;

		if (token.getSimbolo().equals("sentao")) {
			token = lexico();
			token = analisaComandoSimples(token);
			if (inFuncao) {
				nivelRetorno--;
			}

			if (token.getSimbolo().equals("ssenao")) {
				if (inFuncao) {
					nivelRetorno++;
					listaRetorno.add(new Retorno(token.getLexema(), false,
							nivelRetorno));
				}
				token = lexico();
				token = analisaComandoSimples(token);
				if (inFuncao) {
					if (listaRetorno.get(listaRetorno.size() - 1).isRetornado()) {
						validaNivelIgualInferior(nivelRetorno);
					}
					nivelRetorno--;
				}
			}

			return token;
		} else {
			throw new Exception("Erro no método analisaSe(). Na linha "
					+ token.getLinha()
					+ " está faltando a palavra 'entao'. \n Token lido: "
					+ token.getLexema());
		}
	}

	private static Token analisaSubrotinas(Token token) throws Exception {
		if (token.getSimbolo().equals("sprocedimento")
				|| token.getSimbolo().equals("sfuncao")) {
			// geracao de codigo
		}
		while (token.getSimbolo().equals("sprocedimento")
				|| token.getSimbolo().equals("sfuncao")) {
			if (token.getSimbolo().equals("sprocedimento")) {
				token = analisaDeclaracaoProcedimento(token);
			} else {
				token = analisaDeclaracaoFuncao(token);
			}
			if (token.getSimbolo().equals("sponto_virgula")) {
				token = lexico();
			} else {
				throw new Exception(
						"Erro no método analisaSubrotinas(). Na linha "
								+ token.getLinha()
								+ " está faltando um ponto e virgula após declaração de subrotinas. \n Token lido: "
								+ token.getLexema());
			}
		}
		return token;
	}

	private static Token analisaDeclaracaoProcedimento(Token token)
			throws Exception {
		token = lexico();
		if (token.getSimbolo().equals("sidentificador")) {
			if (!pesquisaDeclaracaoProcedimentoTabela(token.getLexema())) {
				nivel++;
				insereTabelaSimbolos(token.getLexema(), null, nivel, null,
						"nomedeprocedimento");

				token = lexico();
				if (token.getSimbolo().equals("sponto_virgula")) {
					return analisaBloco(token);
				} else {
					throw new Exception(
							"Erro no método analisaDeclaracaoProcedimento(). Na linha"
									+ token.getLinha()
									+ " está faltando um ponto e virgula. \n Token lido: "
									+ token.getLexema());
				}
			} else {
				throw new Exception("Erro na linha " + token.getLinha()
						+ ". Já existe um procedimento com esse nome");
			}
		} else {
			throw new Exception(
					"Erro no método analisaDeclaracaoProcedimento(). Na linha"
							+ token.getLinha()
							+ " está faltando um identificador. \n Token lido: "
							+ token.getLexema());
		}
	}

	private static Token analisaDeclaracaoFuncao(Token token) throws Exception {
		token = lexico();
		if (token.getSimbolo().equals("sidentificador")) {
			if (!pesquisaDeclaracaoFuncaoTabela(token.getLexema())) {
				nivel++;
				insereTabelaSimbolos(token.getLexema(), null, nivel, null,
						"nomedefuncao");

				inFuncao = true;
				listaRetorno.add(new Retorno(token.getLexema(), false,
						nivelRetorno));

				token = lexico();
				if (token.getSimbolo().equals("sdoispontos")) {
					token = lexico();
					if ((token.getSimbolo().equals("sinteiro") || token
							.getSimbolo().equals("sbooleano"))) {
						if (token.getSimbolo().equals("sinteiro")) {
							colocaTipoRetornoFuncao("sinteiro");
						} else if (token.getSimbolo().equals("sbooleano")) {
							colocaTipoRetornoFuncao("sbooleano");
						}
						token = lexico();
						if (token.getSimbolo().equals("sponto_virgula")) {
							analisaBloco(token);
						}
					} else {
						throw new Exception(
								"Erro no método analisaDeclaracaoFuncao(). Na linha"
										+ token.getLinha()
										+ " o tipo de retorno é inválido, é permitido apenas inteirou ou booleano. \n Token lido: "
										+ token.getLexema());
					}
				} else {
					throw new Exception(
							"Erro no método analisaDeclaracaoFuncao(). Na linha"
									+ token.getLinha()
									+ " está faltando dois pontos após o identificador. \n Token lido: "
									+ token.getLexema());
				}
			} else {
				throw new Exception("Erro na linha " + token.getLinha()
						+ ". Já existe uma função com esse nome");
			}
		} else {
			throw new Exception(
					"Erro no método analisaDeclaracaoFuncao(). Na linha"
							+ token.getLinha()
							+ " está faltando um identificador. \n Token lido: "
							+ token.getLexema());
		}

		System.out.println("Lista Retorno: ");
		for (Retorno retorno : listaRetorno) {
			System.out.println(retorno.toString());
		}

		if (!listaRetorno.get(0).isRetornado()) {
			throw new Exception("Erro na função: "
					+ listaRetorno.get(0).getComando() + " . Retorno inválido!");
		}

		listaRetorno.removeAll(listaRetorno);
		inFuncao = false;
		return token;
	}

	private static Token analisaExpressao(Token token) throws Exception {
		token = analisaExpressaoSimples(token);
		if (token.getSimbolo().equals("smaior")
				|| token.getSimbolo().equals("smaiorig")
				|| token.getSimbolo().equals("sig")
				|| token.getSimbolo().equals("smenor")
				|| token.getSimbolo().equals("smenorig")
				|| token.getSimbolo().equals("sdif")) {
			tokenAnteriorExpressao = token;
			token.setUnario(false);
			adicionaPilhaPosfixo(token);
			token = lexico();
			token = analisaExpressaoSimples(token);
		}
		return token;
	}

	private static Token analisaExpressaoSimples(Token token) throws Exception {
		if (token.getSimbolo().equals("smais")
				|| token.getSimbolo().equals("smenos")) {
			token.setUnario(true);
			adicionaPilhaPosfixo(token);
			tokenAnteriorExpressao = token;
			token = lexico();
		}
		token = analisaTermo(token);
		while (token.getSimbolo().equals("smais")
				|| token.getSimbolo().equals("smenos")
				|| token.getSimbolo().equals("sou")) {
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
		while (token.getSimbolo().equals("smult")
				|| token.getSimbolo().equals("sdiv")
				|| token.getSimbolo().equals("se")) {
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
				if (tipo.equals("inteiro") || tipo.equals("boleano")) {
					adicionaFilaPosfixo(token);
					token = analisaChamadaFuncao();
				}
			} else {
				// Alterei aqui! Verificar visibilidade da variavel
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
						"Erro no método analisaFator(). Na linha "
								+ token.getLinha()
								+ " está faltando um fecha parenteses, após expressão. \n Token lido: "
								+ token.getLexema());
			}
		} else if (token.getLexema().equals("verdadeiro")
				|| token.getLexema().equals("falso")) {
			adicionaFilaPosfixo(token);
			tokenAnteriorExpressao = token;
			return lexico();
		} else {
			throw new Exception(
					"Erro no método analisaFator(). Na linha "
							+ token.getLinha()
							+ " o fator é inválido. Apenas são permitidos identificadores, numeros, expressões, palavra 'verdadeiro', 'falso' ou 'nao' mais fator. \n Token lido: "
							+ token.getLexema());
		}
	}

	private static Token analisaChamadaProcedimento(Token token)
			throws Exception {
		if (!pesquisaDeclaracaoProcedimentoTabela(tokenAnteriorAtribuicao
				.getLexema())) {
			throw new Exception("Erro na linha "
					+ tokenAnteriorAtribuicao.getLinha() + ". Procedimento "
					+ tokenAnteriorAtribuicao.getLexema() + " não declarado.");
		}
		return token;
	}

	private static Token analisaChamadaFuncao() throws Exception {

		/*
		 * String tipoTokenAnterior =
		 * getTipoFuncaoVariavel(tokenAnteriorAtribuicao.getLexema()); if
		 * (tipo.equals(tipoTokenAnterior)) { return lexico(); } throw new
		 * Exception( "Erro na linha " + tokenAnteriorAtribuicao.getLinha() +
		 * ". Tipos não compativeis, variável de tipo " + tipoTokenAnterior +
		 * " é diferente do tipo de retorno da função (" + tipo + ").");
		 */
		return lexico();
	}

	public static void colocaTrueNiveisAcimaTabelaRetorno(int nivel) {
		int i = listaRetorno.size() - 1;
		while (i >= 0) {
			if (listaRetorno.get(i).getNivel() > nivel) {
				listaRetorno.get(i).setRetornado(true);
			} else if (listaRetorno.get(i).getNivel() == nivel) {
				listaRetorno.get(i).setRetornado(true);
				break;
			}
			i--;
		}
	}

	private static void validaNivelIgualInferior(int nivelRetorno)
			throws Exception {
		int i = listaRetorno.size() - 1;
		boolean seSenaoTrue = false;
		while (i >= 0) {
			if (listaRetorno.get(i).getNivel() == nivelRetorno
					&& listaRetorno.get(i).getComando().equals("se")
					&& listaRetorno.get(i).isRetornado()) {
				seSenaoTrue = true;
			}
			if (seSenaoTrue
					&& listaRetorno.get(i).getNivel() == (nivelRetorno - 1)
					&& !listaRetorno.get(i).isRetornado()) {
				listaRetorno.get(i).setRetornado(true);
				int tamListaRetorno = listaRetorno.size() - 1;
				while (tamListaRetorno > i) {
					listaRetorno.remove(tamListaRetorno);
					tamListaRetorno = listaRetorno.size() - 1;
				}
				break;

			} else if (seSenaoTrue
					&& listaRetorno.get(i).getNivel() == (nivelRetorno - 1)
					&& listaRetorno.get(i).isRetornado()) {
				throw new Exception("Erro na linha "
						+ tokenAnteriorAtribuicao.getLinha() + ". Comando "
						+ tokenAnteriorAtribuicao.getLexema()
						+ " inalcançável. Já existe um retorno.");
			}
			i--;
		}
	}
}