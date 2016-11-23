package main;

public class Retorno {
	
	private String comando;
	private boolean retornado;
	private int nivel;
	
	public Retorno(String comando, boolean retornado, int nivel ){
		this.comando = comando;
		this.retornado = retornado;
		this.nivel = nivel;
	}
	
	public String getComando() {
		return comando;
	}
	public void setComando(String comando) {
		this.comando = comando;
	}
	public boolean isRetornado() {
		return retornado;
	}
	public void setRetornado(boolean retornado) {
		this.retornado = retornado;
	}
	public int getNivel() {
		return nivel;
	}
	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	@Override
	public String toString() {
		return "Retorno [comando=" + comando + ", retornado=" + retornado + ", nivel=" + nivel + "]";
	}
	
}
