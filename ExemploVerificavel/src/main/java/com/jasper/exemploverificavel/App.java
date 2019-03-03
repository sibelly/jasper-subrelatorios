package com.jasper.exemploverificavel;

import com.jasper.exemploverificavel.entidades.Cliente;
import com.jasper.exemploverificavel.entidades.Produto;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

public class App {

    public static void main(String[] args) throws JRException {

        // Gera objetos do relatorio
        List<Produto> produtos = Produto.mockaProdutos();
        Cliente cliente = new Cliente("Teste");

        Integer quantidadeDeItens = produtos.size();
        Integer quantidadeDeItensPorPagina = 13; // Trunco aqui a quantidade de itens para manter o layout
        Integer i = 0;
        Integer j = quantidadeDeItensPorPagina;
        List<Produto> itensPaginados = new ArrayList<>();
        List<JasperPrint> listaDeProdutosParaImpressao = new ArrayList<>();
        
        /**/

        Integer totalPaginas = 0;
        if (((produtos.size() % quantidadeDeItensPorPagina) > 0) || (produtos.isEmpty())) {
            totalPaginas = (produtos.size() / quantidadeDeItensPorPagina) + 1;
        } else {
            totalPaginas = (produtos.size() / quantidadeDeItensPorPagina);
        }       
        
        System.out.println("Total de páginas -> " + totalPaginas);
        
        Integer paginaCorrente = 0;
        if (produtos.isEmpty() || produtos == null) {
            paginaCorrente = paginaCorrente + 1;
            JasperPrint printReport = setarParametrosParaImpressao(itensPaginados, cliente, totalPaginas, paginaCorrente);
            listaDeProdutosParaImpressao.add(printReport);
        } else {
            while (quantidadeDeItens > 0) {
                if (quantidadeDeItens <= quantidadeDeItensPorPagina) {
                    itensPaginados.addAll(produtos.subList(i, produtos.size()));

                    // Workaround para deixar o layout sempre do mesmo tamanho
                    if (itensPaginados.size() < quantidadeDeItensPorPagina) {
                        while (itensPaginados.size() < quantidadeDeItensPorPagina) {
                            Produto item = new Produto();
                            itensPaginados.add(item);
                        }
                    }
                } else {
                    itensPaginados.addAll(produtos.subList(i, j));
                }

                paginaCorrente = paginaCorrente + 1;

                JasperPrint printReport = setarParametrosParaImpressao(itensPaginados, cliente, totalPaginas, paginaCorrente);
                listaDeProdutosParaImpressao.add(printReport);

                i = i + quantidadeDeItensPorPagina;
                j = j + quantidadeDeItensPorPagina;
                quantidadeDeItens = quantidadeDeItens - quantidadeDeItensPorPagina;
                itensPaginados = new ArrayList<>();

            }
        }        
        
        /**/    
        if (listaDeProdutosParaImpressao != null && !listaDeProdutosParaImpressao.isEmpty()) {
            JasperPrint jrPrint = new JasperPrint();
            JasperPrint jp = (JasperPrint) listaDeProdutosParaImpressao.get(0);

            jrPrint.setOrientation(jp.getOrientationValue());
            jrPrint.setLocaleCode(jp.getLocaleCode());
            jrPrint.setPageHeight(jp.getPageHeight());
            jrPrint.setPageWidth(jp.getPageWidth());
            jrPrint.setTimeZoneId(jp.getTimeZoneId());
            jrPrint.setName(jp.getName());

            for (i = 0; i < listaDeProdutosParaImpressao.size(); i++) {

                jp = (JasperPrint) listaDeProdutosParaImpressao.get(i);
                List<JRPrintPage> list = jp.getPages();

                for (j = 0; j < list.size(); j++) {
                    jrPrint.addPage(list.get(j));
                }
            }

            try {
                
                JasperExportManager.exportReportToPdfFile(jrPrint,"Arquivo.pdf");
                File file = new File("Arquivo.pdf");
                Desktop.getDesktop().open(file);
                
            } catch (Exception ex) {
                System.out.println("Erro ao gerar impressaoo!");
            }
        }
    }
    
    public static JasperPrint setarParametrosParaImpressao(List<Produto> itensPaginados, Cliente cliente, Integer totalPaginas, Integer paginaCorrente) {
        
    	JasperPrint printReport = null;
    	
        // Meu diretorio 
    	String caminhoArquivo = "/home/boogiepop/Documents/relatorio-pai.jasper";
        String subReportDir = "/home/boogiepop/Documents/";
    	
        Map<String, Object> parametros = new HashMap<>();
        parametros.put(JRParameter.REPORT_LOCALE, new Locale("pt", "BR"));
        parametros.put("CLIENTE", cliente);
        parametros.put("PRODUTOS", itensPaginados);
        parametros.put("TOTAL_PAGINAS", totalPaginas);
        parametros.put("PAGINA_CORRENTE", paginaCorrente);
        parametros.put("SUBREPORT_DIR", subReportDir);

        try {
            printReport = JasperFillManager.fillReport(caminhoArquivo, parametros, new JREmptyDataSource(1));

        } catch (Exception ex) {
            System.out.println("ERRO AO GERAR PDF ".concat(String.valueOf(cliente)));
        }
        
        return printReport;
        
    }    
    
}
