package wo.stock;

public class AlarmStockVO {
    private String name;
    private String code;
    private String highBound;
    private String lowBound;

    public AlarmStockVO(String name, String code, String highBound, String lowBound) {
        this.name = name;
        this.code = code;
        this.highBound = highBound;
        this.lowBound = lowBound;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getHighBound() {
        return highBound;
    }

    public void setHighBound(String highBound) {
        this.highBound = highBound;
    }

    public String getLowBound() {
        return lowBound;
    }

    public void setLowBound(String lowBound) {
        this.lowBound = lowBound;
    }

}