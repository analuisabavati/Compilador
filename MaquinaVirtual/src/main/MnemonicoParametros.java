package main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MnemonicoParametros {

	public static boolean isMnemonicoComUmParametro(List<String> elementosLinha) {
		return isMnemonicoPrimeiroElemento(elementosLinha)
				&& (retornaNumParametrosIntrucao(elementosLinha.get(1)) == null);
	}

	public static boolean isMnemonicoPrimeiroElemento(List<String> elementosLinha) {
		return retornaNumParametrosIntrucao(elementosLinha.get(0)) != null;
	}

	public static boolean isMnemonicoComDoisParametros(List<String> elementosLinha) {
		return isMnemonicoPrimeiroElemento(elementosLinha) 
				&& (retornaNumParametrosIntrucao(elementosLinha.get(1)) == null)
				&& (retornaNumParametrosIntrucao(elementosLinha.get(2)) == null);
	}

	public static Integer retornaNumParametrosIntrucao(String mnemonico) {
		return (Integer) montaMapInstrucoesNumParametros().get(mnemonico);
	}

	public static Map<String, Integer> montaMapInstrucoesNumParametros() {
		Map<String, Integer> namesMap = new HashMap<String, Integer>();
		// Mnemonico , Numero de Parametros
		namesMap.put("LDC", 1);
		namesMap.put("LDV", 1);
		namesMap.put("ADD", 0);
		namesMap.put("SUB", 0);
		namesMap.put("MULT", 0);
		namesMap.put("DIVI", 0);
		namesMap.put("INV", 0);
		namesMap.put("AND", 0);
		namesMap.put("OR", 0);
		namesMap.put("NEG", 0);
		namesMap.put("CME", 0);
		namesMap.put("CMA", 0);
		namesMap.put("CEQ", 0);
		namesMap.put("CMEQ", 0);
		namesMap.put("CMAQ", 0);
		namesMap.put("CDIF", 0);
		namesMap.put("START", 0);
		namesMap.put("HLT", 0);
		namesMap.put("STR", 1);
		namesMap.put("JMP", 1);
		namesMap.put("JMPF", 1);
		namesMap.put("NULL", 0);
		namesMap.put("RD", 0);
		namesMap.put("PRN", 0);
		namesMap.put("ALLOC", 2);
		namesMap.put("DALLOC", 2);
		namesMap.put("CALL", 1);
		namesMap.put("RETURN", 0);
		return namesMap;
	}

}
