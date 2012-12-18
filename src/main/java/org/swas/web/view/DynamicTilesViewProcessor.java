package org.swas.web.view;

import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.TilesException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.JstlUtils;
import org.springframework.web.servlet.support.RequestContext;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Alexei.Gubanov@gmail.com
 *         Date: 23.01.12
 */
public class DynamicTilesViewProcessor {
    final Logger logger = LoggerFactory.getLogger(DynamicTilesViewProcessor.class);
    private String derivedDefinitionName = null;

    private String tilesDefinitionName = "mainTemplate";
    private String tilesBodyAttributeName = "content";
    private String tilesDefinitionDelimiter = ".";

    public void setTilesDefinitionName(String tilesDefinitionName) {
        this.tilesDefinitionName = tilesDefinitionName;
    }

    public void setTilesBodyAttributeName(String tilesBodyAttributeName) {
        this.tilesBodyAttributeName = tilesBodyAttributeName;
    }

    public void setTilesDefinitionDelimiter(String tilesDefinitionDelimiter) {
        this.tilesDefinitionDelimiter = tilesDefinitionDelimiter;
    }

    protected void renderMergedOutputModel(String beanName, String url,
                                           ServletContext servletContext,
                                           HttpServletRequest request, HttpServletResponse response,
                                           TilesContainer container)
            throws Exception {
        JstlUtils.exposeLocalizationContext(new RequestContext(request, servletContext));
        String definitionName = startDynamicDefinition(beanName, url, request, response, container);
        container.render(definitionName, request, response);
        endDynamicDefinition(definitionName, beanName, request, response, container);
    }

    protected String startDynamicDefinition(String beanName, String url,
                                            HttpServletRequest request, HttpServletResponse response,
                                            TilesContainer container)
            throws TilesException {
        String definitionName = processTilesDefinitionName(beanName, container,
                request, response);
        if (!definitionName.equals(beanName)) {
            Attribute attr = new Attribute();
            attr.setValue(url);

            AttributeContext attributeContext = container.startContext(request, response);
            attributeContext.putAttribute(tilesBodyAttributeName, attr);

            logger.debug("URL used for Tiles body.  url='" + url + "'.");
        }

        return definitionName;
    }

    protected void endDynamicDefinition(String definitionName, String beanName,
                                        HttpServletRequest request, HttpServletResponse response,
                                        TilesContainer container) {
        if (!definitionName.equals(beanName)) {
            container.endContext(request, response);
        }
    }

    protected String processTilesDefinitionName(String beanName,
                                                TilesContainer container,
                                                HttpServletRequest request,
                                                HttpServletResponse response)
            throws TilesException {
        if (derivedDefinitionName != null) {
            return derivedDefinitionName;
        } else if (container.isValidDefinition(beanName, request, response)) {
            derivedDefinitionName = beanName;

            return beanName;
        } else {
            String result;

            StringBuilder sb = new StringBuilder();
            int lastIndex = beanName.lastIndexOf("/");
            boolean rootDefinition = false;

            if (StringUtils.hasLength(tilesDefinitionDelimiter)) {
                sb.append(tilesDefinitionDelimiter);
            }

            // if no '/', then at context root
            if (lastIndex == -1) {
                rootDefinition = true;
            } else {
                String path = (beanName.substring(0, lastIndex));

                //rotate path if it contains one or more /
                if (path.contains("/")) {
                    StringBuilder newPath = new StringBuilder();
                    String[] a = path.split("/");
                    for (int i = 0; i < a.length; i++) {
                        if (newPath.length() > 0)
                            newPath.append("/");
                        newPath.append(a[a.length - i - 1]);
                    }
                    path = newPath.toString();
                }

                if (StringUtils.hasLength(tilesDefinitionDelimiter)) {
                    path = StringUtils.replace(path, "/", tilesDefinitionDelimiter);

                }


                sb.append(path);

                if (StringUtils.hasLength(tilesDefinitionDelimiter)) {
                    sb.append(tilesDefinitionDelimiter);
                }
            }

            sb.append(tilesDefinitionName);

            result = sb.toString();
            if (!container.isValidDefinition(result, request, response)) {
                boolean found = false;
                while (result.length() > tilesDefinitionName.length() + tilesDefinitionDelimiter.length()) {
                    result = result.substring(result.indexOf(tilesDefinitionDelimiter, tilesDefinitionDelimiter.length()));
                    if (container.isValidDefinition(result, request, response)) {
                        found = true;
                        break;
                    }
                }
                if (!found && !rootDefinition) {
                    String root = null;

                    if (StringUtils.hasLength(tilesDefinitionDelimiter)) {
                        root = tilesDefinitionDelimiter;
                    }

                    root += tilesDefinitionName;

                    if (container.isValidDefinition(root, request, response)) {
                        result = root;
                    } else {
                        throw new TilesException("No defintion of found for " +
                                "'" + root + "'" +
                                " or '" + sb.toString() + "'");
                    }

                }
            }

            derivedDefinitionName = result;

            return result;
        }
    }


}
