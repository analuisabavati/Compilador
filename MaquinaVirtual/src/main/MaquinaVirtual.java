package main;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;

import static main.MnemonicoParametros.*;
import static main.ValidadorInstrucao.*;
import static main.MnemonicoMetodos.*;

public class MaquinaVirtual {

	private static int numeroMaximoElementosLinha = 3;
	private static String path;
	private static boolean isListaInstrucoesVazia = true;
	private static List<Instrucao> pilhaInstrucoes = new ArrayList<>();
	private static int numeroLinha = 0;
	
	/*
	 * TODO: 
	 * Conferir msg de erros 
	 * Fazer breakpoints 
	 */

	public static List<Instrucao> leInstrucoesDoArquivo(String caminhoDoArquivo) throws Exception {
		
		if(caminhoDoArquivo == null) {
			throw new Exception("Caminho do arquivo invalido!");
		}
		
		List<Instrucao> instrucoes = new ArrayList<>();
		Scanner leArquivo = new Scanner(new FileReader(caminhoDoArquivo));
		while (leArquivo.hasNextLine()) {
			String linhaArquivo = leArquivo.nextLine();
			numeroLinha++;
			StringTokenizer conteudoLinha = new StringTokenizer(linhaArquivo, ", ");
			verificaNumElementosLinha(conteudoLinha);
			Instrucao instrucao = montaInstrucao(conteudoLinha);
			instrucoes.add(instrucao);
		}
		leArquivo.close();
		verificaStart(instrucoes);
		verificaHlt(instrucoes);
		return instrucoes;
	}

	private static Instrucao montaInstrucao(StringTokenizer conteudoLinha) throws Exception {

		List<String> elementosLinha = new ArrayList<>();
		Instrucao instrucao = new Instrucao();

		while (conteudoLinha.hasMoreTokens()) {
			elementosLinha.add(conteudoLinha.nextToken());
		}
		
		Integer numeroElementosLinha = elementosLinha.size();
		
		switch(numeroElementosLinha) {
			case 1:	if(isMnemonicoPrimeiroElemento(elementosLinha)) {
						instrucao.setMnemonico(elementosLinha.get(0));
					}
					else {
						throw new Exception ("Erro na linha: "+numeroLinha+". Linha com somente um elemento deve ser um mnemonico!");
					}
					break;
					
			case 2: if(isMnemonicoComUmParametro(elementosLinha)) {
						instrucao.setMnemonico(elementosLinha.get(0));
						instrucao.setParametro1(toInteger(elementosLinha.get(1)));
					} else if(!isMnemonicoPrimeiroElemento(elementosLinha)) {
						if(isNotLabelInvalido(elementosLinha)) {
							instrucao.setLabel(elementosLinha.get(0));
							instrucao.setParametro1(toInteger(elementosLinha.get(1)));
						} else {
							throw new Exception ("Erro na linha: "+numeroLinha+". Linha deve começar com um mnemonico ou label!");
						}
					} else {
						throw new Exception ("Erro na linha: "+numeroLinha+". Linha deve começar com um mnemonico ou label!");
					}
					break;
				
			case 3:	if(isMnemonicoComDoisParametros(elementosLinha)) {
						instrucao.setMnemonico(elementosLinha.get(0));
						instrucao.setParametro1(toInteger(elementosLinha.get(1)));
						instrucao.setParametro2(toInteger(elementosLinha.get(2)));
					}
					break;
				
			default: break;
		}

		return instrucao;
	}

	private static void verificaNumElementosLinha(StringTokenizer conteudoLinha) throws Exception {
		if (conteudoLinha.countTokens() > numeroMaximoElementosLinha) {
			throw new Exception ("Erro na linha: "+numeroLinha+". Numero de elementos por linha invalido!");
		}
	}
	
	private static boolean isNotLabelInvalido(List<String> elementosLinha) {
		return !(elementosLinha.get(0).equals("NULL")) && elementosLinha.get(1).equals("NULL");
	}

