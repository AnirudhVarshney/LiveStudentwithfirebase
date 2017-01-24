package com.example.abhinav.studentfirebase;

/**
 * Created by Ray on 5/12/2016.
 */
/*    studentid date amount description type  */
public class Fee {


    private String date;
    private String amount;
    private String description;
    private String type;

    public Fee( String date, String amount, String description, String type) {

        this.date = date;
        this.amount = amount;
        this.description = description;
        this.type = type;
    }

    public Fee() {

        this.date = "";
        this.amount = "";
        this.type = "";
        this.description = "";
    }

    @Override
    public String toString() {
        return  " : date=" + date + " : amount=" + amount + " : type=" +
                type + " : description=" + description;
    }



    public String getdate() {
        return date;
    }

    public void setdate(String date) {
        this.date = date;
    }

    public String getamount() {
        return amount;
    }

    public void setamount(String amount) {
        this.amount = amount;
    }

    public String gettype() {
        return type;
    }

    public void settype(String type) {
        this.type = type;
    }

    public String getdescription() {
        return description;
    }

    public void setdescription(String description) {
        this.description = description;
    }
}
