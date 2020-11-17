import java.util.Arrays;

public class Individuo implements Comparable<Individuo> {

	public Integer[] cromosoma = new Integer[23];
	public Integer aptitud;
	public int generacion = 1;
	
	@Override
	public int compareTo(Individuo o) {
		return aptitud.compareTo(o.aptitud);
	}

	@Override
	public String toString() {
		return "cromosoma = " + Arrays.toString(cromosoma);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((aptitud == null) ? 0 : aptitud.hashCode());
		result = prime * result + Arrays.hashCode(cromosoma);
		result = prime * result + generacion;
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
		Individuo other = (Individuo) obj;
		if (aptitud == null) {
			if (other.aptitud != null)
				return false;
		} else if (!aptitud.equals(other.aptitud))
			return false;
		if (!Arrays.equals(cromosoma, other.cromosoma))
			return false;
		if (generacion != other.generacion)
			return false;
		return true;
	}
}