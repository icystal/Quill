package fun.icystal.quill.obj.entity;

import lombok.Data;

@Data
public class BookBrief {

    /**
     * 小说类型
     */
    private String genre;

    /**
     * 一句话概要
     */
    private String brief;

}
