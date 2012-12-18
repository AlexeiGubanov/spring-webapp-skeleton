package org.swas.web.view;

import org.apache.tiles.TilesContainer;
import org.apache.tiles.impl.CannotRenderException;
import org.apache.tiles.servlet.context.ServletUtil;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author Alexei.Gubanov@gmail.com
 *         Date: 23.01.12
 */
public class DynamicTilesView extends AbstractUrlBasedView {


    final DynamicTilesViewProcessor dynamicTilesViewProcessor = new DynamicTilesViewProcessor();


    protected void renderMergedOutputModel(Map model, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ServletContext servletContext = getServletContext();
        TilesContainer container = ServletUtil.getContainer(servletContext);
        if (container == null) {
            throw new ServletException("Tiles container is not initialized. " +
                    "Have you added a TilesConfigurer to your web application context?");
        }

        exposeModelAsRequestAttributes(model, request);

        try {
            dynamicTilesViewProcessor.renderMergedOutputModel(getBeanName(), getUrl(),
                    servletContext, request, response, container);
        } catch (CannotRenderException ex) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    public void setTilesDefinitionName(String tilesDefinitionName) {
        dynamicTilesViewProcessor.setTilesDefinitionName(tilesDefinitionName);
    }

    public void setTilesBodyAttributeName(String tilesBodyAttributeName) {
        dynamicTilesViewProcessor.setTilesBodyAttributeName(tilesBodyAttributeName);
    }

    public void setTilesDefinitionDelimiter(String tilesDefinitionDelimiter) {
        dynamicTilesViewProcessor.setTilesDefinitionDelimiter(tilesDefinitionDelimiter);
    }
}
