package com.hy.filelibrary.utils.format;

import com.hy.filelibrary.R;

/**
 * @author:MtBaby
 * @date:2020/06/19 16:18
 * @desc:
 */
public enum FormatEnum {
    FOLDER("folder", R.mipmap.icon_directory),//文件夹
    AI("ai", R.mipmap.f_ai,"ai"),//AI
    APK("apk", R.mipmap.f_apk,"apk"),//APK
    VIDEO("video", R.mipmap.f_avi, "flv", "mpg", "mpeg","3gp", "mov", "rmvb", "mkv"), //视频格式
    MP4("mp4", R.mipmap.f_mp4, "mp4"), //视频格式
    COM("com", R.mipmap.f_com,"com"),//COM
    WORD("word", R.mipmap.f_doc, "docx", "dotx", "doc", "dot", "pagers"), //文档格式
    EPS("eps", R.mipmap.f_eps, "eps"),//pdf
    IMG("img", R.mipmap.f_gif, "gif","jpeg", "bmp", "tiff"), //图片格式
    JPG("jpg", R.mipmap.f_jpg, "jpg"),//pdf
    PNG("png", R.mipmap.f_png, "png"),//pdf
    HTML("html", R.mipmap.f_html, "html"), //cad CAD("cad", R.mipmap.file_icon_cad, "dwg","dxf","dwt"),//网页格式
    MP3("mp3", R.mipmap.f_mp3, "mp3", "wav", "wma"),//音频格式
    PDF("pdf", R.mipmap.f_pdf, "pdf"),//pdf
    TXT("txt", R.mipmap.f_txt, "txt"), //文本格式
    SVG("svg", R.mipmap.f_svg, "svg"), //文本格式
    TTF("ttf", R.mipmap.f_ttf, "ttf"), //文本格式
    EXCEL("excel", R.mipmap.f_xls, "xls", "xlsx", "xlt", "xltx"),//电子表格
    PPT("ppt", R.mipmap.f_ppt, "ppt", "pptx"),//ppt
    PS("ps", R.mipmap.f_psd, "psd", "pdd"), //max MAX3D("3DMax", R.mipmap.file_icon_max, "max"), //ps
    ZIP("zip", R.mipmap.f_zip, "zip", "jar", "rar", "7z"), //压缩包
    UNKNOWN("unknown", R.mipmap.f_unknow); //未知格式
    private static final String TAG = "FormatEnum";
    public String TYPE;
    public int ICON;
    public String[] FORMATS;

    /**
     * @param type 文件类型 * @param icon 对应icon * @param formats 包含格式
     */
    FormatEnum(String type, int icon, String... formats) {
        this.TYPE = type;
        this.ICON = icon;
        this.FORMATS = formats;
    }

    /**
     * 通过文件类型获取对应枚举 * * @param extension 文件扩展名 * @return 文件对应的枚举信息，如果没有，返回未知
     */
    public static FormatEnum getFormat(String extension) {
        for (FormatEnum format : FormatEnum.values()) {
            for (String extend : format.FORMATS) {
                if (extend.equalsIgnoreCase(extension)) {
                    return format;
                }
            }
        }
        return UNKNOWN;
    }
}
