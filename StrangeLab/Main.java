package StrangeLab;
import StrangeLab.Strange;
import java.util.Arrays;
import java.nio.charset.Charset;

public class Main {
    public static void main(String[] args){
        Strange strange = Strange.getInstance();
        String clave = "mistica", texto_plano = "un texto interesante de encriptar ya que es más largo y tiene caractéres utf-8";
        /** Encriptar **/
        System.out.println("key: "+clave);
        System.out.println("Texto plano: "+texto_plano);

        byte[] enc_array = strange.encode(clave.getBytes(), texto_plano.getBytes());
		String s_enc = new String(enc_array, Charset.forName("UTF-8"));
        
        System.out.println("\nTexto encriptado: "+s_enc);

		/** Desencriptar **/
		byte[] dec_array = strange.decode(clave.getBytes(),s_enc.getBytes());
		String s_dec = new String(dec_array, Charset.forName("UTF-8"));

        System.out.println("Texto desen: "+s_dec);

       // for(byte b : dec_array)
		//	printBinary(b);
        //System.out.println("\nTexto desencriptado: "+Arrays.toString(dec_array));
        

    }
    public static void printBinary(byte b){
		int i_b = b&127;
		if((b&128)!=0)
			i_b = i_b | 128;
		System.out.print(String.format("%08d ", Integer.parseInt(Integer.toBinaryString(i_b))));
	}
}