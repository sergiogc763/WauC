package com.gcs.wauc.tools;


import android.util.Base64;
import android.widget.EditText;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * La clase HerramientaMetodos nace con la necesidad de modular la aplicación, y crear un única
 * clase que contenga herramientas de validación de datos (en este caso el DNI), dar formato fechas,...
 */
public class HerramientaMetodos {

    private static HerramientaMetodos herramientaMetodos = null;

    public static HerramientaMetodos herramienta() {

        herramientaMetodos = new HerramientaMetodos();

        return herramientaMetodos;
    }

    /**
     *  Método que utiliza las validaciones de números y de la letra del Dni
     *
     * @param dni
     * @return boolean
     */
    public boolean validarDNI(String dni){

        String letra = "";

        if(dni.length() !=9 ||
                !Character.isLetter(dni.charAt(8))){

            return false;

        }else{

            letra = dni.substring(8).toUpperCase();

            if(numerosDNI(dni) && letraDNI(dni).equals(letra)){

                return true;

            }
            else{

                return false;

            }
        }
    }

    /**
     * Método que valida que los 8 primeros valores introducidos del Dni sean números
     *
     * @param dni
     * @return boolean
     */
    public boolean numerosDNI(String dni){

        int i, x;

        String num = "";
        String dniFinal = "";
        String[] numeros ={"0","1","2","3","4","5","6","7","8","9"};

        for(i = 0; i< dni.length() -1 ;i++){

            num = dni.substring(i,i+1);

            for(x = 0; x <numeros.length;x++){

                if(num.equalsIgnoreCase(numeros[x])){
                    dniFinal += numeros[x];
                }

            }

        }

        if (dniFinal.length() !=8){
            return false;
        }else{
            return true;
        }
    }

    /**
     * Método que valida que la letra del Dni introducido sea valida y correcta
     *
     * @param dni
     * @return String
     */
    public String letraDNI(String dni){

        int numerosDni = Integer.parseInt(dni.substring(0,8));

        String letraFinal = "";
        String[] letras = {"T","R","W","A","G","M","Y"
                ,"F","P","D","X","B","N","J"
                ,"Z","S","Q","V","H","L","C","K","E"};

        int res = numerosDni%23;

        letraFinal = letras[res];

        return letraFinal;
    }

    /**
     *
     *
     /**
     * Método utilizado para comprobar que los datos que se introducen en el formulario son correctos.
     *     Controla:
     *
     *         - DNI. Comprueba que el DNI sea correcto y a su vez confirma que sea verdadero
     *         - Teléfono. Comprueba que se introduce un teléfono con correcto formato
     *         - Pass. Comprueba que la contraseña cumple los requisitos, en este caso que tenga mínimo
     *         4 caracteres
     *
     * @param etDni
     * @param etNombre
     * @param etTelefono
     * @param etPassword
     * @param etPasswordConfirmar
     * @return boolean
     */
    public boolean comprobarDatos(EditText etDni, EditText etNombre, EditText etTelefono, EditText etPassword, EditText etPasswordConfirmar){

        boolean flag;

        String dni = etDni.getText().toString();
        String nombre = etNombre.getText().toString();
        String telefono = etTelefono.getText().toString();
        String pass = etPassword.getText().toString();
        String passConfirmar = etPasswordConfirmar.getText().toString();

        if( (!(dni.isEmpty() || dni.contentEquals(""))) && HerramientaMetodos.herramienta().validarDNI(dni) ){
            if(!(nombre.isEmpty() || nombre.contentEquals(""))){

                if((!(telefono.isEmpty() || telefono.contentEquals(""))) && telefono.length() == 9){

                    if( (!(pass.isEmpty() || pass.contentEquals(""))) &&
                            (!(passConfirmar.isEmpty() || passConfirmar.contentEquals("")))){

                        if( (pass.length() >= 4) || (passConfirmar.length() >= 4)){

                                if(pass.contentEquals(passConfirmar)){
                                    flag = true;
                                }else{
                                    etPasswordConfirmar.setError("Ambas contraseñas deben coincidir");
                                    flag = false;

                                }
                        }else{
                            etPassword.setError("La contraseña debe tener como mínimo 4 carateres");
                            flag = false;
                        }

                    }else{
                        etPassword.setError("Introduzca una contraseña valida");
                        flag = false;
                    }

                }else{
                    etTelefono.setError("Introduzca un teléfono valido");
                    flag = false;
                }

            }else{
                etNombre.setError("No se ha introducido ningún caracter. Por favor introduzca su nombre");
                flag = false;
            }

        }else{
            etDni.setError("DNI erroneo. Introduzca un DNI valido");
            flag = false;
        }

        return flag;

    }


    /**
     *  Método que encripta la contraseña del usuario
     *
     * @param datos
     * @param key
     * @return
     * @throws Exception
     */
    public static String encriptar(String datos, String key) throws Exception{
        SecretKeySpec secretKey = generateKey(key);
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] datosEncriptadosBytes = cipher.doFinal(datos.getBytes());
        String datosEncriptadosString = Base64.encodeToString(datosEncriptadosBytes, Base64.DEFAULT);
        return datosEncriptadosString;
    }

    /**
     *  Método que genera la key para la encriptación
     *
     * @param password
     * @return
     * @throws Exception
     */
    public static SecretKeySpec generateKey(String password) throws Exception{
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] key = password.getBytes("UTF-8");
        key = sha.digest(key);
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        return secretKey;
    }

    /**
     *  Método que genera un codigo aleatorio. Utilizado para generar una "key" para encriptar
     *
     * @return
     */
    public static String generarPalabraKey(){

        String palabra = "";
        int caracteres = (int)(Math.random()*20)+2;
        for (int i=0; i<caracteres; i++){
            int codigoAscii = (int)Math.floor(Math.random()*(122 -
                    97)+97);
            palabra = palabra + (char)codigoAscii;
        }
        return palabra;
    }




}
