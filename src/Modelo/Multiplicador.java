package Modelo;

import java.util.ArrayList;

/**
 *
 * @author joseleonec
 */
public class Multiplicador {

    public ArrayList<ArrayList<String[]>> iteraciones = new ArrayList<>();
    public int multiplicando, multiplicador, cantidadDeBits, resultadoDecimal;
    public String resultadoBinario;

    public ArrayList<ArrayList<String[]>> getIteraciones() {
        return iteraciones;
    }

    public int getMultiplicando() {
        return multiplicando;
    }

    public int getMultiplicador() {
        return multiplicador;
    }

    public int getCantidadDeBits() {
        return cantidadDeBits;
    }

    public void setCantidadDeBits(int cantidadDeBits) {
        this.cantidadDeBits = cantidadDeBits;
    }

    public int getResultadoDecimal() {
        return resultadoDecimal;
    }

    public String getResultadoBinario() {
        return resultadoBinario;
    }

    public Multiplicador(int multiplicando, int multiplicador, int cantidadDeBits) {
        this.multiplicando = multiplicando;
        this.multiplicador = multiplicador;
        this.cantidadDeBits = cantidadDeBits;
    }

//    Realiza la multiplicacion con los parametros pasados al instaciar el objeto y devuelve una descripcion paso a paso 
    public ArrayList<ArrayList<String[]>> multiplicarPasoAPaso() {
        try {
            if (esRepresentableEnCa2(multiplicando, cantidadDeBits) && esRepresentableEnCa2(multiplicador, cantidadDeBits) && esRepresentableEnCa2(multiplicador * multiplicando, cantidadDeBits)) {
                ArrayList<String[]> paso = new ArrayList<>();
                String md, mr;//md = multiplicando y mr = multiplicador, ambos son cadenas binarioas
                md = complementoA2(multiplicando, cantidadDeBits);
                mr = complementoA2(multiplicador, cantidadDeBits);
                String A, S, P;
                String operacion;
//            configuracion inicial -> ITERACION 0
                A = md + completarBits("", cantidadDeBits) + "0"; //completarBits("", nBits) -> genera uma cadena con 0's de longitud nBits 
                S = complementoA2(md) + completarBits("", cantidadDeBits) + "0";
                P = completarBits("", cantidadDeBits) + mr + "0";
//          Se guarda el estado de cada iteracion en un array
                paso.add(new String[]{"A", A});
                paso.add(new String[]{"S", S});
                paso.add(new String[]{"P", P});
                iteraciones.add((ArrayList<String[]>) new ArrayList<String[]>(paso));
                paso.clear();
//            COMIENZO DE LAS ITERACIONES
                for (int i = 1; i <= cantidadDeBits; i++) {
                    String finalTwoBits = P.substring(P.length() - 2);
                    switch (finalTwoBits) {
                        case "01":
                            operacion = "P <- P + A";
                            paso.add(new String[]{"A", A});
                            paso.add(new String[]{"P", P});
                            P = addBinaryWithoutFinalCarry(P, A);
                            paso.add(new String[]{operacion, P});
                            break;
                        case "10":
                            operacion = "P <- P + S";
                            paso.add(new String[]{"S", S});
                            paso.add(new String[]{"P", P});
                            P = addBinaryWithoutFinalCarry(P, S);
                            paso.add(new String[]{operacion, P});
                            break;
                    }
                    operacion = "P <- P >> 1";
                    P = desplazar(P);
                    paso.add(new String[]{operacion, P});
                    iteraciones.add((ArrayList<String[]>) new ArrayList<String[]>(paso));
                    paso.clear();
                }
//          Datos para la Presentacion del resultadoBinario
                resultadoBinario = P.substring(0, P.length() - 1);
                if (resultadoBinario.substring(0, 1).equalsIgnoreCase("1")) {
                    paso.add(new String[]{"Resultado Ca2", resultadoBinario});
                    resultadoBinario = complementoA2(resultadoBinario);
                    resultadoDecimal = (-Integer.parseInt(resultadoBinario, 2));
                } else {
                    paso.add(new String[]{"Resultado binario", resultadoBinario});
                    resultadoDecimal = Integer.parseInt(resultadoBinario, 2);
                }
                paso.add(new String[]{"Resultado decimal", resultadoDecimal + ""});
                iteraciones.add((ArrayList<String[]>) new ArrayList<String[]>(paso));
                paso.clear();
            } else {
                System.out.println("Al menos uno de los FACTORES o el PRODUCTO no se puede representar en complemento a 2 con TAN SOLO " + cantidadDeBits + " bits   ");
                return null;
            }
        } catch (Exception e) {
            System.out.println("No se pudo realizar el proceso. Revisar los campos.");
            return null;
        }

        return iteraciones;
    }

//    Realiza la multiplicacion con los parametros pasados al instaciar el objeto y devuelve solo el resultado 
    public int multiplicar() {
        if (esRepresentableEnCa2(multiplicando, cantidadDeBits) && esRepresentableEnCa2(multiplicador, cantidadDeBits) && esRepresentableEnCa2(multiplicador * multiplicando, cantidadDeBits)) {
            String md, mr;//md = multiplicando y mr = multiplicador, ambos son cadenas binarioas
            md = complementoA2(multiplicando, cantidadDeBits);
            mr = complementoA2(multiplicador, cantidadDeBits);
            String A, S, P;
//            configuracion inicial -> ITERACION 0
            A = md + completarBits("", cantidadDeBits) + "0"; //completarBits("", nBits) -> genera uma cadena con 0's de longitud nBits 
            S = complementoA2(md) + completarBits("", cantidadDeBits) + "0";
            P = completarBits("", cantidadDeBits) + mr + "0";
//          Se guarda el estado de cada iteracion en un array
//            COMIENZO DE LAS ITERACIONES
            for (int i = 1; i <= cantidadDeBits; i++) {
                String finalTwoBits = P.substring(P.length() - 2);
                switch (finalTwoBits) {
                    case "01":
//                        operacion = "P <- P + A";
                        P = addBinaryWithoutFinalCarry(P, A);
                        break;
                    case "10":
//                        operacion = "P <- P + S";
                        P = addBinaryWithoutFinalCarry(P, S);
                        break;
                }
//                operacion = "P <- P >> 1";
                P = desplazar(P);
            }
//          Datos para la Presentaciondel resultadoBinario
            resultadoBinario = P.substring(0, P.length() - 1);
            if (resultadoBinario.substring(0, 1).equalsIgnoreCase("1")) {
                resultadoBinario = complementoA2(resultadoBinario);
                resultadoDecimal = (-Integer.parseInt(resultadoBinario, 2));
            } else {
                resultadoDecimal = Integer.parseInt(resultadoBinario, 2);
            }
        } else {
            throw new IllegalArgumentException("Uno de los numeros no se puede representar en complemento a 2 con " + cantidadDeBits + " bits");
        }
        return resultadoDecimal;
    }

//  Muestra en consola la descripcion paso a paso del proceso
    public ArrayList<String[]> desplegarProceso() {
        ArrayList<String[]> pasos = new ArrayList<>();
        if (iteraciones != null && !iteraciones.isEmpty()) {
            int c = 0;
            for (ArrayList<String[]> iteracion : iteraciones) {
                if (c >= 1 && c <= cantidadDeBits) {
                    System.out.println("Iteracion: " + c);
                    pasos.add(new String[]{String.valueOf("Iteracion: " + c)});
                } else if (c == 0) {
                    System.out.println("Configuracion inicial");
                    pasos.add(new String[]{"Configuración Inicial"});
                } else {
                    System.out.println("RESULTADOS");
                    pasos.add(new String[]{"RESULTADOS"});
                }
                for (String[] o : iteracion) {
                    if (c >= 1 && c <= cantidadDeBits) {
                        System.out.println(o[0] + "\t\t" + o[1].substring(0, cantidadDeBits) + "\t" + o[1].substring(cantidadDeBits, cantidadDeBits * 2) + "\t" + o[1].substring(cantidadDeBits * 2));
                        pasos.add(new String[]{o[0], o[1].substring(0, cantidadDeBits), o[1].substring(cantidadDeBits, cantidadDeBits * 2), o[1].substring(cantidadDeBits * 2)});
                    } else {
                        System.out.println(o[0] + " " + o[1]);
                        pasos.add(new String[]{o[0] + " " + o[1]});
                    }
                }
                c++;
            }
        } else {
            System.out.println("Asegurece de realizar primero la multiplicacion");
        }
        return pasos;
    }

//    metodo para validar que un numero pueda ser representdo con una cantidad de bits nBits
    private static boolean esRepresentableEnCa2(int numero, int nBits) {
//        -[ 2^(n-1) - 1 ] < = n < =  2^(n-1) - 1
        return numero >= -1 * (Math.pow(2, nBits - 1) - 1) && numero <= Math.pow(2, nBits - 1) - 1;
    }

//    Completa la cadena de binarios con 0's
    private static String completarBits(String binaryString, int sizeRequired) {
        String ceros = "000000000000000000000000000000000000000000000000000000000000000000000000000000000";
        int binarylLen = binaryString.length();
        return binarylLen < sizeRequired ? ceros.substring(0, sizeRequired - binarylLen) + binaryString : binaryString;
    }

