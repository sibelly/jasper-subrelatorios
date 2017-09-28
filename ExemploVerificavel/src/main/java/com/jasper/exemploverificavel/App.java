package com.jasper.exemploverificavel;

import com.jasper.exemploverificavel.entidades.Cliente;
import com.jasper.exemploverificavel.entidades.Produto;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;

public class App {

    public static void main(String[] args) throws JRException {

        // Meu diretorio 
        String reportDir = "C:\\Users\\suporte\\Desktop\\";

        // Gera objetos do relatorio
        List<Produto> produtos = Produto.mockaProdutos();
        Cliente cliente = new Cliente("Teste");

        Map<String, Object> parametros = new HashMap<>();
        parametros.put(JRParameter.REPORT_LOCALE, new Locale("pt", "BR"));
        parametros.put("CLIENTE", cliente);
        parametros.put("PRODUTOS", produtos);
        parametros.put("SUBREPORT_DIR", reportDir);

        // Compilacao no formato jasper para o jrprint
        JasperFillManager.fillReportToFile("reports/relatorio-pai.jasper", parametros, new JREmptyDataSource(1));
        // Geracao do PDF
        JasperExportManager.exportReportToPdfFile("reports/relatorio-pai.jrprint");

    }
}
