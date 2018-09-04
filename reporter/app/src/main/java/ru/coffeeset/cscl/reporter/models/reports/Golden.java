package ru.coffeeset.cscl.reporter.models.reports;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.valueOf;


public class Golden implements IReport {


//PARSING NEW REPORT

    public Golden() {
    }

    public Golden(String report) {
        Type type = new TypeToken<List<ReportItem>>() {
        }.getType();

        ReportItem = new ArrayList<>(new Gson().fromJson(report, type));
    }


//GOLDEN
    private String state = ReportState.LOADING;
    private List<ReportItem> ReportItem;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<ReportItem> getReportItems() {
        return ReportItem;
    }



//REPORT ITEM

    public class ReportItem {

        private String Author;
        private String DivisionName;
        private double SumInvoiceQty;
        private double Rate;
        private List<Product> ProductInfo;

        public String getAuthor() {
            return Author;
        }

        public String getDivisionName() {
            return DivisionName;
        }

        public String getSumInvoiceQty() {
            return valueOf((int) SumInvoiceQty);
        }

        public String getRate() {
            return "% " + new DecimalFormat("#0.0").format(Rate);
        }

        public List<Product> getProductInfo() {

            List<Product> newProductInfo = new ArrayList<>();
            newProductInfo.add(null);
            newProductInfo.add(null);
            newProductInfo.add(null);

            for (int i = 0; i < 3; i++) {
                for (Product p : ProductInfo) {
                    if (getCategoryByPosition(i).equals(p.getCategory()))
                        newProductInfo.set(i, p);
                }
            }

            for (int i = 0; i < newProductInfo.size(); i++) {
                if (newProductInfo.get(i) == null)
                    newProductInfo.set(i, new Product(getCategoryByPosition(i)));
            }

            return newProductInfo;
        }

        private String getCategoryByPosition(int position) {
            switch (position) {
                case 0:
                    return "coffee";
                case 1:
                    return "drink";
                case 2:
                    return "food";
                default:
                    return "";
            }
        }
    }


//PRODUCT

    public class Product {
        private String Category;
        private double Qty;

        private Product(String category) {
            this.Category = category;
        }

        public String getCategory() {
            return Category.toLowerCase();
        }

        public String getQty() {
            return valueOf((int) Qty);
        }
    }
}
