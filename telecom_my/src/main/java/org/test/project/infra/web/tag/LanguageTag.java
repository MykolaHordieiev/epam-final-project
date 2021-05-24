package org.test.project.infra.web.tag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageTag extends TagSupport {

    private static Logger log = LogManager.getLogger(LanguageTag.class);

    private String message;
    private Utf8Control utf8Control = new Utf8Control();
    private List<Locale> locales = new ArrayList<>();

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int doStartTag() {
        HttpSession session = pageContext.getSession();
        if (session.getAttribute("Locale") == null) {
            Locale en = new Locale("en");
            Locale ru = new Locale("ru");
            locales.add(ru);
            locales.add(en);
            session.setAttribute("Locale", en);
            session.setAttribute("locales", locales);
        }
        Locale locale = (Locale) session.getAttribute("Locale");
        if (message != null && !message.isEmpty()) {
            ResourceBundle messages = ResourceBundle.getBundle("i18n.resources", locale, utf8Control);
            String locMessage = messages.getString(message);
            try {
                pageContext.getOut().print(locMessage);
            } catch (IOException ex) {
                log.error(ex.getMessage());
            }
        }
        return SKIP_BODY;
    }
}
