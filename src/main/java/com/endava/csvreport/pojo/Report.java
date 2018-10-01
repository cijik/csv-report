package com.endava.csvreport.pojo;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.univocity.parsers.annotations.Parsed;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@JsonPropertyOrder(value = {"paymentId","staffId","customerId"})
public class Report implements Serializable {

    @Parsed(index = 0)
    private Integer paymentId;
    @Parsed(index = 1)
    private Integer customerId;
    @Parsed(index = 2)
    private Integer staffId;
    @Parsed(index = 3)
    private Integer rentalId;
    @Parsed(index = 4)
    private Integer amount;
    @Parsed(index = 5)
    private Date paymentDate;

}
