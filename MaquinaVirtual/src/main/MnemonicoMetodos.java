package main;

import java.util.List;

import javax.swing.JOptionPane;

import static gui.TelaPrincipal.*;

public class MnemonicoMetodos {
	

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
		pilhaDados.set(pegaTopo(pilhaDados), isUm(valorAnteriorTopo) && isUm(valorTopo) ?  1 : 0);	
		return pilhaDados;
	}

	public static List<Integer> execOR(List<Integer> pilhaDados) {
		Integer valorTopo = pilhaDados.remove(pegaTopo(pilhaDados));
		Integer valorAnteriorTopo = pilhaDados.get(pegaTopo(pilhaDados));
		pilhaDados.set(pegaTopo(pilhaDados), isUm(valorAnteriorTopo) || isUm(valorTopo) ?  1 : 0);	
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
	
	public static List<Integer> execCDIF(List<Integer> pilhaDados) {
		Integer valorTopo = pilhaDados.remove(pegaTopo(pilhaDados));
		Integer valorAnteriorTopo = pilhaDados.get(pegaTopo(pilhaDados));
		pilhaDados.set(pegaTopo(pilhaDados), !valorAnteriorTopo.equals(valorTopo) ?  1 : 0);	
		return pilhaDados;
	}

	public static List<Integer> execSTR(List<Integer> pilhaDados, Integer posicao) {
		pilhaDados.set(posicao, pilhaDados.remove(pegaTopo(pilhaDados)));
		return pilhaDados;
	}


	public static List<Integer> execRD(List<Integer> pilhaDados) {							
		Integer valor = Integer.parseInt(getJanelaEntrada()); 				
		return execLDC(pilhaDados, valor);
	}

	public static List<Integer> execPRN(List<Integer> pilhaDados) {
		Integer ultimoValor = pilhaDados.remove(pegaTopo(pilhaDados));
		printJanelaSaida(ultimoValor);
		return pilhaDados;
	}

	public static List<Integer> execALLOC(List<Integer> pilhaDados, Integer posicaoInicio, Integer quantidadeVariaveis) {
		
		for (int i = 0; i < quantidadeVariaveis; i++) {
			pilhaDados.add(posicaoInicio, null);
		}
		return pilhaDados;
	}

	public static List<Integer> execDALLOC(List<Integer> pilhaDados, Integer posicaoInicio, Integer quantidadeVariaveis) {	
		for (int i = 0; i < quantidadeVariaveis; i++) {		
			pilhaDados.remove(posicaoInicio.intValue());
		}
		return pilhaDados;
	}
	
	public static int pegaTopo(List<Integer> pilhaDados) {
		return pilhaDados.size() - 1;
	}
	
	private static boolean isUm(Integer valor) {
		return 1 == (valor);
	}
	
}
