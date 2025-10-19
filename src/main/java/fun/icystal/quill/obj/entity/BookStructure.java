package fun.icystal.quill.obj.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookStructure {

    /**
     * 故事背景和人物设定
     */
    private String background;

    /**
     * 故事第一部分
     */
    private String firstPart;

    /**
     * 故事第二部分
     */
    private String secondPart;

    /**
     * 故事第三部分
     */
    private  String thirdPart;

    /**
     * 故事第四部分
     */
    private String fourthPart;

}
