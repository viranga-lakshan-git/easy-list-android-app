package lk.sliit.easylist.module;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

public class ListItem implements Parcelable,Comparable<ListItem> {
    String itemName;
    int amount;

    public ListItem(String itemName, int count) {
        this.itemName = itemName;
        this.amount = count;
    }

    public ListItem() {
    }

    protected ListItem(Parcel in) {
        itemName = in.readString();
        amount = in.readInt();
    }

    public static final Creator<ListItem> CREATOR = new Creator<ListItem>() {
        @Override
        public ListItem createFromParcel(Parcel in) {
            return new ListItem(in);
        }

        @Override
        public ListItem[] newArray(int size) {
            return new ListItem[size];
        }
    };

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(itemName);
        dest.writeInt(amount);
    }

    @Override
    public int compareTo(ListItem o) {
        if (itemName.equals(o.itemName))
            return 1;
        else return 0;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof ListItem){
            ListItem o = (ListItem)obj;
            if(this.getItemName().equals(o.getItemName())){
                return true;
            }
        }
        return false;
    }
}
