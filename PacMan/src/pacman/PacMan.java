package pacman;
import java.util.Scanner;
import java.util.Random;

/**
 *
 * @author Emanuel López
 */
public class PacMan {
    //Variables que serviran para almacenar la informacion:
    //MEDIDAS DEL TABLERO PEQUEÑO
    private static final int FILAS_PEQUENO = 5;
    private static final int COLUMNAS_PEQUENO =6;
    //MEDIDAS DEL TABLERO GRANDE
    private static final int FILAS_GRANDE =10;
    private static final int COLUMNAS_GRANDE=10;
    //TIPO DE PREMIOS QUE TENRA EL TABLERO
    private static final String fantasma = "@";
    private static final String premio = "0";
    private static final String premio_especial = "$";
    private static final String pared = "X";
    private static final String pacman = "<";
    //OBJETO DE LAS LIBRERIAS DE JAVA
    private static Scanner sc = new Scanner(System.in);
    private static Random rand = new Random();
    //VARIABLES DEL TOTAL DE VIDAS: para poder visualizar el menu de punteo
    public static int vidas = 3;
    public static int premiosRestante;
    public static int punteo;
    //Historial de Juego
    public static String[] historialNombre = new String[3];
    public static int[] historialPuntos = new int[3];
    public static int totalPartidas;
    
