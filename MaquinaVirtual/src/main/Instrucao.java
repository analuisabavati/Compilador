package main;

public class Instrucao {
	
	private String label;
	private String mnemonico;
	private String parametro1;
	private String parametro2;
	
	public Instrucao(String label, String mnemonico, String parametro1, String parametro2) {
		this.label = label;
		this.mnemonico = mnemonico;
		this.parametro1 = parametro1;
		this.parametro2 = parametro2;
	}
	
	public Instrucao() {
		
	}

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
	public String getParametro1() {
		return parametro1;
	}
	public void setParametro1(String p1) {
		this.parametro1 = p1;
	}
	public String getParametro2() {
		return parametro2;
	}
	public void setParametro2(String p2) {
		this.parametro2 = p2;
	}
	@Override
	public String toString() {
		return "Instrucao [label=" + label + ", mnemonico=" + mnemonico + ", parametro1=" + parametro1 + ", parametro2="
				+ parametro2 + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ((mnemonico == null) ? 0 : mnemonico.hashCode());
		result = prime * result + ((parametro1 == null) ? 0 : parametro1.hashCode());
		result = prime * result + ((parametro2 == null) ? 0 : parametro2.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Instrucao other = (Instrucao) obj;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (mnemonico == null) {
			if (other.mnemonico != null)
				return false;
		} else if (!mnemonico.equals(other.mnemonico))
			return false;
		if (parametro1 == null) {
			if (other.parametro1 != null)
				return false;
		} else if (!parametro1.equals(other.parametro1))
			return false;
		if (parametro2 == null) {
			if (other.parametro2 != null)
				return false;
		} else if (!parametro2.equals(other.parametro2))
			return false;
		return true;
	}
	
}
