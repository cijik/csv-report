package com.endava.csvreport.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Setter
@JsonPropertyOrder(value = {"paymentId","customerId","staffId","rentalId","amount","paymentDate"})
public class Report implements Serializable {

    private int paymentId;
    private int customerId;
    private int staffId;
    private int rentalId;
    private int amount;
    private Date paymentDate;

    public int getPaymentId() {
        return paymentId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getStaffId() {
        return staffId;
    }

    public int getRentalId() {
        return rentalId;
    }

    public int getAmount() {
        return amount;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    public Date getPaymentDate(){
        return paymentDate;
    }
}
