package ar.edu.um.programacion2.cine.web.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;

/**
 * REST controller for managing Ocupacion.
 */
@RestController
@RequestMapping("/api")
public class PagoResource {

 
    /**
     * GET  /ocupacions : get all the ocupacions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of ocupacions in body
     * @throws IOException 
     */
    @GetMapping("/pago")
    @Timed
    public String getPago() throws IOException {
        
    	URL url = new URL("http://localhost:8090/api/pago/1231231233211233213/500");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Autenticaton","y le pones el token de autenticaciÃ³n del administrador de la parte de pago");
    
        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP Error code : "
                + conn.getResponseCode());
        }
    
        InputStreamReader in = new InputStreamReader(conn.getInputStream());
        BufferedReader br = new BufferedReader(in);
        String output;
        while ((output = br.readLine()) != null) {
            System.out.println(output);
        }
        conn.disconnect();

        return output;
        
    }

}
