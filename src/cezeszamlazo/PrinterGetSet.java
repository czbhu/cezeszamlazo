package cezeszamlazo;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.JComboBox;

/**
 *
 * @author czbhu
 */
public class PrinterGetSet
{
    private static String PrinterName = "100";

    public void setPrinterName(String printerName)
    {
        PrinterName = printerName;
    }

    public String getPrinterName()
    {
        return this.PrinterName;
    }
    
    public void fillPrinterCombobox (JComboBox printerCombobox) 
    {
        printerCombobox.removeAllItems();
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        for (PrintService printer : printServices)
        {
            printerCombobox.addItem(String.valueOf(printer.getName()));
        }
        PrintService service = PrintServiceLookup.lookupDefaultPrintService();
        printerCombobox.getModel().setSelectedItem(String.valueOf(service.getName()));
    }
}