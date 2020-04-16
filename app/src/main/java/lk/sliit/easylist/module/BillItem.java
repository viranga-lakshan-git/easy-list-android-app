package lk.sliit.easylist.module;

import java.util.ArrayList;

public class BillItem {
    private String billno;
    private ArrayList<String> items;

    public  BillItem(){}

    public BillItem(String billno, ArrayList<String> items) {
        this.billno = billno;
        this.items = items;
    }

    public String getBillno() {
        return billno;
    }

    public void setBillno(String billno) {
        this.billno = billno;
    }

    public ArrayList<String> getItems() {
        return items;
    }

    public void setItems(ArrayList<String> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return billno + "  " +
                items
                ;
    }
}
