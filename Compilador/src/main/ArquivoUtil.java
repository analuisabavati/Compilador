package main;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class ArquivoUtil {

	private final static String pathArquivo = "C:\\Users\\13022165\\Downloads\\teste.txt";
	protected final static String pathArquivoTratado = "C:\\Users\\13022165\\Downloads\\arquivoTratado.txt";
	private static int numeroLinha = 0;

	public static void gravaArquivoFonteTratado() throws Exception {
		Scanner leArquivo = abreArquivoFonte(pathArquivo);

		leArquivoFonte(leArquivo);

		leArquivo.close();
	}

	private static void leArquivoFonte(Scanner leArquivo) throws Exception {
		FileWriter arquivoTratado = new FileWriter(pathArquivoTratado);
		PrintWriter gravarArq = new PrintWriter(arquivoTratado);

		while (leArquivo.hasNextLine()) {
			String linhaArquivo = leArquivo.nextLine();
			String novaLinha = "";

			linhaArquivo = linhaArquivo.replaceAll("\t", "");

			if (linhaArquivo.length() > 0) {
				novaLinha = trataComentarioChaves(linhaArquivo);
			}
			if (novaLinha.length() > 1) {
				novaLinha = trataComentarioBarraAsterisco(novaLinha);
			}
			
			numeroLinha++;
			if(!novaLinha.trim().isEmpty()) {
				System.out.println(novaLinha);
				gravarArq.println(novaLinha);
			} else {
				System.out.println("");
				gravarArq.println("");
			}
		}

		arquivoTratado.close();
	}

	public static Scanner abreArquivoFonte(String path) throws Exception,
			FileNotFoundException {
		if (path == null) {
			throw new Exception("Caminho do arquivo invalido!");
		}

		Scanner leArquivo = new Scanner(new FileReader(path));
		return leArquivo;
	}

	public static String trataComentarioChaves(String linhaArquivo)
			throws Exception {
		int indexCaractereLinha = 0;
		char caractereLinha;
		String novaLinha = "";
		while (indexCaractereLinha < linhaArquivo.length()) {
			caractereLinha = linhaArquivo.charAt(indexCaractereLinha);
			if (caractereLinha == '}') {
				throw new Exception("Erro na linha " + numeroLinha
						+ ". Formato de comentario invalido!");
			}
			while (caractereLinha == '{') {
				if (caractereLinha == '{') {
					while (caractereLinha != '}') {
						indexCaractereLinha++;
						if (indexCaractereLinha == linhaArquivo.length()) {
							throw new Exception(
									"Erro! Comentario nao fechado na linha "
											+ numeroLinha);
						}
						caractereLinha = linhaArquivo
								.charAt(indexCaractereLinha);
					}
				}
			}
			if (caractereLinha != '}') {
				novaLinha = novaLinha + caractereLinha;
			}

			indexCaractereLinha++;
		}

		return novaLinha;
	}

	public static String trataComentarioBarraAsterisco(String linhaArquivo)
			throws Exception {
		int indexCaractereLinha = 0;
		char caractereLinha;
		char proximoCaractereLinha;
		String novaLinha = "";

		proximoCaractereLinha = linhaArquivo.charAt(indexCaractereLinha + 1);
		while (indexCaractereLinha < linhaArquivo.length()) {
			caractereLinha = linhaArquivo.charAt(indexCaractereLinha);
			if ((caractereLinha == '*')
					&& (linhaArquivo.charAt(indexCaractereLinha + 1) == '/')) {
				throw new Exception("Erro na linha " + numeroLinha
						+ ". Formato de comentario invalido!");
			}
			while ((caractereLinha == '/') && (proximoCaractereLinha == '*')) {
				if ((indexCaractereLinha + 2) >= linhaArquivo.length()) {
					throw new Exception("Erro na linha " + numeroLinha
							+ ". Comentario aberto e nao fechado!");
				}
				indexCaractereLinha += 2;
				caractereLinha = linhaArquivo.charAt(indexCaractereLinha);
				proximoCaractereLinha = linhaArquivo
						.charAt(indexCaractereLinha + 1);
				if ((caractereLinha == '*') && (proximoCaractereLinha == '/')) {
					indexCaractereLinha += 2;
					break;
				}
				while (proximoCaractereLinha != '/') {
					indexCaractereLinha++;
					caractereLinha = linhaArquivo.charAt(indexCaractereLinha);
					proximoCaractereLinha = linhaArquivo
							.charAt(indexCaractereLinha + 1);
					if ((caractereLinha == '*')
							&& (proximoCaractereLinha == '/')) {
						indexCaractereLinha += 2;
						break;
					}
				}
				indexCaractereLinha += 2;
				if ((indexCaractereLinha + 1) >= linhaArquivo.length()) {
					break;
				} else {
					caractereLinha = linhaArquivo.charAt(indexCaractereLinha);
				}
			}

			if ((indexCaractereLinha + 1) >= linhaArquivo.length()) {
				break;
			} else {
				indexCaractereLinha++;
				novaLinha = novaLinha + caractereLinha;
			}

			if ((indexCaractereLinha + 1) >= linhaArquivo.length()) {
				novaLinha = novaLinha
						+ linhaArquivo.charAt(indexCaractereLinha);
				break;
			} else {
				proximoCaractereLinha = linhaArquivo
						.charAt(indexCaractereLinha + 1);
			}
		}
		return novaLinha;
	}
}
