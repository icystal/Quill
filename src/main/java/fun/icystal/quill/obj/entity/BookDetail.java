package fun.icystal.quill.obj.entity;

import lombok.Data;

import java.util.List;

@Data
public class BookDetail {

    private BookBrief brief;

    private BookStructure structure;

    private List<Character> characters;

}
