package lk.ijse.pos_system.report;

import javafx.collections.ObservableList;
import lk.ijse.pos_system.view.tm.CartTM;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

;
import java.util.HashMap;

public class CustomerOrderReport {
    public static void generateInvoice(HashMap map, ObservableList<CartTM> orderItems)  {

        try {
            JasperDesign design = JRXmlLoader.load(CustomerOrderReport.class.getResourceAsStream("../view/reports/InvoiceReport.jrxml"));
            JasperReport jasperReport = JasperCompileManager.compileReport(design);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, map, new JRBeanArrayDataSource(orderItems.toArray()));
            JasperViewer.viewReport(jasperPrint,false);
        } catch (JRException e) {
            e.printStackTrace();
        }

    }
}
