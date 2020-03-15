package crm.dbservice.dataSets;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@Table(name = "trade_history")
public class TradeHistoryDataSet {

    public TradeHistoryDataSet(String name, int nums, double price, Date date, boolean income) {
        this.name = name;
        this.nums = nums;
        this.price = price;
        this.date = date;
        this.income = income;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "nums")
    private int nums;

    @Column(name = "price")
    private double price;

    @Column(name = "date")
    private Date date;

    @Column(name = "income")
    private boolean income;
}
