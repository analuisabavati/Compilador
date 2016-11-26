package main;

import java.util.HashMap;
import java.util.Map;

public class Operadores {

	private static Map<String, Integer> precedenciaOperadores = new HashMap<String, Integer>();
	static {
		precedenciaOperadores.put("(", 0);
		precedenciaOperadores.put(")", 0);
		precedenciaOperadores.put("unario", 7);
		precedenciaOperadores.put("nao", 7);
		precedenciaOperadores.put("*", 6);
		precedenciaOperadores.put("div", 6);		
		precedenciaOperadores.put("+", 5);
		precedenciaOperadores.put("-", 5);		
		precedenciaOperadores.put("<", 4);
		precedenciaOperadores.put("<=", 4);
		precedenciaOperadores.put(">", 4);
		precedenciaOperadores.put(">=", 4);
		precedenciaOperadores.put("=", 3);
		precedenciaOperadores.put("!=", 3);
		precedenciaOperadores.put("e", 2);
		precedenciaOperadores.put("ou", 1);
	}
	
	public static Integer getPrecedenciaOperadores(String operador) {
		return precedenciaOperadores.get(operador);
	}
	
}
