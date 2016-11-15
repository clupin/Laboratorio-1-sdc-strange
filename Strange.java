import java.nio.charset.Charset;
import java.io.ByteArrayOutputStream;

class Strange {
	private static Strange instance = null; 
	public static Strange getInstance(){
		if(instance==null)
			instance = new Strange();
		return instance;
	}
	private byte elVelozZorroCafe(byte key,byte tp_byte){
		//para evitar problemas con negativo
		int tp_int = tp_byte&127;
		if((tp_byte&128)!=0)
			tp_int = tp_int | 128;
		int key_int = key&127;
		if((key&128)!=0)
			key_int = key_int | 128;
		int[] traslacion = {19,7,4,16,20,8,2,10,1,17,14,22,13,5,23,9,12,15,18,21,11,0,25,24,3,6};
		int enc_mod = traslacion[((tp_int+(key_int>>2))%26)];
		int enc = (tp_int/26)*26+enc_mod;
		return (byte)enc ;
	}
	private byte desElVelozZorroCafe(byte key,byte tp_byte){
		//para evitar problemas con negativo
		int tp_int = tp_byte&127;
		if((tp_byte&128)!=0)
			tp_int = tp_int | 128;
		int key_int = key&127;
		if((key&128)!=0)
			key_int = key_int | 128;
		int[] traslacion   = {19,7,4,16,20,8,2,10,1,17,14,22,13,5,23,9,12,15,18,21,11,0,25,24,3,6};
		int mod_wos = tp_int%26;
		int mod_shift = (key_int>>2)%26;
		int i=0;
		for (i=0;i<26;i++)
			if(traslacion[i]==mod_wos)
				break;
		int trans_index = (mod_shift>i)?26-i+mod_shift:i-mod_shift;
		int enc_mod = trans_index;
		int enc = (tp_int/26)*26+enc_mod;
		return (byte)enc ;
	}
	private static byte[] ofuscacion(byte key,byte tp_byte){
		int tp_int = tp_byte&127;
		if((tp_byte&128)!=0)
			tp_int = tp_int | 128;
		int key_int = key&127;
		if((key&128)!=0)
			key_int = key_int | 128;

		int relleno = (key_int>>2)&7;
		int paridad = 0;
		for(int i=0;i<3;i++)
			paridad += (key>>(5+i))&1;
		byte[] retorno = new byte[relleno+1];
		byte[] texto = "lorem ip".getBytes();
		for(int i=0;i<=relleno;i++)
			retorno[i] = (paridad%2==0 && i%2==0)? (byte)(tp_int^texto[i]^255):(byte)(tp_int^texto[i]);
		return retorno;
	}
	private byte avecesNoRotaIzq(byte key,byte tp_byte){
		//problemas con la conversion automatica si el binario es negativo
		int tp_int = tp_byte&127;
		if((tp_byte&128)!=0)
			tp_int = tp_int | 128;

		int shifting = (key>>2)&7;
		int paridad = 0;
		for(int i=0;i<3;i++)
			paridad += (key>>(5+i))&1;

		boolean negar = paridad%2==0;
		int left_side = tp_int>>shifting;
		int right_side = tp_int<< (8-shifting);
		int enc_byte = (left_side|right_side)&255;
		if(negar)
			enc_byte = (~enc_byte)&255;
		return (byte) enc_byte;
	}
	private byte desAvecesNoRotaIzq(byte key,byte te_byte){
		//problemas con la conversion automatica si el binario es negativo
		int te_int = te_byte&127;
		if((te_byte&128)!=0)
			te_int = te_int | 128;

		int shifting = (key>>2)&7;
		int paridad = 0;
		for(int i=0;i<3;i++)
			paridad += (key>>(5+i))&1;
		boolean negar = paridad%2==0; 
		if(negar)
			te_int = te_int^255;
		int left_side = (te_int<<shifting)&255;
		int right_side =te_int>>(8-shifting);
		int enc_byte = (left_side|right_side)&255;
		return (byte) enc_byte;
	}
	private byte byte_xor(byte key,byte tp_byte){
		//problemas con la conversion automatica si el binario es negativo
		int tp_int = tp_byte&127;
		if((tp_byte&128)!=0)
			tp_int = tp_int | 128;
		int key_int = key&127;
		if((key&128)!=0)
			key_int = key_int | 128;
		//ambos deberían ser menores a 256 ya que nunca suman ni nada
		int enc_byte = tp_int^key_int;
		return (byte) enc_byte;
	}
	public byte[] encode(byte[] key, byte[] texto_plano){
		int key_ind = 0;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte enc =0;
		byte[] ofus=null;
		for (byte b : texto_plano) {
			//TODO: manejar relleno cuando se acabe la clave
			//en especial en la ofuscação
			switch(key[key_ind]&3){
				case 0:
				//veloz zorro
				enc = this.elVelozZorroCafe(key[key_ind],b);
				baos.write(new byte[]{enc}, 0, 1);
				key_ind++;
				break;
				case 1:
				//ANRI
				enc = this.avecesNoRotaIzq(key[key_ind],b);
				baos.write(new byte[]{enc}, 0, 1);
				key_ind++;
				break;
				case 2:
				//xor
				enc = this.byte_xor(key[key_ind],b);
				baos.write(new byte[]{enc}, 0, 1);
				key_ind++;
				break;
				case 3:
				//ofuscação
				ofus = this.ofuscacion(key[key_ind],b);
				baos.write(ofus);
				ey_ind+=ofus.length();
				break;
			}
		}
		byte[] encriptado = baos.toByteArray();
		baos.close();
		return encriptado;
	}
	public byte[] decode(byte[] key, byte[] texto_encriptado){
		int  key_ind = 0;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte dec =0;
		byte[] ofus=null;
		//TODO: crear relleno desde texto encriptado

		for (byte b : texto_decriptado) {
			switch(key[key_ind]&3){
				case 0:
				//veloz zorro
				dec = this.desElVelozZorroCafe(key[key_ind],b);
				baos.write(new byte[]{dec}, 0, 1);
				key_ind++;
				break;
				case 1:
				//ANRI
				dec = this.desAvecesNoRotaIzq(key[key_ind],b);
				baos.write(new byte[]{dec}, 0, 1);
				key_ind++;
				break;
				case 2:
				//xor
				dec = this.byte_xor(key[key_ind],b);
				baos.write(new byte[]{dec}, 0, 1);
				key_ind++;
				break;
				case 3:
				//ofuscação
				int key_int = key[key_ind]&127;
				if((key[key_ind]&128)!=0)
					key_int = key_int | 128;
				int relleno = (key_int>>2)&7;
				int paridad = 0;
				for(int i=0;i<3;i++)
					paridad += (key_int>>(5+i))&1;

				int i_dec = (paridad%2==0)?this.byte_xor("l".getBytes[0],b)^255:this.byte_xor("l".getBytes[0],b);
				dec = (byte) i_dec;
				baos.write(new byte[]{dec}, 0, 1);
				ey_ind+=relleno+1;//se salta el relleno
				break;
			}
		}
		byte[] desencriptado = baos.toByteArray();
		baos.close();
		return desencriptado;
	}
}