    public static void main(String[] args) { 
        //Variables para almacenar info. del usuario
        String nombreUsuario = ""; 
        int opcionInicio = 0;
        String tipoTablero = "";
       
        //CICLO DEL MENU PRINCIPAL
    while(opcionInicio!=3){
        System.out.println("Bienvenido a pacman:");
        System.out.println("Elija las opciones");
        System.out.println("Elija 1 para crear el tablero");
        System.out.println("Elija 2 para ver los puntos");
        System.out.println("Elija 3 para salir");
        opcionInicio = sc.nextInt();
        //METODO DE LA OPCION QUE ELIJA EL USUARIO: 
        opcionElegida(opcionInicio, nombreUsuario,tipoTablero);
    }    
    }
    //MÉTODO DE LAS POSIBLES OPCIONES QUE ELIGA EL USUARIO
    public static void opcionElegida(int opcionInicio, String nombreUsuario, String tipoTablero){
        //VARIABLES DONDE SE ALMACENARAN LAS CANTIDAD QUE USUARIO VA ESCOGER.
        int numeroPremiosEspecial;
        int numeroPremios;
        int numeroParedes;
        int numeroTrampas;
        switch (opcionInicio) {  
                //LAS OPCIONES CON SUS DIFERENTES METODOS DEL MENÚ
                case 1:
                    //CASO 1: CREAR TABLERO: Ingresando su nombre, tipo de Tablero, interaccion del usuario.
                    System.out.println("Ingrese su nombre: ");
                     nombreUsuario = sc.next();  
                     //TIPO DE TABLERO
                     System.out.println("P. Tablero de 5x6");
                     System.out.println("G. Tablero de 10x10");
                     System.out.println("Tipo de Tablero: ");
                     tipoTablero = sc.next();
                    int filas = 0;
                    int columnas = 0;
                    if(tipoTablero.equals("P")){
                        filas = FILAS_PEQUENO;
                        columnas = COLUMNAS_PEQUENO;
                    } else if(tipoTablero.equals("G")){
                        filas = FILAS_GRANDE;
                        columnas = COLUMNAS_GRANDE;
                    } else {
                        System.out.println("Tipo inválido, se usará pequeño por defecto");
                    }
                    //CANTIDAD DE PREMIOS, PAREDES, FANTASMAS QUE TENDRA EL TABLERO.
                    System.out.println("Por favor, ingresa los siguientes valores:");
                    
                    //Asigna premios
                    numeroPremios = asignarCantidades("premios", 0.4,filas,columnas);
                    numeroPremiosEspecial = asignarCantidades("premios Especiales", 0.4, filas, columnas);
                    
                    //Asigna paredes
                    numeroParedes = asignarCantidades("paredes",0.2,filas,columnas);
                    
                    //Asigna trampas
                    numeroTrampas = asignarCantidades("fantasmas",0.4,filas,columnas);
                    
                    System.out.println("Creando tablero...");
                    
                   //VARIABLE DONDE SE ALMACENARAN LOS DIFERENTES PREMIOS
                    premiosRestante = numeroPremios + numeroPremiosEspecial;
                    
                    //MATRIZ DIMENSIONAL DONDE SE ALMACENARAN LAS FILAS Y COLUMNAS
                    String[][] tablero = new String[filas][columnas];
                    //CICLO DONDE SE VA IR RELLENANDO LA MATRIZ
                    for(int i = 0; i < tablero.length ; i++){
                        for(int j = 0; j < tablero[0].length; j++){
                            tablero[i][j]= " ";
                        }
                    }
                    //ASIGNACION DE LOS PREMIOS, PAREDES, FANTASMAS.
                    //PREMIOS
                    colocarElemento(tablero, premio,numeroPremios);
                    colocarElemento(tablero, premio_especial,numeroPremiosEspecial);
                    //PAREDES
                    colocarElemento(tablero, pared, numeroParedes);
                    //FANTASMAS
                    colocarElemento(tablero,fantasma,numeroTrampas);
                    //MOSTRANDO EL TABLERO
                    imprimirTablero(tablero);
                    System.out.println();
                    
                    //ALMACENAMIENTO DE LOS DIFERENTES MOVIMIENTOS DEL PERSONAJE.
                    int personajeFila;
                    int personajeColumna;
                    //BUCLE PARA QUE MUESTRE EN DONDE VA IR COLOCANDO AL PACMAN
                    do{
                        System.out.println("Escoja donde desea colocar al personaje:");
                        System.out.print("Filas: ");
                        personajeFila = sc.nextInt();
                        System.out.print("Columnas: ");
                        personajeColumna = sc.nextInt();
                        //EN CASO QUE NO SEA VALIDA LA FILA Y LA COLUMNA
                        if(verificacionPosicionPersonaje(personajeFila,personajeColumna,tablero) == false){
                            System.out.println("Posicion no valida, escoja otro lugar");
                        }
                    }while(verificacionPosicionPersonaje(personajeFila,personajeColumna,tablero) == false);
                    
                    System.out.println("Personaje colocado exitosamente!");
                    tablero[personajeFila-1][personajeColumna-1] = "<";
                    
                    //EL LUGAR DONDE SE VA IR POSICIONANDO EL PERSONAJE: Y SE RESTA UN ESPACIO
                    int[] posicion = { personajeFila -1, personajeColumna -1 };
                    
                    String movimientoPersonaje;
                    boolean jugando = true;
                    do{ 
                        //Menú de nombre del usuario, vidas, puntos
                        EstadodeJuego(nombreUsuario);
                        imprimirTablero(tablero);
                        //MOVIMIENTOS QUE SE PUEDEN IR REALIZANDO
                        System.out.println("=================================");
                        System.out.println("Mueve tu personaje");
                        System.out.println("8. Arriba");
                        System.out.println("5. Abajo");
                        System.out.println("6. Derecha");
                        System.out.println("4. Izquierda");
                        System.out.print(">");
                        movimientoPersonaje = sc.next();
                        
                        //METODO SI EN UN DADO CASO DESEA PAUSAR EL JUEGO
                        if(movimientoPersonaje.equals("F")){
                            mostrarMenuPausa();
                            int opcionPausa;
                            //POSIBLES OPCIONES QUE EL USUARIO PUEDE HACER DURANTE LA PAUSA DEL JUEGO
                            System.out.println("Ingrese una opción Correcta: ");
                            opcionPausa = sc.nextInt();
                            System.out.println("");
                            //POSIBLES OPCIONES EN EL MENU DE PAUSA
                            switch (opcionPausa) {
                                case 3:
                                    System.out.println("Regresando...");
                                    break;
                                case 4:
                                    System.out.println("Juego Terminado...");
                                    //PARA QUE VAYA ALMACENANDO EL NOMBRE, EL TOTAL DE PUNTEOS
                                    historialNombre[totalPartidas] = nombreUsuario;
                                    historialPuntos[totalPartidas] = punteo;
                                     totalPartidas = totalPartidas +1;
                                    jugando = false;
                                    break;
                                default:
                                    System.out.println("Opción Invalida");       
                            }
                            continue;
                        }
                        //LLAMADA DE METODO MOVER PERSONAJE CON SUS PARAMETROS
                        moverPersonaje(tablero,posicion,movimientoPersonaje);
                        //CONDICIÓN PARA FINALIZAR LA PARTIDA SI EN DADO CASO SE QUEDA SIN VIDA
                        //O SE COME TODOS LOS PREMIOS
                        if(vidas<=0){
                            System.out.println("Te quedastes sin vidas!");
                            jugando = false;
                            historialNombre[totalPartidas] = nombreUsuario;
                            historialPuntos[totalPartidas] = punteo;
                            totalPartidas = totalPartidas +1;
                            
                        }else if(premiosRestante == 0){
                            System.out.println("Ganaste!");
                            historialNombre[totalPartidas] = nombreUsuario;
                            historialPuntos[totalPartidas] = punteo;
                            totalPartidas = totalPartidas +1;
                            jugando = false;
                        }
                        
                    }while(jugando);
                    break;
                case 2:
                    //MOSTRAR EL HISTORIAL DE LOS JUEGOS DEL USUARIO
                    mostrarHistorial();
                    break;
                case 3:
                    //TERMINARA EL PROGRAMA COMO TAL
                    opcionInicio = 0;
                    System.out.println("El Programa a Finalizado");
                    break;
                default:
                    System.out.println("Opción inválida. Intenta de nuevo");
        }
        
    }
    //Metodo para el movimiento de personaje
    public static boolean moverPersonaje(String[][] tablero, int[] posicion, String direccion){
        int filaActual = posicion[0];
        int columnaActual = posicion[1];
        
        int nuevaFila = filaActual;
        int nuevaColumna = columnaActual;
        //DEPENDE DEL MOVIMIENTO DEL USUARIO
        switch(direccion){
            case "8": //Arriba
                nuevaFila = nuevaFila -1;
                break;
            case "6": //Derecha
                nuevaColumna = nuevaColumna +1;
                break; 
            case "5": //Abajo
                nuevaFila = nuevaFila +1;
                break;
            case "4"://Izquierda
                nuevaColumna = nuevaColumna -1;
                break;
            default:
                System.out.println("Movimiento no válido");
                return false;
        }
        //Verificar Límites; DE LAS COLUMNAS
        //Cantidad de filas, columnas del tablero
        int filas = tablero.length;
        int columnas = tablero[0].length;
        //VERIFICACION DE LIMITE DE LA FILAS
        if(nuevaFila < 0){
            nuevaFila = filas - 1; //Sale arriba -> aparece abajo
        }else if(nuevaFila>= filas){
            nuevaFila = 0; //Sale abajo -> aparece arriba
        }
        //VERIFICACION DE LIMITES DE LA COLUMNAS
        if (nuevaColumna < 0) {
            nuevaColumna = columnas - 1; // Sale izquierda -> aparece derecha
        } 
        else if (nuevaColumna >= columnas) {
            nuevaColumna = 0;            // Sale derecha -> aparece izquierda
        }
        
        //EN CASO QUE A TOPA CON ALGO EN EL MOVIMIENTO NUEVO QUE SE LE ESTA DANDO
        if(tablero[nuevaFila][nuevaColumna].equals(fantasma)){
            System.out.println("==============================");
            System.out.println("Te atrapó un fantasma!");
            //SE VA RESTANDO LA VIDA
            vidas= vidas - 1;
            tablero [nuevaFila][nuevaColumna] = " ";
            
        } else if(tablero[nuevaFila][nuevaColumna].equals(premio)){
            System.out.println("==============================");
            System.out.println("Atrapaste un Premio!");
            //SUMA DE PUNTEO
            punteo = punteo + 10;
            premiosRestante = premiosRestante - 1;
            
        }else if(tablero[nuevaFila][nuevaColumna].equals(pared)){
            System.out.println("==============================");
            System.out.println("Te Atrapó una Pared");
            return false;
        }else if(tablero[nuevaFila][nuevaColumna].equals(premio_especial)){
            System.out.println("==============================");
            System.out.println("Atrapaste un Premio Especial");
            //SUMA DE PREMIOS ESPECIALES
            punteo = punteo + 15;
            premiosRestante = premiosRestante -1;
        }
        //IMPRIMIENDO LA NUEVA POSICIÓN Y ELIMINANDO LOS PREMIOS.
        tablero[filaActual][columnaActual]=" ";
        tablero[nuevaFila][nuevaColumna]=pacman;
        
        posicion[0] = nuevaFila;
        posicion[1] = nuevaColumna;
        
        return true;
}
    //METODO EN CASO QUE EL USUARIO PONGA EN PAUSA EL JUEGO
    public static void mostrarMenuPausa(){
    System.out.println("===== JUEGO EN PAUSA =====");
    System.out.println("3. Regresar");
    System.out.println("4. Terminar partida");

    }
    
