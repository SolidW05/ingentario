package DemoInv;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

public class TwilioSMS {
    // Tus credenciales de Twilio
    public static final String ACCOUNT_SID = "";
    public static final String AUTH_TOKEN = "";

    public static void main(String[] args) {
        // Ejemplo de envío de un mensaje
        String mensaje = "¡Producto bajo de stock!";
        enviarMensaje(mensaje);
    }

    // Método para enviar mensaje a través de Twilio
    public static void enviarMensaje(String mensaje) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                        new com.twilio.type.PhoneNumber("+523329266146"),  // Número de destino
                        new com.twilio.type.PhoneNumber("+14425001326"),  // Tu número de Twilio
                        mensaje)  // Mensaje a enviar
                .create();

        System.out.println("Mensaje enviado: " + message.getBody());
    }
}
