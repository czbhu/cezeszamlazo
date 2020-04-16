package cezeszamlazo.ugyfel;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class KijeloltUgyfelekFrame<T extends javax.swing.JFrame> extends javax.swing.JDialog
{
    public KijeloltUgyfelekFrame(T parent)
    {
        initComponents(parent);
        init();
    }
    
    public KijeloltUgyfelekFrame(int x, int y, T parent)
    {
    	initComponents(parent);
    	init(x, y);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings({ "unchecked", "serial" })
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents(T parent)
    {
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        select = new javax.swing.JButton();
        
        parentFrame = parent;
        

        select.setFont(new java.awt.Font("Tahoma", 0, 12));
        select.setText("Kiválasztottak kijelölése");
        select.addActionListener(new java.awt.event.ActionListener(){

			@Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				selectButtonAction();
			}
        	
        });

        table.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
                "ID", "Név"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table.setRowHeight(20);
        table.getTableHeader().setReorderingAllowed(false);

        jScrollPane1.setViewportView(table);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                    .addComponent(select, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)                		)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                .addGap(5)
                .addComponent(select)
                .addContainerGap())
        );
        
        model = (DefaultTableModel) table.getModel();
        
         
        sorter = new TableRowSorter<TableModel>(((DefaultTableModel) table.getModel()));
        sorter.setRowFilter(RowFilter.regexFilter("^(Nothing matches to this pattern)$"));
        table.setRowSorter(sorter);

        

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private T parentFrame;
    
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable table;
    private javax.swing.JButton select;
    private DefaultTableModel model;
    private TableRowSorter<TableModel> sorter;
    

    public void init()
    {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;

//      setIconImage(PixiRendszer.img);
        setLocation(x, y);
        setTitle("Ügyfelek");
    }
    
    public void init(int x, int y)
    {
//      setIconImage(PixiRendszer.img);
        setLocation(x, y);
        setTitle("Kiválasztott Ügyfelek");
    }

    private void selectButtonAction()
    {
    	ArrayList<Integer> ids = new ArrayList<>();
    	
    	for(int i = 0; i<table.getRowCount(); i++)
        {
            ids.add(Integer.parseInt(String.valueOf(table.getValueAt(i, 0))));
    	}
    	
    	if(parentFrame.getClass() == UgyfelekFrame.class)
        {
            ((UgyfelekFrame) parentFrame).setSelected(ids);
        }
    	else
        {
            ((KapcsolattartokFrame) parentFrame).setSelected(ids);
        }
    }
    
    public void fillUp(Map<Integer, String> data)
    {
    	clear();
    	for (int i: data.keySet())
        {
            add(i, data.get(i));
        }
    }
    
    public void add(int id, String name)
    {
    	model.addRow(new Object[] {id, name});
    }
    
    public void clear()
    {
    	model.setRowCount(0);
    }
    
    public void sortTable(String regex)
    {                                            
        sorter.setRowFilter(RowFilter.regexFilter(regex));
    }  
    
    public void nyit()
    {
        setVisible(true);
    }
}