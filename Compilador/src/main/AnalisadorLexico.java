package main;

import java.io.BufferedReader;
import java.io.FileReader;

import static main.TabelaTokens.*;

public class AnalisadorLexico {

	public static String pathArquivo;
	
	private static BufferedReader entrada;
	private static int numeroLinhaArquivo;
	private static char caractere;

	private final static char ENTER = 13;
	private final static char QUEBRA_LINHA = 10;
	private final static char BACKSPACE = 8;
	private final static char TAB = 9;
	private final static char ESPACO = 32;

	private final static char ABRE_CHAVES = 123;
	private final static char FECHA_CHAVES = 125;
	

	public static void main(String[] args) throws Exception {

		numeroLinhaArquivo = 0;

		try {
			entrada = new BufferedReader(new FileReader(pathArquivo));
			caractere = (char) entrada.read();
		} catch (Exception e) {
			throw new Exception("Não foi possivel ler o arquivo.");
		}

	}

	public static Token lexico() throws Exception {

		char caractereAnterior;

		while (caractere == ENTER || caractere == QUEBRA_LINHA) {
			numeroLinhaArquivo++;
			caractere = (char) entrada.read();
		}

		while (isCaracterValido() && (isBarra() || caractere == ABRE_CHAVES || caractere == BACKSPACE
				|| caractere == TAB || caractere == ESPACO)) {
			if (caractere == ABRE_CHAVES) {
				while (isCaracterValido() && caractere != '\uffff' && caractere != FECHA_CHAVES) {
					if (caractere == ENTER || caractere == QUEBRA_LINHA) {
						numeroLinhaArquivo++;
					}
					caractere = (char) entrada.read();
				}
				if (caractere == FECHA_CHAVES) {
					caractere = (char) entrada.read();
				} else {
					throw new Exception("Erro: Comentário foi aberto e não foi fechado!");
				}
			}

			if (isBarra()) {
				caractereAnterior = caractere;
				caractere = (char) entrada.read();
				if (isAsterisco()) {
					caractere = (char) entrada.read();
					while (!isBarra() && isCaracterValido() && caractere != '\uffff') {
						caractereAnterior = caractere;
						caractere = (char) entrada.read();
						if (caractere == ENTER || caractere == QUEBRA_LINHA) {
							numeroLinhaArquivo++;
						}
					}
					if (isBarra() && isAsterisco(caractereAnterior)) {
						caractere = (char) entrada.read();
					} else {
						throw new Exception("Erro: Comentário foi aberto e não foi fechado!");
					}
				} else {
					throw new Exception("Erro: Formato de comentário inválido.");
				}
			}

			while (isCaracterValido() && caractere != '\uffff' && (caractere == QUEBRA_LINHA || caractere == ENTER
					|| caractere == BACKSPACE || caractere == TAB || caractere == ESPACO)) {
				if (caractere == ENTER || caractere == QUEBRA_LINHA) {
					numeroLinhaArquivo++;
				}
				caractere = (char) entrada.read();
			}
		}

		if (isCaracterValido() && caractere != '\uffff') {
			Token token;
			try {
				token = pegaToken();
			} catch (Throwable e) {
				throw new Exception(e.getMessage());
			}
			return token;
		} else {
			throw new Exception("Chegou ao fim do arquivo. Não há mais tokens.");
		}
	}

	private static boolean isAsterisco() {
		return (char) caractere == '*';
	}

	private static boolean isAsterisco(char caractereAnterior) {
		return (char) caractereAnterior == '*';
	}

	private static boolean isBarra() {
		return (char) caractere == '/';
	}

	private static boolean isCaracterValido() {
		return (int) caractere != -1;
	}

	private static Token pegaToken() throws Throwable {
		if (isDigito(caractere)) {
			return trataDigito();
		} else if (isLetra(caractere)) {
			return trataIdentificadorAndPalavraReservada();
		} else if (caractere == ':') {
			return trataAtribuicao();
		} else if (caractere == '+' || caractere == '-' || caractere == '*') {
			return trataOperadorAritmetico();
		} else if (caractere == '<' || caractere == '>' || caractere == '=' || caractere == '!') {
			return trataOperadoRelacional();
		} else if (caractere == ';' || caractere == ',' || caractere == '(' || caractere == ')' || caractere == '.') {
			return trataPontuacao();
		} else {
			throw new Exception("Erro na linha " + numeroLinhaArquivo + ". Caractere inválido: " + caractere + "");
		}
	}

