package wo.stock;

public class StockVO {
    private String name;
    private String code;
    private String hPrice;
    private String lPrice;
    private String sPrice;
    private String vPrice;
    private String pPrice;
    private String vRate;
    private String tradeAmount;

    public String getvRate() { return vRate; }

    public void setvRate(String vRate) { this.vRate = vRate; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getHighPrice() {
        return hPrice;
    }

    public void setHighPrice(String hPrice) {
        this.hPrice = hPrice;
    }

    public String getLowPrice() {
        return lPrice;
    }

    public void setLowPrice(String lPrice) {
        this.lPrice = lPrice;
    }

    public String getStartPrice() {
        return sPrice;
    }

    public void setStartPrice(String sPrice) {
        this.sPrice = sPrice;
    }

    public String getVariPrice() {
        return vPrice;
    }

    public void setVariPrice(String vPrice) {
        this.vPrice = vPrice;
    }

    public String getPrePrice() {
        return pPrice;
    }

    public void setPrePrice(String pPrice) {
        this.pPrice = pPrice;
    }

    public String getTradeAmount() {
        return tradeAmount;
    }

    public void setTradeAmount(String tradeAmount) {
        this.tradeAmount = tradeAmount;
    }

    public int size() {
        return 8;
    }
}