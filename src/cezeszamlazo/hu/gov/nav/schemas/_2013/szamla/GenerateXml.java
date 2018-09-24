/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cezeszamlazo.hu.gov.nav.schemas._2013.szamla;

import cezeszamlazo.App;
import cezeszamlazo.database.Query;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class GenerateXml {

    protected Object[][] szamlakArray;

    public GenerateXml() {
    }

    public boolean generateByDate(String kezdoIdo, String zaroIdo, File file) throws Exception {
        JAXBContext context = JAXBContext.newInstance(SzamlakTipus.class);

        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        SzamlakTipus szamlak = new SzamlakTipus();
        Query query = new Query.QueryBuilder()
                .select("*")
                .from("szamlazo_szamla")
                .where("kelt BETWEEN '" + kezdoIdo + "' AND '" + zaroIdo + "' ")
                .order("id")
                .build();
        szamlakArray = App.db.select(query.getQuery());
        int maxCount = szamlakArray.length - 1;

//        System.out.println(szamlakArray.length);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(new Date());
        XMLGregorianCalendar calendar = javax.xml.datatype.DatatypeFactory.newInstance()
                .newXMLGregorianCalendar(
                        date);

        szamlak.setExportDatuma(calendar);
        szamlak.setExportSzlaDb(new BigDecimal(szamlakArray.length));
        szamlak.setKezdoIdo(Helper.convertStringToXMLDate(kezdoIdo));
        szamlak.setKezdoSzlaSzam(String.valueOf(szamlakArray[0][1]));
        szamlak.setZaroIdo(Helper.convertStringToXMLDate(zaroIdo));
        szamlak.setZaroSzlaSzam(String.valueOf(szamlakArray[maxCount][1]));

        List<SzamlaTipus> szamlaList = new ArrayList<SzamlaTipus>();

        for (int i = 0; i < szamlakArray.length; i++) {
            SzamlaTipus szamla = new SzamlaTipus();
            FejlecTipus fejlecTipus = new FejlecTipus();
            fejlecTipus.setSzlasorszam(String.valueOf(szamlakArray[i][1]));
            fejlecTipus.setSzlatipus(new BigInteger(String.valueOf(szamlakArray[i][10])));
            fejlecTipus.setSzladatum(Helper.convertStringToXMLDate(String.valueOf(szamlakArray[i][4])));
            fejlecTipus.setTeljdatum(Helper.convertStringToXMLDate(String.valueOf(szamlakArray[i][6])));

            SzamlakibocsatoTipus szamlakibocsatoTipus = new SzamlakibocsatoTipus();
            szamlakibocsatoTipus.setAdoszam(String.valueOf(szamlakArray[i][33]));
            szamlakibocsatoTipus.setNev(String.valueOf(szamlakArray[i][29]));

            CimTipus cimTipus = new CimTipus();
            cimTipus.setIranyitoszam(String.valueOf(szamlakArray[i][30]));
            cimTipus.setTelepules(String.valueOf(szamlakArray[i][31]));
            cimTipus.setKozteruletNeve(String.valueOf(szamlakArray[i][32]));
            cimTipus.setKozteruletJellege("");
            cimTipus.setHazszam("");
            szamlakibocsatoTipus.setCim(cimTipus);

            VevoTipus vevoTipus = new VevoTipus();
            vevoTipus.setNev(String.valueOf(szamlakArray[i][11]));

            cimTipus = new CimTipus();
            cimTipus.setIranyitoszam(String.valueOf(szamlakArray[i][12]));
            cimTipus.setTelepules(String.valueOf(szamlakArray[i][13]));
            cimTipus.setKozteruletNeve(String.valueOf(szamlakArray[i][14]));
            cimTipus.setKozteruletJellege("");
            cimTipus.setHazszam("");
            vevoTipus.setCim(cimTipus);

            String termekAzon = String.valueOf(szamlakArray[i][24]);
            String szamlaSorszam = String.valueOf(szamlakArray[i][1]);
            query = new Query.QueryBuilder()
                    .select("*")
                    .from("szamlazo_szamla_adatok")
                    .where("azon = '" + termekAzon + "' AND szamla_sorszam = '" + szamlaSorszam + "'")
                    .build();
            Object[][] termek = App.db.select(query.getQuery());
            List<TermekSzolgaltatasTetelekTipus> tstts = new ArrayList<TermekSzolgaltatasTetelekTipus>();

            TermekSzolgaltatasTetelekTipus termekSzolgaltatasTetelekTipus = null;

            for (int j = 0; j < termek.length; j++) {

                termekSzolgaltatasTetelekTipus = new TermekSzolgaltatasTetelekTipus();
                termekSzolgaltatasTetelekTipus.setTermeknev(String.valueOf(termek[j][2]));
                termekSzolgaltatasTetelekTipus.setBesorszam(String.valueOf(termek[j][10]));
                termekSzolgaltatasTetelekTipus.setMenny(new BigDecimal(String.valueOf(termek[j][4])));
                termekSzolgaltatasTetelekTipus.setMertekegys(String.valueOf(termek[j][5]));

                BigDecimal nettoar = new BigDecimal(String.valueOf(termek[j][7]));
                BigDecimal adokulcs = new BigDecimal(String.valueOf(termek[j][8]));
                BigDecimal adoertek = nettoar.multiply(adokulcs.divide(new BigDecimal("100")));
                BigDecimal bruttoar = nettoar.add(adoertek);

                termekSzolgaltatasTetelekTipus.setNettoar(nettoar);
                termekSzolgaltatasTetelekTipus.setNettoegysar(new BigDecimal(String.valueOf(termek[j][6])));
                termekSzolgaltatasTetelekTipus.setAdokulcs(adokulcs);
                termekSzolgaltatasTetelekTipus.setAdoertek(adoertek);
                termekSzolgaltatasTetelekTipus.setBruttoar(bruttoar);
                termekSzolgaltatasTetelekTipus.setArengedm(new BigDecimal(String.valueOf(termek[j][9])));
            }

            tstts.add(termekSzolgaltatasTetelekTipus);

            NemKotelezoTipus nemKotelezoTipus = new NemKotelezoTipus();
            nemKotelezoTipus.setFizHatarido(Helper.convertStringToXMLDate(String.valueOf(szamlakArray[i][5])));
            String fizMod = "Készpénz";
            switch (Integer.parseInt(String.valueOf(szamlakArray[i][2]))) {
                case 0:
                    fizMod = "Készpénz";
                    break;
                case 1:
                    fizMod = "Átutalás";
                    break;
                case 2:
                    fizMod = "Utánvét";
                    break;
            }
            nemKotelezoTipus.setFizMod(fizMod);
            nemKotelezoTipus.setSzlaForma("papír alapon továbbított számla");
            nemKotelezoTipus.setKibocsatoBankszla(String.valueOf(szamlakArray[i][35]));

            AfarovatTipus afarovatTipus = new AfarovatTipus();
            BigDecimal nettoAr = new BigDecimal(String.valueOf(szamlakArray[i][8]));
            query = new Query.QueryBuilder()
                    .select("afa")
                    .from("szamlazo_afa")
                    .where("id = " + (Integer.parseInt(String.valueOf(szamlakArray[i][10])) + 1))
                    .order("")
                    .build();
            Object[][] adoKulcsObject = App.db.select(query.getQuery());

            BigDecimal adoKulcs = new BigDecimal(String.valueOf(adoKulcsObject[0][0]));
            BigDecimal adoErtek = new BigDecimal(String.valueOf(szamlakArray[i][9]));
            BigDecimal bruttoAr = nettoAr.add(adoErtek);

            afarovatTipus.setNettoar(nettoAr);
            afarovatTipus.setAdokulcs(adoKulcs);
            afarovatTipus.setAdoertek(adoErtek);
            afarovatTipus.setBruttoar(bruttoAr);

            List<AfarovatTipus> afarovatTipuses = new ArrayList<AfarovatTipus>();
            afarovatTipuses.add(afarovatTipus);

            VegosszegTipus vegosszegTipus = new VegosszegTipus();
            vegosszegTipus.setNettoarossz(nettoAr);
            vegosszegTipus.setAfaertekossz(adoErtek);
            vegosszegTipus.setBruttoarossz(bruttoAr);

            OsszesitesTipus osszesitesTipus = new OsszesitesTipus();
            osszesitesTipus.setAfarovat(afarovatTipuses);
            osszesitesTipus.setVegosszeg(vegosszegTipus);

            szamla.setFejlec(fejlecTipus);
            szamla.setSzamlakibocsato(szamlakibocsatoTipus);
            szamla.setVevo(vevoTipus);
            szamla.setTermekSzolgaltatasTetelek(tstts);
            szamla.setNemKotelezo(nemKotelezoTipus);
            szamla.setOsszesites(osszesitesTipus);
            szamlaList.add(szamla);
        }
        szamlak.setSzamla(szamlaList);

//        marshaller.marshal(szamlak, System.out);
        marshaller.marshal(szamlak, file);
        return true;

    }

    public boolean generateBySorszam(String type, int yearBegin, int yearEnd, int start, int end, File file) throws Exception {

        JAXBContext context = JAXBContext.newInstance(SzamlakTipus.class);
//
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        SzamlakTipus szamlak = new SzamlakTipus();
        List<String> szamlaSorszamList = new ArrayList<String>();
        String whereText = "WHERE 1 = 0 ";
        String szamlaSorszam = type + " " + yearBegin + "/";

        if (yearBegin == yearEnd) {
            for (int i = start; i <= end; i++) {
                String tmpSorszam = szamlaSorszam + i;
                szamlaSorszamList.add(tmpSorszam);
            }
            for (String tmpSzamlaSorszam : szamlaSorszamList) {
                whereText += "OR szamla_sorszam = '" + tmpSzamlaSorszam + "' ";

            }
        } else {
            int tmpYear = yearEnd - yearBegin;
            List<Integer> years = new ArrayList<Integer>();
            years.add(yearBegin);
            for (int i = 0; i < tmpYear; i++) {
                years.add(yearBegin + i + 1);

            }

            for (Integer year : years) {
                if (year == yearEnd) {
                    for (int i = 1; i <= end; i++) {
                        String tmpSorszam = type + " " + yearEnd + "/" + i;
                        szamlaSorszamList.add(tmpSorszam);
                    }
                    for (String tmpSzamlaSorszam : szamlaSorszamList) {
                        whereText += "OR szamla_sorszam = '" + tmpSzamlaSorszam + "' ";

                    }
                } else {
                    szamlaSorszam = type + " " + year + "/%";
                    whereText += "OR szamla_sorszam like '" + szamlaSorszam + "' ";
                }

            }
        }
        Query query = new Query.QueryBuilder()
                .select("*")
                .from("szamlazo_szamla")
                .where(whereText)
                .order("id")
                .build();
        szamlakArray = App.db.select(query.getQuery());

        int maxCount = szamlakArray.length - 1;

//        System.out.println(szamlakArray.length);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(new Date());
        XMLGregorianCalendar calendar = DatatypeFactory.newInstance()
                .newXMLGregorianCalendar(
                        date);

        szamlak.setExportDatuma(calendar);
        szamlak.setExportSzlaDb(new BigDecimal(szamlakArray.length));
        szamlak.setKezdoIdo(Helper.convertStringToXMLDate(String.valueOf(szamlakArray[0][4])));
        szamlak.setKezdoSzlaSzam(String.valueOf(szamlakArray[0][1]));
        szamlak.setZaroIdo(Helper.convertStringToXMLDate(String.valueOf(szamlakArray[maxCount][4])));
        szamlak.setZaroSzlaSzam(String.valueOf(szamlakArray[maxCount][1]));

        List<SzamlaTipus> szamlaList = new ArrayList<SzamlaTipus>();

        for (int i = 0; i < szamlakArray.length; i++) {
            SzamlaTipus szamla = new SzamlaTipus();
            FejlecTipus fejlecTipus = new FejlecTipus();
            fejlecTipus.setSzlasorszam(String.valueOf(szamlakArray[i][1]));
            fejlecTipus.setSzlatipus(new BigInteger(String.valueOf(szamlakArray[i][10])));
            fejlecTipus.setSzladatum(Helper.convertStringToXMLDate(String.valueOf(szamlakArray[i][4])));
            fejlecTipus.setTeljdatum(Helper.convertStringToXMLDate(String.valueOf(szamlakArray[i][6])));

            SzamlakibocsatoTipus szamlakibocsatoTipus = new SzamlakibocsatoTipus();
            szamlakibocsatoTipus.setAdoszam(String.valueOf(szamlakArray[i][33]));
            szamlakibocsatoTipus.setNev(String.valueOf(szamlakArray[i][29]));

            CimTipus cimTipus = new CimTipus();
            cimTipus.setIranyitoszam(String.valueOf(szamlakArray[i][30]));
            cimTipus.setTelepules(String.valueOf(szamlakArray[i][31]));
            cimTipus.setKozteruletNeve(String.valueOf(szamlakArray[i][32]));
            cimTipus.setKozteruletJellege("");
            cimTipus.setHazszam("");
            szamlakibocsatoTipus.setCim(cimTipus);

            VevoTipus vevoTipus = new VevoTipus();
            vevoTipus.setNev(String.valueOf(szamlakArray[i][11]));

            cimTipus = new CimTipus();
            cimTipus.setIranyitoszam(String.valueOf(szamlakArray[i][12]));
            cimTipus.setTelepules(String.valueOf(szamlakArray[i][13]));
            cimTipus.setKozteruletNeve(String.valueOf(szamlakArray[i][14]));
            cimTipus.setKozteruletJellege("");
            cimTipus.setHazszam("");
            vevoTipus.setCim(cimTipus);

            String termekAzon = String.valueOf(szamlakArray[i][24]);
            szamlaSorszam = String.valueOf(szamlakArray[i][1]);
            query = new Query.QueryBuilder()
                    .select("*")
                    .from("szamlazo_szamla_adatok")
                    .where("azon = '" + termekAzon + "' AND szamla_sorszam = '" + szamlaSorszam + "'")
                    .build();
            Object[][] termek = App.db.select(query.getQuery());
            List<TermekSzolgaltatasTetelekTipus> tstts = new ArrayList<TermekSzolgaltatasTetelekTipus>();

            TermekSzolgaltatasTetelekTipus termekSzolgaltatasTetelekTipus = null;

            for (int j = 0; j < termek.length; j++) {

                termekSzolgaltatasTetelekTipus = new TermekSzolgaltatasTetelekTipus();
                termekSzolgaltatasTetelekTipus.setTermeknev(String.valueOf(termek[j][2]));
                termekSzolgaltatasTetelekTipus.setBesorszam(String.valueOf(termek[j][10]));
                termekSzolgaltatasTetelekTipus.setMenny(new BigDecimal(String.valueOf(termek[j][4])));
                termekSzolgaltatasTetelekTipus.setMertekegys(String.valueOf(termek[j][5]));

                BigDecimal nettoar = new BigDecimal(String.valueOf(termek[j][7]));
                BigDecimal adokulcs = new BigDecimal(String.valueOf(termek[j][8]));
                BigDecimal adoertek = nettoar.multiply(adokulcs.divide(new BigDecimal("100")));
                BigDecimal bruttoar = nettoar.add(adoertek);

                termekSzolgaltatasTetelekTipus.setNettoar(nettoar);
                termekSzolgaltatasTetelekTipus.setNettoegysar(new BigDecimal(String.valueOf(termek[j][6])));
                termekSzolgaltatasTetelekTipus.setAdokulcs(adokulcs);
                termekSzolgaltatasTetelekTipus.setAdoertek(adoertek);
                termekSzolgaltatasTetelekTipus.setBruttoar(bruttoar);
                termekSzolgaltatasTetelekTipus.setArengedm(new BigDecimal(String.valueOf(termek[j][9])));
            }

            tstts.add(termekSzolgaltatasTetelekTipus);

            NemKotelezoTipus nemKotelezoTipus = new NemKotelezoTipus();
            nemKotelezoTipus.setFizHatarido(Helper.convertStringToXMLDate(String.valueOf(szamlakArray[i][5])));
            String fizMod = "Készpénz";
            switch (Integer.parseInt(String.valueOf(szamlakArray[i][2]))) {
                case 0:
                    fizMod = "Készpénz";
                    break;
                case 1:
                    fizMod = "Átutalás";
                    break;
                case 2:
                    fizMod = "Utánvét";
                    break;
            }
            nemKotelezoTipus.setFizMod(fizMod);
            nemKotelezoTipus.setSzlaForma("papír alapon továbbított számla");
            nemKotelezoTipus.setKibocsatoBankszla(String.valueOf(szamlakArray[i][35]));

            AfarovatTipus afarovatTipus = new AfarovatTipus();
            BigDecimal nettoAr = new BigDecimal(String.valueOf(szamlakArray[i][8]));
            query = new Query.QueryBuilder()
                    .select("afa")
                    .from("szamlazo_afa")
                    .where("id = " + (Integer.parseInt(String.valueOf(szamlakArray[i][10])) + 1))
                    .build();
            Object[][] adoKulcsObject = App.db.select(query.getQuery());

            BigDecimal adoKulcs = new BigDecimal(String.valueOf(adoKulcsObject[0][0]));
            BigDecimal adoErtek = new BigDecimal(String.valueOf(szamlakArray[i][9]));
            BigDecimal bruttoAr = nettoAr.add(adoErtek);

            afarovatTipus.setNettoar(nettoAr);
            afarovatTipus.setAdokulcs(adoKulcs);
            afarovatTipus.setAdoertek(adoErtek);
            afarovatTipus.setBruttoar(bruttoAr);

            List<AfarovatTipus> afarovatTipuses = new ArrayList<AfarovatTipus>();
            afarovatTipuses.add(afarovatTipus);

            VegosszegTipus vegosszegTipus = new VegosszegTipus();
            vegosszegTipus.setNettoarossz(nettoAr);
            vegosszegTipus.setAfaertekossz(adoErtek);
            vegosszegTipus.setBruttoarossz(bruttoAr);

            OsszesitesTipus osszesitesTipus = new OsszesitesTipus();
            osszesitesTipus.setAfarovat(afarovatTipuses);
            osszesitesTipus.setVegosszeg(vegosszegTipus);

            szamla.setFejlec(fejlecTipus);
            szamla.setSzamlakibocsato(szamlakibocsatoTipus);
            szamla.setVevo(vevoTipus);
            szamla.setTermekSzolgaltatasTetelek(tstts);
            szamla.setNemKotelezo(nemKotelezoTipus);
            szamla.setOsszesites(osszesitesTipus);
            szamlaList.add(szamla);
        }
        szamlak.setSzamla(szamlaList);

//        marshaller.marshal(szamlak, System.out);
        marshaller.marshal(szamlak, file);
        return true;
    }

}