	public static void procurarArquivo(JFrame janela) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = fileChooser.showOpenDialog(janela);
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			path = selectedFile.getAbsolutePath();
		}
	}

	public static DefaultTableModel montaDadosTabelaInstrucao() throws Exception {

		pilhaInstrucoes = leInstrucoesDoArquivo(path);

		List<String[]> listaInstrucoesParaTabela = montaListaInstrucoesParaPreencherTabela(pilhaInstrucoes);
		String[] nomesColunas = { "Linha", "Label", "Mnemonico", "Parametro 1", "Parametro 2" };
		DefaultTableModel model = new DefaultTableModel(
				listaInstrucoesParaTabela.toArray(new String[listaInstrucoesParaTabela.size()][]), nomesColunas);

		isListaInstrucoesVazia = false;
		
		return model;
	}

	private static List<String[]> montaListaInstrucoesParaPreencherTabela(List<Instrucao>  pilhaInstrucoes) {
		List<String[]> listaInstrucoesParaTabela = new ArrayList<>();
		Integer numLinha = 0;
		for (Instrucao ins : pilhaInstrucoes) {
			listaInstrucoesParaTabela.add(new String[] { numLinha.toString(), ins.getLabel(), ins.getMnemonico(),
					ins.getParametro1() == null ? null : ins.getParametro1().toString(), ins.getParametro2() == null ? null : ins.getParametro2().toString() });
			numLinha++;
		}
		return listaInstrucoesParaTabela;
	}
	
	public static DefaultTableModel executaInstrucoes() throws Exception {
		if (isListaInstrucoesVazia) {
			throw new Exception ("Erro na linha: "+numeroLinha+". Lista de instrucoes vazia! Selecione um arquivo.");
		}
		List<Integer> pilhaDados = new ArrayList<>();
		 
		for (Instrucao instrucao : pilhaInstrucoes) {
			switch (instrucao.getMnemonico()) {
			case "LDC":
				pilhaDados = execLDC(pilhaDados, instrucao.getParametro1());
				break;

			case "LDV":
				pilhaDados = execLDV(pilhaDados, instrucao.getParametro1());
				break;

			case "ADD":
				pilhaDados = execADD(pilhaDados);
				break;

			case "SUB":
				pilhaDados = execSUB(pilhaDados);
				break;

			case "MULT":
				pilhaDados = execMULT(pilhaDados);
				break;

			case "DIVI":
				pilhaDados = execDIVI(pilhaDados);
				break;

			case "INV":
				pilhaDados = execINV(pilhaDados);
				break;

			case "AND":
				pilhaDados = execAND(pilhaDados);
				break;

			case "OR":
				pilhaDados = execOR(pilhaDados);
				break;

			case "NEG":
				pilhaDados = execNEG(pilhaDados);
				break;

			case "CME":
				pilhaDados = execCME(pilhaDados);
				break;

			case "CMA":
				pilhaDados = execCMA(pilhaDados);
				break;

			case "CEQ":
				pilhaDados = execCEQ(pilhaDados);
				break;

			case "CMEQ":
				pilhaDados = execCMEQ(pilhaDados);
				break;

			case "CMAQ":
				pilhaDados = execCMAQ(pilhaDados);
				break;

			case "STR":
				pilhaDados = execSTR(pilhaDados, instrucao.getParametro1());
				break;

			case "JMP":																				// certo ??
				//pilhaDados = execJMP(pilhaDados, instrucao.getParametro1());
				int indexJMP = pilhaInstrucoes.indexOf(instrucao.getParametro1().equals(instrucao.getLabel()));
				instrucao = pilhaInstrucoes.get(indexJMP);
				break;

			case "JMPF":																	// certo ??
			//	pilhaDados = execJMFP(pilhaDados, instrucao.getParametro1());
				int indexJMPF = pilhaInstrucoes.indexOf(instrucao.getParametro1().equals(instrucao.getLabel()));
				instrucao = pilhaInstrucoes.get(indexJMPF);
				break;

			case "RD":
				pilhaDados = execRD(pilhaDados);
				break;

			case "PRN":
				pilhaDados = execPRN(pilhaDados);
				break;

			case "ALLOC":
				pilhaDados = execALLOC(pilhaDados, instrucao.getParametro1(), instrucao.getParametro2());
				break;

			case "DALLOC":
				pilhaDados = execDALLOC(pilhaDados, instrucao.getParametro1(), instrucao.getParametro2());
				break;

			case "CALL":
				pilhaDados = execCALL(pilhaDados, instrucao.getParametro1());
				break;

			case "RETURN":
				pilhaDados = execRETURN(pilhaDados);
				break;

			default:
				break;
			}
		}
		
		return atualizaTabelaDados(pilhaDados);
	}

	private static DefaultTableModel atualizaTabelaDados(List<Integer>  pilhaDados) {	
		List<String[]> listaDadosParaTabela = montaListaDadosParaPreencherTabela(pilhaDados);
		String[] nomesColunas = { "Endereço", "Valor"};
		DefaultTableModel model = new DefaultTableModel(
				listaDadosParaTabela.toArray(new String[listaDadosParaTabela.size()][]), nomesColunas);
		
		return model;
	}
	
	private static List<String[]> montaListaDadosParaPreencherTabela(List<Integer>  pilhaDados) {
		List<String[]> listaInstrucoesParaTabela = new ArrayList<>();
		Integer numLinha = 0;
		for (Integer dado : pilhaDados) {
			listaInstrucoesParaTabela.add(new String[] { numLinha.toString(), dado.toString()});
			numLinha++;
		}
		return listaInstrucoesParaTabela;
	}
	
	private static Integer toInteger(String valor) {
		return Integer.parseInt(valor);
	}
	
	
	
}