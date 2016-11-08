package main;

import java.util.ArrayList;
import java.util.List;

public class BreakPoints {

	private List<Integer> breakPoints = new ArrayList<>();
	
	public void adicionaBreakPoint (Integer numeroLinha) {
		breakPoints.add(numeroLinha);
	}
	
	public void removeBreakPoint (Integer numeroLinha) {
		breakPoints.remove(numeroLinha);
	}
	
	public boolean isLinhaBreakPoint(Integer numeroLinha) {
		return breakPoints.contains(numeroLinha);
	}
	
}
