package cn.looty.common.enums;

import org.apache.poi.ss.usermodel.HorizontalAlignment;

public enum ExportColumnEnum {
    EXAMPLE_NO("no", "序号", ExportTypeEnum.EXAMPLE, HorizontalAlignment.CENTER, 2, true, 1),
    EXAMPLE_NAME("name", "名", ExportTypeEnum.EXAMPLE, HorizontalAlignment.CENTER, 4, true, 2),
    EXAMPLE_TYPE("type", "类型", ExportTypeEnum.EXAMPLE, HorizontalAlignment.CENTER, 4, true, 3),
    EXAMPLE_START("start", "开始时间", ExportTypeEnum.EXAMPLE, HorizontalAlignment.CENTER, 8, true, 5),
    EXAMPLE_END("end", "结束时间", ExportTypeEnum.EXAMPLE, HorizontalAlignment.CENTER, 8, true, 7),

    ;

    private final String column;
    private final String columnName;
    private final ExportTypeEnum exportClass;
    private final HorizontalAlignment alignment;
    // 列宽：1-5：正常宽度、其他：5
    private final int width;
    private final boolean defaultShow;
    private final int columnIndex;

    ExportColumnEnum(String column, String columnName,
                     ExportTypeEnum exportClass, HorizontalAlignment alignment,
                     int width, boolean defaultShow, int columnIndex) {
        this.column = column;
        this.columnName = columnName;
        this.exportClass = exportClass;
        this.alignment = alignment;
        this.width = width;
        this.defaultShow = defaultShow;
        this.columnIndex = columnIndex;
    }

    public boolean isDefaultShow() {
        return defaultShow;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getColumn() {
        return column;
    }

    public ExportTypeEnum getExportClass() {
        return exportClass;
    }

    public int toWidth() {
        if (this.width == 8) {
            return 8000;
        } else if (this.width <= 0) {
            return 5000;
        } else if (this.width > 5) {
            return 6000;
        } else {
            return width * 1000;
        }
    }

    public HorizontalAlignment toAlignment() {
        return alignment;
    }

    public Integer getColumnIndex() {
        return columnIndex;
    }

}
