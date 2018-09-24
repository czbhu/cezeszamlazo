package cezeszamlazo;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import cezeszamlazo.controller.Afa;
import cezeszamlazo.controller.Functions;
import cezeszamlazo.controller.Szamla;
import cezeszamlazo.controller.SzamlaTermek;
import cezeszamlazo.controller.TermekDij;
import cezeszamlazo.database.Query;

/**
 *
 * @author adam.papp
 */
public class SzamlaAdatokDialog extends javax.swing.JDialog {

    /**
     * A return status code - returned if Cancel button has been pressed
     */
    public static final int RET_CANCEL = 0;
    /**
     * A return status code - returned if OK button has been pressed
     */
    public static final int RET_OK = 1;

    /**
     * Creates new form SzamlaAdatokDialog
     */
    public SzamlaAdatokDialog(String azon) {
        initComponents();
        System.out.println(azon);
        Szamla szla = new Szamla(azon);

        // szállító
        szallito.setText(szla.getSzallito().toString());
        szallito.setCaretPosition(0);
        // vevő
        vevo.setText(szla.getVevo().toString());
        vevo.setCaretPosition(0);

        String fizetesiMod = "";

        switch (szla.getVevo().getFizetesiMod()) {
            case 0:
                fizetesiMod = "Készpénz";
                break;
            case 1:
                fizetesiMod = "Átutalás";
                break;
            case 2:
                fizetesiMod = "Utánvét";
                break;
        }

        // fejléc
        Object[] row = new Object[4];
        row[0] = fizetesiMod;
        row[1] = szla.getKelt();
        row[2] = szla.getTeljesites();
        row[3] = szla.getEsedekesseg();
        DefaultTableModel model = (DefaultTableModel) fejlecTable.getModel();
        model.addRow(row);

        // számla termékek
        model = (DefaultTableModel) termekekTable.getModel();
        SzamlaTermek szt;
        row = new Object[9];
        for (int i = 0; i < szla.getTermekek().size(); i++) {
            szt = (SzamlaTermek) szla.getTermekek().get(i);
            row[0] = szt.getNev();
            row[1] = szt.getVtszTeszor();
            row[2] = szt.getMennyiseg();
            row[3] = szt.getMee();
            row[4] = szt.getEgysegar();
            row[5] = szt.getNetto(szla.isDeviza());
            row[6] = szt.getAfa();
            row[7] = szt.getAfaErtek(szla.isDeviza());
            row[8] = szt.getBrutto(szla.isDeviza());
            model.addRow(row);
            if (szt.getTermekDij() != null && !szla.isAtvallal()) {
                TermekDij td = szt.getTermekDij();
                row[0] = "Környezetvédelmi termékdíj (" + (td.getTermekDij() == 20 ? "csomagolószer" : "reklámpapír") + ")";
                row[1] = "";
                row[2] = td.getSuly();
                row[3] = "kg";
                row[4] = td.getTermekDij();
                row[5] = td.getOsszTermekDijNetto(szla.isDeviza());
                row[6] = 27;
                row[7] = td.getOsszTermekDijAfaErtek(szla.isDeviza());
                row[8] = td.getOsszTermekDijBrutto(szla.isDeviza());
                model.addRow(row);
            }
        }

        DefaultTableRender render = new DefaultTableRender(new int[]{5, 7, 8}, szla.getValuta());
        TableColumn col;
        int[] meret = {150, 40, 40, 30, 60, 60, 30, 60, 60};
        for (int i = 0; i < meret.length; i++) {
            col = termekekTable.getColumnModel().getColumn(i);
            col.setPreferredWidth(meret[i]);
            col.setCellRenderer(render);
        }

        // áfa összesítő
        model = (DefaultTableModel) afaTable.getModel();
        row = new Object[4];
        double ossz = 0.0;
        for (Afa a : szla.getAfak(true)) {
            row[0] = a.getAfa() + "%";
            row[1] = a.getNetto(szla.isDeviza());
            row[2] = a.getAfaErtek(szla.isDeviza());
            row[3] = a.getBrutto(szla.isDeviza());
            ossz += a.getBrutto(szla.isDeviza());
            model.addRow(row);
        }
        boolean isUtalas = (szla.getVevo().getFizetesiMod() == 1) ? true : false;
        ossz = Functions.kerekit(ossz, isUtalas);
        render = new DefaultTableRender(new int[]{1, 2, 3}, szla.getValuta());
        for (int i = 0; i < 4; i++) {
            col = afaTable.getColumnModel().getColumn(i);
            col.setCellRenderer(render);
        }

        fizetendoLabel.setText((szla.getTipus() == 2 ? "Visszatérítendő" : "Fizetendő") + ": " + EncodeDecode.numberFormat(String.valueOf(ossz), szla.isDeviza()) + " " + szla.getValuta());
        betuvelLabel.setText("azaz " + betuvel(ossz) + " " + szla.getValuta());

        // megjegyzés
        megjegyzes.setText(szla.getMegjegyzes() + "\n" + szla.getLablec());
        megjegyzes.setCaretPosition(0);

        // kifizetések
        render = new DefaultTableRender(new int[]{1}, szla.getValuta());
        model = (DefaultTableModel) kifizetesekTable.getModel();
        Query query = new Query.QueryBuilder()
                .select("datum, osszeg")
                .from("szamlazo_szamla_kifizetesek")
                .where(" azon = '" + szla.getAzon() + "'")
                .build();
        model.setDataVector(App.db.select(query.getQuery()),
                new String[]{"Dátum", "Összeg"});
        meret = new int[]{100, 500};
        for (int i = 0; i < meret.length; i++) {
            col = kifizetesekTable.getColumnModel().getColumn(i);
            col.setPreferredWidth(meret[i]);
            col.setCellRenderer(render);
        }

        init("Száml" + (szla.getTipus() != 2 ? "a" : "ával egy tekintet alá eső okirat") + ": " + szla.getSorszam());

        // Close the dialog when Esc is pressed
        String cancelName = "cancel";
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), cancelName);
        ActionMap actionMap = getRootPane().getActionMap();
        actionMap.put(cancelName, new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                doClose(RET_CANCEL);
            }
        });
    }

    /**
     * @return the return status of this dialog - one of RET_OK or RET_CANCEL
     */
    public int getReturnStatus() {
        return returnStatus;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        szallito = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        vevo = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        fejlecTable = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        termekekTable = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        afaTable = new javax.swing.JTable();
        fizetendoLabel = new javax.swing.JLabel();
        betuvelLabel = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        megjegyzes = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        kifizetesekTable = new javax.swing.JTable();

        setName("Form"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(cezeszamlazo.App.class).getContext().getResourceMap(SzamlaAdatokDialog.class);
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("jPanel1.border.border.lineColor")), resourceMap.getString("jPanel1.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jPanel1.border.titleFont"))); // NOI18N
        jPanel1.setName("jPanel1"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        szallito.setColumns(20);
        szallito.setEditable(false);
        szallito.setFont(resourceMap.getFont("szallito.font")); // NOI18N
        szallito.setLineWrap(true);
        szallito.setRows(5);
        szallito.setText(resourceMap.getString("szallito.text")); // NOI18N
        szallito.setWrapStyleWord(true);
        szallito.setDisabledTextColor(resourceMap.getColor("szallito.disabledTextColor")); // NOI18N
        szallito.setEnabled(false);
        szallito.setName("szallito"); // NOI18N
        jScrollPane1.setViewportView(szallito);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("jPanel2.border.border.lineColor")), resourceMap.getString("jPanel2.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jPanel2.border.titleFont"))); // NOI18N
        jPanel2.setName("jPanel2"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        vevo.setColumns(20);
        vevo.setEditable(false);
        vevo.setFont(resourceMap.getFont("szallito.font")); // NOI18N
        vevo.setLineWrap(true);
        vevo.setRows(5);
        vevo.setText(resourceMap.getString("vevo.text")); // NOI18N
        vevo.setWrapStyleWord(true);
        vevo.setDisabledTextColor(resourceMap.getColor("vevo.disabledTextColor")); // NOI18N
        vevo.setEnabled(false);
        vevo.setName("vevo"); // NOI18N
        jScrollPane2.setViewportView(vevo);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane3.setName("jScrollPane3"); // NOI18N

        fejlecTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Fizetési mód", "Kelt", "Teljesítés", "Esedékesség"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        fejlecTable.setName("fejlecTable"); // NOI18N
        fejlecTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(fejlecTable);
        if (fejlecTable.getColumnModel().getColumnCount() > 0) {
            fejlecTable.getColumnModel().getColumn(0).setResizable(false);
            fejlecTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("fejlecTable.columnModel.title0")); // NOI18N
            fejlecTable.getColumnModel().getColumn(1).setResizable(false);
            fejlecTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("fejlecTable.columnModel.title1")); // NOI18N
            fejlecTable.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("fejlecTable.columnModel.title2")); // NOI18N
            fejlecTable.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("fejlecTable.columnModel.title3")); // NOI18N
        }

        jScrollPane4.setName("jScrollPane4"); // NOI18N

        termekekTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Megnevezés", "VTSZ/TESZOR", "Mennyiség", "Mee.", "Egységár", "Nettó", "ÁFA", "Áfa érték", "Bruttó"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        termekekTable.setName("termekekTable"); // NOI18N
        termekekTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(termekekTable);
        if (termekekTable.getColumnModel().getColumnCount() > 0) {
            termekekTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("termekekTable.columnModel.title0")); // NOI18N
            termekekTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("termekekTable.columnModel.title1")); // NOI18N
            termekekTable.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("termekekTable.columnModel.title2")); // NOI18N
            termekekTable.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("termekekTable.columnModel.title3")); // NOI18N
            termekekTable.getColumnModel().getColumn(4).setHeaderValue(resourceMap.getString("termekekTable.columnModel.title4")); // NOI18N
            termekekTable.getColumnModel().getColumn(5).setHeaderValue(resourceMap.getString("termekekTable.columnModel.title5")); // NOI18N
            termekekTable.getColumnModel().getColumn(6).setHeaderValue(resourceMap.getString("termekekTable.columnModel.title6")); // NOI18N
            termekekTable.getColumnModel().getColumn(7).setHeaderValue(resourceMap.getString("termekekTable.columnModel.title7")); // NOI18N
            termekekTable.getColumnModel().getColumn(8).setHeaderValue(resourceMap.getString("termekekTable.columnModel.title8")); // NOI18N
        }

        jScrollPane5.setName("jScrollPane5"); // NOI18N

        afaTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ÁFA", "Nettó", "Áfa érték", "Bruttó"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        afaTable.setName("afaTable"); // NOI18N
        afaTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane5.setViewportView(afaTable);
        if (afaTable.getColumnModel().getColumnCount() > 0) {
            afaTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("afaTable.columnModel.title0")); // NOI18N
            afaTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("afaTable.columnModel.title1")); // NOI18N
            afaTable.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("afaTable.columnModel.title2")); // NOI18N
            afaTable.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("afaTable.columnModel.title3")); // NOI18N
        }

        fizetendoLabel.setFont(resourceMap.getFont("fizetendoLabel.font")); // NOI18N
        fizetendoLabel.setText(resourceMap.getString("fizetendoLabel.text")); // NOI18N
        fizetendoLabel.setName("fizetendoLabel"); // NOI18N

        betuvelLabel.setFont(resourceMap.getFont("betuvelLabel.font")); // NOI18N
        betuvelLabel.setText(resourceMap.getString("betuvelLabel.text")); // NOI18N
        betuvelLabel.setName("betuvelLabel"); // NOI18N

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("jPanel3.border.border.lineColor")), resourceMap.getString("jPanel3.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jPanel3.border.titleFont"))); // NOI18N
        jPanel3.setName("jPanel3"); // NOI18N

        jScrollPane6.setName("jScrollPane6"); // NOI18N

        megjegyzes.setColumns(20);
        megjegyzes.setEditable(false);
        megjegyzes.setFont(resourceMap.getFont("megjegyzes.font")); // NOI18N
        megjegyzes.setLineWrap(true);
        megjegyzes.setRows(5);
        megjegyzes.setWrapStyleWord(true);
        megjegyzes.setDisabledTextColor(resourceMap.getColor("megjegyzes.disabledTextColor")); // NOI18N
        megjegyzes.setEnabled(false);
        megjegyzes.setName("megjegyzes"); // NOI18N
        jScrollPane6.setViewportView(megjegyzes);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 650, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(resourceMap.getColor("jPanel4.border.border.lineColor")), resourceMap.getString("jPanel4.border.title"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, resourceMap.getFont("jPanel4.border.titleFont"))); // NOI18N
        jPanel4.setName("jPanel4"); // NOI18N

        jScrollPane7.setName("jScrollPane7"); // NOI18N

        kifizetesekTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        kifizetesekTable.setName("kifizetesekTable"); // NOI18N
        kifizetesekTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane7.setViewportView(kifizetesekTable);
        if (kifizetesekTable.getColumnModel().getColumnCount() > 0) {
            kifizetesekTable.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("kifizetesekTable.columnModel.title0")); // NOI18N
            kifizetesekTable.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("kifizetesekTable.columnModel.title1")); // NOI18N
            kifizetesekTable.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("kifizetesekTable.columnModel.title2")); // NOI18N
            kifizetesekTable.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("kifizetesekTable.columnModel.title3")); // NOI18N
        }

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 650, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 680, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 680, Short.MAX_VALUE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 509, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fizetendoLabel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(betuvelLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 680, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fizetendoLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(betuvelLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Closes the dialog
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        doClose(RET_CANCEL);
    }//GEN-LAST:event_closeDialog

    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable afaTable;
    private javax.swing.JLabel betuvelLabel;
    private javax.swing.JTable fejlecTable;
    private javax.swing.JLabel fizetendoLabel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTable kifizetesekTable;
    private javax.swing.JTextArea megjegyzes;
    private javax.swing.JTextArea szallito;
    private javax.swing.JTable termekekTable;
    private javax.swing.JTextArea vevo;
    // End of variables declaration//GEN-END:variables
    private int returnStatus = RET_CANCEL;

    private void init(String title) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;

        java.net.URL url = ClassLoader.getSystemResource("cezeszamlazo/resources/szamlak.png");
        java.awt.Image img = toolkit.createImage(url);

        setIconImage(img);

        setLocation(x, y);
        setTitle(title);
        setModal(true);
        setVisible(true);
    }

    private String betuvel(double osszeg) {
        String result = "";
        int num = (int) Math.floor(osszeg);
        int tizedes = (int) Math.round((osszeg - Math.floor(osszeg)) * 100);
        int ezres = num % 1000;
        num /= 1000;
        int szazezres = num % 1000;
        num /= 1000;
        int millios = num % 1000;
        num /= 1000;
        int milliardos = num % 1000;
        num /= 1000;
        if (milliardos != 0) {
            result += azaz(milliardos) + "millárd";
            if (millios != 0 || szazezres != 0 || ezres != 0) {
                result += "-";
            }
        }
        if (millios != 0) {
            result += azaz(millios) + "millió";
            if (szazezres != 0 || ezres != 0) {
                result += "-";
            }
        }
        if (szazezres != 0) {
            result += azaz(szazezres) + "ezer";
            if (ezres != 0 && osszeg > 2000) {
                result += "-";
            }
        }
        if (ezres != 0) {
            result += azaz(ezres);
        }

        if (osszeg == 0) {
            result = "nulla";
        }

//	if (osszeg < 1) {
//	    result = "nulla";
//	}
        result = result.substring(0, 1).toUpperCase() + result.substring(1);
        if (tizedes != 0) {
            result += "\\" + tizedes;
        }
        return result;
    }

    private String azaz(int osszeg) {
        String result = "";
        int sz, t, e;
        String[] egyes = {"", "egy", "kettő", "három", "négy", "öt", "hat", "hét", "nyolc", "kilenc"};
        String[] tizes1 = {"", "tizen", "huszon", "harminc", "negyven", "ötven", "hatvan", "hetven", "nyolcvan", "kilencven"};
        String[] tizes2 = {"", "tíz", "húsz", "harminc", "negyven", "ötven", "hatvan", "hetven", "nyolcvan", "kilencven"};

        sz = osszeg / 100;
        t = osszeg % 100 / 10;
        e = osszeg % 10;

        if (sz < 0) {
            sz = sz * -1;
        }
        if (t < 0) {
            t = t * -1;
        }
        if (e < 0) {
            e = e * -1;
        }

        if (sz != 0) {
            result += egyes[sz] + "száz";
        }
        if (t != 0 && e != 0) {
            result += tizes1[t] + egyes[e];
        } else if (t != 0 && e == 0) {
            result += tizes2[t];
        } else if (t == 0 && e != 0) {
            result += egyes[e];
        }

        return result;
    }

}
