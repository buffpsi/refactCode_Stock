package com.ll.tenmindaily.standard.util;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Component;

@Component //스프링부트에 의해 관리되는 빈(bean, 자바객체)으로 등록
//빈(bean)으로 등록된 컴포넌트는 템플릿에서 바로 사용
public class CommonUtil {

        public String markdown(String markdown) {
                Parser parser = Parser.builder().build();
                Node document = parser.parse(markdown);
                HtmlRenderer renderer = HtmlRenderer.builder().build();
                return renderer.render(document);
        }
}
