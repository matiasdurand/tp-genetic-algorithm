import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class Calculador {

	//Parametros modificables
	private final int CANT_COLORES = 23;
	private final int PENALIDAD = 1;
	private final int TAM_POBLACION = 50;
	private final int CANT_TORNEOS = 2;
	private final int CANT_COMPETIDORES = 5;
	private final int PROB_MUTACION = 2;
	private final int F = 4;
	
	private int[][] grafo = new int[23][23];
	private Individuo[] poblacion = new Individuo[TAM_POBLACION];
	private Individuo masApto;
	
	public void resolver() {
		inicializarGrafo();
		inicializarPoblacion();
		
		masApto = (Individuo) Arrays.asList(poblacion).stream().min(Comparator.naturalOrder()).get();
		
		do {
			Individuo[] seleccionados = seleccionar();
			
			Individuo[] individuos = cruzar(seleccionados);

			if(new Random().nextInt(PROB_MUTACION) == 0) mutar(individuos[2]);
			if(new Random().nextInt(PROB_MUTACION) == 0) mutar(individuos[3]);
			
			reemplazar(individuos);
			
			guardarMasApto();
			
			System.out.println("--------------------------------------------------" );
		}
		while (masApto.aptitud > F);
		
		System.out.println("Solucion final:\n" + masApto);
	}
	
	private void inicializarGrafo() {
		int[][] adyacencias = {	{0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
								{1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
								{0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
								{0, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
								{0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
								{0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
								{0, 0, 0, 1, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
								{0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
								{0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0},
								{0, 0, 0, 0, 0, 1, 1, 0, 1, 0, 1, 0, 0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0},
								{0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
								{0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
								{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
								{0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
								{0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 1, 1, 0, 0, 0, 0, 1, 0},
								{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 0},
								{0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 1, 0, 0, 0, 1, 0},
								{0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 1, 1, 0},
								{0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0},
								{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
								{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0},
								{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 1, 0, 1},
								{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0}};
		grafo = adyacencias;
	}
	
	private void inicializarPoblacion() {
		for(int i=0; i<TAM_POBLACION; i++) {
			Individuo individuo = new Individuo();
			
			for(int x=0; x<23; x++) {
				int color = (int) ((Math.random()*(CANT_COLORES-1))+1);
				individuo.cromosoma[x] = color;
			}
			
			individuo.aptitud = aptitud(individuo.cromosoma);
			
			poblacion[i] = individuo;
		}
	}
	
	private int aptitud(Integer[] cromosoma) {
		int colores = colores(cromosoma);
		int penalidad = penalidad(cromosoma);
		return colores + penalidad;
	}
	
	private int colores(Integer[] cromosoma) {
		return new HashSet<>(Arrays.asList(cromosoma)).size();
	}
	
	private int penalidad(Integer[] cromosoma) {
		int penalidad = 0;
		
		for(int i=0; i<23; i++) {
			for(int j=0; j<i; j++) {
				if((grafo[i][j] == 1) && (cromosoma[i] == cromosoma[j])) {
					penalidad += PENALIDAD;
				}
			}
		}
		
		return penalidad;
	}
	
	private Individuo[] seleccionar() {
		System.out.println("Seleccionando individuos..");
		
		Individuo[] seleccionados = new Individuo[CANT_TORNEOS];
		
		System.out.println("Ganadores de torneos:");
		
		for(int x=0; x<CANT_TORNEOS; x++) {
			seleccionados[x] = torneo();
		}
		
		return seleccionados;
	}
	
	private Individuo torneo() {
		List<Integer> indices = new ArrayList<Integer>();
		List<Individuo> competidores = new ArrayList<Individuo>();
		
		while(competidores.size()<CANT_COMPETIDORES) {
			int indice = (int) (Math.random()*(TAM_POBLACION-1));
			
			if(!indices.contains(Integer.valueOf(indice))) {
				indices.add(Integer.valueOf(indice));
				competidores.add(poblacion[indice]);
			}
		}
		
		Individuo ganador = (Individuo) competidores.stream().min(Comparator.naturalOrder()).get();

		System.out.println(ganador);
		
		return ganador;
	}
	
	private Individuo[] cruzar(Individuo[] seleccionados) {
		System.out.println("\nReproduccion:");
		Individuo[] individuos = new Individuo[4];
		
		individuos[0] = seleccionados[0];
		individuos[1] = seleccionados[1];
		
		Integer[] cromosoma1 = seleccionados[0].cromosoma;
		Integer[] cromosoma2 = seleccionados[1].cromosoma;
		
		Individuo hijo1 = new Individuo();
		Individuo hijo2 = new Individuo();
		
		Integer[] cromosomaHijo1 = new Integer[23];
		Integer[] cromosomaHijo2 = new Integer[23];
		
		int puntoCorte = obtenerPuntoCorte(cromosoma1);
		
		for(int x=0; x<23; x++) {
			if(x <= puntoCorte) {
				cromosomaHijo1[x] = cromosoma1[x];
			}
			else cromosomaHijo1[x] = cromosoma2[x];
		}
		
		for(int x=0; x<23; x++) {
			if(x <= puntoCorte) {
				cromosomaHijo2[x] = cromosoma2[x];
			}
			else cromosomaHijo2[x] = cromosoma1[x];
		}
		
		hijo1.cromosoma = cromosomaHijo1;
		hijo2.cromosoma = cromosomaHijo2;
		
		hijo1.aptitud = aptitud(hijo1.cromosoma);
		hijo2.aptitud = aptitud(hijo2.cromosoma);
		
		if(individuos[0].generacion > individuos[1].generacion) {
			hijo1.generacion = individuos[0].generacion+1;
			hijo2.generacion = individuos[0].generacion+1;
		}
		else {
			hijo1.generacion = individuos[1].generacion+1;
			hijo2.generacion = individuos[1].generacion+1;
		}
		
		individuos[2] = hijo1;
		individuos[3] = hijo2;
		
		System.out.println("Hijos generados (punto de corte en "+ puntoCorte +"):\n" + hijo1 + "\n" + hijo2);
		
		return individuos;
	}
	
	private int obtenerPuntoCorte(Integer[] cromosoma) {
		for(int i=0; i<23; i++) {
			for(int j=0; j<23; j++) {
				if((grafo[i][j] == 1) && (cromosoma[i] == cromosoma[j])) {
					return i;
				}
			}
		}
		return (int) (Math.random()*22);
	}
	
	private Individuo mutar(Individuo individuo) {
		System.out.println("\nMutacion:");
		Integer[] cromosoma = individuo.cromosoma;
		
		System.out.println(individuo);
		int[] posiciones = obtenerPosicionesInvalidas(cromosoma);
		
		if(posiciones != null) {
			System.out.println("Individuo inviable. Provincias en conflicto: " + posiciones[0] + " y " + posiciones[1]);
			int color;
			do {
				color = (int) ((Math.random()*(CANT_COLORES-1))+1);
			}
			while(cromosoma[posiciones[0]] == color);
			
			Integer[] opcion1 = new Integer[23];
			Integer[] opcion2 = new Integer[23];
			
			System.arraycopy(cromosoma, 0, opcion1, 0, 23);
			System.arraycopy(cromosoma, 0, opcion1, 0, 23);
			
			opcion1[posiciones[0]] = color;
			opcion2[posiciones[1]] = color;
			
			if (penalidad(opcion1) <= penalidad(opcion2)) cromosoma = opcion1;
			else cromosoma = opcion2;
		}
		else {
			System.out.println("Individuo viable");
			int posicion = (int) (Math.random()*22);
			
			int color1 = cromosoma[posicion];
			
			do {
				posicion = (int) (Math.random()*22);
			}
			while(cromosoma[posicion] == color1);
			
			int color2 = cromosoma[posicion];
			
			System.out.println("Se reemplaza el color " + color2 + " por el color " + color1);
			
			for(int x=0; x<23; x++) {
				if (cromosoma[x] == color2) cromosoma[x] = color1; 
			}
		}
		
		individuo.cromosoma = cromosoma;
		individuo.aptitud = aptitud(cromosoma);
		
		System.out.println("Individuo mutado: " + individuo);
		
		return individuo;
	}
	
	private int[] obtenerPosicionesInvalidas(Integer[] cromosoma) {
		for(int i=0; i<23; i++) {
			for(int j=0; j<23; j++) {
				if((grafo[i][j] == 1) && (cromosoma[i] == cromosoma[j])) {
					int[] posiciones = new int[2];
					posiciones[0] = i;
					posiciones[1] = j;
					return posiciones;
				}
			}
		}
		return null;
	}
	
	private void reemplazar(Individuo[] individuos) {
		System.out.println("\nReemplazo:\n");
		
		Individuo padre1 = individuos[0];
		Individuo padre2 = individuos[1];
		Individuo hijo1 = individuos[2];
		Individuo hijo2 = individuos[3];
		
		int indice1 = Arrays.asList(poblacion).indexOf(padre1);
		int indice2 = Arrays.asList(poblacion).indexOf(padre2);
	
		List<Individuo> listaIndividuos = Arrays.asList(individuos);
		Collections.sort(listaIndividuos);
		
		if(listaIndividuos.get(0).equals(hijo1) || listaIndividuos.get(0).equals(hijo2)) {
			if(listaIndividuos.get(2).equals(padre1) || listaIndividuos.get(3).equals(padre1)) {
				System.out.println(listaIndividuos.get(0) + " reemplaza a " + poblacion[indice1] + "\n");
				poblacion[indice1] = listaIndividuos.get(0);
			}
			
			if(listaIndividuos.get(2).equals(padre2) || listaIndividuos.get(3).equals(padre2)) {
				System.out.println(listaIndividuos.get(0) + " reemplaza a " + poblacion[indice2] + "\n");
				poblacion[indice2] = listaIndividuos.get(0);
			}
		}
		
		if(listaIndividuos.get(1).equals(hijo1) || listaIndividuos.get(1).equals(hijo2)) {
			if(listaIndividuos.get(2).equals(padre1)) {
				System.out.println(listaIndividuos.get(1) + " reemplaza a " + poblacion[indice1] + "\n");
				poblacion[indice1] = listaIndividuos.get(1);
			}
			
			if(listaIndividuos.get(3).equals(padre2)) {
				System.out.println(listaIndividuos.get(1) + " reemplaza a " + poblacion[indice2] + "\n");
				poblacion[indice2] = listaIndividuos.get(1);
			}
		}
	}
	
	private void guardarMasApto() {
		Individuo masApto = (Individuo) Arrays.asList(poblacion).stream().min(Comparator.naturalOrder()).get();
		if(!this.masApto.equals(masApto)) this.masApto = masApto;
	}
}
