package cn.looty.example.生成PPTX.utils;

import java.util.List;

public class ExponentRatioVo {
    // 项目名称
    private String itemName;
    // 上线以来
    private String itemUp;
    // 3月迭代以来
    private String itemDown;
    // 值
    private List<ExponentVo> exponents;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemUp() {
        return itemUp;
    }

    public void setItemUp(String itemUp) {
        this.itemUp = itemUp;
    }

    public String getItemDown() {
        return itemDown;
    }

    public void setItemDown(String itemDown) {
        this.itemDown = itemDown;
    }

    public List<ExponentVo> getExponents() {
        return exponents;
    }

    public void setExponents(List<ExponentVo> exponents) {
        this.exponents = exponents;
    }
}
