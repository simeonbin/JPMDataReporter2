//package JPMDataReporter;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Simeon
 */
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;


public class JPMDataReporter extends JFrame {
    JPMDataObject jpmData = new JPMDataObject();;

    // byValue cmp = new byValue();
    List<JPMDataObject> listJPMD = new ArrayList<>();

    Map<String, Double> incomingEntityMap = new TreeMap<>();
    Map<String, Double> outgoingEntityMap = new TreeMap<>();

    // Text file info
    private final JTextField jtfFilename = new JTextField();
    private final JTextArea jtaFile = new JTextArea();

    private final JTextArea jtaUsdIncoming = new JTextArea();
    JScrollPane jtaScrollUsdIncoming = new JScrollPane (jtaUsdIncoming,
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

    private final JTextArea jtaUsdOutgoing = new JTextArea();
    JScrollPane jtaScrollUsdOutgoing = new JScrollPane (jtaUsdOutgoing,
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

    private final JTextArea jtaRankingIncoming = new JTextArea();
    JScrollPane jtaScrollRankingIncoming = new JScrollPane (jtaRankingIncoming,
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

    private final JTextArea jtaRankingOutgoing = new JTextArea();
    JScrollPane jtaScrollRankingOutgoing = new JScrollPane (jtaRankingOutgoing,
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

    private final JTextArea jtaColumnNames = new JTextArea();
    JScrollPane jtaScrollColumnNames = new JScrollPane (jtaColumnNames,
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);


    private final JTextField jtfTableName = new JTextField();

    private final JButton jbtViewFile = new JButton("View JPM Data File");
    private final JButton jbtCopy = new JButton("Create Report");
    private JLabel jlblStatus = new JLabel();

    public JPMDataReporter() {

        JPanel jPanel1 = new JPanel();

        Dimension tfDim = new Dimension(120, 25);
        jtfFilename.setPreferredSize(tfDim);
        jtfFilename.setMinimumSize(tfDim);
        jtfFilename.setText("jpmdata_input.txt");
        jPanel1.setLayout(new BorderLayout());
        jPanel1.add(new JLabel("Filename"), BorderLayout.WEST);
        jPanel1.add(jbtViewFile, BorderLayout.EAST);

        jPanel1.add(jtfFilename, BorderLayout.CENTER);

        JPanel jPanel2 = new JPanel();
        jPanel2.setLayout(new BorderLayout());
        jPanel2.setBorder(new TitledBorder("Source JPM Data File"));
        jPanel2.add(jPanel1, BorderLayout.NORTH);
        jPanel2.add(new JScrollPane(jtaFile), BorderLayout.CENTER);

        JPanel jPanel3 = new JPanel();
        jPanel3.setLayout(new GridLayout(5, 0));
        jPanel3.add(new JLabel("USD Incoming"));
        jPanel3.add(new JLabel("USD Outgoing"));
        jPanel3.add(new JLabel("Ranking Incoming"));
        jPanel3.add(new JLabel("Ranking Outgoing"));
        jPanel3.add(new JLabel("Data Column Names"));

        JPanel jPanel4 = new JPanel();
        jPanel4.setLayout(new GridLayout(5, 0));

        jtaUsdIncoming.setEditable(false);
        jtaUsdOutgoing.setEditable(false);
        jtaRankingIncoming.setEditable(false);
        jtaRankingOutgoing.setEditable(false);

        jPanel4.add(jtaScrollUsdIncoming);
        jPanel4.add(jtaScrollUsdOutgoing);
        jPanel4.add(jtaScrollRankingIncoming);
        jPanel4.add(jtaScrollRankingOutgoing);
        jPanel4.add(jtaScrollColumnNames);

        JPanel jPanel5 = new JPanel();
        jPanel5.setLayout(new BorderLayout());
        jPanel5.setBorder(new TitledBorder("JPM Data Reporter"));
        jPanel5.add(jbtCopy, BorderLayout.SOUTH);
        jPanel5.add(jPanel3, BorderLayout.WEST);
        jPanel5.add(jPanel4, BorderLayout.CENTER);

        add(jlblStatus, BorderLayout.SOUTH);
        add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                jPanel2, jPanel5), BorderLayout.CENTER);

        jbtViewFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                readAndShowDataFile();
            }
        });

