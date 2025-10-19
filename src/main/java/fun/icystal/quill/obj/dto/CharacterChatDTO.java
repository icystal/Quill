package fun.icystal.quill.obj.dto;

import fun.icystal.quill.fundamental.Comment;
import fun.icystal.quill.obj.entity.Character;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CharacterChatDTO {

    @Comment(desc = "主要角色的简介, 是一个列表")
    private List<Character> characters;

}
