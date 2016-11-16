package main;

import java.util.HashMap;
import java.util.Map;

public class Operadores {

	private static Map<String, Integer> precedenciaOperadores = new HashMap<String, Integer>();
	static {
		precedenciaOperadores.put("(", 8);
		precedenciaOperadores.put(")", 8);
		precedenciaOperadores.put("unario", 7);
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
		precedenciaOperadores.put("AND", 2);
		precedenciaOperadores.put("OR", 1);
	}
	
	public static int getPrecedenciaOperadores(String operador) {
		return precedenciaOperadores.get(operador);
	}
	
}
