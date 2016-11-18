package main;

public class Token {
	
	private String lexema;
	private String simbolo;
	private Integer linha;
	private boolean isUnario;
	
	public boolean isUnario() {
		return isUnario;
	}
	public void setUnario(boolean isUnario) {
		this.isUnario = isUnario;
	}
	public String getLexema() {
		return lexema;
	}
	public void setLexema(String l) {
		this.lexema = l;
	}
	public String getSimbolo() {
		return simbolo;
	}
	public void setSimbolo(String s) {
		this.simbolo = s;
	}
	public Integer getLinha() {
		return linha;
	}
	public void setLinha(Integer l) {
		this.linha = l;
	}
}