    //METODO PARA IMPRIMIR EL TABLERO SEGUN EL TIPO DE FILA Y COLUMNA
    public static void imprimirTablero(String[][] tablero) {
        int filas = tablero.length;
        int columnas = tablero[0].length;
        // Borde superior
        for (int j = 0; j < columnas + 2; j++) {
            System.out.print("--");
        }
        System.out.println();
        // Filas del tablero
        for (int i = 0; i < filas; i++) {
            System.out.print("|");

            for (int j = 0; j < columnas; j++) {
                System.out.print(tablero[i][j] + " ");
            }
            System.out.println("|");
        }
        // Borde inferior
        for (int j = 0; j < columnas + 2; j++) {
            System.out.print("--");
        }
        System.out.println(); 
    }
     //Impresion del tabla de nombre del usuario, vidas, punteo
    public static void EstadodeJuego(String nombreUsuario){
        System.out.println("==============================");
        System.out.println("Usuario: " + nombreUsuario );
        System.out.println("Punteo: " + punteo);
        System.out.println("Vidas: " + vidas );
        System.out.println("==============================");
    }

    //Verificar la posicion del personaje en donde se va ir posicionando
    public static boolean verificacionPosicionPersonaje(int x, int y, String[][] tablero){
        if(tablero[x-1][y-1].equals(" ")){
            return true;
        }
        return false;
        
    }
    //Asignación de los premios del tablero pequeño
    public static int asignarCantidades(String objeto, double porcentaje, int filas, int columnas){
        boolean bandera = false;
        int cantidad = 1;
        while(bandera == false){
            System.out.print("Elige la cantidad de: "+objeto+": ");
            cantidad = sc.nextInt();
            bandera = true;
            int maxPremios = (int)(filas * columnas * porcentaje);
            if (cantidad < 1 || cantidad > maxPremios) {
                System.out.println("Por favor ingresa una cantidad correcta.");
                bandera = false;
            }  
        }
        return cantidad;
    }
    
    //Elementos del Tablero
    public static void colocarElemento(String[][] tablero, String simbolo, int cantidad) {
        int colocados = 0;
        int filas = tablero.length;
        int columnas = tablero[1].length;

        while (colocados < cantidad) {
            int fila = rand.nextInt(filas);
            int columna = rand.nextInt(columnas);

            if (tablero[fila][columna].equals(" ")) {
                tablero[fila][columna] = simbolo;
                colocados++;
            }
        }
    }
    //HISTORIAL DE PARTIDAS
    public static void mostrarHistorial(){
    System.out.println("===== HISTORIAL DE PARTIDAS =====");
    //EN CASO QUE ESTE EN 0 LAS PARTIDAS
    if(totalPartidas == 0){
        System.out.println("No hay movimientos registrados.");
        return;
    }
    //EN CASO QUE NO ESTE EN 0 EL HISTORIAL DE PARTIDAS; imprimira los datos 
    for(int i = totalPartidas - 1; i >= 0; i--){
        System.out.println(historialNombre[i] + " - Puntos: " + historialPuntos[i]);
    }
    System.out.println("=================================");
}
}