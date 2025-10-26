package fun.icystal.quill.obj.entity;

import fun.icystal.quill.fundamental.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookAbstract {

    @Comment(desc = "故事的背景和人物设定")
    private String background;

    @Comment(desc = "故事的第一部分内容")
    private String firstSection;

    @Comment(desc = "故事的第二部分内容")
    private String secondSection;

    @Comment(desc = "故事的第三部分内容")
    private String thirdSection;

    @Comment(desc = "故事的第四部分内容")
    private String fourthSection;

    public String contentString() {
        return "* 故事背景和主要人物: " + background + "\n" +
                "* 第一部分: " + firstSection + "\n" +
                "* 第二部分: " + secondSection + "\n" +
                "* 第三部分: " + thirdSection + "\n" +
                "* 第四部分: " + fourthSection + "\n";
    }

}
