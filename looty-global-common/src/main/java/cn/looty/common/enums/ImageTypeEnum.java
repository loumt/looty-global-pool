package cn.looty.common.enums;

public enum ImageTypeEnum {
    JPG("jpg", "image/jpeg"),
    TIFF("tiff", "image/tiff"),
    GIF("gif", "image/gif"),
    JFIF("jfif", "image/jpeg"),
    PNG("png", "image/png"),
    TIF("tif", "image/tiff"),
    ICO("ico", "image/x-icon"),
    JPEG("jpeg", "image/jpeg"),
    WBMP("wbmp", "image/vnd.wap.wbmp"),
    FAX("fax", "image/fax"),
    NET("net", "image/pnetvue"),
    JPE("jpe", "image/jpeg"),
    RP("rp", "image/vnd.rn-realpix"),
    BMP("bmp", "image/bmp");

    private final String type;
    private final String contentType;

    ImageTypeEnum(String type, String contentType) {
        this.type = type;
        this.contentType = contentType;
    }

    public String getType() {
        return type;
    }

    public String getContentType() {
        return contentType;
    }
}
