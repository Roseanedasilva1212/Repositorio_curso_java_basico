import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class RelatorioIBGE {

    // URL da API do IBGE para obter a população por estado
    private static final String IBGE_API_URL = "https://servicodados.ibge.gov.br/api/v1/projecoes/populacao";

    public static void main(String[] args) {
        try {
            // Código da UF do estado desejado (exemplo: SP = 35)
            int uf = 35;  
            String jsonResponse = getDadosIBGE();
            
            if (jsonResponse != null) {
                // Processa a resposta JSON
                JSONObject jsonObject = new JSONObject(jsonResponse);
                int populacao = jsonObject.getJSONObject("projecao").getInt("populacao");
                
                // Cria o relatório
                String relatorio = gerarRelatorio("São Paulo", populacao);
                
                // Salva o relatório em um arquivo
                salvarRelatorio("relatorio_ibge.txt", relatorio);
                
                System.out.println("Relatório gerado com sucesso!");
            } else {
                System.out.println("Falha ao obter dados do IBGE.");
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    // Método para buscar dados da API do IBGE
    private static String getDadosIBGE() {
        try {
            URL url = new URL(IBGE_API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                return null;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String output;

            while ((output = br.readLine()) != null) {
                sb.append(output);
            }

            conn.disconnect();
            return sb.toString();

        } catch (IOException e) {
            return null;
        }
    }

    // Método para gerar um relatório formatado
    private static String gerarRelatorio(String estado, int populacao) {
        return "Relatório do IBGE\n" +
               "--------------------------\n" +
               "Estado: " + estado + "\n" +
               "População estimada: " + populacao + "\n" +
               "Fonte: IBGE\n";
    }

    // Método para salvar o relatório em um arquivo
    private static void salvarRelatorio(String nomeArquivo, String conteudo) {
        try (FileWriter writer = new FileWriter(nomeArquivo)) {
            writer.write(conteudo);
        } catch (IOException e) {
            System.out.println("Erro ao salvar relatório: " + e.getMessage());
        }
    }
}