    private static String complementoA2(int numero, int nBits) {
        String s = "";
        if (esRepresentableEnCa2(numero, nBits)) {
            s = Integer.toBinaryString(numero);
            if (numero >= 0) {
                s = completarBits(s, nBits);
            } else {
                s = s.substring(s.length() - nBits);
            }
        }
        return s;
    }

//    Realiza el procedimiento de complemento a 2 sin tener en cuenta si el numero es positivo o negativo
    private static String complementoA2(String binaryString) {
        String ca1 = complementoA1(binaryString);
        return addBinary(ca1, "1");
    }

    private static String complementoA1(String binaryString) {
        String ca1 = "";
        for (int i = 0; i < binaryString.length(); i++) {
            ca1 += (1 - Integer.parseInt(binaryString.charAt(i) + "")) + "";
        }
        return ca1;
    }

//    suma dos cadenas binarias y devuelve el resultado en otra cadena binaria
    private static String addBinary(String a, String b) {

        // Initialize result 
        String result = "";

        // Initialize digit sum 
        int s = 0;

        // Traverse both strings starting  
        // from last characters 
        int i = a.length() - 1, j = b.length() - 1;
        while (i >= 0 || j >= 0 || s == 1) {

            // Comput sum of last  
            // digits and carry 
            s += ((i >= 0) ? a.charAt(i) - '0' : 0);
            s += ((j >= 0) ? b.charAt(j) - '0' : 0);

            // If current digit sum is  
            // 1 or 3, add 1 to result 
            result = (char) (s % 2 + '0') + result;

            // Compute carry 
            s /= 2;

            // Move to next digits 
            i--;
            j--;
        }

        return result;
    }

//    suma dos cadenas binarias y devuelve el resultado en otra cadena binaria pero sin considerar el ultimo acarreo
    private static String addBinaryWithoutFinalCarry(String a, String b) {
        int bits_iniciales = Math.max(a.length(), b.length());
        String result = addBinary(a, b);
        return result.length() <= bits_iniciales ? completarBits(result, bits_iniciales) : result.substring(result.length() - bits_iniciales);
    }

//    realiza el desplazamiento del numero binario en string
    private static String desplazar(String binaryString) {
        String s = binaryString.substring(0, 1) + binaryString.substring(0, binaryString.length() - 1);
//        101010 -> 110101
        return s;
    }

//    public static void main(String[] args) {
//        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//    Multiplicador multiplicador = new Multiplicador(55, -66, 16);
//        multiplicador.multiplicarPasoAPaso();
//        multiplicador.desplegarProceso();
//        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//        multiplicador = new Multiplicador(6, -5, 8);
//        multiplicador.multiplicarPasoAPaso();
//        multiplicador.desplegarProceso();
//        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//        multiplicador = new Multiplicador(-6, 5, 8);
//        multiplicador.multiplicarPasoAPaso();
//        multiplicador.desplegarProceso();
//        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
//        multiplicador = new Multiplicador(-6, -5, 8);
//        multiplicador.multiplicarPasoAPaso();
//        multiplicador.desplegarProceso();
//
//    }
}
