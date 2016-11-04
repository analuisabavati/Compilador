package main;

import java.util.Scanner;

import static main.ArquivoUtil.*;
import static main.TabelaToken.*;

public class AnalisadorLexico {

	private static int indexCaractereLinha = 0;
	private static int numeroLinha = 0;
	private static Scanner arquivoTratado;
	private static String linhaArquivo = null;

	public static void main(String[] args) throws Exception {
		gravaArquivoFonteTratado();
		arquivoTratado = abreArquivoFonte(pathArquivoTratado);
		if (arquivoTratado.hasNextLine()) {
			trataLinhaVazia();
		} else {
			throw new Exception("Arquivo Vazio!");
		}
	}

	public static Token lexico() throws Exception {
		if (indexCaractereLinha > linhaArquivo.length() - 1) {
			indexCaractereLinha = 0;
			if (arquivoTratado.hasNextLine()) {
				trataLinhaVazia();
			} else {
				throw new Exception("Chegou ao fim do arquivo. Não há mais tokens.");
			}
		} 
		return pegaToken(linhaArquivo);
	}

	private static void trataLinhaVazia() {
		do {
			linhaArquivo = arquivoTratado.nextLine();	
			numeroLinha++;
		} while (linhaArquivo.trim().isEmpty() && arquivoTratado.hasNextLine());
	}

	private static Token pegaToken(String linha) throws Exception {
		Character caractere = linha.charAt(indexCaractereLinha);
		if (isDigito(caractere)) {
			return trataDigito(linha);
		} else if (isLetra(caractere)) {
			return trataIdentificadorAndPalavraReservada(linha);
		} else if (caractere == ':') {
			return trataAtribuicao(linha);
		} else if (caractere == '+' || caractere == '-' || caractere == '*') {
			return trataOperadorAritmetico(linha);
		} else if (caractere == '<' || caractere == '>' || caractere == '=' || caractere == '!') {
			return trataOperadoRelacional(linha);
		} else if (caractere == ';' || caractere == ',' || caractere == '(' || caractere == ')' || caractere == '.') {
			return trataPontuacao(linha);
		} else if (caractere == ' ') {
			getProximoCaracterValido(linha);
			return pegaToken(linhaArquivo);
		} else {
			throw new Exception("Caractere Invalido: " + caractere + " na linha: " + numeroLinha + ".");
		}
	}

	private static void getProximoCaracterValido(String linha) throws Exception {
		Character caractere;
		do {
			indexCaractereLinha++;
			if (indexCaractereLinha > linhaArquivo.length() - 1) {
				indexCaractereLinha = 0;
				if (arquivoTratado.hasNextLine()) {
					trataLinhaVazia();
				} else {
					throw new Exception("Chegou ao fim do arquivo. Não há mais tokens.");
				}
				break;
			} else {
				caractere = linha.charAt(indexCaractereLinha);
			}
		} while (caractere == ' ');
	}

	private static Token trataDigito(String linha) {
		StringBuilder numero = new StringBuilder();
		do {
			numero.append(linha.charAt(indexCaractereLinha));
			indexCaractereLinha++;
		} while ((indexCaractereLinha < linha.length()) && (isDigito(linha.charAt(indexCaractereLinha))));
		
		Token token = new Token();
		token.setSimbolo("snumero");
		token.setLexema(numero.toString());
		token.setLinha(numeroLinha);
		return token;
	}

	private static Token trataIdentificadorAndPalavraReservada(String linha) {
		StringBuilder id = new StringBuilder();
		do {
			id.append(linha.charAt(indexCaractereLinha));
			indexCaractereLinha++;
		} while ((indexCaractereLinha < linha.length()) && (isDigito(linha.charAt(indexCaractereLinha))
				|| isLetra(linha.charAt(indexCaractereLinha)) || linha.charAt(indexCaractereLinha) == '_'));

		Token token = new Token();
		token.setLexema(id.toString());
		token.setSimbolo(retornaSimbolo(id.toString()) != null ? retornaSimbolo(id.toString()) : "sidentificador");
		token.setLinha(numeroLinha);
		return token;
	}

	private static Token trataAtribuicao(String linha) {
		StringBuilder id = new StringBuilder();
		id.append(linha.charAt(indexCaractereLinha));

		Token token = new Token();
		token.setLexema(id.toString());
		token.setSimbolo(retornaSimbolo(id.toString()));
		token.setLinha(numeroLinha);

		indexCaractereLinha++;
		if (linha.charAt(indexCaractereLinha) == '=') {
			id.append(linha.charAt(indexCaractereLinha));
			token.setLexema(id.toString());
			token.setSimbolo(retornaSimbolo(id.toString()));
			indexCaractereLinha++;
		}
		return token;
	}

	private static Token trataOperadorAritmetico(String linha) {
		StringBuilder id = new StringBuilder();
		id.append(linha.charAt(indexCaractereLinha));

		Token token = new Token();
		token.setLexema(id.toString());
		token.setSimbolo(retornaSimbolo(id.toString()));
		token.setLinha(numeroLinha);

		indexCaractereLinha++;

		return token;
	}

	private static Token trataOperadoRelacional(String linha) throws Exception {
		Token token = new Token();
		StringBuilder id = new StringBuilder();
		
		if (linha.charAt(indexCaractereLinha) == '<' || linha.charAt(indexCaractereLinha) == '>') {
			id.append(linha.charAt(indexCaractereLinha));

			token.setLexema(id.toString());
			token.setSimbolo(retornaSimbolo(id.toString()));
			token.setLinha(numeroLinha);
			indexCaractereLinha++;
			if (linha.charAt(indexCaractereLinha) == '=') {
				id.append(linha.charAt(indexCaractereLinha));
				token.setLexema(id.toString());
				token.setSimbolo(retornaSimbolo(id.toString()));
				indexCaractereLinha++;
			}
		} else if (linha.charAt(indexCaractereLinha) == '!') {
			indexCaractereLinha++;
			if (linha.charAt(indexCaractereLinha) == '=') {
				id.append(linha.charAt(indexCaractereLinha));

				token.setLexema(id.toString());
				token.setSimbolo(retornaSimbolo(id.toString()));
				token.setLinha(numeroLinha);
				indexCaractereLinha++;
			} else {
				throw new Exception("Não existe o comando : !");
			}
		} else if (linha.charAt(indexCaractereLinha) == '=') {
			id.append(linha.charAt(indexCaractereLinha));

			token.setLexema(id.toString());
			token.setSimbolo(retornaSimbolo(id.toString()));
			token.setLinha(numeroLinha);
			indexCaractereLinha++;
		}

		return token;
	}

	private static Token trataPontuacao(String linha) {
		StringBuilder id = new StringBuilder();
		id.append(linha.charAt(indexCaractereLinha));

		Token token = new Token();
		token.setLexema(id.toString());
		token.setSimbolo(retornaSimbolo(id.toString()));
		token.setLinha(numeroLinha);

		indexCaractereLinha++;

		return token;
	}

	private static boolean isDigito(char c) {
		return Character.toString(c).matches("^[0-9]*$");
	}

	private static boolean isLetra(char c) {
		return Character.toString(c).matches("[a-zA-Z]+");
	}
}
