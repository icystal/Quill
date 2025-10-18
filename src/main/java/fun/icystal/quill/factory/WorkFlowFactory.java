package fun.icystal.quill.factory;

import fun.icystal.quill.constant.BookStep;
import fun.icystal.quill.obj.response.BookNode;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class WorkFlowFactory {

    public static List<BookNode> WORK_FLOW = Arrays.stream(BookStep.values())
            .sorted((Comparator.comparingInt(BookStep::getSort)))
            .map(o1 -> new BookNode(o1.getSort(), o1.getDesc()))
            .toList();
}