	private static Token trataDigito() throws Exception {
		StringBuilder numero = new StringBuilder();

		while (Character.isDigit(caractere)) {
			numero.append(caractere);
			caractere = (char) entrada.read();
		}

		Token token = new Token();
		token.setSimbolo("snumero");
		token.setLexema(numero.toString());
		token.setLinha(numeroLinhaArquivo);
		return token;
	}

	private static Token trataIdentificadorAndPalavraReservada() throws Exception {
		
		StringBuilder id = new StringBuilder();
	     
        while(isDigito(caractere) || isLetra(caractere) || caractere =='_') {
            id.append(caractere);
            caractere = (char) entrada.read();
        }

		Token token = new Token();
		token.setLexema(id.toString());
		token.setSimbolo(retornaSimbolo(id.toString()) != null ? retornaSimbolo(id.toString()) : "sidentificador");
		token.setLinha(numeroLinhaArquivo);
		return token;
	}

	private static Token trataAtribuicao() throws Exception {
		
		StringBuilder id = new StringBuilder();
		id.append(caractere);

		Token token = new Token();
		token.setLexema(id.toString());
		token.setSimbolo(retornaSimbolo(id.toString()));
		token.setLinha(numeroLinhaArquivo);

        caractere = (char) entrada.read();
		
		if (caractere == '=') {
			id.append(caractere);
			token.setLexema(id.toString());
			token.setSimbolo(retornaSimbolo(id.toString()));
	        caractere = (char) entrada.read();
		}
		return token;
	}

	private static Token trataOperadorAritmetico() throws Exception {
		StringBuilder id = new StringBuilder();
		id.append(caractere);

		Token token = new Token();
		token.setLexema(id.toString());
		token.setSimbolo(retornaSimbolo(id.toString()));
		token.setLinha(numeroLinhaArquivo);

		caractere = (char) entrada.read();

		return token;
	}

	private static Token trataOperadoRelacional() throws Exception {
		Token token = new Token();
		StringBuilder id = new StringBuilder();

		if (caractere == '<' || caractere == '>') {
			id.append(caractere);

			token.setLexema(id.toString());
			token.setSimbolo(retornaSimbolo(id.toString()));
			token.setLinha(numeroLinhaArquivo);
			
			caractere = (char) entrada.read();
			if (caractere == '=') {
				id.append(caractere);
				token.setLexema(id.toString());
				token.setSimbolo(retornaSimbolo(id.toString()));
				caractere = (char) entrada.read();
			}
		} else if (caractere == '!') {
			caractere = (char) entrada.read();
			if (caractere == '=') {
				id.append(caractere);

				token.setLexema(id.toString());
				token.setSimbolo(retornaSimbolo(id.toString()));
				token.setLinha(numeroLinhaArquivo);
				caractere = (char) entrada.read();
			} else {
				throw new Exception("Não existe o comando : !");
			}
		} else if (caractere == '=') {
			id.append(caractere);

			token.setLexema(id.toString());
			token.setSimbolo(retornaSimbolo(id.toString()));
			token.setLinha(numeroLinhaArquivo);
			caractere = (char) entrada.read();
		}

		return token;
	}

	private static Token trataPontuacao() throws Throwable {
		StringBuilder id = new StringBuilder();
		id.append(caractere);

		Token token = new Token();
		token.setLexema(id.toString());
		token.setSimbolo(retornaSimbolo(id.toString()));
		token.setLinha(numeroLinhaArquivo);

		caractere = (char) entrada.read();

		return token;
	}

	private static boolean isDigito(char c) {
		return Character.toString(c).matches("^[0-9]*$");
	}

	private static boolean isLetra(char c) {
		return Character.toString(c).matches("[a-zA-Z]+");
	}
}
