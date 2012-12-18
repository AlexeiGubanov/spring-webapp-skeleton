package org.swas.service.impl;

import org.swas.service.TmplMerger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import java.util.Locale;
import java.util.Map;

/**
 * @author Alexei.Gubanov@gmail.com
 *         Date: 02.02.12
 */
@Service
public class VelocityTmplMerger implements TmplMerger {

    @Autowired
    private VelocityEngine velocityEngine;

    @Autowired
    @Qualifier("velocityTemplatesConfig")
    private MessageSource config;

    public String mergeTemplate(String tmplName, Map model, Locale locale) {
        String templatePath = config.getMessage(tmplName, new Object[]{}, locale);
        return VelocityEngineUtils.mergeTemplateIntoString(
                velocityEngine, templatePath, "UTF-8", model);


    }


}
