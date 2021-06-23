package wo.stock;

public class StockGroupVO {
    private String name;
    private StockVO stockVO = new StockVO();

    public StockGroupVO(String name, StockVO stockVO) {
        //super();
        this.name = name;
        this.stockVO = stockVO;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StockVO getStockVO() {
        return stockVO;
    }

    public void setStockVO(StockVO stockVO) {
        this.stockVO = stockVO;
    }
}