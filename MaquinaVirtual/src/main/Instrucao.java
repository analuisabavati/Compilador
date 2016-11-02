package main;

public class Instrucao {
	
	private String label;
	private String mnemonico;
	private Integer parametro1;
	private Integer parametro2;
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getMnemonico() {
		return mnemonico;
	}
	public void setMnemonico(String m) {
		this.mnemonico = m;
	}
	public Integer getParametro1() {
		return parametro1;
	}
	public void setParametro1(Integer p1) {
		this.parametro1 = p1;
	}
	public Integer getParametro2() {
		return parametro2;
	}
	public void setParametro2(Integer p2) {
		this.parametro2 = p2;
	}
	@Override
	public String toString() {
		return "Instrucao [label=" + label + ", mnemonico=" + mnemonico + ", parametro1=" + parametro1 + ", parametro2="
				+ parametro2 + "]";
	}
	
}
