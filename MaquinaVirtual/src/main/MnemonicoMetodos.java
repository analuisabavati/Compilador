package main;

import java.util.List;
import static gui.TelaPrincipal.*;

public class MnemonicoMetodos {
	
	/*
	 * TODO:
	 * Finalizar os metodos (jmp, etc)
	 */

	public static List<Integer> execLDC(List<Integer> pilhaDados, Integer constante) {
		pilhaDados.add(constante);
		return pilhaDados;
	}

	public static List<Integer> execLDV(List<Integer> pilhaDados, Integer posicao) {
		pilhaDados.add(pilhaDados.get(posicao));
		return pilhaDados;
	}

	public static List<Integer> execADD(List<Integer> pilhaDados) {
		Integer valorTopo = pilhaDados.remove(pegaTopo(pilhaDados));
		Integer valorAntesTopo = pilhaDados.get(pegaTopo(pilhaDados));
		pilhaDados.set(pegaTopo(pilhaDados), valorTopo + valorAntesTopo);
		return pilhaDados;
	}

	public static List<Integer> execSUB(List<Integer> pilhaDados) {
		Integer valorTopo = pilhaDados.remove(pegaTopo(pilhaDados));
		Integer valorAntesTopo = pilhaDados.get(pegaTopo(pilhaDados));
		pilhaDados.set(pegaTopo(pilhaDados), valorAntesTopo - valorTopo);
		return pilhaDados;
	}

	public static List<Integer> execMULT(List<Integer> pilhaDados) {
		Integer valorTopo = pilhaDados.remove(pegaTopo(pilhaDados));
		Integer valorAntesTopo = pilhaDados.get(pegaTopo(pilhaDados));
		pilhaDados.set(pegaTopo(pilhaDados), valorAntesTopo * valorTopo);
		return pilhaDados;
	}

	public static List<Integer> execDIVI(List<Integer> pilhaDados) {
		Integer valorTopo = pilhaDados.remove(pegaTopo(pilhaDados));
		Integer valorAntesTopo = pilhaDados.get(pegaTopo(pilhaDados));
		pilhaDados.set(pegaTopo(pilhaDados), valorAntesTopo/valorTopo);
		return pilhaDados;
	}

	public static List<Integer> execINV(List<Integer> pilhaDados) {
		pilhaDados.set(pegaTopo(pilhaDados), pilhaDados.get(pegaTopo(pilhaDados)) * (-1));
		return pilhaDados;
	}

	public static List<Integer> execAND(List<Integer> pilhaDados) {
		Integer valorTopo = pilhaDados.remove(pegaTopo(pilhaDados));
		Integer valorAnteriorTopo = pilhaDados.get(pegaTopo(pilhaDados));
		pilhaDados.set(pegaTopo(pilhaDados), isUm(valorAnteriorTopo) && isUm(valorTopo) ?  0 : 1);	
		return pilhaDados;
	}

	public static List<Integer> execOR(List<Integer> pilhaDados) {
		Integer valorTopo = pilhaDados.remove(pegaTopo(pilhaDados));
		Integer valorAnteriorTopo = pilhaDados.get(pegaTopo(pilhaDados));
		pilhaDados.set(pegaTopo(pilhaDados), isUm(valorAnteriorTopo) || isUm(valorTopo) ?  0 : 1);	
		return pilhaDados;	
	}

	public static List<Integer> execNEG(List<Integer> pilhaDados) {
		Integer valorTopo = pilhaDados.get(pegaTopo(pilhaDados));
		pilhaDados.set(pegaTopo(pilhaDados), 1 - valorTopo);
		return pilhaDados;
	}

	public static List<Integer> execCME(List<Integer> pilhaDados) {
		Integer valorTopo = pilhaDados.remove(pegaTopo(pilhaDados));
		Integer valorAnteriorTopo = pilhaDados.get(pegaTopo(pilhaDados));
		pilhaDados.set(pegaTopo(pilhaDados), valorAnteriorTopo.compareTo(valorTopo) < 0 ?  1 : 0);	
		return pilhaDados;
	}

