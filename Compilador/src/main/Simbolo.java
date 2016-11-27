package main;

public class Simbolo {
	
	private String lexema;
	private String tipo;
	private Integer nivel;
	private String rotulo;
	private String tipoLexema;
	private Integer endereco;
	
	public Integer getEndereco() {
		return endereco;
	}
	public void setEndereco(Integer endereco) {
		this.endereco = endereco;
	}
	public String getTipoLexema() {
		return tipoLexema;
	}
	public void setTipoLexema(String tipoLexema) {
		this.tipoLexema = tipoLexema;
	}
	public String getRotulo() {
		return rotulo;
	}
	public void setRotulo(String rotulo) {
		this.rotulo = rotulo;
	}
	public String getLexema() {
		return lexema;
	}
	public void setLexema(String lexema) {
		this.lexema = lexema;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public Integer getNivel() {
		return nivel;
	}
	public void setNivel(Integer nivel) {
		this.nivel = nivel;
	}
}
