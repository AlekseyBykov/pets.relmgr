package dev.abykov.pets.relmgr.core.infra;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

public class TemplateRenderer {

    private final Configuration cfg;

    public TemplateRenderer() {
        cfg = new Configuration(new Version("2.3.31"));
        cfg.setDefaultEncoding("UTF-8");

        cfg.setClassLoaderForTemplateLoading(
                getClass().getClassLoader(),
                "templates"
        );
    }

    public String render(String templateName, Map<String, Object> model) {
        try {
            Template template = cfg.getTemplate(templateName);
            StringWriter out = new StringWriter();
            template.process(model, out);
            return out.toString();

        } catch (IOException | TemplateException e) {
            throw new RuntimeException("Ошибка рендера шаблона: " + templateName, e);
        }
    }
}
