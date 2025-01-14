package com.example.trabalho_t2.menu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/metricas")
@PreAuthorize("hasRole('ADMIN')")
public class MetricMenuController {

    @Autowired
    private RestTemplate restTemplate;

    private final String baseUrl = "http://localhost:8080/metricas";

    @GetMapping
    @PreAuthorize("permitAll()")
    public String showMenu() {
        StringBuilder menu = new StringBuilder();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null && auth.isAuthenticated();




        menu.append("<h1>Bem-vindo ao Sistema de Métricas</h1>");
        menu.append("<ul>");
        menu.append("<li><a href='/metricas/filter?filter=quarto'>Consultar por Quarto</a></li>");
        menu.append("<li><a href='/metricas/filter?filter=piso'>Consultar por Piso</a></li>");
        menu.append("<li><a href='/metricas/filter?filter=servico'>Consultar por Serviço</a></li>");
        menu.append("<li><a href='/metricas/filter?filter=edificio'>Consultar por Edifício</a></li>");
        menu.append("<li><a href='/metricas/mediaform'>Consultar Médias de Temperatura e Humidade por Quarto</a></li>");
        menu.append("<li><a href='/metricas/mediaformServico'>Consultar Médias de Temperatura e Humidade por Serviço</a></li>");
        menu.append("<li><a href='/metricas/mediaformEdificio'>Consultar Médias de Temperatura e Humidade por Edifício</a></li>");
        menu.append("<li><a href='/metricas/mediaformPiso'>Consultar Médias de Temperatura e Humidade por Piso</a></li>");
        menu.append("</ul>");
        return menu.toString();
    }

    @GetMapping("/filter")
    public String showFilterForm(@RequestParam String filter) {
        StringBuilder form = new StringBuilder();
        form.append("<h1>Filtrar Métricas por ").append(filter).append("</h1>");
        form.append("<form action='/metricas/query' method='get'>");
        form.append("<input type='hidden' name='filter' value='").append(filter).append("' />");
        form.append("Identificador (").append(filter).append("): <input type='text' name='identifier'><br><br>");
        form.append("<button type='submit'>Consultar</button>");
        form.append("</form>");
        return form.toString();
    }

    @GetMapping("/mediaformServico")
    public String showAverageMetricsFormServico() {
        StringBuilder form = new StringBuilder();
        form.append("<h1>Consultar Médias de Temperatura e Humidade por Serviço</h1>");
        form.append("<form action='/metricas/mediasServico' method='get'>");
        form.append("Nome do Serviço: <input type='text' name='servico'><br><br>");
        form.append("<button type='submit'>Consultar</button>");
        form.append("</form>");
        return form.toString();
    }

    @GetMapping("/mediaformEdificio")
    public String showAverageMetricsFormEdificio() {
        StringBuilder form = new StringBuilder();
        form.append("<h1>Consultar Médias de Temperatura e Humidade por Edifício</h1>");
        form.append("<form action='/metricas/mediasEdificio' method='get'>");
        form.append("Nome do Edifício: <input type='text' name='edificio'><br><br>");
        form.append("<button type='submit'>Consultar</button>");
        form.append("</form>");
        return form.toString();
    }

    @GetMapping("/mediaformPiso")
    public String showAverageMetricsFormPiso() {
        StringBuilder form = new StringBuilder();
        form.append("<h1>Consultar Médias de Temperatura e Humidade por Piso</h1>");
        form.append("<form action='/metricas/mediasPiso' method='get'>");
        form.append("Número do Piso: <input type='text' name='piso'><br><br>");
        form.append("<button type='submit'>Consultar</button>");
        form.append("</form>");
        return form.toString();
    }

    @GetMapping("/query")
    public ResponseEntity<String> fetchMetrics(
            @RequestParam String filter,
            @RequestParam String identifier) {
        try {
            String url = baseUrl;

            switch (filter.toLowerCase()) {
                case "quarto":
                    url += "/quarto/" + identifier;
                    break;
                case "piso":
                    url += "/piso/" + identifier;
                    break;
                case "servico":
                    url += "/servico/" + identifier;
                    break;
                case "edificio":
                    url += "/edificio/" + identifier;
                    break;
                default:
                    throw new IllegalArgumentException("Filtro inválido: " + filter);
            }

            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                String responseBody = response.getBody();
                
                String formattedResponse = responseBody.replace("[", "<br><b>Metricas:</b><br>")
                                                       .replace("},{", "</br><br><b>Metric:</b><br>")
                                                       .replace("}", "</br>");
                
                return ResponseEntity.ok(
                    "<h2>Resultados:</h2>" +
                    "<p>Filtro: " + filter + "</p>" +
                    "<p>Identificador: " + identifier + "</p>" +
                    "<div>" + formattedResponse + "</div>"
                );
            } else {
                return ResponseEntity.status(response.getStatusCode())
                    .body("Erro ao buscar métricas.");
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao buscar métricas: " + e.getMessage());
        }
    }