        jbtCopy.addActionListener (new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    createReport(listJPMD);
                }
                catch (Exception ex) {
                    jlblStatus.setText(ex.toString());
                }
            }
        });
    }

    // This helps with Sorting a Map by Value
    static <K,V extends Comparable<? super V>>
    SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {

        SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>> (

                new Comparator<Map.Entry<K,V>>() {

                    @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
                        int res = e2.getValue().compareTo(e1.getValue());
                        return res != 0 ? res : 1;
                    }
                }
        );

        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }


    /**
     * @param d
     * @param days
     * @return
     */
    public static Date addDays(Date d, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }

    public static boolean isBankHoliday(java.util.Date d, String currency) {
        Calendar c = new GregorianCalendar();
        c.setTime(d);

        if ( currency.equalsIgnoreCase("AED") || currency.equalsIgnoreCase("SAR") ) {

            if ((Calendar.FRIDAY == c.get(c.DAY_OF_WEEK)) || (Calendar.SATURDAY == c.get(c.DAY_OF_WEEK)) ) {
                return (true);
            } else {
                return false;
            }
        }

        else {
            if ((Calendar.SATURDAY == c.get(c.DAY_OF_WEEK)) || (Calendar.SUNDAY == c.get(c.DAY_OF_WEEK)) ) {
                return (true);
            } else {
                return false;
            }
        }
    }

    private String changeSettlementDate(String settleDateString, String currency) {
        DateTimeFormatter formatter;
        LocalDate settleDateLDT;
        java.util.Date settleDate = null;
        String dStr=null;

        try {
            settleDate =  new SimpleDateFormat("dd MMM yyyy").parse(settleDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        while (isBankHoliday(settleDate, currency)) {
            settleDate = addDays(settleDate, 1);
        }
        dStr = (new SimpleDateFormat("dd MMM yyyy").format(settleDate) );

        return dStr ;
    }


    /** Display the Financial Data File in the text area below in Front Screen */
    private void readAndShowDataFile() {
        String jpmDataObjStr;
        String settleDateString;
        String settleDateString2;
        String currency="USD";

        Scanner input = null;

        try {
            // Use a Scanner to read text from the file
            input = new Scanner(new File(jtfFilename.getText().trim()));
            ReadLineAppendToTextArea(input);
        }        // try
        catch (FileNotFoundException ex) {
            System.out.println("File not found: " + jtfFilename.getText());
        }
        /*catch (IOException ex) {
            ex.printStackTrace();
        }*/
        finally {
            if (input != null) input.close();
        }
    }

    private void ReadLineAppendToTextArea(Scanner input) {
        String jpmDataObjStr;
        String settleDateString;
        String currency;
        String settleDateString2;

        // Read a line and append the line to the text area
        while (input.hasNext()) {
            jpmDataObjStr = input.nextLine();
            jtaFile.append(jpmDataObjStr + '\n');

            String[] jpmD = jpmDataObjStr.split(",");
            JPMDataObject jpmDO = new JPMDataObject();


            settleDateString = jpmD[5].trim();
            currency = jpmD[3].trim();
            settleDateString2 = changeSettlementDate(settleDateString, currency );

            jpmDO.setEntity(jpmD[0].trim());
            jpmDO.setBuySell(jpmD[1].trim());
            jpmDO.setAgreedFx(Double.valueOf(jpmD[2].trim()));
            jpmDO.setCurrency(jpmD[3].trim());
            jpmDO.setInstructionDate(jpmD[4].trim());
            jpmDO.setSettlementDate(settleDateString2.trim());
            jpmDO.setUnits(Long.valueOf(jpmD[6].trim()) );
            jpmDO.setPricePerUnit(Double.valueOf(jpmD[7].trim()));

            listJPMD.add(jpmDO);
            Collections.sort(listJPMD);

            if (jpmDO.getBuySell().equalsIgnoreCase("S") ) {
                incomingEntityMap.put(jpmDO.getEntity(), 0.0 );
            }
            else {
                outgoingEntityMap.put(jpmDO.getEntity(), 0.0 );
            }
        }      // while (input.hasNext())
    }

    Double calculateUsdAmountOfTrade(Double ppu, Long units, Double agreedFx) {

        return (ppu * units * agreedFx);
    }

    // Create Report with prints and fill up the 4 Text Areas  with
    // Incoming and Outgoing USD$ Total Amount Settled Daily
    // Ranking of Entities according to Incoming and Outgoing USD$ Totals
    private void createReport(List<JPMDataObject> listJPMD) throws Exception {
        Double amt = 0.0;
        String settleDPrevious;
        String settleD;
        Double totalAmt = 0.0;
        Double valAmount = 0.0;
        Double value = 0.0;
        Integer iRankIncoming = 1;
        Integer iRankOutgoing = 1;
        JPMDataObject jpmDO = null;
        List<Double> amtIncomingList = new ArrayList<>();
        List<Double> amtOutgoingList = new ArrayList<>();

        jpmDO = listJPMD.get(0);

        jtaUsdIncoming.setText("");
        jtaUsdOutgoing.setText("");
        amtIncomingList.clear();
        amtOutgoingList.clear();
        jtaRankingIncoming.setText("");
        jtaRankingOutgoing.setText("");
        jtaColumnNames.setText("");

        settleTrade(listJPMD, jpmDO, amtIncomingList, amtOutgoingList);
        rankEntitiesToIncomingAmount(iRankIncoming);
        rankEntitiesToOutgoingAmount(iRankOutgoing);

        System.out.println("Entity BuySell AgreedFx Currency " + '\n' + "InstructionSet SettlementDate Units PricePerUnit" + '\n' );
        jtaColumnNames.append("Entity BuySell AgreedFx Currency " + '\n' + '\n' + "InstructionSet SettlementDate Units PricePerUnit" + '\n' );

    }   //private void createReport

    private void rankEntitiesToOutgoingAmount(Integer iRankOutgoing) {
        Double value;Set set;
        Iterator i;
        // Ranking of Entities according to Outgoing Amount
        set =  entriesSortedByValues(outgoingEntityMap);
        i = set.iterator();
        // Display elements
        while(i.hasNext()) {
            Map.Entry me = (Map.Entry)i.next();

            System.out.print("Rank : " + iRankOutgoing + ", Outgoing Entity : ");
            System.out.print(me.getKey() + ": ");
            jtaRankingOutgoing.append ("Rank : " + iRankOutgoing + ", Outgoing Entity : ");
            jtaRankingOutgoing.append (me.getKey() + ": ");
            iRankOutgoing++;

            value = (double)Math.round(100d * (double)me.getValue()) / 100d;
            System.out.println(value);
            jtaRankingOutgoing.append(" " + value + '\n');
        }
        System.out.println();
    }

    private void rankEntitiesToIncomingAmount(Integer iRankIncoming) {
        Double value;// Ranking of Entities according to Incoming Amount
        Set set =  entriesSortedByValues(incomingEntityMap);
        Iterator i = set.iterator();
        // Display elements
        while(i.hasNext()) {
            Map.Entry me = (Map.Entry)i.next();

            System.out.print("Rank : " + iRankIncoming + ", Incoming Entity : ");
            System.out.print(me.getKey() + ": ");
            jtaRankingIncoming.append ("Rank : " + iRankIncoming + ", Incoming Entity : ");
            jtaRankingIncoming.append (me.getKey() + ": ");
            iRankIncoming++;

            value = (double)Math.round(100d * (double)me.getValue()) / 100d;
            System.out.println(value);
            jtaRankingIncoming.append(" " + value + '\n');
        }
        System.out.println();
    }


    private void settleTrade(List<JPMDataObject> listJPMD, JPMDataObject jpmDO, List<Double> amtIncomingList, List<Double> amtOutgoingList) {
        String settleDPrevious;
        String settleD;
        Double totalAmt;
        Double amt;
        Double valAmount;
        Double value;
        settleDPrevious = jpmDO.getSettlementDate();
        Iterator<JPMDataObject> it = listJPMD.iterator();

        settleD = settleDPrevious;

        try {
            jpmDO = it.next();
        }  catch (NoSuchElementException e) {
            System.out.println("We found NO Elements in the List");
        }

        while (it.hasNext() || (jpmDO != null) ) {

            amtIncomingList.clear();
            amtOutgoingList.clear();
            totalAmt = 0.0;
            settleDPrevious = settleD;

            while (settleDPrevious.equals(settleD)) {
                amt = calculateUsdAmountOfTrade (jpmDO.getPricePerUnit(), jpmDO.getUnits(), jpmDO.getAgreedFx() );

                setEntityMap(jpmDO, amtIncomingList, amtOutgoingList, amt);
                settleDPrevious = settleD;

                if (it.hasNext())  {
                    jpmDO = it.next();
                    settleD = jpmDO.getSettlementDate();
                }
                else {
                    jpmDO = null;
                    break;
                }
            }  // end while

            Boolean listIncomingStatus = (amtIncomingList.isEmpty() || (amtIncomingList == null) );
            totalAmt = amountIncoming(amtIncomingList, settleDPrevious, totalAmt, listIncomingStatus);

            Boolean listOutgoingStatus = (amtOutgoingList.isEmpty() || (amtOutgoingList == null) );
            amountOutgoing(amtOutgoingList, settleDPrevious, totalAmt, listOutgoingStatus);

        } // while (it.hasNext() || (jpmDO != null) )
    }   // settleTrade

    private void setEntityMap(JPMDataObject jpmDO, List<Double> amtIncomingList, List<Double> amtOutgoingList, Double amt) {
        Double valAmount;
        if (jpmDO.getBuySell().equalsIgnoreCase("S")) {
            amtIncomingList.add(amt);
            valAmount = incomingEntityMap.get(jpmDO.getEntity());
            incomingEntityMap.put(jpmDO.getEntity(), (valAmount + amt) );
        }
        else {
            amtOutgoingList.add(amt);
            valAmount = outgoingEntityMap.get(jpmDO.getEntity());
            outgoingEntityMap.put(jpmDO.getEntity(), (valAmount + amt) );
        }
    }

    private void amountOutgoing(List<Double> amtOutgoingList, String settleDPrevious, Double totalAmt, Boolean listOutgoingStatus) {
        Double value;
        if (!listOutgoingStatus) {

            System.out.print(settleDPrevious + " " + "Outgoing : ");
            jtaUsdOutgoing.append (settleDPrevious + " ");

            for (int i=0; i <= (amtOutgoingList.size() - 1); i++ ) {
                totalAmt += amtOutgoingList.get(i);
            }

            value = (double)Math.round(100d * totalAmt) / 100d;
            System.out.println( String.valueOf(value) );
            System.out.println();
            jtaUsdOutgoing.append (String.valueOf(value) + '\n' );
        }
    }

    private Double amountIncoming(List<Double> amtIncomingList, String settleDPrevious, Double totalAmt, Boolean listIncomingStatus) {
        Double value;
        if (!listIncomingStatus) {

            System.out.print(settleDPrevious + " " + "Incoming : ");
            jtaUsdIncoming.append (settleDPrevious + " ");

            for (int i=0; i <= (amtIncomingList.size() - 1); i++ ) {
                totalAmt += amtIncomingList.get(i);
            }

            value = (double)Math.round(100d * totalAmt) / 100d;
            System.out.println( String.valueOf(value) );
            System.out.println();
            jtaUsdIncoming.append (String.valueOf(value) + '\n' );
        }
        return totalAmt;
    }


    public static void main(String args[]) {
        JFrame frame = new JPMDataReporter();
        frame.setTitle("JP Morgan Data Reporter");
        frame.setSize(700, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}

