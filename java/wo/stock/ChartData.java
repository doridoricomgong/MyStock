package wo.stock;

public class ChartData {
    private int date;
    private float startPrice;
    private float endPrice;
    private float highPrice;
    private float lowPrice;

    public ChartData(int d, float sP, float eP, float hP, float lP) {
        this.date = d;
        this.startPrice = sP;
        this.endPrice = eP;
        this.highPrice = hP;
        this.lowPrice = lP;
    }

    public int getDate() { return date; }
    public float getStartPrice() { return startPrice; }
    public float getEndPrice() { return endPrice; }
    public float getHighPrice() { return highPrice; }
    public float getLowPrice() { return lowPrice; }

}
