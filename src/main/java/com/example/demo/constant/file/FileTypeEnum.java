package com.example.demo.constant.file;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author administrator
 * @date 2020/07/16
 * @description: 类描述: 文件类型 若新增文件类型必须更改版本号
 **/
@Getter
@AllArgsConstructor
public enum FileTypeEnum implements FileType {
    /**
     * 文件夹
     */
    DIR,

    /**
     * html
     */
    HTM,
    HTML,

    /**
     * js\css
     */
    JS,
    CSS,

    /**
     * pic
     */
    GIF,
    JPG,
    JPEG,
    PNG,
    BMP,
    ICO,
    SVG,
    RAW,
    WEBP,
    EXIF,
    PSD,
    CRD,
    DXF,
    AI,

    /**
     * font
     */
    WOFF,
    WOFF2,
    TTF,
    OTF,
    EOT,

    /**
     * video
     */
    MPEG,
    DAT,
    MKV,
    RM,
    RMVB,
    VOB,
    SWF,
    ASX,
    ASF,
    MPG,
    WMV,
    _3GP,
    MP4,
    MOV,
    AVI,
    FLV,



    /**
     * audio
     */
    MID,
    MP3,
    AAC,
    FLAC,
    WAV,
    APE,
    ALAC,

    /**
     * package
     */
    RAR,
    ZIP,
    _7Z,

    /**
     * text
     */
    TXT,
    JSON,
    XML,
    INI,
    PROPERTIES,
    LOG,
    MD,

    /**
     * office
     */
    DOC,
    DOCX,
    PPT,
    PPTX,
    XLS,
    XLSX,

    /**
     * pdf
     */
    PDF,


    /**
     * pic补充格式
     */
    EMF;

    /**
     * 获取文件格式
     * 对应{@link UploadFileDO#getType()}的值
     */
    @Override
    public int getIntType() {
        return this.ordinal() + 1;
    }

    /**
     * 获取后缀名
     *
     * @return suffix
     */
    @Override
    public String getSuffix() {
        String result = this.name();
        result = result.replace("_", "");
        return result.toLowerCase();
    }

}