    @GetMapping("/mediaform")
    public String showAverageMetricsForm() {
        StringBuilder form = new StringBuilder();
        form.append("<h1>Consultar Médias de Temperatura e Humidade por Quarto</h1>");
        form.append("<form action='/metricas/medias' method='get'>");
        form.append("Número do Quarto: <input type='text' name='quarto'><br><br>");
        form.append("<button type='submit'>Consultar</button>");
        form.append("</form>");
        return form.toString();
    }

    @GetMapping("/medias")
    public ResponseEntity<String> getAverageMetricsForRoom(@RequestParam String quarto) {
        try {
            String tempUrl = baseUrl + "/media/temperatura/" + quarto;
            ResponseEntity<String> tempResponse = restTemplate.getForEntity(tempUrl, String.class);

            String humidityUrl = baseUrl + "/media/humidade/" + quarto;
            ResponseEntity<String> humidityResponse = restTemplate.getForEntity(humidityUrl, String.class);

            if (tempResponse.getStatusCode().is2xxSuccessful() && humidityResponse.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok(
                    "<h2>Média de Temperatura e Humidade para o Quarto " + quarto + "</h2>" +
                    "<p><b>Média de Temperatura:</b> " + tempResponse.getBody() + "</p>" +
                    "<p><b>Média de Humidade:</b> " + humidityResponse.getBody() + "</p>"
                );
            } else {
                return ResponseEntity.status(500).body("Erro ao calcular as médias de temperatura e humidade.");
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao calcular as médias: " + e.getMessage());
        }
    }



    @GetMapping("/mediasServico")
    public ResponseEntity<String> getAverageMetricsForService(@RequestParam String servico) {
        try {
            String tempUrl = baseUrl + "/media/temperatura/" + servico;
            ResponseEntity<String> tempResponse = restTemplate.getForEntity(tempUrl, String.class);

            // Consultando a média de humidade
            String humidityUrl = baseUrl + "/media/humidade/" + servico;
            ResponseEntity<String> humidityResponse = restTemplate.getForEntity(humidityUrl, String.class);

            if (tempResponse.getStatusCode().is2xxSuccessful() && humidityResponse.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok(
                    "<h2>Média de Temperatura e Humidade para o Serviço " + servico + "</h2>" +
                    "<p><b>Média de Temperatura:</b> " + tempResponse.getBody() + "</p>" +
                    "<p><b>Média de Humidade:</b> " + humidityResponse.getBody() + "</p>"
                );
            } else {
                return ResponseEntity.status(500).body("Erro ao calcular as médias de temperatura e humidade para o serviço.");
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao calcular as médias: " + e.getMessage());
        }
    }

    @GetMapping("/mediasEdificio")
    public ResponseEntity<String> getAverageMetricsForBuilding(@RequestParam String edificio) {
        try {
            String tempUrl = baseUrl + "/media/temperatura/" + edificio;
            ResponseEntity<String> tempResponse = restTemplate.getForEntity(tempUrl, String.class);

            String humidityUrl = baseUrl + "/media/humidade/" + edificio;
            ResponseEntity<String> humidityResponse = restTemplate.getForEntity(humidityUrl, String.class);

            if (tempResponse.getStatusCode().is2xxSuccessful() && humidityResponse.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok(
                    "<h2>Média de Temperatura e Humidade para o Edifício " + edificio + "</h2>" +
                    "<p><b>Média de Temperatura:</b> " + tempResponse.getBody() + "</p>" +
                    "<p><b>Média de Humidade:</b> " + humidityResponse.getBody() + "</p>"
                );
            } else {
                return ResponseEntity.status(500).body("Erro ao calcular as médias de temperatura e humidade para o edifício.");
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao calcular as médias: " + e.getMessage());
        }
    }

    @GetMapping("/mediasPiso")
    public ResponseEntity<String> getAverageMetricsForFloor(@RequestParam String piso) {
        try {
            String tempUrl = baseUrl + "/media/temperatura/" + piso;
            ResponseEntity<String> tempResponse = restTemplate.getForEntity(tempUrl, String.class);

            String humidityUrl = baseUrl + "/media/humidade/" + piso;
            ResponseEntity<String> humidityResponse = restTemplate.getForEntity(humidityUrl, String.class);

            if (tempResponse.getStatusCode().is2xxSuccessful() && humidityResponse.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.ok(
                    "<h2>Média de Temperatura e Humidade para o Piso " + piso + "</h2>" +
                    "<p><b>Média de Temperatura:</b> " + tempResponse.getBody() + "</p>" +
                    "<p><b>Média de Humidade:</b> " + humidityResponse.getBody() + "</p>"
                );
            } else {
                return ResponseEntity.status(500).body("Erro ao calcular as médias de temperatura e humidade para o piso.");
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao calcular as médias: " + e.getMessage());
        }
    }
}
