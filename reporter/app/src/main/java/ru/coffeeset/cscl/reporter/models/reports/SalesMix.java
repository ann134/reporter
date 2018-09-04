package ru.coffeeset.cscl.reporter.models.reports;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import ru.coffeeset.cscl.reporter.models.accessList.Division;

import static java.lang.String.valueOf;


public class SalesMix implements IReport {


//PARSING NEW REPORT

    public SalesMix() {
    }

    public SalesMix(String report) {
        Type type = new TypeToken<List<ReportItem>>() {
        }.getType();

        ReportItem = new ArrayList<>(new Gson().fromJson(report, type));
    }


//SALES MIX

    private String state = ReportState.LOADING;
    private List<SalesMix.ReportItem> ReportItem;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<SalesMix.ReportItem> getReportItems() {
        return ReportItem;
    }


//REPORT ITEM

    public class ReportItem {
        private String Author;
        private double FullSumm;
        private String DivisionName;
        private Division division;
        private List<Category> PurchaseCategoryInfo;

        public String getAuthor() {
            return Author;
        }

        public String getFullSumm() {
            return valueOf(FullSumm);
        }

        public String getDivisionName() {
            return DivisionName;
        }

        public List<Category> getPurchaseCategoryInfo() {

            List<Category> newCategoryInfo = new ArrayList<>();
            newCategoryInfo.add(null);
            newCategoryInfo.add(null);
            newCategoryInfo.add(null);
            newCategoryInfo.add(null);

            for (int i = 0; i < 4; i++) {
                for (Category c : PurchaseCategoryInfo) {
                    if (getCategoryByPosition(i).equals(c.getCategory()))
                        newCategoryInfo.set(i, c);
                }
            }

            for (int i = 0; i < newCategoryInfo.size(); i++) {
                if (newCategoryInfo.get(i) == null)
                    newCategoryInfo.set(i, new Category(getCategoryByPosition(i)));
            }

            return newCategoryInfo;
        }

        private String getCategoryByPosition(int position) {
            switch (position) {
                case 0:
                    return "coffee";
                case 1:
                    return "drink";
                case 2:
                    return "food";
                case 3:
                    return "others";
                default:
                    return "";
            }
        }
    }


//CATEGORY

    public class Category {
        private String Category;
        private double Qty;
        private double CategorySumm;
        private double Percent;

        private Category(String category) {
            this.Category = category;
        }

        public String getCategory() {
            return Category.toLowerCase();
        }

        public String getQty() {
            return valueOf(Qty);
        }

        public String getCategorySumm() {
            if (CategorySumm == 0)
                return "0";

            String categorySumm = valueOf(this.CategorySumm);

            DecimalFormat df = new DecimalFormat();
            df.setGroupingSize(3);
            DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance();
            dfs.setGroupingSeparator(' ');
            df.setDecimalFormatSymbols(dfs);

            categorySumm = categorySumm.split("\\.")[0];
            categorySumm = df.format(Integer.parseInt(categorySumm));

            return categorySumm;
        }

        public String getPercent() {
            if (CategorySumm == 0)
                return "% 0.0";
            return "% " + new DecimalFormat("#0.0").format(Percent);
        }
    }
}
