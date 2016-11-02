package main;

public class Token {
	
	private String lexema;
	private String simbolo;
	private Integer linha;
	
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
