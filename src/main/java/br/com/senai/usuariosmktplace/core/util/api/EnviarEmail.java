package br.com.senai.usuariosmktplace.core.util.api;

import java.io.IOException;
import java.util.Properties;

import br.com.senai.usuariosmktplace.core.util.Manipulador;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EnviarEmail {
	
    public static final MediaType JSON = MediaType.get("application/json");
    
    public static Response enviarEmail(String mensagem) throws IOException {
    	
    	Properties prop = Manipulador.getProp();
	    String adminEmail = prop.getProperty("emailAdministrador");
	    String emailRemetente = prop.getProperty("emailRemetente");
	    String nomeRemetente = prop.getProperty("nomeRemetente");

        OkHttpClient client = new OkHttpClient();

        String url = "https://api.sendgrid.com/v3/mail/send";
        String token = prop.getProperty("token");
        String subject = "Uepa! Uma senha foi alterada.";
        String body = String.format(
            "{\"personalizations\":[{\"to\":["
            + "{\"email\":\"%s\",\"name\":\"" + nomeRemetente  + "\"}],\"subject\":\"%s\"}],"
            + "\"content\": [{\"type\": \"text/html\", \"value\": \"%s\"}],"
            + "\"from\":{\"email\":\"" + emailRemetente + "\",\"name\":\"" + nomeRemetente + " \"},"
            + "\"reply_to\":{\"email\":\"" + emailRemetente + "\",\"name\":\"" + nomeRemetente +"\"}}",
            adminEmail, subject, mensagem
        );

        RequestBody requestBody = RequestBody.create(JSON, body);
        Request request = new Request.Builder().url(url).post(requestBody)
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response;
        } 
    }

}
