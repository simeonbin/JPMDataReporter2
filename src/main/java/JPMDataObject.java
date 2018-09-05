import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class JPMDataObject implements Comparable<JPMDataObject> {

        private String Entity;
        private String BuySell;
        private Double AgreedFx;
        private String Currency ;
        private String InstructionDate;
        private String SettlementDate;

   /* public JPMDataObject(String entity) {
        Entity = entity;
    }*/

        private Long   Units;
        private double PricePerUnit;

        public String getEntity() {
            return Entity;
        }

        public void setEntity(String Entity) {
            this.Entity = Entity;
        }

        public String getBuySell() {
            return BuySell;
        }

        public void setBuySell(String BuySell) {
            this.BuySell = BuySell;
        }

        public Double getAgreedFx() {
            return AgreedFx;
        }

        public void setAgreedFx(Double AgreedFx) {
            this.AgreedFx = AgreedFx;
        }

        public String getCurrency() {
            return Currency;
        }

        public void setCurrency(String Currency) {
            this.Currency = Currency;
        }

        public String getInstructionDate() {
            return InstructionDate;
        }

        public void setInstructionDate(String InstructionDate) {
            this.InstructionDate = InstructionDate;
        }

        public String getSettlementDate() {
            return SettlementDate;
        }

        public void setSettlementDate(String SettlementDate) {
            this.SettlementDate = SettlementDate;
        }

        public Long getUnits() {
            return Units;
        }

        public void setUnits(Long Units) {
            this.Units = Units;
        }

        public double getPricePerUnit() {
            return PricePerUnit;
        }

        public void setPricePerUnit(double PricePerUnit) {
            this.PricePerUnit = PricePerUnit;
        }

        // Compare two Dates in ascending order. Used in algorithm to Total
        // Incoming and Outgoing for a given Day.
        @Override
        public int compareTo(JPMDataObject o) {

            Date settleDate = null;
            Date  settleDate2 = null;

            try {

                String settleDateString = getSettlementDate();
                String settleDateString2 = (o.getSettlementDate());

                settleDate  =  new SimpleDateFormat("dd MMM yyyy").parse(settleDateString);
                settleDate2 =  new SimpleDateFormat("dd MMM yyyy").parse(settleDateString2);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (settleDate.before(settleDate2)) return -1;
            else if (settleDate.after(settleDate2)) return 1;
            else return 0;
        }

    }



