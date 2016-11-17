package StrangeLab;
import java.util.Arrays;
import java.nio.charset.Charset;
import java.io.*;
import java.util.Scanner;


public class Main {
    public static void main(String[] args){
    int opcion = -1;
    Scanner sc = new Scanner(System.in);
    String texto_plano, clave ,texto_enc,Fichero,salida,op;
    Strange strange;
    FileWriter file =null;
    PrintWriter pw= null;
    while(opcion !=0){
        System.out.println("Menu:\n1-Encriptar texto\n2-Desencriptar texto\n3-Encriptar Archivo\n4-DecriptarArchivo\n0-Salir \nEscriba el numero de la opción:\n");
        op= sc.nextLine();
        opcion=Integer.parseInt(op);
        switch(opcion){
            case 1:
                System.out.println("\nIngrese un texto a encriptar:");
                texto_plano =sc.nextLine();
                System.out.println("\nIngrese una Clave con la cual encriptar");
                clave = sc.nextLine();
                strange = Strange.getInstance();
                /** Encriptar **/
               // byte[] enc_array = strange.encode(clave.getBytes(), texto_plano.getBytes());
		//String s_enc = new String(enc_array, Charset.forName("UTF-8"));
               // System.out.println("\nTexto encriptado: \n"+s_enc);
                break;
            case 2:
                System.out.println("\nIngrese un texto a desencriptar :");
                texto_enc =sc.nextLine();
                System.out.println("\nIngrese la Clave del texto encriptado:");
                clave=sc.nextLine();
                strange = Strange.getInstance();
                /** Desencriptar **/
		byte[] dec_array = strange.decode(clave.getBytes(),texto_enc.getBytes());
		String s_dec = new String(dec_array, Charset.forName("UTF-8"));
                System.out.println("Texto desencriptado: \n"+s_dec);
                break;
            case 3:
                System.out.println("\nIngrese el nombre del archivo a encriptar(.txt)");
                Fichero=sc.nextLine();
                System.out.println("\nIngrese el nombre del archivo de salida");
                salida=sc.nextLine();
                System.out.println("\nIngrese la Clave con la cual encriptar");
                clave=sc.nextLine();
                strange = Strange.getInstance();
                try{
                    FileReader fr = new FileReader(Fichero);
                    BufferedReader br = new BufferedReader(fr);
                    file = new FileWriter(salida);
                    pw = new PrintWriter(file);
                    /** Encriptar **/
                        while((texto_plano = br.readLine()) != null){
                            byte[] enc_array = strange.encode(clave.getBytes(), texto_plano.getBytes());
                            String txt_enc = new String(enc_array, Charset.forName("UTF-8"));
                            pw.println(txt_enc);
                        }
                    fr.close();
                    file.close();
                }catch(Exception e){
                    System.out.println("Error al leer el archivo " + Fichero);
                }
                break;
            case 4:
                System.out.println("\nIngrese el nombre del archivo a desencriptar(.txt):");
                Fichero=sc.nextLine();
                System.out.println("\nIngrese el nombre del archivo de salida");
                salida=sc.nextLine();
                System.out.println("\nIngrese la Clave del texto encriptado");
                clave=sc.nextLine();
                strange = Strange.getInstance();
                try{
                    FileReader fr = new FileReader(Fichero);
                    BufferedReader br = new BufferedReader(fr);
                    file = new FileWriter(salida);
                    pw = new PrintWriter(file);
                    /** Encriptar **/
                        while((texto_plano = br.readLine()) != null){
                            byte[] txt_array = strange.decode(clave.getBytes(), texto_plano.getBytes());
                            String txt_dec = new String(txt_array, Charset.forName("UTF-8"));
                            pw.println(txt_dec);
                        }
                    fr.close();
                    file.close();
                }catch(Exception e){
                    System.out.println("Error al leer el archivo " + Fichero);
                }
                break;
            case 0:
                System.out.println("\nAdios!! Gracias por utilizar Strange Encriptation");
                break;
            default:
                System.out.println("\nOpción Invalida, porfavor intente denuevo.");
                

        }
    }    

    }
    public static void printBinary(byte b){
		int i_b = b&127;
		if((b&128)!=0)
			i_b = i_b | 128;
		System.out.print(String.format("%08d ", Integer.parseInt(Integer.toBinaryString(i_b))));
	}
}