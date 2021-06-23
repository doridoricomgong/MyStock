package wo.main;

public class OrderListViewItem {
    private String contentStr ;
    private String highRateStr;
    private String lowRateStr;

    public void setContent(String content, String highRate, String lowRate) {
        contentStr = content ;
        highRateStr = highRate;
        lowRateStr = lowRate;
    }


    public String[] getContent() {
        return new String[]{contentStr, highRateStr, lowRateStr};
    }
}
