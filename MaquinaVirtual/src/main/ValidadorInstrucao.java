package main;

import java.util.List;

public class ValidadorInstrucao {

	public static void verificaStart(List<Instrucao> instrucoes) throws Exception {

		int cont = 0;

		for (Instrucao inst : instrucoes) {
			if (cont == 0 && !inst.getMnemonico().equals("START")) {
				throw new Exception("O primeiro elemento tem que ser START.");
			} else if (cont != 0 && inst.getMnemonico() != null && inst.getMnemonico().equals("START")) {
				throw new Exception("Apenas o primeiro elemento deve ser START.");
			}
			cont++;
		}
	}

	public static void verificaHlt(List<Instrucao> instrucoes) throws Exception {

		int cont = 0;

		for (Instrucao inst : instrucoes) {
			if (cont == instrucoes.size() - 1 && !inst.getMnemonico().equals("HLT")) {
				throw new Exception("O ultimo elemento tem que ser HLT.");
			} else if (cont != instrucoes.size() - 1 && inst.getMnemonico() != null && inst.getMnemonico().equals("HLT")) {
				throw new Exception("Apenas o ultimo elemento deve ser HLT.");
			}
			cont++;
		}
	}
}
