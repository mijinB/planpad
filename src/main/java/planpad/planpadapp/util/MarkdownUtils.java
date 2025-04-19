package planpad.planpadapp.util;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

public class MarkdownUtils {

    private static final Parser parser = Parser.builder().build();
    private static final HtmlRenderer renderer = HtmlRenderer.builder().build();

    public static String toHtml(String markdown) {
        Node document = parser.parse(markdown);
        return renderer.render(document);
    }
}