	public static List<Integer> execCMA(List<Integer> pilhaDados) {
		Integer valorTopo = pilhaDados.remove(pegaTopo(pilhaDados));
		Integer valorAnteriorTopo = pilhaDados.get(pegaTopo(pilhaDados));
		pilhaDados.set(pegaTopo(pilhaDados), valorAnteriorTopo.compareTo(valorTopo) > 0 ?  1 : 0);	
		return pilhaDados;
	}

	public static List<Integer> execCEQ(List<Integer> pilhaDados) {
		Integer valorTopo = pilhaDados.remove(pegaTopo(pilhaDados));
		Integer valorAnteriorTopo = pilhaDados.get(pegaTopo(pilhaDados));
		pilhaDados.set(pegaTopo(pilhaDados), valorAnteriorTopo.compareTo(valorTopo) == 0 ?  1 : 0);	
		return pilhaDados;
	}

	public static List<Integer> execCMEQ(List<Integer> pilhaDados) {
		Integer valorTopo = pilhaDados.remove(pegaTopo(pilhaDados));
		Integer valorAnteriorTopo = pilhaDados.get(pegaTopo(pilhaDados));
		pilhaDados.set(pegaTopo(pilhaDados), valorAnteriorTopo.compareTo(valorTopo) <= 0 ?  1 : 0);	
		return pilhaDados;
		
	}

	public static List<Integer> execCMAQ(List<Integer> pilhaDados) {
		Integer valorTopo = pilhaDados.remove(pegaTopo(pilhaDados));
		Integer valorAnteriorTopo = pilhaDados.get(pegaTopo(pilhaDados));
		pilhaDados.set(pegaTopo(pilhaDados), valorAnteriorTopo.compareTo(valorTopo) >= 0 ?  1 : 0);	
		return pilhaDados;
	}

	public static List<Integer> execSTR(List<Integer> pilhaDados, Integer posicao) {
		pilhaDados.set(posicao, pilhaDados.remove(pegaTopo(pilhaDados)));
		return pilhaDados;
	}

	public static List<Integer> execJMP(List<Integer> pilhaDados, Integer valor) {
		return pilhaDados;
	}

	public static List<Integer> execJMFP(List<Integer> pilhaDados, Integer valor) {
		return pilhaDados;
		
	}

	public static List<Integer> execRD(List<Integer> pilhaDados) {							// Verificar e faze método getJanelaEntrada();
		Integer valor = getJanelaEntrada(); 				
		return execLDC(pilhaDados, valor);
	}

	public static List<Integer> execPRN(List<Integer> pilhaDados) {
		Integer ultimoValor = pilhaDados.remove(pegaTopo(pilhaDados));
		printJanelaSaida(ultimoValor);
		return pilhaDados;
	}

	public static List<Integer> execALLOC(List<Integer> pilhaDados, Integer posicaoInicio, Integer quantidadeVariaveis) {
		for (int i = 0; i < quantidadeVariaveis; i++) {
			pilhaDados.add(null);
		}
		return pilhaDados;
	}

	public static List<Integer> execDALLOC(List<Integer> pilhaDados, Integer posicaoInicio, Integer quantidadeVariaveis) {
		int posicao = posicaoInicio;
		for (int i = 0; i < quantidadeVariaveis; i++) {
			pilhaDados.remove(posicao);
			posicao--;
		}
		return pilhaDados;
	}

	public static List<Integer> execCALL(List<Integer> pilhaDados, Integer parametro1) {
		return pilhaDados;
	}

	public static List<Integer> execRETURN(List<Integer> pilhaDados) {
		return pilhaDados;	
	}
	
	
	private static int pegaTopo(List<Integer> pilhaDados) {
		return pilhaDados.size() - 1;
	}
	
	private static boolean isUm(Integer valor) {
		return "1".equals(valor);
	}
	
}
