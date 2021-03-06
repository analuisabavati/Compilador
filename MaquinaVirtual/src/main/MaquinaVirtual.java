package main;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import static main.MnemonicoParametros.*;
import static main.ValidadorInstrucao.*;
import static main.MnemonicoMetodos.*;
import static gui.TelaPrincipal.*;

public class MaquinaVirtual {

	private static int numeroMaximoElementosLinha = 3;
	private static String path;
	private static boolean isListaInstrucoesVazia = true;
	private static List<Instrucao> pilhaInstrucoes = new ArrayList<>();
	private static int numeroLinha = 0;

	private static List<Integer> breakPoints = new ArrayList<>();

	public static List<Instrucao> leInstrucoesDoArquivo(String caminhoDoArquivo) throws Exception {

		if (caminhoDoArquivo == null) {
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

		switch (numeroElementosLinha) {
		case 1:
			if (isMnemonicoPrimeiroElemento(elementosLinha)) {
				instrucao.setMnemonico(elementosLinha.get(0));
			} else {
				throw new Exception(
						"Erro na linha: " + numeroLinha + ". Linha com somente um elemento deve ser um mnemonico!");
			}
			break;

		case 2:
			if (isMnemonicoComUmParametro(elementosLinha)) {
				instrucao.setMnemonico(elementosLinha.get(0));
				instrucao.setParametro1(elementosLinha.get(1));
			} else if (!isMnemonicoPrimeiroElemento(elementosLinha)) {
				if (isNotLabelInvalido(elementosLinha)) {
					instrucao.setLabel(elementosLinha.get(0));
					instrucao.setParametro1(elementosLinha.get(1));
				} else {
					throw new Exception(
							"Erro na linha: " + numeroLinha + ". Linha deve come�ar com um mnemonico ou label!");
				}
			} else {
				throw new Exception(
						"Erro na linha: " + numeroLinha + ". Linha deve come�ar com um mnemonico ou label!");
			}
			break;

		case 3:
			if (isMnemonicoComDoisParametros(elementosLinha)) {
				instrucao.setMnemonico(elementosLinha.get(0));
				instrucao.setParametro1(elementosLinha.get(1));
				instrucao.setParametro2(elementosLinha.get(2));
			}
			break;

		default:
			break;
		}

		return instrucao;
	}

	private static void verificaNumElementosLinha(StringTokenizer conteudoLinha) throws Exception {
		if (conteudoLinha.countTokens() > numeroMaximoElementosLinha) {
			throw new Exception("Erro na linha: " + numeroLinha + ". Numero de elementos por linha invalido!");
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

	private static List<String[]> montaListaInstrucoesParaPreencherTabela(List<Instrucao> pilhaInstrucoes) {
		List<String[]> listaInstrucoesParaTabela = new ArrayList<>();
		Integer numLinha = 0;
		for (Instrucao ins : pilhaInstrucoes) {
			listaInstrucoesParaTabela.add(new String[] { numLinha.toString(), ins.getLabel(), ins.getMnemonico(),
					ins.getParametro1() == null ? null : ins.getParametro1().toString(),
					ins.getParametro2() == null ? null : ins.getParametro2().toString() });
			numLinha++;
		}
		return listaInstrucoesParaTabela;
	}

	public static DefaultTableModel executaInstrucoes() throws Exception {
		if (isListaInstrucoesVazia) {
			throw new Exception("Lista de instrucoes vazia! Selecione um arquivo.");
		}
		List<Integer> pilhaDados = new ArrayList<>();

		DefaultTableModel model = null;

		for (int i = 0; i < pilhaInstrucoes.size() - 1; i++) {
			if (isLinhaBreakPoint(i)) {
				esperaBotaoContinuar();
			}

			switch (pilhaInstrucoes.get(i).getMnemonico() == null ? "NULL" : pilhaInstrucoes.get(i).getMnemonico()) {
			case "LDC":
				pilhaDados = execLDC(pilhaDados, toInteger(pilhaInstrucoes.get(i).getParametro1()));
				break;

			case "LDV":
				pilhaDados = execLDV(pilhaDados, toInteger(pilhaInstrucoes.get(i).getParametro1()));
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
				if (pilhaDados.get(pilhaDados.size() - 1) == 0) {
					JOptionPane.showMessageDialog(null, "Para continuar clique no bot�o \"OK\"!");
					i = pilhaInstrucoes.size() - 2;
				} else {
					pilhaDados = execDIVI(pilhaDados);
				}
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

			case "CDIF":
				pilhaDados = execCDIF(pilhaDados);
				break;

			case "STR":
				pilhaDados = execSTR(pilhaDados, toInteger(pilhaInstrucoes.get(i).getParametro1()));
				break;

			case "JMP":
				Instrucao instrucaoJMP = new Instrucao(pilhaInstrucoes.get(i).getParametro1(), null, "NULL", null);
				i = pilhaInstrucoes.indexOf(instrucaoJMP) - 1;
				break;

			case "JMPF":
				Instrucao instrucaoJMPF = new Instrucao(pilhaInstrucoes.get(i).getParametro1(), null, "NULL", null);
				int indexJMPF = pilhaInstrucoes.indexOf(instrucaoJMPF);
				if (pilhaDados.get(pegaTopo(pilhaDados)).equals(0)) {
					i = indexJMPF - 1;
				}
				pilhaDados.remove(pegaTopo(pilhaDados));
				break;

			case "RD":
				pilhaDados = execRD(pilhaDados);
				break;

			case "PRN":
				pilhaDados = execPRN(pilhaDados);
				break;

			case "ALLOC":
				pilhaDados = execALLOC(pilhaDados, toInteger(pilhaInstrucoes.get(i).getParametro1()),
						toInteger(pilhaInstrucoes.get(i).getParametro2()));
				break;

			case "DALLOC":
				pilhaDados = execDALLOC(pilhaDados, toInteger(pilhaInstrucoes.get(i).getParametro1()),
						toInteger(pilhaInstrucoes.get(i).getParametro2()));
				break;

			case "CALL":
				pilhaDados.add(i + 1);

				Instrucao instrucaoCALL = new Instrucao(pilhaInstrucoes.get(i).getParametro1(), null, "NULL", null);
				i = pilhaInstrucoes.indexOf(instrucaoCALL) - 1;

				break;

			case "RETURN":
				i = pilhaDados.get(pilhaDados.size() - 1) - 1;
				pilhaDados.remove(pilhaDados.size() - 1);

				break;

			case "NULL":
				break;

			case "START":
				pilhaDados.add(null);
				break;

			case "HLT":
				break;

			default:
				break;
			}
			model = atualizaTabelaDados(pilhaDados);
			atualizaTabelaDadosTela(model);
		}

		return model;
	}

	private static DefaultTableModel atualizaTabelaDados(List<Integer> pilhaDados) {
		List<String[]> listaDadosParaTabela = montaListaDadosParaPreencherTabela(pilhaDados);
		String[] nomesColunas = { "Endere�o", "Valor" };
		DefaultTableModel model = new DefaultTableModel(
				listaDadosParaTabela.toArray(new String[listaDadosParaTabela.size()][]), nomesColunas);

		return model;
	}

	private static List<String[]> montaListaDadosParaPreencherTabela(List<Integer> pilhaDados) {
		List<String[]> listaInstrucoesParaTabela = new ArrayList<>();
		Integer numLinha = 0;
		for (Integer dado : pilhaDados) {
			listaInstrucoesParaTabela.add(new String[] { numLinha.toString(), dado == null ? null : dado.toString() });
			numLinha++;
		}
		return listaInstrucoesParaTabela;
	}

	private static Integer toInteger(String valor) {
		return Integer.parseInt(valor);
	}

	public static List<Integer> getBreakPoints() {
		return breakPoints;
	}

	public static void insereBreakPoint(Integer numeroLinha) {
		breakPoints.add(numeroLinha);
	}

	public static void removeBreakPoint(Integer numeroLinha) {
		breakPoints.remove(numeroLinha);
	}

	public static boolean isLinhaBreakPoint(Integer numeroLinha) {
		return breakPoints.contains(numeroLinha);
	}

}