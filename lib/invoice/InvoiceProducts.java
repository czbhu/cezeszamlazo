package invoice;

import java.util.ArrayList;

/**
 * @author Tomy
 */
public class InvoiceProducts
{
    public static final int PRODUCT = 0, PRODUCTFEE = 1;
    
    private ArrayList<InvoiceProduct> products;
    
    
    public InvoiceProducts()
    {
        products = new ArrayList<>();
    }
    
    public void Add(InvoiceProduct product)
    {
        products.add(product);
    }
    
    public void Remove(int index)
    {
        products.remove(index);
    }
    
    public InvoiceProduct Get(int index)
    {
        return products.get(index);
    }
    
    public void Set(int index, InvoiceProduct product)
    {
        products.set(index, product);
    }
    
    public int Size()
    {
        return products.size();
    }
    
    public boolean Empty()
    {
        return products.isEmpty();
    }

    public ArrayList<InvoiceProduct> getProducts()
    {
        return products;
    }
    
    public double getTotalNet(int type, boolean foreignCurrency)
    {
        double net = 0.0;
        
        for(InvoiceProduct product : products)
        {
            if(type == PRODUCT)
            {
                net += product.getNetPrice(foreignCurrency);
            }
            else
            {
                if(product.getProductFee() != null)
                {
                    net += product.getProductFee().getOsszTermekDijNetto(foreignCurrency);
                }
            }
        }

        return net;
    }
    
    public double getTotalVat(int type, boolean foreignCurrency)
    {
        double vat = 0.0;
        
        for(InvoiceProduct product : products)
        {
            if(type == PRODUCT)
            {
                vat += product.getVatAmount(foreignCurrency);
            }
            else
            {
                if(product.getProductFee() != null)
                {
                    vat += product.getProductFee().getOsszTermekDijAfaErtek(foreignCurrency);
                }
            }
        }
        
        return vat;
    }
    
    public double getTotalGross(int type, boolean foreignCurrency)
    {
        double gross = 0.0;
        
        for(InvoiceProduct product : products)
        {
            if(type == PRODUCT)
            {
                gross += product.getGrossPrice(foreignCurrency);
            }
            else
            {
                if(product.getProductFee() != null)
                {
                    gross += product.getProductFee().getOsszTermekDijBrutto(foreignCurrency);
                }
            }
        }

        return gross;
    }
}